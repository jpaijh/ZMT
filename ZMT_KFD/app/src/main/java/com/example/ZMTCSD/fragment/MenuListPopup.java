package com.example.ZMTCSD.fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.example.ZMTCSD.R;
import com.example.ZMTCSD.entity.CustomerDetailsEntity;

import java.util.List;

/**
 *  list的popupwindow
 */
public class MenuListPopup extends PopupWindow {
    private Context mContext;
    private View view;
    private RecyclerView  mRecycler;
    private MenuListAdapter mAdapter;
    public MenuListPopup(Context context ,List<CustomerDetailsEntity.fileGroups> Filelist) {
//        super(context);
        this.mContext=context;
        this.mListener = (OnButtonClickListener) context;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.popup_view_menu, null);
        mRecycler= (RecyclerView) view.findViewById(R.id.recycler);

        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter=new MenuListAdapter(Filelist);
        mRecycler.setAdapter(mAdapter);
        this.view.setFocusableInTouchMode(true);//触摸是否能获得焦点
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        //指定透明背景，back键相关
        this.setBackgroundDrawable(new ColorDrawable());
        this.setFocusable(true);
        this.setOutsideTouchable(true);//// 触摸popupwindow外部，popupwindow消失

        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int height = view.findViewById(R.id.recycler).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
        this.view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });

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
            holder.tv_popup.setText(files.getFileCategoryName()+"让腿f的三个地方局");

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
                        //打开文件管理器
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

    private OnButtonClickListener mListener;


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
