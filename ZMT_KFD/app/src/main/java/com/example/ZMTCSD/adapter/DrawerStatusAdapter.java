package com.example.ZMTCSD.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.R;

import java.util.ArrayList;
import java.util.List;

/**
 *   侧边栏状态的适配器
 */

public class DrawerStatusAdapter extends RecyclerView.Adapter<DrawerStatusAdapter.ViewHolder> {
    private Context mContent;
    private String[] Strings;
    private int oldPosition=-1;
    private int newPosition;
    private List<Integer> checkPositionList;
    private OnRecyclerViewItemClickListener mOnItemClickListener;


    public void setmOnItemClickListener(OnRecyclerViewItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(int  position ,String name);
    }
    public DrawerStatusAdapter(Context mContent, String[] strings) {
        this.mContent = mContent;
        this.checkPositionList=new ArrayList<>();
        this.Strings = strings;
    }


    //重置
    public void ResetPosition(){
        checkPositionList.clear();
        notifyDataSetChanged();
        mOnItemClickListener.onItemClick( -1 , "空");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContent).inflate(R.layout.item_drawer_status, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,  int position) {
        holder.tv_state.setTag(Strings[position]);
        holder.tv_state.setText(Strings[position]);
        holder.tv_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPosition = holder.getLayoutPosition();
                if(checkPositionList.contains(new Integer(newPosition)) ? true : false){
                    checkPositionList.clear();
                }else{
                    if(oldPosition ==newPosition) {
                        checkPositionList.add(newPosition);
                    }else {
                        checkPositionList.clear();
                        checkPositionList.add(newPosition);
                    }
                }
//                oldPosition=newPosition;
                notifyDataSetChanged();
//                LogUtils.d("checkPositionList"+checkPositionList.toString());
                if(checkPositionList.isEmpty()){
                    mOnItemClickListener.onItemClick(oldPosition , "空");
                }else{
                    mOnItemClickListener.onItemClick(oldPosition , (String) v.getTag());
                }
            }
        });
    //更改状态 position == oldPosition &&
        if(checkPositionList.contains(new Integer(position)) ? true : false ){
            holder.tv_state.setBackgroundResource(R.drawable.shape_drawer_ed_select);
            holder.tv_state.setTextColor(mContent.getResources().getColor(R.color.color_theme));//#FF3498DB
        }else{
            holder.tv_state.setBackgroundResource(R.drawable.shape_drawer_ed);
            holder.tv_state.setTextColor(mContent.getResources().getColor(R.color.black_two_mark_54));
        }
    }

    @Override
    public int getItemCount() {
        return Strings.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tv_state ;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_state= (AppCompatTextView) itemView.findViewById(R.id.tv_status_name);
        }
    }
}
