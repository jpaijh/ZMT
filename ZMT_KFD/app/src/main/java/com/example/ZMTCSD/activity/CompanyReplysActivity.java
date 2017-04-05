package com.example.ZMTCSD.activity;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alorma.timeline.RoundTimelineView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.entity.CompanyReplysEntity;
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
 * 中信保的查看批复
 */
@EActivity(R.layout.activity_company_reply)
public class CompanyReplysActivity extends BaseActivity {
    private Context mContent;
    private RequestQueue mRequestQueue;
    private List<CompanyReplysEntity> rowsEntity;
    private ReplysAdapter mAdaper;
    private String applyKeyword;
    private int Type;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    @ViewById(R.id.ll_content)
    LinearLayout ll_content;

    @ViewById(R.id.recyclerView)
    RecyclerView mRecycler;

    @ViewById(R.id.tv_no_reply)
    AppCompatTextView no_reply;

    @ViewById(R.id.tv_no_data)
    AppCompatTextView tv_no_data;

    @ViewById(R.id.progressbar)
    CircleProgressBar mProgressbar;

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_title.setText(getStrings(R.string.toolbar_company_reply));
    }

    @Override
    public void onAfterViews() {
        mContent = CompanyReplysActivity.this;
        mRequestQueue = Volley.newRequestQueue(mContent);
        initToolbar();
        showView(mProgressbar);
        applyKeyword = getIntent().getStringExtra(AppDelegate.REPLYSKEYWORD);
        Type = getIntent().getIntExtra(AppDelegate.TYPE, -1);
        mRecycler.setLayoutManager(new LinearLayoutManager(mContent, LinearLayoutManager.VERTICAL, false));
        mRecycler.setHasFixedSize(true);
        mRecycler.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContent)
                .colorResId(R.color.line_color).marginResId(R.dimen.left_16, R.dimen.right_0) //记录左右的距离
                .build());
        onBackgrounds();
        super.onAfterViews();
    }

    //    买方代码申请批复 0,银行代码申请批复 1,限额申请批复 2,投保申请批复 3,
    @Override
    public void onBackgrounds() {
//        http://183.129.133.147:10086/api/CreditGuarantee/Replys?keyword=4851410&type=0
        final String url = MoreUserDal.GetServerUrl() + "/api/CreditGuarantee/Replys?keyword=" + applyKeyword + "&type=" + Type;
        LogUtils.d("查看批复" + url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<org.json.JSONArray>() {
            @Override
            public void onResponse(org.json.JSONArray response) {
                rowsEntity = JSON.parseArray(response.toString(), CompanyReplysEntity.class);
                if (rowsEntity != null && rowsEntity.size() != 0) {
                    mAdaper = new ReplysAdapter(rowsEntity);
                    mRecycler.setAdapter(mAdaper);
                } else {
                    showView(no_reply);
                }
                showView(ll_content);
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
        jsonArrayRequest.setTag(this);
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 3, 1.0f));
        mRequestQueue.add(jsonArrayRequest);
        super.onBackgrounds();
    }

    public class ReplysAdapter extends RecyclerView.Adapter<ReplysAdapter.ViewHolder> {
        private List<CompanyReplysEntity> replysList;

        public ReplysAdapter(List<CompanyReplysEntity> replysList) {
            this.replysList = replysList;
        }

        @Override
        public int getItemCount() {
            return replysList.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_action_list, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            CompanyReplysEntity entity = replysList.get(position);
            if (entity.getAgreed()) {
                holder.name.setText(getString(R.string.company_reply_three) + StringUtil.StringToNull(entity.getReason()));
            } else {
                holder.name.setText(getString(R.string.company_reply_four) + StringUtil.StringToNull(entity.getReason()));
            }
            holder.time.setText(StringUtil.dateRemoveT(entity.getUpdateTime()));

            if (position == 0) {
                holder.timeline.setTimelineType(RoundTimelineView.TYPE_START);
            }
            if (position != 0 && position == getItemCount() - 1) {
                holder.timeline.setTimelineType(RoundTimelineView.TYPE_END);
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public RoundTimelineView timeline;
            public AppCompatTextView name;
            public AppCompatTextView time;

            public ViewHolder(View itemView) {
                super(itemView);
                timeline = (RoundTimelineView) itemView.findViewById(R.id.timeline_orderAction);
                name = (AppCompatTextView) itemView.findViewById(R.id.tv_name_orderAction);
                time = (AppCompatTextView) itemView.findViewById(R.id.tv_time_orderAction);
                timeline.setDrawInternal(true);//设置可以有中心圈
                timeline.setLineColor(getColors(R.color.details_Approval));
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
        mRequestQueue.cancelAll(this);
        super.onDestroy();
    }
}
