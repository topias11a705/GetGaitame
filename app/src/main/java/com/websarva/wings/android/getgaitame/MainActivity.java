package com.websarva.wings.android.getgaitame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    //ArrayList<Map<String, String>> listData = new ArrayList<Map<String, String>>();
    ArrayList<gaitameDataBox> listData = new ArrayList<>();
    ArrayList<Map<String, String>> listDataClone;
    ListView listView;
    SimpleAdapter adapter;
    ImageView imageView;
    Timer mTimer = new Timer();
    TimerTask mTimerTask = new MainTimerTask();
    Handler mHandler = new Handler();
    boolean menu_flag = true;
    Context contex1;
    Context contex2;
    int position = 0;int y = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.lvCityList);
        ListView listView = (ListView)findViewById(R.id.lvCityList);
        imageView = findViewById(R.id.currency_image_view);
        contex1 = MainActivity.this;
        contex2 = getApplicationContext();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) { actionBar.setHideOnContentScrollEnabled(true); }
        try{
            mTimer.schedule(mTimerTask, 0, 3000);
        }catch(Exception e){
            System.out.print(e);
        }finally {
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
    }
    public class MainTimerTask extends TimerTask { //➀
        @Override
        public void run() { //②
            mHandler.post(new Runnable() {  //④
                @Override
                public void run() {
                    WeatherInfoReceiver receiver = new WeatherInfoReceiver();
                    receiver.execute("");
                }
            });
        }
    }

    /*//リストが選択されたときの処理が記述されたメンバクラス。
    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}
    }*/
    private class WeatherInfoReceiver extends AsyncTask<String, String, String> {
        public WeatherInfoReceiver() {}

        @Override
        public synchronized String doInBackground(String... params) {
            String urlStr = "https://www.gaitameonline.com/rateaj/getrate";
            String result = "";
            HttpURLConnection con = null;
            try {
                URL url = new URL(urlStr);
                con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                result = is2String(con.getInputStream());
            }catch(MalformedURLException ex) {
                System.out.print(ex);
            }catch(IOException ex) {
                System.out.print(ex);
            }finally {
                if(con != null) {con.disconnect();}
            }
            return result;
        }

        @Override
        public synchronized void onPostExecute(String result){
            try{
                listView = (ListView)findViewById(R.id.lvCityList);

                if (adapter != null) {
                    //nullなら初期化するがその前にスクロール位置を記憶する。
                    position = listView.getFirstVisiblePosition();
                    y = listView.getChildAt(0).getTop();
                    listData.clear();
                    adapter.notifyDataSetChanged();

                }else{
                    Log.d("JSONObject", "AAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                }
                JSONObject rootJSON = new JSONObject(result);
                Log.d("JSONObject", rootJSON.toString());
                //Log.d("JSONObject", rootJSON.getJSONArray("quotes").toString());
                for(int i=0;i<24;i++){
                    if(i==0){listData.clear();}
                    //Log .d("JSONObject", rootJSON.getJSONArray("quotes").getJSONObject(i).toString());
                    String currencyPairCode = rootJSON.getJSONArray("quotes").getJSONObject(i).getString("currencyPairCode");
                    String open = rootJSON.getJSONArray("quotes").getJSONObject(i).getString("open");
                    String high = rootJSON.getJSONArray("quotes").getJSONObject(i).getString("high");
                    String low = rootJSON.getJSONArray("quotes").getJSONObject(i).getString("low");
                    String bid = rootJSON.getJSONArray("quotes").getJSONObject(i).getString("bid");
                    String ask = rootJSON.getJSONArray("quotes").getJSONObject(i).getString("ask");
                    Log.d("JSONObject",currencyPairCode+" "+ open+" "+high+" "+low+" "+bid+" "+ask);
                    gaitameDataBox gaitame = new gaitameDataBox();
                    gaitame.setCurrencyPairCode(currencyPairCode);
                    gaitame.setOpen("Open: " + open);
                    gaitame.setHigh("High: " + high);
                    gaitame.setLow("Low: " + low);
                    gaitame.setBid("Bid: " + bid);
                    gaitame.setAsk("Ask: " + ask);
                    gaitame.setImage(get_image_res(currencyPairCode));
                    listData.add(gaitame);
                }

                //*******************************************************************************
                originalListAdapetr adapter
                        = new originalListAdapetr(contex1,R.layout.gaitame_list,listData);
                //int padding = (int)(getResources().getDisplayMetrics().density * 8);
                //listView.setPadding(padding, 0, padding, 0);
                listView.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_OVERLAY);
                //listView.setDivider(null);//境界線をなくす

                LayoutInflater inflater = LayoutInflater.from(contex1);
                //View header = inflater.inflate(R.layout.list_header_footer, listView, false);
                //View footer = inflater.inflate(R.layout.list_header_footer, listView, false);
                //listView.addHeaderView(header, null, false);
                //listView.addFooterView(footer, null, false);
                listView.setSelectionFromTop(position, y);
                listView.setAdapter(adapter);

 
                //*******************************************************************************
            }catch(JSONException ex) {System.out.print(ex);
            }catch(Exception ex) {System.out.print(ex);}
        }

        private String is2String(InputStream is) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            char[] b = new char[1024];
            int line;
            while(0 <= (line = reader.read(b))) {
                sb.append(b, 0, line);
            }
            return sb.toString();
        }
    }
    public Drawable get_image_res(String s){
        Drawable drawable=null;
        switch (s){
            case "GBPNZD":drawable=getResources().getDrawable(R.drawable.gbpnzd);break;
            case "CADJPY":drawable=getResources().getDrawable(R.drawable.cadjpy);break;
            case "GBPAUD":drawable=getResources().getDrawable(R.drawable.gbpaud);break;
            case "AUDJPY":drawable=getResources().getDrawable(R.drawable.audjpy);break;
            case "AUDNZD":drawable=getResources().getDrawable(R.drawable.audnzd);break;
            case "EURCAD":drawable=getResources().getDrawable(R.drawable.eurcad);break;
            case "EURUSD":drawable=getResources().getDrawable(R.drawable.eurusd);break;
            case "NZDJPY":drawable=getResources().getDrawable(R.drawable.nzdjpy);break;
            case "USDCAD":drawable=getResources().getDrawable(R.drawable.usdcad);break;
            case "EURGBP":drawable=getResources().getDrawable(R.drawable.eurgbp);break;
            case "GBPUSD":drawable=getResources().getDrawable(R.drawable.gbpusd);break;
            case "ZARJPY":drawable=getResources().getDrawable(R.drawable.zarjpy);break;
            case "EURCHF":drawable=getResources().getDrawable(R.drawable.eurchf);break;
            case "CHFJPY":drawable=getResources().getDrawable(R.drawable.chfjpy);break;
            case "AUDUSD":drawable=getResources().getDrawable(R.drawable.audusd);break;
            case "USDCHF":drawable=getResources().getDrawable(R.drawable.usdchf);break;
            case "EURJPY":drawable=getResources().getDrawable(R.drawable.eurjpy);break;
            case "GBPCHF":drawable=getResources().getDrawable(R.drawable.gbpchf);break;
            case "EURNZD":drawable=getResources().getDrawable(R.drawable.eurnzd);break;
            case "NZDUSD":drawable=getResources().getDrawable(R.drawable.nzdusd);break;
            case "USDJPY":drawable=getResources().getDrawable(R.drawable.usdjpy);break;
            case "EURAUD":drawable=getResources().getDrawable(R.drawable.euraud);break;
            case "AUDCHF":drawable=getResources().getDrawable(R.drawable.audchf);break;
            case "GBPJPY":drawable=getResources().getDrawable(R.drawable.gbpjpy);break;
        }
        return drawable;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.onoffButton:
                if(mTimer==null){
                    menu_flag = false;
                    mTimer = new Timer();
                    mTimerTask = new MainTimerTask();
                    mHandler = new Handler();
                    mTimer.schedule(mTimerTask, 0, 999);
                    Toast.makeText(this, "自動更新開始", Toast.LENGTH_SHORT).show();
                }else{
                    menu_flag = true;
                    mTimer.cancel();
                    mTimer = null;
                    Toast.makeText(this, "自動更新停止", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }
}
