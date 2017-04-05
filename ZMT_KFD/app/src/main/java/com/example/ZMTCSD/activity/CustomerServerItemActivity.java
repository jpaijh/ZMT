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
import com.example.ZMTCSD.dal.MetaDataDal;
import com.example.ZMTCSD.entity.MetaDataEntity;
import com.example.ZMTCSD.utils.StringUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * 因为在新建客户的时需要服务项。
 * 服务项界面
 */

@EActivity(R.layout.my_select_text_recycler)
public class CustomerServerItemActivity extends BaseActivity {
    private Context mContent;
    private LinearLayoutManager mLinearLayoutManager;
    private ServerItemAdapter mAdapter;
    private String ServerItem;
    private List<String> strList;//从新建客户传来的集合
    private List<String> checkList = new ArrayList<>();//选择的id集合

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
        tv_title.setText(getStrings(R.string.toolbar_CustomerServerItem));
    }

    @Override
    public void onAfterViews() {
        mContent = CustomerServerItemActivity.this;
        initToolbar();
        ServerItem = getIntent().getStringExtra("serviceItemIds");
        strList = StringUtil.StringSplit(ServerItem);
        mLinearLayoutManager = new LinearLayoutManager(mContent, LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(mLinearLayoutManager);
        mRecycler.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContent)
                .colorResId(R.color.color_theme_bj).marginResId(R.dimen.left_16, R.dimen.right_0).showLastDivider().build());
        mAdapter = new ServerItemAdapter(MetaDataDal.getServerItemList());
        mRecycler.setAdapter(mAdapter);
        super.onAfterViews();
    }

    public class ServerItemAdapter extends RecyclerView.Adapter<ServerItemAdapter.ViewHolder> {
        private List<MetaDataEntity.Value> valueList;
        private int LoaderTime = 0;

        public ServerItemAdapter(List<MetaDataEntity.Value> valueList) {
            this.valueList = valueList;
        }

        @Override
        public ServerItemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_customer_server_list, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            MetaDataEntity.Value vEntity = valueList.get(position);
            holder.name.setText(vEntity.getName());
            holder.name.setTag(vEntity.getServiceItemId());// 设置tag 否则划回来时选中消失
            if (LoaderTime < strList.size()) {
                for (String str : strList) {
                    if (str.equals(vEntity.getServiceItemId())) {
                        checkList.add(vEntity.getServiceItemId());
                        LoaderTime += 1;
                    }
                }
            }

            if (checkList.contains(vEntity.getServiceItemId())) {
                showView(holder.tick);
            } else {
                hideView(holder.tick);
            }
        }

        @Override
        public int getItemCount() {
            return valueList.size();
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
                        MetaDataEntity.Value Entity = valueList.get(getLayoutPosition());
                        if (checkList.contains(Entity.getServiceItemId())) {
                            checkList.remove(Entity.getServiceItemId());
                            notifyItemChanged(getLayoutPosition());
                        } else {
                            checkList.add(Entity.getServiceItemId());
                            notifyItemChanged(getLayoutPosition());
                        }
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
        Intent intent = getIntent();
        intent.putExtra("serviceItemIds", StringUtil.join(",", checkList));
        intent.putExtra("ItemNames", StringUtil.join(",", MetaDataDal.GetNameList(checkList)));
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
