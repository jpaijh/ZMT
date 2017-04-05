package com.example.ZMTCSD.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.adapter.ApprovalResultAdapter;
import com.example.ZMTCSD.adapter.AttachmentFileAdapter;
import com.example.ZMTCSD.adapter.MyPropetyGroupsAdapter;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.entity.ApprovalResultEntity;
import com.example.ZMTCSD.entity.ApproveDetailsEntity;
import com.example.ZMTCSD.entity.ContactEntity;
import com.example.ZMTCSD.entity.FileInfoEntity;
import com.example.ZMTCSD.utils.DateUtil;
import com.example.ZMTCSD.utils.IntentUtil;
import com.example.ZMTCSD.utils.StringUtil;
import com.example.ZMTCSD.utils.ToastUtil;
import com.example.ZMTCSD.utils.VolleyErrorHelper;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.thinkcool.circletextimageview.CircleTextImageView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

//详情界面
@EActivity(R.layout.activity_iapprove_list_details)
public class IApprovalListDetailsActivity extends BaseActivity implements ApprovalResultAdapter.OnButtonClickListener {
    private Context mContext;
    private String UserId; //该用户的id
    private String approvalId; //单据的id
    private String flag;  //单据的参数
    private String reporterName;
    private String reporteDate;
    private boolean APPROVAL_TYPE; //单据的我审批的还是我发起的
    private RequestQueue mRequestQueue; //volley
    private ApproveDetailsEntity DetailsEntity; //审批详情
    private List<ApprovalResultEntity> approvalResultlist; //审批结果
    private MyPropetyGroupsAdapter mGroupAdapter;
    private AttachmentFileAdapter madapter;
    private ApprovalResultAdapter mResultAdapter; //审批结果的适配器

    private LocalBroadcastManager mBroadcastManager;//内部广播
    private BroadcastReceiver mReceiver;
    @ViewById(R.id.appbar)
    AppBarLayout mAppbar;

    @ViewById(R.id.rl_head_all)
    RelativeLayout mRlhead;

    @ViewById(R.id.nestedScrollView)
    NestedScrollView mNestedScrollView;

    @ViewById(R.id.progressbar)
    CircleProgressBar mProgressbar;

    @ViewById(R.id.fab_details)
    FloatingActionButton mFab;
    //头部的布局

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    @ViewById(R.id.content_toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_head_status)
    AppCompatTextView mTvStatus;

    @ViewById(R.id.tv_head_approvalType)
    AppCompatTextView mTvApprovalType;

    @ViewById(R.id.tv_head_approvalName)
    AppCompatTextView mTvApprovalName;

    @ViewById(R.id.nav_circleimgview_head)
    CircleTextImageView mCircleImg;

    @ViewById(R.id.tv_head_name)
    AppCompatTextView mTvName;

    @ViewById(R.id.tv_head_time)
    AppCompatTextView mTvDate;

    @ViewById(R.id.img_head_call)
    AppCompatImageView mImgCall;

    //    中间的布局
    @ViewById(R.id.details_recyclerView)
    RecyclerView mDetailsRecycler;

    //附件的布局ll_attachment
    @ViewById(R.id.ll_attachment)
    LinearLayout mllAttachment;

    @ViewById(R.id.tv_attachment)
    AppCompatTextView mTvAttachment;

    @ViewById(R.id.attachment_recycler)
    RecyclerView mAttachRecycler;

    //审批记录的布局
    @ViewById(R.id.tv_approvalLog)
    AppCompatTextView mTvLog;

    @ViewById(R.id.approvalLog_recycler)
    RecyclerView mLogRecycler;

