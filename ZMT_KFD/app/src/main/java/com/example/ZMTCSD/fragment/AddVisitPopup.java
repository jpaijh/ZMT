package com.example.ZMTCSD.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.example.ZMTCSD.R;


/**
 *  客户中的访问记录中的添加记录的popup
 */

public class AddVisitPopup extends PopupWindow {
    private Context mContext;
    private AppCompatTextView cancel ;
    private AppCompatTextView send ;
    private AppCompatEditText edMessage;
    private AddVisitClickListener mListener;
    public AddVisitPopup( Context mContent) {
        this.mContext = mContent;
        //设置可以获得焦点
        setFocusable(true);
        //设置弹窗内可点击
        setTouchable(true);
        //设置弹窗外可点击
        setOutsideTouchable(true);

        //设置弹窗的宽度和高度
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        backgroundAlpha((Activity) mContent,0.5f);//0.0-1.0
        ColorDrawable dw=new ColorDrawable();
        setBackgroundDrawable(new ColorDrawable());
        //设置弹出窗体需要软键盘
        setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        //设置模式，和Activity的一样，覆盖，调整大小。
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View inflate=LayoutInflater.from(mContext).inflate(R.layout.customer_addvisit, null);
        //设置弹窗的布局界面
        setContentView(inflate);

        cancel=(AppCompatTextView) inflate.findViewById(R.id.tv_addVisit_cancel);
        send=(AppCompatTextView) inflate.findViewById(R.id.tv_addVisit_send);
        edMessage= (AppCompatEditText) inflate.findViewById(R.id.ed_addVisit_content);

        initOnClick();
    }

    private void initOnClick() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        edMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edMessage.length() !=0){
                    send.setEnabled(true);
                    send.setTextColor(mContext.getResources().getColor( R.color.black_one_mark_87));
                }else{
                    send.setEnabled(false);
                    send.setTextColor(mContext.getResources().getColor( R.color.black_three_mark_38));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener !=null){
                    String name=edMessage.getText().toString().trim();
                    mListener.OnAddVisitClickListener(v,name);
                    dismiss();
                }
            }
        });

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha((Activity) mContext,1f);//0.0-1.0
                dismiss();
            }
        });
    }

    public void setmListener(AddVisitClickListener mListener) {
        this.mListener = mListener;
    }

    // 接口
    public interface AddVisitClickListener {
        void OnAddVisitClickListener(View v,String str); //
    }
    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha)
    {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
//        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

}
