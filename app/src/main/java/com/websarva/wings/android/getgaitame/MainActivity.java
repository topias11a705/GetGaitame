package com.websarva.wings.android.getgaitame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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


public class MainActivity extends AppCompatActivity {
    ArrayList<gaitameDataBox> listData = new ArrayList<>();
    //CopyOnWriteArrayList<gaitameDataBox> listData_clone = new CopyOnWriteArrayList<>();
    static RecyclerView recycleview= null; ImageView imageView;LinearLayoutManager mLinearLayoutManager;
    SwitchCompat switchButton ;
    ItemTouchHelper mIth;
    Timer mTimer = new Timer();
    TimerTask mTimerTask = new MainTimerTask();
    Handler mHandler = new Handler();
    DiffUtil.DiffResult diffResult;
    static boolean menu_flag = true;
    static boolean recycleHelper_flag = true;
    static boolean excute_flag = true;
    static boolean config_mode_flag = false;
    static boolean config_mode_hozon_flag = false;
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
        switchButton = findViewById(R.id.onnOff_menubar_switch);
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle(R.string.toolbar_title_on);
        setSupportActionBar(toolbar);
        //toolbar.inflateMenu(R.menu.menu);
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

        recycleview.addOnScrollListener(new EndlessScrollListener((LinearLayoutManager) recycleview.getLayoutManager()) {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState){
                super.onScrollStateChanged(recyclerView,newState);
                //Log.i("EndlessScrollListener","EndlessScrollListener onScrollStateChanged "+ String.valueOf(newState));
                if (newState==1){//スクロールはじめ
                    excute_flag = false;
                }else if (newState==0){//スクロール終わり
                    excute_flag = true;
                }
            }
        });

        try{mTimer.schedule(mTimerTask, 0, 1500);}catch(Exception e){System.out.print(e);}
    }
    @Override
    protected void onResume() {
        if(switchButton != null){
            switchButton.setChecked(true);
        }
        super.onResume();
    }
    @Override
    protected void onPause() {
        if(switchButton != null){
            switchButton.setChecked(false);
        }
        super.onPause();
    }
    public class MainTimerTask extends TimerTask { //➀
        @Override
        public void run() {
            mHandler.post(new Runnable() {  //④
                @Override
                public void run() {
                    WeatherInfoReceiver receiver = new WeatherInfoReceiver();
                    receiver.execute("");
                }
            });
        }
    }

    //*******************************************************************************************

    private class RecyclerListViewHolder extends RecyclerView.ViewHolder {
        TextView currencyPairCodetextView_textView, bid_textView, ask_textView, open_textView, high_textView, low_textView;
        ImageView currency_image_view, bit_yajirusi_view, ask_yajirusi_view;
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
            //Log.d("color",(((TextView) itemView.findViewById(R.id.low)).getTextColors().toString()));
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
        public void onBindViewHolder(RecyclerListViewHolder holder, int i){
            gaitameDataBox item = _listData.get(i);
            if(_listData != null && _listData.size() > i && item != null){
                holder.parentLinearLayout.setBackgroundColor(item.getBackGroundColor());
                holder.open_textView.setTextColor(Color.RED);
                holder.currencyPairCodetextView_textView.setText(item.getCurrencyPairCode());
                holder.bid_textView.setText(item.getBid());
                holder.ask_textView.setText(item.getAsk());
                holder.open_textView.setText(item.getOpen());
                holder.high_textView.setText(item.getHigh());
                holder.low_textView.setText(item.getLow());
                holder.open_textView.setTextColor(item.getOpenTextColor());
                holder.high_textView.setTextColor(item.getHighTextColor());
                holder.low_textView.setTextColor(item.getLowTextColor());
                holder.currency_image_view.setImageDrawable(item.getFlag_image());
                holder.bit_yajirusi_view.setImageDrawable(item.getBit_yajirushi_image());
                holder.ask_yajirusi_view.setImageDrawable(item.getAsk_yajirushi_image());
            }
        }
        @Override//リストデータ中の件数をリターン。
        public int getItemCount() {
            if(_listData != null){return _listData.size();}
            else{return 0;}
        }

        public void updateList(ArrayList<gaitameDataBox> newList) {
            diffResult = DiffUtil.calculateDiff(new MyDiffUtilCallback(this._listData, newList));
            diffResult.dispatchUpdatesTo(adapter);
        }
    }
    //*******************************************************************************************
    private class WeatherInfoReceiver extends AsyncTask<String, String, String> {
        public WeatherInfoReceiver(){}
        @Override
        public synchronized String doInBackground(String... params) {
            //*************************************************************
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
            //**************************************************************
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
                            double oldBit,newBit,oldAsk,newAsk,oldOpen,newOpen,oldHigh,newHigh,oldLow,newLow;
                            oldBit  = Double.valueOf(olddata.getBid());  newBit  = Double.valueOf(newdata.getBid());
                            oldAsk  = Double.valueOf(olddata.getAsk());  newAsk  = Double.valueOf(newdata.getAsk());
                            oldOpen = Double.valueOf(olddata.getOpen()); newOpen = Double.valueOf(newdata.getOpen());
                            oldHigh = Double.valueOf(olddata.getHigh()); newHigh = Double.valueOf(newdata.getHigh());
                            oldLow  = Double.valueOf(olddata.getLow());  newLow  = Double.valueOf(newdata.getLow());
                            Drawable uemuki = getResources().getDrawable(R.drawable.uemuki);
                            Drawable sitamuki = getResources().getDrawable(R.drawable.sitamuki);
                            if     (oldBit < newBit){newdata.setBit_yajirushi_image(uemuki);newdata.setBackGroundColor(Color.rgb(244, 150, 206));}
                            else if(oldBit > newBit){newdata.setBit_yajirushi_image(sitamuki);newdata.setBackGroundColor(Color.rgb(141, 172, 239));}
                            if     (oldAsk < newAsk){newdata.setAsk_yajirushi_image(uemuki);}
                            else if(oldAsk > newAsk){newdata.setAsk_yajirushi_image(sitamuki);}
                            if     (oldOpen!=newOpen){newdata.setOpenTextColor(Color.RED);}else{newdata.setOpenTextColor(-1979711488);}
                            if     (oldHigh!=newHigh){newdata.setHighTextColor(Color.RED);}else{newdata.setHighTextColor(-1979711488);}
                            if     (oldLow != newLow){ newdata.setLowTextColor(Color.RED);}else{ newdata.setLowTextColor(-1979711488);}
                            if(oldBit==newBit && oldAsk==newAsk && oldOpen==newOpen && oldHigh==newHigh && oldLow==newLow){ newdata.setAllReset(); }
                         }
                    }
                    if(adapter == null){adapter = new RecyclerListAdapter(listData);}else{adapter._listData = listData;}

                }catch(Exception ex) {
                    ex.printStackTrace();
                }finally {
                    if(con!=null){con.disconnect();}
                }
            }
            return result;
        }
        @Override //UI(メイン)スレッド処理
        public synchronized void onPostExecute(String result){
            if(excute_flag) {
                if (recycleHelper_flag) {
                    switchButton = findViewById(R.id.onnOff_menubar_switch);
                    if(switchButton != null){
                        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            excute_flag = isChecked;
                            if (isChecked) {
                                Toast.makeText(contex1, "自動更新開始", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(contex1, "自動更新停止", Toast.LENGTH_SHORT).show();
                                for(gaitameDataBox data : listData){ data.setAllReset(); }
                            }
                            }
                        });
                    }
                    recycleHelper_flag = false;
                }
                recycleview.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                mLinearLayoutManager.scrollToPositionWithOffset(position, y);
            }
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
            //todo 同一Activityのままではなくて設定用別Activityを作る
            //todo 設定モード中は設定モードであることがユーザーに分かりやすいように何かする(ビジュアルでか、テキストで直接的にするか)
            case R.id.configMode:
                switchButton = findViewById(R.id.onnOff_menubar_switch);
                if(!config_mode_flag){//設定モードじゃないなら設定モードにする
                    config_mode_flag = true;//設定モードをONにする
                    if(switchButton.isChecked()){//ボタンのチェックがTRUEならFALSEにして不使用化する
                        switchButton.setChecked(false);
                        config_mode_hozon_flag =true;
                    }else{
                        config_mode_hozon_flag =false;
                    }
                    switchButton.setEnabled(false);
                    /*
                    mIth = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
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
                    */
                }else{//設定モード中なら元に戻す
                    config_mode_flag = false;//設定モードをOFFにする。
                    //***Listener消せない****
                    Log.d("getItemDecorationCount",String.valueOf(recycleview.getItemDecorationCount()));
                    recycleview.removeItemDecoration(mIth);

                    mIth = null;
                    //***********************
                    switchButton.setEnabled(true);//不使用状態を解除
                    if(config_mode_hozon_flag){//戻したときの実行は設定モードに入る直前の時と同じにする。
                        switchButton.setChecked(true);
                    }else{
                        switchButton.setChecked(false);
                    }
                }
                break;
            /*
            case R.id.onnOff_menubar_switch_item:
                break;
            */
        }
        return super.onOptionsItemSelected(item);
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
}
/*
TODO:順番を任意で入れ替えられるようにする
TODO:タップしたときの処理
TODO:サービスを使ってバックグラウンドで為替の通知表示
TODO:編集タブで表示数や表示順を設定保存できるようにする
TODO:データベースを使用しユーザー登録できるようにする
TODO データ更新時には変更部分だけを更新するようにして動作を早くさせる
TODO getRecycledViewPoolが再利用の仕組みを提供している可能性があるので使用検討する　
todo 設定できることを増やす　背景色/
https://developer.android.com/reference/android/support/v7/widget/RecyclerView.RecycledViewPool

*/

