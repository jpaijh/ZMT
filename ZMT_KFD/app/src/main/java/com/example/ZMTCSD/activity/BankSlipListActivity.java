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
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.fragment.BankSlipListFragment_;
import com.example.ZMTCSD.fragment.DrawerBankSlipFragment_;
import com.example.ZMTCSD.view.AndroidSegmentedControlView;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.adapter.MyFragmentAdapter;
import com.example.ZMTCSD.utils.DeviceUtil;
import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * 水单的列表界面
 */

@EActivity(R.layout.my_drawer_segment_list)
public class BankSlipListActivity extends BaseActivity {
    private RequestQueue mRequestQueue;
    private Context mContext;
    private String left = "Unclaimed";
    private String right = "claimed";
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    @ViewById(R.id.viewpager)
    ViewPager mViewPager;

    @ViewById(R.id.androidSegmentedControlView)
    AndroidSegmentedControlView mSegmentView;

    @ViewById(R.id.progressbar)
    CircleProgressBar mProgressbar;

    @ViewById(R.id.drawer_dr)
    DrawerLayout mDrawerLayout;

    FragmentManager manager;

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
        mContext = BankSlipListActivity.this;
        mRequestQueue = Volley.newRequestQueue(mContext);
        initToolbar();
        initTabLayout();
        initDrawerLayout();
        init();
        initFragment();
    }

    private void initFragment() {
        DrawerBankSlipFragment_ BankSlipLeft = new DrawerBankSlipFragment_();
        Bundle leftBundle = new Bundle();
        leftBundle.putBoolean(AppDelegate.BANKSLIP_LIST_TYPE, false);
        BankSlipLeft.setArguments(leftBundle);

        DrawerBankSlipFragment_ BankSlipRight = new DrawerBankSlipFragment_();
        Bundle rightBundle = new Bundle();
        rightBundle.putBoolean(AppDelegate.BANKSLIP_LIST_TYPE, true);
        BankSlipRight.setArguments(rightBundle);

        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragment, BankSlipLeft, left).commit();
        manager.beginTransaction().add(R.id.fragment, BankSlipRight, right).commit();
    }

    private void init() {
        try {
            mSegmentView.setItems(getsStrings(R.array.bank_slips_option), null);
            mSegmentView.setIdentifier("seagment");
            mSegmentView.setDefaultSelection(0);
            mSegmentView.setOnSelectionChangedListener(new AndroidSegmentedControlView.OnSelectionChangedListener() {
                @Override
                public void newSelection(String identifier, String value) {
                    if (getStrings(R.string.bank_slips_option).equals(value)) {
                        mViewPager.setCurrentItem(0);
                    } else if (getStrings(R.string.bank_slips_option1).equals(value)) {
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
            BankSlipListFragment_ leftFragment = new BankSlipListFragment_();
            Bundle leftBundle = new Bundle();
            leftBundle.putBoolean(AppDelegate.BANKSLIP_LIST_TYPE, false);
            leftFragment.setArguments(leftBundle);
            // rightFragment
            BankSlipListFragment_ rightFragment = new BankSlipListFragment_();
            Bundle rightBundle = new Bundle();
            rightBundle.putBoolean(AppDelegate.BANKSLIP_LIST_TYPE, true);
            rightFragment.setArguments(rightBundle);
            mAdapter.addFragment(leftFragment);
            mAdapter.addFragment(rightFragment);
            mViewPager.setOffscreenPageLimit(2);//缓存
            mViewPager.setAdapter(mAdapter);
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
                    manager.beginTransaction().hide(manager.findFragmentByTag(right)).commit();
                    manager.beginTransaction().show(manager.findFragmentByTag(left)).commit();

                } else if (mViewPager.getCurrentItem() == 1) {
                    manager.beginTransaction().hide(manager.findFragmentByTag(left)).commit();
                    manager.beginTransaction().show(manager.findFragmentByTag(right)).commit();
                }
                mDrawerLayout.openDrawer(GravityCompat.END);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                mDrawerLayout.closeDrawer(GravityCompat.END);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onDestroy() {
        mRequestQueue.cancelAll(this);
        super.onDestroy();
    }

}