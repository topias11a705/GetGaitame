package com.websarva.wings.android.getgaitame;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class gaitameDataBox implements Cloneable {
    private  String currencyPairCode,open,high,low,bid,ask;
    private Drawable flag_image,yajirushi_image; private int backGroundColor = Color.WHITE;
    public gaitameDataBox(){
        super();
    }
    public gaitameDataBox(String currencyPairCode,String open,String high,String low,String bid,String ask){
        super();
        setCurrencyPairCode(currencyPairCode);setOpen(open);setHigh(high);setLow(low);setBid(bid);setAsk(ask);
    }
    @Override
    public gaitameDataBox clone() { //基本的にはpublic修飾子を付け、自分自身の型を返り値とする
        gaitameDataBox b=null;
        try {
            b=(gaitameDataBox)super.clone(); //親クラスのcloneメソッドを呼び出す(親クラスの型で返ってくるので、自分自身の型でのキャストを忘れないようにする)
        }catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return b;
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
    synchronized public Drawable getFlag_image() {return flag_image;}
    synchronized public void setFlag_image(Drawable flag_image){this.flag_image = flag_image;}
    synchronized public Drawable getYajirushi_image() {return yajirushi_image;}
    synchronized public void setYajirushi_image(Drawable  yajirushi_image){this.yajirushi_image = yajirushi_image;}
    synchronized public int getBackGroundColor() {return backGroundColor;}
    synchronized public void setBackGroundColor(int backGroundColor){this.backGroundColor = backGroundColor;}
}