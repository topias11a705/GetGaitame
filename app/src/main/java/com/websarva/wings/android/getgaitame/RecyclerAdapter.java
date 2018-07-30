package com.websarva.wings.android.getgaitame;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by am on 2018/07/30.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

private LayoutInflater mInflater;
private ArrayList<gaitameDataBox> ArrayDate;
private Context mContext;
private OnRecyclerListener mListener;

public RecyclerAdapter(Context context, ArrayList<gaitameDataBox> data, OnRecyclerListener listener) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        ArrayDate = data;
        mListener = listener;
        }

@Override
public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // 表示するレイアウトを設定
        return new ViewHolder(mInflater.inflate(R.layout.gaitame_list, viewGroup, false));
        }

@Override
public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        // データ表示
        if (ArrayDate != null && ArrayDate.size() > i && ArrayDate.get(i) != null) {
            viewHolder.currencyPairCodetextView_textView.setText(ArrayDate.get(i).getCurrencyPairCode());
            viewHolder.bid_textView.setText(ArrayDate.get(i).getBid());
            viewHolder.ask_textView.setText(ArrayDate.get(i).getAsk());
            viewHolder.open_textView.setText(ArrayDate.get(i).getOpen());
            viewHolder.high_textView.setText(ArrayDate.get(i).getHigh());
            viewHolder.low_textView.setText(ArrayDate.get(i).getLow());
            viewHolder.currency_image_view.setImageDrawable(ArrayDate.get(i));


        }

        // クリック処理
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
         @Override
        public void onClick(View v) {
            mListener.onRecyclerClicked(v, i);
        }
        });

}

@Override
public int getItemCount() {
        if (ArrayDate != null) {
        return ArrayDate.size();
        } else {
        return 0;
        }
        }

// ViewHolder(固有ならインナークラスでOK)
class ViewHolder extends RecyclerView.ViewHolder {

    TextView currencyPairCodetextView_textView;
    TextView bid_textView;
    TextView ask_textView;
    TextView open_textView;
    TextView high_textView;
    TextView low_textView;
    ImageView currency_image_view;



    public ViewHolder(View itemView) {
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

}
