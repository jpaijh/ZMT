package com.example.ZMTCSD.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.entity.UserLoginEntity;
import com.example.ZMTCSD.utils.DateUtil;
import com.example.ZMTCSD.utils.VolleyErrorHelper;
import com.example.ZMTCSD.AppDelegate;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;

import java.util.Map;

@EActivity
public class BaseActivity extends AppCompatActivity {

    /**
     * @AfterViews 布局初始化完成
     */
    @AfterViews
    public void onAfterViews() {
    }


    /**
     * @Background 网络请求数据
     */
    @Background
    public void onBackgrounds() {
    }

    /**
     * 获取颜色资源
     */
    public int getColors(int colorRes) {
        return getResources().getColor(colorRes);
    }

    /**
     * 获取文字资源
     *
     * @param stringRes
     * @return
     */
    public String getStrings(int stringRes) {
        return getResources().getString(stringRes);
    }

    /**
     * 获取文字数组资源
     *
     * @param strings
     * @return
     */
    public String[] getsStrings(int strings) {
        return getResources().getStringArray(strings);
    }

    /**
     * 获取高度的资源
     *
     * @param dimensRes
     * @return
     */
    public float getDimension(int dimensRes) {
        return getResources().getDimension(dimensRes);
    }


    /**
     * 保存当前时间
     *
     * @param str 键名
     */
    public void saveCurrentTime(String str) {
        getSharedPreferences(AppDelegate.SP_REFRESH_DATE, Context.MODE_PRIVATE).edit().
                putString(str, DateUtil.longDateToStrMDHMS(System.currentTimeMillis())).commit();
    }

    /**
     * 创建 正在刷新... - 上次刷新时间 Snackbar（用 SharedPreferences 保存上次刷新时间）
     *
     * @param view ViewGroup 子类
     * @param str  记录上次刷新时间的标记：统一用TAG
     */
    public void createRefreshSnackbar(ViewGroup view, String str) {
        String currentDate = DateUtil.longDateToStrMDHMS(System.currentTimeMillis());
        SharedPreferences spRefreshDate = getSharedPreferences(AppDelegate.SP_REFRESH_DATE, Context.MODE_PRIVATE);
        String lastRefreshDate = spRefreshDate.getString(str, currentDate);
        Snackbar snackbar = Snackbar.make(view, "正在刷新...", Snackbar.LENGTH_SHORT);
        snackbar.setAction("上次刷新时间：" + lastRefreshDate, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 这个点击事件必须加上不然 snackbar_action 就无法显示
                // snackbar.dismiss();
            }
        });
        TextView snackbar_action = (TextView) snackbar.getView().findViewById(R.id.snackbar_action);
        snackbar_action.setTextSize(11f);
        snackbar.setActionTextColor(getColors(R.color.white));
        Snackbar.SnackbarLayout snackBarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarLayout.setBackgroundColor(getColors(R.color.color_theme));
        snackBarLayout.setAlpha(0.9f);
        snackbar.show();
        spRefreshDate.edit().putString(str, currentDate).commit();
    }

    /**
     * 创建 正在加载... Snackbar
     *
     * @param view ViewGroup 子类
     */
    public void createLoadingSnackbar(ViewGroup view) {
        Snackbar snackbar = Snackbar.make(view, "正在加载...", Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout snackBarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarLayout.setBackgroundColor(getColors(R.color.color_theme));
        snackBarLayout.setAlpha(0.9f);
        snackbar.show();
    }

    /**
     * 创建 加载完成... Snackbar
     *
     * @param view ViewGroup 子类
     */
    public void createLoadingCompleteSnackbar(ViewGroup view) {
        Snackbar snackbar = Snackbar.make(view, "加载完成...", Snackbar.LENGTH_SHORT);
        Snackbar.SnackbarLayout snackBarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarLayout.setBackgroundColor(getColors(R.color.decoView_line1_start_color));
        snackBarLayout.setAlpha(0.9f);
        snackbar.show();
    }

    /**
     * 创建 已经没有数据了...  SnackbarUserLoginEntity
     *
     * @param view ViewGroup 子类
     */
    public void createNoDateSnackbar(ViewGroup view) {
        Snackbar snackbar = Snackbar.make(view, "已经没有数据了...", Snackbar.LENGTH_SHORT);
        Snackbar.SnackbarLayout snackBarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarLayout.setBackgroundColor(getColors(R.color.red));
        snackBarLayout.setAlpha(0.8f);
        snackbar.show();
    }

    /**
     * 创建 刷新成功...  Snackbar
     *
     * @param view ViewGroup 子类
     */
    public void createRefreshCompleteSnackbar(ViewGroup view) {
        Snackbar snackbar = Snackbar.make(view, "刷新成功...", Snackbar.LENGTH_SHORT);
        Snackbar.SnackbarLayout snackBarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarLayout.setBackgroundColor(getColors(R.color.decoView_line1_start_color));
        snackBarLayout.setAlpha(0.9f);
        snackbar.show();
    }

    /**
     * 显示 View
     *
     * @param view
     */
    public void showView(View view) {
        view.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏 View 但占领位置
     *
     * @param view
     */
    public void InvisibleView(View view) {
        view.setVisibility(View.INVISIBLE);
    }

    /**
     * 隐藏 View
     *
     * @param view
     */
    public void hideView(View view) {
        view.setVisibility(View.GONE);
    }


    private RequestQueue mRequestQueue;
    private boolean iswin;

    public boolean isRefreshWin(int second) {
        long unsend = MoreUserDal.GetSecond() + MoreUserDal.GetExpiresin();
//        int unsend= MoreUserDal.GetSecond() +20;
        if (second > unsend) {
            Refreshtoken();
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return iswin;
        } else {
            return true;
        }
    }

    public void Refreshtoken() {
        mRequestQueue = Volley.newRequestQueue(this);
        final String url_RefreshToken = MoreUserDal.GetServerUrl() + "/api/OAuth/Token";
        LogUtils.d("刷新token" + url_RefreshToken);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url_RefreshToken, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                UserLoginEntity userLoginEntity = JSON.parseObject(response.toString(), UserLoginEntity.class);
                // 登陆成功：保存用户名
                int second = (int) (System.currentTimeMillis() / 1000);
                LogUtils.e("更新前" + MoreUserDal.GetAccessToken());
                MoreUserDal.UpdateMoreUser(userLoginEntity, second);
                mHandler.sendMessage(mHandler.obtainMessage(0));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d("刷新失败" + VolleyErrorHelper.getMessage(error, BaseActivity.this));
                mHandler.sendMessage(mHandler.obtainMessage(1));
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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    iswin = true;
                    break;
                case 1:
                    iswin = false;
                    break;
                default:
                    break;
            }
        }
    };
}
