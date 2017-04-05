package com.example.ZMTCSD.activity;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.utils.BitmapCache;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 图片放大缩小使用多点触控和双击
 */
@EActivity(R.layout.activity_photo_view)
public class PhotoViewActivity extends BaseActivity{
    private PhotoViewAttacher mAttacher;
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    @ViewById(R.id.photoView)
    PhotoView mPhotoView;

    @ViewById(R.id.progressbar)
    CircleProgressBar mProgressbar;

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_title.setText(getStrings(R.string.toolbar_PhotoView));
    }
    @Override
    public void onAfterViews() {
        initToolbar();
        showView(mProgressbar);
        String url = getIntent().getStringExtra(AppDelegate.DETAILS_PHOTOVIEW);
//        LogUtils.e("图片："+url);
        ImageLoader imageLoader = new ImageLoader(Volley.newRequestQueue(this), new BitmapCache());
        // ImageLoader.getImageListener的第二个参数是默认的图片resource id
        // 第三个参数是请求失败时候的资源id，可以指定为0
        ImageLoader.ImageListener listener = imageLoader.getImageListener(mPhotoView,R.mipmap.ic_img_load,R.mipmap.ic_img_commit);
        imageLoader.get(url, listener);

        //附加一个PhotoViewAttacher,负责所有的缩放功能。
        mAttacher=new PhotoViewAttacher(mPhotoView);

        hideView(mProgressbar);
        mAttacher.update();
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
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }
}
