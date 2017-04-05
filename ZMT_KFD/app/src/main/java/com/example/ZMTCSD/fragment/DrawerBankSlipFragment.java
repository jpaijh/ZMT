package com.example.ZMTCSD.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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
 *   水单的侧边栏
 */

@EFragment(R.layout.fragment_drawer_bank_slip)
public class DrawerBankSlipFragment extends BaseFragment implements FragmentBackHandler {
    private Context mContext;
    private DrawerStatusAdapter mAdapter;
    private boolean IsClaimed;
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

    @ViewById(R.id.include)
    View  mInclude;

    private TimePickerView pvTime, pvTimed;
    private String keyword="";
    private String dateFrom="";
    private String dateTo="";
    private String amountFrom="";
    private String amountTo="";
    private String currency="";


    @Override
    public void onAfterViews() {
        this.mContext=getActivity();
        IsClaimed=getArguments().getBoolean(AppDelegate.BANKSLIP_LIST_TYPE);
        if(IsClaimed){hideView(mInclude);}

        mDateName.setText("收汇日期范围");
        mAmountName.setText("收汇金额范围");
        pvTime = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
        pvTimed = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
        mRecyclerState.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
        mRecyclerState.setHasFixedSize(true);
        mRecyclerState.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .colorResId(R.color.white).sizeResId(R.dimen.left_16).build());
        mAdapter = new DrawerStatusAdapter(mContext, getsStrings(R.array.BankSlip_status_name));
        mAdapter.setmOnItemClickListener(new DrawerStatusAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, String name) {
                ConfirmCurrency(name);
            }
        });
        mRecyclerState.setAdapter(mAdapter);

        super.onAfterViews();
    }


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

            Intent intent = new Intent(AppDelegate.BANK_SLIP_DRAWER_SCREEN);
//           AmountFrom=2&AmountTo=3&DateFrom=2016-10-28&DateTo=2016-10-28&currency=USD&keyword=%
            String  str="&keyword="+keyUTF+"&DateFrom="+dateFrom+"&DateTo="+dateTo+"&AmountFrom="
                    +amountFrom+"&AmountTo="+amountTo+"&currency="+currency;
            intent.putExtra(AppDelegate.BANK_SLIP_LIST_SCREEN,str);
            intent.putExtra(AppDelegate.BANKSLIP_LIST_TYPE,IsClaimed);
//            LogUtils.e(IsClaimed+"水单的搜索" + str);
            LocalBroadcastManager.getInstance(mContext).sendBroadcastSync(intent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void ConfirmCurrency(String name){
        switch(name){
            case "CNY":
                currency="CNY";
                break;
            case "USD":
                currency="USD";
                break;
            case "HKD":
                currency="HKD";
                break;
            case "JPY":
                currency="JPY";
                break;
            case "EUR":
                currency="EUR";
                break;
            default:
                currency="";
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
    }

    boolean backHandled;
    @Override
    public boolean onBackPressed() {
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
