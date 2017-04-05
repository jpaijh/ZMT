package com.example.ZMTCSD.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
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
import com.example.ZMTCSD.entity.CustomerListEntity;
import com.example.ZMTCSD.entity.PaymentAccountEntity;
import com.example.ZMTCSD.entity.PaymentDataEntity;
import com.example.ZMTCSD.fragment.PaymentNewFinanceFragment;
import com.example.ZMTCSD.fragment.PaymentNewFinanceFragment_;
import com.example.ZMTCSD.fragment.PaymentNewTwoFragment;
import com.example.ZMTCSD.fragment.PaymentNewTwoFragment_;
import com.example.ZMTCSD.utils.AnimUtils;
import com.example.ZMTCSD.utils.DeviceUtil;
import com.example.ZMTCSD.utils.ToastUtil;
import com.example.ZMTCSD.utils.VolleyErrorHelper;
import com.example.ZMTCSD.view.AndroidSegmentedControlView;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *   待客提款
 */
@EActivity(R.layout.activity_payment_new)
public class PaymentNewActivity extends BaseActivity {
    private final static  int PAYMENTECUSTOMER=1;
    private final static int PAYMENTMETHOD=2;
    private final static int PATMENTGUARABTEE=3;
    private final static int PAYMENTSUBTYPE=4;
    private final static String  PAGER_TWO="pagerTwo";
    private final static  String  FINANCE_PAGER_TWO="finance_pager_two";
    private  FragmentManager fm;
    private Context mContent;
    private RequestQueue mRequestQueue;
    private KProgressHUD hud;
    private PaymentDataEntity paymentDataEntity;//元数据
    public PaymentAccountEntity pyAccount; //客户的资金信息
    private List<PaymentDataEntity.PaymentTypeList> TypeList;
    private List<PaymentDataEntity.PaymentMethodList> methodList;
    private List<PaymentDataEntity.GuaranteeTypeList> guaranteeList;
    public  List<PaymentDataEntity.CurrencyList> currencyList;
    private List<PaymentDataEntity.SubTypeList> subTypeList;
    public int fundSource=0;
    public int PaymentId= -1;//开始传递到选择客户的界面
    public String PaymentName;
    private CustomerListEntity customerEntity;
    public PaymentDataEntity.PaymentMethodList methodEntity;//付款方式
    public PaymentDataEntity.GuaranteeTypeList guaranteeEntity;//担保方式
    public PaymentDataEntity.SubTypeList subTypeEntity;  //费用方式

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    @ViewById(R.id.nested_scroll_view)
    NestedScrollView mNestedView;

    //这是选择客户后，返回的数据
    @ViewById(R.id.ll_pyNew_top)
    LinearLayout ll_top;

    @ViewById(R.id.ll_pyNew_accountName)
    AppCompatTextView accountName;

    @ViewById(R.id.ll_pyNew_nameOne)
    AppCompatTextView nameOne;

    @ViewById(R.id.ll_pyNew_valueOne)
    AppCompatTextView valueOne;

    @ViewById(R.id.ll_pyNew_nameTwo)
    AppCompatTextView nameTwo;

    @ViewById(R.id.ll_pyNew_valueTwo)
    AppCompatTextView valueTwo;

    @ViewById(R.id.ll_pyNew_nameThree)
    AppCompatTextView nameThree;

    @ViewById(R.id.ll_pyNew_valueThree)
    AppCompatTextView valueThree;

    @ViewById(R.id.ll_pyNew_nameFour)
    AppCompatTextView nameFour;

    @ViewById(R.id.ll_pyNew_valueFour)
    AppCompatTextView valueFour;

    //第一页
    @ViewById(R.id.ll_pyNew_pageOne)
    LinearLayout mPageOne;

    @ViewById(R.id.ll_pyNew_name)
    AppCompatTextView AccountName; //客户的名称

    @ViewById(R.id.ll_pyNew_statusStr)
    AppCompatTextView AccountStatus; //客户的状态

