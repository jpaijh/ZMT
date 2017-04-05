package com.example.ZMTCSD.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;

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
import com.example.ZMTCSD.MyApplication_;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.dal.PhraseDal;
import com.example.ZMTCSD.entity.PhraseEntity;
import com.example.ZMTCSD.utils.NetUtil;
import com.example.ZMTCSD.utils.StringUtil;
import com.example.ZMTCSD.utils.ToastUtil;
import com.example.ZMTCSD.view.LimitedEdittext.LimitedEditText;
import com.example.ZMTCSD.view.TagGroup;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.thinkcool.circletextimageview.CircleTextImageView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 审批界面
 */

@EActivity(R.layout.activity_doapproval)
public class DoApprovalActivity extends BaseActivity {
    private RequestQueue mRequestQueue;
    private SharedPreferences userInfoSp;
    private Context mContext;
    private String approvalId; //单据的参数
    private String flag;
    private String reporterName;
    private String reporteDate;
    private ProgressDialog mProgressDialog;
    private KProgressHUD hud;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_content;

    @ViewById(R.id.nav_circleimgview_do)
    CircleTextImageView mCirimage;

    @ViewById(R.id.tv_do_name)
    AppCompatTextView mName;

    @ViewById(R.id.tv_do_time)
    AppCompatTextView mTime;

    @ViewById(R.id.tv_do_select)
    AppCompatTextView mSelect;

    @ViewById(R.id.rbtn_do_unagree)
    RadioButton mBtnUnAgree;

    @ViewById(R.id.rbtn_do_agree)
    RadioButton mBtnAgree;

    @ViewById(R.id.tag_group_do)
    TagGroup mTagGroup;

    @ViewById(R.id.ed_do_remark)
    LimitedEditText mRemark;

    @ViewById(R.id.tv_do_submit)
    AppCompatTextView mSubmit;

