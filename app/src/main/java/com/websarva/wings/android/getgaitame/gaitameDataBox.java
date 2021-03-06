package com.websarva.wings.android.getgaitame;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class gaitameDataBox implements Cloneable {
    private  String currencyPairCode,open,high,low,bid,ask; private int backGroundColor,openTextColor,highTextColor,lowTextColor;
    private Drawable flag_image, bit_yajirushi_image,ask_yajirushi_image,open_yajirushi_image,high_yajirushi_image,low_yajirushi_image;
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
    synchronized public Drawable getBit_yajirushi_image() {return bit_yajirushi_image;}
    synchronized public void setBit_yajirushi_image(Drawable bit_yajirushi_image){this.bit_yajirushi_image = bit_yajirushi_image;}
    synchronized public Drawable getAsk_yajirushi_image() {return ask_yajirushi_image;}
    synchronized public void setAsk_yajirushi_image(Drawable  ask_yajirushi_image){this.ask_yajirushi_image = ask_yajirushi_image;}
    synchronized public void setAllReset(){
        this.backGroundColor = Color.WHITE;
        this.bit_yajirushi_image = null;
        this.ask_yajirushi_image = null;
        this.open_yajirushi_image = null;
        this.high_yajirushi_image = null;
        this.low_yajirushi_image = null;
        this.openTextColor = -1979711488;
        this.highTextColor = -1979711488;
        this.lowTextColor  = -1979711488;
    }
    synchronized public int getBackGroundColor() {return this.backGroundColor;}
    synchronized public void setBackGroundColor(int backGroundColor){this.backGroundColor = backGroundColor;}
    synchronized public int getOpenTextColor() {return this.openTextColor;}
    synchronized public void setOpenTextColor(int openTextColor){this.openTextColor = openTextColor;}
    synchronized public int getHighTextColor() {return this.highTextColor;}
    synchronized public void setHighTextColor(int highTextColor){this.highTextColor = highTextColor;}
    synchronized public int getLowTextColor() {return this.lowTextColor;}
    synchronized public void setLowTextColor(int lowTextColor){this.lowTextColor = lowTextColor;}
}