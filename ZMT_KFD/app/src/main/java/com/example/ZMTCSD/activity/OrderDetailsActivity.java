package com.example.ZMTCSD.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.entity.ContactEntity;
import com.example.ZMTCSD.entity.OrderDetailsEntity;
import com.example.ZMTCSD.entity.OrderSettleInfoEntity;
import com.example.ZMTCSD.utils.DateUtil;
import com.example.ZMTCSD.utils.VolleyErrorHelper;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.utils.StringUtil;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单的详情
 */
@EActivity(R.layout.activity_order_details)
public class OrderDetailsActivity extends BaseActivity {
    private Context mContext;
    private RequestQueue mRequestQueue; //volley
    private int orderId;
    private OrderItemAdapter mAdapter;
    private OrderDetailsEntity detailsEntity;
    private OrderSettleInfoEntity settleEntity;
    private List<OrderDetailsEntity.OrderItems> Orderitems;
    private ContactEntity contactEntity;//电话的实体类

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    @ViewById(R.id.tv_no_data)
    AppCompatTextView tv_no_data;

    @ViewById(R.id.progressbar)
    CircleProgressBar mProgressbar;

    @ViewById(R.id.nsv_orDetail_content)
    NestedScrollView mNsvContent;

    @ViewById(R.id.tv_orDetail_code)
    AppCompatTextView mTvCode;

    @ViewById(R.id.tv_orDetail_statusStr)
    AppCompatTextView mTvStr;

    @ViewById(R.id.tv_orDetail_tax)
    AppCompatTextView mTax;

    @ViewById(R.id.tv_orDetail_date)
    AppCompatTextView mDate;

    @ViewById(R.id.tv_orDetail_AName)
    AppCompatTextView mAname;

    @ViewById(R.id.tv_orDetail_AContact)
    AppCompatTextView mAcontact;

    @ViewById(R.id.img_orDetail_call)
    AppCompatImageView mImgCall;

    @ViewById(R.id.view_orDetail)
    View mCallView;

    @ViewById(R.id.ll_inAndex)
    LinearLayout ll_InAndEx;

    @ViewById(R.id.tv_volley_date)
    AppCompatTextView tv_volley_error;

    @ViewById(R.id.tv_orDetail_income)
    AppCompatTextView mIncome;  //收入

    @ViewById(R.id.tv_orDetail_receiptAmount)
    AppCompatTextView mReceiptA;  //收汇

    @ViewById(R.id.tv_orDetail_rebateAmount)
    AppCompatTextView mRebateA;  //退税

    @ViewById(R.id.tv_orDetail_expense)
    AppCompatTextView mExpense; //支出

    @ViewById(R.id.tv_orDetail_payAmount)
    AppCompatTextView mPayA;  //付款

    @ViewById(R.id.tv_orDetail_costAmount)
    AppCompatTextView mCostA; //费用

    @ViewById(R.id.tv_orDetail_profitAmount)
    AppCompatTextView mProfitA;  //利润