    @ViewById(R.id.tv_no_data)
    AppCompatTextView tv_no_data;


    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_title.setText(getStrings(R.string.toolbar_IApproveDetails));
        if (APPROVAL_TYPE) {
            mToolbar.setTitle(getStrings(R.string.toolbar_IApproveList));
        } else {
            mToolbar.setTitle(getStrings(R.string.toolbar_InitiateList));
        }
        hud = KProgressHUD.create(IApprovalListDetailsActivity.this)
                .setStyle(KProgressHUD.Style.BAR_DETERMINATE)
                .setLabel("正在下载...").setMaxProgress(100);
    }

    private void initBroadcastReceiver() {
        // 注册广播接收者
        mBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppDelegate.ACTION_REFRESH_DETAILS_DATA); //刷新详情的广播

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() == AppDelegate.ACTION_REFRESH_DETAILS_DATA) {
                    onBackgrounds();
                }
            }
        };
        mBroadcastManager.registerReceiver(mReceiver, intentFilter);
    }


    @Override
    public void onAfterViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        showView(mProgressbar);
        this.mContext = this;
        mRequestQueue = Volley.newRequestQueue(this);
        UserId = MoreUserDal.GetUserClaimsID();
        approvalId = getIntent().getStringExtra(AppDelegate.APPROVALID);//获取从列表传来的值
        flag = getIntent().getStringExtra(AppDelegate.FLAG); //获取从列表传来的值
        APPROVAL_TYPE = getIntent().getBooleanExtra(AppDelegate.APPROVAL_LIST_TYPE, false);
