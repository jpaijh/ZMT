package com.example.ZMTCSD.fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.widget.RelativeLayout;
import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.activity.PaymentNewActivity;
import com.example.ZMTCSD.activity.PaymentNewBanksActivity_;
import com.example.ZMTCSD.activity.PaymentNewCompanysActivity_;
import com.example.ZMTCSD.entity.CompanyBanksEntity;
import com.example.ZMTCSD.entity.CompanyListEntity;
import com.example.ZMTCSD.utils.AnimUtils;
import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.github.ikidou.fragmentBackHandler.FragmentBackHandler;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import static android.app.Activity.RESULT_OK;
import static com.example.ZMTCSD.utils.AnimUtils.removeFragment;

/**
 *   待客提款 资金账户 中第2页的功能
 */
@EFragment(R.layout.fragment_payment_new_two)
public class PaymentNewTwoFragment extends BaseFragment implements FragmentBackHandler {
    private Context mContent;
    private FragmentManager fm;
    private PaymentNewActivity ThisActivity;
    private final static String  PAGER_THREE="pagerThree";
    private final static String  PAGER_TWO="pagerTwo";
    private  final static int PAYMNETCOMPANY=5;
    private  final static int PAYMENTBANK=6;
    private  int AccountId;//是付款单位的id
    //由你选择的付款单位得来的id,是付款银行请求的companyId；
    private int companyId= 0 ;
    //你选择那个付款银行的id
    private int companyBankId=0;
    public CompanyListEntity companyEntity=new CompanyListEntity();
    public CompanyBanksEntity companyBankEntity=new CompanyBanksEntity();

    @ViewById(R.id.rl_bankAccount)
    RelativeLayout mBankAccount;

    @ViewById(R.id.ll_pyNew_name)
    AppCompatTextView AccountName; //名称

    @ViewById(R.id.ll_pyNew_statusStr)
    AppCompatTextView AccountStatus; //状态

    @ViewById(R.id.ll_pyNew_bankName)
    AppCompatTextView bankName;

    @ViewById(R.id.ll_pyNew_bankAccount)
    AppCompatTextView bankAccount;

    @ViewById(R.id.btn_pyNew_next)
    AppCompatButton mBtnNext;

    @Override
    public void onAfterViews() {
        mContent=getActivity();
        fm =getActivity().getSupportFragmentManager();
        ThisActivity= (PaymentNewActivity) mContent;
        AccountId=ThisActivity.pyAccount.getAccountId();
//        companyId=ThisActivity.pyAccount.getDefaultCompanyId();
//        AccountName.setText(ThisActivity.pyAccount.getDefaultCompanyName());
//        companyEntity.setId(AccountId);
//        companyEntity.setName(ThisActivity.pyAccount.getAccountName());
        super.onAfterViews();
    }

    @Click(R.id.rl_bankName)
    void onCompanyName(){
        Intent intent=new Intent(mContent,PaymentNewCompanysActivity_.class);
        LogUtils.d(AccountId+"::"+companyId);
        intent.putExtra(AppDelegate.PYNEW_CUSTOMERID,AccountId);
        intent.putExtra(AppDelegate.PYNEW_COMPANYID,companyId);
        startActivityForResult( intent,PAYMNETCOMPANY);
    }

    @Click(R.id.rl_bankAccount)
    void onCompanyBank(){
        Intent intent=new Intent(mContent,PaymentNewBanksActivity_.class);
        intent.putExtra(AppDelegate.PYNEW_CUSTOMERID,companyId);
        intent.putExtra(AppDelegate.PYNEW_COMPANYID,companyBankId);
        startActivityForResult(intent,PAYMENTBANK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( PAYMNETCOMPANY ==requestCode && resultCode==RESULT_OK){
            companyEntity= (CompanyListEntity) data.getSerializableExtra(AppDelegate.PYNEW_COMPANYENTITY);
            AnimUtils.removeFragment(fm,PAGER_THREE);
            companyId=companyEntity.getId();
            AccountName.setText( companyEntity.getName());
            AccountStatus.setText( companyEntity.getStatusStr());
            showView(AccountStatus);
            hideView(bankAccount);
            bankName.setText("请选择收款银行");
            bankAccount.setText("");
        }else if(PAYMENTBANK==requestCode && resultCode==RESULT_OK){
                companyBankEntity= (CompanyBanksEntity) data.getSerializableExtra(AppDelegate.PYNEW_COMPANYBANKENTITY);
                AnimUtils.removeFragment(fm,PAGER_THREE);
                companyBankId=   companyBankEntity.getId();
                bankName.setText( companyBankEntity.getBankName());
                bankAccount.setText(companyBankEntity.getBankAccount());
                showView(bankAccount);
                mBtnNext.setEnabled(true);
        }
    }

    @Click(R.id.btn_pyNew_next)
    void onNextPager(){
        FragmentTransaction f=fm.beginTransaction();
        AnimUtils.TransactionFragmentShow(f);
        if( fm.findFragmentByTag(PAGER_THREE) !=null ){
            f.show(fm.findFragmentByTag(PAGER_THREE)).hide(fm.findFragmentByTag(PAGER_TWO)).commit();
        }else{
            PaymentNewThreeFragment_ PaymentThree=new PaymentNewThreeFragment_();
            PaymentThree.addListener(BackThreeClick);
            Bundle bundle = new Bundle();
            bundle.putSerializable( AppDelegate.PYNEW_COMPANYENTITY,companyEntity);
            bundle.putSerializable(AppDelegate.PYNEW_COMPANYBANKENTITY,companyBankEntity);
            PaymentThree.setArguments(bundle);
            f.add(R.id.fragment ,PaymentThree ,PAGER_THREE).hide(fm.findFragmentByTag(PAGER_TWO)).commit();
        }
    }

    PaymentNewThreeFragment.BackClickListen BackThreeClick=new PaymentNewThreeFragment.BackClickListen() {
        @Override
        public void OnListener(int type) {
            FragmentTransaction f=fm.beginTransaction();
//            AnimUtils.TransactionFragmentHide(f);
            f.hide(fm.findFragmentByTag(PAGER_THREE) ).show(fm.findFragmentByTag(PAGER_TWO)).commit();
        }
    };

    @Override
    public void onBackgrounds() {
        super.onBackgrounds();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    boolean backHandled=true;
    @Override
    public boolean onBackPressed() {
        if (backHandled) {
            if( mListener != null){
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
        void OnClickListener( int pager);
    }
}
