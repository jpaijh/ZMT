package com.example.ZMTCSD.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

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
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.entity.PaymentDetailsEntity;
import com.example.ZMTCSD.utils.StringUtil;
import com.example.ZMTCSD.utils.ToastUtil;
import com.example.ZMTCSD.utils.VolleyErrorHelper;
import com.flyco.animation.BounceEnter.BounceEnter;
import com.flyco.animation.ZoomExit.ZoomInExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.NormalDialog;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.grantland.widget.AutofitTextView;

/**
 * 付款详情界面
 */
@EActivity(R.layout.activity_payment_details)
public class PaymentDetailsActivity extends BaseActivity {
    private Context mContent;
    private RequestQueue mRequestQueue; //volley
    private int PaymentApplyId;
    private int PaymentStatus;
    private PaymentDetailsEntity detailsEntity;
    private List<PaymentDetailsEntity.Orders> ordersList;
    private PaymentDetailAdapter mAdapter;
    private NormalDialog dialog;
    private KProgressHUD hud;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    @ViewById(R.id.tv_no_data)
    AppCompatTextView tv_no_data;

    @ViewById(R.id.progressbar)
    CircleProgressBar mProgressbar;

    @ViewById(R.id.nsv_payDetail_content)
    NestedScrollView mNsvContent;

    @ViewById(R.id.tv_payDetail_code)
    AppCompatTextView mCode;

    @ViewById(R.id.tv_payDetail_statusStr)
    AppCompatTextView mStatus;

    @ViewById(R.id.tv_payDetail_rmb)
    AppCompatTextView mRMBName;

    @ViewById(R.id.tv_payDetail_paymentAmount)
    AppCompatTextView mPayAmount;

    @ViewById(R.id.tv_payDetail_type)
    AppCompatTextView mSubType;

    @ViewById(R.id.tv_payDetail_date)
    AppCompatTextView mDate;

    @ViewById(R.id.tv_payDetail_reName)
    AppCompatTextView mReName;

    @ViewById(R.id.tv_payDetail_reCompany)
    AppCompatTextView mReCompany;

    @ViewById(R.id.tv_payDetail_reBank)
    AppCompatTextView mReBank;

    @ViewById(R.id.tv_payDetail_reBankA)
    AppCompatTextView mReBankAccount;

    @ViewById(R.id.tv_payDetail_items)
    AppCompatTextView mOrders;

