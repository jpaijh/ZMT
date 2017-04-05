package com.example.ZMTCSD.fragment;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.entity.FileInfoEntity;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.adapter.AttachmentFileAdapter;
import com.example.ZMTCSD.utils.IntentUtil;
import com.example.ZMTCSD.utils.ToastUtil;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.List;

import okhttp3.Call;

/**
 * 往来单位详细信息中的 附件
 */
@EFragment(R.layout.my_basic_recycler)
public class CompanyFilesFragment extends BaseFragment {
    private Context mContent;
    private AttachmentFileAdapter mAttachAdapter;
    private List<FileInfoEntity> files;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.recyclerView)
    RecyclerView mAttachRecycler;

    @ViewById(R.id.tv_no_data)
    AppCompatTextView tv_no_data;


    @Override
    public void onAfterViews() {
        hideView(mToolbar);
        mContent = getActivity();
        hud = KProgressHUD.create(mContent)
                .setStyle(KProgressHUD.Style.BAR_DETERMINATE)
                .setLabel("正在下载...").setMaxProgress(100);
        mAttachRecycler.setLayoutManager(new LinearLayoutManager(mContent, LinearLayoutManager.VERTICAL, false));
        mAttachRecycler.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContent)
                .colorResId(R.color.line_color).marginResId(R.dimen.left_16, R.dimen.right_0).showLastDivider() //记录左右的距离
                .build());
        files = (List<FileInfoEntity>) getArguments().getSerializable(AppDelegate.CUSTOMER_ATTACH_COMPANY);
        mAttachAdapter = new AttachmentFileAdapter(mContent, files);
        mAttachAdapter.addmOnFileClickListener(new AttachmentFileAdapter.OnFileClickListener() {
            @Override
            public void onItemClick(String path, String name, String type) {
                downloadFile(path, name, type);
            }
        });
        if (files != null && files.size() != 0) {
            mAttachRecycler.setAdapter(mAttachAdapter);
        } else {
            hideView(mAttachRecycler);
            tv_no_data.setText(getStrings(R.string.Customer_attach_one));
            showView(tv_no_data);
        }
    }

    private KProgressHUD hud;

    public void downloadFile(String url, String fileName, final String fileType) {
        OkHttpUtils.get().url(url).build().execute(new FileCallBack(mContent.getExternalFilesDir(null).getAbsolutePath(), fileName) {

            @Override
            public void onBefore(okhttp3.Request request, int id) {
                hud.show();
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                hud.setProgress((int) (100 * progress));
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                hud.dismiss();
                ToastUtil.showToast(mContent, "文件错误，请检查。。。");
                LogUtils.d(e.getStackTrace() + "错误" + e.getMessage());
            }

            @Override
            public void onResponse(File file, int id) {
                LogUtils.d(fileType + "下载完成" + file.getAbsolutePath());
                IntentUtil.OpenFile(mContent, fileType, file.getAbsolutePath());
            }
        });
    }

    @Override
    public void onBackgrounds() {
        super.onBackgrounds();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
