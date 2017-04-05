package com.example.ZMTCSD.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

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
import com.example.ZMTCSD.MyApplication_;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.dal.MetaDataDal;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.entity.UserLoginEntity;
import com.example.ZMTCSD.utils.VolleyErrorHelper;
import com.example.ZMTCSD.view.AppCompatAutoCompleteClearTextView;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.dal.ServerDal;
import com.example.ZMTCSD.entity.MoreUserEntity;
import com.example.ZMTCSD.entity.UserClaimsEntity;
import com.example.ZMTCSD.utils.DeviceUtil;
import com.example.ZMTCSD.utils.NetUtil;
import com.example.ZMTCSD.utils.ToastUtil;
import com.example.ZMTCSD.view.AppCompatClearEditText;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    private Context mContent;
    private String mName;
    private String mPass;
    private RequestQueue mRequestQueue;
    private SharedPreferences sp_UserInfo;
    private SharedPreferences.Editor edit_UserInfo;
    private KProgressHUD hud;
    private long meta_time;
    private UserLoginEntity userLoginEntity;

    @ViewById(R.id.tv_user_name)
    AppCompatAutoCompleteClearTextView tv_user_name;

    @ViewById(R.id.et_password)
    AppCompatClearEditText et_password;

    @ViewById(R.id.btn_login)
    AppCompatButton btn_login;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    @ViewById(R.id.nested_scroll_view)
    NestedScrollView mScrollView;

    @ViewById(R.id.tv_server_login)
    AppCompatTextView tv_server;

    private int ServerPosition = 0;//是服务器的id
    private boolean addUser;

    @Override
    public void onAfterViews() {
        mContent = LoginActivity.this;
        addUser = getIntent().getBooleanExtra(AppDelegate.LOGIN_ADDUSER, true);
        if (addUser) {
            initToolbar();
        } else {
            initToolbarFalse();
        }
        mRequestQueue = Volley.newRequestQueue(mContent);
        sp_UserInfo = MyApplication_.getInstance().getUserInfoSp();
        edit_UserInfo = sp_UserInfo.edit();
        meta_time = sp_UserInfo.getLong(AppDelegate.METADATE_TIME, 0);
        initInputBox();
    }

    private void initToolbarFalse() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_title.setText("多用户登录");
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    /**
     * 登录
     */
    @Click(R.id.btn_login)
    void login() {
        DeviceUtil.hideSoft(mContent, et_password);
        if (NetUtil.isNetworkConnected(mContent)) {
            mName = tv_user_name.getText().toString().trim();
            mPass = et_password.getText().toString().trim();
            setBtnUnEnable();
            showKProgressHUD();
            onBackgrounds();
        } else {
            ToastUtil.showToast(this, "网络连接不可用，请检查网络");
        }
    }

    private String serverUrl;
    private MoreUserEntity moreEntity;

    @Override
    public void onBackgrounds() {
        serverUrl = ServerDal.GetServerUrl(ServerPosition);
        final String url_Login = serverUrl + "/api/OAuth/Token";
        LogUtils.d(url_Login);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url_Login, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                long second = (System.currentTimeMillis() / 1000);
                userLoginEntity = JSON.parseObject(response.toString(), UserLoginEntity.class);
//                // 登陆成功：保存用户名
                moreEntity = new MoreUserEntity();
                moreEntity.setLoginEntity(userLoginEntity);
                moreEntity.setSecond(second);
                moreEntity.setServerUrl(serverUrl);
                moreEntity.setServerId(ServerPosition);
                String url = serverUrl + "/api/Auth/Claims";
                LogUtils.d(url);
                final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<org.json.JSONArray>() {
                    @Override
                    public void onResponse(org.json.JSONArray response) {
                        List<UserClaimsEntity> claimsList = JSON.parseArray(response.toString(), UserClaimsEntity.class);
                        LogUtils.d("用户成功" + claimsList.get(1).getValue());
                        moreEntity.setClaimsList(claimsList);
                        moreEntity.setLoginName(claimsList.get(2).getValue());
                        MoreUserDal.SetMoreEntity(moreEntity);
                        edit_UserInfo.putString(AppDelegate.SP_MOREUSERENTITY, JSON.toJSONString(moreEntity)).commit();
                        edit_UserInfo.putBoolean(AppDelegate.IS_LOGIN, true).commit();
                        onMetaData();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LogUtils.e(error.toString() + "登录:" + VolleyErrorHelper.getMessage(error, mContent));
                        hud.dismiss();
                        ToastUtil.showToast(LoginActivity.this, "没有获取到用户的信息。请重新获取");
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("Authorization", "bearer   " + userLoginEntity.getAccess_token());
                        return map;
                    }
                };
                jsonArrayRequest.setTag(this);
                jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 3, 1.0f));
                mRequestQueue.add(jsonArrayRequest);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.e(error.toString() + "登录:" + VolleyErrorHelper.getMessage(error, mContent));
                ToastUtil.showToast(mContent, VolleyErrorHelper.getMessage(error, mContent));
                hud.dismiss();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            @Override
            public byte[] getBody() {
                String string = "username=" + mName + "&password=" + mPass + "&grant_type=password";
                return string.getBytes();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        jsonObjectRequest.setTag(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 1, 1.0f));
        mRequestQueue.add(jsonObjectRequest);
    }

    @Background
    void onMetaData() {
        final String url = serverUrl + "/api/resource/metadata";
        LogUtils.e("元数据" + url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<org.json.JSONArray>() {
            @Override
            public void onResponse(org.json.JSONArray response) {
                String jsonString = MetaDataDal.getMetaData();
                if (!TextUtils.isEmpty(jsonString) || !jsonString.equals(response.toString())) {
                    LogUtils.e("元数据 " + response.toString());
                    edit_UserInfo.putString(AppDelegate.SP_META_DATE, response.toString()).commit();
                }
                hud.dismiss();
                MainActivity_.intent(LoginActivity.this).start();
                MyApplication_.getInstance().exit();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d(VolleyErrorHelper.getMessage(error, mContent));
                ToastUtil.showToast(mContent, "元数据没有请求成功");
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
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, 1.0f));
        mRequestQueue.add(jsonArrayRequest);

    }

    private void initInputBox() {
        tv_user_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeScrollView();
                return false;
            }
        });

        et_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeScrollView();
                return false;
            }
        });

        tv_user_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_password.length() != 0 && tv_user_name.length() != 0) {
                    setBtnEnable();
                } else {
                    setBtnUnEnable();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_password.length() != 0 && tv_user_name.length() != 0) {
                    setBtnEnable();
                } else {
                    setBtnUnEnable();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setBtnEnable() {
        btn_login.setEnabled(true);
        btn_login.setBackgroundResource(R.drawable.selector_login_btn_tyt);
    }

    private void setBtnUnEnable() {
        btn_login.setEnabled(false);
        btn_login.setBackgroundResource(R.drawable.shape_login_btn_tint);
    }

    /**
     * 让ScrollView向上移
     */
    private void changeScrollView() {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mScrollView.smoothScrollTo(0, mScrollView.getChildAt(0).getHeight());
            }
        }, 100);
    }

    private void showKProgressHUD() {
        hud = KProgressHUD.create(mContent)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("正在登录...")
                .setCancellable(true);
        hud.show();
    }

    @Click(R.id.tv_cut_login)
    void CutServer() {
        DeviceUtil.hideSoft(mContent, et_password);
        final String[] stringItems = ServerDal.GetServerName();
        final ActionSheetDialog dialog = new ActionSheetDialog(mContent, stringItems, null);
        dialog.title("选择登陆的服务器")//
                .titleTextSize_SP(16f).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.showToast(mContent, stringItems[position]);
                tv_server.setText("当前服务器为：" + stringItems[position]);
                ServerPosition = position;
                dialog.dismiss();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!addUser) {
            getMenuInflater().inflate(R.menu.activity_login, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        mRequestQueue.cancelAll(this);
        super.onDestroy();
    }

    /**
     * 如果在登陆界面按返回键就是退出应用
     */
    @Override
    public void onBackPressed() {
        if (addUser) {
            System.exit(0);
        }
        finish();
    }
}
