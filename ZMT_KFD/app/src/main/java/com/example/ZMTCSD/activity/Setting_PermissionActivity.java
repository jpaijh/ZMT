package com.example.ZMTCSD.activity;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.dal.MetaDataDal;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.entity.MetaDataEntity;
import com.example.ZMTCSD.entity.UserClaimsEntity;
import com.example.ZMTCSD.entity.UserLoginEntity;
import com.example.ZMTCSD.utils.ToastUtil;
import com.example.ZMTCSD.utils.VolleyErrorHelper;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.thinkcool.circletextimageview.CircleTextImageView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  设置中的查看权限
 */
@EActivity(R.layout.my_basic_recycler)
public class Setting_PermissionActivity extends BaseActivity {
    private Context mContent;
    private PermissionAdapter mAdapter;
    private List<String> moduleId ;  //用户的权限集合
    private  List<MetaDataEntity.Value> valueList;
    private RequestQueue mRequestQueue;
    private KProgressHUD hud;

    @ViewById(R.id.recyclerView)
    RecyclerView mRecycler;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_title.setText(getStrings(R.string.toolbar_SettingPermission));
    }

    @Override
    public void onAfterViews() {
        mContent=Setting_PermissionActivity.this;
        initToolbar();
        mRequestQueue = Volley.newRequestQueue(this);
        moduleId=MoreUserDal.GetUserClaimsPermiss();
        hud= KProgressHUD.create(mContent).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("处理中").setSize(100,110).setCornerRadius(5);
        mRecycler.setLayoutManager(new LinearLayoutManager(mContent,LinearLayoutManager.VERTICAL,false));
        mRecycler.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContent)
                .colorResId(R.color.color_theme_bj).margin(R.dimen.left_16,R.dimen.right_0) //记录左右的距离
                .build());
        valueList=MetaDataDal.getPermissionList();
        mAdapter=new PermissionAdapter(valueList);
        mRecycler.setAdapter(mAdapter);
    }

    public class PermissionAdapter extends RecyclerView.Adapter<PermissionAdapter.ViewHolder>{
        private List<MetaDataEntity.Value> list;

        public PermissionAdapter(List<MetaDataEntity.Value> list) {
            this.list = list;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_company_list, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            MetaDataEntity.Value entity=list.get(position);
            holder.name.setText(entity.getModuleName());
            if(moduleId.contains(entity.getModuleId())){
                holder.name.setTextColor(getColors(R.color.black_one_mark_87));
                holder.status.setText(getStrings(R.string.setting_permission_one)+entity.getPermissionName()
                        +getStrings(R.string.setting_permission_three));
            }else {
                holder.name.setTextColor(getColors(R.color.black_three_mark_38));
                holder.status.setText(getStrings(R.string.setting_permission_one) + entity.getPermissionName()
                        + getStrings(R.string.setting_permission_two));
                holder.status.setTextColor(getColors(R.color.black_three_mark_38));
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public RelativeLayout ll_content;
            public CircleTextImageView mCircleImg;
            public AppCompatTextView name;
            public AppCompatTextView status;
            public AppCompatImageView TickImg;
            public ViewHolder(View itemView) {
                super(itemView);
                ll_content= (RelativeLayout) itemView.findViewById(R.id.ll_content);
                mCircleImg= (CircleTextImageView) itemView.findViewById(R.id.img_circle);
                name= (AppCompatTextView) itemView.findViewById(R.id.tv_name);
                status= (AppCompatTextView) itemView.findViewById(R.id.tv_value);
                TickImg= (AppCompatImageView) itemView.findViewById(R.id.img_tick);
                hideView(mCircleImg);

            }
        }
    }

    @Override
    public void onBackgrounds() {
        final String url_RefreshToken = MoreUserDal.GetServerUrl() + "/api/OAuth/Token";
        LogUtils.d("刷新token" + url_RefreshToken);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url_RefreshToken, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                final UserLoginEntity userLoginEntity = JSON.parseObject(response.toString(), UserLoginEntity.class);
                // 登陆成功：保存用户名
                int second = (int) (System.currentTimeMillis() / 1000);
                MoreUserDal.UpdateMoreUser(userLoginEntity, second);

                String url = MoreUserDal.GetServerUrl() + "/api/Auth/Claims";
                final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<org.json.JSONArray>() {
                    @Override
                    public void onResponse(org.json.JSONArray response) {
                        List<UserClaimsEntity> claimsList = JSON.parseArray(response.toString(), UserClaimsEntity.class);
                        LogUtils.d("用户成功"+claimsList.get(1).getValue() );
                        MoreUserDal.UpdateMoreUser(claimsList);
                        moduleId=MoreUserDal.GetUserClaimsPermiss();
                        hud.dismiss();
                        mAdapter.notifyDataSetChanged();
                        ToastUtil.showToast(mContent,"用户权限刷新成功");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LogUtils.e(error.toString() + "登录:"+ VolleyErrorHelper.getMessage(error,mContent));
                        hud.dismiss();
                        ToastUtil.showToast(mContent,VolleyErrorHelper.getMessage(error,mContent));
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("Authorization", "bearer   " + userLoginEntity.getAccess_token());
                        return map;
                    }
                };
                jsonArrayRequest.setTag(this);
                jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 3, 1.0f));
                mRequestQueue.add(jsonArrayRequest);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hud.dismiss();
                LogUtils.d("刷新失败" +  VolleyErrorHelper.getMessage(error,mContent));
                ToastUtil.showToast(mContent,VolleyErrorHelper.getMessage(error,mContent));
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
            @Override
            public byte[] getBody() {
                String string = "grant_type=refresh_token&refresh_token=" + MoreUserDal.GetRefreshToken();
                return string.getBytes();
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        jsonObjectRequest.setTag(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 1, 1.0f));
        mRequestQueue.add(jsonObjectRequest);

        super.onBackgrounds();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reset, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_reset:
                hud.show();
                onBackgrounds();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
