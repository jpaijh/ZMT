package com.example.ZMTCSD.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ZMTCSD.R;
import com.example.ZMTCSD.entity.PropertyGroupsEntity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 *   基本信息中的适配器
 */

public class MyPropetyGroupsAdapter extends RecyclerView.Adapter<MyPropetyGroupsAdapter.ViewHolder>  {
    private List<PropertyGroupsEntity> groupList;
    private Context mContent;
    private MyPropertyInfoAdapter mProperAdapter;
    private List<PropertyGroupsEntity.PropertyInfos> infosList;

    public MyPropetyGroupsAdapter(Context mContext ,List<PropertyGroupsEntity> groupList) {
        this.mContent=mContext;
        if(groupList!=null)
            this.groupList = groupList;
        else
            this.groupList=new ArrayList<>();
    }
    @Override
    public int getItemCount() {
        return groupList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ipprove_details_recycler, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PropertyGroupsEntity proGroupEntity = groupList.get(position);
        holder.groupName.setText(proGroupEntity.getGroupName());
        infosList=proGroupEntity.getPropertyInfos();
        if(infosList !=null && infosList.size()!=0){
            mProperAdapter = new MyPropertyInfoAdapter(mContent,infosList);
            holder.groupRecycler.setAdapter(mProperAdapter);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView groupName; // 分组的头部信息
        public RecyclerView groupRecycler;  //分组的具体信息
        public ViewHolder(View itemView) {
            super(itemView);
            groupName = (AppCompatTextView) itemView.findViewById(R.id.tv_approve_group_name);
            groupRecycler = (RecyclerView) itemView.findViewById(R.id.approve_details_recycler);
            groupRecycler.setLayoutManager(new LinearLayoutManager(mContent,LinearLayoutManager.VERTICAL,false));
            groupRecycler.addItemDecoration( new HorizontalDividerItemDecoration.Builder(mContent)
                    .colorResId(R.color.line_color).marginResId(R.dimen.left_16, R.dimen.right_0) //记录左右的距离
                    .build());
        }
    }
}
