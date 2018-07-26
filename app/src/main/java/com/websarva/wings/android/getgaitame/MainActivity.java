package com.websarva.wings.android.getgaitame;

import android.os.Handler;
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


public class MainActivity extends AppCompatActivity {
    List<Map<String, String>> listData = new ArrayList<Map<String, String>>();
    ListView listView;
    SimpleAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("memory","Puzzle onStart");
        listView=(ListView) findViewById(R.id.lvCityList);
        setContentView(R.layout.activity_main);
        ListView listView=(ListView) findViewById(R.id.lvCityList);
        try{
            WeatherInfoReceiver receiver = new WeatherInfoReceiver();
            receiver.execute("");

        }catch(Exception e){

        }finally {

        }


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("memory", "Puzzle onResume");
        //try{Thread.sleep(1000);showloop();}catch (Exception e){}
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
        public WeatherInfoReceiver() {
        }

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
            }catch(IOException ex) {
            }finally {
                if(con != null) {con.disconnect();}
            }
            //System.out.print(result);
            return result;
        }

        @Override
        public synchronized void onPostExecute(String result){
            try {
                listView=(ListView) findViewById(R.id.lvCityList);
                //while(true){
                if(adapter!=null){

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

                    //都市データを格納するMapオブジェクトの用意とcityListへのデータ登録。
                    Map<String, String> city = new HashMap<>();
                    city.put("currencyPairCode", currencyPairCode);
                    city.put("open", "Open: " + open);
                    city.put("high", "High: " + high);
                    city.put("low", "Low: " + low);
                    city.put("bid", "Bid: " + bid);
                    city.put("ask", "Ask: " + ask);
                    listData.add(city);
                }
                String[] from = {"currencyPairCode", "bid", "ask", "open", "high", "low"};
                int[] to = {R.id.currencyPairCode, R.id.bid, R.id.ask, R.id.open, R.id.high, R.id.low};
                adapter = new SimpleAdapter(MainActivity.this, listData, R.layout.gaitame_list, from, to);
                //ListViewにSimpleAdapterを設定。
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Thread.sleep(1000);
                //listData.clear();
                //adapter.notifyDataSetChanged();
                //}
            }catch(JSONException ex) {
            }catch(Exception ex) {

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
            case R.id.javase8:
                listData.clear();
                adapter.notifyDataSetChanged();
                try{
                    WeatherInfoReceiver receiver = new WeatherInfoReceiver();
                    receiver.execute("");

                }catch(Exception e){

                }finally {

                }
                Log.d("menu", "menu1 Selected.");
                break;

        }
        return true;
    }
}
