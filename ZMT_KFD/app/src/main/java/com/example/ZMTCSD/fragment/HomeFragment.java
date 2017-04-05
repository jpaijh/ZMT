package com.example.ZMTCSD.fragment;

import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.activity.BankSlipListActivity_;
import com.example.ZMTCSD.activity.CompanyBankActivity_;
import com.example.ZMTCSD.activity.CompanyBuyCodeActivity_;
import com.example.ZMTCSD.activity.CompanyInsureActivity_;
import com.example.ZMTCSD.activity.CompanyLCLimitActivity_;
import com.example.ZMTCSD.activity.CustomerActivity_;
import com.example.ZMTCSD.activity.IApproveActivity_;
import com.example.ZMTCSD.activity.InitiateActivity_;
import com.example.ZMTCSD.activity.InvoiceListActivity_;
import com.example.ZMTCSD.activity.OrderListActivity_;
import com.example.ZMTCSD.activity.PaymentListActivity_;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.entity.MetaDataEntity;
import com.example.ZMTCSD.view.dynamicgridview.DynamicGridView;
import com.example.ZMTCSD.adapter.DynamicGridViewAdapter;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import java.util.ArrayList;
import java.util.List;


@EFragment(R.layout.fragment_home)
public class HomeFragment extends BaseFragment {
    private Context mContext;
    private List<DynamicGridViewAdapter.ItemHomeGridView> itemLists;
    private DynamicGridViewAdapter mAdapter;
    @ViewById(R.id.home_gv_drnamicgrid)
    DynamicGridView mGridView;

    @Override
    public void onAfterViews() {
        mContext=getActivity();
        initGridView();

//        BadgeView badge = new BadgeView(getActivity(), mGridView);
////        badge.setBadgeBackgroundColor(droidGreen);
//        badge.setTextColor(Color.BLACK);
//        badge.setText("5");
//        badge.show();
    }

