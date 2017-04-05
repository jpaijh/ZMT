package com.example.ZMTCSD.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.entity.PaymentDataEntity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * 待客提款 的选择 费用方式
 */
@EActivity(R.layout.my_select_text_recycler)
public class PaymentNewSubTypeActivity extends BaseActivity {
    private Context mContent;
    private LinearLayoutManager mLinearLayoutManager;
    private TypeItemAdapter mAdapter;
    private PaymentDataEntity.SubTypeList SubType;
    private List<PaymentDataEntity.SubTypeList> subTypeList;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    @ViewById(R.id.recyclerView)
    RecyclerView mRecycler;

    @ViewById(R.id.tv_volley_date)
    AppCompatTextView tv_date;

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_title.setText(getStrings(R.string.toolbar_paymentNewSubType));
    }

    @Override
    public void onAfterViews() {
        mContent = PaymentNewSubTypeActivity.this;
        initToolbar();
        SubType = (PaymentDataEntity.SubTypeList) getIntent().getSerializableExtra(AppDelegate.PYNEW_SUBTYPE);
        subTypeList = (List<PaymentDataEntity.SubTypeList>) getIntent().getSerializableExtra(AppDelegate.PYNEW_SUBTYPELIST);
        mLinearLayoutManager = new LinearLayoutManager(mContent, LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(mLinearLayoutManager);
        mRecycler.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContent)
                .colorResId(R.color.color_theme_bj).marginResId(R.dimen.left_16, R.dimen.right_0).showLastDivider().build());
        mAdapter = new TypeItemAdapter(subTypeList);
        mRecycler.setAdapter(mAdapter);
        super.onAfterViews();
    }

    public class TypeItemAdapter extends RecyclerView.Adapter<TypeItemAdapter.ViewHolder> {
        private List<PaymentDataEntity.SubTypeList> typeList;

        public TypeItemAdapter(List<PaymentDataEntity.SubTypeList> typeList) {
            this.typeList = typeList;
        }

        @Override
        public TypeItemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_customer_server_list, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TypeItemAdapter.ViewHolder holder, int position) {
            PaymentDataEntity.SubTypeList entity = typeList.get(position);
            holder.name.setText(entity.getPaymentSubTypeName());
            if (SubType.getPaymentSubTypeName().equals(entity.getPaymentSubTypeName()))
                showView(holder.tick);
        }

        @Override
        public int getItemCount() {
            return typeList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private AppCompatTextView name;
            private AppCompatImageView tick;
            private LinearLayout item;

            public ViewHolder(View itemView) {
                super(itemView);
                item = (LinearLayout) itemView.findViewById(R.id.ll_item);
                name = (AppCompatTextView) itemView.findViewById(R.id.tv_item_name);
                tick = (AppCompatImageView) itemView.findViewById(R.id.img_item_tick);

                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PaymentDataEntity.SubTypeList entity = typeList.get(getLayoutPosition());
                        Intent intent = getIntent();
                        intent.putExtra(AppDelegate.PYNEW_SUBTYPE, entity);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });

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
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
