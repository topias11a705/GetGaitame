package com.websarva.wings.android.getgaitame;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class originalListAdapetr  extends ArrayAdapter<gaitameDataBox>{
    LayoutInflater mInflater;
    ArrayList<gaitameDataBox> listData;

    public originalListAdapetr(Context context, int resource, ArrayList<gaitameDataBox> objects) {
        super(context, 0);
        mInflater = LayoutInflater.from(context);
        listData = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){convertView=mInflater.inflate(R.layout.gaitame_list,parent,false);}
        gaitameDataBox gaitamedata = getItem(position);

        TextView tv = (TextView)convertView.findViewById(R.id.currencyPairCode);
        tv.setText(gaitamedata.getCurrencyPairCode());
        tv = (TextView)convertView.findViewById(R.id.bid);
        tv.setText(gaitamedata.getBid());
        tv = (TextView)convertView.findViewById(R.id.ask);
        tv.setText(gaitamedata.getAsk());
        tv = (TextView)convertView.findViewById(R.id.open);
        tv.setText(gaitamedata.getOpen());
        tv = (TextView)convertView.findViewById(R.id.high);
        tv.setText(gaitamedata.getHigh());
        tv = (TextView)convertView.findViewById(R.id.low);
        tv.setText(gaitamedata.getLow());
        ImageView iv = (ImageView)convertView.findViewById(R.id.currency_image_view);
        iv.setImageDrawable(gaitamedata.getImage());


        return convertView;
    }
}
