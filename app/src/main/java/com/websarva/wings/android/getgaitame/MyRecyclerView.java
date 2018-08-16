package com.websarva.wings.android.getgaitame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChild2;
import android.support.v4.view.ScrollingView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by am on 2018/08/02.
 */

public class MyRecyclerView extends RecyclerView{
    public MyRecyclerView(Context context) {this(context, null);}
    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {super(context, attrs, 0);}
/*
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("MOTIONLitener", "onTouchEvent ACTION_DOWN");

                return true;//break;
            case MotionEvent.ACTION_UP:
                Log.d("MOTIONLitener", "onTouchEvent ACTION_UP");

                return true;//break;
        }
        return false;
    }
*/
}