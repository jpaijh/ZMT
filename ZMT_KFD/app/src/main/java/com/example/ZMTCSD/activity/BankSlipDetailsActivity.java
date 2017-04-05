package com.example.ZMTCSD.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
import com.example.ZMTCSD.utils.ToastUtil;
import com.example.ZMTCSD.utils.VolleyErrorHelper;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.entity.BankSlipDetailsEntity;
import com.example.ZMTCSD.utils.StringUtil;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.grantland.widget.AutofitTextView;

/**
 * 水单的详情界面
 */
@EActivity(R.layout.activity_bank_slip_details)
public class BankSlipDetailsActivity extends BaseActivity {
    private Context mContext;
    private RequestQueue mRequestQueue; //volley
    private int BankSlipId;
    private BankSlipDetailsEntity detailsEntity;
    private BankSlipDetailAdapter mAdapter;
    private List<BankSlipDetailsEntity.Accounts> accountList;
    private double NotClaimAmount;//这是未认领的金额
    private double BankRMBRate; //这是银行的汇率

    private LocalBroadcastManager mBroadcastManager;//内部广播
    private BroadcastReceiver mReceiver;
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    @ViewById(R.id.tv_no_data)
    AppCompatTextView tv_no_data;

    @ViewById(R.id.progressbar)
    CircleProgressBar mProgressbar;

    @ViewById(R.id.nsv_bankDetail_content)
    NestedScrollView mNsvContent;

    @ViewById(R.id.tv_bankDetail_code)
    AppCompatTextView mCode;

    @ViewById(R.id.tv_bankDetail_payer)
    AppCompatTextView mPayer;  //收款人

    @ViewById(R.id.tv_bankDetail_Date)
    AppCompatTextView mDate;  //收款时间

    @ViewById(R.id.tv_bankDetail_receiptAmount)
    AppCompatTextView mReceiptAmount; //收汇金额

    @ViewById(R.id.tv_bankDetail_dCosts)
    AppCompatTextView mDCosts;  //国内费用

    @ViewById(R.id.tv_bankDetail_fCosts)
    AppCompatTextView mFCosts;   //国外费用

    @ViewById(R.id.tv_bankDetail_netAmount)
    AppCompatTextView mNetAmount;  //净收汇金额

    @ViewById(R.id.tv_bankDetail_bankRMBRate)
    AppCompatTextView mRMBRate;  //汇率

    @ViewById(R.id.tv_bankDetail_RMBNetAmount)
    AppCompatTextView mRMBNetAmount;  //净收汇金额（RMB)

    @ViewById(R.id.tv_bankDetail_account)
    AppCompatTextView mAccount;

