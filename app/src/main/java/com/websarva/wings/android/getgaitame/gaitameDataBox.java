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
    public String getCurrencyPairCode(){return currencyPairCode;}
    public void setCurrencyPairCode(String currencyPairCode){this.currencyPairCode = currencyPairCode;}
    public String getOpen(){return open;}
    public void setOpen(String open){this.open = open;}
    public String getHigh() {return high;}
    public void setHigh(String high){this.high = high;}
    public String getLow(){return low;}
    public void setLow(String low){this.low = low;}
    public String getBid(){return bid;}
    public void setBid(String bid){this.bid = bid;}
    public String getAsk(){return ask;}
    public void setAsk(String ask){this.ask = ask;}
    public Drawable getImage() {return image;}
    public void setImage(Drawable  image){this.image = image;}
}