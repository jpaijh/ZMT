package com.example.ZMTCSD.helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.apkfuns.logutils.LogUtils;

/**
 * Created by junping on 2016/7/21.
 */
public class MyItemTouchCallback extends ItemTouchHelper.Callback {

    private ItemTouchHelperAdapter helperAdapter;
    private boolean islocation;

    public MyItemTouchCallback(ItemTouchHelperAdapter helperAdapter,boolean islocation) {
        this.helperAdapter = helperAdapter;
        this.islocation=islocation;
    }

    //设置移动方式
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int dragFlags;//拖拽
        final int swipeFlags;
        if(recyclerView.getLayoutManager() instanceof GridLayoutManager){
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            swipeFlags = 0;
        }else{
            dragFlags=ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            swipeFlags= ItemTouchHelper.START |ItemTouchHelper.END;
//           swipeFlags=0; //没有滑动删除
        }
        return makeMovementFlags(dragFlags,swipeFlags);
    }

    //当Item被拖拽的时候被回调
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int firstposition=viewHolder.getAdapterPosition();
        int toposition=target.getAdapterPosition();
        LogUtils.d("个数"+viewHolder.getItemViewType()+","+viewHolder.getLayoutPosition()+"+"+viewHolder.getOldPosition());
        LogUtils.d("个数2"+target.getItemViewType()+","+target.getLayoutPosition()+"+"+target.getOldPosition());
        helperAdapter.onItemMove(firstposition,toposition,islocation);
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //获取的position是相同的
//        LogUtils.e(viewHolder.getAdapterPosition()+":"+viewHolder.getLayoutPosition());
        int position=viewHolder.getAdapterPosition();
        helperAdapter.onItemDismiss(position,islocation); //滑动删除
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // Fade out the view as it is swiped out of the parent's bounds
            final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    /**
     * 在每次View Holder的状态变成拖拽 (ACTION_STATE_DRAG) 或者 滑动 (ACTION_STATE_SWIPE)的时候被调用。
     * @param viewHolder
     * @param actionState
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        // We only want the active item to change
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
//            if (viewHolder instanceof ItemTouchHelperViewHolder) {
//                // Let the view holder know that this item is being moved or dragged
//                ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
//                itemViewHolder.onItemSelected();
//            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    /**
     * 在一个view被拖拽然后被放开的时候被调用，
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setBackgroundColor(0);

//        if (viewHolder instanceof ItemTouchHelperViewHolder) {
//            // Tell the view holder it's time to restore the idle state
//            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
//            itemViewHolder.onItemClear();
//        }
    }


}
