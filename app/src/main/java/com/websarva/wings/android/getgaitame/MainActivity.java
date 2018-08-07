package com.websarva.wings.android.getgaitame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity{
    ArrayList<gaitameDataBox> listData = new ArrayList<>();
    //CopyOnWriteArrayList<gaitameDataBox> listData_clone = new CopyOnWriteArrayList<>();
    static RecyclerView recycleview= null; ImageView imageView;LinearLayoutManager mLinearLayoutManager;

    Timer mTimer = new Timer();
    TimerTask mTimerTask = new MainTimerTask();
    Handler mHandler = new Handler();
    static boolean menu_flag = true;
    static boolean recycleHelper_flag = true;
    static boolean excute_flag = true;
    Context contex1;Context contex2;
    int position = 0; int y = 0;
    RecyclerListAdapter adapter=null;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.currency_image_view);
        contex1 = MainActivity.this; contex2 = getApplicationContext();
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle(R.string.toolbar_title_on);
        //setSupportActionBar(toolbar);
        //CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolbarLayout);
        recycleview = findViewById(R.id.lvCityList);
        mLinearLayoutManager= new LinearLayoutManager(MainActivity.this);
        recycleview.setLayoutManager(mLinearLayoutManager);
        recycleview.addItemDecoration(new DividerItemDecoration(MainActivity.this, mLinearLayoutManager.getOrientation()));
        recycleview.addOnItemTouchListener(new RecyclerItemClickListener(contex2, new RecyclerItemClickListener.OnItemClickListener() {
                @Override  public void onItemClick(View view, int position) {Log.d("MOTIONLitener", "OnItemClickListener onItemClick");}
            }));
        recycleview.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                Log.d("MOTIONLitener", "onInterceptTouchEvent");
                return false;}
            @Override public void onTouchEvent(RecyclerView rv, MotionEvent e) {Log.d("MOTIONLitener", "onTouchEvent");
                ((MyRecyclerView)recycleview).onTouchEvent(e);
            }
            @Override public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                Log.d("MOTIONLitener", "onRequestDisallowInterceptTouchEvent");}
        });

        toolbar.inflateMenu(R.menu.menu);
        try{mTimer.schedule(mTimerTask, 0, 1000);}
        catch(Exception e){System.out.print(e);}
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.onoffButton:
                        excute_flag=!excute_flag;
                        if(excute_flag){Toast.makeText(contex1, "自動更新開始", Toast.LENGTH_SHORT).show();}
                        else{           Toast.makeText(contex1, "自動更新停止", Toast.LENGTH_SHORT).show();}
                        break;
                }
                return true;
            }
        });
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
    //***************************************************************************************************
    private class RecyclerListViewHolder extends RecyclerView.ViewHolder {
        TextView currencyPairCodetextView_textView, bid_textView, ask_textView, open_textView, high_textView, low_textView;
        ImageView currency_image_view, bit_yajirusi_view, ask_yajirusi_view, open_yajirusi_view, high_yajirusi_view, low_yajirusi_view;
        LinearLayout parentLinearLayout; RecyclerView recyclerView_;

        public RecyclerListViewHolder(View itemView) {
            super(itemView);
            recyclerView_ = itemView.findViewById(R.id.lvCityList);
            parentLinearLayout = itemView.findViewById(R.id.parentLinearLayout);
            currencyPairCodetextView_textView = (TextView)itemView.findViewById(R.id.currencyPairCode);
            bid_textView = (TextView)itemView.findViewById(R.id.bid);
            ask_textView = (TextView)itemView.findViewById(R.id.ask);
            open_textView = (TextView)itemView.findViewById(R.id.open);
            high_textView = (TextView)itemView.findViewById(R.id.high);
            low_textView = (TextView)itemView.findViewById(R.id.low);
            currency_image_view = (ImageView)itemView.findViewById(R.id.currency_image_view);
            bit_yajirusi_view = (ImageView)itemView.findViewById(R.id.bit_yajirusi_view);
            ask_yajirusi_view = (ImageView)itemView.findViewById(R.id.ask_yajirusi_view);
            open_yajirusi_view = (ImageView)itemView.findViewById(R.id.open_yajirusi_view);
            high_yajirusi_view = (ImageView)itemView.findViewById(R.id.high_yajirusi_view);
            low_yajirusi_view = (ImageView)itemView.findViewById(R.id.low_yajirusi_view);
        }
    }
    private class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListViewHolder> {
        ArrayList<gaitameDataBox> _listData;
        public RecyclerListAdapter(ArrayList<gaitameDataBox> listData) {
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
            if (_listData != null && _listData.size() > i && item != null) {
                holder.parentLinearLayout.setBackgroundColor(item.getBackGroundColor());
                holder.currencyPairCodetextView_textView.setText(item.getCurrencyPairCode());
                holder.bid_textView.setText(item.getBid());
                holder.ask_textView.setText(item.getAsk());
                holder.open_textView.setText(item.getOpen());
                holder.high_textView.setText(item.getHigh());
                holder.low_textView.setText(item.getLow());
                holder.currency_image_view.setImageDrawable(item.getFlag_image());
                holder.bit_yajirusi_view.setImageDrawable(item.getBit_yajirushi_image());
                holder.ask_yajirusi_view.setImageDrawable(item.getAsk_yajirushi_image());
                holder.open_yajirusi_view.setImageDrawable(item.getOpen_yajirushi_image());
                holder.high_yajirusi_view.setImageDrawable(item.getHigh_yajirushi_image());
                holder.low_yajirusi_view.setImageDrawable(item.getLow_yajirushi_image());
            }
        }

        @Override
        public int getItemCount() {
            //リストデータ中の件数をリターン。
            if(_listData != null){return _listData.size();}
            else{return 0;}
        }
    }
    //***************************************************************************************************
    private class WeatherInfoReceiver extends AsyncTask<String, String, String> {
        public WeatherInfoReceiver() {}

        @Override
        public synchronized String doInBackground(String... params) {
            String urlStr = "https://www.gaitameonline.com/rateaj/getrate";
            String result = "";
            if(excute_flag){
                HttpURLConnection con = null;
                try {
                    URL url = new URL(urlStr);
                    con = (HttpURLConnection)url.openConnection();
                    con.setRequestMethod("GET");
                    con.connect();
                    result = is2String(con.getInputStream());

            //*****************************************************************************************
                    ArrayList<gaitameDataBox> ArraysCopied = new ArrayList<gaitameDataBox>(listData.size());
                    if (adapter != null) {
                        position = mLinearLayoutManager.findFirstVisibleItemPosition();
                        View v = recycleview.getChildAt(0);
                        y = (v == null) ? 0 : (v.getTop() - v.getPaddingTop());
                        //listData_clone.clear();ArraysCopied.clear();
                        for (gaitameDataBox comB : listData) {
                            gaitameDataBox clone = comB.clone();
                            ArraysCopied.add(new gaitameDataBox(
                                    clone.getCurrencyPairCode(), clone.getOpen(), clone.getHigh(), clone.getLow(), clone.getBid(), clone.getAsk()
                            ));
                        }
                        listData = null;
                    }
                    listData = jsonToListData(result);
                    for (int i = 0; i < listData.size(); i++) {
                        if (!ArraysCopied.isEmpty()){
                            //Log .d("JSONObject", String.valueOf(ArraysCopied.get(i).getBid().equals(listData.get(i).getBid())));
                            gaitameDataBox newdata = listData.get(i); gaitameDataBox olddata = ArraysCopied.get(i);
                            Double oldBit,newBit,oldAsk,newAsk,oldOpen,newOpen,oldHigh,newHigh,oldLow,newLow;
                            oldBit  = Double.valueOf(olddata.getBid());
                            newBit  = Double.valueOf(newdata.getBid());
                            oldAsk  = Double.valueOf(olddata.getAsk());
                            newAsk  = Double.valueOf(newdata.getAsk());
                            oldOpen = Double.valueOf(olddata.getOpen());
                            newOpen = Double.valueOf(newdata.getOpen());
                            oldHigh = Double.valueOf(olddata.getHigh());
                            newHigh = Double.valueOf(newdata.getHigh());
                            oldLow  = Double.valueOf(olddata.getLow());
                            newLow  = Double.valueOf(newdata.getLow());
                            Drawable uemuki = getResources().getDrawable(R.drawable.uemuki);
                            Drawable sitamuki = getResources().getDrawable(R.drawable.sitamuki);
                            if     (oldBit < newBit){newdata.setBit_yajirushi_image(uemuki);}
                            else if(oldBit > newBit){newdata.setBit_yajirushi_image(sitamuki);}
                            if     (oldAsk < newAsk){newdata.setAsk_yajirushi_image(uemuki);}
                            else if(oldAsk > newAsk){newdata.setAsk_yajirushi_image(sitamuki);}
                            if     (oldOpen<newOpen){newdata.setOpen_yajirushi_image(uemuki);}
                            else if(oldOpen>newOpen){newdata.setOpen_yajirushi_image(sitamuki);}
                            if     (oldHigh<newHigh){newdata.setHigh_yajirushi_image(uemuki);}
                            else if(oldHigh>newHigh){newdata.setHigh_yajirushi_image(sitamuki);}
                            if     (oldLow < newLow){newdata.setLow_yajirushi_image(uemuki);}
                            else if(oldLow > newLow){newdata.setLow_yajirushi_image(sitamuki);}

                            if(oldBit==newBit && oldAsk==newAsk ){
                                newdata.setBackGroundColor(android.R.color.white);
                                newdata.set_yajirushi_image_is_AllNull();
                            }
                        }
                    }

                }catch(Exception ex) {
                    ex.printStackTrace();
                }finally {
                    if(con != null) {con.disconnect();}
                }
            }
            return result;
        }

        @Override
        public synchronized void onPostExecute(String result){
            if(excute_flag) {
                try {
                    if(adapter == null){adapter = new RecyclerListAdapter(listData);}
                    else {adapter._listData = listData;}
                    recycleview.setAdapter(adapter);
                    mLinearLayoutManager.scrollToPositionWithOffset(position, y);

                    if (recycleHelper_flag) {
                        ItemTouchHelper mIth = new ItemTouchHelper(
                            new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
                                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                                    final int fromPos = viewHolder.getAdapterPosition();
                                    final int toPos = target.getAdapterPosition();
                                    adapter.notifyItemMoved(fromPos, toPos);
                                    Log.d("ItemTouchHelperLitener", "onMove1");
                                    return false;// true if moved, false otherwise
                                }
                                public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
                                    Log.d("ItemTouchHelperLitener", "onMoved");
                                }
                                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                                    final int fromPos = viewHolder.getAdapterPosition();
                                    listData.remove(fromPos);
                                    adapter.notifyItemRemoved(fromPos);
                                    Log.d("ItemTouchHelperLitener", "onSwiped");
                                }
                                @Override public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                                    super.onSelectedChanged(viewHolder, actionState);
                                    if (actionState == ItemTouchHelper.ACTION_STATE_DRAG)
                                        viewHolder.itemView.setVisibility(View.VISIBLE);
                                    Log.d("ItemTouchHelperLitener", "onSelectedChanged");

                                }
                                @Override public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                                    super.clearView(recyclerView, viewHolder);
                                    viewHolder.itemView.setVisibility(View.GONE);
                                    Log.d("ItemTouchHelperLitener", "clearView");
                                }
                            });
                        mIth.attachToRecyclerView(recycleview);
                        recycleHelper_flag = false;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

    }
    private synchronized ArrayList<gaitameDataBox>  jsonToListData(String result) throws JSONException{
        ArrayList<gaitameDataBox> listdata = new ArrayList<>();
        JSONObject rootJSON = new JSONObject(result);
        //Log.d("JSONObject", rootJSON.toString());//Log.d("JSONObject", rootJSON.getJSONArray("quotes").toString());
        for(int i=0;i<24;i++) {
            //Log .d("JSONObject", rootJSON.getJSONArray("quotes").getJSONObject(i).toString());
            JSONObject jsn = rootJSON.getJSONArray("quotes").getJSONObject(i);
            String currencyPairCode = jsn.getString("currencyPairCode");
            String open = jsn.getString("open");
            String high = jsn.getString("high");
            String low = jsn.getString("low");
            String bid = jsn.getString("bid");
            String ask = jsn.getString("ask");
            gaitameDataBox gaitame =new gaitameDataBox(currencyPairCode, open, high, low, bid, ask);
            gaitame.setFlag_image(get_image_res(currencyPairCode));
            listdata.add(gaitame);
            //Log.d("JSONObject",currencyPairCode+" "+ open+" "+high+" "+low+" "+bid+" "+ask);
        }
        return listdata;
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
    private boolean inViewBounds(final View view, int x, int y){
        Rect outRect = new Rect();
        view.getDrawingRect(outRect);
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        outRect.offset(location[0], location[1]);
        return outRect.contains(x, y);
    }
}
