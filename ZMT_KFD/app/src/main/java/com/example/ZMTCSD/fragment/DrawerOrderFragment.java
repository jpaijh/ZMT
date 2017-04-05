package com.example.ZMTCSD.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.apkfuns.logutils.LogUtils;
import com.bigkoo.pickerview.TimePickerView;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.adapter.ServerItemAdapter;
import com.example.ZMTCSD.dal.MetaDataDal;
import com.example.ZMTCSD.utils.DateUtil;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.adapter.DrawerStatusAdapter;
import com.example.ZMTCSD.entity.MetaDataEntity;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 订单列表的侧边栏
 */
@EFragment(R.layout.fragment_drawer_order)
public class DrawerOrderFragment extends BaseFragment implements FragmentBackHandler {
    private Context mContext;
    private DrawerStatusAdapter mAdapter;
    private ServerItemAdapter mItemAdapter;
    private DrawerStatusAdapter mMatchAdapter;
    private List<MetaDataEntity.Value> valueEntity = new ArrayList<>();
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

    @ViewById(R.id.recycler_drawer_match)
    RecyclerView mRecyclerMatch;

//

    private TimePickerView pvTime, pvTimed;
    private String keyword = "";
    private String dateFrom = "";
    private String dateTo = "";
    private String Status = "";
    private String serviceItemId = "";
    private String InvoiceMatch = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onAfterViews() {
        mDateName.setText("下单日期范围");
        pvTime = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
        pvTimed = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
        mRecyclerState.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
        mRecyclerState.setHasFixedSize(true);
        mRecyclerState.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .colorResId(R.color.white).sizeResId(R.dimen.left_16).build());
        mAdapter = new DrawerStatusAdapter(mContext, getsStrings(R.array.Order_status_name));
        mAdapter.setmOnItemClickListener(new DrawerStatusAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, String name) {
                ConfirmResult(name);
            }
        });
        mRecyclerState.setAdapter(mAdapter);

        mRecyclerMatch.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
        mRecyclerMatch.setHasFixedSize(true);
        mRecyclerMatch.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .colorResId(R.color.white).sizeResId(R.dimen.left_16).build());
        mMatchAdapter = new DrawerStatusAdapter(mContext, getsStrings(R.array.Order_itemId_match));
        mMatchAdapter.setmOnItemClickListener(new DrawerStatusAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, String name) {
                ConFirmIsInvoiceMatch(name);
            }
        });
        mRecyclerMatch.setAdapter(mMatchAdapter);


        mRecyclerItem.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
        mRecyclerItem.setHasFixedSize(true);
        mRecyclerItem.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .colorResId(R.color.white).sizeResId(R.dimen.left_16).build());
        valueEntity = MetaDataDal.getServerItemList();//获取元数据中的服务项

        mItemAdapter = new ServerItemAdapter(mContext, valueEntity);
        mItemAdapter.addmOnItemClickListener(new ServerItemAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, String name) {
                ConFirmServiceItemId(name);
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
            String ItemId = URLEncoder.encode(serviceItemId, "UTF-8");
            Intent intent = new Intent(AppDelegate.ORDER_DRAWER_SCREEN);
            String str;
            //DateFrom=2016-10-26&DateTo=2016-09-26&keyword=&pageIndex=1&pageSize=20&serviceItemId=1004&status=1
            str = "&keyword=" + keyUTF + "&DateFrom=" + dateFrom + "&DateTo=" + dateTo +
                    "&status=" + Status + "&serviceItemId=" + ItemId + "&isInvoiceMatch=" + InvoiceMatch;
            intent.putExtra(AppDelegate.ORDER_LIST_SCREEN, str);
            LogUtils.e("订单的搜索" + str);
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
        mMatchAdapter.ResetPosition();
        mItemAdapter.ResetPosition();
    }

    public void ConFirmServiceItemId(String name) {
        serviceItemId = name;
    }

    public void ConFirmIsInvoiceMatch(String name) {
        switch (name) {
            case "已匹配":
                InvoiceMatch = "true";
                break;
            case "未匹配":
                InvoiceMatch = "false";
                break;
            default:
                InvoiceMatch = "";
                break;
        }
    }

    //确认状态的值
    public void ConfirmResult(String name) {
//        1待受理 2进行中 4已完成 -1已取消 -2已拒绝 0草稿 已交单结算3
        switch (name) {
            case "待受理":
                Status = "1";
                break;
            case "进行中":
                Status = "2";
                break;
            case "已完成":
                Status = "4";
                break;
            case "已取消":
                Status = "-1";
                break;
            case "已拒绝":
                Status = "-2";
                break;
            case "草稿":
                Status = "0";
                break;
            default:
                Status = "";
                break;
        }
    }

    boolean backHandled;

    @Override
    public boolean onBackPressed() {
// 当确认没有子Fragmnt时可以直接return false
        if (pvTimed.isShowing() || pvTime.isShowing()) {
            backHandled = true;
        } else {
            backHandled = false;
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