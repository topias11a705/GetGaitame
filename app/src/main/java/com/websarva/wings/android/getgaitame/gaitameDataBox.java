package com.websarva.wings.android.getgaitame;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.websarva.wings.android.getgaitame.R;

public class gaitameDataBox {
    private  String currencyPairCode,open,high,low,bid,ask;
    private Drawable image;
    public gaitameDataBox(){
        super();

    }
    synchronized public String getCurrencyPairCode(){return currencyPairCode;}
    synchronized public void setCurrencyPairCode(String currencyPairCode){this.currencyPairCode = currencyPairCode;}
    synchronized public String getOpen(){return open;}
    synchronized public void setOpen(String open){this.open = open;}
    synchronized public String getHigh() {return high;}
    synchronized public void setHigh(String high){this.high = high;}
    synchronized public String getLow(){return low;}
    synchronized public void setLow(String low){this.low = low;}
    synchronized public String getBid(){return bid;}
    synchronized public void setBid(String bid){this.bid = bid;}
    synchronized public String getAsk(){return ask;}
    synchronized public void setAsk(String ask){this.ask = ask;}
    synchronized public Drawable getImage() {return image;}
    synchronized public void setImage(Drawable  image){this.image = image;}
}