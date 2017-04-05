package com.example.ZMTCSD.activity;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.adapter.MyPropetyGroupsAdapter;
import com.example.ZMTCSD.entity.PropertyGroupsEntity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * 客户中心-客户的详细信息-客户的基本信息
 */
@EActivity(R.layout.my_basic_recycler)
public class CustomerBasicInfoActivity extends BaseActivity {
    private Context mContext;
    private List<PropertyGroupsEntity> mGroupsList;
    private MyPropetyGroupsAdapter mGroupAdapter;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    @ViewById(R.id.recyclerView)
    RecyclerView mDetailsRecycler;

    @ViewById(R.id.tv_no_data)
    AppCompatTextView tv_no_data;

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_title.setText(getStrings(R.string.toolbar_CustomerBasic));
    }

    @Override
    public void onAfterViews() {
        mContext = CustomerBasicInfoActivity.this;
        initToolbar();
        mGroupsList = (List<PropertyGroupsEntity>) getIntent().getSerializableExtra(AppDelegate.CUSTOMER_BASIC_COMPANY);
        mDetailsRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mDetailsRecycler.setHasFixedSize(true);
        if (mGroupsList != null && mGroupsList.size() != 0) {
            mGroupAdapter = new MyPropetyGroupsAdapter(mContext, mGroupsList);
            mDetailsRecycler.setAdapter(mGroupAdapter);
        } else {
            showView(tv_no_data);
        }
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
