package com.example.ZMTCSD.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.apkfuns.logutils.LogUtils;
import com.balysv.materialripple.MaterialRippleLayout;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.NoDoubleClickListener;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.entity.CompanyBanksEntity;
import com.example.ZMTCSD.fragment.DrawerKeywordFragment_;
import com.example.ZMTCSD.utils.DeviceUtil;
import com.example.ZMTCSD.utils.StringUtil;
import com.example.ZMTCSD.utils.VolleyErrorHelper;
import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.thinkcool.circletextimageview.CircleTextImageView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 待客提款的 收款银行界面
 */
@EActivity(R.layout.my_drawer_all_list)
public class PaymentNewBanksActivity extends BaseActivity {
    private static final String TAG = "PaymentNewBanksActivity";
    private RequestQueue mRequestQueue;
    private static final String COMPANYBANK = "company_bank";
    private Context mContext;
    private int companyId;
    private int companyBnakId;//你选择银行的id
    private int lastVisibleItem;
    private int firstVisibleItem;
    private boolean isLoading;
    private boolean isFirstLoading; //是否为第一次加载
    private int totalPage;   //数据有多少页数
    private int pageIndex = 1;   //第一页
    private LinearLayoutManager mLinearLayoutManager;
    private CustomerListAdapter mAdapter;
    private List<CompanyBanksEntity> rowsEntity;
    private LocalBroadcastManager mBroadcastManager;
    private BroadcastReceiver mReceiver;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    @ViewById(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @ViewById(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @ViewById(R.id.tv_no_data)
    AppCompatTextView tv_no_data;


    @ViewById(R.id.drawer_dr)
    DrawerLayout mDrawerLayout;

    @ViewById(R.id.fragment)
    RelativeLayout mFragment;

    private String ARGUMENTS = "";

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_title.setText(getStrings(R.string.toolbar_CustomerList3));
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
        mContext = PaymentNewBanksActivity.this;
        mRequestQueue = Volley.newRequestQueue(mContext);
        initToolbar();
        companyId = getIntent().getIntExtra(AppDelegate.PYNEW_CUSTOMERID,-1);
        companyBnakId = getIntent().getIntExtra(AppDelegate.PYNEW_COMPANYID, -1);
        initBroadcastReceiver();
        initDrawerLayout();
        initRecyclerView();
        initSwipeRefreshLayout();
        initFragment();
    }

    private void initFragment() {
        DrawerKeywordFragment_ Keyword = new DrawerKeywordFragment_();
        Bundle bundle = new Bundle();
        bundle.putString(AppDelegate.PYNEW_SELECT_TYPE, COMPANYBANK);
        Keyword.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, Keyword).commit();

    }

