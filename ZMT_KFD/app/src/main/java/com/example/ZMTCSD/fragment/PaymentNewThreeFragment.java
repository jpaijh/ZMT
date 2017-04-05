package com.example.ZMTCSD.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
import com.example.ZMTCSD.NoDoubleClickListener;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.activity.PaymentNewActivity;
import com.example.ZMTCSD.activity.PaymentNewCurrencyActivity_;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.entity.CompanyBanksEntity;
import com.example.ZMTCSD.entity.CompanyListEntity;
import com.example.ZMTCSD.entity.PaymentAddApplyEntity;
import com.example.ZMTCSD.entity.PaymentDataEntity;
import com.example.ZMTCSD.entity.PaymentOrderEntity;
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
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

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
 *  待客提款 中第3页的功能
 */
@EFragment(R.layout.fragment_payment_new_three)
public class PaymentNewThreeFragment extends BaseFragment implements FragmentBackHandler {
    private Context mContent;
    private RequestQueue mRequestQueue;
    private  FragmentManager fm;
    private final static int PAYMENTCURRENT=7;//去选择 币别
    private final static String  PAGER_THREE="pagerThree";
    private final static String  PAGER_FOUR="pagerFour";
//    private final static String  PAGER_TWO="pagerTwo";
    private PaymentNewActivity ThisActivity;
    private List<PaymentDataEntity.CurrencyList> currencyList;
    private SpiltOrderAdapter mAdapter;
    private  String json; //这是你认领是发送的jsog格式
    private KProgressHUD hud;
    private PaymentDataEntity.CurrencyList CurrencyEntity;
    private CompanyListEntity companyEntity;
    private CompanyBanksEntity companyBankEntity;
    private double settleAmount=0;

    List<PaymentOrderEntity> orderList=new ArrayList<>();//拆分订单的类

    @ViewById(R.id.tv_pyNew_currency)
    AppCompatTextView mCurrency; //币别

    @ViewById(R.id.med_pyNew_RMBRate)
    AppCompatClearEditText mRmbRate; //汇率

    @ViewById(R.id.tv_pyNew_PaymentAmount)
    AppCompatTextView tv_Amount;

    @ViewById(R.id.med_pyNew_PaymentAmount)
    AppCompatClearEditText mPaymentAmount; //支付金额

    @ViewById(R.id.tv_pyNew_SettleAmount)
    AppCompatTextView tv_Settle;

    @ViewById(R.id.med_pyNew_SettleAmount)
    AppCompatClearEditText mSettleAmount; //结算金额

    @ViewById(R.id.tv_pyNew_hint)
    AppCompatTextView AmountHint;

    @ViewById(R.id.btn_pyNew_switch)
    SwitchButton switchBtn;

    @ViewById(R.id.btn_pyNew_next)
    AppCompatButton mBtnNext;

    @ViewById(R.id.ll_spiltOrder)
    LinearLayout SpiltOrder;

    @ViewById(R.id.recycler_pyNew)
    SwipeMenuRecyclerView mRecycler;

    @Override
    public void onAfterViews() {
        mContent=getActivity();
        mRequestQueue= Volley.newRequestQueue(mContent);
        ThisActivity= (PaymentNewActivity) mContent;
        fm=getActivity().getSupportFragmentManager();
        companyEntity= (CompanyListEntity) getArguments().getSerializable(AppDelegate.PYNEW_COMPANYENTITY);
        companyBankEntity= (CompanyBanksEntity) getArguments().getSerializable(AppDelegate.PYNEW_COMPANYBANKENTITY);
//        LogUtils.e(companyEntity.getId()+"：:"+companyBankEntity.getId());
        currencyList=ThisActivity.currencyList;
        CurrencyEntity=currencyList.get(0);
        mCurrency.setText(CurrencyEntity.getCurrency());
        mRmbRate.setText(StringUtil.numberForRate( currencyList.get(0).getRmbRate()));
        AmountHint.setText(getStrings(R.string.Payment_new_twenty_two)+" "+StringUtil.numberDecimal(ThisActivity.pyAccount.getAmount()));
        hud=KProgressHUD.create(mContent).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("正在提款，请等待.");
        initTextChanged();
        initRecycler();
        showSpilt();
        super.onAfterViews();
    }