    @ViewById(R.id.recycler_orDetail)
    RecyclerView mRecycler;

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_title.setText(getStrings(R.string.toolbar_OrderDetails));
    }

    @Override
    public void onAfterViews() {
        mContext = OrderDetailsActivity.this;
        mRequestQueue = Volley.newRequestQueue(mContext);
        initToolbar();
        showView(mProgressbar);//开启progress
        orderId = getIntent().getIntExtra(AppDelegate.CUSTOMER_ID_COMPANY, 0);
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mRecycler.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .colorResId(R.color.line_color).marginResId(R.dimen.left_74, R.dimen.right_0)//记录左右的距离
                .build());
        onBackgrounds();
    }

    @Override
    public void onBackgrounds() {
        int second = (int) (System.currentTimeMillis() / 1000);
        if (isRefreshWin(second)) {
            DoOrderDetails();
        } else {
            hideView(mProgressbar);
            tv_no_data.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.ic_bug3), null, null);
            tv_no_data.setText(getStrings(R.string.token_volley_error));
            showView(tv_no_data);
        }
    }

    //订单中的信息
    void DoOrderDetails() {
        final String url = MoreUserDal.GetServerUrl() + "/api/order/order?orderId=" + orderId;
        LogUtils.d("订单详情" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                detailsEntity = JSON.parseObject(response.toString(), OrderDetailsEntity.class);
                mTvCode.setText(getStrings(R.string.Order_one) + detailsEntity.getCode());
                mTvStr.setText(detailsEntity.getStatusStr());
                mTax.setText(StringUtil.StringToNulls(detailsEntity.getCurrency()) + " " + StringUtil.numberFormat(detailsEntity.getDeclarationAmount()));
                mDate.setText(StringUtil.YMDDtoYMD(detailsEntity.getCreateDate()));
                mAname.setText(detailsEntity.getAccountName());
                contactEntity = detailsEntity.getAccountContact();
                //TODO 进行判断 contactEntity 是否为null
                if (contactEntity != null) {
                    if(TextUtils.isEmpty(contactEntity.getName()) || TextUtils.isEmpty(contactEntity.getPhoneNumber())){
                        mAcontact.setText(" — — ");
                    }else{
                        showView(mCallView);
                        showView(mImgCall);
                        mAcontact.setText(contactEntity.getName()+" - "+ contactEntity.getPhoneNumber());
                    }
                }

                Orderitems = detailsEntity.getOrderItems();
                mAdapter = new OrderItemAdapter(Orderitems);
                mRecycler.setAdapter(mAdapter);
                DoOrderSettle();
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

    //订单中的收支状态
    void DoOrderSettle() {
        final String url = MoreUserDal.GetServerUrl() + "/api/order/settleInfo?orderId=" + orderId;
        LogUtils.d("收支详情" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                settleEntity = JSON.parseObject(response.toString(), OrderSettleInfoEntity.class);
                mIncome.setText(StringUtil.numberDecimal(settleEntity.getReceiptAmount() + settleEntity.getRebateAmount()));
                mReceiptA.setText(StringUtil.numberDecimal(settleEntity.getReceiptAmount()));
                mRebateA.setText(StringUtil.numberDecimal(settleEntity.getRebateAmount()));
                mExpense.setText("-" + StringUtil.numberDecimal(settleEntity.getPayAmount() + settleEntity.getCostAmount()));
                mPayA.setText("-" + StringUtil.numberDecimal(settleEntity.getPayAmount()));
                mCostA.setText("-" + StringUtil.numberDecimal(settleEntity.getCostAmount()));
                mProfitA.setText(StringUtil.numberDecimal(settleEntity.getProfitAmount()));
                hideView(mProgressbar);
                showView(mNsvContent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d("查询失败" + VolleyErrorHelper.getMessage(error, mContext));
                hideView(mProgressbar);
                InvisibleView(ll_InAndEx);
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

    private class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {
        private List<OrderDetailsEntity.OrderItems> itemlist;

        public OrderItemAdapter(List<OrderDetailsEntity.OrderItems> properlist) {
            this.itemlist = properlist;
        }

        @Override
        public int getItemCount() {
            return itemlist.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_detail_list, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            OrderDetailsEntity.OrderItems item = itemlist.get(position);
            int serviceItemId = item.getServiceItem().getId();
            switch (serviceItemId) {
                case 1001:  //一般贸易出口通关
                    holder.ServerImg.setImageResource(R.mipmap.ic_order_generaltrade);
                    break;
                case 1004: //退税融资
                    holder.ServerImg.setImageResource(R.mipmap.ic_order_exportrebates);
                    break;
                case 1005: //赊销融资
                    holder.ServerImg.setImageResource(R.mipmap.ic_order_creditfinancing);
                    break;
                case 1020: //中信保投保
                    holder.ServerImg.setImageResource(R.mipmap.ic_order_sinosurelnsure);
                    break;
                default:
                    holder.ServerImg.setImageResource(R.mipmap.ic_order_universalserver);
                    break;
            }
            holder.name.setText(item.getServiceItem().getName());
            List<OrderDetailsEntity.Actions> actionList = item.getServiceItem().getActions();
            holder.action.setText(actionList.get(DoActionNo(actionList)).getActionName());
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private AppCompatTextView name;
            private AppCompatTextView action;
            private AppCompatImageView ServerImg;

            public ViewHolder(final View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderDetailsEntity.OrderItems entity = itemlist.get(getLayoutPosition());
                        String actionName = DoActionName(entity.getServiceItem().getActions());
                        int actionNo = DoActionNo(entity.getServiceItem().getActions());
                        OrderServersActivity_.intent(mContext).extra(AppDelegate.ORDER_ITEMS, entity)
                                .extra(AppDelegate.ORDER_ACTION_NAME, actionNo).start();
                    }
                });
                name = (AppCompatTextView) itemView.findViewById(R.id.tv_orDetail_name);
                action = (AppCompatTextView) itemView.findViewById(R.id.tv_orDetail_action);
                ServerImg = (AppCompatImageView) itemView.findViewById(R.id.img_orDetail_list);
            }
        }
    }

    @Click(R.id.img_orDetail_call)
    void DoImgCall() {
        //在获取数据后 去除没有电话为 [] 或者为 null 的时候
        DateUtil.DoCallDialog(mContext, contactEntity);
    }

    @Click(R.id.v_financeFour_SettlePdf)
    void onSettlePdf() {
//        http://www.tjzmt.com:9080/manage/orderExport/SettlePdf/4998793
        String url = AppDelegate.ORDER_SETTLE_PDF_URL + orderId;
//        WebViewActivity_.intent(mContext).extra(AppDelegate.DETAILS_WEBVIEW_NAME,"结算单").extra(AppDelegate.DETAILS_WEBVIEW,url).start();
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);
    }

    //获取服务项在那个状态
    String DoActionName(List<OrderDetailsEntity.Actions> actionList) {
        String actionName = "";
        for (int i = actionList.size() - 1; i < actionList.size(); i--) {
            if (actionList.get(i).getStatus() == 1 || actionList.get(i).getStatus() == 2) {
                actionName = actionList.get(i).getActionName();
                break;
            } else {
                if (i == 0) {
                    actionName = actionList.get(i).getActionName();
                    break;
                }
            }
        }
        return actionName;
    }

    int DoActionNo(List<OrderDetailsEntity.Actions> actionList) {
        int actionNo = -1;
        for (int i = actionList.size() - 1; i < actionList.size(); i--) {
            if (actionList.get(i).getStatus() == 1 || actionList.get(i).getStatus() == 2) {
                actionNo = i;
                break;
            } else {
                if (i == 0) {
                    actionNo = 0;
                    break;
                }
            }
        }
        return actionNo;
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
        super.onDestroy();
    }
}

