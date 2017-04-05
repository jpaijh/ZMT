package com.example.ZMTCSD.activity;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
import com.example.ZMTCSD.entity.MessageEntity;
import com.example.ZMTCSD.utils.VolleyErrorHelper;
import com.example.ZMTCSD.AppDelegate;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.zzhoujay.richtext.RichText;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

/**
 * 信息的详情界面
 */
@EActivity(R.layout.activity_message_details)
public class MessageDetailsActivity extends BaseActivity{
    private Context mContent;
    private RequestQueue mRequestQueue; //volley
    private int MessageId;
    private MessageEntity detailsEntity;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    @ViewById(R.id.tv_no_data)
    AppCompatTextView tv_no_data;

    @ViewById(R.id.progressbar)
    CircleProgressBar mProgressbar;

    @ViewById(R.id.tv_meDetail_text)
    AppCompatTextView mText;

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_title.setText(getStrings(R.string.toolbar_MessageDetails));
    }
    @Override
    public void onAfterViews() {
        mContent=MessageDetailsActivity.this;
        mRequestQueue = Volley.newRequestQueue(mContent);
        initToolbar();
        showView(mProgressbar);//开启progress
        MessageId=getIntent().getIntExtra(AppDelegate.CUSTOMER_ID_COMPANY ,0);
        onBackgrounds();
    }

    @Override
    public void onBackgrounds() {
        int second = (int) (System.currentTimeMillis() / 1000);
        if (isRefreshWin(second)) {
            DoMessageDetails();
        }else{
            hideView(mProgressbar);
            tv_no_data.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.mipmap.ic_bug3),null,null);
            tv_no_data.setText(getStrings(R.string.token_volley_error));
            showView(tv_no_data);
        }
    }

    void DoMessageDetails(){
        final String url = MoreUserDal.GetServerUrl() + "/api/message/Message?MessageId=" + MessageId;
        LogUtils.d("信息详情" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                detailsEntity = JSON.parseObject(response.toString(), MessageEntity.class);
                // 设置为Html
                hideView(mProgressbar);
                RichText.fromHtml(detailsEntity.getContent()).into(mText);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d("查询失败" +  VolleyErrorHelper.getMessage(error,mContent));
                hideView(mProgressbar);
                tv_no_data.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.mipmap.ic_bug3),null,null);
                tv_no_data.setText(VolleyErrorHelper.getMessage(error,mContent));
                showView(tv_no_data);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "bearer " + MoreUserDal.GetAccessToken());
                return map;
            }
        };
        jsonObjectRequest.setTag(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50 * 1000, 3, 1.0f));
        mRequestQueue.add(jsonObjectRequest);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        mRequestQueue.cancelAll(this);
        finish();
        super.onDestroy();
    }
}
