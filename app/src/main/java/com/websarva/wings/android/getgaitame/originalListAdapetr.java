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
    LayoutInflater mInflater;    ArrayList<gaitameDataBox> listData;
    public originalListAdapetr(Context context, int resource, ArrayList<gaitameDataBox> objects) {
        super(context, resource,objects);
        mInflater = LayoutInflater.from(context);
        listData = (ArrayList<gaitameDataBox>)objects;
    }
    @Override
    public gaitameDataBox getItem(int position) {
        return listData.get(position);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.gaitame_list,parent,false);
        }
        gaitameDataBox gaitamedata = getItem(position);

        ((TextView)convertView.findViewById(R.id.currencyPairCode))
                .setText(gaitamedata.getCurrencyPairCode());
        ((TextView)convertView.findViewById(R.id.bid))
                .setText(gaitamedata.getBid());
        ((TextView)convertView.findViewById(R.id.ask))
                .setText(gaitamedata.getAsk());
        ((TextView)convertView.findViewById(R.id.open))
                .setText(gaitamedata.getOpen());
        ((TextView)convertView.findViewById(R.id.high))
                .setText(gaitamedata.getHigh());
        ((TextView)convertView.findViewById(R.id.low))
                .setText(gaitamedata.getLow());
        ((ImageView)convertView.findViewById(R.id.currency_image_view))
                .setImageDrawable(gaitamedata.getImage());
        return convertView;
    }
}
