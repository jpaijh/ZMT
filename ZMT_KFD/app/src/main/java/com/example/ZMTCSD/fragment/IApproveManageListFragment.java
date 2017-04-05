package com.example.ZMTCSD.fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
import com.example.ZMTCSD.activity.IApprovalListDetailsActivity_;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.utils.VolleyErrorHelper;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.NoDoubleClickListener;
import com.example.ZMTCSD.entity.ApproveManageListEntity;
import com.example.ZMTCSD.utils.StringUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.grantland.widget.AutofitTextView;


@EFragment(R.layout.my_segment_viewpager)
public class IApproveManageListFragment extends BaseFragment {
    private static final String TAG = "IApproveManageListFragment";
    private Context mContent;
    private String IsClaimed; //是分辨那个分页上的搜索
    private String UserId;
    private String APPROVAL_URL;
    private boolean APPROVAL_TYPE; //用来在审批详情中分辨是我发起的 还是我审批的
    private String Paging;
    private int lastVisibleItem; //获取滑动时最后显示出来的view是什么位置。
    private  int firstVisibleItem;
    private int totalPage;   //数据有多少页数
    private int pageIndex = 1;   //第一页
    private boolean isLoading;
    private boolean isFirstLoading; //是否为第一次加载
    private LinearLayoutManager mLinearLayoutManager;
    private RequestQueue mRequestQueue;
    private LocalBroadcastManager mBroadcastManager;
    private BroadcastReceiver mReceiver;
    private List<ApproveManageListEntity> rowsEntity; //每个记录的集合
    private MyAdapter mAdapter;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;


