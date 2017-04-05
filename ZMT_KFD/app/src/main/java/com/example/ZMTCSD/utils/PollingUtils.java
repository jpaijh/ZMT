package com.example.ZMTCSD.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import com.apkfuns.logutils.LogUtils;

/**
 * 轮询工具类
 */

public class PollingUtils {
    //开启轮询服务
    public static void startPollingService(Context context, int seconds, Class<?> cls, String action) {
        //获取AlarmManager系统服务
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        //包装需要执行Service的Intent
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //触发服务的起始时间
        long triggerAtTime = SystemClock.elapsedRealtime();//返回系统启动到现在的毫秒数，包含休眠时间。
//        long triggerAtTime=SystemClock.currentThreadTimeMillis();
        LogUtils.d("startPollingService: "+triggerAtTime);

        // 这里要注意，如果API>=19，就不能再使用setRepeating，应该改为setWindow
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            LogUtils.d("startPollingService: 111");
//            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, seconds * 1000, pendingIntent);
//        }else
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LogUtils.d("startPollingService: ");
            manager.setExact(AlarmManager.RTC, triggerAtTime+seconds * 1000, pendingIntent);
//            manager.setWindow(AlarmManager.RTC_WAKEUP, triggerAtTime+ seconds * 1000,
//                    seconds * 1000, pendingIntent);
        } else {
            LogUtils.d("startPollingService: 222");
            // 使用AlarmManger的setRepeating方法设置定期执行的时间间隔（seconds秒）和需要执行的Service
            manager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtTime,
                    seconds * 1000, pendingIntent);
        }
    }
    //停止轮询服务
    public static void stopPollingService(Context context, Class<?> cls, String action) {
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //取消正在执行的服务
        manager.cancel(pendingIntent);
    }
}
