package com.websarva.wings.android.getgaitame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    ArrayList<gaitameDataBox> listData = new ArrayList<>();
    ListView listView;
    RecyclerView recyclerView;
    ImageView imageView;
    Timer mTimer = new Timer();
    TimerTask mTimerTask = new MainTimerTask();
    Handler mHandler = new Handler();
    boolean menu_flag = true;
    Context contex1;Context contex2;
    static int position = 0;static int y = 0;
    static originalListAdapetr adapter =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_main);
        //listView = (ListView)findViewById(R.id.lvCityList);
        //listView.setFastScrollEnabled(true);
        imageView = findViewById(R.id.currency_image_view);
        contex1 = MainActivity.this; contex2 = getApplicationContext();

        /*listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i){
            }
            @Override
            public void onScroll(AbsListView absListView,int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });*/
        //ActionBar actionBar = getSupportActionBar();
        //if (actionBar != null) { actionBar.setHideOnContentScrollEnabled(true); }
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        //toolbar.setTitle(R.string.toolbar_title_on);
        //setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolbarLayout);
        toolbarLayout.setTitle(getString(R.string.toolbar_title_on));
        toolbarLayout.setExpandedTitleColor(Color.WHITE);
        toolbarLayout.setCollapsedTitleTextColor(Color.LTGRAY);

        RecyclerView lvMenu = findViewById(R.id.lvCityList);

        //RecyclerViewにレイアウトマネージャーとしてLinearLayoutManagerを設定。
        lvMenu.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        //以下は他の2種のレイアウトマネージャー。
//		new GridLayoutManager(ScrollListActivity.this, 5);
//		new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL);
        toolbar.inflateMenu(R.menu.menu);
        try{
            mTimer.schedule(mTimerTask, 0, 999);
        }catch(Exception e){
            System.out.print(e);
        }finally {
        }
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.onoffButton:
                        if(mTimer==null){
                            ((Toolbar)findViewById(R.id.tool_bar)).setTitle(R.string.toolbar_title_on);
                            menu_flag = false;
                            mTimer = new Timer();
                            mTimerTask = new MainTimerTask();
                            mHandler = new Handler();
                            mTimer.schedule(mTimerTask, 0, 999);
                            Toast.makeText(contex1, "自動更新開始", Toast.LENGTH_SHORT).show();
                        }else{
                            ((Toolbar)findViewById(R.id.tool_bar)).setTitle(R.string.toolbar_title_off);
                            menu_flag = true;
                            mTimer.cancel();
                            mTimer = null;
                            Toast.makeText(contex1, "自動更新停止", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                return true;
            }
        });


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
                if (adapter != null) {
                    //nullなら初期化するがその前にスクロール位置を記憶する。
                    position = listView.getFirstVisiblePosition();
                    y = listView.getChildAt(0).getTop();
                    listData.clear();
                }
                JSONObject rootJSON = new JSONObject(result);
                Log.d("JSONObject", rootJSON.toString());//Log.d("JSONObject", rootJSON.getJSONArray("quotes").toString());
                for(int i=0;i<24;i++){
                    if(i==0){listData.clear();}
                    //Log .d("JSONObject", rootJSON.getJSONArray("quotes").getJSONObject(i).toString());
                    String currencyPairCode = rootJSON.getJSONArray("quotes").getJSONObject(i).getString("currencyPairCode");
                    String open = rootJSON.getJSONArray("quotes").getJSONObject(i).getString("open");
                    String high = rootJSON.getJSONArray("quotes").getJSONObject(i).getString("high");
                    String low = rootJSON.getJSONArray("quotes").getJSONObject(i).getString("low");
                    String bid = rootJSON.getJSONArray("quotes").getJSONObject(i).getString("bid");
                    String ask = rootJSON.getJSONArray("quotes").getJSONObject(i).getString("ask");
                    //Log.d("JSONObject",currencyPairCode+" "+ open+" "+high+" "+low+" "+bid+" "+ask);
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
                if(adapter==null){
                    adapter= new originalListAdapetr(contex1,R.layout.gaitame_list,listData);
                }else{
                    adapter.ArrayDate=listData;
                }

                RecyclerListAdapter adapter = new RecyclerListAdapter(listData);
                recyclerView.setAdapter(adapter);

                //listView.setAdapter(adapter);
                //adapter.notifyDataSetChanged();
                //listView.setSelectionFromTop(position, y);
                //*******************************************************************************
            }catch(JSONException ex) {System.out.print(ex);
            }catch(Exception ex) {System.out.print(ex);}
        }
        //***************************************************************************************************
        //***************************************************************************************************
        private class RecyclerListViewHolder extends RecyclerView.ViewHolder {
            TextView currencyPairCodetextView_textView;
            TextView bid_textView;
            TextView ask_textView;
            TextView open_textView;
            TextView high_textView;
            TextView low_textView;
            ImageView currency_image_view;

            public RecyclerListViewHolder(View itemView) {
                //親クラスのコンストラクタの呼び出し。
                super(itemView);
                currencyPairCodetextView_textView = (TextView) itemView.findViewById(R.id.currencyPairCode);
                bid_textView = (TextView) itemView.findViewById(R.id.bid);
                ask_textView = (TextView) itemView.findViewById(R.id.ask);
                open_textView = (TextView) itemView.findViewById(R.id.open);
                high_textView = (TextView) itemView.findViewById(R.id.high);
                low_textView = (TextView) itemView.findViewById(R.id.low);
                currency_image_view = (ImageView) itemView.findViewById(R.id.currency_image_view);
            }
        }
        private class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListViewHolder> {
            private ArrayList<gaitameDataBox> _listData;

            public RecyclerListAdapter(ArrayList<gaitameDataBox> listData) {
                //引数のリストデータをフィールドに格納。
                _listData = listData;
            }

            @Override
            public RecyclerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                return new RecyclerListViewHolder(inflater.inflate(R.layout.gaitame_list, parent, false));
            }

            @Override
            public void onBindViewHolder(RecyclerListViewHolder holder, int i) {
                gaitameDataBox item = _listData.get(i);
                //リストデータから該当1行分のデータを取得。
                if (_listData != null && _listData.size() > i && _listData.get(i) != null) {
                    holder.currencyPairCodetextView_textView.setText(_listData.get(i).getCurrencyPairCode());
                    holder.bid_textView.setText(_listData.get(i).getBid());
                    holder.ask_textView.setText(_listData.get(i).getAsk());
                    holder.open_textView.setText(_listData.get(i).getOpen());
                    holder.high_textView.setText(_listData.get(i).getHigh());
                    holder.low_textView.setText(_listData.get(i).getLow());
                    holder.currency_image_view.setImageDrawable(_listData.get(i).getImage());
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(contex1, "クリック", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public int getItemCount() {
                //リストデータ中の件数をリターン。
                if(_listData != null){return _listData.size();}
                else{return 0;}
            }
        }
        //***************************************************************************************************
        //***************************************************************************************************
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
    private class ItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

        }
    }
}
