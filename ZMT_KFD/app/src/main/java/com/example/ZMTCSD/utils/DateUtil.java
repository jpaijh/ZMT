package com.example.ZMTCSD.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;

import com.example.ZMTCSD.entity.ContactEntity;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DateUtil {

    /**
     * 将long类型转化成字符串格式 MM月dd日
     *
     * @param time
     * @return 返回短时间字符串格式MM月dd日
     */
    public static String longDateToStrMD(long time) {
        Date currentTime = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 将long类型转化成字符串格式 MM月dd日HH:mm:ss
     *
     * @param time
     * @return 返回短时间字符串格式 MM月dd日HH:mm:ss
     */
    public static String longDateToStrMDHMS(long time) {
        Date currentTime = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    /**
     * 将long类型转化成字符串格式yyyy年MM月dd日
     *
     * @param time
     * @return 返回短时间字符串格式yyyy年MM月dd日
     */
    public static String longDateToStrYMD(long time) {
        Date currentTime = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 将long类型转化成字符串格式yyyy年MM月dd日 HH:mm:ss
     *
     * @param time
     * @return 返回短时间字符串格式yyyy年MM月dd日 HH:mm:ss
     */
    public static String longDateToStrYMDHMS(long time) {
        Date currentTime = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    /**
     * 将短时间格式时间转换为字符串 yyyy-MM-dd
     *
     * @param dateDate
     * @return
     */
    public static String dateToStr(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
     *
     * @param dateDate
     * @return
     */
    public static String dateToStrLong(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(dateDate);
        return dateString;
    }


    /**
     * 打电话
     */
    public static void DoCallDialog(final Context mContext, ContactEntity contactEntity){
        String s=contactEntity.getName()+"  "+contactEntity.getPhoneNumber();
//        LogUtils.e(  s.split("  ")[0]+"dddd"+s.split("  ")[1]);
        final String[] stringItems ={s };
        final ActionSheetDialog dialog = new ActionSheetDialog(mContext, stringItems, null);
        dialog.title("消息提示 (拨打电话)")//
                .titleTextSize_SP(16f).show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + stringItems[position].toString().split("  ")[1] ) ;
                intent.setData(data);
                mContext.startActivity(intent);
                dialog.dismiss();
            }
        });
    }

    public static void DoCallDialog (final Context mContext, List<ContactEntity> EntityList){
        List<String> listStr=new ArrayList<>();
        for(int i=0;i<EntityList.size();i++){
            listStr.add(EntityList.get(i).getName()+"  "+EntityList.get(i).getPhoneNumber());
        }
       final  String[] stringItems = listStr.toArray(new String[] {});
//        LogUtils.e(  stringItems[0].toString().split("  ")[0]+"dddd"+stringItems[0].toString().split("  ")[1]);
        final ActionSheetDialog dialog = new ActionSheetDialog(mContext, stringItems, null);
        dialog.title("消息提示\r\n(拨打电话)")//
                .titleTextSize_SP(16f).show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + stringItems[position].toString().split("  ")[1] ) ;
                intent.setData(data);
                mContext.startActivity(intent);
                dialog.dismiss();
            }
        });
    }

}
