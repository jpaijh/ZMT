package com.example.ZMTCSD.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.entity.CompanyBankApplyEntity;
import com.example.ZMTCSD.fragment.DrawerCBankFragment_;
import com.example.ZMTCSD.utils.DeviceUtil;
import com.example.ZMTCSD.utils.StringUtil;
import com.example.ZMTCSD.utils.VolleyErrorHelper;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 中信保的银行 列表
 */
@EActivity(R.layout.my_drawer_all_list)
public class CompanyBankActivity extends BaseActivity {
    private static final String TAG = "CompanyBankActivity";
    private Context mContext;
    private RequestQueue mRequestQueue;
    private final int BankType = 1; //查看批复时的type
    private LinearLayoutManager mLinearLayoutManager;
    private int lastVisibleItem;
    private int firstVisibleItem;
    private int totalPage;   //数据有多少页数
    private int pageIndex = 1;   //第一页
    private boolean isLoading;
    private boolean isFirstLoading; //是否为第一次加载
    private List<CompanyBankApplyEntity> rowsEntity;
    private BankApplyAdapter mAdapter;
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

    @ViewById(R.id.progressbar)
    CircleProgressBar mProgressbar;

    @ViewById(R.id.drawer_dr)
    DrawerLayout mDrawerLayout;

    @ViewById(R.id.fragment)
    RelativeLayout mFragment;