    private String Remark;
    private boolean result;
    private String RemarkUTF;

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_content.setText("审批");
    }

    @Override
    public void onAfterViews() {
        this.mContext = DoApprovalActivity.this;
        initToolbar();
        mRequestQueue = Volley.newRequestQueue(mContext);
        userInfoSp = MyApplication_.getInstance().getUserInfoSp();
        //TODO 传来 数据
        approvalId = getIntent().getStringExtra(AppDelegate.APPROVALID);//获取从列表传来的值
        flag = getIntent().getStringExtra(AppDelegate.FLAG);
        reporterName = getIntent().getStringExtra(AppDelegate.APPROVAL_REPORTERNAME);
        reporteDate = getIntent().getStringExtra(AppDelegate.APPROVAL_REPORTERDATE);
        mName.setText(reporterName);
        mTime.setText(StringUtil.dateRemoveT(reporteDate));
        mSubmit.setEnabled(false);//禁止点击
        mSubmit.setBackgroundColor(getColors(R.color.doApproval_Ungreen));
    }

    @Click(R.id.rbtn_do_agree)
    void rbAgree() {
        mSelect.setText("你选择了  同意");
        mBtnAgree.setTextColor(getColors(R.color.white));
        mBtnUnAgree.setTextColor(getColors(R.color.color_theme));
        mRemark.setText("");
        result = true;
        mRemark.setHint(getStrings(R.string.DoApproval_Agree));
        mTagGroup.setTags(AddTags());
        mTagGroup.setOnTagClickListener(taglist);
        mSubmit.setEnabled(true);//可以点击true
        mSubmit.setBackgroundColor(getColors(R.color.doApproval_green));
    }

    @Click(R.id.rbtn_do_unagree)
    void rbUnAgree() {
        mSelect.setText("你选择了 不同意");
        mBtnUnAgree.setTextColor(getColors(R.color.white));
        mBtnAgree.setTextColor(getColors(R.color.color_theme));
        mRemark.setText("");
        result = false;
        mRemark.setHint(getStrings(R.string.DoApproval_UnAgree));
        mTagGroup.setTags(AddUnTags());
        mTagGroup.setOnTagClickListener(taglist);

        mSubmit.setEnabled(false);//禁止点击
        mSubmit.setBackgroundColor(getColors(R.color.doApproval_Ungreen));
        mRemark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mRemark.length() != 0) {
                    mSubmit.setEnabled(true);
                    mSubmit.setBackgroundColor(getColors(R.color.doApproval_green));
                } else {
                    mSubmit.setEnabled(false);
                    mSubmit.setBackgroundColor(getColors(R.color.doApproval_Ungreen));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Click(R.id.tv_do_submit)
    void Submit() {
        try {
            RemarkUTF = URLEncoder.encode(mRemark.getText().toString(), "UTF-8");
            LogUtils.d(mRemark.getText().toString() + "Submit: " + RemarkUTF);
            onBackgrounds();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackgrounds() {
        int second = (int) (System.currentTimeMillis() / 1000);
        if (isRefreshWin(second)) {
            DoApproval();
        }
    }

    @Background
    public void DoApproval() {
        if (NetUtil.isNetworkConnected(this)) {
            mHandler.sendMessage(mHandler.obtainMessage(2));
            final String url = MoreUserDal.GetServerUrl() + "/api/Approval/Approval/Approval";
            LogUtils.d(url);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new Response.Listener<org.json.JSONObject>() {
                @Override
                public void onResponse(org.json.JSONObject response) {
                    hud.dismiss();
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(AppDelegate.ACTION_REFRESH_DETAILS_DATA));
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(AppDelegate.ACTION_REFRESH_LIST_DATA));
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    LogUtils.d("审批失败" + error.toString() + ":" + new String(error.networkResponse.data));
                    hud.dismiss();
                    ToastUtil.showToast(DoApprovalActivity.this, "审批操作失败，请稍后再试...");
                }
            }) {
                //result=true&remark=%E7%AC%AC%E4%B8%89%E6%96%B9&flag=3178700&approvalId=3848502
                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded";
                }

                @Override
                public byte[] getBody() {
                    String string = "result=" + result + "&remark=" + RemarkUTF + "&flag=" + flag + "&approvalId=" + approvalId;
                    LogUtils.e(string);
                    return string.getBytes();
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return super.getParams();
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("Authorization", "bearer " + MoreUserDal.GetAccessToken());
                    return map;
                }

            };
            jsonObjectRequest.setTag(this);
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50 * 1000, 1, 1.0f));
            mRequestQueue.add(jsonObjectRequest);
        } else {
            mHandler.sendMessage(mHandler.obtainMessage(0));
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ToastUtil.showToast(DoApprovalActivity.this, "网络连接不可用，请检查网络");
                    break;
                case 1:
                    mProgressDialog = new ProgressDialog(DoApprovalActivity.this, R.style.MyProgressDialogStyle);
                    mProgressDialog.setMessage("审批中，请稍后...");
                    mProgressDialog.setCancelable(false);  //按返回键不能退出
                    mProgressDialog.setCanceledOnTouchOutside(false);  //外部不能取消
                    mProgressDialog.show();
                    break;
                case 2:
                    hud = KProgressHUD.create(DoApprovalActivity.this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setLabel("审批中，请稍后...")
                            .setCancellable(false);

                    hud.show();
                default:
                    break;
            }
        }
    };

    TagGroup.OnTagClickListener taglist = new TagGroup.OnTagClickListener() {
        @Override
        public void onTagClick(String tag, boolean isSelect) {
            Remark = mRemark.getText().toString();
            if (isSelect) {
                Remark = StringUtil.StringBuilderAndString(Remark, tag).trim();
            } else {
                if (Remark.contains(tag)) {
                    Remark = Remark.substring(0, Remark.lastIndexOf(tag)) + Remark.substring(Remark.lastIndexOf(tag) + tag.length()).trim();
                }
            }
            mRemark.setText(Remark);
            mRemark.setSelection(Remark.length());
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                finish();
                // 添加返回过渡动画.
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public List<String> AddTags() {
        List<PhraseEntity> list = PhraseDal.GetPhraseTop();
        return StringUtil.ACacheTag(list);
    }

    public List<String> AddUnTags() {
        List<PhraseEntity> list = PhraseDal.GetPhraseButton();
        return StringUtil.ACacheTag(list);
    }

    @Override
    protected void onDestroy() {
        mRequestQueue.cancelAll(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // 添加返回过渡动画.
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
