package com.example.ZMTCSD.activity;

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

import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.dal.ServerDal;
import com.example.ZMTCSD.entity.ServerDeployEntity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_setting_server)
public class Setting_ServerActivity extends BaseActivity {
    private ServerRecyclerAdapter adapter;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView mTitle;

    @ViewById(R.id.server_recycler)
    RecyclerView mRecyclerview;

    @Override
    public void onAfterViews() {
        initToolbar();
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        List<ServerDeployEntity> list= ServerDal.GetListServer();
        adapter=new ServerRecyclerAdapter(list);
        mRecyclerview.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .colorResId(R.color.line_color)
                        .marginResId(R.dimen.left_16, R.dimen.right_0) //记录左右的距离
                        .build());
        mRecyclerview.setAdapter(adapter);
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mTitle.setText(getStrings(R.string.toolbar_SettingServer));
    }

    private class ServerRecyclerAdapter extends RecyclerView.Adapter<ServerRecyclerAdapter.ViewHolder> {

        private List<ServerDeployEntity> ServerList;

        public ServerRecyclerAdapter(List<ServerDeployEntity> ServerList) {
            this.ServerList = ServerList;
        }

        @Override
        public int getItemCount() {
            return ServerList.size();
        }

        @Override
        public ServerRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_server_recycler, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ServerRecyclerAdapter.ViewHolder holder, int position) {
            ServerDeployEntity entity=ServerList.get(position);
            holder.serverName.setText(entity.getServerName());
            holder.serverRemark.setText(entity.getServerRemark());
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public RelativeLayout mitemView;
            public AppCompatTextView serverName;
            public AppCompatTextView serverRemark;

            public ViewHolder(final View itemView) {
                super(itemView);
                serverName = (AppCompatTextView) itemView.findViewById(R.id.tv_server_name);
                serverRemark = (AppCompatTextView) itemView.findViewById(R.id.tv_server_remark);
                mitemView= (RelativeLayout) itemView.findViewById(R.id.rl_server_item);
                mitemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //去服务器详细界面
                        ServerDeployEntity item_server=ServerList.get(getLayoutPosition());
                        Setting_AboutServerActivity_.intent(Setting_ServerActivity.this)
                                .extra(AppDelegate.SERVER_ITEM,item_server).start();
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
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
        super.onDestroy();
    }
}
