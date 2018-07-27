package com.websarva.wings.android.getgaitame;

import android.os.Handler;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
    ArrayList<Map<String, String>> listData = new ArrayList<Map<String, String>>();
    ArrayList<Map<String, String>> listDataClone;
    ListView listView;
    SimpleAdapter adapter;
    Timer mTimer = new Timer();
    TimerTask mTimerTask = new MainTimerTask();
    Handler mHandler = new Handler();
    boolean menu_flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_main);
        listView=(ListView) findViewById(R.id.lvCityList);
        ListView listView=(ListView) findViewById(R.id.lvCityList);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHideOnContentScrollEnabled(true);
            actionBar.setShowHideAnimationEnabled(true);
        }
        try{
            mTimer.schedule(mTimerTask, 0, 999);

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

    /*
    //リストが選択されたときの処理が記述されたメンバクラス。
    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        }
    }
    */
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
            System.out.print(result);
            Log.i("memory",result);
            return result;
        }

        @Override
        public synchronized void onPostExecute(String result){
            try {
                listView=(ListView) findViewById(R.id.lvCityList);
                int position=0;int y=0;
                if(adapter!=null){
                    //nullなら初期化するがその前にスクロール位置を記憶する。
                    position = listView.getFirstVisiblePosition();
                    y = listView.getChildAt(0).getTop();
                    listData.clear();
                    adapter.notifyDataSetChanged();
                }
                JSONObject rootJSON = new JSONObject(result);
                Log.d("JSONObject", rootJSON.toString());
                Log.d("JSONObject", rootJSON.getJSONArray("quotes").toString());

                for(int i =0;i<24;i++) {
                    //Log.d("JSONObject", rootJSON.getJSONArray("quotes").getJSONObject(i).toString());
                    String currencyPairCode = rootJSON.getJSONArray("quotes").getJSONObject(i).getString("currencyPairCode");
                    String open = rootJSON.getJSONArray("quotes").getJSONObject(i).getString("open");
                    String high = rootJSON.getJSONArray("quotes").getJSONObject(i).getString("high");
                    String low = rootJSON.getJSONArray("quotes").getJSONObject(i).getString("low");
                    String bid = rootJSON.getJSONArray("quotes").getJSONObject(i).getString("bid");
                    String ask = rootJSON.getJSONArray("quotes").getJSONObject(i).getString("ask");

                    Map<String, String> city = new HashMap<>();
                    city.put("currencyPairCode", currencyPairCode);
                    city.put("open", "Open: " + open);
                    city.put("high", "High: " + high);
                    city.put("low", "Low: " + low);
                    city.put("bid", "Bid: " + bid);
                    city.put("ask", "Ask: " + ask);
                    listData.add(city);
                }
                /*
                if(listDataClone!=null){
                    for (Map<String, String> listDataMap : listData) { }
                    for (Map<String, String> listDatacloneMap : listDataClone) {}
                }*/
                String[] from = {"currencyPairCode", "bid", "ask", "open", "high", "low"};
                int[] to = {R.id.currencyPairCode, R.id.bid, R.id.ask, R.id.open, R.id.high, R.id.low};
                adapter = new SimpleAdapter(MainActivity.this, listData, R.layout.gaitame_list, from, to);
                //ListViewにSimpleAdapterを設定。
                listView.setAdapter(adapter);
                listView.setSelectionFromTop(position, y);
                listDataClone =(ArrayList<Map<String, String>>)listData.clone();//配列データの複製

            }catch(JSONException ex) {
                System.out.print(ex);
            }catch(Exception ex) {
                System.out.print(ex);
            }
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
