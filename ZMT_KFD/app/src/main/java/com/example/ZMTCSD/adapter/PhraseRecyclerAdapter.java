package com.example.ZMTCSD.adapter;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.entity.PhraseEntity;
import com.example.ZMTCSD.helper.ItemTouchHelperAdapter;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by junping on 2016/7/21.
 */
public class PhraseRecyclerAdapter extends RecyclerView.Adapter<PhraseRecyclerAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    //    public List<PhraseEntity>  list=new ArrayList<>();
    private List<PhraseEntity> listtop=new ArrayList<>();
    private List<PhraseEntity> listbutton=new ArrayList<>();
    private boolean isLocation;


    public List<PhraseEntity> getListtop() {
        return listtop;
    }

    public void setListtop(List<PhraseEntity> listtop) {
        this.listtop = listtop;
    }

    public List<PhraseEntity> getListbutton() {
        return listbutton;
    }

    public void setListbutton(List<PhraseEntity> listbutton) {
        this.listbutton = listbutton;
    }

    public PhraseRecyclerAdapter(List<PhraseEntity> list, boolean islocation) {

        if (islocation)
            this.listtop = list;
        else
            this.listbutton = list;

        this.isLocation = islocation;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phrase_recycler, parent, false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (isLocation) {
            LogUtils.d("我是top");
            PhraseEntity entity = listtop.get(position);
            holder.centent.setText(entity.getText());
        } else {
            LogUtils.d("我是button");
            PhraseEntity entity = listbutton.get(position);
            holder.centent.setText(entity.getText());
        }
        holder.menu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    LogUtils.d("menu的点击");
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (isLocation)
            return listtop.size();
        else
            return listbutton.size();
    }

    public void addItem(String s, int position, boolean istop) {
        if (istop) {
            LogUtils.e("top增加的数据" + s + ":" + position);
            listtop.add(new PhraseEntity(s, position));
            notifyItemInserted(listtop.size());
        } else {
            LogUtils.e("button增加的数据" + s + ":" + position);
            listbutton.add(new PhraseEntity(s, position));
            notifyItemInserted(listbutton.size());
        }
    }

    /**
     * 拖拽
     *
     * @param fromPosition
     * @param toPosition
     * @return
     */
    @Override
    public boolean onItemMove(int fromPosition, int toPosition, boolean isLocation) {
        if (isLocation) {
            LogUtils.d("拖拽top");
            Collections.swap(listtop, fromPosition, toPosition);
        } else {
            LogUtils.d("拖拽button");
            Collections.swap(listbutton, fromPosition, toPosition);
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    /**
     * 滑动删除
     *
     * @param position
     */
    @Override
    public void onItemDismiss(int position, boolean isLocation) {
        if (isLocation) {
            LogUtils.d("滑动删除top");
            listtop.remove(position);
        } else {
            LogUtils.d("滑动删除button");
            listbutton.remove(position);
        }
        notifyItemRemoved(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView cancel;
        AppCompatImageView menu;
        MaterialEditText centent;

        public ViewHolder(View itemView) {
            super(itemView);
            cancel = (AppCompatImageView) itemView.findViewById(R.id.img_phrase_cancel);
            menu = (AppCompatImageView) itemView.findViewById(R.id.img_phrase_menu);
            centent = (MaterialEditText) itemView.findViewById(R.id.ed_phrase_content);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtils.e("点击onclick");
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    LogUtils.e("点击onlongclick");
                    return false;
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtils.d("删除" + getLayoutPosition() + ":" + getAdapterPosition());
                    int position = getLayoutPosition();
                    onItemDismiss(getLayoutPosition(),isLocation);
                }
            });

            centent.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (centent.length() != 0) {
                        if (isLocation) {
                            listtop.get(getLayoutPosition()).setText(centent.getText().toString());
                            LogUtils.e("top输入数据后" + listtop.toString());
                        } else {
                            listbutton.get(getLayoutPosition()).setText(centent.getText().toString());
                            LogUtils.e("button输入数据后" + listbutton.toString());
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private OnStartDragListener mDragStartListener;

    public OnStartDragListener getmDragStartListener() {
        return mDragStartListener;
    }

    public void setmDragStartListener(OnStartDragListener mDragStartListener) {
        this.mDragStartListener = mDragStartListener;
    }

    //这是滑动的事件
    public interface OnStartDragListener {

        void onStartDrag(RecyclerView.ViewHolder viewHolder);
//        void onStartSwipe(RecyclerView.ViewHolder viewHolder);
    }
}
