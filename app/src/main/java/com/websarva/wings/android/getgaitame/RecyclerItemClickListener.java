package com.websarva.wings.android.getgaitame;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    GestureDetector mGestureDetector;
    private OnItemClickListener mListener;

    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {

                Log.v("Gesture", "onDoubleTap");
                return super.onDoubleTap(e);
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {

                Log.v("Gesture", "onDoubleTapEvent");
                return super.onDoubleTapEvent(e);
            }

            @Override
            public boolean onDown(MotionEvent e) {

                Log.v("Gesture", "onDown");
                return super.onDown(e);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {

                Log.v("Gesture", "onFling");
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public void onLongPress(MotionEvent e) {

                Log.v("Gesture", "onLongPress");
                super.onLongPress(e);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

                Log.v("Gesture", "onScroll");
                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            @Override
            public void onShowPress(MotionEvent e) {

                Log.v("Gesture", "onShowPress");
                super.onShowPress(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {

                Log.v("Gesture", "onSingleTapConfirmed");
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {

                Log.v("Gesture", "onSingleTapUp");
                return super.onSingleTapUp(e);
            }
        });
    }
    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        // タッチした箇所のViewを取得
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            // onInterceptTouchEventのタイミングだとアイテムのtouch feedbackがつく前にonItemClickが呼ばれてしまうので、明示的にsetPressed(true)を呼んでいます。
            childView.setPressed(true);
            mListener.onItemClick(childView, view.getChildPosition(childView));
        }
        return false;
    }
    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept){
    }
    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        // Do nothing
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}