package com.example.ZMTCSD.fragment;


import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;

import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.entity.PaymentFinanceOrderEntity;
import com.example.ZMTCSD.utils.MyInputFilter;
import com.example.ZMTCSD.utils.StringUtil;
import com.example.ZMTCSD.utils.ToastUtil;
import com.example.ZMTCSD.view.AppCompatClearEditText;
import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.github.ikidou.fragmentBackHandler.FragmentBackHandler;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * 拆分到订单
 */
@EFragment(R.layout.fragment_payment_new_fourfin)
public class PaymentNewFinanceFourFragment extends BaseFragment implements FragmentBackHandler {
    private Context mContent;
    private PaymentFinanceOrderEntity.PaymentOrder paymentOrder;

    @ViewById(R.id.tv_financeFour_name)
    AppCompatTextView OrderCode;

    @ViewById(R.id.tv_financeFour_invoiceAmount)
    AppCompatTextView invoiceAmount;

    @ViewById(R.id.tv_financeFour_paidAmount)
    AppCompatTextView paidAmount;

    @ViewById(R.id.tv_financeFour_UnpaidAmount)
    AppCompatTextView UnpaidAmount;

    @ViewById(R.id.ed_financeFour_paymentAmount)
    AppCompatClearEditText ed_payment;

    @ViewById(R.id.ed_financeFour_settleAmount)
    AppCompatClearEditText ed_settle;

    @ViewById(R.id.btn_financeFour_next)
    AppCompatButton btn_next;

    @Override
    public void onAfterViews() {
        paymentOrder = (PaymentFinanceOrderEntity.PaymentOrder)
                getArguments().getSerializable(AppDelegate.PYNEW_FINANCE_ORDERENTITY);
        LogUtils.e("融资3来的" + paymentOrder.getOrderCode());
        initTextChanger();
        OrderCode.setText(paymentOrder.getOrderCode());
        invoiceAmount.setText(StringUtil.numberDecimal(paymentOrder.getInvoiceAmount()));
        paidAmount.setText(StringUtil.numberDecimal(paymentOrder.getPaidAmount()));
        UnpaidAmount.setText(StringUtil.numberDecimal(paymentOrder.getUnPaidAmount()));
        ed_payment.setText(StringUtil.numberFormat1(paymentOrder.getPaymentAmount()));
        ed_settle.setText(StringUtil.numberFormat1(paymentOrder.getSettleAmount()));
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
                if(ed_payment.length()!=0 &&ed_settle.length()!=0 &&Double.valueOf(charSequence.toString())> 0.00
                        && Double.valueOf(ed_settle.getText().toString()) > 0.00){
                    btn_next.setEnabled(true);
                }else{
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
                if(ed_payment.length()!=0 &&ed_settle.length()!=0&&Double.valueOf(charSequence.toString())> 0.00
                        && Double.valueOf(ed_payment.getText().toString()) > 0.00 ){
                    btn_next.setEnabled(true);
                }else{
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

    @Click(R.id.btn_financeFour_next)
    void onBtnNext() {
        if (mListener != null) {
            double payment = Double.valueOf(ed_payment.getText().toString());
            double settle = Double.valueOf(ed_settle.getText().toString());
            if (payment == 0 || settle == 0) {
                ToastUtil.showToast(mContent, "支付金额和结算金额不能为0.");
            } else {
                paymentOrder.setPaymentAmount(payment);
                paymentOrder.setSettleAmount(settle);
                mListener.OnClickListener(15, payment, settle);
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
                mListener.OnClickListener(14, 0, 0);
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
        void OnClickListener(int pager, double name, double value);
    }
}

