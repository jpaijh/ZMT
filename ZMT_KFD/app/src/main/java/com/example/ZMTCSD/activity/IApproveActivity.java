package com.example.ZMTCSD.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.adapter.MyFragmentAdapter;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.fragment.DrawerApprovalFragment_;
import com.example.ZMTCSD.fragment.IApproveManageListFragment_;
import com.example.ZMTCSD.utils.DeviceUtil;
import com.example.ZMTCSD.view.AndroidSegmentedControlView;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * 我审批的
 */
@EActivity(R.layout.my_drawer_segment_list)
public class IApproveActivity extends BaseActivity {
    private Context mContext;
    private RequestQueue mRequestQueue;
    private String IAPPROVAL_LEFT;
    private String IAPPROVAL_RIGHT;
    private String left = "UnDisposed";
    private String right = "Disposed";
    @ViewById(R.id.viewpager)
    ViewPager mViewPager;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    @ViewById(R.id.androidSegmentedControlView)
    AndroidSegmentedControlView mSegmentView;

    @ViewById(R.id.progressbar)
    CircleProgressBar mProgressbar;

    @ViewById(R.id.drawer_dr)
    DrawerLayout mDrawerLayout;
    FragmentManager manager;

    @Override
    public void onAfterViews() {
        mContext = IApproveActivity.this;
        showView(mProgressbar);//开启progress
        mRequestQueue = Volley.newRequestQueue(this);
        initToolbar();
        initArguments();
        initDrawerLayout();
        initTabLayout();
        init();
        initFragment();
    }

    private void initFragment() {
        DrawerApprovalFragment_ UnDisposed = new DrawerApprovalFragment_();
        Bundle leftBundle = new Bundle();
        leftBundle.putString(AppDelegate.DRAWER_APPROVAL_TYPE, left);
        UnDisposed.setArguments(leftBundle);
//
        DrawerApprovalFragment_ Disposed = new DrawerApprovalFragment_();
        Bundle rightBundle = new Bundle();
        rightBundle.putString(AppDelegate.DRAWER_APPROVAL_TYPE, right);
        Disposed.setArguments(rightBundle);
        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragment, UnDisposed, left).commit();
        manager.beginTransaction().add(R.id.fragment, Disposed, right).commit();
    }

    private void initDrawerLayout() {
        //不能滑动打开也不能滑动关闭
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //设置成可以滑动关闭
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                DeviceUtil.hideSoft(mContext, mDrawerLayout);
            }
        });
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
//        tv_title.setText(getStrings(R.string.toolbar_IApproveList));\
        tv_title.setText(getIntent().getStringExtra(AppDelegate.TOOLBAR_NAME));
    }

    private void initArguments() {
        IAPPROVAL_LEFT = MoreUserDal.GetServerUrl() + "/api/Approval/Approval/Lists?Type=1&PageSize=20&Status=0";
        IAPPROVAL_RIGHT = MoreUserDal.GetServerUrl() + "/api/Approval/Approval/Lists?Type=1&PageSize=20&Status=1";
    }

    private void init() {
        try {
//            mSegmentView.setEqualWidth(true); //设置为真,如果你想要每个段宽度相等
//            mSegmentView.setStretch(true); // 设置为true如果视图应该延伸至填满它的父视
            mSegmentView.setItems(getsStrings(R.array.Approve_state_option), null);
            mSegmentView.setIdentifier("seagment");
            mSegmentView.setDefaultSelection(0);
            mSegmentView.setOnSelectionChangedListener(new AndroidSegmentedControlView.OnSelectionChangedListener() {
                @Override
                public void newSelection(String identifier, String value) {
                    if (getStrings(R.string.Approve_item_UnApproval).equals(value)) {
                        mViewPager.setCurrentItem(0);
                    } else if (getStrings(R.string.Approve_item_Approval).equals(value)) {
                        mViewPager.setCurrentItem(1);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        try {
                            mSegmentView.setDefaultSelection(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        try {
                            mSegmentView.setDefaultSelection(1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void initTabLayout() {
        if (mViewPager != null) {
            MyFragmentAdapter mAdapter = new MyFragmentAdapter(getSupportFragmentManager());
            IApproveManageListFragment_ leftFragment = new IApproveManageListFragment_();
            Bundle leftBundle = new Bundle();
            leftBundle.putString(AppDelegate.APPROVAL_BASE_URL, IAPPROVAL_LEFT);
            leftBundle.putBoolean(AppDelegate.APPROVAL_LIST_TYPE, true); //传递是我审批的还是 我发起的
            leftBundle.putString(AppDelegate.DRAWER_APPROVAL_TYPE, left);
            leftFragment.setArguments(leftBundle);
            // rightFragment
            IApproveManageListFragment_ rightFragment = new IApproveManageListFragment_();
            Bundle rightBundle = new Bundle();
            rightBundle.putString(AppDelegate.APPROVAL_BASE_URL, IAPPROVAL_RIGHT);
            rightBundle.putBoolean(AppDelegate.APPROVAL_LIST_TYPE, true);
            rightBundle.putString(AppDelegate.DRAWER_APPROVAL_TYPE, right);
            rightFragment.setArguments(rightBundle);

            mAdapter.addFragment(leftFragment);
            mAdapter.addFragment(rightFragment);
            mViewPager.setOffscreenPageLimit(2);//缓存
            mViewPager.setAdapter(mAdapter);
        }
        hideView(mProgressbar);
    }

    @Override
    public void onBackgrounds() {
        super.onBackgrounds();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_approval_manage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_search:
                if (mSegmentView.getDefaultSelection() == 0) {
                    manager.beginTransaction().hide(manager.findFragmentByTag(right)).commit();
                    manager.beginTransaction().show(manager.findFragmentByTag(left)).commit();
                } else if (mSegmentView.getDefaultSelection() == 1) {
                    manager.beginTransaction().hide(manager.findFragmentByTag(left)).commit();
                    manager.beginTransaction().show(manager.findFragmentByTag(right)).commit();
                }
                mDrawerLayout.openDrawer(GravityCompat.END);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        mRequestQueue.cancelAll(this);
        super.onDestroy();
    }
}
