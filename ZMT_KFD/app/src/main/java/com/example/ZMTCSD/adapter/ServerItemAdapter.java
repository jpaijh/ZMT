package com.example.ZMTCSD.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ZMTCSD.R;
import com.example.ZMTCSD.entity.MetaDataEntity;
import java.util.ArrayList;
import java.util.List;

/**
 *  服务项的适配器
 */

public class ServerItemAdapter extends RecyclerView.Adapter<ServerItemAdapter.ViewHolder>  {
    private Context mContent;
    private List<MetaDataEntity.Value> valueEntity;
    private List<Integer> checkPositionList;
    private int oldPosition=-1;
    private int newPosition;
    private OnRecyclerViewItemClickListener mOnItemClickListener;


    public void addmOnItemClickListener(OnRecyclerViewItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(int  position ,String name);
    }

    public ServerItemAdapter(Context mContent , List<MetaDataEntity.Value> valueEntity) {
        this.mContent = mContent;
        this.checkPositionList=new ArrayList<>();
        if(valueEntity !=null){
        this.valueEntity = valueEntity;
        }else{
            this.valueEntity=new ArrayList<>();
        }
    }

    //重置
    public void ResetPosition(){
        checkPositionList.clear();
        notifyDataSetChanged();
        mOnItemClickListener.onItemClick( -1 , "");
    }


    @Override
    public ServerItemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContent).inflate(R.layout.item_drawer_status, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MetaDataEntity.Value entity=valueEntity.get(position);
        holder.tv_state.setTag(entity.getServiceItemId());
        holder.tv_state.setText(entity.getName());
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
                oldPosition=newPosition;
                notifyDataSetChanged();
                mOnItemClickListener.onItemClick(newPosition , (String) v.getTag());
            }
        });
        //更改状态
        if(position == oldPosition && checkPositionList.contains(new Integer(position)) ? true : false ){
            holder.tv_state.setBackgroundResource(R.drawable.shape_drawer_ed_select);
            holder.tv_state.setTextColor(mContent.getResources().getColor(R.color.color_theme));//#FF3498DB
        }else{
            holder.tv_state.setBackgroundResource(R.drawable.shape_drawer_ed);
            holder.tv_state.setTextColor(mContent.getResources().getColor(R.color.black_two_mark_54));
        }
    }

    @Override
    public int getItemCount() {
        return valueEntity.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tv_state ;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_state= (AppCompatTextView) itemView.findViewById(R.id.tv_status_name);

        }
    }
}