    private String ARGUMENTS = "";//搜索返回来的数据

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
        mContext = CompanyBankActivity.this;
        mRequestQueue = Volley.newRequestQueue(mContext);
        initToolbar();
        initDrawerLayout();
        initBroadcastReceiver();
        initRecyclerView();
        initSwipeRefreshLayout();
        initFragment();
        super.onAfterViews();
    }

    private void initFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new DrawerCBankFragment_()).commit();
    }

    /**
     * 内部的广播
     */
    private void initBroadcastReceiver() {
        // 注册广播接收者
        mBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppDelegate.COMPANY_BANk_DRAWER_SCREEN); //筛选的广播
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() == AppDelegate.COMPANY_BANk_DRAWER_SCREEN) {
                    mDrawerLayout.closeDrawer(GravityCompat.END);
                    ARGUMENTS = intent.getStringExtra(AppDelegate.COMPANY_BANk_LIST_SCREEN);
                    LogUtils.d("银行广播" + ARGUMENTS);
                    refreshList();
                }
            }
        };
        mBroadcastManager.registerReceiver(mReceiver, intentFilter);
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .colorResId(R.color.color_theme_bj).showLastDivider()
                .sizeResId(R.dimen.left_16).build());
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (mAdapter != null && lastVisibleItem + 1 == mAdapter.getItemCount()
                        && !isLoading && newState == RecyclerView.SCROLL_STATE_IDLE && firstVisibleItem != 0) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    isLoading = true;

                    if (pageIndex < totalPage) {
                        createLoadingSnackbar(mRecyclerView);
                        pageIndex = pageIndex + 1;
                        onBackgrounds();
                    } else {
                        mSwipeRefreshLayout.setRefreshing(false);
                        isLoading = false;
                        createNoDateSnackbar(mRecyclerView);
                    }
                    super.onScrollStateChanged(recyclerView, newState);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //获取滑动时最后显示出来的view是什么位置。
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
                firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshList();
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_progress_1, R.color.color_progress_2);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                isFirstLoading = false;
                onBackgrounds();
                createRefreshSnackbar(mRecyclerView, TAG);
            }
        });
    }

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
        final String url = MoreUserDal.GetServerUrl() + "/api/creditguarantee/CompanyBankApplys?PageSize=20&PageIndex=" + pageIndex + ARGUMENTS;
        LogUtils.d("中信保的银行" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                JSONObject jsonObject = JSON.parseObject(response.toString());
                int total = jsonObject.getIntValue("total");
                totalPage = jsonObject.getIntValue("totalPage");

                if (total == 0) {
                    hideView(mRecyclerView);
                    showView(tv_no_data);
                } else {
                    hideView(tv_no_data);
                    showView(mRecyclerView);
                }

                if (total != 0 && pageIndex == 1) {
                    JSONArray rows = jsonObject.getJSONArray("rows");
                    rowsEntity = JSON.parseArray(rows.toString(), CompanyBankApplyEntity.class);
                    mAdapter = new BankApplyAdapter(rowsEntity);
                    mRecyclerView.setAdapter(mAdapter);
                    if (!isFirstLoading)
                        createRefreshCompleteSnackbar(mRecyclerView);
                } else if (pageIndex <= totalPage) {
                    JSONArray rows = jsonObject.getJSONArray("rows");
                    rowsEntity.addAll(lastVisibleItem + 1, JSON.parseArray(rows.toString(), CompanyBankApplyEntity.class));
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
                LogUtils.d("查询失败" + VolleyErrorHelper.getMessage(error, mContext));
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
        super.onBackgrounds();
    }


    public class BankApplyAdapter extends RecyclerView.Adapter<BankApplyAdapter.ViewHolder> {
        List<CompanyBankApplyEntity> list;

        public BankApplyAdapter(List<CompanyBankApplyEntity> list) {
            this.list = list;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            final LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            return new ViewHolder(MaterialRippleLayout.on(inflater.inflate(R.layout.item_company_code, viewGroup, false))
                    .rippleOverlay(true)//如果这是真的,涟漪在前景;错:背景
                    .rippleAlpha(0.1f)//α的涟漪
                    .rippleColor(getColors(R.color.color_ripple_btn))//涟漪的颜色
                    .rippleDelayClick(true)
                    .rippleHover(true)//如果这是真的,悬停效果时画的观点是感动
                    .create());
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            CompanyBankApplyEntity entity = list.get(position);
            holder.name.setText(StringUtil.StringToNull(entity.getAccountName()));
            if (TextUtils.isEmpty(entity.getCgSwiftCode())) {
                holder.CGSwift.setText(getStrings(R.string.company_bank_one));
                holder.CGSwift.setTextColor(getColors(R.color.red));
            } else {
                holder.CGSwift.setText(entity.getCgSwiftCode());
                holder.CGSwift.setTextColor(getColors(R.color.list_status));
            }
            holder.country.setText(getString(R.string.Company_bank_three));
            holder.accountName.setText(entity.getCompanyName());
            holder.bankName.setText(entity.getBankName());
            holder.bankAccount.setText(entity.getBankAccount());
            holder.swiftCode.setText(entity.getSwiftCode());

            if (entity.getIsUploaded()) {
                holder.date.setText(getStrings(R.string.company_bank_five) + StringUtil.YMDDtoYMD(entity.getUploadedTime()));
                holder.date.setTextColor(getColors(R.color.list_status));
            } else {
                holder.date.setText(getStrings(R.string.company_code_four));
                holder.date.setTextColor(getColors(R.color.red));
            }

        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public LinearLayout ll_content, ll_one, ll_two, ll_three;
            public AppCompatTextView name; //名称
            public AppCompatTextView CGSwift;
            public AppCompatTextView country; //往来单位
            public AppCompatTextView accountName;
            public AppCompatTextView date;
            public AppCompatTextView bankName;
            public AppCompatTextView bankAccount;
            public AppCompatTextView swiftCode;
            public View view;

            public ViewHolder(View itemView) {
                super(itemView);
                ll_content = (LinearLayout) itemView.findViewById(R.id.ll_content);
                name = (AppCompatTextView) itemView.findViewById(R.id.tv_buyerCode_Name);
                CGSwift = (AppCompatTextView) itemView.findViewById(R.id.tv_buyerCode_buyerCode);
                country = (AppCompatTextView) itemView.findViewById(R.id.tv_buyerCode_country);
                accountName = (AppCompatTextView) itemView.findViewById(R.id.tv_buyerCode_companyId);
                date = (AppCompatTextView) itemView.findViewById(R.id.tv_buyerCode_uploadedTime);
                ll_one = (LinearLayout) itemView.findViewById(R.id.ll_company_one);
                ll_two = (LinearLayout) itemView.findViewById(R.id.ll_company_two);
                ll_three = (LinearLayout) itemView.findViewById(R.id.ll_company_three);
                bankName = (AppCompatTextView) itemView.findViewById(R.id.tv_buyerCode_bankName);
                bankAccount = (AppCompatTextView) itemView.findViewById(R.id.tv_buyerCode_bankAccount);
                swiftCode = (AppCompatTextView) itemView.findViewById(R.id.tv_buyerCode_swiftCode);
                view = itemView.findViewById(R.id.view);
                showView(ll_one);
                showView(ll_two);
                showView(ll_three);
                showView(view);

                ll_content.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        //TODO 去中信保银行的详情
                        if (getLayoutPosition() != -1) {
                            CompanyBankApplyEntity entity = list.get(getLayoutPosition());
                            CompanyReplysActivity_.intent(mContext).extra(AppDelegate.REPLYSKEYWORD, entity.getApplyKeyword())
                                    .extra(AppDelegate.TYPE, BankType).start();
                        }
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
    protected void onDestroy() {
        mRequestQueue.cancelAll(this);
        super.onDestroy();
    }
}
