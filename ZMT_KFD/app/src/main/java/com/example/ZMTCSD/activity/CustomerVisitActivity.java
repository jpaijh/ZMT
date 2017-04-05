package com.example.ZMTCSD.activity;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.entity.VisitRecordsEntity;
import com.example.ZMTCSD.fragment.AddVisitPopup;
import com.example.ZMTCSD.utils.DeviceUtil;
import com.example.ZMTCSD.utils.StringUtil;
import com.example.ZMTCSD.utils.ToastUtil;
import com.example.ZMTCSD.utils.VolleyErrorHelper;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.ZMTCSD.R.dimen.right_0;

/**
 * 客户的来来访记录界面
 */
@EActivity(R.layout.my_activity_all_list)
public class CustomerVisitActivity extends BaseActivity {
    private static final String TAG = "CustomerVisitActivity";
    private RequestQueue mRequestQueue;
    private Context mContext;
    private int accountId;
    private String accountName;
    private int lastVisibleItem;
    private int firstVisibleItem;
    private boolean isLoading;
    private boolean isFirstLoading; //是否为第一次加载
    private int totalPage;   //数据有多少页数
    private int pageIndex = 1;   //第一页
    private LinearLayoutManager mLinearLayoutManager;
    private VisitRecordsAdapter mAdapter;
    private List<VisitRecordsEntity> rowsEntity = new ArrayList<>();

