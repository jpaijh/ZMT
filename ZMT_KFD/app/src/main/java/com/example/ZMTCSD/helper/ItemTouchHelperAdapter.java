package com.example.ZMTCSD.helper;

/**
 * itemtouchhelper 拖拽 滑动 的Adapter， 实现在recyclerview的adapter中
 */
public interface ItemTouchHelperAdapter {

    /**
     * 拖拽
     * @param fromPosition
     * @param toPosition
     * @return
     */
    boolean onItemMove(int fromPosition, int toPosition,boolean islocation);

    /**
     * 滑动删除
     * @param position
     */
    void onItemDismiss(int position ,boolean islocation);
}
