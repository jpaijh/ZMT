package com.example.ZMTCSD.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.activity.PaymentNewActivity;
import com.example.ZMTCSD.activity.PaymentNewOrderActivity_;
import com.example.ZMTCSD.activity.PaymentNewSelectNameActivity_;
import com.example.ZMTCSD.entity.PaymentFinanceOrderEntity;
import com.example.ZMTCSD.utils.AnimUtils;
import com.example.ZMTCSD.utils.StringUtil;
import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.github.ikidou.fragmentBackHandler.FragmentBackHandler;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;

import static android.app.Activity.RESULT_OK;

/**
 * 待客提款 融资账户中第2页的功能
 */
@EFragment(R.layout.fragment_payment_new_twofin)
public class PaymentNewFinanceFragment extends BaseFragment implements FragmentBackHandler {
    private Context mContent;
    private FragmentManager fm;
    private PaymentNewActivity ThisActivity;
    private final static String FINANCE_PAGER_THREE = "finance_pager_three";
    private final static String FINANCE_PAGER_TWO = "finance_pager_two";
    public int AccountId;//提款客户传来的id 是订单的id
    public int orderId = -1; //你选择订单的id;
    private final static int PAYMENT_FINANCE_ORDER = 11;
    private final static int PAYMENT_ORDER_ACCOUNTNAME = 12;
    public PaymentFinanceOrderEntity orderEntity = new PaymentFinanceOrderEntity();
    private PaymentFinanceOrderEntity.PaymentAccountList paymentAccounts;

    @ViewById(R.id.tv_pyNew_orderCode)
    AppCompatTextView orderCode;

    @ViewById(R.id.tv_pyNew_Amount)
    AppCompatTextView orderAmount;

    @ViewById(R.id.ll_pyNew_companyName)
    LinearLayout ll_companyName;

    @ViewById(R.id.tv_pyNew_nameLeft)
    AppCompatTextView nameLeft;

    @ViewById(R.id.tv_pyNew_companyName)
    AppCompatTextView companyName;

    @ViewById(R.id.tv_pyNew_bankName)
    AppCompatTextView bankName;

    @ViewById(R.id.tv_pyNew_bankAccount)
    AppCompatTextView bankAccount;

    @ViewById(R.id.btn_pyNew_next)
    AppCompatButton mBtnNext;

    @Override
    public void onAfterViews() {
        mContent = getActivity();
        fm = getActivity().getSupportFragmentManager();
        ThisActivity = (PaymentNewActivity) mContent;
        AccountId = ThisActivity.pyAccount.getAccountId();
        super.onAfterViews();
    }

    @Click(R.id.rl_order)
    void onCompanyOrder() {
        Intent intent = new Intent(mContent, PaymentNewOrderActivity_.class);
        intent.putExtra(AppDelegate.PYNEW_CUSTOMERID, AccountId);
        intent.putExtra(AppDelegate.PYNEW_COMPANYID, orderId);
        startActivityForResult(intent, PAYMENT_FINANCE_ORDER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (PAYMENT_FINANCE_ORDER == requestCode && resultCode == RESULT_OK) {
            orderEntity = (PaymentFinanceOrderEntity) data.getSerializableExtra(AppDelegate.PYNEW_FINANCE_ORDERENTITY);
            orderId = orderEntity.getOrderId();
            //TODO 每当改变订单的时， 应该将FINANCE_PAGER_THREE 给删除掉，不然里面的数据不会更改
            AnimUtils.removeFragment(fm, FINANCE_PAGER_THREE);
            orderCode.setText(orderEntity.getOrderCode());
            orderAmount.setText(getStrings(R.string.Payment_newFinance_four) + " " + StringUtil.numberDecimal(orderEntity.getPaymentAmount()));
            showView(orderAmount);
            paymentAccounts = orderEntity.getPaymentAccounts().get(0);
            companyName.setText(paymentAccounts.getCompanyName());
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_arrow_next);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//对图片进行压缩
            companyName.setCompoundDrawables(null, null, drawable, null);
            nameLeft.setTextColor(getColors(R.color.black_one_mark_87));
            bankName.setText(paymentAccounts.getBankName());
            bankAccount.setText(paymentAccounts.getBankAccount());
            ll_companyName.setBackgroundResource(R.drawable.selector_mine_item_background);
            ll_companyName.setOnClickListener(click);
            mBtnNext.setEnabled(true);
        } else if (PAYMENT_ORDER_ACCOUNTNAME == requestCode && resultCode == RESULT_OK) {
            paymentAccounts = (PaymentFinanceOrderEntity.PaymentAccountList)
                    data.getSerializableExtra(AppDelegate.PYNEW_PAYMENTACCOUNTENTITY);
            companyName.setText(paymentAccounts.getCompanyName());
            bankName.setText(paymentAccounts.getBankName());
            bankAccount.setText(paymentAccounts.getBankAccount());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContent, PaymentNewSelectNameActivity_.class);
            intent.putExtra(AppDelegate.PYNEW_PAYMENTACCOUNT, companyName.getText().toString());
            intent.putExtra(AppDelegate.PYNEW_PAYMENTACCOUNTENTITY, (Serializable) orderEntity.getPaymentAccounts());
            startActivityForResult(intent, PAYMENT_ORDER_ACCOUNTNAME);
        }
    };

    @Click(R.id.btn_pyNew_next)
    void onNextPager() {
        FragmentTransaction f = fm.beginTransaction();
        AnimUtils.TransactionFragmentShow(f);
        if (fm.findFragmentByTag(FINANCE_PAGER_THREE) != null) {
            f.show(fm.findFragmentByTag(FINANCE_PAGER_THREE)).hide(fm.findFragmentByTag(FINANCE_PAGER_TWO)).commit();
        } else {
            PaymentNewFinanceThreeFragment_ financeThreeFragment = new PaymentNewFinanceThreeFragment_();
            financeThreeFragment.addListener(new PaymentNewFinanceThreeFragment.BackClickListener() {
                @Override
                public void OnClickListener(int pager) {
                    FragmentTransaction f = fm.beginTransaction();
//                    AnimUtils.TransactionFragmentHide(f);
                    f.hide(fm.findFragmentByTag(FINANCE_PAGER_THREE)).show(fm.findFragmentByTag(FINANCE_PAGER_TWO)).commit();
                }
            });
            Bundle bundle = new Bundle();
            bundle.putSerializable(AppDelegate.PYNEW_FINANCE_ORDERENTITY, orderEntity);//将选择的订单整个
            financeThreeFragment.setArguments(bundle);
            f.add(R.id.fragment, financeThreeFragment, FINANCE_PAGER_THREE).hide(fm.findFragmentByTag(FINANCE_PAGER_TWO)).commit();
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
                mListener.OnClickListener(2);
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
