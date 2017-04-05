package com.example.ZMTCSD.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.apkfuns.logutils.LogUtils;
import com.bigkoo.pickerview.TimePickerView;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.adapter.DrawerStatusAdapter;
import com.example.ZMTCSD.utils.DateUtil;
import com.example.ZMTCSD.utils.DeviceUtil;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

/**
 * 赠票的侧边栏
 */
@EFragment(R.layout.fragment_drawer_invoice)
public class DrawerInvoiceFragment extends BaseFragment {
    private Context mContext;
    private DrawerStatusAdapter mAdapter;
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
    AppCompatTextView mStateName;

    @ViewById(R.id.recycler_drawer_state)
    RecyclerView mRecyclerState;

    private TimePickerView pvTime, pvTimed;
    private String keyword = "";
    private String dateFrom = "";
    private String dateTo = "";
    private String mStatus = "";


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onAfterViews() {
        mDateName.setText("创建日期范围");
        pvTime = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
        pvTimed = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
        mRecyclerState.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
        mRecyclerState.setHasFixedSize(true);
        mRecyclerState.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .colorResId(R.color.white).sizeResId(R.dimen.left_16).build());
        mAdapter = new DrawerStatusAdapter(mContext, getsStrings(R.array.Invoice_status_name));
        mRecyclerState.setAdapter(mAdapter);
        mAdapter.setmOnItemClickListener(new DrawerStatusAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, String name) {
                InvoiceResult(name);
            }
        });
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

            Intent intent = new Intent(AppDelegate.INVOICE_DRAWER_SCREEN);
            String str = "&keyword=" + keyUTF + "&DateFrom=" + dateFrom + "&DateTo=" + dateTo + "&Status=" + mStatus;
            intent.putExtra(AppDelegate.INVOICE_LIST_SCREEN, str);
            LogUtils.e("赠票的搜索" + str);
//      LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            LocalBroadcastManager.getInstance(mContext).sendBroadcastSync(intent);
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
    }

    public void InvoiceResult(String name){
        switch (name){
            case "新制":
                mStatus="0";
            break;
            case "提交":
                mStatus="1";
                break;
            case "受理":
                mStatus="2";
                break;
            case "生效":
                mStatus="3";
                break;
            case "退回":
                mStatus="4";
                break;
            default:
                mStatus="";
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
