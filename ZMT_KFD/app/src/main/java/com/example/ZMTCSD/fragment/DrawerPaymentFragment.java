package com.example.ZMTCSD.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.apkfuns.logutils.LogUtils;
import com.bigkoo.pickerview.TimePickerView;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.utils.DateUtil;
import com.example.ZMTCSD.utils.DeviceUtil;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.adapter.DrawerStatusAdapter;
import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.github.ikidou.fragmentBackHandler.FragmentBackHandler;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

/**
 *  付款的侧边栏
 */
@EFragment(R.layout.fragment_drawer_payment)
public class DrawerPaymentFragment extends BaseFragment implements FragmentBackHandler {
    private Context mContext;
    private DrawerStatusAdapter mAdapter;
    private DrawerStatusAdapter mAdapterSource;
    private DrawerStatusAdapter mAdapterType;
    //侧边栏
    @ViewById(R.id.ed_drawer_keyword)
    MaterialEditText mKeyword;

    @ViewById(R.id.tv_drawer_dateName)
    AppCompatTextView mDateName;

    @ViewById(R.id.tv_drawer_dateFrom)
    AppCompatTextView mDateFrom;

    @ViewById(R.id.tv_drawer_dateTo)
    AppCompatTextView mDateTo;

    @ViewById(R.id.tv_drawer_amountName)
    AppCompatTextView mAmountName;

    @ViewById(R.id.ed_drawer_amountFrom)
    AppCompatEditText mAmountFrom;

    @ViewById(R.id.ed_drawer_amountTo)
    AppCompatEditText mAmountTo;

    @ViewById(R.id.recycler_drawer_state)
    RecyclerView mRecyclerState;

    @ViewById(R.id.recycler_drawer_source)
    RecyclerView mRecyclerSource;

    @ViewById(R.id.recycler_drawer_type)
    RecyclerView mRecyclerType;


