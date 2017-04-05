package com.example.ZMTCSD.activity;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.adapter.AttachmentFileAdapter;
import com.example.ZMTCSD.adapter.MyPropetyGroupsAdapter;
import com.example.ZMTCSD.entity.FileInfoEntity;
import com.example.ZMTCSD.entity.OrderDetailsEntity;
import com.example.ZMTCSD.entity.PropertyGroupsEntity;
import com.example.ZMTCSD.utils.IntentUtil;
import com.example.ZMTCSD.utils.ToastUtil;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import okhttp3.Call;

/**
 * 订单的服务项
 */
@EActivity(R.layout.activity_order_servers)
public class OrderServersActivity extends BaseActivity {
    private Context mContent;
    private int actionNo;
    private OrderDetailsEntity.OrderItems orderItems;
    private List<PropertyGroupsEntity> mGroupsList; //信息
    private List<FileInfoEntity> fileList;  //附件
    private List<OrderDetailsEntity.Actions> actionList; //服务项状态
    private List<OrderDetailsEntity.SubGroupList> subGroupList;//商品合并信息
    private MyPropetyGroupsAdapter mGroupAdapter;
    private AttachmentFileAdapter mAttachAdapter;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    @ViewById(R.id.tv_orServer_name)
    AppCompatTextView tv_name;

    @ViewById(R.id.tv_orServer_actions)
    AppCompatTextView tv_actions;

    @ViewById(R.id.recycler_orServer)
    RecyclerView mRecycler;

    @ViewById(R.id.rl_orServer_after)
    RelativeLayout afterGroup;

    @ViewById(R.id.tv_orServer_afterName)
    AppCompatTextView afterName;

    @ViewById(R.id.tv_orServer_afterSubName)
    AppCompatTextView afterSubName;

    //附件的情况
    @ViewById(R.id.ll_attachment)
    LinearLayout mAttach;

    @ViewById(R.id.recycler_attachment)
    RecyclerView mAttachRecycler;

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_title.setText(getStrings(R.string.toolbar_OrderServer));
    }

    @Override
    public void onAfterViews() {
        mContent = OrderServersActivity.this;
        initToolbar();
        hud = KProgressHUD.create(mContent)
                .setStyle(KProgressHUD.Style.BAR_DETERMINATE)
                .setLabel("正在下载...").setMaxProgress(100);
        orderItems = (OrderDetailsEntity.OrderItems) getIntent().getSerializableExtra(AppDelegate.ORDER_ITEMS);
        actionNo = getIntent().getIntExtra(AppDelegate.ORDER_ACTION_NAME, -2);
        mGroupsList = orderItems.getPropertyGroups();
        fileList = orderItems.getFiles();
        actionList = orderItems.getServiceItem().getActions();
        subGroupList = orderItems.getSubGroups();
        tv_name.setText(orderItems.getServiceItem().getName());
        tv_actions.setText(orderItems.getServiceItem().getActions().get(actionNo).getActionName());
        initRecycler();
    }

    private void initRecycler() {
        if (subGroupList != null && subGroupList.size() != 0) {
            afterName.setText(subGroupList.get(0).getSubGroupName());
            afterSubName.setText(subGroupList.get(0).getSubGroupSubName());
            showView(afterGroup);
        }

        mRecycler.setLayoutManager(new LinearLayoutManager(mContent, LinearLayoutManager.VERTICAL, false));
        mAttachRecycler.setLayoutManager(new LinearLayoutManager(mContent, LinearLayoutManager.VERTICAL, false));
        mAttachRecycler.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContent)
                .colorResId(R.color.line_color).marginResId(R.dimen.left_16, R.dimen.right_0) //记录左右的距离
                .build());
        if (mGroupsList != null && mGroupsList.size() != 0) {
            mGroupAdapter = new MyPropetyGroupsAdapter(mContent, mGroupsList);
            mRecycler.setAdapter(mGroupAdapter);
        }

        if (fileList != null && fileList.size() != 0) {
            showView(mAttach);
            mAttachAdapter = new AttachmentFileAdapter(mContent, fileList);
            mAttachAdapter.addmOnFileClickListener(new AttachmentFileAdapter.OnFileClickListener() {
                @Override
                public void onItemClick(String path, String name, String type) {
                    downloadFile(path, name, type);
                }
            });
            mAttachRecycler.setAdapter(mAttachAdapter);
        }
    }

    @Click(R.id.tv_orServer_actions)
    void DoActions() {
        //去状态栏
        OrderItemsActionActivity_.intent(mContent).extra(AppDelegate.ORDER_ACTIONS, (Serializable) actionList)
                .extra(AppDelegate.ORDER_ACTION_NAME, actionNo).start();
    }

    @Click(R.id.rl_orServer_after)
    void onAfterCommodity() {
        OrderCommodityActivity_.intent(mContent).extra(AppDelegate.ORDER_DETAIL_SUBGROUPS, (Serializable) subGroupList.get(0).getPropertyGroups())
                .extra(AppDelegate.ORDER_DETAIL_SUBGROUPNAME, subGroupList.get(0).getSubGroupName()).start();
    }


    private KProgressHUD hud;

    public void downloadFile(String url, String fileName, final String fileType) {
        OkHttpUtils.get().url(url).build().execute(new FileCallBack(getExternalFilesDir(null).getAbsolutePath(), fileName) {

            @Override
            public void onBefore(okhttp3.Request request, int id) {
                hud.show();
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                hud.setProgress((int) (100 * progress));
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                hud.dismiss();
                ToastUtil.showToast(mContent, "文件错误，请检查。。。");
            }

            @Override
            public void onResponse(File file, int id) {
                LogUtils.d(fileType + "下载完成" + file.getAbsolutePath());
                IntentUtil.OpenFile(mContent, fileType, file.getAbsolutePath());
            }
        });
    }

    @Override
    public void onBackgrounds() {
        super.onBackgrounds();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