    private void showSpilt(){
        showView(SpiltOrder);
        AnimUtils.SwitchBtnAnimShow(mContent,SpiltOrder);
        tv_Amount.setTextColor(getColors(R.color.black_two_mark_54));
        tv_Settle.setTextColor(getColors(R.color.black_two_mark_54));
        mPaymentAmount.setEnabled(false);
        mSettleAmount.setEnabled(false);
        mPaymentAmount.setTextColor(getColors(R.color.black_two_mark_54));
        mSettleAmount.setTextColor(getColors(R.color.black_two_mark_54));
        gainAmount();
    }
    private void hideSpilt(){
        AnimUtils.SwitchBtnAnimHide(mContent,SpiltOrder);
        mPaymentAmount.setEnabled(true);
        mSettleAmount.setEnabled(true);
        tv_Amount.setTextColor(getColors(R.color.black_one_mark_87));
        tv_Settle.setTextColor(getColors(R.color.black_one_mark_87));
        mPaymentAmount.setTextColor(getColors(R.color.black_one_mark_87));
        mSettleAmount.setTextColor(getColors(R.color.black_one_mark_87));
        mPaymentAmount.setText("");
        mSettleAmount.setText("");
        hideView(SpiltOrder);
    }

    private void initRecycler() {
        mRecycler.setLayoutManager(new LinearLayoutManager(mContent,LinearLayoutManager.VERTICAL,false));
        mRecycler.setHasFixedSize(true);
        mRecycler.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContent)
                .colorResId(R.color.line_color).marginResId(R.dimen.left_16, R.dimen.right_0) //记录左右的距离
                .showLastDivider().build());
