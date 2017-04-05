package com.example.ZMTCSD.utils;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.NumberKeyListener;
import android.widget.EditText;

import java.text.DecimalFormat;

/**
 * Created by junping on 2017/1/9.
 */

public class InputFilterUtils implements InputFilter {
    private static final DecimalFormat format=new DecimalFormat("###,###");
    private static final int POINT_LENGHT=2;
    private EditText mEditText;
    private Context mContext;
    private boolean isPoint;

    /**
     *
     * @param context
     * @param editText
     * @param isFloat:是否可以输入小数
     */
    public InputFilterUtils(Context context,EditText editText,boolean isFloat) {
        mContext=context;
        mEditText=editText;
        isPoint=isFloat;
        init();
    }

    /**
     * 限制EditText可接收的字符
     * 但非法字符依然可以输入,只是不显示在EditText上而已,所以下面依旧要做判断
     */
    private void init() {
        mEditText.setKeyListener(new NumberKeyListener() {
            @Override
            protected char[] getAcceptedChars() {
                if(isPoint){
                    return new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0','.'};
                }
                return new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
            }

            @Override
            public int getInputType() {
                return InputType.TYPE_CLASS_NUMBER;
            }
        });
    }

    /**
     *通过过滤器实现金额的格式化,并限制小数位数
     * @param source   :新输入的字符串
     * @param start    :新输入的字符串起始下标，一般为0
     * @param end       :新输入的字符串终点下标，一般为source长度-1
     * @param dest      :输入之前文本框内容
     * @param dstart    :原内容起始坐标，一般为0
     * @param dend      :原内容终点坐标，一般为dest长度-1
     * @return
     */
    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {

        String oldStr = dest.toString();
        String newStr=source.toString();

        // 删除,特殊字符，直接返回null
        // "".equals(source.toString():不添加会出现删除不了内容的情况
        if (newStr==null  || "".equals(newStr)) {
            return null;
        }
        if(oldStr!=null && oldStr.contains(",")){
            oldStr = oldStr.replace(",", "");
        }
        //输入非法字符,-,/等等
        if(isPoint){
            if(!newStr.contains("1")&&!newStr.contains("2")&&!newStr.contains("3")&&!newStr.contains("4")&&!newStr.contains("5")&&
                    !newStr.contains("6")&&!newStr.contains("7")&&!newStr.contains("8")&&!newStr.contains("9")&&!newStr.contains("0")
                    &&!newStr.contains(".")){
                return "";
            }
        }else{
            if(!newStr.contains("1")&&!newStr.contains("2")&&!newStr.contains("3")&&!newStr.contains("4")&&!newStr.contains("5")&&
                    !newStr.contains("6")&&!newStr.contains("7")&&!newStr.contains("8")&&!newStr.contains("9")&&!newStr.contains("0")){
                return "";
            }
        }

        //如果第一位输入小数点则自动补零,变成0.
        if (oldStr.length() == 0 && newStr.equals(".") && isPoint) {
            return "0.";
        }
        //限制只能输入一个小数点,且小数点后限制保留两位
        if(oldStr!=null && isPoint) {
            if(oldStr.contains(".")){
                //只能输入一个小数点
                if (newStr.contains(".")) {
                    return "";
                }
                //以小数点为界分割字符串
                String[] splitArray = oldStr.split("\\.");
                if (splitArray.length > 1) {
                    //得到小数点后的字符串
                    String dotValue = splitArray[1];
                    if (dotValue.length() == POINT_LENGHT) {
                        return "";
                    }
                }
            }
            //此处不设置的话在格式化金额后无法输入小数点,
            //表示技术太渣,至今并不知道原因
            if(newStr!=null && newStr.contains(".")){
                return newStr;
            }
        }
        //格式化金额,三位一断
        if (oldStr!=null && oldStr.length() > 2
                && !oldStr.contains(".") && !oldStr.contains(",")) {
            String newValue = oldStr + source;
            String amountStr = format.format(Double.parseDouble(newValue));
            mEditText.setText("");
            mEditText.setText(amountStr);
            mEditText.setSelection(amountStr.length());
            return amountStr;
        } else {
            return null;
        }
    }

}
