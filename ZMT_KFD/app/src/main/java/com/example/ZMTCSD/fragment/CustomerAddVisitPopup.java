package com.example.ZMTCSD.fragment;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.ZMTCSD.R;
import com.flyco.dialog.widget.popup.base.BasePopup;

/**
 *  客户中的访问记录中的添加记录的popup
 */

public class CustomerAddVisitPopup extends BasePopup<CustomerAddVisitPopup> {
    private Context mContent;
    private AppCompatTextView cancel ;
    private AppCompatTextView send ;
    private AppCompatEditText edMessage;
    private AddVisitClickListener mListener;
    CustomerAddVisitPopup mPopup;
    public CustomerAddVisitPopup(Context context) {
        super(context);
        this.mContent=context;
//        this.mListener= (AddVisitClickListener) context;
    }

    @Override
    public View onCreatePopupView() {
        final View inflate = View.inflate(mContext, R.layout.customer_addvisit, null);
        cancel=(AppCompatTextView) inflate.findViewById(R.id.tv_addVisit_cancel);
        send=(AppCompatTextView) inflate.findViewById(R.id.tv_addVisit_send);
        edMessage= (AppCompatEditText) inflate.findViewById(R.id.ed_addVisit_content);

        return inflate;
    }

    @Override
    public void setUiBeforShow() {
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
                    send.setTextColor(mContent.getResources().getColor( R.color.black_one_mark_87));
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
    }


    public AddVisitClickListener getmListener() {
        return mListener;
    }
    public void setmListener(AddVisitClickListener mListener) {
        this.mListener = mListener;
    }
    // 接口
    public interface AddVisitClickListener {
        void OnAddVisitClickListener(View v,String str); //
    }


}
