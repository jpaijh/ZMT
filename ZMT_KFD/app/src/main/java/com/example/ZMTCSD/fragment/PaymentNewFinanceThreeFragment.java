package com.example.ZMTCSD.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
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
import com.example.ZMTCSD.activity.PaymentNewActivity;
import com.example.ZMTCSD.activity.PaymentNewCurrencyActivity_;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.entity.PaymentAddApplyEntity;
import com.example.ZMTCSD.entity.PaymentDataEntity;
import com.example.ZMTCSD.entity.PaymentFinanceOrderEntity;
import com.example.ZMTCSD.utils.AnimUtils;
import com.example.ZMTCSD.utils.MyInputFilter;
import com.example.ZMTCSD.utils.StringUtil;
import com.example.ZMTCSD.utils.ToastUtil;
import com.example.ZMTCSD.utils.VolleyErrorHelper;
import com.example.ZMTCSD.view.AppCompatClearEditText;
import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.github.ikidou.fragmentBackHandler.FragmentBackHandler;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.suke.widget.SwitchButton;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * 待客提款 融资账户中第3页的功能
 */
@EFragment(R.layout.fragment_payment_new_threefin)
public class PaymentNewFinanceThreeFragment extends BaseFragment implements FragmentBackHandler {
    private Context mContent;
    private FragmentManager fm;
    private PaymentNewActivity ThisActivity;
    private RequestQueue mRequestQueue;
    private final static String FINANCE_PAGER_FOUR = "finance_pager_four";
    private final static String FINANCE_PAGER_THREE = "finance_pager_three";
    private final static int PAYMENTCURRENT = 13;//去选择 币别
    private KProgressHUD hud;
    private String json; //这是发送的jsog格式
    private List<PaymentDataEntity.CurrencyList> currencyList;
    private PaymentDataEntity.CurrencyList CurrencyEntity;
    private PaymentFinanceOrderEntity orderEntity;
    private PaymentFinanceOrderEntity.PaymentOrder paymentOrder;
    private List<PaymentAddApplyEntity.PaymentOrderList> orderList = new ArrayList<>();

    @ViewById(R.id.tv_pyNew_currency)
    AppCompatTextView mCurrency; //币别

    @ViewById(R.id.med_pyNew_RMBRate)
    AppCompatClearEditText mRmbRate; //汇率

    @ViewById(R.id.tv_pyNew_PaymentAmount)
    AppCompatTextView mPaymentAmount; //支付金额

    @ViewById(R.id.tv_pyNew_SettleAmount)
    AppCompatTextView mSettleAmount; //结算金额

    @ViewById(R.id.tv_pyNew_hint)
    AppCompatTextView AmountHint;

    @ViewById(R.id.btn_pyNew_switch)
    SwitchButton switchBtn;

    @ViewById(R.id.btn_pyNew_next)
    AppCompatButton mBtnNext;

    @ViewById(R.id.tv_spiltOrder_code)
    AppCompatTextView spiltCode; //订单中的

    @ViewById(R.id.tv_spiltOrder_settleAmount)
    AppCompatTextView spiltAmount; //结算金额

    @Override
    public void onAfterViews() {
        mContent = getActivity();
        ThisActivity = (PaymentNewActivity) mContent;
        mRequestQueue = Volley.newRequestQueue(mContent);
        fm = getActivity().getSupportFragmentManager();
        currencyList = ThisActivity.currencyList;
        CurrencyEntity = currencyList.get(0);
        orderEntity = (PaymentFinanceOrderEntity) getArguments().getSerializable(AppDelegate.PYNEW_FINANCE_ORDERENTITY);
        mCurrency.setText(CurrencyEntity.getCurrency());
        mRmbRate.setText(StringUtil.numberForRate(currencyList.get(0).getRmbRate()));
        paymentOrder = orderEntity.getPaymentOrder();
        switchBtn.setEnabled(false);
        spiltCode.setText(paymentOrder.getOrderCode());
        spiltAmount.setText(getStrings(R.string.Payment_newFinance_five) + StringUtil.numberFormat1(paymentOrder.getPaymentAmount()) + " "
                + getStrings(R.string.Payment_newFinance_six) + StringUtil.numberFormat1(paymentOrder.getSettleAmount()));
        mPaymentAmount.setText(StringUtil.numberFormat1(paymentOrder.getPaymentAmount()));
        mSettleAmount.setText(StringUtil.numberFormat1(paymentOrder.getSettleAmount()));
        AmountHint.setText(getStrings(R.string.Payment_new_twenty_three)+" "+StringUtil.numberDecimal(ThisActivity.pyAccount.getFinancialAmount()));
        initTextChanger();
        hud = KProgressHUD.create(mContent).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("正在提款，请等待.").setCancellable(false);
        super.onAfterViews();
    }

