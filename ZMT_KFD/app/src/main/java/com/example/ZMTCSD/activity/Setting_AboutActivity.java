package com.example.ZMTCSD.activity;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.utils.DeviceUtil;
import com.example.ZMTCSD.utils.StringUtil;
import com.example.ZMTCSD.utils.ToastUtil;
import com.flyco.animation.BounceEnter.BounceEnter;
import com.flyco.animation.ZoomExit.ZoomInExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import static com.pgyersdk.b.a.m;

@EActivity(R.layout.activity_setting_about)
public class Setting_AboutActivity extends BaseActivity {
    private Context mContent;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView mTitle;

    @ViewById(R.id.tv_versionName)
    AppCompatTextView mVersionName;

    @Override
    public void onAfterViews() {
        mContent=Setting_AboutActivity.this;
        initToolbar();
        mVersionName.setText(DeviceUtil.getVersionName(mContent));
        super.onAfterViews();
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mTitle.setText(getStrings(R.string.toolbar_SettingAbout));
    }

    @Click(R.id.ll_check_update)
    void checkUpdate() {
        PgyUpdateManager.register(this, null, new UpdateManagerListener() {
            @Override
            public void onUpdateAvailable(final String result) {
                // 将新版本信息封装到AppBean中
                final AppBean appBean = getAppBeanFromString(result);
                final MaterialDialog dialog = new MaterialDialog(mContent);
                LogUtils.e(appBean.getVersionCode()+":"+appBean.getVersionName());
                dialog.title("发现新版本")
                        .titleTextColor(getColors(R.color.black_two_mark_54))
                        .titleTextSize(18)//
                        .content(appBean.getReleaseNote())
                        .contentTextSize(20)
                        .contentGravity(Gravity.LEFT)
                        .contentTextColor(getColors(R.color.black_one_mark_87))
                        .btnText("取消", "下载")
                        .isTitleShow(true)
                        .showAnim(new BounceEnter())//
//                        .dismissAnim(new ZoomInExit())
                        .show();
                dialog.setOnBtnClickL(new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                }, new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        startDownloadTask(Setting_AboutActivity.this , appBean.getDownloadURL());
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onNoUpdateAvailable() {

                ToastUtil.showToast(mContent, "当前已是最新版本.");
            }
        });

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
