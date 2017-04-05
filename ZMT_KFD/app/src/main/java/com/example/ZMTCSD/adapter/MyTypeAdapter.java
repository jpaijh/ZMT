package com.example.ZMTCSD.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.entity.PaymentDataEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 *
 */

public class MyTypeAdapter extends RecyclerView.Adapter<MyTypeAdapter.ViewHolder> {
    private Context mContent;
    private String Type;
    private OnTypeClickListener mTypeClick;
    private Serializable lists;
    private List<PaymentDataEntity.PaymentMethodList> meodList=new ArrayList<>();

    public void AddmTypeClick(OnTypeClickListener mTypeClick) {
        this.mTypeClick = mTypeClick;
    }

    public MyTypeAdapter(Context mContent, String type, Serializable ser) {
        this.mContent = mContent;
        this.Type = type;
        this.lists= ser;
        LogUtils.e(""+Type);
        if("methodtype".equals(Type) ) {
            meodList= (List<PaymentDataEntity.PaymentMethodList>) lists;

        }

    }

    @Override
    public MyTypeAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_customer_server_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyTypeAdapter.ViewHolder holder, int position) {
        if(Type.equals(ItemType.paymentMethods)){
            PaymentDataEntity.PaymentMethodList  method= meodList.get(position);
            holder.name.setText( method.getName());
        }

    }

    @Override
    public int getItemCount() {
        int  count=0;
        switch (Type){
            case ItemType.paymentMethods:
                count=meodList.size();
            break;
            case ItemType.guaranteeTypes:
            break;
        }
        LogUtils.e("size:"+count);
        return count;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView name;
        private AppCompatImageView tick;
        private LinearLayout item;
        public ViewHolder(View itemView) {
            super(itemView);

            item= (LinearLayout) itemView.findViewById(R.id.ll_item);
            name= (AppCompatTextView) itemView.findViewById(R.id.tv_item_name);
            tick= (AppCompatImageView) itemView.findViewById(R.id.img_item_tick);
        }
    }

    //接口
    public interface OnTypeClickListener {
        void onTypeClick(int  position ,String name);
    }

    public class ItemType {
        public final static String paymentMethods = "methodtype";
        public final static String guaranteeTypes = "";
//        public final static String END = 2;
//        public final static String ATOM = -1;
    }

}