    @ViewById(R.id.recycler_payDetail)
    RecyclerView mRecycler;

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_title.setText(getStrings(R.string.toolbar_PaymentDetails));
    }

    @Override
    public void onAfterViews() {
        mContent = PaymentDetailsActivity.this;
        mRequestQueue = Volley.newRequestQueue(mContent);
        hud = KProgressHUD.create(mContent).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("处理中").setSize(100, 110).setCornerRadius(5);
        initToolbar();
        showView(mProgressbar);//开启progress
        dialog = new NormalDialog(mContent);
        PaymentApplyId = getIntent().getIntExtra(AppDelegate.CUSTOMER_ID_COMPANY, 0);
        mRecycler.setLayoutManager(new LinearLayoutManager(mContent, LinearLayoutManager.VERTICAL, false));
        mRecycler.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContent)
                .colorResId(R.color.color_theme_bj).sizeResId(R.dimen.left_16) //记录左右的距离
                .build());
        onBackgrounds();
    }

    @Override
    public void onBackgrounds() {
        int second = (int) (System.currentTimeMillis() / 1000);
        if (isRefreshWin(second)) {
            DoPaymentDetails();
        } else {
            hideView(mProgressbar);
            tv_no_data.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.ic_bug3), null, null);
            tv_no_data.setText("刷新token不成功");
            showView(tv_no_data);
        }
    }

    void DoPaymentDetails() {
        final String url = MoreUserDal.GetServerUrl() + "/api/payment/PaymentApply?PaymentApplyId=" + PaymentApplyId;
        LogUtils.d("付款详情" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                detailsEntity = JSON.parseObject(response.toString(), PaymentDetailsEntity.class);
                mCode.setText(detailsEntity.getCode());
                PaymentStatus = detailsEntity.getStatus();
                menuItem.setEnabled(true);
                mStatus.setText(detailsEntity.getStatusStr());
                if (!("RMB").equalsIgnoreCase(detailsEntity.getCurrency())) {
                    mRMBName.setText(getString(R.string.Payment_two_one) + StringUtil.numberFormat(detailsEntity.getRmbAmount()) + " " +
                            getString(R.string.Payment_two_two) + StringUtil.numberForRate(detailsEntity.getRmbRate()) + ")");
                    showView(mRMBName);
                }
                mPayAmount.setText(detailsEntity.getCurrency() + " " + StringUtil.numberFormat(detailsEntity.getPaymentAmount()));
                mSubType.setText(detailsEntity.getType() + " - " + detailsEntity.getSubType());
                mDate.setText(StringUtil.YMDDtoYMD(detailsEntity.getCreateDate()));
                mReName.setText(StringUtil.StringToNull(detailsEntity.getAccountName()));
                mReCompany.setText(StringUtil.StringToNull(detailsEntity.getReceivingCompany()));
                mReBank.setText(StringUtil.StringToNull(detailsEntity.getReceivingBank()));
                mReBankAccount.setText(StringUtil.StringToNull(detailsEntity.getReceivingBankAccount()));

                ordersList = detailsEntity.getOrders();
                if (ordersList != null && ordersList.size() != 0) {
                    mOrders.setText(getStrings(R.string.Payment_ten_two1) + ordersList.size() + getStrings(R.string.Payment_ten_two2));
                    mAdapter = new PaymentDetailAdapter(ordersList);
                    mRecycler.setAdapter(mAdapter);
                } else {
                    mOrders.setText(getStrings(R.string.Payment_ten_one));
                }

                hideView(mProgressbar);
                showView(mNsvContent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d("查询失败" + VolleyErrorHelper.getMessage(error, mContent));
                hideView(mProgressbar);
                tv_no_data.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.ic_bug3), null, null);
                tv_no_data.setText(VolleyErrorHelper.getMessage(error, mContent));
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

    private class PaymentDetailAdapter extends RecyclerView.Adapter<PaymentDetailAdapter.ViewHolder> {
        private List<PaymentDetailsEntity.Orders> itemsList;

        public PaymentDetailAdapter(List<PaymentDetailsEntity.Orders> ordersList) {
            this.itemsList = ordersList;
        }

        @Override
        public int getItemCount() {
            return itemsList.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_payment_detail_list, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            PaymentDetailsEntity.Orders entity = itemsList.get(position);
            holder.code.setText(entity.getOrderCode());
            holder.type.setText(entity.getType());
            holder.payAmount.setText(StringUtil.numberFormat(entity.getPaymentAmount()));
            holder.settleAmount.setText(StringUtil.numberFormat(entity.getSettleAmount()));
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            AppCompatTextView code;
            AppCompatTextView type;
            AutofitTextView payAmount; //支付金额
            AutofitTextView settleAmount;  //结算金额

            public ViewHolder(final View itemView) {
                super(itemView);
                code = (AppCompatTextView) itemView.findViewById(R.id.tv_PMDetail_code);
                type = (AppCompatTextView) itemView.findViewById(R.id.tv_PMDetail_type);
                payAmount = (AutofitTextView) itemView.findViewById(R.id.tv_PMDetails_paymentAmount);
                settleAmount = (AutofitTextView) itemView.findViewById(R.id.tv_PMDetails_settleAmount);
            }
        }
    }

    void onCommit() {
        dialog.title(getStrings(R.string.Payment_dialog_one)).titleTextColor(getColors(R.color.black_one_mark_87))
                .titleTextSize(18).content(getStrings(R.string.Payment_dialog_two)).contentGravity(Gravity.CENTER)
                .style(NormalDialog.STYLE_TWO).btnText(getStrings(R.string.Payment_dialog_three), getStrings(R.string.Payment_dialog_four))
                .showAnim(new BounceEnter()).dismissAnim(new ZoomInExit()).show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnBtnClickL(
                new OnBtnClickL() {//left btn click listener
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {//right btn click listener
                    @Override
                    public void onBtnClick() {
                        hud.show();
                        commitPaymentApply();
                        dialog.dismiss();
                    }
                });
    }

    void commitPaymentApply() {
//        http://183.129.133.147:10086/api/payment/commitApply?paymentApplyId=4924438
        final String url = MoreUserDal.GetServerUrl() + "/api/payment/commitApply?paymentApplyId=" + PaymentApplyId;
        LogUtils.d("提交付款单" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                LogUtils.d("付款单" + response.toString());
                //返回的是付款详情
                Intent intent = new Intent(AppDelegate.PAYMENT_DETAILS_COMMIT);
                LocalBroadcastManager.getInstance(mContent).sendBroadcastSync(intent);
                ToastUtil.showToast(mContent, "提交付款单" + PaymentApplyId + "成功。");
                hud.dismiss();
                onBackgrounds();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hud.dismiss();
                LogUtils.d("提交失败" + VolleyErrorHelper.getMessage(error, mContent));
                ToastUtil.showToast(mContent, VolleyErrorHelper.getMessage(error, mContent));
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 1, 1.0f));
        mRequestQueue.add(jsonObjectRequest);
    }

    //点击删除图标的功能
    void onDelete() {
        dialog.title(getStrings(R.string.Payment_dialog_one)).titleTextColor(getColors(R.color.black_one_mark_87))
                .titleTextSize(18).content(getStrings(R.string.Payment_dialog_two)).contentGravity(Gravity.CENTER)
                .style(NormalDialog.STYLE_TWO).btnText(getStrings(R.string.Payment_dialog_three), getStrings(R.string.Payment_dialog_five))
                .showAnim(new BounceEnter()).dismissAnim(new ZoomInExit()).show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnBtnClickL(
                new OnBtnClickL() {//left btn click listener
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {//right btn click listener
                    @Override
                    public void onBtnClick() {
                        hud.show();
                        onRemovePaymentApply();
                        dialog.dismiss();
                    }
                });
    }

    void onRemovePaymentApply() {
//        http://183.129.133.147:10086/api/payment/remove?paymentApplyId=4924448
        final String url = MoreUserDal.GetServerUrl() + "/api/payment/remove?paymentApplyId=" + PaymentApplyId;
        LogUtils.d("删除付款单" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                LogUtils.d("删除单" + response.toString());
                hud.dismiss();
                ToastUtil.showToast(mContent, "删除付款单" + PaymentApplyId + "成功。");
                Intent intent = new Intent(AppDelegate.PAYMENT_DETAILS_REMOVE);
                LocalBroadcastManager.getInstance(mContent).sendBroadcastSync(intent);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hud.dismiss();
                LogUtils.d("删除失败" + VolleyErrorHelper.getMessage(error, mContent));
                ToastUtil.showToast(mContent, VolleyErrorHelper.getMessage(error, mContent));
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 1, 1.0f));
        mRequestQueue.add(jsonObjectRequest);
    }

    private MenuItem menuItem;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_export, menu);
        menuItem = menu.findItem(R.id.action_export);
        menuItem.setEnabled(false);
        return true;
    }

    private String[] stringItems;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_export:

                if (PaymentStatus == -1) {
                    stringItems = new String[]{"付款单文件", "提交审批", "删除付款单"};
                } else {
                    stringItems = new String[]{"付款单文件"};
                }
                final ActionSheetDialog dialog = new ActionSheetDialog(mContent, stringItems, null);
                dialog.title("操作").titleHeight(40).itemHeight(40)
                        .titleTextSize_SP(14.5f)//
                        .show();
                dialog.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                onSettlePdf();
                                break;
                            case 1:
                                onCommit();
                                break;
                            case 2:
                                onDelete();
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void onSettlePdf() {
//       http://www.tjzmt.com:9080/manage/vatInvoice/VatInvoiceExportPdf/5064503
        String url = AppDelegate.PAYMENT_SETTLE_PDF_URL + PaymentApplyId;
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        mRequestQueue.cancelAll(this);
        super.onDestroy();
    }
}
