package com.example.ZMTCSD.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.MyApplication;
import com.example.ZMTCSD.MyApplication_;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.entity.MoreUserEntity;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.fragment.AccountFragment_;
import com.example.ZMTCSD.fragment.HomeFragment_;
import com.example.ZMTCSD.fragment.MessageFragment_;
import com.example.ZMTCSD.fragment.SettingFragment_;
import com.example.ZMTCSD.server.TaskService_;
import com.example.ZMTCSD.utils.ToastUtil;
import com.flyco.animation.BounceEnter.BounceEnter;
import com.flyco.animation.ZoomExit.ZoomInExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.jaeger.library.StatusBarUtil;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Context mContent;
    private RequestQueue mRequestQueue;
    private SharedPreferences userInfoSp;
    private AppCompatTextView mUserName;
    private AppCompatTextView mUserStatus;

    private LocalBroadcastManager mBroadcastManager;//内部广播
    private BroadcastReceiver mReceiver;

    @ViewById(R.id.nav_view)
    NavigationView mNavigationView;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.fragment)
    FrameLayout mFramLayout;

    @ViewById(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private int mStatusBarColor;
    private int mAlpha = 50;
    @Override
    public void onAfterViews() {
        mContent=MainActivity.this;
        setStatusBar();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
//            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
//            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
//                //将侧边栏顶部延伸至status bar
//                mDrawerLayout.setFitsSystemWindows(true);
//                //将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
//                mDrawerLayout.setClipToPadding(false);
//            }
//        }

        MyApplication_.getInstance().exit(); // 退出Loading和Login界面
        MyApplication_.getInstance().addActivity(this);
        mRequestQueue = Volley.newRequestQueue(this);
        userInfoSp= MyApplication_.getInstance().getUserInfoSp();
        initToolbar();
        initBroadcastReceiver();
        initNavigetHeader();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState(); //初始化状态
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new HomeFragment_()).commit();
        mNavigationView.setCheckedItem(R.id.nav_home);
        mNavigationView.setNavigationItemSelectedListener(this);
        checkUpdate();

        bindService();
    }

    private void bindService(){
        LogUtils.e( "bindService()");
        Intent intent = new Intent(mContent,TaskService_.class);
        startService(intent);
    }

    private void unBindService(){
        LogUtils.e( "unBindService()");
        Intent intent = new Intent(mContent,TaskService_.class);
        stopService(intent);
    }

    /**
     * 检查更新
     */
    private void checkUpdate() {
        PgyUpdateManager.register(this, null, new UpdateManagerListener() {
            @Override
            public void onUpdateAvailable(final String result) {
//                 将新版本信息封装到AppBean中
                final AppBean appBean = getAppBeanFromString(result);
                final MaterialDialog dialog = new MaterialDialog(mContent);
                dialog.title("发现新版本")
                        .titleTextColor(getColors(R.color.black_two_mark_54))
                        .titleTextSize(18)//
                        .content(appBean.getReleaseNote())
                        .contentTextSize(20)
                        .contentGravity(Gravity.LEFT)
                        .contentTextColor(getColors(R.color.black_one_mark_87))
                        .btnText("取消", "下载")
                        .isTitleShow(true)
                        .showAnim(new BounceEnter())
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
                        startDownloadTask(MainActivity.this , appBean.getDownloadURL());
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onNoUpdateAvailable() { }
        });
    }

    private void initNavigetHeader() {
        View headview = mNavigationView.getHeaderView(0);
        mUserName= (AppCompatTextView) headview.findViewById(R.id.nav_user_name);
        mUserStatus= (AppCompatTextView) headview.findViewById(R.id.nav_user_status);

        mUserName.setText(MoreUserDal.GetUserClaimsName());
        mUserStatus.setText(MoreUserDal.GetUserClaimsDepart());
   }

    private void initBroadcastReceiver() {
        // 注册广播接收者
        mBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppDelegate.ACCOUNT_MOREUSER); //更新用户的数据

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() == AppDelegate.ACCOUNT_MOREUSER) {
                    //TODO 没有进行fragment的动画效果
                    Animation animation= AnimationUtils.loadAnimation(mContent,R.anim.in_translate_top);
                    mFramLayout.startAnimation(animation);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new HomeFragment_()).commit();
                    mToolbar.setTitle("工作台");
                    mNavigationView.setCheckedItem(R.id.nav_home);
                    mUserName.setText(MoreUserDal.GetUserClaimsName());
                    mUserStatus.setText(MoreUserDal.GetUserClaimsDepart());
                }
            }
        };
        mBroadcastManager.registerReceiver(mReceiver, intentFilter);
    }


    private void initToolbar() {
        mToolbar.setTitle("工作台");
        setSupportActionBar(mToolbar);
    }
    protected void setStatusBar() {
        mStatusBarColor = getResources().getColor(R.color.colorPrimary);
        StatusBarUtil.setColorForDrawerLayout(this, (DrawerLayout) findViewById(R.id.drawer_layout), mStatusBarColor, mAlpha);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new HomeFragment_()).commit();
                mToolbar.setTitle("工作台");
                break;
            case R.id.nav_account:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new AccountFragment_()).commit();
                mToolbar.setTitle("账户");
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new SettingFragment_()).commit();
                mToolbar.setTitle("设置");
                break;
            case R.id.nav_message:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new MessageFragment_()).commit();
                mToolbar.setTitle("消息列表");
                break;
            case R.id.nav_cancel:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("确定退出当前用户?")
                        .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LogUtils.d(MoreUserDal.GetMorentity().size());
                                // 跳转登录界面,重新登录
                                if( MoreUserDal.GetMorentity().size() ==1){
                                    // 退出需要重新登录，所以要将isLogin设置为false,避免会根据已存用户信息自动登录
                                    MoreUserDal.CleanMoreUser(MoreUserDal.GetMoreUser().getLoginName(),MoreUserDal.GetMoreUser().getServerId());
                                    userInfoSp.edit().putBoolean(AppDelegate.IS_LOGIN, false).commit();
                                    MainActivity.this.finish();
                                    MyApplication_.getInstance().exit();
                                }else{
                                    MoreUserEntity Entity=MoreUserDal.GetMoreUser();
                                    MoreUserDal.CleanMoreUser(Entity.getLoginName(),Entity.getServerId());
                                    MoreUserDal.RandomMoreUser();
                                    ToastUtil.showToast(mContent,"已退出用户"+Entity.getLoginName());
                                    LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(new Intent(AppDelegate.ACCOUNT_MOREUSER));
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
                break;
            default:
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private long mPressedTime = 0;
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            long mNowTime = System.currentTimeMillis();//获取第一次按键时间
            if ((mNowTime - mPressedTime) > 2000) { // 比较两次按键时间差（2秒 之内双击就退出应用）
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mPressedTime = mNowTime;
            } else { //退出程序
                finish();
                System.exit(0);
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        LogUtils.e("onDestroy");
        mBroadcastManager.unregisterReceiver(mReceiver);
        unBindService();
        MyApplication_.getInstance().exit();
        // 如果程序崩溃，会调用这个方法，然后上报 catch 异常
        try {
        } catch (Exception e) {
            PgyCrashManager.reportCaughtException(this, e);
        }
        super.onDestroy();
    }

}