    private String keyword="";
    private String dateFrom="";
    private String dateTo="";
    private String amountFrom="";
    private String amountTo="";
    private String status="";
    private String paymentType="";
    private String Source="";


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onAfterViews() {
        mDateName.setText("申请日期范围");
        mAmountName.setText("支付金额范围");
        pvTime = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
        pvTimed = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
        mRecyclerState.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
        mRecyclerState.setHasFixedSize(true);
        mRecyclerState.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .colorResId(R.color.white).sizeResId(R.dimen.left_16).build());
        mAdapter = new DrawerStatusAdapter(mContext, getsStrings(R.array.Payment_status_name));
        mAdapter.setmOnItemClickListener(new DrawerStatusAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, String name) {
                ConfirmStatus(name);
            }
        });
        mRecyclerState.setAdapter(mAdapter);

        mRecyclerSource.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
        mRecyclerSource.setHasFixedSize(true);
        mRecyclerSource.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .colorResId(R.color.white).sizeResId(R.dimen.left_16).build());
        mAdapterSource = new DrawerStatusAdapter(mContext, getsStrings(R.array.Payment_source_name));
        mAdapterSource.setmOnItemClickListener(new DrawerStatusAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, String name) {
                ConfirmSource(name);
            }
        });
        mRecyclerSource.setAdapter(mAdapterSource);

        mRecyclerType.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
        mRecyclerType.setHasFixedSize(true);
        mRecyclerType.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .colorResId(R.color.white).sizeResId(R.dimen.left_16).build());
        mAdapterType = new DrawerStatusAdapter(mContext, getsStrings(R.array.Payment_type_name));
        mAdapterType.setmOnItemClickListener(new DrawerStatusAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, String name) {
                ConfirmPaymentType(name);
            }
        });
        mRecyclerType.setAdapter(mAdapterType);

        super.onAfterViews();
    }

    private TimePickerView pvTime, pvTimed;
    @Click(R.id.tv_drawer_dateFrom)
    void DateFrom() {
        DeviceUtil.hideSoft(mContext, mDateFrom);
        pvTime.setTime(new Date());
        pvTime.setCyclic(true); //设置是否循环滚动
        pvTime.setCancelable(true); //点击外部可以关闭
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                mDateFrom.setText(DateUtil.dateToStr(date));
            }
        });
        //弹出时间选择器
        pvTime.show();
    }

    @Click(R.id.tv_drawer_dateTo)
    void DateTo() {
        DeviceUtil.hideSoft(mContext, mDateTo);
        pvTimed.setTime(new Date());
        pvTimed.setCyclic(true); //设置是否循环滚动
        pvTimed.setCancelable(true);
        //时间选择后回调
        pvTimed.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                mDateTo.setText(DateUtil.dateToStr(date));
            }
        });
        //弹出时间选择器
        pvTimed.show();

    }

    @Click(R.id.tv_drawer_confirm)
    void ConFirm(){
        try {
            keyword= String.valueOf(mKeyword.getText());
            String keyUTF = URLEncoder.encode(keyword,"UTF-8");
            dateFrom= String.valueOf(mDateFrom.getText());
            dateTo= String.valueOf(mDateTo.getText());
            amountFrom= String.valueOf(mAmountFrom.getText());
            amountTo= String.valueOf(mAmountTo.getText());
            Intent intent = new Intent(AppDelegate.PAYMENT_DRAWER_SCREEN);
//AmountFrom=2&AmountTo=6&DateFrom=2016-10-27&DateTo=2016-10-27&PaymentType=2&Source=0&Status=1&keyword=f
            String  str="&keyword="+keyUTF+"&DateFrom="+dateFrom+"&DateTo="+dateTo+"&AmountFrom="
                    +amountFrom+"&AmountTo="+amountTo+"&Status="+status+"&PaymentType="+paymentType+"&Source="+Source;
            intent.putExtra(AppDelegate.PAYMENT_LIST_SCREEN,str);
            LogUtils.e("付款的搜索" + str);
            LocalBroadcastManager.getInstance(mContext).sendBroadcastSync(intent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public void ConfirmPaymentType(String  Type){
        switch (Type){
            case "货款":
                paymentType="0";
                break;
            case "费用":
                paymentType="2";
                break;
            case "预付款":
                paymentType="1";
                break;
            default:
                paymentType="";
                break;
        }
    }

    //来源
    public void ConfirmSource(String name){
        //客户 source=1  后台0
        switch (name){
            case "客户自助申请":
                Source="1";
                break;
            case "后台代客提款":
                Source="0";
                break;
            default:
                Source="";
                break;
        }
    }
    //状态
    public void ConfirmStatus(String name){
// value="-1">未申请审批   value="1">审批中  value="2">审批同意  value="-2">审批不同意  value="3">已支付
// value="4">已入账  value="0">新制 value="-11">已退回  value="-12">结算撤销  alue="-10">待受理
//        批准待支付-5 待支付审批6
        switch (name){
            case "未申请审批":
                status="-1";
                break;
            case "审批中":
                status="1";
                break;
            case "审批同意":
                status="2";
                break;
            case "审批不同意":
                status="-2";
                break;
            case "已支付":
                status="3";
                break;
            case "已入账":
                status="4";
                break;
            case "新制":
                status="0";
                break;
            case "已退回":
                status="-11";
                break;
            case "结算撤销":
                status="-12";
                break;
            case "待受理":
                status="-10";
                break;
            case "批准待支付":
                status="-5";
                break;
            case "待支付审批":
                status="6";
                break;
            default:
                status="";
                break;
        }
    }

    @Click(R.id.tv_drawer_reset)
    void Reset(){
        mKeyword.setText("");
        mDateFrom.setText("");
        mDateTo.setText("");
        mAmountFrom.setText("");
        mAmountTo.setText("");
        mAdapter.ResetPosition();
        mAdapterSource.ResetPosition();
        mAdapterType.ResetPosition();
    }

    boolean backHandled;
    @Override
    public boolean onBackPressed() {
// 当确认没有子Fragmnt时可以直接return false
        if(pvTimed.isShowing() || pvTime.isShowing()){
            backHandled=true;
        }else{
            backHandled=false;
        }
        if (backHandled) {
            pvTime.dismissImmediately();
            pvTimed.dismissImmediately();
            return true;
        } else {
            return BackHandlerHelper.handleBackPress(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
