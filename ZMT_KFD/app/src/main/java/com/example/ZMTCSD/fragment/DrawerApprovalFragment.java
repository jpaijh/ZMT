package com.example.ZMTCSD.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

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
 *  我审批的侧边栏
 */
@EFragment(R.layout.fragment_drawer_approval)
public class DrawerApprovalFragment extends BaseFragment implements FragmentBackHandler {
    private Context mContext;
    private DrawerStatusAdapter mAdapter;
    private String IsClaimed; //判断是我审批 和我发起中的 那个分页来的搜索
    //侧边栏
    @ViewById(R.id.ed_drawer_keyword)
    MaterialEditText mKeyword;

    @ViewById(R.id.tv_drawer_dateName)
    AppCompatTextView mDateName;

    @ViewById(R.id.tv_drawer_dateFrom)
    AppCompatTextView mDateFrom;

    @ViewById(R.id.tv_drawer_dateTo)
    AppCompatTextView mDateTo;

    @ViewById(R.id.tv_drawer_stateName)
    AppCompatTextView stateName;

    @ViewById(R.id.recycler_drawer_state)
    RecyclerView mRecyclerState;

    private TimePickerView pvTime, pvTimed;
    private String keyword = "";
    private String dateFrom = "";
    private String dateTo = "";
    private int reportType=-1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onAfterViews() {
        this.mContext=getActivity();
        IsClaimed=getArguments().getString(AppDelegate.DRAWER_APPROVAL_TYPE);
        mDateName.setText("报审时间范围");
        stateName.setText("审批类型");
        pvTime = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
        pvTimed = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
        mRecyclerState.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
        mRecyclerState.setHasFixedSize(true);
        mRecyclerState.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .colorResId(R.color.white).sizeResId(R.dimen.left_16).build());
        mAdapter = new DrawerStatusAdapter(mContext, getsStrings(R.array.Approval_status_name));
        mAdapter.setmOnItemClickListener(new DrawerStatusAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, String name) {
                SelecterReportType(name);
            }
        });
        mRecyclerState.setAdapter(mAdapter);

        super.onAfterViews();
    }

    @Click(R.id.tv_drawer_confirm)
    void ConFirm(){
        try {
            keyword= String.valueOf(mKeyword.getText());
            String keyUTF = URLEncoder.encode(keyword,"UTF-8");
            dateFrom= String.valueOf(mDateFrom.getText());
            dateTo= String.valueOf(mDateTo.getText());
            Intent intent = new Intent(AppDelegate.APPROCVAL_DRAWER_SCREEN);
            String  str="&keyword="+keyUTF+"&DateFrom="+dateFrom+"&DateTo="+dateTo;
            if(reportType !=-1){
                str = str+"&reportType="+reportType;
            }
            intent.putExtra(AppDelegate.APPROCVAL_LIST_SCREEN,str);
            intent.putExtra(AppDelegate.DRAWER_APPROVAL_TYPE,IsClaimed);
//            LogUtils.e(IsClaimed+"审批的搜索" + str);
            LocalBroadcastManager.getInstance(mContext).sendBroadcastSync(intent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.tv_drawer_dateFrom)
    void DateFrom() {
        DeviceUtil.hideSoft(mContext, mDateFrom);
//        pvTime=new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
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
//        pvTimed=new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
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


    @Click(R.id.tv_drawer_reset)
    void Reset(){
        mKeyword.setText("");
        mDateFrom.setText("");
        mDateTo.setText("");
        mAdapter.ResetPosition();
    }

    public void SelecterReportType(String  position){
//        <!--reportType 审批类型 协议审批  26600 付款审批 3178700 客户审批 0-->
        switch (position){
            case "协议审批":
                reportType=26600;
                break;
            case "付款审批":
                reportType=3178700;
                break;
            case "客户审批":
                reportType=0;
                break;
            default:
                reportType=-1;
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