    @ViewById(R.id.recycler_bankDetail)
    RecyclerView mRecycler;

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_title.setText(getStrings(R.string.toolbar_BankSlipDetails));
    }

    private void initBroadcastReceiver() {
        // 注册广播接收者
        mBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppDelegate.BANK_SLIP_CLAIM_DETAILS); //水单认领的广播
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() == AppDelegate.BANK_SLIP_CLAIM_DETAILS) {
                    onBackgrounds();
                }
            }
        };
        mBroadcastManager.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onAfterViews() {
        mContext = BankSlipDetailsActivity.this;
        mRequestQueue = Volley.newRequestQueue(mContext);
        showView(mProgressbar);
        BankSlipId = getIntent().getIntExtra(AppDelegate.CUSTOMER_ID_COMPANY, 0);
        initToolbar();
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mRecycler.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .colorResId(R.color.color_theme_bj).sizeResId(R.dimen.lay_10) //记录左右的距离
                .build());
        initBroadcastReceiver();
        onBackgrounds();
    }

    @Override
    public void onBackgrounds() {
        int second = (int) (System.currentTimeMillis() / 1000);
        if (isRefreshWin(second)) {
            DoBankSlipDetails();
        } else {
            hideView(mProgressbar);
            tv_no_data.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.ic_bug3), null, null);
            tv_no_data.setText("刷新token不成功");
            showView(tv_no_data);
        }
    }

    void DoBankSlipDetails() {
        final String url = MoreUserDal.GetServerUrl() + "/api/bankslip/BankSlip?BankSlipId=" + BankSlipId;
        LogUtils.d("水单的url:" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                detailsEntity = JSON.parseObject(response.toString(), BankSlipDetailsEntity.class);
                mCode.setText(StringUtil.StringToNull(detailsEntity.getCode()));
                mPayer.setText(StringUtil.StringToNull(detailsEntity.getPayer()));
                mDate.setText(StringUtil.YMDDtoYMD(detailsEntity.getReceiptDate()));

                mReceiptAmount.setText(detailsEntity.getCurrency() + " " + StringUtil.numberFormat(detailsEntity.getReceiptAmount()));
                mDCosts.setText(detailsEntity.getCurrency() + " " + StringUtil.numberFormat(detailsEntity.getDomesticCosts()));
                mFCosts.setText(detailsEntity.getCurrency() + " " + StringUtil.numberFormat(detailsEntity.getForeignCosts()));
                mNetAmount.setText(detailsEntity.getCurrency() + " " + StringUtil.numberFormat(detailsEntity.getNetReceiptAmount()));
                BankRMBRate = detailsEntity.getBankRMBRate();
                mRMBRate.setText(StringUtil.numberForRate(BankRMBRate));
                mRMBNetAmount.setText(StringUtil.numberDecimal(detailsEntity.getRmbNetReceiptAmount()));
                accountList = detailsEntity.getAccounts();
                NotClaimAmount = detailsEntity.getRemainingAmount();
                if (accountList != null && accountList.size() != 0) {
                    mAdapter = new BankSlipDetailAdapter(detailsEntity.getAccounts());
                    mRecycler.setAdapter(mAdapter);
                    if (NotClaimAmount == 0.00) {
                        mAccount.setText(getStrings(R.string.BankSlip_nine));
                    } else {
                        mAccount.setText(getStrings(R.string.BankSlip_nine_two) + detailsEntity.getCurrency() + " " +
                                StringUtil.numberFormat(NotClaimAmount) + getStrings(R.string.BankSlip_nine_three));
                        menuItem.setEnabled(true);
                        menuItem.setVisible(true);
                    }
                } else {
                    mAccount.setText(getStrings(R.string.BankSlip_nine_one));
                    menuItem.setEnabled(true);
                    menuItem.setVisible(true);
                }

                hideView(mProgressbar);
                showView(mNsvContent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d("水单详情错误" + VolleyErrorHelper.getMessage(error, mContext));
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 3, 1.0f));
        mRequestQueue.add(jsonObjectRequest);
    }

    public class BankSlipDetailAdapter extends RecyclerView.Adapter<BankSlipDetailAdapter.ViewHolder> {
        private List<BankSlipDetailsEntity.Accounts> accountsList;

        public BankSlipDetailAdapter(List<BankSlipDetailsEntity.Accounts> accountsList) {
            this.accountsList = accountsList;
        }

        @Override
        public int getItemCount() {
            return accountsList.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bank_detail_list, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            BankSlipDetailsEntity.Accounts entity = accountsList.get(position);
            holder.mName.setText(entity.getAccountName());
            holder.currency.setText(" (" + entity.getCurrency() + ")");
            holder.mClaimAmount.setText(StringUtil.numberFormat(entity.getClaimAmount()));
            holder.RMBRate.setText(StringUtil.numberForRate(entity.getCustomerRMBRate()));
            holder.mDate.setText(StringUtil.YMDDtoYMD(entity.getClaimDate()));
            holder.mRMBAmount.setText(StringUtil.numberDecimal(entity.getRmbClaimAmount()));
            List<BankSlipDetailsEntity.Orders> orderList = entity.getOrders();
            if (orderList != null && orderList.size() != 0) {
                holder.SpiltOrder.setText(orderList.size() + getString(R.string.BankSlip_list_six));
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public AppCompatTextView mName;
            public AppCompatTextView mRMBAmount;
            public AppCompatTextView mDate;
            public AutofitTextView mClaimAmount;
            public AutofitTextView RMBRate;
            public AppCompatTextView currency;
            public AppCompatTextView SpiltOrder;

            public ViewHolder(View itemView) {
                super(itemView);
                mName = (AppCompatTextView) itemView.findViewById(R.id.tv_BKDetails_name);
                mRMBAmount = (AppCompatTextView) itemView.findViewById(R.id.tv_BKDetails_RMBAmount);
                mDate = (AppCompatTextView) itemView.findViewById(R.id.tv_BKDetails_Date);
                mClaimAmount = (AutofitTextView) itemView.findViewById(R.id.tv_BKDetails_claimAmount);
                RMBRate = (AutofitTextView) itemView.findViewById(R.id.tv_BKDetails_RMBRate);
                currency = (AppCompatTextView) itemView.findViewById(R.id.tv_BKDetails_currency);
                SpiltOrder = (AppCompatTextView) itemView.findViewById(R.id.tv_BKDetails_order);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        List<BankSlipDetailsEntity.Orders> orderList = accountsList.get(getLayoutPosition()).getOrders();
                        if (orderList != null && orderList.size() != 0) {
                            //TODO 到显示水单拆分到订单
                            BankSlipOrderActivity_.intent(mContext).extra(AppDelegate.BANKSLIP_DETAIL_ORDER, (Serializable) orderList).start();
                        } else {
                            ToastUtil.showToast(mContext, "无拆分到订单信息");
                        }
                    }
                });
            }
        }
    }


    private MenuItem menuItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_claim, menu);
        menuItem = menu.findItem(R.id.action_claim);
        menuItem.setEnabled(false);
        menuItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_claim:
                BankSlipClaimActivity_.intent(mContext).extra("netReceiptAmount", NotClaimAmount)
                        .extra("bankSlipId", BankSlipId).extra("bankRMBRate", BankRMBRate).start();
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
