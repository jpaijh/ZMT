package com.example.ZMTCSD.fragment;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.apkfuns.logutils.LogUtils;
import com.balysv.materialripple.MaterialRippleLayout;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.activity.BanksDetailsActivity_;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.utils.VolleyErrorHelper;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.entity.CompanyBanksEntity;
import com.example.ZMTCSD.utils.StringUtil;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.grantland.widget.AutofitTextView;

/**
 * 往来单位详细信息中的 银行信息
 */
@EFragment(R.layout.fragment_company_banks)
public class CompanyBanksFragment extends BaseFragment {
    private RequestQueue mRequestQueue;
    private Context mContext;
    private int companyId;
    private List<CompanyBanksEntity> banksList;
    private BanksRecyclerAdapter mAdapter;

    @ViewById(R.id.recycler_bank)
    RecyclerView mRecycler;

    @ViewById(R.id.tv_no_data)
    AppCompatTextView tv_no_data;

    @Override
    public void onAfterViews() {
        mContext = getActivity();
        mRequestQueue = Volley.newRequestQueue(mContext);
        companyId = getArguments().getInt(AppDelegate.CUSTOMER_ID_COMPANY);
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        onBackgrounds();
    }

    @Override
    public void onBackgrounds() {
        int second = (int) (System.currentTimeMillis() / 1000);
        if (isRefreshWin(second)) {
            DoCompanyBanks();
        }
    }

    void DoCompanyBanks() {
        final String url = MoreUserDal.GetServerUrl() + "/api/platformAccount/banks?companyId=" + companyId;
        LogUtils.d("银行" + url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<org.json.JSONArray>() {
            @Override
            public void onResponse(org.json.JSONArray response) {
                banksList = JSON.parseArray(response.toString(), CompanyBanksEntity.class);
                if (banksList != null && banksList.size() != 0) {
                    mAdapter = new BanksRecyclerAdapter(banksList);
                    mRecycler.setAdapter(mAdapter);
                } else {
                    tv_no_data.setText("没有银行信息");
                    showView(tv_no_data);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d("银行错误" + VolleyErrorHelper.getMessage(error, mContext));
                tv_no_data.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.ic_bug3), null, null);
                tv_no_data.setText(VolleyErrorHelper.getMessage(error, mContext));
                showView(tv_no_data);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "bearer " + MoreUserDal.GetAccessToken());
                return map;
            }
        };
        jsonArrayRequest.setTag(this);
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(50 * 1000, 3, 1.0f));
        mRequestQueue.add(jsonArrayRequest);
    }

    private class BanksRecyclerAdapter extends RecyclerView.Adapter<BanksRecyclerAdapter.ViewHolder> {

        private List<CompanyBanksEntity> bankList;

        public BanksRecyclerAdapter(List<CompanyBanksEntity> bankList) {
            this.bankList = bankList;
        }

        @Override
        public int getItemCount() {
            return bankList.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_company_bank_list, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final CompanyBanksEntity entity = bankList.get(position);
            holder.name.setText(entity.getBankName());
            holder.status.setText(entity.getStatusStr());
            holder.swiftCode.setText(getStrings(R.string.company_bank_list__one) + StringUtil.StringToNull(entity.getSwiftCode()));
            holder.bankAccount.setText(entity.getBankAccount());
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private LinearLayout ll_content;
            private AutofitTextView name;
            private AppCompatTextView status;
            private AppCompatTextView swiftCode;
            private AutofitTextView bankAccount;

            public ViewHolder(final View itemView) {
                super(itemView);
                name = (AutofitTextView) itemView.findViewById(R.id.tv_banks_name);
                status = (AppCompatTextView) itemView.findViewById(R.id.tv_banks_statusStr);
                swiftCode = (AppCompatTextView) itemView.findViewById(R.id.tv_banks_swiftCode);
                bankAccount = (AutofitTextView) itemView.findViewById(R.id.tv_banks_bankAccount);
                ll_content = (LinearLayout) itemView.findViewById(R.id.ll_banks_content);
                MaterialRippleLayout.on(ll_content)
                        .rippleOverlay(true)//如果这是真的,涟漪在前景;错:背景
                        .rippleAlpha(0.1f)//α的涟漪
                        .rippleColor(getColors(R.color.color_ripple_btn))//涟漪的颜色
                        .rippleHover(true)//如果这是真的,悬停效果时画的观点是感动
                        .create();
                ll_content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CompanyBanksEntity entity = bankList.get(getAdapterPosition());
                        BanksDetailsActivity_.intent(mContext).extra(AppDelegate.COMPANY_BANKS_DETAILS, entity).start();
                    }
                });
            }
        }
    }

    @Override
    public void onDestroy() {
        mRequestQueue.cancelAll(this);
        super.onDestroy();
    }
}
