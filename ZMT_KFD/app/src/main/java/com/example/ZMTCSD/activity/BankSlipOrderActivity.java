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

import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.adapter.MyPropertyInfoAdapter;
import com.example.ZMTCSD.adapter.MyPropetyGroupsAdapter;
import com.example.ZMTCSD.entity.BankSlipDetailsEntity;
import com.example.ZMTCSD.entity.PropertyGroupsEntity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * 水单拆分到订单
 */

@EActivity(R.layout.my_basic_recycler)
public class BankSlipOrderActivity extends BaseActivity {
    private Context mContent;
    private List<BankSlipDetailsEntity.Orders> orderList;
    private BankSlipOrderAdapter mAdapter;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    @ViewById(R.id.recyclerView)
    RecyclerView mDetailsRecycler;

    @ViewById(R.id.tv_no_data)
    AppCompatTextView tv_no_data;

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_title.setText(getStrings(R.string.toolbar_BankSlipOrder));
    }

    @Override
    public void onAfterViews() {
        mContent = BankSlipOrderActivity.this;
        initToolbar();
        orderList = (List<BankSlipDetailsEntity.Orders>)
                getIntent().getSerializableExtra(AppDelegate.BANKSLIP_DETAIL_ORDER);
        mDetailsRecycler.setLayoutManager(new LinearLayoutManager(mContent, LinearLayoutManager.VERTICAL, false));
        mDetailsRecycler.setHasFixedSize(true);
        mAdapter = new BankSlipOrderAdapter(orderList);
        mDetailsRecycler.setAdapter(mAdapter);
        super.onAfterViews();
    }

    public class BankSlipOrderAdapter extends RecyclerView.Adapter<BankSlipOrderAdapter.ViewHolder> {
        private List<BankSlipDetailsEntity.Orders> list;
        private MyPropertyInfoAdapter mProperAdapter;
        List<PropertyGroupsEntity.PropertyInfos> properList;

        public BankSlipOrderAdapter(List<BankSlipDetailsEntity.Orders> list) {
            this.list = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ipprove_details_recycler, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            BankSlipDetailsEntity.Orders entity = list.get(position);
            holder.groupName.setText("订单 " + (position + 1));
            properList = entity.getPropertyGroup().getPropertyInfos();
            mProperAdapter = new MyPropertyInfoAdapter(mContent, properList);
            holder.groupRecycler.setAdapter(mProperAdapter);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public AppCompatTextView groupName; // 分组的头部信息
            public RecyclerView groupRecycler;  //分组的具体信息

            public ViewHolder(View itemView) {
                super(itemView);
                groupName = (AppCompatTextView) itemView.findViewById(R.id.tv_approve_group_name);
                groupRecycler = (RecyclerView) itemView.findViewById(R.id.approve_details_recycler);
                groupRecycler.setLayoutManager(new LinearLayoutManager(mContent, LinearLayoutManager.VERTICAL, false));
                groupRecycler.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContent)
                        .colorResId(R.color.line_color).marginResId(R.dimen.left_16, R.dimen.right_0) //记录左右的距离
                        .build());
            }
        }
    }

    @Override
    public void onBackgrounds() {
        super.onBackgrounds();
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
        finish();
        super.onDestroy();
    }
}
