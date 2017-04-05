package com.example.ZMTCSD.utils;

import android.text.InputFilter;
import android.text.Spanned;

import com.apkfuns.logutils.LogUtils;

/**
 * EditText的选择小数点的筛选器
 */

public class MyInputFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String oldStr = dest.toString();
        String newStr=source.toString();
//        LogUtils.e(oldStr+"::"+newStr);
        //如果第一位输入小数点则自动补零,变成0.
        if (oldStr.length() == 0 && newStr.equals(".") ) {
            return "0.";
        }
        if(oldStr!=null && oldStr.contains(".") &&newStr.contains(".")){
            return "";
        }
        return null;
    }
}
