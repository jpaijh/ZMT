package com.example.ZMTCSD.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

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
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.entity.ContactEntity;
import com.example.ZMTCSD.entity.CustomerDetailsEntity;
import com.example.ZMTCSD.entity.CustomerFundsEntity;
import com.example.ZMTCSD.entity.PropertyGroupsEntity;
import com.example.ZMTCSD.utils.DateUtil;
import com.example.ZMTCSD.utils.VolleyErrorHelper;
import com.example.ZMTCSD.utils.StringUtil;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户的详情界面
 */

@EActivity(R.layout.activity_customer_details)
public class CustomerDetailsActivity extends BaseActivity {
    private Context mContext;
    private int accountId;
    private String accountName;
    private RequestQueue mRequestQueue; //volley
    private CustomerDetailsEntity detailsEntity;
    private CustomerFundsEntity fundsEntity;
    private List<PropertyGroupsEntity> propetyGroupsList;
    private List<CustomerDetailsEntity.fileGroups> fileGroupsList;
    private ContactEntity contactEntity;

    private LocalBroadcastManager mBroadcastManager;
    private BroadcastReceiver mReceiver;
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    @ViewById(R.id.tv_no_data)
    AppCompatTextView tv_no_data;

    @ViewById(R.id.progressbar)
    CircleProgressBar mProgressbar;

    @ViewById(R.id.nsv_cuDetail_content)
    NestedScrollView mNsvContent;

    @ViewById(R.id.tv_cuDetail_name)
    AppCompatTextView mName;

    @ViewById(R.id.tv_cuDetail_status)
    AppCompatTextView mStatus;

    @ViewById(R.id.tv_cuDetail_code)
    AppCompatTextView mCode;

    @ViewById(R.id.tv_cuDetail_value)
    AppCompatTextView mValue;

    @ViewById(R.id.img_cuDetail_call)
    AppCompatImageView mImgCall;

    @ViewById(R.id.view_cuDetail)
    View mCallView;

    @ViewById(R.id.ll_funds)
    LinearLayout mFunds; //资金信息

    @ViewById(R.id.tv_cuDetail_accountBalance)
    AppCompatTextView mBalance; //普通账户

    @ViewById(R.id.tv_cuDetail_financingAccount)
    AppCompatTextView mFinancing; //融资账户

    @ViewById(R.id.tv_cuDetail_debtAccount)
    AppCompatTextView mDabt;   //融资欠款

    @ViewById(R.id.tv_cuDetail_invoiceAmount)
    AppCompatTextView mInvoice;  //总开票额

