package com.example.ZMTCSD.fragment;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
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
import com.example.ZMTCSD.activity.BankSlipDetailsActivity_;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.utils.VolleyErrorHelper;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.NoDoubleClickListener;
import com.example.ZMTCSD.entity.BankSlipListEntity;
import com.example.ZMTCSD.utils.StringUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.grantland.widget.AutofitTextView;


/**
 *  水单的待认领和已认领的列表
 */
@EFragment(R.layout.my_segment_viewpager)
public class BankSlipListFragment extends BaseFragment {
    private static final String TAG = "BankSlipListFragment";
    private RequestQueue mRequestQueue;
    private Context mContent;
    private int lastVisibleItem;
    private int firstVisibleItem;
    private boolean isLoading;
    private boolean isFirstLoading; //是否为第一次加载
    private int totalPage;   //数据有多少页数
    private int pageIndex = 1;   //第一页
    private LinearLayoutManager mLinearLayoutManager;
    private BankSlipListAdapter  mAdapter;
    private List<BankSlipListEntity> rowsEntity;
    private LocalBroadcastManager mBroadcastManager;
    private BroadcastReceiver mReceiver;
    private String ClaimedString = "";

    private boolean IsClaimed;

    @ViewById(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @ViewById(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @ViewById(R.id.tv_no_data)
    AppCompatTextView tv_no_data;
    DrawerLayout mDrawerLayout;

    @Override
    public void onAfterViews() {
//        setHasOptionsMenu(true);//将fragment中的菜单添加到activity.
        this.mContent=getActivity();
        mDrawerLayout= (DrawerLayout) getActivity().findViewById(R.id.drawer_dr);
        IsClaimed=getArguments().getBoolean(AppDelegate.BANKSLIP_LIST_TYPE);
        mRequestQueue = Volley.newRequestQueue(mContent);
        initSwipeRefreshLayout();
        initRecyclerView();
        initBroadcastReceiver();
    }
    /**
     * 内部的广播
     */
    private void initBroadcastReceiver() {
        // 注册广播接收者
        mBroadcastManager = LocalBroadcastManager.getInstance(mContent);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppDelegate.BANK_SLIP_DRAWER_SCREEN); //水单搜索的广播
        intentFilter.addAction(AppDelegate.BANK_SLIP_CLAIM_FRAGMENT);//水单认领的广播
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() == AppDelegate.BANK_SLIP_DRAWER_SCREEN) {
                    boolean listType=intent.getBooleanExtra(AppDelegate.BANKSLIP_LIST_TYPE,false);
                    ClaimedString=intent.getStringExtra(AppDelegate.BANK_SLIP_LIST_SCREEN);
                    mDrawerLayout.closeDrawer(GravityCompat.END);
                    if(listType == IsClaimed){
                        refreshList();
                    }
                }else if(intent.getAction().equals(AppDelegate.BANK_SLIP_CLAIM_FRAGMENT)){
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
                .colorResId(R.color.color_theme_bj).sizeResId(R.dimen.left_16) //记录左右的距离
                .showLastDivider().build());
        //TODO 当数据不满足一屏幕的时候，刷新不了数据。 所以加了 firstVisibleItem!=0
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if ((mAdapter != null && lastVisibleItem + 1 == mAdapter.getItemCount())
                        && newState == RecyclerView.SCROLL_STATE_IDLE && !isLoading && firstVisibleItem!=0) {
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
                firstVisibleItem=mLinearLayoutManager.findFirstVisibleItemPosition();//获取滑动时的开始的position
//                LogUtils.e(mLinearLayoutManager.findFirstCompletelyVisibleItemPosition()+":"+firstVisibleItem
//                        +"sss"+lastVisibleItem+":"+mLinearLayoutManager.findLastCompletelyVisibleItemPosition()+";"+mAdapter.getItemCount());
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
                onBackgrounds();
                createRefreshSnackbar(mRecyclerView, TAG);  //创建 正在刷新...的snackbar
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
        int second = (int) (System.currentTimeMillis() / 1000);
        if (isRefreshWin(second)){
            DoBankSlipList();
        }else{

        }
    }

    void DoBankSlipList(){
        String url= MoreUserDal.GetServerUrl()+"/api/bankslip/BankSlips?PageSize=20&PageIndex="+pageIndex+"&IsClaimed="+IsClaimed+ClaimedString;
        LogUtils.d(IsClaimed+"水单列表数据"+url);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<org.json.JSONObject>() {
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
                    rowsEntity = JSON.parseArray(rows.toString(), BankSlipListEntity.class);
                    mAdapter = new BankSlipListAdapter(rowsEntity);
                    mRecyclerView.setAdapter(mAdapter);
                    if (!isFirstLoading)
                        createRefreshCompleteSnackbar(mRecyclerView);
                } else if (pageIndex <= totalPage) {
                    JSONArray rows = jsonObject.getJSONArray("rows");
                    rowsEntity.addAll(lastVisibleItem + 1, JSON.parseArray(rows.toString(), BankSlipListEntity.class));
                    mAdapter.notifyDataSetChanged();
                    createLoadingCompleteSnackbar(mRecyclerView);
                }

                mSwipeRefreshLayout.setRefreshing(false);
                isLoading = false;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d("查询失败" + VolleyErrorHelper.getMessage(error,mContent));
                mSwipeRefreshLayout.setRefreshing(false);
                isLoading = false;
                hideView(mRecyclerView);
                tv_no_data.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.mipmap.ic_bug3),null,null);
                tv_no_data.setText(VolleyErrorHelper.getMessage(error,mContent));
                showView(tv_no_data);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization","bearer   "+MoreUserDal.GetAccessToken());
                return map;
            }
        };
        jsonObjectRequest.setTag(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 3, 1.0f));
        mRequestQueue.add(jsonObjectRequest);
    }

    public class BankSlipListAdapter extends RecyclerView.Adapter<BankSlipListAdapter.ViewHolder>{
        private List<BankSlipListEntity> list;

        public BankSlipListAdapter(List<BankSlipListEntity> list) {
            this.list = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            final LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            return new ViewHolder(MaterialRippleLayout
                    .on(inflater.inflate(R.layout.item_bank_slip_list, viewGroup, false))
                    .rippleOverlay(true)//如果这是真的,涟漪在前景;错:背景
                    .rippleAlpha(0.1f)//α的涟漪
                    .rippleColor(getColors(R.color.color_ripple_btn))//涟漪的颜色
                    .rippleDelayClick(true)
                    .rippleHover(true)//如果这是真的,悬停效果时画的观点是感动
                    .create());
//            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bank_slip_list, viewGroup, false);
//            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            BankSlipListEntity entity=list.get(position);
            //看是否已认领了。
           if(IsClaimed){
               showView(holder.ll_two);
               holder.name.setText(StringUtil.StringToNull(entity.getAccountName()));
               holder.amountName.setText(getStrings(R.string.bankSlip_five) );
               holder.rateName.setText(getStrings(R.string.bankSlip_six) );
               holder.leftClaim.setText(StringUtil.numberFormat( entity.getClaimedAmount()));
               holder.leftClaim.setTextColor(getColors(R.color.red));
               holder.RMBRate.setText(StringUtil.numberForRate( entity.getCustomerRMBRate()) );
           }else{
               showView(holder.ll_one);
               holder.name.setText(StringUtil.StringToNull(entity.getPayer()));
               holder.amountName.setText(getStrings(R.string.bankSlip_two) );
               holder.rateName.setText(getStrings(R.string.bankSlip_three) );
               holder.leftClaim.setText(StringUtil.numberFormat( entity.getLeftClaimAmount()));
               holder.RMBRate.setText(StringUtil.numberForRate( entity.getBankRMBRate()) );
           }
            holder.currency.setText(entity.getCurrency());

            holder.netReceipt.setText( StringUtil.numberFormat(entity.getNetReceiptAmount()));
            holder.receiptDate.setText(StringUtil.YMDDtoYMD( entity.getReceiptDate()));

            holder.claimedDate.setText(StringUtil.YMDDtoYMD(entity.getClaimedDate()));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            AppCompatTextView name;
            AppCompatTextView amountName;
            AppCompatTextView currency;
            AppCompatTextView rateName;
            AutofitTextView leftClaim;
            AutofitTextView RMBRate;
            LinearLayout ll_one;
            AppCompatTextView netReceipt;
            AppCompatTextView receiptDate;
            LinearLayout ll_two;
            AppCompatTextView claimedDate;
            LinearLayout ll_content;
            public ViewHolder(View itemView) {
                super(itemView);
                name= (AppCompatTextView) itemView.findViewById(R.id.tv_bankSlip_name);
                amountName= (AppCompatTextView) itemView.findViewById(R.id.tv_bankSlip_amountName);
                currency= (AppCompatTextView) itemView.findViewById(R.id.tv_bankSlip_currency);
                rateName= (AppCompatTextView) itemView.findViewById(R.id.tv_bankSlip_rateName);
                leftClaim= (AutofitTextView) itemView.findViewById(R.id.tv_bankSlip_leftClaim);
                RMBRate= (AutofitTextView) itemView.findViewById(R.id.tv_bankSlip_RMBRate);
                ll_one= (LinearLayout) itemView.findViewById(R.id.ll_bankSlip_one);
                ll_two= (LinearLayout) itemView.findViewById(R.id.ll_bankSlip_two);
                netReceipt= (AppCompatTextView) itemView.findViewById(R.id.tv_bankSlip_netReceipt);
                receiptDate= (AppCompatTextView) itemView.findViewById(R.id.tv_bankSlip_receiptDate);
                claimedDate= (AppCompatTextView) itemView.findViewById(R.id.tv_bankSlip_claimedDate);

                ll_content= (LinearLayout) itemView.findViewById(R.id.ll_bank_slip);
                ll_content.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        //TODO 去详情
                        BankSlipListEntity customer=list.get(getLayoutPosition());
                        BankSlipDetailsActivity_.intent(mContent).extra(AppDelegate.CUSTOMER_ID_COMPANY,customer.getId()).start();
                    }
                });
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_approval_manage, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroyView() {
        mRequestQueue.cancelAll(this);
        mBroadcastManager.unregisterReceiver(mReceiver);
        super.onDestroyView();
    }
}
