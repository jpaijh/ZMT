//package com.example.ZMTCSD.activity;
//
//import android.content.Context;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.AppCompatTextView;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.android.volley.AuthFailureError;
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//import com.apkfuns.logutils.LogUtils;
//import com.balysv.materialripple.MaterialRippleLayout;
//import com.example.ZMTCSD.AppDelegate;
//import com.example.ZMTCSD.NoDoubleClickListener;
//import com.example.ZMTCSD.R;
//import com.example.ZMTCSD.dal.MoreUserDal;
//import com.example.ZMTCSD.entity.MessageEntity;
//import com.example.ZMTCSD.utils.VolleyErrorHelper;
//import com.example.ZMTCSD.utils.StringUtil;
//import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
//import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
//
//import org.androidannotations.annotations.EActivity;
//import org.androidannotations.annotations.ViewById;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
////信息列表界面
//@EActivity(R.layout.my_activity_all_list)
//public class MessageActivity extends BaseActivity {
//    private static final String TAG = "MessageActivity";
//    private Context mContext;
//    private RequestQueue mRequestQueue;
//    private LinearLayoutManager mLinearLayoutManager;
//    private MessageAdapter mAdapter;
//    private int lastVisibleItem;
//    private int firstVisibleItem;
//    private boolean isLoading;
//    private boolean isFirstLoading; //是否为第一次加载
//    private int totalPage;   //数据有多少页数
//    private int pageIndex = 1;   //第一页
//    private List<MessageEntity> rowsEntity;
//
//    @ViewById(R.id.toolbar)
//    Toolbar mToolbar;
//
//    @ViewById(R.id.tv_title)
//    AppCompatTextView tv_title;
//
//    @ViewById(R.id.swipe_refresh_layout)
//    SwipeRefreshLayout mSwipeRefreshLayout;
//
//    @ViewById(R.id.recyclerView)
//    RecyclerView mRecyclerView;
//
//    @ViewById(R.id.tv_no_data)
//    AppCompatTextView tv_no_data;
//
//    @ViewById(R.id.progressbar)
//    CircleProgressBar mProgressbar;
//
//    private void initToolbar() {
//        mToolbar.setTitle("");
//        setSupportActionBar(mToolbar);
//        tv_title.setText(getIntent().getStringExtra(AppDelegate.TOOLBAR_NAME));
//    }
//
//    @Override
//    public void onAfterViews() {
//        mContext = MessageActivity.this;
//        initToolbar();
//        mRequestQueue = Volley.newRequestQueue(mContext);
//        initRecyclerView();
//        initSwipeRefreshLayout();
//    }
//
//    private void initRecyclerView() {
//        mRecyclerView.setHasFixedSize(true);
//        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
//        mRecyclerView.setLayoutManager(mLinearLayoutManager);
//        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
//                .colorResId(R.color.color_theme_bj).showLastDivider()
//                .sizeResId(R.dimen.left_16).build());
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                if (mAdapter != null && lastVisibleItem + 1 == mAdapter.getItemCount()
//                        && !isLoading && newState == RecyclerView.SCROLL_STATE_IDLE && firstVisibleItem != 0) {
//                    mSwipeRefreshLayout.setRefreshing(true);
//                    isLoading = true;
//
//                    if (pageIndex < totalPage) {
//                        createLoadingSnackbar(mRecyclerView);
//                        pageIndex = pageIndex + 1;
//                        onBackgrounds();
//                    } else {
//                        mSwipeRefreshLayout.setRefreshing(false);
//                        isLoading = false;
//                        createNoDateSnackbar(mRecyclerView);
//                    }
//                    super.onScrollStateChanged(recyclerView, newState);
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                //获取滑动时最后显示出来的view是什么位置。
//                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
//                firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
//                super.onScrolled(recyclerView, dx, dy);
//            }
//
//        });
//
//    }
//
//    private void initSwipeRefreshLayout() {
//        mSwipeRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                mSwipeRefreshLayout.setRefreshing(true);
//                isLoading = true;//是否在刷新
//                saveCurrentTime(TAG);//保存当前时间
//                pageIndex = 1;
//                isFirstLoading = true;//是否第一次加载
//
//                onBackgrounds();
//            }
//        });
//
//        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_progress_1, R.color.color_progress_2);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                LogUtils.d("进入下拉刷新");
//                pageIndex = 1;
//                isFirstLoading = false;
//                onBackgrounds();
//                createRefreshSnackbar(mRecyclerView, TAG);
//            }
//        });
//    }
//
//    @Override
//    public void onBackgrounds() {
//        int second = (int) (System.currentTimeMillis() / 1000);
//        long Unsecond = MoreUserDal.GetSecond() + 20;
//        if (isRefreshWin(second)) {
//            MessageList();
//        } else {
//
//        }
//    }
//
//    void MessageList() {
//        final String url = MoreUserDal.GetServerUrl() + "/api/Message/Messages?pageSize=20&pageIndex=" + pageIndex;
//        LogUtils.d("信息列表数据" + url);
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<org.json.JSONObject>() {
//            @Override
//            public void onResponse(org.json.JSONObject response) {
//                JSONObject jsonObject = JSON.parseObject(response.toString());
//                int total = jsonObject.getIntValue("total");
//                totalPage = jsonObject.getIntValue("totalPage");
//
//                if (total == 0) {
//                    showView(tv_no_data);
//                } else {
//                    hideView(tv_no_data);
//                }
//
//                if (total != 0 && pageIndex == 1) {
//                    JSONArray rows = jsonObject.getJSONArray("rows");
//                    rowsEntity = JSON.parseArray(rows.toString(), MessageEntity.class);
//                    mAdapter = new MessageAdapter(rowsEntity);
//                    mRecyclerView.setAdapter(mAdapter);
//                    if (!isFirstLoading)
//                        createRefreshCompleteSnackbar(mRecyclerView);
//                } else if (pageIndex <= totalPage) {
//                    JSONArray rows = jsonObject.getJSONArray("rows");
//                    rowsEntity.addAll(lastVisibleItem + 1, JSON.parseArray(rows.toString(), MessageEntity.class));
//                    mAdapter.notifyDataSetChanged();
//                    createLoadingCompleteSnackbar(mRecyclerView);
//                }
//
//                mSwipeRefreshLayout.setRefreshing(false);
//                isLoading = false;
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                mSwipeRefreshLayout.setRefreshing(false);
//                isLoading = false;
//                LogUtils.d("查询失败" + VolleyErrorHelper.getMessage(error, mContext));
//                hideView(mRecyclerView);
//                tv_no_data.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.ic_bug3), null, null);
//                tv_no_data.setText(VolleyErrorHelper.getMessage(error, mContext));
//                showView(tv_no_data);
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> map = new HashMap<>();
//                map.put("Authorization", "bearer   " + MoreUserDal.GetAccessToken());
//                return map;
//            }
//        };
//        jsonObjectRequest.setTag(this);
//        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 3, 1.0f));
//        mRequestQueue.add(jsonObjectRequest);
//    }
//
//    private class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
//        private List<MessageEntity> list;
//
//        public MessageAdapter(List<MessageEntity> list) {
//            this.list = list;
//        }
//
//        @Override
//        public int getItemCount() {
//            return list.size();
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//            final LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
//            return new ViewHolder(MaterialRippleLayout.on(inflater.inflate(R.layout.item_message_list, viewGroup, false))
//                    .rippleOverlay(true)//如果这是真的,涟漪在前景;错:背景
//                    .rippleAlpha(0.1f)//α的涟漪
//                    .rippleColor(getColors(R.color.color_ripple_btn))//涟漪的颜色
//                    .rippleDelayClick(true)
//                    .rippleHover(true)//如果这是真的,悬停效果时画的观点是感动
//                    .create());
//        }
//
//        @Override
//        public void onBindViewHolder(final ViewHolder viewHolder, int position) {
//            MessageEntity entity = list.get(position);
//            viewHolder.tv_content.setText(entity.getTitle());
//            viewHolder.tv_title.setText(entity.getMessageTypeName());
//            viewHolder.tv_sendDate.setText(StringUtil.dateRemoveT(entity.getSendDate()));
//            viewHolder.tv_creatorName.setText(entity.getCreatorName());
//        }
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//            RelativeLayout rl_content;
//            TextView tv_title; // 标题
//            TextView tv_content; // 内容
//            TextView tv_sendDate; // 发送时间
//            TextView tv_creatorName; // 发件人
//            ImageView img_message; // 发件人
//
//            public ViewHolder(View itemView) {
//                super(itemView);
//                tv_title = (TextView) itemView.findViewById(R.id.tv_title_message);
//                tv_content = (TextView) itemView.findViewById(R.id.tv_content_message);
//                tv_sendDate = (TextView) itemView.findViewById(R.id.tv_sendDate_message);
//                tv_creatorName = (TextView) itemView.findViewById(R.id.tv_creatorName_message);
//                img_message = (ImageView) itemView.findViewById(R.id.img_message);
//                rl_content = (RelativeLayout) itemView.findViewById(R.id.rl_content_message);
//                rl_content.setOnClickListener(new NoDoubleClickListener() {
//                    @Override
//                    public void onNoDoubleClick(View v) {
//                        //TODO 没有阅读过的属性，不能实现
//                        if (getLayoutPosition() != -1) {
//                            MessageEntity rowsEntity = list.get(getLayoutPosition());
//                            MessageDetailsActivity_.intent(mContext).extra(AppDelegate.CUSTOMER_ID_COMPANY, rowsEntity.getId()).start();
//                        }
//                    }
//                });
//            }
//        }
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onDestroy() {
//        mRequestQueue.cancelAll(this);
//        super.onDestroy();
//    }
//
//}