    @ViewById(R.id.tv_volley_date)
    AppCompatTextView tv_volley_error;

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_title.setText(getStrings(R.string.toolbar_CustomerDetails));
    }

    /**
     * 内部的广播
     */
    private void initBroadcastReceiver() {
        // 注册广播接收者
        mBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppDelegate.CUSTOMER_ATTACHMENT_REFRESH); //附件更改，刷新详情的广播
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() == AppDelegate.CUSTOMER_ATTACHMENT_REFRESH) {
                    onBackgrounds();
                }
            }
        };

        mBroadcastManager.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onAfterViews() {
        showView(mProgressbar);
        initToolbar();
        mContext = CustomerDetailsActivity.this;
        mRequestQueue = Volley.newRequestQueue(mContext);
        accountId = getIntent().getIntExtra(AppDelegate.CUSTOMER_ID_COMPANY, 0);//获取从列表传来的值
        initBroadcastReceiver();
        onBackgrounds();
    }

    @Override
    public void onBackgrounds() {
        int second = (int) (System.currentTimeMillis() / 1000);
        if (isRefreshWin(second)) {
            DoCustomerDetails();
        } else {
            hideView(mProgressbar);
            tv_no_data.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.ic_bug3), null, null);
            tv_no_data.setText("刷新token不成功");
            showView(tv_no_data);
        }
    }

    void DoCustomerDetails() {
        final String url = MoreUserDal.GetServerUrl() + "/api/platformAccount/Account?accountId=" + accountId;
        LogUtils.d("客户的详情" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                detailsEntity = JSON.parseObject(response.toString(), CustomerDetailsEntity.class);
                accountName = detailsEntity.getName();
                contactEntity = detailsEntity.getContact();
                mName.setText(accountName);
                mStatus.setText(detailsEntity.getStatusStr());
                mCode.setText(getStrings(R.string.Customer_one) + StringUtil.StringToNull(detailsEntity.getCode()));
                if (contactEntity != null) {
                    if(TextUtils.isEmpty(contactEntity.getName()) || TextUtils.isEmpty(contactEntity.getPhoneNumber())){
                        mValue.setText(" — — ");
                    }else{
                        showView(mCallView);
                        showView(mImgCall);
                        mValue.setText(contactEntity.getName()+" - "+ contactEntity.getPhoneNumber());
                    }
                }

                propetyGroupsList = detailsEntity.getPropetyGroups();
                fileGroupsList = detailsEntity.getFileGroups();
                DoAccountFunds();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d("查询失败" + VolleyErrorHelper.getMessage(error, mContext));
                hideView(mProgressbar);
                tv_no_data.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.ic_bug3), null, null);
                tv_no_data.setText(VolleyErrorHelper.getMessage(error, mContext));
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

    //资金信息
    void DoAccountFunds() {
        final String url = MoreUserDal.GetServerUrl() + "/api/platformAccount/accountFunds?accountId=" + accountId;
        LogUtils.d("客户资金信息" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                fundsEntity = JSON.parseObject(response.toString(), CustomerFundsEntity.class);
                mBalance.setText(StringUtil.numberDecimal(fundsEntity.getAccountBalance()));
                mFinancing.setText(StringUtil.numberDecimal(fundsEntity.getFinancingAccountBanance()));
                mDabt.setText(StringUtil.numberDecimal(fundsEntity.getDebtAccountBanance()));
                mInvoice.setText(StringUtil.numberDecimal(fundsEntity.getInvoiceAmount()));
                hideView(mProgressbar);
                showView(mNsvContent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d("查询失败" + VolleyErrorHelper.getMessage(error, mContext));
                hideView(mProgressbar);
                InvisibleView(mFunds);
                tv_volley_error.setText(VolleyErrorHelper.getMessage(error, mContext));
                showView(tv_volley_error);
                showView(mNsvContent);
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

    @Click(R.id.rl_cuDetail_visitRecords)
    void DoVisit() {
        CustomerVisitActivity_.intent(mContext).extra(AppDelegate.CUSTOMER_ID_COMPANY, accountId)
                .extra(AppDelegate.CUSTOMER_ATTACH_NAME, accountName).start();
    }

    @Click(R.id.rl_cuDetail_basic)
    void DoBasic() {
        CustomerBasicInfoActivity_.intent(mContext).extra(AppDelegate.CUSTOMER_BASIC_COMPANY, (Serializable) propetyGroupsList).start();
    }

    @Click(R.id.rl_cuDetail_attachment)
    void DoAttachment() {
        CustomerAttachmentActivity_.intent(mContext).extra(AppDelegate.CUSTOMER_ATTACH_COMPANY, (Serializable) fileGroupsList)
                .extra(AppDelegate.CUSTOMER_ID_COMPANY, accountId).extra(AppDelegate.CUSTOMER_ATTACH_NAME, accountName).start();
    }

    @Click(R.id.rl_cuDetail_company)
    void DoCompany() {
        CustomerCompanyListActivity_.intent(mContext).extra(AppDelegate.CUSTOMER_ID_COMPANY, accountId).start();
    }

    @Click(R.id.img_cuDetail_call)
    void DoCall() {
        //在获取数据库的数据后 去除没有电话为 [] 或者为 null 的时候
        DateUtil.DoCallDialog(mContext, contactEntity);
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
        mBroadcastManager.unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
