package com.example.ZMTCSD.activity;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.MyApplication_;
import com.example.ZMTCSD.R;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_loading)
public class LoadingActivity extends BaseActivity {

    @Override
    public void onAfterViews() {
        MyApplication_.getInstance().addActivity(this);
        onBackgrounds();
    }

    @Override
    public void onBackgrounds() {
        try {
            if (getSharedPreferences(AppDelegate.SP_USER_INFO, MODE_PRIVATE).getBoolean(AppDelegate.IS_LOGIN, false)) {
                Thread.sleep(2000);
                MainActivity_.intent(this).start();
                finish();
            } else {
                LoginActivity_.intent(this).start();
                finish();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loading界面-返回键就是退出应用
     */
    @Override
    public void onBackPressed() {
        System.exit(0);
    }

}
