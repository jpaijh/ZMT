package com.example.ZMTCSD.utils;

import android.content.Context;
import android.view.Gravity;

import com.example.ZMTCSD.R;
import com.flyco.animation.BounceEnter.BounceEnter;
import com.flyco.animation.ZoomExit.ZoomInExit;
import com.flyco.dialog.widget.NormalDialog;
import com.flyco.dialog.widget.base.BaseDialog;

/**
 *  对话框
 */

public class MyDialog  {
    public MyDialog(Context mContent, String Title , String content, int titleColor) {

        MyDialogshow( mContent,  Title ,  content,  titleColor);
    }

    public BaseDialog MyDialogshow(Context mContent, String Title , String content, int titleColor) {
        final NormalDialog dialog = new NormalDialog(mContent);
        dialog.title(Title)
                .titleTextColor(titleColor)
                .titleTextSize(18)//
                .content(content)
                .contentGravity(Gravity.CENTER)
                .style(NormalDialog.STYLE_TWO)//
                .btnText("取消", "提交")
                .isTitleShow(true)
                .showAnim(new BounceEnter())//
                .dismissAnim(new ZoomInExit());
        return dialog;
    }


}
