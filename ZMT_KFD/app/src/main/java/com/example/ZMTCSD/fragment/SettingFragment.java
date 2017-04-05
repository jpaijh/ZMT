package com.example.ZMTCSD.fragment;

import android.content.Context;

import com.example.ZMTCSD.R;
import com.example.ZMTCSD.activity.Setting_AboutActivity_;
import com.example.ZMTCSD.activity.Setting_PermissionActivity_;
import com.example.ZMTCSD.activity.Setting_PhraseActivity_;
import com.example.ZMTCSD.activity.Setting_ServerActivity_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
 //设置界面

@EFragment(R.layout.fragment_settings)
public class SettingFragment extends BaseFragment {
    private Context mContent;
    @Override
    public void onAfterViews() {
        mContent=getActivity();
        super.onAfterViews();
    }

    //服务器连接
    @Click(R.id.tv_server_setting)
    void goServer() {
        Setting_ServerActivity_.intent(mContent).start();
    }

    //快捷短语
    @Click(R.id.tv_phrase_setting)
    void goPhrase() {
        Setting_PhraseActivity_.intent(mContent).start();
    }

    //关于界面
    @Click(R.id.tv_about_setting)
    void goAbout() {
        Setting_AboutActivity_.intent(mContent).start();
    }

    @Click(R.id.tv_permission_setting)
    void goPermission(){
        Setting_PermissionActivity_.intent(mContent).start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
