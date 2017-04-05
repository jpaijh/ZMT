package com.example.ZMTCSD.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.adapter.MyFragmentAdapter;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.entity.CompanyDetailsEntity;
import com.example.ZMTCSD.entity.FileInfoEntity;
import com.example.ZMTCSD.entity.PropertyGroupsEntity;
import com.example.ZMTCSD.fragment.CompanyBanksFragment_;
import com.example.ZMTCSD.fragment.CompanyBasicFragment_;
import com.example.ZMTCSD.fragment.CompanyFilesFragment_;
import com.example.ZMTCSD.utils.VolleyErrorHelper;
import com.example.ZMTCSD.view.AndroidSegmentedControlView;
import com.example.ZMTCSD.AppDelegate;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户中心-客户的详细信息-往来单位-详细信息
 * 也是中信报外商代码的详情
 */
@EActivity(R.layout.my_segment_all_list)
public class CustomerCompanyDetailsActivity extends BaseActivity {
    private Context mContent;
    private RequestQueue mRequestQueue;
    private final int BuyerCodeType = 0;
    private final String CodeType = "BuyerCodeDetails"; //
    private final String CompanyType = "CompanyDetails";//往来单位的
    private int companyId; //详情的id
    private String IsType;
    private String applyKeyword; //外商查看批复的id.
    private CompanyDetailsEntity detailsEntity;
    private List<PropertyGroupsEntity> propertyGroupsList = new ArrayList<>();
    private List<FileInfoEntity> filesList = new ArrayList<>();

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    @ViewById(R.id.progressbar)
    CircleProgressBar mProgressbar;

    @ViewById(R.id.androidSegmentedControlView)
    AndroidSegmentedControlView mSegmentView;

    @ViewById(R.id.viewpager)
    ViewPager mViewPager;


    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        if (CodeType.equals(IsType)) {
            tv_title.setText(getStrings(R.string.toolbar_company_BuyerCode_details));
        } else if (CompanyType.equals(IsType))
            tv_title.setText(getStrings(R.string.toolbar_CompanyDetails));
    }

    @Override
    public void onAfterViews() {
        mContent = CustomerCompanyDetailsActivity.this;
        mRequestQueue = Volley.newRequestQueue(mContent);
        showView(mProgressbar);//开启progress
        companyId = getIntent().getIntExtra(AppDelegate.CUSTOMER_ID_COMPANY, 0);//获取从列表传来的值
        applyKeyword = getIntent().getStringExtra(AppDelegate.REPLYSKEYWORD);
        IsType = getIntent().getStringExtra(AppDelegate.TYPE);
        initToolbar();
        onBackgrounds();
        initViewPager();
    }

    private void initTabLayout() {
        if (mViewPager != null) {
            final ArrayList<Fragment> fragmentList = new ArrayList<>();
            //TODO 加载 基本信息，附件， 银行的fragment
            CompanyBasicFragment_ leftFragment = new CompanyBasicFragment_();
            Bundle leftBundle = new Bundle();
            leftBundle.putSerializable(AppDelegate.CUSTOMER_BASIC_COMPANY, (Serializable) propertyGroupsList);
            leftFragment.setArguments(leftBundle);
//            //contentFragment
            CompanyFilesFragment_ contentFragment = new CompanyFilesFragment_();
            Bundle contentBundle = new Bundle();
            contentBundle.putSerializable(AppDelegate.CUSTOMER_ATTACH_COMPANY, (Serializable) filesList);
            contentFragment.setArguments(contentBundle);

//             rightFragment
            CompanyBanksFragment_ rightFragment = new CompanyBanksFragment_();
            Bundle rightBundle = new Bundle();
            rightBundle.putInt(AppDelegate.CUSTOMER_ID_COMPANY, companyId);
            rightFragment.setArguments(rightBundle);

            fragmentList.add(leftFragment);
            fragmentList.add(contentFragment);
            fragmentList.add(rightFragment);
            MyFragmentAdapter mAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragmentList);
            mViewPager.setAdapter(mAdapter);
            mViewPager.setOffscreenPageLimit(2);
        }
//        hideView(mProgressbar);
    }

    private void initViewPager() {
//        mSegmentView.setEqualWidth(true); //设置为真,如果你想要每个段宽度相等
//            mSegmentView.setStretch(true); // 设置为true如果视图应该延伸至填满它的父视
        try {
            mSegmentView.setItems(getsStrings(R.array.Customer_company_option), null);
            mSegmentView.setIdentifier("seagment");
            mSegmentView.setDefaultSelection(0);
            mSegmentView.setOnSelectionChangedListener(new AndroidSegmentedControlView.OnSelectionChangedListener() {
                @Override
                public void newSelection(String identifier, String value) {
                    if (getStrings(R.string.Customer_company_item_basic).equals(value)) {
                        mViewPager.setCurrentItem(0);
                    } else if (getStrings(R.string.Customer_company_item_file).equals(value)) {
                        mViewPager.setCurrentItem(1);
                    } else if (getStrings(R.string.Customer_company_item_banks).equals(value)) {
                        mViewPager.setCurrentItem(2);
                    }
                }
            });

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackgrounds() {
        int second = (int) (System.currentTimeMillis() / 1000);
        if (isRefreshWin(second)) {
            DoCompanyDetails();
        }
    }

    void DoCompanyDetails() {
        final String url = MoreUserDal.GetServerUrl() + "/api/platformAccount/company?companyId=" + companyId;
        LogUtils.d("往来单位或者外商代码详情" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                detailsEntity = JSON.parseObject(response.toString(), CompanyDetailsEntity.class);
                propertyGroupsList = detailsEntity.getPropetyGroups();
                filesList = detailsEntity.getFileInfos();
                hideView(mProgressbar);
                initTabLayout();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d("详细数据错误" + VolleyErrorHelper.getMessage(error, mContent));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "bearer " + MoreUserDal.GetAccessToken());
                return map;
            }
        };
        jsonObjectRequest.setTag(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 3, 1.0f));
        mRequestQueue.add(jsonObjectRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reply, menu);
        MenuItem menuItem = menu.findItem(R.id.action_reply);
        if (CompanyType.equals(IsType)) {
            menuItem.setEnabled(false);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_reply:
                //TODO 去查看批复的界面。
                CompanyReplysActivity_.intent(mContent).extra(AppDelegate.REPLYSKEYWORD, applyKeyword)
                        .extra(AppDelegate.TYPE, BuyerCodeType).start();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        mRequestQueue.cancelAll(this);
        super.onDestroy();
    }
}
