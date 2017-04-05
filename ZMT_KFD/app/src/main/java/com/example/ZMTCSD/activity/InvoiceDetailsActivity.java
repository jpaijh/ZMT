package com.example.ZMTCSD.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.entity.InvoiceDetailsEntity;
import com.example.ZMTCSD.utils.VolleyErrorHelper;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.utils.StringUtil;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.grantland.widget.AutofitTextView;

/**
 * 增票的详情界面
 */
@EActivity(R.layout.activity_invoice_details)
public class InvoiceDetailsActivity extends BaseActivity {
    private Context mContent;
    private RequestQueue mRequestQueue; //volley
    private int InvoiceId;
    private InvoiceDetailsEntity detailsEntity;
    private InvoiceDetailAdapter mAdapter;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    @ViewById(R.id.tv_no_data)
    AppCompatTextView tv_no_data;

    @ViewById(R.id.progressbar)
    CircleProgressBar mProgressbar;

    @ViewById(R.id.nsv_InDetail_content)
    NestedScrollView mNsvContent;

    @ViewById(R.id.tv_InDetail_Corporation)
    AppCompatTextView mCorporation; //名称

    @ViewById(R.id.tv_InDetail_WithTax)
    AutofitTextView mWithTax; //价税合计

    @ViewById(R.id.tv_InDetail_WithOutTax)
    AutofitTextView mWithOutTax; //不含税金额

    @ViewById(R.id.tv_InDetail_Tax)
    AutofitTextView mTax; //税额

    @ViewById(R.id.tv_InDetail_Date)
    AppCompatTextView mDate;

    @ViewById(R.id.tv_InDetail_code)
    AppCompatTextView mCode;

    @ViewById(R.id.tv_InDetail_orderCode)
    AppCompatTextView mOrderCode;

    @ViewById(R.id.tv_InDetail_no)
    AppCompatTextView mNo;

    @ViewById(R.id.tv_InDetail_items)
    AppCompatTextView  mItemsize;

