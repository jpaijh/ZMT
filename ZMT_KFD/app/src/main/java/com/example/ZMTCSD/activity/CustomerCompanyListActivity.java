package com.example.ZMTCSD.activity;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.entity.CompanyListEntity;
import com.example.ZMTCSD.utils.StringUtil;
import com.example.ZMTCSD.utils.VolleyErrorHelper;
import com.example.ZMTCSD.AppDelegate;
import com.thinkcool.circletextimageview.CircleTextImageView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账户中心-账户的详细信息-往来单位信息
 */
@EActivity(R.layout.my_activity_all_list)
public class CustomerCompanyListActivity extends BaseActivity {
    private static final String TAG = "CustomerCompanyListActivity";
    private Context mContext;
    private RequestQueue mRequestQueue;
    private final String CompanyType = "CompanyDetails";//往来单位的
    private LinearLayoutManager mLinearLayoutManager;
    private CompanyListAdapter mAdapter;
    private int lastVisibleItem;
    private int firstVisibleItem;
    private boolean isLoading;
    private boolean isFirstLoading; //是否为第一次加载
    private int totalPage;   //数据有多少页数
    private int pageIndex = 1;   //第一页
    private List<CompanyListEntity> rowsEntity;
    private int accountId;

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

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_title.setText(getStrings(R.string.toolbar_CustomerCompany));
    }

    @Override
    public void onAfterViews() {
        mContext = CustomerCompanyListActivity.this;
        initToolbar();
        mRequestQueue = Volley.newRequestQueue(mContext);
        accountId = getIntent().getIntExtra(AppDelegate.CUSTOMER_ID_COMPANY, 0);
        initRecyclerView();
        initSwipeRefreshLayout();
    }

    private void initRecyclerView() {
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .colorResId(R.color.color_theme_bj).sizeResId(R.dimen.left_16).showLastDivider().build());
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
                mSwipeRefreshLayout.setRefreshing(true);
                isLoading = true;//是否在刷新
                saveCurrentTime(TAG);//保存当前时间
                pageIndex = 1;
                isFirstLoading = true;//是否第一次加载
                onBackgrounds();
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_progress_1, R.color.color_progress_2);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtils.d("进入下拉刷新");
                pageIndex = 1;
                isFirstLoading = false;
                onBackgrounds();
                createRefreshSnackbar(mRecyclerView, TAG);
            }
        });
    }

    @Override
    public void onBackgrounds() {
        int second = (int) (System.currentTimeMillis() / 1000);
        if (isRefreshWin(second)) {
            DoCompanyList();
        }
    }

    void DoCompanyList() {
        final String url = MoreUserDal.GetServerUrl() + "/api/platformAccount/companies?accountId=" + accountId + "&pageIndex=" + pageIndex;
        LogUtils.d("往来单位列表" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                JSONObject jsonObject = JSON.parseObject(response.toString());
                int total = jsonObject.getIntValue("total");
                totalPage = jsonObject.getIntValue("totalPage");

                if (total == 0) {
                    tv_no_data.setVisibility(View.VISIBLE);
                } else {
                    tv_no_data.setVisibility(View.GONE);
                }

                if (total != 0 && pageIndex == 1) {
                    JSONArray rows = jsonObject.getJSONArray("rows");
                    rowsEntity = JSON.parseArray(rows.toString(), CompanyListEntity.class);
                    mAdapter = new CompanyListAdapter(rowsEntity);
                    mRecyclerView.setAdapter(mAdapter);
                    if (!isFirstLoading)
                        createRefreshCompleteSnackbar(mRecyclerView);
                } else if (pageIndex <= totalPage) {
                    JSONArray rows = jsonObject.getJSONArray("rows");
                    rowsEntity.addAll(lastVisibleItem + 1, JSON.parseArray(rows.toString(), CompanyListEntity.class));
                    mAdapter.notifyDataSetChanged();
                    createLoadingCompleteSnackbar(mRecyclerView);
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
        jsonObjectRequest.setTag(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 3, 1.0f));
        mRequestQueue.add(jsonObjectRequest);

    }

    private class CompanyListAdapter extends RecyclerView.Adapter<CompanyListAdapter.ViewHolder> {

        private List<CompanyListEntity> list;

        public CompanyListAdapter(List<CompanyListEntity> list) {
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
            CompanyListEntity entity = list.get(position);
            holder.name.setText(StringUtil.StringToNull(entity.getName()));
            holder.status.setText(StringUtil.StringToNull(entity.getStatusStr()));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public RelativeLayout AccountItem;
            public CircleTextImageView mCircleImg;
            public AppCompatTextView name;
            public AppCompatTextView status;
            public AppCompatImageView TickImg;

            public ViewHolder(View itemView) {
                super(itemView);
                AccountItem = (RelativeLayout) itemView.findViewById(R.id.ll_content);
                mCircleImg = (CircleTextImageView) itemView.findViewById(R.id.img_circle);
                name = (AppCompatTextView) itemView.findViewById(R.id.tv_name);
                status = (AppCompatTextView) itemView.findViewById(R.id.tv_value);
                TickImg = (AppCompatImageView) itemView.findViewById(R.id.img_tick);
                AccountItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getLayoutPosition() != -1) {
                            CompanyListEntity entity = list.get(getLayoutPosition());
                            CustomerCompanyDetailsActivity_.intent(mContext).extra(AppDelegate.TYPE, CompanyType)
                                    .extra(AppDelegate.CUSTOMER_ID_COMPANY, entity.getId()).start();
                        }
                    }
                });
            }
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
        super.onDestroy();
    }
}
