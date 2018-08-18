package com.websarva.wings.android.getgaitame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class AdGaitameActivity extends MainActivity {
    ArrayList<gaitameDataBox> listData = new ArrayList<>();
    //CopyOnWriteArrayList<gaitameDataBox> listData_clone = new CopyOnWriteArrayList<>();
    static RecyclerView recycleview = null;
    ImageView imageView;
    LinearLayoutManager mLinearLayoutManager;
    SwitchCompat switchButton;
    ItemTouchHelper mIth;
    Timer mTimer = new Timer();
    TimerTask mTimerTask = new MainTimerTask();
    Handler mHandler = new Handler();
    static boolean menu_flag = true;
    static boolean recycleHelper_flag = true;
    static boolean excute_flag = true;
    static boolean config_mode_flag = false;
    static boolean config_mode_hozon_flag = false;
    Context contex1, contex2;
    int position = 0; int y = 0;
    RecyclerListAdapter adapter = null;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.currency_image_view);
        contex1 = AdGaitameActivity.this;
        contex2 = getApplicationContext();
        switchButton = findViewById(R.id.onnOff_menubar_switch);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle(R.string.toolbar_title_on);
        setSupportActionBar(toolbar);
        //toolbar.inflateMenu(R.menu.menu);
        //CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolbarLayout);
        recycleview = findViewById(R.id.lvCityList);
        mLinearLayoutManager = new LinearLayoutManager(AdGaitameActivity.this);
        recycleview.setLayoutManager(mLinearLayoutManager);
        recycleview.addItemDecoration(new DividerItemDecoration(AdGaitameActivity.this, mLinearLayoutManager.getOrientation()));
        recycleview.addOnItemTouchListener(new RecyclerItemClickListener(contex2, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("MOTIONLitener", "OnItemClickListener onItemClick");
            }
        }));
        recycleview.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                Log.d("MOTIONLitener", "onInterceptTouchEvent");
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                Log.d("MOTIONLitener", "onTouchEvent");
                ((MyRecyclerView) recycleview).onTouchEvent(e);
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                Log.d("MOTIONLitener", "onRequestDisallowInterceptTouchEvent");
            }
        });
        recycleview.addOnScrollListener(new EndlessScrollListener((LinearLayoutManager) recycleview.getLayoutManager()) {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //Log.i("EndlessScrollListener","EndlessScrollListener onScrollStateChanged "+ String.valueOf(newState));
                if (newState == 1) {//スクロールはじめ
                    excute_flag = false;
                } else if (newState == 0) {//スクロール終わり
                    excute_flag = true;
                }
            }
        });
        AdView mAdView = findViewById(R.id.adView);
        mAdView.loadAd(new AdRequest.Builder().build());
        try{mTimer.schedule(mTimerTask, 0, 5000);}catch(Exception e){System.out.print(e);}
    }
    @Override protected void onResume() {
        super.onResume();
        if(switchButton != null){switchButton.setChecked(true);}
    }
    @Override protected void onPause() {
        super.onPause();
        if(switchButton != null){switchButton.setChecked(false);}
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
        LinearLayout parentLinearLayout;RecyclerView recyclerView_;
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
    private class RecyclerListAdViewHolder extends RecyclerView.ViewHolder {
        AdView adview;
        public RecyclerListAdViewHolder(View itemView) {
            super(itemView);
            adview = itemView.findViewById(R.id.adView);
        }
    }
    private class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        ArrayList<gaitameDataBox> _listData;
        public RecyclerListAdapter(ArrayList<gaitameDataBox> listData) {
            _listData = listData;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(AdGaitameActivity.this);
            switch (viewType){
                case 100:
                    return new RecyclerListAdViewHolder(inflater.inflate(R.layout.adview_list, parent, false));
                default:
                    return new RecyclerListViewHolder(inflater.inflate(R.layout.gaitame_list, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
            switch (getItemViewType(i)){
                case 100:
                    ((RecyclerListAdViewHolder)holder).adview.loadAd( new AdRequest.Builder().build() );
                    break;
                default:
                    gaitameDataBox item = _listData.get(i);
                    if (_listData != null && _listData.size() > i && item != null) {
                        ((RecyclerListViewHolder)holder).parentLinearLayout.setBackgroundColor(item.getBackGroundColor());
                        ((RecyclerListViewHolder)holder).open_textView.setTextColor(Color.RED);
                        ((RecyclerListViewHolder)holder).currencyPairCodetextView_textView.setText(item.getCurrencyPairCode());
                        ((RecyclerListViewHolder)holder).bid_textView.setText(item.getBid());
                        ((RecyclerListViewHolder)holder).ask_textView.setText(item.getAsk());
                        ((RecyclerListViewHolder)holder).open_textView.setText(item.getOpen());
                        ((RecyclerListViewHolder)holder).high_textView.setText(item.getHigh());
                        ((RecyclerListViewHolder)holder).low_textView.setText(item.getLow());
                        ((RecyclerListViewHolder)holder).open_textView.setTextColor(item.getOpenTextColor());
                        ((RecyclerListViewHolder)holder).high_textView.setTextColor(item.getHighTextColor());
                        ((RecyclerListViewHolder)holder).low_textView.setTextColor(item.getLowTextColor());
                        ((RecyclerListViewHolder)holder).currency_image_view.setImageDrawable(item.getFlag_image());
                        ((RecyclerListViewHolder)holder).bit_yajirusi_view.setImageDrawable(item.getBit_yajirushi_image());
                        ((RecyclerListViewHolder)holder).ask_yajirusi_view.setImageDrawable(item.getAsk_yajirushi_image());
                    }
                    break;
            }

        }

        @Override//リストデータ中の件数をリターン。
        public int getItemCount() {
            if(_listData != null){return _listData.size();}
            else{return 0;}
        }

        @Override
        public int getItemViewType(int position) {
            int a = 0;
            a = position%5==0 ? 100 : position;
            if(position == 0){a=0;}
            return a;
        }
    }

    //******************************************************************************************
    private class WeatherInfoReceiver extends AsyncTask<String, String, String> {
        public WeatherInfoReceiver() {
        }

        @Override
        public synchronized String doInBackground(String... params) {
            //*************************************************************
            String urlStr = "https://www.gaitameonline.com/rateaj/getrate";
            String result = "";
            if (excute_flag) {
                HttpURLConnection con = null;
                try {
                    URL url = new URL(urlStr);
                    con = (HttpURLConnection) url.openConnection();
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

                    //並べ替え
                    ArrayList<gaitameDataBox> a = listData;
                    for (int i = 0; i < listData.size(); i++) {
                        listData.set(i, a.get(i/*←a.get(i)のiの値を任意の値に変更する*/));
                    }
                    listData.set(0, a.get(20));
                    listData.set(1, a.get(16));
                    listData.set(2, a.get(23));
                    listData.set(23, a.get(2));
                    listData.set(16, a.get(1));
                    listData.set(20, a.get(0));

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


                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                }
            }
            return result;
        }

        @Override //UI(メイン)スレッド処理
        public synchronized void onPostExecute(String result) {
            if (excute_flag) {
                if (recycleHelper_flag) {
                    switchButton = findViewById(R.id.onnOff_menubar_switch);
                    if (switchButton != null) {
                        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                excute_flag = isChecked;
                                if (isChecked) {
                                    Toast.makeText(contex1, "自動更新開始", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(contex1, "自動更新停止", Toast.LENGTH_SHORT).show();
                                    for (gaitameDataBox data : listData) {
                                        data.setAllReset();
                                    }
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
        switch (item.getItemId()) {
            //todo 同一Activityのままではなくて設定用別Activityを作る
            //todo 設定モード中は設定モードであることがユーザーに分かりやすいように何かする(ビジュアルでか、テキストで直接的にするか)
            case R.id.configMode:
                switchButton = findViewById(R.id.onnOff_menubar_switch);
                if (!config_mode_flag) {//設定モードじゃないなら設定モードにする
                    config_mode_flag = true;//設定モードをONにする
                    if (switchButton.isChecked()) {//ボタンのチェックがTRUEならFALSEにして不使用化する
                        switchButton.setChecked(false);
                        config_mode_hozon_flag = true;
                    } else {
                        config_mode_hozon_flag = false;
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
                } else {//設定モード中なら元に戻す
                    config_mode_flag = false;//設定モードをOFFにする。
                    //***Listener消せない****
                    Log.d("getItemDecorationCount", String.valueOf(recycleview.getItemDecorationCount()));
                    recycleview.removeItemDecoration(mIth);

                    mIth = null;
                    //***********************
                    switchButton.setEnabled(true);//不使用状態を解除
                    if (config_mode_hozon_flag) {//戻したときの実行は設定モードに入る直前の時と同じにする。
                        switchButton.setChecked(true);
                    } else {
                        switchButton.setChecked(false);
                    }
                }
                break;


        }
        return super.onOptionsItemSelected(item);
    }
}