    @ViewById(R.id.recycler_InDetail)
    RecyclerView  mRecycler;


    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_title.setText(getStrings(R.string.toolbar_InvoiceDetails));
    }
    @Override
    public void onAfterViews() {
        mContent=InvoiceDetailsActivity.this;
        mRequestQueue= Volley.newRequestQueue(mContent);
        showView(mProgressbar);
        initToolbar();
        InvoiceId=getIntent().getIntExtra(AppDelegate.CUSTOMER_ID_COMPANY,0);//获取从列表传来的值
        mRecycler.setLayoutManager(new LinearLayoutManager(mContent));
        mRecycler.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContent)
                .colorResId(R.color.color_theme_bj).sizeResId(R.dimen.lay_10) //记录左右的距离
                .build());
        onBackgrounds();
    }

    @Override
    public void onBackgrounds() {
        int second = (int) (System.currentTimeMillis() / 1000);
        if (isRefreshWin(second)){
            DoInvoiceDetails();
        }else{
            hideView(mProgressbar);
            tv_no_data.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.mipmap.ic_bug3),null,null);
            tv_no_data.setText("刷新token不成功");
            showView(tv_no_data);
        }
    }

    void DoInvoiceDetails(){
        final String url= MoreUserDal.GetServerUrl()+"/api/invoice/Invoice?InvoiceId="+InvoiceId;
        LogUtils.d("增票的url:" + url);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                detailsEntity=JSON.parseObject(response.toString(), InvoiceDetailsEntity.class );
                mCorporation.setText(detailsEntity.getInvoiceCorporation());
                mWithTax.setText( StringUtil.numberDecimal(detailsEntity.getAmountWithTax()));
                mWithOutTax.setText(   StringUtil.numberDecimal(detailsEntity.getAmountWithOutTax()));
                mTax.setText(  StringUtil.numberDecimal(detailsEntity.getTax() ));
                mDate.setText(StringUtil.YMDDtoYMD(detailsEntity.getInvoiceDate()));
                mCode.setText(detailsEntity.getCode());
                mNo.setText(StringUtil.StringToNull(detailsEntity.getNo()));//修改当为null是变成--
                mOrderCode.setText(detailsEntity.getOrderCode());
                if(detailsEntity.getInvoiceItems()!=null && detailsEntity.getInvoiceItems().size() !=0 ){
                    mItemsize.setText(getStrings(R.string.Invoice_eight)+detailsEntity.getInvoiceItems().size()+
                            getStrings(R.string.Invoice_nine));
                    mAdapter=new InvoiceDetailAdapter(detailsEntity.getInvoiceItems());
                    mRecycler.setAdapter(mAdapter);
                }

                hideView(mProgressbar);
                showView(mNsvContent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d("增票详情错误"+ VolleyErrorHelper.getMessage(error,mContent));
                hideView(mProgressbar);
                tv_no_data.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.mipmap.ic_bug3),null,null);
                tv_no_data.setText(VolleyErrorHelper.getMessage(error,mContent));
                showView(tv_no_data);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "bearer " + MoreUserDal.GetAccessToken());
                return map;
            }
        };
        jsonObjectRequest.setTag(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50 * 1000, 3, 1.0f));
        mRequestQueue.add(jsonObjectRequest);
    }

    private class InvoiceDetailAdapter extends RecyclerView.Adapter<InvoiceDetailAdapter.ViewHolder> {
        private List<InvoiceDetailsEntity.InvoiceItems> itemsList;

        public InvoiceDetailAdapter(List<InvoiceDetailsEntity.InvoiceItems> properlist) {
            this.itemsList = properlist;
        }

        @Override
        public int getItemCount() {
            return itemsList.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_invoice_detail_list, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            InvoiceDetailsEntity.InvoiceItems entity = itemsList.get(position);
            holder.unit.setText(getStrings(R.string.Invoice_list_three)+" ("+entity.getUnit()+")");
            holder.name.setText(entity.getProductName());
            holder.taxRate.setText(StringUtil.taxRateFormat(entity.getTaxRate()));
            holder.price.setText(StringUtil.numberDecimal(entity.getUnitPrice()));
            holder.quantity.setText(""+entity.getProductQuantity());
            holder.withoutTax.setText(StringUtil.numberDecimal(entity.getAmountWithoutTax()));
            holder.tax.setText(StringUtil.numberDecimal(entity.getTax()));
            holder.withTax.setText(StringUtil.numberDecimal(entity.getAmountWithTax()));
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            AppCompatTextView name; //名称
            AppCompatTextView taxRate;  //税率
            AppCompatTextView price;  //单价
            AppCompatTextView quantity; //数量
            AppCompatTextView withoutTax;  //不含税金额
            AppCompatTextView tax;  //税额
            AppCompatTextView withTax;  //价税合计
            AppCompatTextView unit;

            public ViewHolder(final View itemView) {
                super(itemView);
                name= (AppCompatTextView) itemView.findViewById(R.id.tv_InDetail_list_name);
                taxRate= (AppCompatTextView) itemView.findViewById(R.id.tv_InDetail_list_taxRate);
                price= (AppCompatTextView) itemView.findViewById(R.id.tv_InDetail_list_Price);
                quantity= (AppCompatTextView) itemView.findViewById(R.id.tv_InDetail_list_Quantity);
                withoutTax= (AppCompatTextView) itemView.findViewById(R.id.tv_InDetail_list_WithoutTax);
                tax= (AppCompatTextView) itemView.findViewById(R.id.tv_InDetail_list_Tax);
                withTax= (AppCompatTextView) itemView.findViewById(R.id.tv_InDetail_list_WithTax);
                unit= (AppCompatTextView) itemView.findViewById(R.id.tv_InDetail_list_unit);
            }
        }
    }

    void onSettlePdf(){
//       http://www.tjzmt.com:9080/manage/vatInvoice/VatInvoiceExportPdf/5064503
        String url=AppDelegate.INVOICE_SETTLE_PDF_URL+InvoiceId;
//        WebViewActivity_.intent(mContent).extra(AppDelegate.DETAILS_WEBVIEW_NAME,"增值税发票").extra(AppDelegate.DETAILS_WEBVIEW,url).start();
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_export, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_export:
                onSettlePdf();
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
