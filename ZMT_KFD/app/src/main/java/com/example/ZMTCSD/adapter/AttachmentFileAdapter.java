package com.example.ZMTCSD.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.activity.PhotoViewActivity_;
import com.example.ZMTCSD.activity.WebViewActivity_;
import com.example.ZMTCSD.entity.FileInfoEntity;
import com.example.ZMTCSD.utils.IntentUtil;
import com.example.ZMTCSD.utils.StringUtil;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 *  文件的适配器
 */

public class AttachmentFileAdapter extends SwipeMenuAdapter<AttachmentFileAdapter.ViewHolder> {
    private Context mContent;
    private List<FileInfoEntity> filelist;
    private OnFileClickListener mOnFileClickListener;

    public void addmOnFileClickListener(OnFileClickListener mOnFileClickListener) {
        this.mOnFileClickListener = mOnFileClickListener;
    }

    public AttachmentFileAdapter(Context content, List<FileInfoEntity> filelist) {
        this.mContent=content;
        if(filelist!=null ){
            this.filelist = filelist;
        }else{
            this.filelist=new ArrayList<>();
        }

    }

    @Override
    public int getItemCount() {
        return filelist.size();
    }

    @Override
    public View onCreateContentView(ViewGroup viewGroup, int viewType) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ipprove_attachment_recycler, viewGroup, false);
    }

    @Override
    public ViewHolder onCompatCreateViewHolder(View view, int viewType) {
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public AppCompatImageView image;
        public AppCompatTextView name;
        public AppCompatTextView update;
        public LinearLayout file_content;
        public ViewHolder(View itemView) {
            super(itemView);
            image = (AppCompatImageView) itemView.findViewById(R.id.file_image);
            name = (AppCompatTextView) itemView.findViewById(R.id.file_Name);
            update = (AppCompatTextView) itemView.findViewById(R.id.file_update);
            file_content = (LinearLayout) itemView.findViewById(R.id.file_ll_content);
            file_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FileInfoEntity entity=filelist.get(getLayoutPosition());
                    LogUtils.d("文件"+entity.toString());
                    String path = entity.getFileUrl();
                    String name=entity.getFileName();
                    String FileType="";
                    if(!TextUtils.isEmpty(path))
                        FileType=path.substring(path.lastIndexOf(".")+1);

                    //TODO 这里进行文件的下载和查看
                    if(IntentUtil.FileWhatType(FileType) == 0){
                        PhotoViewActivity_.intent(mContent).extra(AppDelegate.DETAILS_PHOTOVIEW,path).start();
                    }else if(IntentUtil.FileWhatType(FileType)== -1){
                        WebViewActivity_.intent(mContent).extra(AppDelegate.DETAILS_WEBVIEW_NAME,name).extra(AppDelegate.DETAILS_WEBVIEW,path).start();
                    }else{
                        if(mOnFileClickListener !=null){
                            mOnFileClickListener.onItemClick(path,name,FileType);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final FileInfoEntity fileEntity = filelist.get(position);
        String FileType="";
        if(!TextUtils.isEmpty(fileEntity.getFileUrl()))
            FileType=fileEntity.getFileUrl().substring(fileEntity.getFileUrl().lastIndexOf(".")+1);

        switch (IntentUtil.FileWhatType(FileType)) {
            case 0:
                viewHolder.image.setImageResource(R.mipmap.ic_approval_file_image3);
                break;
            case 1:
                viewHolder.image.setImageResource(R.mipmap.ic_approval_file_xls3);
                break;
            case 2:
                viewHolder.image.setImageResource(R.mipmap.ic_approval_file_doc3);
                break;
            case 3:
                viewHolder.image.setImageResource(R.mipmap.ic_approval_file_pdf3);
                break;
            case 4:
                viewHolder.image.setImageResource(R.mipmap.ic_approval_file_zip3);
                break;
            default:
                viewHolder.image.setImageResource(R.mipmap.ic_approval_file_default3);
                break;
        }
        viewHolder.name.setText(fileEntity.getFileName());
        String date = StringUtil.dateRemoveT(fileEntity.getUploadDate());
        viewHolder.update.setText(StringUtil.StringToNull(fileEntity.getFileSubTitle()));
    }

    //下载的接口
    public interface OnFileClickListener {
        void onItemClick(String  path ,String name ,String type);
    }

}
