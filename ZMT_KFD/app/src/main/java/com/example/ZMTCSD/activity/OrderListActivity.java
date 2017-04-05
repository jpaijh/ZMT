package com.example.ZMTCSD.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.example.ZMTCSD.dal.MetaDataDal;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.entity.OrderListEntity;
import com.example.ZMTCSD.fragment.DrawerOrderFragment_;
import com.example.ZMTCSD.utils.DeviceUtil;
import com.example.ZMTCSD.utils.StringUtil;
import com.example.ZMTCSD.utils.VolleyErrorHelper;
import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.yqritc.recyclerviewflexibledivider.VerticalDividerItemDecoration;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 订单列表界面
 */
@EActivity(R.layout.my_drawer_all_list)
public class OrderListActivity extends BaseActivity {
    private static final String TAG = "OrderListActivity";
    private RequestQueue mRequestQueue;
    private Context mContext;
    private int lastVisibleItem;
    private int firstVisibleItem;
    private boolean isLoading;
    private boolean isFirstLoading; //是否为第一次加载
    private int totalPage;   //数据有多少页数
    private int pageIndex = 1;   //第一页
    private LinearLayoutManager mLinearLayoutManager;
    private OrderListAdapter mAdapter;
    private ServerItemAdapter mImageAdapter;
    private List<OrderListEntity> rowsEntity;
    private LocalBroadcastManager mBroadcastManager;
    private BroadcastReceiver mReceiver;
    private int[] colorshort;

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
        mContext = OrderListActivity.this;
        mRequestQueue = Volley.newRequestQueue(mContext);
        colorshort = new int[]{R.color.short1, R.color.short2, R.color.short3, R.color.short4, R.color.short5, R.color.short6};
        initToolbar();
        initBroadcastReceiver();
        initDrawerLayout();
        initRecyclerView();
        initSwipeRefreshLayout();
        initFragment();
    }

    public void initFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new DrawerOrderFragment_()).commit();
    }

    /**
     * 内部的广播
     */
    private void initBroadcastReceiver() {
        // 注册广播接收者
        mBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppDelegate.ORDER_DRAWER_SCREEN); //订单的广播
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (AppDelegate.ORDER_DRAWER_SCREEN.equals(intent.getAction())) {
                    ARGUMENTS = intent.getStringExtra(AppDelegate.ORDER_LIST_SCREEN);
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
        mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this).colorResId(R.color.color_theme_bj)
                        .sizeResId(R.dimen.left_16).showLastDivider() //大小
                        .build());//记录左右的距离
        //TODO 当数据不满足一屏幕的时候，刷新不了数据。 所以加了 firstVisibleItem!=0
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
                firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();//获取滑动时的开始的position
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    /**
     * 下拉刷新
     */
    private void initSwipeRefreshLayout() {
        //自动刷新
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
            DoOrderList();
        } else {

        }
    }

    void DoOrderList() {
        final String url = MoreUserDal.GetServerUrl() + "/api/order/orders?PageSize=20&pageIndex=" + pageIndex + ARGUMENTS;
        LogUtils.d("订单列表数据" + url);
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
                    rowsEntity = JSON.parseArray(rows.toString(), OrderListEntity.class);
                    mAdapter = new OrderListAdapter(rowsEntity);
                    mRecyclerView.setAdapter(mAdapter);
                    if (!isFirstLoading)
                        createRefreshCompleteSnackbar(mRecyclerView);
                } else if (pageIndex <= totalPage) {
                    JSONArray rows = jsonObject.getJSONArray("rows");
                    rowsEntity.addAll(lastVisibleItem + 1, JSON.parseArray(rows.toString(), OrderListEntity.class));
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

    public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
        private List<OrderListEntity> list;

        public OrderListAdapter(List<OrderListEntity> list) {
            this.list = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            final LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            return new ViewHolder(
                    MaterialRippleLayout.on(inflater.inflate(R.layout.item_orders_list, viewGroup, false))
                            .rippleOverlay(true)//如果这是真的,涟漪在前景;错:背景
                            .rippleAlpha(0.1f)//α的涟漪
                            .rippleColor(getColors(R.color.color_ripple_btn))//涟漪的颜色
                            .rippleDelayClick(true)
                            .rippleHover(true)//如果这是真的,悬停效果时画的观点是感动
                            .create()
            );
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            OrderListEntity entity = list.get(position);
            holder.Name.setText(StringUtil.StringToNull(entity.getAccountName()));
            holder.Status.setText(entity.getStatusStr());
            holder.Code.setText(entity.getCode());
            holder.Amount.setText(entity.getCurrency() + " " + StringUtil.numberFormat(entity.getDeclarationAmount()));
            holder.createDate.setText(StringUtil.YMDDtoYMD(entity.getCreateDate()));
            List<String> list = StringUtil.StringSplit(entity.getServiceId());
            mImageAdapter = new ServerItemAdapter(list);
            holder.recycler_image.setAdapter(mImageAdapter);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout ll_content;
            LinearLayout ll_image;
            AppCompatTextView Name;
            AppCompatTextView Status;
            AppCompatTextView Code;
            AppCompatTextView Amount;
            AppCompatTextView createDate;
            RecyclerView recycler_image;

            public ViewHolder(View itemView) {
                super(itemView);
                ll_content = (LinearLayout) itemView.findViewById(R.id.ll_orders);
                ll_image = (LinearLayout) itemView.findViewById(R.id.ll_server_item);
                Name = (AppCompatTextView) itemView.findViewById(R.id.tv_orders_accountName);
                Status = (AppCompatTextView) itemView.findViewById(R.id.tv_orders_statusStr);
                Code = (AppCompatTextView) itemView.findViewById(R.id.tv_orders_code);
                Amount = (AppCompatTextView) itemView.findViewById(R.id.tv_orders_declarationAmount);
                createDate = (AppCompatTextView) itemView.findViewById(R.id.tv_orders_date);
                recycler_image = (RecyclerView) itemView.findViewById(R.id.recycler_image);
                recycler_image.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                recycler_image.setHasFixedSize(true);
                recycler_image.addItemDecoration(new VerticalDividerItemDecoration.Builder(mContext)
                        .colorResId(R.color.white).sizeResId(R.dimen.right_8).build());
//
                ll_content.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        //TODO 去订单的详情 这里做判断是，为了刷新数据的时候，数据源还没有取到时，发生的数组下标越界错误
                        if (getLayoutPosition() != -1) {
                            OrderListEntity order = list.get(getLayoutPosition());
                            OrderDetailsActivity_.intent(mContext).extra(AppDelegate.CUSTOMER_ID_COMPANY, order.getId()).start();
                        }
                    }
                });
            }
        }
    }

    //服务项小字
    public class ServerItemAdapter extends RecyclerView.Adapter<ServerItemAdapter.ViewHolder> {
        private List<String> strlist;

        public ServerItemAdapter(List<String> strlist) {
            this.strlist = strlist;
        }

        @Override
        public ServerItemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_orders_list_recycler, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            int  i=MetaDataDal.GetListPosition( strlist.get(position));
            String str = MetaDataDal.GetShortName(strlist.get(position));
            holder.order_server.setText(str);
            if( i + 1 >colorshort.length ){
                holder.order_server.setBackgroundColor(getColors(R.color.color_theme));
            }else{
                holder.order_server.setBackgroundColor( getResources().getColor(colorshort[i]));
            }
        }

        @Override
        public int getItemCount() {
            return  strlist !=null ? strlist.size() : 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            AppCompatTextView order_server;

            public ViewHolder(View itemView) {
                super(itemView);
                order_server = (AppCompatTextView) itemView.findViewById(R.id.tv_order_server);
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