    private void initGridView() {
        itemLists = new ArrayList<>();
//        // 我审批的
//        DynamicGridViewAdapter.ItemHomeGridView approval_manage = new DynamicGridViewAdapter.ItemHomeGridView(R.string.home_me_approval, R.mipmap.ic_me_approval_801);
//        // 我发起的
//        DynamicGridViewAdapter.ItemHomeGridView start_manage = new DynamicGridViewAdapter.ItemHomeGridView(R.string.home_me_start, R.mipmap.ic_me_start_802);
//        // 消息中心
//        DynamicGridViewAdapter.ItemHomeGridView message_manage = new DynamicGridViewAdapter.ItemHomeGridView(R.string.home_me_message, R.mipmap.ic_me_message_101);
//        //客户管理
//        DynamicGridViewAdapter.ItemHomeGridView customer_manage = new DynamicGridViewAdapter.ItemHomeGridView(R.string.home_me_customer, R.mipmap.ic_me_customer_201);
//        //订单管理
//        DynamicGridViewAdapter.ItemHomeGridView orders_manage = new DynamicGridViewAdapter.ItemHomeGridView(R.string.home_me_orders, R.mipmap.ic_me_orders_301);
//        //增票管理
//        DynamicGridViewAdapter.ItemHomeGridView invoice_manage = new DynamicGridViewAdapter.ItemHomeGridView(R.string.home_me_Invoice, R.mipmap.ic_me_invoice_303);
//        //水单管理
//        DynamicGridViewAdapter.ItemHomeGridView bankSlip_manage = new DynamicGridViewAdapter.ItemHomeGridView(R.string.home_me_bankSlip, R.mipmap.ic_me_bankslip_304);
//        //付款管理
//        DynamicGridViewAdapter.ItemHomeGridView payment_manage = new DynamicGridViewAdapter.ItemHomeGridView(R.string.home_me_payment, R.mipmap.ic_me_payment_302);
//        //中信保外商代码
//        DynamicGridViewAdapter.ItemHomeGridView company_buyerCode = new DynamicGridViewAdapter.ItemHomeGridView(R.string.home_me_buyerCode, R.mipmap.ic_me_buyer_code_401);
//        //中信保的限额
//        DynamicGridViewAdapter.ItemHomeGridView company_LCLimit = new DynamicGridViewAdapter.ItemHomeGridView(R.string.home_me_LCLimit, R.mipmap.ic_me_lc_limit_403);
//        //中信保的银行
//        DynamicGridViewAdapter.ItemHomeGridView company_Bank = new DynamicGridViewAdapter.ItemHomeGridView(R.string.home_me_BankApply, R.mipmap.ic_me_bank_apply_402);
//        //中信保的投保
//        DynamicGridViewAdapter.ItemHomeGridView company_Insure = new DynamicGridViewAdapter.ItemHomeGridView(R.string.home_me_InsureApply, R.mipmap.ic_me_insure_apply_404);


    List<MetaDataEntity.Value> s=MoreUserDal.GetUserPermissToValue();
    for(MetaDataEntity.Value v:s){
        switch (v.getModuleId() ){
            case "801":
                itemLists.add(new DynamicGridViewAdapter.ItemHomeGridView(v.getModuleId(),v.getModuleName(),R.mipmap.ic_me_start_801));
                break;
            case "802":
                itemLists.add(new DynamicGridViewAdapter.ItemHomeGridView(v.getModuleId(),v.getModuleName(),R.mipmap.ic_me_approval_802));
                break;
            case "101":
                itemLists.add(new DynamicGridViewAdapter.ItemHomeGridView(v.getModuleId(),v.getModuleName(),R.mipmap.ic_me_message_101));
                break;
            case "201":
                itemLists.add(new DynamicGridViewAdapter.ItemHomeGridView(v.getModuleId(),v.getModuleName(),R.mipmap.ic_me_customer_201));
                break;
            case "301":
                itemLists.add(new DynamicGridViewAdapter.ItemHomeGridView(v.getModuleId(),v.getModuleName(),R.mipmap.ic_me_orders_301));
                break;
            case "302":
                itemLists.add(new DynamicGridViewAdapter.ItemHomeGridView(v.getModuleId(),v.getModuleName(),R.mipmap.ic_me_payment_302));
                break;
            case "303":
                itemLists.add(new DynamicGridViewAdapter.ItemHomeGridView(v.getModuleId(),v.getModuleName(),R.mipmap.ic_me_invoice_303));
                break;
            case "304":
                itemLists.add(new DynamicGridViewAdapter.ItemHomeGridView(v.getModuleId(),v.getModuleName(),R.mipmap.ic_me_bankslip_304));
                break;
            case "401":
                itemLists.add(new DynamicGridViewAdapter.ItemHomeGridView(v.getModuleId(),v.getModuleName(),R.mipmap.ic_me_buyer_code_401));
                break;
            case "402":
                itemLists.add(new DynamicGridViewAdapter.ItemHomeGridView(v.getModuleId(),v.getModuleName(),R.mipmap.ic_me_bank_apply_402));
                break;
            case "403":
                itemLists.add(new DynamicGridViewAdapter.ItemHomeGridView(v.getModuleId(),v.getModuleName(),R.mipmap.ic_me_lc_limit_403));
                break;
            case "404":
                itemLists.add(new DynamicGridViewAdapter.ItemHomeGridView(v.getModuleId(),v.getModuleName(),R.mipmap.ic_me_insure_apply_404));
                break;
        }
    }
        mAdapter = new DynamicGridViewAdapter(mContext, itemLists, getResources().getInteger(R.integer.gridView_columns));
        mGridView.setAdapter(mAdapter);
        // 设置是否显示网格线
        mGridView.setShowLines(true, true);
        // 拖拽之后的处理：停止编辑模式,保存用户操作之后状态
        mGridView.setOnDropListener(new DynamicGridView.OnDropListener() {
            @Override
            public void onActionDrop() {
                mGridView.stopEditMode();
            }
        });

        mGridView.setOnDragListener(new DynamicGridView.OnDragListener() {
            @Override
            public void onDragStarted(int position) {
            }

            @Override
            public void onDragPositionsChanged(int oldPosition, int newPosition) {
                LogUtils.d(String.format("drag gridview item %d to %d", oldPosition, newPosition));
            }
        });

        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // 添加震动
                Vibrator mVibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                mVibrator.vibrate(50);
                // 长点击开启拖拽模式
                mGridView.startEditMode(position);
                return true;
            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                             @Override
                                             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                 DynamicGridViewAdapter.ItemHomeGridView item = (DynamicGridViewAdapter.ItemHomeGridView) parent.getAdapter().getItem(position);
                                                 switch (item.getPermissionId()) {
                                                     case "801": // 进入我审批的
                                                         IApproveActivity_.intent(mContext).extra(AppDelegate.TOOLBAR_NAME,item.getTextRes()).start();
                                                         break;
                                                     case "802": // 进入我发起的
                                                         InitiateActivity_.intent(mContext).extra(AppDelegate.TOOLBAR_NAME,item.getTextRes()).start();
                                                         break;
//                                                     case "101": // 进入消息中心
//                                                         MessageActivity_.intent(mContext).extra(AppDelegate.TOOLBAR_NAME,item.getTextRes()).start();
//                                                         break;
                                                     case "201":// 进入客户中心
                                                         CustomerActivity_.intent(mContext).extra(AppDelegate.TOOLBAR_NAME,item.getTextRes()).start();
                                                         break;
                                                     case "301":// 进入订单中心
                                                         OrderListActivity_.intent(mContext).extra(AppDelegate.TOOLBAR_NAME,item.getTextRes()).start();
                                                         break;
                                                     case "302": //
                                                         PaymentListActivity_.intent( mContext).extra(AppDelegate.TOOLBAR_NAME,item.getTextRes()).start();
                                                         break;
                                                     case "303": // 进入增票中心
                                                         InvoiceListActivity_.intent(mContext).extra(AppDelegate.TOOLBAR_NAME,item.getTextRes()).start();
                                                         break;
                                                     case "304": // 进入水单中心
                                                         BankSlipListActivity_.intent(mContext).extra(AppDelegate.TOOLBAR_NAME,item.getTextRes()).start();
                                                         break;
                                                     case "401":
                                                         CompanyBuyCodeActivity_.intent(mContext).extra(AppDelegate.TOOLBAR_NAME,item.getTextRes()).start();
                                                         break;
                                                     case "403":
                                                         CompanyLCLimitActivity_.intent(mContext).extra(AppDelegate.TOOLBAR_NAME,item.getTextRes()).start();
                                                         break;
                                                     case "402":
                                                         CompanyBankActivity_.intent(mContext).extra(AppDelegate.TOOLBAR_NAME,item.getTextRes()).start();
                                                         break;
                                                     case "404":
                                                         CompanyInsureActivity_.intent(mContext).extra(AppDelegate.TOOLBAR_NAME,item.getTextRes()).start();
                                                         break;
                                                 }
                                             }
                                         }
        );
    }
}
