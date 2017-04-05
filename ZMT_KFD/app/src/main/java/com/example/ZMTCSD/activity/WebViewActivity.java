package com.example.ZMTCSD.activity;

import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.MyApplication;
import com.example.ZMTCSD.R;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import static android.webkit.WebSettings.LayoutAlgorithm.SINGLE_COLUMN;

/**
 *  打开链接的WebView
*/

@EActivity(R.layout.activity_web_view)
public class WebViewActivity extends BaseActivity {
    private String url;
    private String name;
    @ViewById(R.id.webView)
    WebView mWebView;

    @ViewById(R.id.progressbar)
    CircleProgressBar mProgressbar;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;


    @Override
    public void onAfterViews() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        showView(mProgressbar);
        url=getIntent().getStringExtra(AppDelegate.DETAILS_WEBVIEW);
        name= getIntent().getStringExtra(AppDelegate.DETAILS_WEBVIEW_NAME);
        if(!TextUtils.isEmpty(name)){
           tv_title.setText(name);
        }else{
            tv_title.setText("文件");
        }

        // WebView加载web资源

//        initWebViewSettings();
//        mWebView.loadUrl(url);
        LogUtils.d("WebView加载web资源"+getIntent().getStringExtra(AppDelegate.DETAILS_WEBVIEW));
        // 设置网页用WebView打开
//        mWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                //返回值是true的时候WebView打开，为false调用系统浏览器或第三方浏览器
//                return true;
//            }
//        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                LogUtils.i( "onLoadResource url="+url);
                super.onLoadResource(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webview, String url) {
                LogUtils.i( "intercept url="+url);
                webview.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                LogUtils.e( "onPageStarted");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                String title = view.getTitle();

                LogUtils.e( "onPageFinished WebView title=" + title);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getApplicationContext(), "隐藏加载界面", Toast.LENGTH_LONG).show();
            }
        });

        // 判断页面加载过程
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 网页加载完成
                    mProgressbar.setVisibility(View.GONE);
                }
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                LogUtils.e( "onJsAlert " + message);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                result.confirm();
                return true;
            }
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                LogUtils.e( "onJsConfirm " + message);
                return super.onJsConfirm(view, url, message, result);
            }
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                LogUtils.e( "onJsPrompt " + url);
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });


        WebSettings settings = mWebView.getSettings();
//         启用支持javascript(支持缩放必备)
        settings.setJavaScriptEnabled(true);
//         设定支持缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
//         不显示webview缩放按钮（Android 3.0）
        settings.setDisplayZoomControls(false);
//         设定支持viewport（双击缩放）
        settings.setUseWideViewPort(true);
//         设定第一次进入页面显示整页web视图
        settings.setLoadWithOverviewMode(true);
//         缓存的使用-优先使用缓存
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//         设置网页的排列算法，SINGLE_COLUMN 单列显示
        // settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.loadUrl(url);
    }

    private void initWebViewSettings(){
//        myWebView.getSettings().setSupportZoom(true);
//        myWebView.getSettings().setBuiltInZoomControls(true);
//        myWebView.getSettings().setDefaultFontSize(12);
//        myWebView.getSettings().setLoadWithOverviewMode(true);
        // 设置可以访问文件
        mWebView.getSettings().setAllowFileAccess(true);
        //如果访问的页面中有Javascript，则webview必须设置支持Javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.getSettings().setUserAgentString(MyApplication.getUserAgent());
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
    }

    // 改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();// 返回上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }



}
