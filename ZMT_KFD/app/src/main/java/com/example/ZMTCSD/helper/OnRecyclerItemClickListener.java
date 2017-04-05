package com.example.ZMTCSD.helper;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * 这是继承RecyclerView.SimpleOnItemTouchListener
 * 与实现RecyclerView.OnItemTouchListener  一样
 * 都是RecyclerView 的触摸实现
 */
public class OnRecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    //处理手势的类：手势探测器
    private GestureDetectorCompat mGestureDetector;
    private RecyclerView recyclerView;

    public OnRecyclerItemClickListener(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(),new ItemTouchHelperGestureListeners());
    }


    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    /** GestureDetector.SimpleOnGestureListener实现了手势 所有的监听
     * 这是手势 监听器
     */
    private class ItemTouchHelperGestureListeners extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            //点击的是那个position
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child!=null) {
                RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(child);
                onItemClick(vh);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child!=null) {
                RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(child);
//                onLongClick(vh);
            }
        }
    }

    public void onLongClick(RecyclerView.ViewHolder vh){}
    public void onItemClick(RecyclerView.ViewHolder vh){}

    /**
     *   OnGestureListener 为单击的监听器
     *    //用户按下屏幕就会触发
     public boolean onDown(MotionEvent e);
     //如果是按下的时间超过瞬间，而且在按下的时候没有松开或者是拖动的，那么onShowPress就会执行
     public void onShowPress(MotionEvent e);
     //一次单独的轻击抬起操作,也就是轻击一下屏幕，就是普通点击事件
     public boolean onSingleTapUp(MotionEvent e);
     //在屏幕上拖动事件
     public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);
     //长按触摸屏，超过一定时长，就会触发这个事件
     public void onLongPress(MotionEvent e);
     //滑屏，用户按下触摸屏、快速移动后松开
     public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY);

     OnDoubleTapListener 为双击的监听器
     //单击事件。用来判定该次点击是SingleTap而不是DoubleTap，
     //如果连续点击两次就是DoubleTap手势，如果只点击一次，
     //系统等待一段时间后没有收到第二次点击则判定该次点击为SingleTap而不是DoubleTap，
     //然后触发SingleTapConfirmed事件
     public boolean onSingleTapConfirmed(MotionEvent e);
     //双击事件
     public boolean onDoubleTap(MotionEvent e);
     //双击间隔中发生的动作。指触发onDoubleTap以后，在双击之间发生的其它动作
     public boolean onDoubleTapEvent(MotionEvent e);
     */
}
