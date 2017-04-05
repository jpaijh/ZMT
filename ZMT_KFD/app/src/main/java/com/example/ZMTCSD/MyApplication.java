package com.example.ZMTCSD;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.apkfuns.logutils.LogLevel;
import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.server.TaskService;
import com.example.ZMTCSD.server.TaskService_;
import com.example.ZMTCSD.utils.PollingUtils;
import com.pgyersdk.crash.PgyCrashManager;

import org.androidannotations.annotations.EApplication;
import java.util.LinkedList;
import java.util.List;

import static com.example.ZMTCSD.dal.ServerDal.SaveListServer;

@EApplication
public class MyApplication extends Application {

    private List<Activity> activityList = new LinkedList<>();
    private static MyApplication instance;

    /**
     * 获取用户信息SharedPreferences
     */
    public SharedPreferences getUserInfoSp() {
        return getSharedPreferences(AppDelegate.SP_USER_INFO, Context.MODE_PRIVATE);
    }

    /**
     * 获取用户信息SharedPreferences.Editor
     */
    public SharedPreferences.Editor getUserInfoEditor() {
        return getUserInfoSp().edit();
    }

    /**
     * 获取搜索记录SharedPreferences
     */
    public SharedPreferences getSearchHistorySp() {
        return getSharedPreferences(AppDelegate.SP_SEARCH_HISTORY, Context.MODE_PRIVATE);
    }

    /**
     * 获取搜索记录SharedPreferences.Editor
     */
    public SharedPreferences.Editor getSearchHistoryEdit() {
        return getSearchHistorySp().edit();
    }

    // 单例模式中获取唯一的MyApplication实例
    public static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;
    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    /**
     * 遍历所有Activity并finish，但是不关闭应用
     * （这个方法主要是用于 修改密码和退出登录后 跳转登陆界面，需关闭掉被addActivity（Activity activity）方法添加进activityList的Activity）
     * 不然按返回键会回到之前界面
     */
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.getLogConfig()
                .configAllowLog(true)
                .configTagPrefix("MyAppName")
                .configShowBorders(true)
                .configFormatTag("%d{HH:mm:ss:SSS} %t %c{-5}")
                .configLevel(LogLevel.TYPE_VERBOSE);

        PgyCrashManager.register(this); // 初始化蒲公英（注册Crash接口）
        SaveListServer();//开始保存服务器

//        bindService();
    }

    private void bindService(){
        LogUtils.i( "bindService()");
        Intent intent = new Intent(MyApplication.this,TaskService_.class);
        startService(intent);
    }

    private void unBindService(){
        LogUtils.i( "unBindService()");
        Intent intent = new Intent(MyApplication.this,TaskService_.class);
        stopService(intent);
    }

    public  void initService() {
        PollingUtils.startPollingService(this, 20, TaskService_.class, TaskService.ACTION);
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
//        unBindService();
        super.onTerminate();
    }
}
