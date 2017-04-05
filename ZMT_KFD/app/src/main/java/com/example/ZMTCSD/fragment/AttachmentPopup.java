package com.example.ZMTCSD.fragment;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ZMTCSD.R;
import com.example.ZMTCSD.entity.CustomerDetailsEntity;
import com.flyco.dialog.widget.popup.base.BasePopup;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

/**
 *  附件的 popup
 */

public class AttachmentPopup  extends BasePopup<AttachmentPopup> {
    private OnButtonClickListener mListener;
    private RecyclerView mRecycler;
    private MenuListAdapter mAdapter;
    private List<CustomerDetailsEntity.fileGroups> FileList;

    public AttachmentPopup(Context context ,List<CustomerDetailsEntity.fileGroups> FileList) {
        super(context);
        this.mListener = (AttachmentPopup.OnButtonClickListener) context;
        this.FileList=FileList;
    }

    @Override
    public View onCreatePopupView() {
        View inflate = View.inflate(mContext, R.layout.popup_view_menu, null);
        mRecycler= (RecyclerView) inflate.findViewById(R.id.recycler);
        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        mRecycler.setHasFixedSize(true);
        mRecycler.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .colorResId(R.color.line_color).marginResId(R.dimen.left_16, R.dimen.right_0).showLastDivider() //记录左右的距离
                .build());
        mAdapter=new MenuListAdapter(FileList);
        mRecycler.setAdapter(mAdapter);
    }

    public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.ViewHolder>{

        List<CustomerDetailsEntity.fileGroups> fileList;

        public MenuListAdapter(List<CustomerDetailsEntity.fileGroups> fileList) {
            this.fileList = fileList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_popup_menu, viewGroup, false);
            return new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            CustomerDetailsEntity.fileGroups files=fileList.get(position);
            holder.tv_popup.setText(files.getFileCategoryName());
        }
        @Override
        public int getItemCount() {
            return fileList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            AppCompatTextView tv_popup;
            public ViewHolder(View itemView) {
                super(itemView);
                tv_popup= (AppCompatTextView) itemView.findViewById(R.id.tv_item_popup);
                tv_popup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mListener !=null){
                            CustomerDetailsEntity.fileGroups entity=fileList.get(getLayoutPosition());
                            mListener.OnButtonClickListener(v,entity.getFileCategoryId(),entity.getFileCategoryName()); //调用接口
                            dismiss();
                        }
                    }
                });
            }
        }
    }

    public OnButtonClickListener getmListener() {
        return mListener;
    }

    public void setmListener(OnButtonClickListener mListener) {
        this.mListener = mListener;
    }
    // 接口
    public interface OnButtonClickListener {
        void OnButtonClickListener(View v,int fileId,String fileName); //
    }
}
