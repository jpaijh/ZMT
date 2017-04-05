package com.example.ZMTCSD.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

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
 *   中信保限额的侧边栏
 */
@EFragment(R.layout.fragment_drawer_limit)
public class DrawerCLCLimitFragment extends  BaseFragment implements FragmentBackHandler {
    private Context mContext;
    private DrawerStatusAdapter LcTypeAdapter;
    private DrawerStatusAdapter CGStatusAdapter;

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

    @ViewById(R.id.recycler_drawer_CGStatus)
    RecyclerView mRecyclerCGStatus;

    private TimePickerView pvTime, pvTimed;
    private String keyword="";
    private String dateFrom="";
    private String dateTo="";
    private String isLcType="";
    private String CGStatus="";


    @Override
    public void onAfterViews() {
        this.mContext=getActivity();
        mDateName.setText("申请日期");
        stateName.setText("类别");
        pvTime = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
        pvTimed = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
        mRecyclerState.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
        mRecyclerState.setHasFixedSize(true);
        mRecyclerState.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .colorResId(R.color.white).sizeResId(R.dimen.left_16).build());
        LcTypeAdapter = new DrawerStatusAdapter(mContext, getsStrings(R.array.LCLimit_status_LcType));
        LcTypeAdapter.setmOnItemClickListener(new DrawerStatusAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, String name) {
                ConfirmLcTypeStatus(name);
            }
        });
        mRecyclerState.setAdapter(LcTypeAdapter);


        mRecyclerCGStatus.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
        mRecyclerCGStatus.setHasFixedSize(true);
        mRecyclerCGStatus.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .colorResId(R.color.white).sizeResId(R.dimen.left_16).build());
        CGStatusAdapter = new DrawerStatusAdapter(mContext, getsStrings(R.array.Insure_status_LCStatus));
        CGStatusAdapter.setmOnItemClickListener(new DrawerStatusAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, String name) {
                ConfirmCGStatus(name);
            }
        });
        mRecyclerCGStatus.setAdapter(CGStatusAdapter);

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
            dateFrom= String.valueOf(mDateFrom.getText());
            dateTo= String.valueOf(mDateTo.getText());
//            &Keyword=&isLcType=true,false&applyStatus=-1,0,1&CGStatus=1,0&replyStatus=1,0,-1
            Intent intent = new Intent(AppDelegate.COMPANY_CLLIMIT_DRAWER_SCREEN);
            String str = "&keyword=" + keyUTF +"&DateFrom="+dateFrom+"&DateTo="+dateTo+
                     "&CGStatus=" + CGStatus ;
            if(!TextUtils.isEmpty(isLcType)){
                str= str+"&isLcType="+isLcType;
            }
            intent.putExtra(AppDelegate.COMPANY_CLLIMIT_LIST_SCREEN, str);
            LogUtils.e("限额搜索" + str);
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
        LcTypeAdapter.ResetPosition();
        CGStatusAdapter.ResetPosition();
    }


    public void  ConfirmLcTypeStatus(String  name){
        switch (name) {
            case "LC":
                isLcType ="true";
                break;
            case "非LC":
                isLcType = "false";
                break;
            default:
                isLcType = "";
                break;
        }
    }

    //未上传 0，未批复 1，批复通过 2，批复不通过 -1.
    public void  ConfirmCGStatus(String  name){
        switch (name) {
            case "未上传":
                CGStatus ="0";
                break;
            case "未批复":
                CGStatus = "1";
                break;
            case "批复通过":
                CGStatus = "2";
                break;
            case "批复不通过":
                CGStatus = "-1";
                break;
            default:
                CGStatus = "";
                break;
        }
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
