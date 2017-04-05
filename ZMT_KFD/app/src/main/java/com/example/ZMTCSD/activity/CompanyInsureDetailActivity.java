package com.example.ZMTCSD.activity;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
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
import com.example.ZMTCSD.adapter.AttachmentFileAdapter;
import com.example.ZMTCSD.adapter.MyPropetyGroupsAdapter;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.entity.CompanyInsureEntity;
import com.example.ZMTCSD.entity.FileInfoEntity;
import com.example.ZMTCSD.entity.PropertyGroupsEntity;
import com.example.ZMTCSD.utils.IntentUtil;
import com.example.ZMTCSD.utils.ToastUtil;
import com.example.ZMTCSD.utils.VolleyErrorHelper;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 中信保的投保详情
 */
@EActivity(R.layout.activity_company_lclimit_details)
public class CompanyInsureDetailActivity extends BaseActivity {
    private Context mContent;
    private RequestQueue mRequestQueue;
    private final int InsureType = 3; //查看批复时的type
    private KProgressHUD hud;
    private int orderId;
    private String applyKeyword;
    private List<PropertyGroupsEntity> mGroupsList;
    private List<FileInfoEntity> fileList;
    private MyPropetyGroupsAdapter mGroupAdapter;
    private AttachmentFileAdapter mAttachAdapter;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    @ViewById(R.id.recycler_orServer)
    RecyclerView mRecycler;

    //附件的情况
    @ViewById(R.id.ll_attachment)
    LinearLayout mAttach;

    @ViewById(R.id.recycler_attachment)
    RecyclerView mAttachRecycler;

    @ViewById(R.id.tv_no_data)
    AppCompatTextView tv_no_data;

    @ViewById(R.id.progressbar)
    CircleProgressBar mProgressbar;

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_title.setText(getStrings(R.string.toolbar_company_Insure_Detail));
    }

    @Override
    public void onAfterViews() {
        mContent = CompanyInsureDetailActivity.this;
        mRequestQueue = Volley.newRequestQueue(mContent);
        orderId = getIntent().getIntExtra(AppDelegate.CUSTOMER_ID_COMPANY, 0);
        applyKeyword = getIntent().getStringExtra(AppDelegate.REPLYSKEYWORD);
        initToolbar();
        showView(mProgressbar);//开启progress
        hud = KProgressHUD.create(mContent)
                .setStyle(KProgressHUD.Style.BAR_DETERMINATE)
                .setLabel("正在下载...").setMaxProgress(100);
        mRecycler.setLayoutManager(new LinearLayoutManager(mContent, LinearLayoutManager.VERTICAL, false));
        mRecycler.setHasFixedSize(true);
        mAttachRecycler.setLayoutManager(new LinearLayoutManager(mContent, LinearLayoutManager.VERTICAL, false));
        mAttachRecycler.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContent)
                .colorResId(R.color.line_color).marginResId(R.dimen.left_16, R.dimen.right_0) //记录左右的距离
                .build());
        onBackgrounds();
        super.onAfterViews();
    }

    @Override
    public void onBackgrounds() {
        final String url = MoreUserDal.GetServerUrl() + "/api/CreditGuarantee/CompanyInsureDetail?orderId=" + orderId;
        LogUtils.d("投保的详情" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                CompanyInsureEntity insureEntity = JSON.parseObject(response.toString(), CompanyInsureEntity.class);
                mGroupsList = insureEntity.getPropertyGroupInfos();
                fileList = insureEntity.getFileInfos();
                mGroupAdapter = new MyPropetyGroupsAdapter(mContent, mGroupsList);
                mRecycler.setAdapter(mGroupAdapter);
                if (fileList != null && fileList.size() != 0) {
                    showView(mAttach);
                    mAttachAdapter = new AttachmentFileAdapter(mContent, fileList);
                    mAttachAdapter.addmOnFileClickListener(new AttachmentFileAdapter.OnFileClickListener() {
                        @Override
                        public void onItemClick(String path, String name, String type) {
                            downloadFile(path, name, type);
                        }
                    });
                    mAttachRecycler.setAdapter(mAttachAdapter);
                }

                hideView(mProgressbar);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d("查询失败" + VolleyErrorHelper.getMessage(error, mContent));
                hideView(mProgressbar);
                tv_no_data.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.ic_bug3), null, null);
                tv_no_data.setText(VolleyErrorHelper.getMessage(error, mContent));
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
                ToastUtil.showToast(mContent, "文件错误，请检查。。。");
            }

            @Override
            public void onResponse(File file, int id) {
                LogUtils.d(fileType + "下载完成" + file.getAbsolutePath());
                IntentUtil.OpenFile(mContent, fileType, file.getAbsolutePath());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reply, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_reply:
                //TODO 去查看批复的界面。
                CompanyReplysActivity_.intent(mContent).extra(AppDelegate.REPLYSKEYWORD, applyKeyword)
                        .extra(AppDelegate.TYPE, InsureType).start();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        mRequestQueue.cancelAll(this);
        super.onDestroy();
    }
}
