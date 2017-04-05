package com.example.ZMTCSD.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.bigkoo.pickerview.TimePickerView;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.adapter.DrawerStatusAdapter;
import com.example.ZMTCSD.utils.DateUtil;
import com.example.ZMTCSD.utils.DeviceUtil;
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
 *  客户列表中的 侧边栏
 */
@EFragment(R.layout.fragment_drawer_customer)
public class DrawerCustomerFragment extends BaseFragment  implements FragmentBackHandler {
    private Context mContext;
    private DrawerStatusAdapter mAdapter;
    private DrawerStatusAdapter mItemAdapter;
    private boolean IsBankSelect=false;
    //侧边栏
    @ViewById(R.id.ed_drawer_keyword)
    MaterialEditText mKeyword;

    @ViewById(R.id.tv_drawer_dateName)
    AppCompatTextView mDateName;

    @ViewById(R.id.tv_drawer_dateFrom)
    AppCompatTextView mDateFrom;

    @ViewById(R.id.tv_drawer_dateTo)
    AppCompatTextView mDateTo;

    @ViewById(R.id.recycler_drawer_itemId)
    RecyclerView mRecyclerItem;

    @ViewById(R.id.recycler_drawer_state)
    RecyclerView mRecyclerState;

    @ViewById(R.id.include)
    View mInclude;

    private TimePickerView pvTime, pvTimed;
    private String keyword = "";
    private String dateFrom = "";
    private String dateTo = "";
    private String Status = "";
    private String serviceItemId = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onAfterViews() {
        mDateName.setText("创建日期范围");
        IsBankSelect=getArguments().getBoolean(AppDelegate.BANKSLIP_LIST_TYPE);
        if(IsBankSelect){hideView(mInclude);}

        pvTime = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
        pvTimed = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
        mRecyclerState.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
        mRecyclerState.setHasFixedSize(true);
        mRecyclerState.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .colorResId(R.color.white).sizeResId(R.dimen.left_16).build());
        mAdapter = new DrawerStatusAdapter(mContext, getsStrings(R.array.Customer_status_name));
        mAdapter.setmOnItemClickListener(new DrawerStatusAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, String name) {
                ConfirmResult(name);
            }
        });
        mRecyclerState.setAdapter(mAdapter);

        mRecyclerItem.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
        mRecyclerItem.setHasFixedSize(true);
        mRecyclerItem.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .colorResId(R.color.white).sizeResId(R.dimen.left_16).build());
        mItemAdapter = new DrawerStatusAdapter(mContext, getsStrings(R.array.Customer_type_name));
        mItemAdapter.setmOnItemClickListener(new DrawerStatusAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, String name) {
                ConFirmServerItem(name);
            }
        });
        mRecyclerItem.setAdapter(mItemAdapter);
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
    void ConFirm() {
        try {
            keyword = String.valueOf(mKeyword.getText());
            String keyUTF = URLEncoder.encode(keyword, "UTF-8");
            dateFrom = String.valueOf(mDateFrom.getText());
            dateTo = String.valueOf(mDateTo.getText());
            Intent intent = new Intent(AppDelegate.CUSTOMER_DRAWER_SCREEN);
            //DateFrom=2016-10-26&DateTo=2016-09-26&keyword=&pageIndex=1&pageSize=20&serviceItemId=1004&status=1
            String str = "&keyword=" + keyUTF + "&DateFrom=" + dateFrom + "&DateTo=" + dateTo
                    + "&status=" + Status + "&accountType=" + serviceItemId;
            intent.putExtra(AppDelegate.CUSTOMER_LIST_SCREEN, str);
            LogUtils.e("客户的搜索" + str);
            LocalBroadcastManager.getInstance(mContext).sendBroadcastSync(intent);
            //sendBroadcastSync 与sendBroadCast. 一个同步和异步广播
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.tv_drawer_reset)
    void Reset() {
        mKeyword.setText("");
        mDateFrom.setText("");
        mDateTo.setText("");
        mAdapter.ResetPosition();
        mItemAdapter.ResetPosition();
    }

    //确认状态的值
    public void ConfirmResult(String name) {
//        status //未审核 0， 审核中1，生效 2，驳回 -1，注销 3
        switch (name) {
            case "未审核":
                Status = "0";
                break;
            case "审核中":
                Status = "1";
                break;
            case "生效":
                Status = "2";
                break;
            case "注销":
                Status = "3";
                break;
            case "驳回":
                Status = "-1";
                break;
            default:
                Status="";
                break;
        }
    }
    //确认类型的值
    public void ConFirmServerItem(String name){
//        国内工厂 0，个人商户3，中间商 2，国外买手 1
//        type 所有企业类型 -10，国内工厂 0，国外买手 1，中间商 2，个人商户 3，
        switch (name) {
            case "国内工厂":
                serviceItemId = "0";
                break;
            case "国外买手":
                serviceItemId = "1";
                break;
            case "中间商":
                serviceItemId = "2";
                break;
            case "个人商户":
                serviceItemId = "3";
                break;
            default:
                serviceItemId="";
                break;
        }
    }

    private boolean backHandled;
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
