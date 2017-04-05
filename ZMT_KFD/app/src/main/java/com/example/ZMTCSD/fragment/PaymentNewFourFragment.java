package com.example.ZMTCSD.fragment;

import android.content.Context;
import android.content.Entity;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.activity.PaymentNewActivity;
import com.example.ZMTCSD.activity.PaymentNewOrdersActivity_;
import com.example.ZMTCSD.entity.PaymentOrderEntity;
import com.example.ZMTCSD.utils.MyInputFilter;
import com.example.ZMTCSD.utils.StringUtil;
import com.example.ZMTCSD.view.AppCompatClearEditText;
import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.github.ikidou.fragmentBackHandler.FragmentBackHandler;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * 资金账户的4
 */
@EFragment(R.layout.fragment_payment_new_four)
public class PaymentNewFourFragment extends BaseFragment implements FragmentBackHandler {
    private Context mContent;
    private PaymentNewActivity ThisActivity;
    private PaymentOrderEntity PaymentOrder;
    private ArrayList<String> codeList;
    private int orderId = 0;//
    private final static int PAYMENT_FOUR_ORDER = 8;
    private PaymentOrderEntity orderEntity; //返回订单实体类

    @ViewById(R.id.ll_pyNewFour_order)
    RelativeLayout rl_order;

    @ViewById(R.id.image_right)
    AppCompatImageView imgRight;

    @ViewById(R.id.tv_pyNewFour_name)
    AppCompatTextView orderCode;

    @ViewById(R.id.tv_pyNewFour_invoiceAmount)
    AppCompatTextView invoiceAmount;

    @ViewById(R.id.tv_pyNewFour_paidAmount)
    AppCompatTextView paidAmount;

    @ViewById(R.id.tv_pyNewFour_UnpaidAmount)
    AppCompatTextView UnpaidAmount;

    @ViewById(R.id.ll_pyNewFour_spiltAmount)
    LinearLayout SpiltAmount;

    @ViewById(R.id.ed_pyNewFour_paymentAmount)
    AppCompatClearEditText ed_payment;

    @ViewById(R.id.ed_pyNewFour_settleAmount)
    AppCompatClearEditText ed_settle;

    @ViewById(R.id.btn_pyNewFour_next)
    AppCompatButton btn_next;

    @Override
    public void onAfterViews() {
        mContent = getActivity();
        ThisActivity = (PaymentNewActivity) mContent;
        PaymentOrder = (PaymentOrderEntity) getArguments().getSerializable(AppDelegate.PYNEW_FOUR_ORDERENTITY);
        codeList = getArguments().getStringArrayList(AppDelegate.PYNEW_FOUR_ORDERCODE);
        LogUtils.e(PaymentOrder.toString() + "：资金3传来:" + codeList.toString());
        //TODO 没有做修改金额的准备
        if (PaymentOrder.getId() != 0) {
            showView(SpiltAmount);
            hideView(imgRight);
            rl_order.setBackgroundColor(getColors(R.color.white));
            orderCode.setTextColor(getColors(R.color.black_two_mark_54));
            orderCode.setText(PaymentOrder.getOrderCode());
            invoiceAmount.setText(StringUtil.numberDecimal(PaymentOrder.getInvoiceAmount()));
            paidAmount.setText(StringUtil.numberDecimal(PaymentOrder.getPaidAmount()));
            UnpaidAmount.setText(StringUtil.numberDecimal(PaymentOrder.getUnPaidAmount()));
            ed_payment.setText(StringUtil.numberFormat1(PaymentOrder.getPaymentAmount()));
            ed_settle.setText(StringUtil.numberFormat1(PaymentOrder.getSettleAmount()));
            btn_next.setText(getStrings(R.string.Payment_newFinance4_seven));
            btn_next.setEnabled(true);
        } else {
            rl_order.setBackgroundResource(R.drawable.selector_mine_item_background);
            showView(imgRight);
            orderCode.setTextColor(getColors(R.color.black_one_mark_87));
            rl_order.setOnClickListener(click);
        }
        initTextChanger();
        super.onAfterViews();
    }

    private void initTextChanger() {
        ed_payment.setFilters(new InputFilter[]{new MyInputFilter()});
        ed_payment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (ed_payment.length() != 0 && ed_settle.length() != 0 && Double.valueOf(charSequence.toString()) > 0.00
                        && Double.valueOf(ed_settle.getText().toString()) > 0.00) {
                    btn_next.setEnabled(true);
                } else {
                    btn_next.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                //以小数点为界分割字符串
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    edt.delete(posDot + 3, posDot + 4);
                }
            }
        });
        ed_settle.setFilters(new InputFilter[]{new MyInputFilter()});
        ed_settle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (ed_payment.length() != 0 && ed_settle.length() != 0 && Double.valueOf(charSequence.toString()) > 0.00
                        && Double.valueOf(ed_payment.getText().toString()) > 0.00) {
                    btn_next.setEnabled(true);
                } else {
                    btn_next.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                //以小数点为界分割字符串
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    edt.delete(posDot + 3, posDot + 4);
                }
            }
        });
    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContent, PaymentNewOrdersActivity_.class);
            intent.putExtra(AppDelegate.PYNEW_CUSTOMERID, ThisActivity.PaymentId);
            intent.putExtra(AppDelegate.PYNEW_FOUR_ORDERCODE, codeList); //
            startActivityForResult(intent, PAYMENT_FOUR_ORDER);
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (PAYMENT_FOUR_ORDER == requestCode && resultCode == RESULT_OK) {
            orderEntity = (PaymentOrderEntity) data.getSerializableExtra(AppDelegate.PYNEW_FOUR_ORDERENTITY);
            orderCode.setText(orderEntity.getOrderCode());
            invoiceAmount.setText(StringUtil.numberDecimal(orderEntity.getInvoiceAmount()));
            paidAmount.setText(StringUtil.numberDecimal(orderEntity.getPaidAmount()));
            UnpaidAmount.setText(StringUtil.numberDecimal(orderEntity.getUnPaidAmount()));
            showView(SpiltAmount);
        }
    }

    @Click(R.id.btn_pyNewFour_next)
    void onBtnNext() {
        if (mListener != null) {
//            LogUtils.d(ed_payment.getText().toString()+"你"+ed_settle.getText().toString());
            double payment = Double.valueOf(ed_payment.getText().toString());
            double settle = Double.valueOf(ed_settle.getText().toString());
            if (PaymentOrder.getId() != 0) {
                PaymentOrder.setPaymentAmount(payment);
                PaymentOrder.setSettleAmount(settle);
                LogUtils.e("返回到资金3：" + PaymentOrder.toString());
                mListener.OnClickListener(5, PaymentOrder);
            } else {
                orderEntity.setPaymentAmount(payment);
                orderEntity.setSettleAmount(settle);
                LogUtils.e("返回到资金3：" + orderEntity.toString());
                mListener.OnClickListener(5, orderEntity);
            }
        }
    }

    @Override
    public void onBackgrounds() {
        super.onBackgrounds();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    boolean backHandled = true;

    @Override
    public boolean onBackPressed() {
        if (backHandled) {
            if (mListener != null) {
                mListener.OnClickListener(4, null);
            }
            //外理返回键
            return true;
        } else {
            return BackHandlerHelper.handleBackPress(this);
        }
    }

    private BackClickListener mListener;

    public void addListener(BackClickListener mListener) {
        this.mListener = mListener;
    }

    // 接口
    public interface BackClickListener {
        void OnClickListener(int pager, PaymentOrderEntity entity);
    }

}
