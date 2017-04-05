package com.example.ZMTCSD.activity;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.adapter.MyPropetyGroupsAdapter;
import com.example.ZMTCSD.entity.PropertyGroupsEntity;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * 订单中的合并前后的商品信息 （一般在“自助通关/结汇”服务项）
 */
@EActivity(R.layout.my_basic_recycler)
public class OrderCommodityActivity extends BaseActivity {
    private Context mContent;
    private List<PropertyGroupsEntity> mGroupsList;
    private String subGroupName;
    private MyPropetyGroupsAdapter mGroupAdapter;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    @ViewById(R.id.nested_scroll_view)
    NestedScrollView mNestedView;

    @ViewById(R.id.recyclerView)
    RecyclerView mDetailsRecycler;

    @ViewById(R.id.tv_no_data)
    AppCompatTextView tv_no_data;

    @ViewById(R.id.progressbar)
    CircleProgressBar mProgress;

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

    }

    @Override
    public void onAfterViews() {
        mContent = OrderCommodityActivity.this;
        initToolbar();
        mGroupsList = (List<PropertyGroupsEntity>)
                getIntent().getSerializableExtra(AppDelegate.ORDER_DETAIL_SUBGROUPS);
        subGroupName = getIntent().getStringExtra(AppDelegate.ORDER_DETAIL_SUBGROUPNAME);
        tv_title.setText(subGroupName);
        mDetailsRecycler.setLayoutManager(new LinearLayoutManager(mContent, LinearLayoutManager.VERTICAL, false));
        mDetailsRecycler.setHasFixedSize(true);
        mGroupAdapter = new MyPropetyGroupsAdapter(mContent, mGroupsList);
        mDetailsRecycler.setAdapter(mGroupAdapter);
        mNestedView.smoothScrollTo(0, 0);
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
        finish();
        super.onDestroy();
    }
}
