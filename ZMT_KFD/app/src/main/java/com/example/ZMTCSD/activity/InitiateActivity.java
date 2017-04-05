package com.example.ZMTCSD.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.adapter.MyFragmentAdapter;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.fragment.DrawerApprovalFragment_;
import com.example.ZMTCSD.fragment.IApproveManageListFragment_;
import com.example.ZMTCSD.utils.DeviceUtil;
import com.example.ZMTCSD.view.AndroidSegmentedControlView;
import com.example.ZMTCSD.AppDelegate;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

/**
 * 我发起的
 */
@EActivity(R.layout.my_drawer_segment_list)
public class InitiateActivity extends BaseActivity {
    private Context mContext;
    private RequestQueue mRequestQueue;
    private String Initiate_left;
    private String Initiate_right;
    private String Initiate_center;
    private String left = "InPass";
    private String centre = "Pass";
    private String right = "UnPass";

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

    private FragmentManager manager;

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_title.setText(getIntent().getStringExtra(AppDelegate.TOOLBAR_NAME));
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

    @Override
    public void onAfterViews() {
        mContext = InitiateActivity.this;
        mRequestQueue = Volley.newRequestQueue(this);
        showView(mProgressbar);//开启progress
        initToolbar();
        initArguments();
        initDrawerLayout();
        initTabLayout();
        init();
        initFragment();
    }

    private void initFragment() {
        DrawerApprovalFragment_ InPass = new DrawerApprovalFragment_();
        Bundle leftBundle = new Bundle();
        leftBundle.putString(AppDelegate.DRAWER_APPROVAL_TYPE, left);
        InPass.setArguments(leftBundle);

        DrawerApprovalFragment_ Pass = new DrawerApprovalFragment_();
        Bundle contreBundle = new Bundle();
        contreBundle.putString(AppDelegate.DRAWER_APPROVAL_TYPE, centre);
        Pass.setArguments(contreBundle);

        DrawerApprovalFragment_ UnPass = new DrawerApprovalFragment_();
        Bundle rightBundle = new Bundle();
        rightBundle.putString(AppDelegate.DRAWER_APPROVAL_TYPE, right);
        UnPass.setArguments(rightBundle);

        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragment, InPass, left).commit();
        manager.beginTransaction().add(R.id.fragment, Pass, centre).commit();
        manager.beginTransaction().add(R.id.fragment, UnPass, right).commit();
    }


    private void initArguments() {
        Initiate_left = MoreUserDal.GetServerUrl() + "/api/Approval/Approval/Lists?Type=0&PageSize=20&Status=10";
        Initiate_center = MoreUserDal.GetServerUrl() + "/api/Approval/Approval/Lists?Type=0&PageSize=20&Status=11";
        Initiate_right = MoreUserDal.GetServerUrl() + "/api/Approval/Approval/Lists?Type=0&PageSize=20&Status=12";
    }

    private void init() {
//            mSegmentView.setEqualWidth(true); //设置为真,如果你想要每个段宽度相等
//            mSegmentView.setStretch(true); // 设置为true如果视图应该延伸至填满它的父视
        try {
            mSegmentView.setItems(getsStrings(R.array.Initiate_state_option), null);
            mSegmentView.setIdentifier("seagment");
            mSegmentView.setDefaultSelection(0);
            mSegmentView.setOnSelectionChangedListener(new AndroidSegmentedControlView.OnSelectionChangedListener() {
                @Override
                public void newSelection(String identifier, String value) {
                    if (getStrings(R.string.Initiate_item_InApproval).equals(value)) {
                        mViewPager.setCurrentItem(0);
                    } else if (getStrings(R.string.Initiate_item_Approval).equals(value)) {
                        mViewPager.setCurrentItem(1);
                    } else if (getStrings(R.string.Initiate_item_UnApproval).equals(value)) {
                        mViewPager.setCurrentItem(2);
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
                    case 2:
                        try {
                            mSegmentView.setDefaultSelection(2);
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

    private void initTabLayout() {
        if (mViewPager != null) {
            final ArrayList<Fragment> fragmentList = new ArrayList<>();
            IApproveManageListFragment_ leftFragment = new IApproveManageListFragment_();
            Bundle leftBundle = new Bundle();
            leftBundle.putString(AppDelegate.APPROVAL_BASE_URL, Initiate_left);
            leftBundle.putBoolean(AppDelegate.APPROVAL_LIST_TYPE, false);
            leftBundle.putString(AppDelegate.DRAWER_APPROVAL_TYPE, left);
            leftFragment.setArguments(leftBundle);

            //contentFragment
            IApproveManageListFragment_ contentFragment = new IApproveManageListFragment_();
            Bundle contentBundle = new Bundle();
            contentBundle.putString(AppDelegate.APPROVAL_BASE_URL, Initiate_center);
            contentBundle.putBoolean(AppDelegate.APPROVAL_LIST_TYPE, false);
            contentBundle.putString(AppDelegate.DRAWER_APPROVAL_TYPE, centre);
            contentFragment.setArguments(contentBundle);

//             rightFragment
            IApproveManageListFragment_ rightFragment = new IApproveManageListFragment_();
            Bundle rightBundle = new Bundle();
            rightBundle.putString(AppDelegate.APPROVAL_BASE_URL, Initiate_right);
            rightBundle.putBoolean(AppDelegate.APPROVAL_LIST_TYPE, false);
            rightBundle.putString(AppDelegate.DRAWER_APPROVAL_TYPE, right);
            rightFragment.setArguments(rightBundle);

            fragmentList.add(leftFragment);
            fragmentList.add(contentFragment);
            fragmentList.add(rightFragment);
            MyFragmentAdapter mAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragmentList);
            mViewPager.setAdapter(mAdapter);
            mViewPager.setOffscreenPageLimit(2);
        }
        hideView(mProgressbar);
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
                    manager.beginTransaction().hide(manager.findFragmentByTag(centre)).commit();
                    manager.beginTransaction().hide(manager.findFragmentByTag(right)).commit();
                    manager.beginTransaction().show(manager.findFragmentByTag(left)).commit();
                } else if (mSegmentView.getDefaultSelection() == 1) {
                    manager.beginTransaction().hide(manager.findFragmentByTag(left)).commit();
                    manager.beginTransaction().hide(manager.findFragmentByTag(right)).commit();
                    manager.beginTransaction().show(manager.findFragmentByTag(centre)).commit();
                } else if (mViewPager.getCurrentItem() == 2) {
                    manager.beginTransaction().hide(manager.findFragmentByTag(left)).commit();
                    manager.beginTransaction().hide(manager.findFragmentByTag(centre)).commit();
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
