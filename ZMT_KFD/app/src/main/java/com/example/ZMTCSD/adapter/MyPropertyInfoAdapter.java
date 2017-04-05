package com.example.ZMTCSD.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.entity.PropertyGroupsEntity;

import java.util.List;

/**
 *   基本信息中的分组信息
 */

public class MyPropertyInfoAdapter extends RecyclerView.Adapter<MyPropertyInfoAdapter.ViewHolder> {
    private List<PropertyGroupsEntity.PropertyInfos> properList;
    private Context mContent;

    public MyPropertyInfoAdapter(Context mContext ,List<PropertyGroupsEntity.PropertyInfos> properlist) {
        this.mContent=mContext;
        this.properList = properlist;
    }
    @Override
    public int getItemCount() {
        return properList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_details_item_recyclerview, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PropertyGroupsEntity.PropertyInfos propertyEntity = properList.get(position);
        if(propertyEntity.getImportance() ==1){
            holder.propertyValue.setTextColor(mContent.getResources().getColor(R.color.red));
        }
        holder.propertyName.setText(propertyEntity.getPropertyName());
        holder.propertyValue.setText(propertyEntity.getPropertyValue());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView propertyName;
        public AppCompatTextView propertyValue;
        public ViewHolder(View itemView) {
            super(itemView);
            propertyName = (AppCompatTextView) itemView.findViewById(R.id.tv_item_details_item_name);
            propertyValue = (AppCompatTextView) itemView.findViewById(R.id.tv_item_details_item_value);
        }
    }
}