    private void initBroadcastReceiver() {
        // 注册广播接收者
        mBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppDelegate.PAYMENT_NEW_SELECTOR);//待客提款中所有选择的 筛选广播动作
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(AppDelegate.PAYMENT_NEW_SELECTOR) && intent.getStringExtra(AppDelegate.PYNEW_SELECT_TYPE).equals(COMPANYBANK)) {
                    ARGUMENTS = intent.getStringExtra(AppDelegate.PAYMENT_LIST_SCREEN);
                    mDrawerLayout.closeDrawer(GravityCompat.END);
                    refreshList();
                }
            }
        };
        mBroadcastManager.registerReceiver(mReceiver, intentFilter);
    }

    /**
     * 上拉加载
     */
    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .colorResId(R.color.color_theme_bj).marginResId(R.dimen.left_16, R.dimen.right_0)
                .showLastDivider().build());
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if ((mAdapter != null && lastVisibleItem + 1 == mAdapter.getItemCount())
                        && newState == RecyclerView.SCROLL_STATE_IDLE && !isLoading && firstVisibleItem != 0) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    isLoading = true;
                    if (pageIndex < totalPage) {
                        createLoadingSnackbar(mRecyclerView); //创建正在加载的 snckbar
                        pageIndex = pageIndex + 1;
                        onBackgrounds();
                    } else {
                        mSwipeRefreshLayout.setRefreshing(false);
                        isLoading = false;
                        createNoDateSnackbar(mRecyclerView); //创建没有数据的 snckbar
                    }
                    super.onScrollStateChanged(recyclerView, newState);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();//获取滑动时最后显示出来的view是什么位置。
                firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    /**
     * 下拉刷新
     */
    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshList();
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_progress_1, R.color.color_progress_2);
        //当下拉加载时产生
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                isFirstLoading = false;
                createRefreshSnackbar(mRecyclerView, TAG);  //创建 正在刷新...的snackbar
                onBackgrounds();
            }
        });
    }

    /**
     * 刷新过程
     */
    private void refreshList() {
        mSwipeRefreshLayout.setRefreshing(true);
        isLoading = true;   //是否在刷新
        saveCurrentTime(TAG);  //保存当前时间
        pageIndex = 1;    //页数
        isFirstLoading = true;   //是否第一次加载
        onBackgrounds();
    }

    @Override
    public void onBackgrounds() {
        int second = (int) (System.currentTimeMillis() / 1000);
        if (isRefreshWin(second)) {
            DoCustomerList();
        } else {

        }
    }

    void DoCustomerList() {
        //http://183.129.133.147:10086/api/payment/banks?companyId=4679401&pageIndex=1&pageSize=20
        final String url = MoreUserDal.GetServerUrl() + "/api/payment/banks?pageSize=20&companyId=" + companyId + "&pageIndex=" + pageIndex + ARGUMENTS;
        LogUtils.d("待客提款的 收款银行" + url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<org.json.JSONArray>() {
            @Override
            public void onResponse(org.json.JSONArray response) {
                rowsEntity = JSON.parseArray(response.toString(), CompanyBanksEntity.class);
                if (rowsEntity != null && rowsEntity.size() != 0) {
                    mAdapter = new CustomerListAdapter(rowsEntity);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    tv_no_data.setText("没有银行信息");
                    showView(tv_no_data);
                }

                mSwipeRefreshLayout.setRefreshing(false);
                isLoading = false;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d("查询失败" + VolleyErrorHelper.getMessage(error, mContext));
                mSwipeRefreshLayout.setRefreshing(false);
                isLoading = false;
                tv_no_data.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.ic_bug3), null, null);
                tv_no_data.setText(VolleyErrorHelper.getMessage(error, mContext));
                showView(tv_no_data);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "bearer   " + MoreUserDal.GetAccessToken());
                return map;
            }
        };
        jsonArrayRequest.setTag(this);
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 3, 1.0f));
        mRequestQueue.add(jsonArrayRequest);
    }

    public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.ViewHolder> {
        private List<CompanyBanksEntity> list;

        public CustomerListAdapter(List<CompanyBanksEntity> list) {
            this.list = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            final LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            return new ViewHolder(MaterialRippleLayout.on(inflater.inflate(R.layout.item_company_list, viewGroup, false))
                    .rippleOverlay(true)//如果这是真的,涟漪在前景;错:背景
                    .rippleAlpha(0.1f)//α的涟漪
                    .rippleColor(getColors(R.color.color_ripple_btn))//涟漪的颜色
                    .rippleDelayClick(true)  //延迟调用onclicklistener直到涟漪反应结束
                    .rippleHover(true)//如果这是真的,悬停效果时画的观点是感动
                    .create());
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            CompanyBanksEntity entity = list.get(position);
            holder.name.setText(StringUtil.StringToNull(entity.getBankName()));
            holder.status.setText(StringUtil.StringToNull(entity.getBankAccount()));
            if (companyBnakId == entity.getId())
                showView(holder.TickImg);
            else {
                hideView(holder.TickImg);
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public RelativeLayout ll_content;
            public CircleTextImageView mCircleImg;
            public AppCompatTextView name;
            public AppCompatTextView status;
            public AppCompatImageView TickImg;

            public ViewHolder(View itemView) {
                super(itemView);
                ll_content = (RelativeLayout) itemView.findViewById(R.id.ll_content);
                mCircleImg = (CircleTextImageView) itemView.findViewById(R.id.img_circle);
                name = (AppCompatTextView) itemView.findViewById(R.id.tv_name);
                status = (AppCompatTextView) itemView.findViewById(R.id.tv_value);
                TickImg = (AppCompatImageView) itemView.findViewById(R.id.img_tick);
                hideView(mCircleImg);
                ll_content.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        CompanyBanksEntity entity = list.get(getLayoutPosition());
                        Intent intent = getIntent();
                        intent.putExtra(AppDelegate.PYNEW_COMPANYBANKENTITY, entity);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }
        }
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
                finish();
                super.onBackPressed();
            }
        }
    }


    @Override
    protected void onDestroy() {
        mRequestQueue.cancelAll(this);
        mBroadcastManager.unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