    @ViewById(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @ViewById(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @ViewById(R.id.tv_no_data)
    AppCompatTextView tv_no_data;
    DrawerLayout mDrawerLayout;
    private String ARGUMENTS = ""; //接受传来的数据
    @Override
    public void onAfterViews() {
        this.mContent=getActivity();
        mDrawerLayout= (DrawerLayout) getActivity().findViewById(R.id.drawer_dr);
        APPROVAL_URL = getArguments().getString(AppDelegate.APPROVAL_BASE_URL);
        APPROVAL_TYPE=getArguments().getBoolean(AppDelegate.APPROVAL_LIST_TYPE);
        Paging=getArguments().getString(AppDelegate.DRAWER_APPROVAL_TYPE);
        mRequestQueue = Volley.newRequestQueue(mContent);
        UserId = MoreUserDal.GetUserClaimsID();
        initBroadcastReceiver();
        initRecyclerView();
        initSwipeRefreshLayout();
    }

    /**
     * 内部的广播
     */
    private void initBroadcastReceiver() {
        // 注册广播接收者
        mBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppDelegate.APPROCVAL_DRAWER_SCREEN); //关键字的广播
        intentFilter.addAction(AppDelegate.ACTION_REFRESH_LIST_DATA); //刷新列表的广播

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() == AppDelegate.APPROCVAL_DRAWER_SCREEN) {
                    IsClaimed=intent.getStringExtra(AppDelegate.DRAWER_APPROVAL_TYPE);
                    ARGUMENTS=intent.getStringExtra(AppDelegate.APPROCVAL_LIST_SCREEN);
                    //TODO 传来数据
                    mDrawerLayout.closeDrawer(GravityCompat.END);
                    if(Paging.equalsIgnoreCase(IsClaimed)){
                        refreshList();
                    }
                } else if (intent.getAction() == AppDelegate.ACTION_REFRESH_LIST_DATA) {
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
        mLinearLayoutManager = new LinearLayoutManager(mContent,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContent)
                .colorResId(R.color.color_theme_bj)
                .sizeResId(R.dimen.left_16).showLastDivider().build());
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if ((mAdapter != null && lastVisibleItem + 1 == mAdapter.getItemCount())
                        && newState == RecyclerView.SCROLL_STATE_IDLE && !isLoading && firstVisibleItem !=0) {
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
                firstVisibleItem=mLinearLayoutManager.findFirstVisibleItemPosition();
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
                mSwipeRefreshLayout.setRefreshing(true);
                isLoading = true;
                saveCurrentTime(TAG);
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
        if (isRefreshWin(second)){
            ApprovalList();
        }
    }

    void ApprovalList() {
//        final String url = AppDelegate.BASE_URL + "/api/Approval/Approval/Lists?Type=1&PageSize=10&Status=" + STATUS + "&PageIndex=" + pageIndex+ARGUMENTS;
        String url=APPROVAL_URL+ "&PageIndex=" + pageIndex+ARGUMENTS;
        LogUtils.d("列表数据"+url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                JSONObject jsonObject = JSON.parseObject(response.toString());
                int total = jsonObject.getIntValue("total");  //数据有多少条
                totalPage = jsonObject.getIntValue("totalPage");  //数据有多少页

                if (total == 0) {
                    showView(tv_no_data);
                    hideView(mRecyclerView);
                } else {
                    hideView(tv_no_data);
                    showView(mRecyclerView);
                }

                if (total != 0 && pageIndex == 1) {
                    JSONArray rows = jsonObject.getJSONArray("rows");
                    rowsEntity = JSON.parseArray(rows.toString(), ApproveManageListEntity.class);
                    mAdapter = new MyAdapter(rowsEntity);
                    mRecyclerView.setAdapter(mAdapter);
                    if (!isFirstLoading)
                        createRefreshCompleteSnackbar(mRecyclerView); //创建刷新成功的snckbar
                } else if (pageIndex <= totalPage) {
                    JSONArray rows = jsonObject.getJSONArray("rows");
                    rowsEntity.addAll(lastVisibleItem + 1, JSON.parseArray(rows.toString(), ApproveManageListEntity.class));
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
                LogUtils.d("查询失败"+ VolleyErrorHelper.getMessage(error,mContent));
                tv_no_data.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.mipmap.ic_bug3),null,null);
                tv_no_data.setText(VolleyErrorHelper.getMessage(error,mContent));
                showView(tv_no_data);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization","bearer   "+MoreUserDal.GetAccessToken());
               return map;
            }
        };
        jsonObjectRequest.setTag(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, 1.0f));
        mRequestQueue.add(jsonObjectRequest);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<ApproveManageListEntity> list;

        public MyAdapter(List<ApproveManageListEntity> list) {
            this.list = list;
        }
        @Override
        public int getItemCount() {
            return list.size();
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            final LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            return new ViewHolder(
                    MaterialRippleLayout.on(inflater.inflate(R.layout.item_approve_manage_initiate, viewGroup, false))
                            .rippleOverlay(true)//如果这是真的,涟漪在前景;错:背景
                            .rippleAlpha(0.1f)//α的涟漪
                            .rippleColor(getColors(R.color.color_ripple_btn))//涟漪的颜色
                            .rippleDelayClick(true)
                            .rippleHover(true)//如果这是真的,悬停效果时画的观点是感动
                            .create());
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public AppCompatTextView tv_top_left, tv_top_right, tv_one_text, tv_one, tv_two_text, tv_two,
                    tv_three_text, tv_three;
            public LinearLayout ll_content ,ll_Bottom;
//            public AppCompatButton iApprove_btn;
            public RecyclerView iApprove_RecyclerView; //加载更多是适配器
            public AppCompatImageView iApprove_img;

            public ViewHolder(final View itemView) {
                super(itemView);
                ll_content= (LinearLayout) itemView.findViewById(R.id.ll_content);
                ll_content.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        ApproveManageListEntity manageListEntity = list.get(getLayoutPosition());
                        IApprovalListDetailsActivity_.intent(getActivity()).extra(AppDelegate.APPROVALID, manageListEntity.getApprovalId())
                         .extra(AppDelegate.FLAG, manageListEntity.getFlag()).extra(AppDelegate.APPROVAL_LIST_TYPE,APPROVAL_TYPE).start();
                    }
                });
                iApprove_img= (AppCompatImageView) itemView.findViewById(R.id.iApprove_img);
                ll_Bottom = (LinearLayout) itemView.findViewById(R.id.ll_Bottom);

                tv_top_left = (AppCompatTextView) itemView.findViewById(R.id.tv_top_left);
                tv_top_right = (AppCompatTextView) itemView.findViewById(R.id.tv_top_right);

                tv_one_text = (AppCompatTextView) itemView.findViewById(R.id.tv_one_text);
                tv_one = (AppCompatTextView) itemView.findViewById(R.id.tv_one);
                tv_two_text = (AppCompatTextView) itemView.findViewById(R.id.tv_two_text);
                tv_two = (AppCompatTextView) itemView.findViewById(R.id.tv_two);
                tv_three_text = (AppCompatTextView) itemView.findViewById(R.id.tv_three_text);
                tv_three = (AppCompatTextView) itemView.findViewById(R.id.tv_three);

                iApprove_RecyclerView = (RecyclerView) itemView.findViewById(R.id.iApprove_RecyclerView);
                iApprove_RecyclerView.setLayoutManager(new LinearLayoutManager(mContent));
                iApprove_RecyclerView.setHasFixedSize(true);
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final ApproveManageListEntity entity = list.get(position);
            switch( entity.getApprovalTypeId()){
                case "3178700":
                    holder.iApprove_img.setImageResource(R.mipmap.ic_iapprove_image_fk);
                    break;
                case "26600":
                    holder.iApprove_img.setImageResource(R.mipmap.ic_iapprove_image_xy);
                    break;
                case "0":
                    holder.iApprove_img.setImageResource(R.mipmap.ic_iapprove_image_kh);
                    break;
                default:
                    holder.iApprove_img.setImageResource(R.mipmap.ic_iapprove_image);
                    break;
            }

            holder.tv_top_left.setText(entity.getApprovalType());
            //TODO 当处于starus=0的时候并且 currentApproverId 等于用户的id的时候，按钮是可以显示的
            switch (entity.getStatus()) {
                case 0:
                    holder.tv_top_right.setText(getStrings(R.string.Initiate_item_InApproval));
                    holder.tv_top_right.setTextColor(getColors(R.color.details_InApproval));
                    break;
                case 1:
                    holder.tv_top_right.setText(getStrings(R.string.Initiate_item_Approval));
                    holder.tv_top_right.setTextColor(getColors(R.color.details_Approval));
                    break;
                case -1:
                    holder.tv_top_right.setText(getStrings(R.string.Initiate_item_UnApproval));
                    holder.tv_top_right.setTextColor(getColors(R.color.details_UnApproval));
                    break;
                case -2:
                    holder.tv_top_right.setText(getStrings(R.string.Initiate_item_RepealApproval));
                    holder.tv_top_right.setTextColor(getColors(R.color.details_UnApproval));
                    break;
                default:
                    break;
            }
            holder.tv_one_text.setText(getStrings(R.string.Approve_one));
            holder.tv_one.setText(entity.getKeyword());
            if(APPROVAL_TYPE){
                holder.tv_two_text.setText(getStrings(R.string.Approve_two));
                holder.tv_two.setText(entity.getReporterName());
            }else{
                holder.tv_two_text.setText(getStrings(R.string.Approve_two2));
                holder.tv_two.setText(entity.getCurrentApproverName());
            }
            holder.tv_three_text.setText(getStrings(R.string.Approve_three));
            holder.tv_three.setText(StringUtil.YMDDtoYMD(entity.getReporteDate()));

            //TODO 对propertyEntity 进行判断，但 为 null 和"[]"的时候 不显示
            if(entity.getPropertyInfos() !=null && entity.getPropertyInfos().size() !=0){
                //是否有property的数据
                showView(holder.ll_Bottom);
                List<ApproveManageListEntity.PropertyInfoList> PropertyList = entity.getPropertyInfos();
                myRecyclerViewAdapter=new MyRecyclerViewAdapter(PropertyList);
                holder.iApprove_RecyclerView.setAdapter(myRecyclerViewAdapter);
            }

        }
    }

    private class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.DefulteHolder>{
        private List<ApproveManageListEntity.PropertyInfoList> list;

        public MyRecyclerViewAdapter(List<ApproveManageListEntity.PropertyInfoList> list) {
            this.list = list;
        }

        @Override
        public MyRecyclerViewAdapter.DefulteHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(mContent).inflate(R.layout.item_iapprove_manage_recyclerview, viewGroup, false);
            return new DefulteHolder(view);
        }

        @Override
        public void onBindViewHolder(MyRecyclerViewAdapter.DefulteHolder holder, int position) {
            ApproveManageListEntity.PropertyInfoList entity=list.get(position);
            if(entity.getImportance() ==1){
                holder.PropertyValue.setTextColor(mContent.getResources().getColor(R.color.red));
            }
            holder.PropertyName.setText(entity.getPropertyName());
            holder.PropertyValue.setText(entity.getPropertyValue());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class DefulteHolder extends RecyclerView.ViewHolder {
            public AppCompatTextView PropertyName;
            public AutofitTextView PropertyValue;
            public DefulteHolder(View itemView) {
                super(itemView);
                PropertyName = (AppCompatTextView) itemView.findViewById(R.id.tv_approve_frag_name);
                PropertyValue = (AutofitTextView) itemView.findViewById(R.id.tv_approve_frag_value);
            }
        }
    }


    @Override
    public void onDestroyView() {
        mRequestQueue.cancelAll(this);
        mBroadcastManager.unregisterReceiver(mReceiver);
        super.onDestroyView();
    }
}
