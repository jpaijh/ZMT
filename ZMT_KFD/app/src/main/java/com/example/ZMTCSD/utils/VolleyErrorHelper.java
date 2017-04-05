package com.example.ZMTCSD.utils;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.activity.LoginActivity;

/**
 * Created by 汤军平 on 2016/8/10.
 * //正如前面代码看到的，在创建一个请求时，需要添加一个错误监听onErrorResponse。如果请求发生异常，
 * 会返回一个VolleyError实例。
 //以下是Volley的异常列表：
 //AuthFailureError：如果在做一个HTTP的身份验证，可能会发生这个错误。
 //NetworkError：Socket关闭，服务器宕机，DNS错误都会产生这个错误。
 //NoConnectionError：和NetworkError类似，这个是客户端没有网络连接。
 //ParseError：在使用JsonObjectRequest或JsonArrayRequest时，如果接收到的JSON是畸形，会产生异常。
 //SERVERERROR：服务器的响应的一个错误，最有可能的4xx或5xx HTTP状态代码。
 //TimeoutError：Socket超时，服务器太忙或网络延迟会产生这个异常。默认情况下，Volley的超时时间为2.5秒。
 如果得到这个错误可以使用RetryPolicy。
 */
public class VolleyErrorHelper {
    /**
     * Returns appropriate message which is to be displayed to the user against
     * 返回适当的消息是显示给用户
     * the specified error object.
     * @param error
     * @param context
     * @return
     */
    public static String getMessage(Object error, Context context) {
        if (error instanceof TimeoutError) {
            return context.getResources().getString(R.string.TimeoutError);
        } else if (isServerProblem(error)) {
            return handleServerError(error, context);
        } else if (isNetworkProblem(error)) {
            return context.getResources().getString(R.string.NoConnectionError);
        }
        return context.getResources().getString(R.string.generic_error);
    }

    /**
     * Determines whether the error is related to network
     *是否与网络相关的错误
     * @param error
     * @return
     */
    private static boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError)
                || (error instanceof NoConnectionError);
    }

    /**
     * Determines whether the error is related to server
     *是否与服务器错误
     * @param error
     * @return
     */
    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError)
                || (error instanceof AuthFailureError);
    }

    /**
     * Handles the server error, tries to determine whether to show a stock
     * message or to show a message retrieved from the server.
     *
     * @param err
     * @param context
     * @return
     */
    private static String handleServerError(Object err, Context context) {
        VolleyError error = (VolleyError) err;
        NetworkResponse response = error.networkResponse;
        if (response != null) {
            switch (response.statusCode) {

                case 401:
                    //当授权失效
                    JSONObject TokenObject  = JSON.parseObject(new String(response.data));
                    String mesg=TokenObject.getString("message");
                    if(mesg !=null && mesg.equals("Authorization has been denied for this request.")){
                        mesg="没有授权";
                    }
                    return mesg;
                case 400:
                    //这是当账号或者密码错误的时候
                    String results = new String(response.data);
                    JSONObject jsonObject = JSON.parseObject(results.toString());
                    if( results.contains("text/html")) {
                        return "返回了网页";
                    }
                    if( !TextUtils.isEmpty(jsonObject.getString("error_description"))){
                        return jsonObject.getString("error_description");
                    }
                    return  jsonObject.getString("message");
                case 404:
                case 422:
                case 500:
                    String result = new String(response.data);
                    JSONObject object = JSON.parseObject(result.toString());
                    LogUtils.e("500"+object.toString());
                    String message = object.getString("message");
                    return message;
                default:
                    return context.getResources().getString(
                            R.string.generic_server_down);
            }
        }
        return context.getResources().getString(R.string.generic_server_down);
    }

}
