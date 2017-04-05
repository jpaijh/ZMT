package com.example.ZMTCSD.activity;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.entity.ServerDeployEntity;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_setting_serverabout)
public class Setting_AboutServerActivity extends BaseActivity {
    private ServerDeployEntity  Entity;
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView mTitle;


    @ViewById(R.id.tv_server_about_id)
    AppCompatTextView mTvID;

    @ViewById(R.id.tv_server_about_name)
    AppCompatTextView mTvName;

    @ViewById(R.id.tv_server_about_remark)
    AppCompatTextView mTvRemark;

    @ViewById(R.id.tv_server_about_address)
    AppCompatTextView mTvAddress;

    @Override
    public void onAfterViews() {
        initToolbar();
        Entity= (ServerDeployEntity) getIntent().getSerializableExtra(AppDelegate.SERVER_ITEM);
        mTvID.setText(Entity.getServerCode());
        mTvName.setText(Entity.getServerName());
        mTvRemark.setText(Entity.getServerRemark());
        mTvAddress.setText(Entity.getServerAddress());
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mTitle.setText("服务器信息");
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