//        mRecycler.setItemAnimator();
//        为SwipeRecyclerView的Item创建菜单就两句话，不错就是这么简单： 设置菜单创建器。
        mRecycler.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                int size = getResources().getDimensionPixelSize(R.dimen.lay_60);
                SwipeMenuItem deleteItem = new SwipeMenuItem(mContent).setImage(R.mipmap.ic_action_delete)
                        .setBackgroundDrawable(R.drawable.selector_swipemenu_red)
                        .setText("删除") // 文字，还可以设置文字颜色，大小等。。
                        .setWidth(size).setHeight(size).setTextColor(Color.WHITE);
                swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
            }
        });
        mAdapter=new SpiltOrderAdapter(orderList);
        mRecycler.setAdapter(mAdapter);
    }

    //监听editText
    private void initTextChanged() {
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
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(mRmbRate.length()!=0  && mPaymentAmount.length() != 0 && Double.valueOf(s.toString()) > 0.00
                && Double.valueOf(mPaymentAmount.getText().toString()) > 0.00 ){
                    settleAmount= Double.valueOf(mPaymentAmount.getText().toString())*
                            Double.valueOf(mRmbRate.getText().toString());
                    mSettleAmount.setText(StringUtil.numberFormat1(settleAmount));
//                    mBtnNext.setEnabled(true);
                }else{
                    mBtnNext.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                //以小数点为界分割字符串
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 4) {
                    edt.delete(posDot + 5, posDot + 6);
                }
            }
        });
        mRmbRate.setFilters(new InputFilter[]{new MyInputFilter()});
        mPaymentAmount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mPaymentAmount.requestFocus();
                return false;
            }
        });
        mPaymentAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(mRmbRate.length()!=0 && mPaymentAmount.length()!=0 && Double.valueOf(s.toString()) > 0.00
                        &&Double.valueOf(mRmbRate.getText().toString()) > 0.00 ){
                    settleAmount= Double.valueOf(mPaymentAmount.getText().toString())*
                            Double.valueOf(mRmbRate.getText().toString());
                    mSettleAmount.setText(StringUtil.numberFormat1(settleAmount));
//                    mBtnNext.setEnabled(true);
                }else{
                    mBtnNext.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    edt.delete(posDot + 3, posDot + 4);
                }
            }
        });
        mPaymentAmount.setFilters( new InputFilter[]{new MyInputFilter()} );
        mSettleAmount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mSettleAmount.requestFocus();
                return false;
            }
        });

        mSettleAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(mRmbRate.length()!=0 && mPaymentAmount.length()!=0 && mSettleAmount.length()!=0
                &&Double.valueOf(mRmbRate.getText().toString())> 0.00&&Double.valueOf(s.toString())> 0.00
                &&Double.valueOf(mPaymentAmount.getText().toString())> 0.00
                && Double.valueOf(s.toString())<=ThisActivity.pyAccount.getAmount()){
                    mBtnNext.setEnabled(true);
                }else{
                    mBtnNext.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    edt.delete(posDot + 3, posDot + 4);
                }

            }
        });
        mSettleAmount.setFilters( new InputFilter[]{new MyInputFilter()} );
        switchBtn.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked){
                    showSpilt();
                }else
                    hideSpilt();
            }
        });
    }

    @Click(R.id.ll_pyNew_currency)
    void onCurrency( ){
        Intent intent=new Intent(mContent,PaymentNewCurrencyActivity_.class);
        intent.putExtra(AppDelegate.PYNEW_CURRENCYLIST, (Serializable) currencyList);
        intent.putExtra(AppDelegate.PYNEW_CURRENCY,CurrencyEntity);
        startActivityForResult( intent,PAYMENTCURRENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(PAYMENTCURRENT ==requestCode && resultCode==RESULT_OK){
            CurrencyEntity= (PaymentDataEntity.CurrencyList) data.getSerializableExtra(AppDelegate.PYNEW_CURRENCY);
            mCurrency.setText(CurrencyEntity.getCurrency() );
            mRmbRate.setText(StringUtil.numberForRate(CurrencyEntity.getRmbRate()));
            mRmbRate.requestFocus();
        }
    }

    @Click(R.id.btn_pyNew_next)
    void onNextPager(){
//        private double PaymentAmount;  //提款是输入的支付金额
//        private double RMBRate;  //汇率
//        private double SettleAmount;  //结算金额
//        private String currency; //币别
//        private String companyName; //收款单位的名称
//        private int companyId; //收款单位的id
//        private String bankName; //提款银行的 名称
//        private String bankAccount;  //提款银行的账号
//        private int accountId;  //选择客户时的id
//        private String accountCode;//选择客户时的code
//        private String accountName;//选择客户时的名称
//        private String PaymentMethodId; //付款方式的id
//        private String PaymentMethodName; //付款方式的名称
//        private String PaymentTypeName; //付款的类型子类型 的名称
//        private String PaymentTypeId; //付款的类型中的子类型的id
//        private String GuaranteeTypeId; //担保方式的id
//        private String GuaranteeTypeName;//担保方式的 名称
        double payment=Double.valueOf(mPaymentAmount.getText().toString());
        double settle=Double.valueOf(mSettleAmount.getText().toString());
        LogUtils.e(payment+"金额不能为"+settle);
//        if(payment==0 || settle==0 && settle<=ThisActivity.pyAccount.getAmount()){
//            ToastUtil.showToast(mContent,"金额不能为0或者结算金额不能大于总金额。");
//        }else{
            PaymentAddApplyEntity entity=new PaymentAddApplyEntity();
            entity.setCompanyId(companyEntity.getId());
            entity.setCompanyName(companyEntity.getName());
            entity.setBankName(companyBankEntity.getBankName());
            entity.setBankAccount(companyBankEntity.getBankAccount());
            entity.setRMBRate(mRmbRate.getText().toString());
            entity.setCurrency(mCurrency.getText().toString());
            entity.setPaymentAmount( mPaymentAmount.getText().toString());
            entity.setSettleAmount(mSettleAmount.getText().toString());
            entity.setAccountId(ThisActivity.pyAccount.getAccountId());
            entity.setAccountCode(ThisActivity.pyAccount.getAccountCode());
            entity.setAccountName(ThisActivity.pyAccount.getAccountName() );
            entity.setPaymentMethodId( ThisActivity.methodEntity.getValue());
            entity.setPaymentMethodName( ThisActivity.methodEntity.getName());
            entity.setGuaranteeTypeId(ThisActivity.guaranteeEntity.getValue());
            entity.setGuaranteeTypeName(ThisActivity.guaranteeEntity.getName());
            entity.setPaymentTypeId(ThisActivity.subTypeEntity.getPaymentSubTypeId());
            entity.setPaymentTypeName(ThisActivity.subTypeEntity.getPaymentSubTypeName());
            entity.setFundSource(ThisActivity.fundSource);
            if(switchBtn.isChecked()){
                entity.setPaymentOrders(mAdapter.getOrderList());
            }
            json= JSONObject.toJSONString(entity);
            LogUtils.e("待客提款"+json.toString());
            hud.show();
            onBackgrounds();
//        }
    }

    @Override
    public void onBackgrounds() {
        final String url= MoreUserDal.GetServerUrl()+"/api/payment/addPaymentApply";
        LogUtils.d( " 待客提款"+url);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, url, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                LogUtils.d(""+response.toString());
                hud.dismiss();
                ToastUtil.showToast(mContent,"创建成功");
                LocalBroadcastManager.getInstance(mContent).sendBroadcastSync( new Intent(AppDelegate.PAYMENT_NEW_FRAGMENT));
                ThisActivity.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d("待客提款"+ VolleyErrorHelper.getMessage(error,mContent));
                hud.dismiss();
                ToastUtil.showToast(mContent,VolleyErrorHelper.getMessage(error,mContent));
            }
        }){
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

    @Click(R.id.ll_pyNew_addOrder)
    void onAddOrder(){
        mAdapter.addItem();
    }

    public class SpiltOrderAdapter extends SwipeMenuAdapter<SpiltOrderAdapter.ViewHolder>{
        public List<PaymentOrderEntity> list;
        public SpiltOrderAdapter(List<PaymentOrderEntity> list) {
            this.list = list;
        }
        public void addItem(){
            list.add( new PaymentOrderEntity());
            notifyDataSetChanged();
        }

        public ArrayList<String> getCodeList(){
            ArrayList<String> CodeList=new ArrayList<>();
            for(PaymentOrderEntity p: list ){
                CodeList.add(p.getOrderCode());
            }
            return CodeList;
        }

        public double getPaymentAmounts(){
            double payment=0;
            for(PaymentOrderEntity p: list ){
                payment+=p.getPaymentAmount();
            }
            return payment;
        }
        public double getSettleAmounts(){
            double settle=0;
            for(PaymentOrderEntity p: list ){
                settle+=p.getSettleAmount();
            }
            return settle;
        }

        public List<PaymentAddApplyEntity.PaymentOrderList> getOrderList(){
            List<PaymentAddApplyEntity.PaymentOrderList> addOrderList=new ArrayList<>();
            for(PaymentOrderEntity p: list){
                if(p.getId()!=0){
                    addOrderList.add(new PaymentAddApplyEntity.PaymentOrderList
                            (p.getId(),p.getOrderCode(),p.getPaymentAmount(),p.getSettleAmount()));
                }
            }
            return addOrderList;
        }

        @Override
        public View onCreateContentView(ViewGroup viewGroup, int viewType) {
            return LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_payment_spilt_order, viewGroup, false);
        }

        @Override
        public ViewHolder onCompatCreateViewHolder(View view, int viewType) {
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            PaymentOrderEntity entity=list.get(position);
            if(entity.getId()!=0){
                holder.code.setText(entity.getOrderCode());
                holder.settleAmount.setText(getStrings(R.string.Payment_newFinance_five)+StringUtil.numberFormat1(entity.getPaymentAmount())+" "
                    +getStrings(R.string.Payment_newFinance_six)+StringUtil.numberFormat1(entity.getSettleAmount()));
                showView(holder.settleAmount);
            }
//            设置菜单Item点击监听。
            mRecycler.setSwipeMenuItemClickListener(new OnSwipeMenuItemClickListener() {
                @Override
                public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
                    closeable.smoothCloseMenu();// 关闭被点击的菜单。
                    list.remove(adapterPosition);
                    LogUtils.d(list.size());
                    notifyDataSetChanged();
                    gainAmount();
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public AppCompatImageView image;
            public AppCompatTextView code;
            public AppCompatTextView settleAmount;
            public RelativeLayout item_content;

            public ViewHolder(View itemView) {
                super(itemView);
                image= (AppCompatImageView) itemView.findViewById(R.id.img_spiltOrder_remove);
                code= (AppCompatTextView) itemView.findViewById(R.id.tv_spiltOrder_code);
                settleAmount= (AppCompatTextView) itemView.findViewById(R.id.tv_spiltOrder_settleAmount);
                item_content= (RelativeLayout) itemView.findViewById(R.id.rl_spiltOrder);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mRecycler.openRightMenu(getLayoutPosition());
                    }
                });
                item_content.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View view) {
                        PaymentOrderEntity entity=list.get(getLayoutPosition());
                        PaymentNewFourFragment_ PaymentFour=new PaymentNewFourFragment_();
                        PaymentFour.addListener(back);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(AppDelegate.PYNEW_FOUR_ORDERENTITY,entity);
                        bundle.putStringArrayList(AppDelegate.PYNEW_FOUR_ORDERCODE,getCodeList());
                        PaymentFour.setArguments(bundle);
                        FragmentTransaction f=fm.beginTransaction();
                        AnimUtils.TransactionFragmentShow(f);
                        f.add(R.id.fragment,PaymentFour,PAGER_FOUR).hide(fm.findFragmentByTag(PAGER_THREE)).commit();
                    }
                });
            }
            PaymentNewFourFragment.BackClickListener back=new PaymentNewFourFragment.BackClickListener() {
                @Override
                public void OnClickListener(int pager, PaymentOrderEntity entity) {
                    if(pager==5){
                        list.set(getLayoutPosition(),entity);
                        notifyItemChanged(getLayoutPosition());
                        gainAmount();
                    }
                    FragmentTransaction f=fm.beginTransaction();
//                    AnimUtils.TransactionFragmentHide(f);
                    f.show(fm.findFragmentByTag(PAGER_THREE)).remove(fm.findFragmentByTag(PAGER_FOUR)).commit();
                }
            };
        }
    }

    //获取你选择订单后要显示的总数据
    private void gainAmount(){
//        if(mAdapter.getPaymentAmounts() !=0 && mAdapter.getSettleAmounts()!=0){
            mPaymentAmount.setText(StringUtil.numberFormat1(mAdapter.getPaymentAmounts()));
            mSettleAmount.setText(StringUtil.numberFormat1(mAdapter.getSettleAmounts()));
//        }
    }

    @Override
    public void onDestroy() {
        mRequestQueue.cancelAll(mContent);
        super.onDestroy();
    }
    boolean backHandled=true;
    @Override
    public boolean onBackPressed() {
        if (backHandled) {
            if( mListener != null){
                mListener.OnListener(3);
            }
            //外理返回键
            return true;
        } else {
            return BackHandlerHelper.handleBackPress(this);
        }
    }

    private BackClickListen mListener;
    public void addListener(BackClickListen mListener) {
        this.mListener = mListener;
    }
    // 接口
    public interface BackClickListen {
        void OnListener( int pager);
    }



}