    private void initTextChanger() {
        mRmbRate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mRmbRate.requestFocus();
                return false;
            }
        });
        mRmbRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mRmbRate.length() != 0 && mPaymentAmount.length() != 0 && mSettleAmount.length() != 0 &&
                        Double.valueOf(charSequence.toString()) > 0.00) {
                    mBtnNext.setEnabled(true);
                } else {
                    mBtnNext.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 4) {
                    edt.delete(posDot + 5, posDot + 6);
                }
            }
        });
        mRmbRate.setFilters(new InputFilter[]{new MyInputFilter()});
    }

    //选择币别
    @Click(R.id.ll_pyNew_currency)
    void onCurrency() {
        Intent intent = new Intent(mContent, PaymentNewCurrencyActivity_.class);
        intent.putExtra(AppDelegate.PYNEW_CURRENCYLIST, (Serializable) currencyList);
        intent.putExtra(AppDelegate.PYNEW_CURRENCY, CurrencyEntity);
        startActivityForResult(intent, PAYMENTCURRENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (PAYMENTCURRENT == requestCode && resultCode == RESULT_OK) {
            CurrencyEntity = (PaymentDataEntity.CurrencyList) data.getSerializableExtra(AppDelegate.PYNEW_CURRENCY);
            mCurrency.setText(CurrencyEntity.getCurrency());
            mRmbRate.setText(StringUtil.numberForRate(CurrencyEntity.getRmbRate()));
            mRmbRate.requestFocus();
        }
    }

    @Click(R.id.rl_spiltOrder_fin)
    void onSpiltOrder() {
        FragmentTransaction f = fm.beginTransaction();
        AnimUtils.TransactionFragmentShow(f);
        PaymentNewFinanceFourFragment_ financeFourFragment = new PaymentNewFinanceFourFragment_();
        financeFourFragment.addListener(new PaymentNewFinanceFourFragment.BackClickListener() {
            @Override
            public void OnClickListener(int pager, double name, double value) {
                if (pager == 15) {
                    paymentOrder.setPaymentAmount(name);
                    paymentOrder.setSettleAmount(value);
                    mPaymentAmount.setText(StringUtil.numberFormat1(name));
                    mSettleAmount.setText(StringUtil.numberFormat1(value));
                    spiltAmount.setText(getStrings(R.string.Payment_newFinance_five) + StringUtil.numberFormat1(name) + " "
                            + getStrings(R.string.Payment_newFinance_six) + StringUtil.numberFormat1(value));
                    if (mRmbRate.length()!= 0 && Double.valueOf(mRmbRate.getText().toString())> 0.00
                            &&value<=ThisActivity.pyAccount.getFinancialAmount()) {
                        mBtnNext.setEnabled(true);
                    }
                }
                FragmentTransaction f = fm.beginTransaction();
//                TransactionFragment(f);
                f.remove(fm.findFragmentByTag(FINANCE_PAGER_FOUR)).show(fm.findFragmentByTag(FINANCE_PAGER_THREE)).commit();
            }
        });
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppDelegate.PYNEW_FINANCE_ORDERENTITY, paymentOrder);
        financeFourFragment.setArguments(bundle);
        f.add(R.id.fragment, financeFourFragment, FINANCE_PAGER_FOUR).hide(fm.findFragmentByTag(FINANCE_PAGER_THREE)).commit();
    }

    @Click(R.id.btn_pyNew_next)
    void addPaymentApply() {
        PaymentAddApplyEntity entity = new PaymentAddApplyEntity();
        entity.setCompanyId(orderEntity.getPaymentAccounts().get(0).getCompanyId());
        entity.setCompanyName(orderEntity.getPaymentAccounts().get(0).getCompanyName());
        entity.setBankName(orderEntity.getPaymentAccounts().get(0).getBankName());
        entity.setBankAccount(orderEntity.getPaymentAccounts().get(0).getBankAccount());
        entity.setRMBRate(mRmbRate.getText().toString());
        entity.setCurrency(mCurrency.getText().toString());
        entity.setPaymentAmount(mPaymentAmount.getText().toString());
        entity.setSettleAmount(mSettleAmount.getText().toString());
        entity.setAccountId(ThisActivity.pyAccount.getAccountId());
        entity.setAccountCode(ThisActivity.pyAccount.getAccountCode());
        entity.setAccountName(ThisActivity.pyAccount.getAccountName());
        entity.setPaymentMethodId(ThisActivity.methodEntity.getValue());
        entity.setPaymentMethodName(ThisActivity.methodEntity.getName());
        entity.setGuaranteeTypeId(ThisActivity.guaranteeEntity.getValue());
        entity.setGuaranteeTypeName(ThisActivity.guaranteeEntity.getName());
        entity.setPaymentTypeId(ThisActivity.subTypeEntity.getPaymentSubTypeId());
        entity.setPaymentTypeName(ThisActivity.subTypeEntity.getPaymentSubTypeName());
        entity.setFundSource(ThisActivity.fundSource);
        PaymentAddApplyEntity.PaymentOrderList order = new PaymentAddApplyEntity.PaymentOrderList
                (paymentOrder.getId(), paymentOrder.getOrderCode(), paymentOrder.getPaymentAmount(), paymentOrder.getSettleAmount());
        orderList.add(order);
        entity.setPaymentOrders(orderList);
        json = JSONObject.toJSONString(entity);
        LogUtils.e("待客提款" + json.toString());
        hud.show();
        onBackgrounds();
    }

    @Override
    public void onBackgrounds() {
        final String url = MoreUserDal.GetServerUrl() + "/api/payment/addPaymentApply";
        LogUtils.d(" 待客提款" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                LogUtils.d("" + response.toString());
                hud.dismiss();
                LocalBroadcastManager.getInstance(mContent).sendBroadcastSync(new Intent(AppDelegate.PAYMENT_NEW_FRAGMENT));
                ThisActivity.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d("待客提款" + VolleyErrorHelper.getMessage(error, mContent));
                hud.dismiss();
                ToastUtil.showToast(mContent, VolleyErrorHelper.getMessage(error, mContent));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "bearer   " + MoreUserDal.GetAccessToken());
                map.put("Content-Type", "application/json");
                return map;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() {
                return json.getBytes();
            }
        };
        jsonObjectRequest.setTag(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 1, 1.0f));
        mRequestQueue.add(jsonObjectRequest);
        super.onBackgrounds();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mRequestQueue.cancelAll(mContent);
        super.onDestroy();
    }

    boolean backHandled = true;

    @Override
    public boolean onBackPressed() {
        if (backHandled) {
            if (mListener != null) {
                mListener.OnClickListener(13);
            }
            //外理返回键
            return true;
        } else {
            // 如果不包含子Fragment
            // 或子Fragment没有外理back需求
            // 可如直接 return false;
            // 注：如果Fragment/Activity 中可以使用ViewPager 代替 this
            return BackHandlerHelper.handleBackPress(this);
        }
    }

    private BackClickListener mListener;

    public void addListener(BackClickListener mListener) {
        this.mListener = mListener;
    }

    // 接口
    public interface BackClickListener {
        void OnClickListener(int pager);
    }
}
