package com.example.ZMTCSD.server;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.WebHistoryItem;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.MyApplication_;
import com.example.ZMTCSD.dal.MetaDataDal;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.entity.UserLoginEntity;
import com.example.ZMTCSD.utils.VolleyErrorHelper;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

/**
 *  开启服务，
 */
@EService
public class TaskService extends Service {
    public static final String ACTION = "com.zmt_kfd.service.TaskService";
    private Context mContent;
    private RequestQueue mRequestQueue;
    private MyCount mc;
    private long Unsecond;
    private SharedPreferences sp_UserInfo;
    private SharedPreferences.Editor edit_UserInfo;
    private  long endTime;//当前时间
    ScheduledExecutorService executorService;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mContent=TaskService.this;
        mRequestQueue= Volley.newRequestQueue(mContent);
        sp_UserInfo = MyApplication_.getInstance().getUserInfoSp();
        edit_UserInfo = sp_UserInfo.edit();
//        executorService = new ScheduledThreadPoolExecutor(20);

        super.onCreate();
    }
    @Override
    public void onStart(Intent intent, int startId) {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Unsecond = MoreUserDal.GetSecond() ;
//        endTime = (System.currentTimeMillis() / 1000);
//        long unsend= MoreUserDal.GetSecond() +MoreUserDal.GetExpiresin();
//        LogUtils.d("时间"+Unsecond);

        mc=new MyCount(500000*1000 , 500*1000);
        mc.start();
//        PollingThread task = new PollingThread();
        //1秒后开始执行任务，以后每隔3秒执行一次
//        executorService.scheduleWithFixedDelay(task, 1000, 3000, TimeUnit.MILLISECONDS);

        return super.onStartCommand(intent, flags, startId);
    }
    private boolean isRefreshToken=false;
    class mythred extends Thread{
        @Override
        public void run() {
            endTime = (System.currentTimeMillis() / 1000);
            if(endTime> Unsecond){
                isRefreshToken=true;
            }
            while (true){
                onRefreshtoken();
            }

        }
    }

//    class PollingThread extends Thread {
//        @Override
//        public void run() {
//            try {
//                endTime=(System.currentTimeMillis() / 1000);
//                if(endTime >Unsecond ){
//                    Unsecond=endTime+20;
//                    LogUtils.d("时间"+Unsecond);
//                }
//            }catch (Throwable e){
//
//            }
//        }
//    }

    class MyCount extends CountDownTimer {

        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
//            LogUtils.e("long"+l);
            onMetaData();
        }

        @Override
        public void onFinish() {
            mc.start();
        }
    }

    @Background
    void onMetaData(){
        final String url = MoreUserDal.GetServerUrl() + "/api/resource/metadata";
        LogUtils.d("刷新元数据" + url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<org.json.JSONArray>() {
            @Override
            public void onResponse(org.json.JSONArray response) {
                String  jsonString= MetaDataDal.getMetaData();
                if(!TextUtils.isEmpty(jsonString)  || !jsonString.equals(response.toString())){
                    edit_UserInfo.putString(AppDelegate.SP_META_DATE,response.toString()).commit();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d(VolleyErrorHelper.getMessage(error, mContent));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "bearer " + MoreUserDal.GetAccessToken());
                return map;
            }
            @Override
            public Priority getPriority() {
                return super.getPriority();
            }
        };
        jsonArrayRequest.setTag(this);
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 3, 1.0f));
        mRequestQueue.add(jsonArrayRequest);

    }

    @Background
    void onRefreshtoken(){
        final String url_RefreshToken = MoreUserDal.GetServerUrl() + "/api/OAuth/Token";
        LogUtils.d("刷新token" + url_RefreshToken);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url_RefreshToken, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                UserLoginEntity userLoginEntity = JSON.parseObject(response.toString(), UserLoginEntity.class);
                // 登陆成功：保存用户名
                long second = (System.currentTimeMillis() / 1000);
                LogUtils.e("更新前" + MoreUserDal.GetAccessToken());
                MoreUserDal.UpdateMoreUser(userLoginEntity, second);
                Unsecond =  second+30;

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d("刷新失败" +  VolleyErrorHelper.getMessage(error,mContent) );
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
            @Override
            public byte[] getBody() {
                String string = "grant_type=refresh_token&refresh_token=" + MoreUserDal.GetRefreshToken();
                return string.getBytes();
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        jsonObjectRequest.setTag(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50 * 1000, 1, 1.0f));
        mRequestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onDestroy() {
        mc.cancel();
        super.onDestroy();
    }
}
