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

import com.example.ZMTCSD.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 因为在新建客户的时需要客户类型。
 * 客户类型界面
 */
@EActivity(R.layout.my_select_text_recycler)
public class CustomerTypeActivity extends BaseActivity {
    private Context mContent;
    private String type;
    private TypeItemAdapter mAdapter;
    private List<CustomerType> lists = new ArrayList<>();

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
        tv_title.setText(getStrings(R.string.toolbar_CustomerType));
    }

    //    国内工厂 0 ，个人商户 3 ，中间商 2 ，国外买手 1，
    @Override
    public void onAfterViews() {
        mContent = CustomerTypeActivity.this;
        initToolbar();
        type = getIntent().getStringExtra("value");
        lists.add(new CustomerType("国内工厂", "0"));
        lists.add(new CustomerType("个人商户", "3"));
        lists.add(new CustomerType("中间商", "2"));
        lists.add(new CustomerType("国外买手", "1"));
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(mContent, LinearLayoutManager.VERTICAL, false));
        mRecycler.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContent)
                .colorResId(R.color.color_theme_bj).marginResId(R.dimen.left_16, R.dimen.right_0).showLastDivider().build());
        mAdapter = new TypeItemAdapter(lists);
        mRecycler.setAdapter(mAdapter);
        super.onAfterViews();
    }


    public class TypeItemAdapter extends RecyclerView.Adapter<TypeItemAdapter.ViewHolder> {
        private List<CustomerType> typeList;

        public TypeItemAdapter(List<CustomerType> typeList) {
            this.typeList = typeList;
        }

        @Override
        public TypeItemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_customer_server_list, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TypeItemAdapter.ViewHolder holder, int position) {
            CustomerType entity = typeList.get(position);
            holder.name.setText(entity.getName());
            if (type.equals(entity.getValue()))
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
                        CustomerType entity = typeList.get(getLayoutPosition());
                        Intent intent = getIntent();
                        intent.putExtra("value", entity.getValue());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });

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
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putExtra("value", type);
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }

    public static class CustomerType implements Serializable {
        private String name;
        private String type;

        @Override
        public String toString() {
            return "CustomerType{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }

        public CustomerType(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public void setValue(String value) {
            this.type = value;
        }

        public String getValue() {
            return this.type;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