    AddVisitPopup mAddVisitPopup;
    private KProgressHUD uploadhud;
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

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_title.setText(getStrings(R.string.toolbar_CustomerVisit));
    }

    @Override
    public void onAfterViews() {
        this.mContext = CustomerVisitActivity.this;
        showView(mProgressbar);
        mRequestQueue = Volley.newRequestQueue(mContext);
        accountId = getIntent().getIntExtra(AppDelegate.CUSTOMER_ID_COMPANY, 0);
        accountName = getIntent().getStringExtra(AppDelegate.CUSTOMER_ATTACH_NAME);
        mAdapter = new VisitRecordsAdapter(rowsEntity);
        mRecyclerView.setAdapter(mAdapter);
        initToolbar();
        initRecyclerView();
        initSwipeRefreshLayout();

        uploadhud = KProgressHUD.create(mContext).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("添加访问记录").setDetailsLabel("请等待....").setCancellable(false);
    }

    /**
     * 上拉加载
     */
    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.line_color).marginResId(right_0, right_0) //记录左右的距离left_61
                .showLastDivider()  //最后的item有下划线。
                .build());
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
                saveCurrentTime(TAG);//保存当前时间
                pageIndex = 1;
                isFirstLoading = true;//是否第一次加载
                onBackgrounds();
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_progress_1, R.color.color_progress_2);
        //当下拉加载时产生
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                isFirstLoading = false;
                onBackgrounds();
                createRefreshSnackbar(mRecyclerView, TAG);  //创建 正在刷新...的snackbar
            }
        });
    }

    @Override
    public void onBackgrounds() {
        int second = (int) (System.currentTimeMillis() / 1000);
        if (isRefreshWin(second)) {
            DoCustomerVisit();
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            isLoading = false;
            tv_no_data.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.ic_bug3), null, null);
            tv_no_data.setText("刷新token不成功");
            showView(tv_no_data);
        }
    }

    void DoCustomerVisit() {
        final String url = MoreUserDal.GetServerUrl() + "/api/platformaccount/VisitRecords?PageSize=20&AccountId=" + accountId + "&PageIndex=" + pageIndex;
        LogUtils.d("来访记录" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                hideView(mProgressbar);
                JSONObject jsonObject = JSON.parseObject(response.toString());
                int total = jsonObject.getIntValue("total");
                totalPage = jsonObject.getIntValue("totalPage");

                if (total == 0) {
                    showView(tv_no_data);
                } else {
                    hideView(tv_no_data);
                }
                if (total != 0 && pageIndex == 1) {
                    JSONArray rows = jsonObject.getJSONArray("rows");
                    rowsEntity.addAll(JSON.parseArray(rows.toString(), VisitRecordsEntity.class));
                    //TODO 如果开始没有数据，那么加载数据的时候会报错
                    mAdapter.notifyDataSetChanged();
                    if (!isFirstLoading)
                        createRefreshCompleteSnackbar(mRecyclerView);
                } else if (pageIndex <= totalPage) {
                    JSONArray rows = jsonObject.getJSONArray("rows");
                    rowsEntity.addAll(lastVisibleItem + 1, JSON.parseArray(rows.toString(), VisitRecordsEntity.class));
                    mAdapter.notifyDataSetChanged();
                    createLoadingCompleteSnackbar(mRecyclerView);
                }

                mSwipeRefreshLayout.setRefreshing(false);
                isLoading = false;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideView(mProgressbar);
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

    public class VisitRecordsAdapter extends RecyclerView.Adapter<VisitRecordsAdapter.ViewHolder> {
        private List<VisitRecordsEntity> list;

        public VisitRecordsAdapter(List<VisitRecordsEntity> list) {
            if (list != null) {
                this.list = list;
            } else {
                this.list = new ArrayList<>();
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_customer_visit_list, viewGroup, false);
            return new ViewHolder(view);
        }

        void addResult(List<VisitRecordsEntity> s) {
            list.clear();
            list.addAll(s);
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            VisitRecordsEntity entity = list.get(position);
            holder.tv_name.setText(entity.getUserName());
            holder.tv_date.setText(StringUtil.dateRemoveT(entity.getVisitDate()));
            holder.tv_content.setText(entity.getContent());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            AppCompatTextView tv_name;
            AppCompatTextView tv_date;
            AppCompatTextView tv_content;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_date = (AppCompatTextView) itemView.findViewById(R.id.tv_visit_date);
                tv_name = (AppCompatTextView) itemView.findViewById(R.id.tv_visit_name);
                tv_content = (AppCompatTextView) itemView.findViewById(R.id.tv_visit_content);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_add:
                mAddVisitPopup = new AddVisitPopup(mContext);
                mAddVisitPopup.showAtLocation(CustomerVisitActivity.this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                mAddVisitPopup.setmListener(new AddVisitPopup.AddVisitClickListener() {
                    @Override
                    public void OnAddVisitClickListener(View v, String str) {
                        uploadhud.show();
                        AddVisitRecord(str);
                    }
                });
                DeviceUtil.showSoft(mContext);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void AddVisitRecord(String name) {
        VisitRecordsEntity Visit = new VisitRecordsEntity();
        Visit.setAccountId(accountId);
        Visit.setAccountName(accountName);
        Visit.setContent(name);
        final String json = JSONObject.toJSONString(Visit);
        final String url = MoreUserDal.GetServerUrl() + "/api/platformaccount/AddVisitRecord";
//        LogUtils.d(json + "增加来访" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                uploadhud.dismiss();
                hideView(tv_no_data);
                JSONObject jsonObject = JSON.parseObject(response.toString());
                pageIndex = 1;
                totalPage = jsonObject.getIntValue("totalPage");
                JSONArray rows = jsonObject.getJSONArray("rows");
                List<VisitRecordsEntity> rowsEntity1 = JSON.parseArray(rows.toString(), VisitRecordsEntity.class);
                mAdapter.addResult(rowsEntity1);
                mRecyclerView.scrollToPosition(0);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d("增加来访查询失败" + VolleyErrorHelper.getMessage(error, mContext));
                uploadhud.dismiss();
                ToastUtil.showToast(mContext, "增加来访记录失败");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "bearer   " + MoreUserDal.GetAccessToken());
                map.put("Content-Type", "application/json");
                return map;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() {
                return json.getBytes();
            }
        };
        jsonObjectRequest.setTag(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 1, 1.0f));
        mRequestQueue.add(jsonObjectRequest);
    }

    @Override
    protected void onDestroy() {
        mRequestQueue.cancelAll(this);
        finish();
        super.onDestroy();
    }
}
