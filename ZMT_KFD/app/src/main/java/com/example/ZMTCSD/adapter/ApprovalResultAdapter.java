package com.example.ZMTCSD.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.alorma.timeline.RoundTimelineView;
import com.bumptech.glide.Glide;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.entity.ApprovalResultEntity;
import com.example.ZMTCSD.entity.ContactEntity;
import com.example.ZMTCSD.utils.StringUtil;

import java.util.List;

/**
 * 审批结果的adapter
 */
public class ApprovalResultAdapter extends RecyclerView.Adapter<ApprovalResultAdapter.ViewHolder> {
    private List<ApprovalResultEntity> resultlist;
    private Context mContext;

    public ApprovalResultAdapter(List<ApprovalResultEntity> resultlist, Context context) {
        this.resultlist = resultlist;
        this.mContext = context;
        mListener = (OnButtonClickListener) context;
    }

    @Override
    public int getItemCount() {
        return resultlist.size();
    }

    @Override
    public int getItemViewType(int position) {
        final int size = resultlist.size() - 1;
        if (position == 0)
            return ItemType.START;
        else if (position == size)
            return ItemType.END;
        else return ItemType.NORMAL;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_iapprove_approvallog_recycler, viewGroup, false);
        ViewHolder vh = new ViewHolder(view,viewType );
        return vh;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RoundTimelineView timeline;
        public View line;
        public AppCompatTextView tv_name;
        public AppCompatTextView tv_remark;
        public AppCompatTextView tv_time;
        public AppCompatImageView img_call;

        public ViewHolder(final View itemView,int type) {
            super(itemView);
            timeline = (RoundTimelineView) itemView.findViewById(R.id.timeline_approvalLog);
            line = itemView.findViewById(R.id.view_approvalLog);
            tv_name = (AppCompatTextView) itemView.findViewById(R.id.tv_approvalLog_name);
            tv_remark = (AppCompatTextView) itemView.findViewById(R.id.tv_approvalLog_remark);
            tv_time = (AppCompatTextView) itemView.findViewById(R.id.tv_approvalLog_time);
            img_call = (AppCompatImageView) itemView.findViewById(R.id.img_approvalLog_call);
            if (type == ItemType.START) {
                timeline.setTimelineType(RoundTimelineView.TYPE_START);
            } else if (type == ItemType.END) {
                timeline.setTimelineType(RoundTimelineView.TYPE_END);
            }
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final ApprovalResultEntity resultEntity = resultlist.get(position);

        if(resultEntity.getResult() ==1){
            Glide.with(mContext).load(R.mipmap.ic_approval_result_check3).into(viewHolder.timeline);
            viewHolder.line.setBackgroundColor(mContext.getResources().getColor(R.color.details_Approval));
        }else if(resultEntity.getResult() ==-1){
            Glide.with(mContext).load(R.mipmap.ic_approval_result_cancel3).into(viewHolder.timeline);
            viewHolder.line.setBackgroundColor(mContext.getResources().getColor(R.color.details_UnApproval));
        }


        if (resultEntity.getContacts() != null && resultEntity.getContacts().size()!=0 ) {
            viewHolder.img_call.setVisibility(View.VISIBLE);
            viewHolder.img_call.setTag(resultEntity.getContacts());
        }

        viewHolder.img_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
//                    //注意这里使用getTag方法获取数据
                    mListener.OnButtonClickListener((List<ContactEntity>) v.getTag());
                }
            }
        });


        //对为 null 或者“null”
        if ("null".equals(resultEntity.getApprovalName()) || null == resultEntity.getApprovalName()) {
            viewHolder.tv_name.setText(resultEntity.getNodeName());
        } else {
            viewHolder.tv_name.setText(resultEntity.getApprovalName() + resultEntity.getNodeName());
        }

        if ("null".equalsIgnoreCase(resultEntity.getRemark()) || null == resultEntity.getRemark()) {
            if (resultEntity.getResult() ==1) {
                viewHolder.tv_remark.setText("同意");
            } else {
                viewHolder.tv_remark.setText("不同意");
            }
        } else {
            viewHolder.tv_remark.setText(resultEntity.getRemark());
        }
        viewHolder.tv_time.setText(StringUtil.dateRemoveT(resultEntity.getApproveDate()));
    }

    private OnButtonClickListener mListener;

    public OnButtonClickListener getmListener() {
        return mListener;
    }

    public void setmListener(OnButtonClickListener mListener) {
        this.mListener = mListener;
    }

    // 接口
    public interface OnButtonClickListener {
        void OnButtonClickListener( List<ContactEntity> entityList); //message为你要传入activity中的信息
    }

    public class ItemType {
        public final static int NORMAL = 0;
        public final static int START = 1;
        public final static int END = 2;
        public final static int ATOM = -1;
    }
}