//        LogUtils.d(APPROVAL_TYPE+"详情" + approvalId + ":" + flag);
        mDetailsRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAttachRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAttachRecycler.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .colorResId(R.color.line_color).marginResId(R.dimen.left_16, R.dimen.right_0).showLastDivider() //记录左右的距离
                .build());
        mLogRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        initToolbar();
        initBroadcastReceiver();
        onBackgrounds();
    }

    @Override
    public void onBackgrounds() {
        int second = (int) (System.currentTimeMillis() / 1000);
        if (isRefreshWin(second)) {
            ApprovelDetails();
        }
    }

    public void ApprovelDetails() {
        final String url = MoreUserDal.GetServerUrl() + "/api/Approval/Approval/Detail?approvalId=" + approvalId + "&flag=" + flag;
        LogUtils.d("详情" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                DetailsEntity = JSON.parseObject(response.toString(), ApproveDetailsEntity.class);
                reporterName = DetailsEntity.getReporterName();
                reporteDate = DetailsEntity.getReporteDate();
                hideView(mTvApprovalName);
                hideView(mFab);
                switch (DetailsEntity.getStatus()) {
                    case 0:
                        mTvStatus.setText(getStrings(R.string.Initiate_item_InApproval));
                        mRlhead.setBackgroundColor(getColors(R.color.details_InApproval));
                        showView(mTvApprovalName);
                        mTvApprovalName.setText("等待" + DetailsEntity.getCurrentApproverName() + "审批");
                        if (DetailsEntity.getCurrentApproverId().equals(UserId)) {
                            showView(mFab);  //是在审批中的时候就显示审批按钮
                        }
                        break;
                    case 1:
                        mTvStatus.setText(getStrings(R.string.Initiate_item_Approval));
                        mRlhead.setBackgroundColor(getColors(R.color.details_Approval));
                        break;
                    case -1:
                        mTvStatus.setText(getStrings(R.string.Initiate_item_UnApproval));
                        mRlhead.setBackgroundColor(getColors(R.color.details_UnApproval));
                        break;
                    case -2:
                        mTvStatus.setText(getStrings(R.string.Initiate_item_RepealApproval));
                        mRlhead.setBackgroundColor(getColors(R.color.details_UnApproval));
                        break;
                    default:
                        break;
                }
                mTvApprovalType.setText(DetailsEntity.getApprovalType());

                mTvName.setText(DetailsEntity.getReporterName());
                mTvDate.setText(StringUtil.dateRemoveT(DetailsEntity.getReporteDate()));

                //TODO 打电话 我把 targetSdkVersion 22，不使用6.0的权限
                if (DetailsEntity.getContacts() != null && DetailsEntity.getContacts().size() != 0) {
                    showView(mImgCall);
                    mImgCall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //显示窗口
                            DateUtil.DoCallDialog(mContext, DetailsEntity.getContacts());
                        }
                    });
                }

                if (DetailsEntity.getPropetyGroups() != null && DetailsEntity.getPropetyGroups().size() != 0) {
                    showView(mDetailsRecycler);
                    mGroupAdapter = new MyPropetyGroupsAdapter(mContext, DetailsEntity.getPropetyGroups());
                    mDetailsRecycler.setAdapter(mGroupAdapter);
                }
                //TODO  如果没有附件信息 ，返回的是[] null
                if (DetailsEntity.getFiles() != null && DetailsEntity.getFiles().size() != 0) {
                    LogUtils.e("附件信息"+DetailsEntity.getFiles().size());
                    showView(mllAttachment);
                    mTvAttachment.setText(getStrings(R.string.TvAttachment));
                    List<FileInfoEntity> attachments = DetailsEntity.getFiles();
                    showView(mAttachRecycler);
                    madapter = new AttachmentFileAdapter(mContext, attachments);
                    madapter.addmOnFileClickListener(new AttachmentFileAdapter.OnFileClickListener() {
                        @Override
                        public void onItemClick(String path, String name, String type) {
                            downloadFile(path, name, type);
                        }
                    });
                    mAttachRecycler.setAdapter(madapter);
                }

                hideView(mProgressbar); //取消progress
                showView(mAppbar);  //显示头部
                showView(mNestedScrollView); //显示content

                //查询审批结果
                final String Resulturl = MoreUserDal.GetServerUrl() + "/api/Approval/Approval/Results?approvalId=" + approvalId;
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Resulturl, new Response.Listener<org.json.JSONArray>() {
                    @Override
                    public void onResponse(org.json.JSONArray response) {
                        approvalResultlist = JSON.parseArray(response.toString(), ApprovalResultEntity.class);
                        if (approvalResultlist.size() == 0) {
                            mTvLog.setText(getStrings(R.string.TvApprovalUnResult));
                        } else {
                            mTvLog.setText(getStrings(R.string.TvApprovalResult));
                            showView(mLogRecycler);
                            mResultAdapter = new ApprovalResultAdapter(approvalResultlist, mContext);
                            mLogRecycler.setAdapter(mResultAdapter);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LogUtils.e("审批记录" + VolleyErrorHelper.getMessage(error, mContext));
                        ToastUtil.showToast(mContext, VolleyErrorHelper.getMessage(error, mContext));
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("Authorization", "bearer  " + MoreUserDal.GetAccessToken());
                        return map;
                    }
                };
                jsonArrayRequest.setTag(this);
                jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(50 * 1000, 3, 1.0f));
                mRequestQueue.add(jsonArrayRequest);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideView(mProgressbar); //取消progress
                LogUtils.e("单据详情" + VolleyErrorHelper.getMessage(error, mContext));
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

    /**
     * 回调审批记录中的电话。
     *
     * @param
     * @param entityList
     */
    @Override
    public void OnButtonClickListener(List<ContactEntity> entityList) {
        LogUtils.e(entityList);
        DateUtil.DoCallDialog(mContext, entityList);
    }

    @Click(R.id.fab_details)
    void Approval() {
        DoApprovalActivity_.intent(this).extra(AppDelegate.APPROVALID, approvalId)
                .extra(AppDelegate.FLAG, flag).extra(AppDelegate.APPROVAL_REPORTERNAME, reporterName)
                .extra(AppDelegate.APPROVAL_REPORTERDATE, reporteDate).start();
    }

    private KProgressHUD hud;

    public void downloadFile(String url, String fileName, final String fileType) {
        OkHttpUtils.get().url(url).build().execute(new FileCallBack(getExternalFilesDir(null).getAbsolutePath(), fileName) {

            @Override
            public void onBefore(okhttp3.Request request, int id) {
                hud.show();
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                hud.setProgress((int) (100 * progress));
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                hud.dismiss();
                ToastUtil.showToast(mContext, "文件错误，请检查。。。");
                LogUtils.d(e.getStackTrace() + "错误" + e.getMessage());
            }

            @Override
            public void onResponse(File file, int id) {
                LogUtils.d(fileType + "下载完成" + file.getAbsolutePath());
                IntentUtil.OpenFile(mContext, fileType, file.getAbsolutePath());
            }
        });
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
        mRequestQueue.cancelAll(this);
        mBroadcastManager.unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
