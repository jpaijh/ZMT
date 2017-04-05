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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.apkfuns.logutils.LogUtils;
import com.balysv.materialripple.MaterialRippleLayout;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.NoDoubleClickListener;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.entity.PaymentOrderEntity;
import com.example.ZMTCSD.fragment.DrawerKeywordFragment_;
import com.example.ZMTCSD.utils.DeviceUtil;
import com.example.ZMTCSD.utils.StringUtil;
import com.example.ZMTCSD.utils.ToastUtil;
import com.example.ZMTCSD.utils.VolleyErrorHelper;
import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.thinkcool.circletextimageview.CircleTextImageView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 待客提款中的资金账户  的选择订单
 */
@EActivity(R.layout.my_drawer_all_list)
public class PaymentNewOrdersActivity extends BaseActivity {
    private static final String TAG = "PaymentNewOrdersActivity";
    private static final String PaymentOrder = "payment_order";
    private RequestQueue mRequestQueue;
    private Context mContext;
    private int AccountId;
    private ArrayList<String> codeList;
    private int lastVisibleItem;
    private int firstVisibleItem;
    private boolean isLoading;
    private boolean isFirstLoading; //是否为第一次加载
    private int totalPage;   //数据有多少页数
    private int pageIndex = 1;   //第一页
    private LinearLayoutManager mLinearLayoutManager;
    private PaymentOrderAdapter mAdapter;
    private List<PaymentOrderEntity> rowsEntity;
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
        tv_title.setText(getStrings(R.string.toolbar_PaymentOrder));
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
        mContext = PaymentNewOrdersActivity.this;
        mRequestQueue = Volley.newRequestQueue(mContext);
        initToolbar();
        AccountId = getIntent().getIntExtra(AppDelegate.PYNEW_CUSTOMERID, -1);
        codeList = getIntent().getStringArrayListExtra(AppDelegate.PYNEW_FOUR_ORDERCODE);
        LogUtils.e(AccountId + ":订单:" + codeList.toString());
        initBroadcastReceiver();
        initDrawerLayout();
        initRecyclerView();
        initSwipeRefreshLayout();
        initFragment();
        super.onAfterViews();
    }

    private void initFragment() {
        DrawerKeywordFragment_ Keyword = new DrawerKeywordFragment_();
        Bundle bundle = new Bundle();
        bundle.putString(AppDelegate.PYNEW_SELECT_TYPE, PaymentOrder);
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
                if (intent.getAction().equals(AppDelegate.PAYMENT_NEW_SELECTOR) && intent.getStringExtra(AppDelegate.PYNEW_SELECT_TYPE).equals(PaymentOrder)) {
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
            DoPaymentOrder();
        } else {

        }
    }

    private void DoPaymentOrder() {
//        http://183.129.133.147:10086/api/payment/paymentOrders?accountId=3143903
        final String url = MoreUserDal.GetServerUrl() + "/api/payment/paymentOrders?accountId=" + AccountId + ARGUMENTS;
        LogUtils.d("待客提款订单" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                JSONObject jsonObject = JSON.parseObject(response.toString());
                int total = jsonObject.getIntValue("total");
                totalPage = jsonObject.getIntValue("totalPage");
                if (total == 0) {
                    showView(tv_no_data);
                    hideView(mRecyclerView);
                } else {
                    hideView(tv_no_data);
                    showView(mRecyclerView);
                }

                if (total != 0 && pageIndex == 1) {
                    JSONArray rows = jsonObject.getJSONArray("rows");
                    rowsEntity = JSON.parseArray(rows.toString(), PaymentOrderEntity.class);
                    mAdapter = new PaymentOrderAdapter(rowsEntity);
                    mRecyclerView.setAdapter(mAdapter);
                    if (!isFirstLoading)
                        createRefreshCompleteSnackbar(mRecyclerView);
                } else if (pageIndex <= totalPage) {
                    JSONArray rows = jsonObject.getJSONArray("rows");
                    rowsEntity.addAll(lastVisibleItem + 1, JSON.parseArray(rows.toString(), PaymentOrderEntity.class));
                    mAdapter.notifyDataSetChanged();
                    createLoadingCompleteSnackbar(mRecyclerView);
                }

                mSwipeRefreshLayout.setRefreshing(false);
                isLoading = false;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSwipeRefreshLayout.setRefreshing(false);
                isLoading = false;
                hideView(mRecyclerView);
                LogUtils.d(error.toString() + "查询失败" + VolleyErrorHelper.getMessage(error, mContext));
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
        jsonObjectRequest.setTag(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 3, 1.0f));
        mRequestQueue.add(jsonObjectRequest);
    }

    public class PaymentOrderAdapter extends RecyclerView.Adapter<PaymentOrderAdapter.ViewHolder> {
        private List<PaymentOrderEntity> OrderList;

        public PaymentOrderAdapter(List<PaymentOrderEntity> orderList) {
            OrderList = orderList;
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
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final PaymentOrderEntity entity = OrderList.get(position);
            if (codeList.contains(entity.getOrderCode())) {
                holder.name.setText(getStrings(R.string.Payment_newFinance4_nine) + " " + entity.getOrderCode());
            } else {
                holder.name.setText(StringUtil.StringToNull(entity.getOrderCode()));
            }
            holder.status.setText(getStrings(R.string.Payment_newFinance4_three) + " " + StringUtil.numberFormat(entity.getInvoiceAmount()));
            holder.ll_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!codeList.contains(entity.getOrderCode())) {
                        PaymentOrderEntity entity = OrderList.get(position);
                        Intent intent = getIntent();
                        intent.putExtra(AppDelegate.PYNEW_FOUR_ORDERENTITY, entity);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        ToastUtil.showToast(mContext, "已经添加此订单，请不要重复添加。");
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return OrderList.size();
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