    @ViewById(R.id.tv_pyNew_Method)
    AppCompatTextView mMethod; //付款方式

    @ViewById(R.id.tv_pyNew_Type)
    AppCompatTextView Type; //账户的类型

    @ViewById(R.id.tv_pyNew_subType)
    AppCompatTextView SubType; //账户的子类型

    @ViewById(R.id.tv_pyNew_guarantee)
    AppCompatTextView mGuarantee; //担保方式

    @ViewById(R.id.btn_pyNew_next)
    AppCompatButton mBtnNext;

    @ViewById(R.id.fragment)
    FrameLayout mFragment;

    @ViewById(R.id.progressbar)
    CircleProgressBar mProgressbar;

    @ViewById(R.id.tv_volley_date)
    AppCompatTextView tv_volley;

    @ViewById(R.id.androidSegmentedControlView)
    AndroidSegmentedControlView mSegmentView;

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_title.setText(getStrings(R.string.toolbar_PaymentNew));
    }

    @Override
    public void onAfterViews() {
        mContent=PaymentNewActivity.this;
        mRequestQueue= Volley.newRequestQueue(mContent);
        fm= getSupportFragmentManager();
        hud= KProgressHUD.create(mContent).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("处理中").setSize(100,110).setCornerRadius(5);
        initToolbar();
        showView(mProgressbar);
        onBackgrounds();
        initSegment();
        super.onAfterViews();
    }

    private void initSegment() {
        try {
            mSegmentView.setItems(getsStrings(R.array.payment_new_option), null);
            mSegmentView.setIdentifier("seagment");
            mSegmentView.setDefaultSelection(0);
            mSegmentView.setOnSelectionChangedListener(new AndroidSegmentedControlView.OnSelectionChangedListener() {
                @Override
                public void newSelection(String identifier, String value) {
                    if(getStrings(R.string.payment_new_option).equals(value)){
                        fundSource=0;
                    }else if(getStrings(R.string.payment_new_option1).equals(value)){
                        fundSource=1;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackgrounds() {
//        http://183.129.133.147:10086/api/payment/paymentData
        final String url= MoreUserDal.GetServerUrl()+"/api/payment/paymentData";
        LogUtils.d("待客提款 的元数据 "+url);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                paymentDataEntity=JSON.parseObject(response.toString(), PaymentDataEntity.class);
                TypeList=paymentDataEntity.getPaymentTypes();
                methodList=paymentDataEntity.getPaymentMethods();
                currencyList=paymentDataEntity.getCurrencys();
                guaranteeList=paymentDataEntity.getGuaranteeTypes();

                subTypeList=TypeList.get(0).getSubTypes();
                methodEntity=methodList.get(0);
                guaranteeEntity=guaranteeList.get(0);
                subTypeEntity=subTypeList.get(0);

                mMethod.setText(methodEntity.getName());
                Type.setText(TypeList.get(0).getPaymentTypeName());
                SubType.setText(subTypeList.get(0).getPaymentSubTypeName() );
                mGuarantee.setText(guaranteeEntity.getName());

                hideView(mProgressbar);
                showView(mNestedView);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideView(mProgressbar);
                LogUtils.d("查询失败" +  VolleyErrorHelper.getMessage(error,mContent));
                tv_volley.setText(VolleyErrorHelper.getMessage(error,mContent));
                showView(tv_volley);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization","bearer   "+MoreUserDal.GetAccessToken());
                return map;
            }
        };
        jsonObjectRequest.setTag(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 3, 1.0f));
        mRequestQueue.add(jsonObjectRequest);

        super.onBackgrounds();
    }

    @Click(R.id.rl_pyNew_customer)
    void onCustomerDate(){
        PaymentNewCustomerActivity_.intent(mContent).extra(AppDelegate.PYNEW_CUSTOMERID,PaymentId).startForResult(PAYMENTECUSTOMER);
    }

    @Click(R.id.ll_pyNew_Method)
    void onMethodDate(){
        PaymentNewMethodActivity_.intent(mContent).extra(AppDelegate.PYNEW_METHODLIST, (Serializable) methodList)
                .extra(AppDelegate.PYNEW_METHOD, methodEntity).startForResult(PAYMENTMETHOD);
    }

    @Click(R.id.tv_pyNew_Type)
    void onPaymentTypeName(){
        List<String> listStr=new ArrayList<>();
        for(PaymentDataEntity.PaymentTypeList type: TypeList ){
            listStr.add( type.getPaymentTypeName());
        }
        final String[] stringItems = listStr.toArray(new String[listStr.size()]);
        final ActionSheetDialog dialog = new ActionSheetDialog(mContent, stringItems, null);
        dialog.title("付款类型").titleTextSize_SP(14f).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ToastUtil.showToast(mContent,stringItems[position]);
                Type.setText(stringItems[position]);
                subTypeList=TypeList.get(position).getSubTypes();
                subTypeEntity=subTypeList.get(0);
                SubType.setText(subTypeEntity.getPaymentSubTypeName());
                dialog.dismiss();
            }
        });
    }

    @Click(R.id.tv_pyNew_subType)
    void onPaymentTypeId(){
        PaymentNewSubTypeActivity_.intent(mContent).extra(AppDelegate.PYNEW_SUBTYPELIST, (Serializable) subTypeList)
                .extra(AppDelegate.PYNEW_SUBTYPE,subTypeEntity).startForResult(PAYMENTSUBTYPE);
    }

    @Click(R.id.ll_pyNew_guarantee)
    void onGuarantee(){
        PaymentNewGuaranteeActivity_.intent(mContent).extra(AppDelegate.PYNEW_GUARANTEETYPELIST, (Serializable) guaranteeList)
                .extra(AppDelegate.PYNEW_GUARANTEETYPE,guaranteeEntity ).startForResult(PATMENTGUARABTEE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK ){
            switch (requestCode){
                case PAYMENTECUSTOMER:
                    customerEntity= (CustomerListEntity) data.getSerializableExtra(AppDelegate.PYNEW_CUSTOMERENTITY);
                    PaymentId=customerEntity.getId();
                    PaymentName=customerEntity.getName();
                    AccountName.setText(PaymentName);
                    AccountStatus.setText(customerEntity.getStatusStr() );
                    showView(AccountStatus);
                    hud.show();
                    onPaymentAccount(PaymentId);
                    break;
                case PAYMENTMETHOD:
                    methodEntity= (PaymentDataEntity.PaymentMethodList) data.getSerializableExtra(AppDelegate.PYNEW_METHOD);
                    mMethod.setText( methodEntity.getName());
                    break;
                case PATMENTGUARABTEE:
                    guaranteeEntity= (PaymentDataEntity.GuaranteeTypeList) data.getSerializableExtra(AppDelegate.PYNEW_GUARANTEETYPE);
                    mGuarantee.setText( guaranteeEntity.getName());
                    break;
                case PAYMENTSUBTYPE:
                    subTypeEntity= (PaymentDataEntity.SubTypeList) data.getSerializableExtra(AppDelegate.PYNEW_SUBTYPE);
                    SubType.setText(subTypeEntity.getPaymentSubTypeName());
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Background
    void onPaymentAccount( int id ){
        final String url= MoreUserDal.GetServerUrl()+"/api/payment/PaymentAccountInfo?accountId="+id;
        LogUtils.e("选择客户后 获取的客户资金"+url);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                pyAccount=JSON.parseObject(response.toString(),PaymentAccountEntity.class);
                List<PaymentAccountEntity.PropertyItemList> itemList=pyAccount.getPropertyItems();
                accountName.setText( pyAccount.getAccountName());
                nameOne.setText( itemList.get(0).getPropertyName());
                valueOne.setText(itemList.get(0).getPropertyValue());
                nameTwo.setText( itemList.get(1).getPropertyName());
                valueTwo.setText(itemList.get(1).getPropertyValue());
                nameThree.setText( itemList.get(2).getPropertyName());
                valueThree.setText(itemList.get(2).getPropertyValue() );
                nameFour.setText( itemList.get(3).getPropertyName());
                valueFour.setText(itemList.get(3).getPropertyValue() );
                mBtnNext.setEnabled(true);
                AnimUtils.removeFragment(fm,PAGER_TWO);
                AnimUtils.removeFragment(fm,FINANCE_PAGER_TWO);
                hud.dismiss();
                showView(ll_top);
                Animation animation= AnimationUtils.loadAnimation(mContent,R.anim.push_top_in);
                ll_top.startAnimation(animation);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d("获取的客户资金" +  VolleyErrorHelper.getMessage(error,mContent));
                hud.dismiss();
                mBtnNext.setEnabled(false);
                ToastUtil.showToast(mContent,VolleyErrorHelper.getMessage(error,mContent));
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization","bearer   "+MoreUserDal.GetAccessToken());
                return map;
            }
        };
        jsonObjectRequest.setTag(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 3, 1.0f));
        mRequestQueue.add(jsonObjectRequest);
    }

    @Click(R.id.btn_pyNew_next)
    void onNextPager(){
        FragmentTransaction f=fm.beginTransaction();
        AnimUtils.TransactionFragmentShow(f);
        AnimUtils.setLoadAnimationLeft(mContent,mPageOne);
        if(fundSource==0){
            if(fm.findFragmentByTag(PAGER_TWO) !=null ){
                f.show(fm.findFragmentByTag(PAGER_TWO)).commit();
            }else{
                PaymentNewTwoFragment_ PaymentTwo=new PaymentNewTwoFragment_();
                PaymentTwo.addListener(BackTwoClick);
                f.replace(R.id.fragment ,PaymentTwo ,PAGER_TWO).commit();
            }
        }else if(fundSource==1){
            if(fm.findFragmentByTag(FINANCE_PAGER_TWO) !=null ){
                fm.beginTransaction().show(fm.findFragmentByTag(FINANCE_PAGER_TWO)).commit();
            }else{
                PaymentNewFinanceFragment_ financeFragment=new PaymentNewFinanceFragment_();
                financeFragment.addListener(FinanceCliCk);
               f.replace(R.id.fragment ,financeFragment ,FINANCE_PAGER_TWO).commit();
            }
        }

        hideView(mPageOne);
    }

    PaymentNewTwoFragment.BackClickListener BackTwoClick=new PaymentNewTwoFragment.BackClickListener() {
        @Override
        public void OnClickListener(int type) {
            FragmentTransaction f=fm.beginTransaction();
            AnimUtils.TransactionFragment(f);
            LogUtils.e("PaymentNewTwoFragment");
            f.hide(fm.findFragmentByTag(PAGER_TWO)).commit();
            showView(mPageOne);
            AnimUtils.setLoadAnimationRight(mContent,mPageOne);
        }
    };

    PaymentNewFinanceFragment.BackClickListener FinanceCliCk=new PaymentNewFinanceFragment.BackClickListener() {
        @Override
        public void OnClickListener(int pager) {
            FragmentTransaction f=fm.beginTransaction();
            AnimUtils.TransactionFragment(f);
            LogUtils.e("PaymentNewFinanceFragment");
            fm.beginTransaction().hide(fm.findFragmentByTag(FINANCE_PAGER_TWO)).commit();
            showView(mPageOne);
            AnimUtils.setLoadAnimationRight(mContent,mPageOne);
        }
    };

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cancel, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cancel:
                DeviceUtil.hideSoft((Activity)mContent);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        //fragment没有处理
        if (!BackHandlerHelper.handleBackPress(this)) {
            LogUtils.e(":没有处理:"+BackHandlerHelper.handleBackPress(this));
            super.onBackPressed();
        }else{
            LogUtils.e(":处理:"+BackHandlerHelper.handleBackPress(this));
        }
    }

    @Override
    protected void onDestroy() {
        mRequestQueue.cancelAll(mContent);
        super.onDestroy();
    }
}
