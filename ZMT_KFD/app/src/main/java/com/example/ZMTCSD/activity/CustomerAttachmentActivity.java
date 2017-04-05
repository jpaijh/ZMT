package com.example.ZMTCSD.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.adapter.AttachmentFileAdapter;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.entity.CustomerDetailsEntity;
import com.example.ZMTCSD.entity.FileInfoEntity;
import com.example.ZMTCSD.entity.UploadAccountFile;
import com.example.ZMTCSD.entity.UploadFile;
import com.example.ZMTCSD.fragment.AttachmentPopup;
import com.example.ZMTCSD.helper.VolleyMultipartRequest;
import com.example.ZMTCSD.utils.IntentUtil;
import com.example.ZMTCSD.utils.StringUtil;
import com.example.ZMTCSD.utils.ToastUtil;
import com.example.ZMTCSD.utils.VolleyErrorHelper;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.iwf.photopicker.PhotoPickUtils;
import me.iwf.photopicker.PhotoPicker;
import me.shaohui.advancedluban.Luban;
import me.shaohui.advancedluban.OnMultiCompressListener;
import okhttp3.Call;

/**
 * 客户的详情界面—客户的附件界面
 */
@EActivity(R.layout.my_basic_recycler)
public class CustomerAttachmentActivity extends BaseActivity implements AttachmentPopup.OnButtonClickListener {
    private Context mContent;
    private RequestQueue mRequestQueue;
    private AttachmentFileAdapter menuadapter;
    private MyConterRecyclerAdapter mConAdapter;
    private List<CustomerDetailsEntity.fileGroups> files;
    private List<File> FileList; //文件上传时选择的文件的路径的集合
    private KProgressHUD hud, uploadhud, removeHud;
    private int fileCategoryId;
    private String fileCategoryName;
    private int accountId;
    private String accountName;
    private AttachmentPopup attachmentPopup;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    @ViewById(R.id.recyclerView)
    RecyclerView mAttachRecycler;

    @ViewById(R.id.tv_no_data)
    AppCompatTextView tv_no_data;

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_title.setText(getStrings(R.string.toolbar_CustomerAttachment));
    }

    @Override
    public void onAfterViews() {
        mContent = CustomerAttachmentActivity.this;
        mRequestQueue = Volley.newRequestQueue(mContent);
        files = (List<CustomerDetailsEntity.fileGroups>) getIntent().getSerializableExtra(AppDelegate.CUSTOMER_ATTACH_COMPANY);
        accountId = getIntent().getIntExtra(AppDelegate.CUSTOMER_ID_COMPANY, 0);
        accountName = getIntent().getStringExtra(AppDelegate.CUSTOMER_ATTACH_NAME);
//        LogUtils.d(accountId + "附件" + accountName);
        initToolbar();
        hud = KProgressHUD.create(mContent).setStyle(KProgressHUD.Style.BAR_DETERMINATE)
                .setLabel("正在下载...").setMaxProgress(100);
        uploadhud = KProgressHUD.create(mContent).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("上传文件中").setDetailsLabel("请等待....").setCancellable(true);
        removeHud = KProgressHUD.create(mContent).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("文件删除中").setCancellable(true);
        mAttachRecycler.setLayoutManager(new LinearLayoutManager(mContent));
        mConAdapter = new MyConterRecyclerAdapter(files);
        mAttachRecycler.setAdapter(mConAdapter);
        if (!isFileInfos(files)) {
            hideView(mAttachRecycler);
            tv_no_data.setText(getStrings(R.string.Customer_attach_one));
            showView(tv_no_data);
        }
    }

    private class MyConterRecyclerAdapter extends RecyclerView.Adapter<MyConterRecyclerAdapter.ViewHolder> {
        private List<CustomerDetailsEntity.fileGroups> fileDescList;

        public MyConterRecyclerAdapter(List<CustomerDetailsEntity.fileGroups> prolist) {
            if (prolist != null) {
                this.fileDescList = prolist;
            } else {
                this.fileDescList = new ArrayList<>();
            }
        }

        void alterList(List<CustomerDetailsEntity.fileGroups> s) {
            fileDescList.clear();
            fileDescList.addAll(s);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return fileDescList.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_customer_attach_recycler, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final CustomerDetailsEntity.fileGroups fileDescentity = fileDescList.get(position);
            holder.groupName.setText(fileDescentity.getFileCategoryName());
            List<FileInfoEntity> FileInfoList = fileDescentity.getFileInfos();
            if (FileInfoList != null && FileInfoList.size() != 0) {
                menuadapter = new AttachmentFileAdapter(mContent, FileInfoList);
                holder.groupRecycler.setAdapter(menuadapter);
                menuadapter.addmOnFileClickListener(new AttachmentFileAdapter.OnFileClickListener() {
                    @Override
                    public void onItemClick(String path, String name, String FileType) {
                        downloadFile(path, name, FileType);
                    }
                });
                showView(holder.groupName);
            } else {
                hideView(holder.groupName);
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public LinearLayout ll_group;
            public AppCompatTextView groupName; // 分组的头部信息
            public SwipeMenuRecyclerView groupRecycler;  //分组的具体信息

            public ViewHolder(final View itemView) {
                super(itemView);
                ll_group = (LinearLayout) itemView.findViewById(R.id.ll_file);
                groupName = (AppCompatTextView) itemView.findViewById(R.id.tv_customer_attach_name);
                groupRecycler = (SwipeMenuRecyclerView) itemView.findViewById(R.id.recycler_customer_attach);
                groupRecycler.setLayoutManager(new LinearLayoutManager(mContent, LinearLayoutManager.VERTICAL, false));
                groupRecycler.setHasFixedSize(true);
                groupRecycler.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContent)
                        .colorResId(R.color.line_color).marginResId(R.dimen.left_16, R.dimen.right_0) //记录左右的距离
                        .build());
//                 为SwipeRecyclerView的Item创建菜单就两句话，不错就是这么简单： 设置菜单创建器。
                groupRecycler.setSwipeMenuCreator(new SwipeMenuCreator() {
                    @Override
                    public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                        int size = getResources().getDimensionPixelSize(R.dimen.lay_70);
                        SwipeMenuItem deleteItem = new SwipeMenuItem(mContent).setImage(R.mipmap.ic_action_delete)
                                .setBackgroundDrawable(R.drawable.selector_swipemenu_red)
                                .setText("删除") // 文字，还可以设置文字颜色，大小等。。
                                .setWidth(size).setHeight(size).setTextColor(Color.WHITE);
                        swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
                    }
                });
//                 设置菜单Item点击监听。
                groupRecycler.setSwipeMenuItemClickListener(new OnSwipeMenuItemClickListener() {
                    @Override
                    public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
                        final CustomerDetailsEntity.fileGroups fileDescentity = fileDescList.get(getAdapterPosition());
                        closeable.smoothCloseMenu();// 关闭被点击的菜单。
                        LogUtils.e(fileDescentity.getFileCategoryName() + "ss" + fileDescentity.getFileInfos());
                        FileInfoEntity file = fileDescentity.getFileInfos().get(adapterPosition);
                        removeHud.show();
                        RemoveFile(file.getFileId());
                    }
                });
            }
        }
    }

    //删除客户的附件文件
    public void RemoveFile(String FileId) {
        final String url = MoreUserDal.GetServerUrl() + "/api/platformaccount/RemoveAccountFile?accountId=" + accountId + "&fileId=" + FileId;
        LogUtils.d("附件的删除" + url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, new Response.Listener<org.json.JSONArray>() {
            @Override
            public void onResponse(org.json.JSONArray response) {
                removeHud.dismiss();
                List<CustomerDetailsEntity.fileGroups> fileEntity = JSON.parseArray(response.toString(), CustomerDetailsEntity.fileGroups.class);
                if (isFileInfos(fileEntity)) {
                    mConAdapter.alterList(fileEntity);
                } else {
                    hideView(mAttachRecycler);
                    tv_no_data.setText(getStrings(R.string.Customer_attach_one));
                    showView(tv_no_data);
                }
                ToastUtil.showToast(mContent, "删除附件成功");
                LocalBroadcastManager.getInstance(mContent).sendBroadcast(new Intent(AppDelegate.CUSTOMER_ATTACHMENT_REFRESH));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                removeHud.dismiss();
                LogUtils.d("附件的删除" + VolleyErrorHelper.getMessage(error, mContent));
                ToastUtil.showToast(mContent, "删除附件失败");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "bearer " + MoreUserDal.GetAccessToken());
                return map;
            }
        };
        jsonArrayRequest.setTag(this);
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 1, 1.0f));
        mRequestQueue.add(jsonArrayRequest);
    }

    //文件的下载
    public void downloadFile(String url, String fileName, final String fileType) {
        OkHttpUtils.get().url(url).build().execute(new FileCallBack(getExternalFilesDir(null).getAbsolutePath(), fileName) {
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
                IntentUtil.OpenFile(mContent, fileType, file.getAbsolutePath());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_add:
                attachmentPopup = new AttachmentPopup(mContent, files);
                attachmentPopup
                        .anchorView(CustomerAttachmentActivity.this.findViewById(R.id.action_add))
                        .gravity(Gravity.BOTTOM)
                        .showAnim(new BounceTopEnter())
                        .dismissAnim(null) //new SlideTopExit()
                        .dimEnabled(true)//有阴影
                        .autoDismiss(false)
                        .show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnButtonClickListener(View v, int fileId, String fileName) {
        fileCategoryId = fileId;
        fileCategoryName = fileName;
        PhotoPicker.builder()
                .setPhotoCount(3)  //只能想着3张图片
                .setShowCamera(true) //有照相机
                .setPreviewEnabled(false)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoPickUtils.onActivityResult(requestCode, resultCode, data, new PhotoPickUtils.PickHandler() {
            @Override
            public void onPickSuccess(ArrayList<String> photos) {//已经预先做了null或size为0的判断
                List<File> mFileList = new ArrayList<File>();
                for (String s : photos) {
                    mFileList.add(new File(s));
                }
                compressImageList(mFileList);
                uploadhud.show();
            }

            @Override
            public void onPreviewBack(ArrayList<String> arrayList) {
                LogUtils.i("onPreviewBack" + arrayList.toString());
            }

            //没有选择图片的时候
            @Override
            public void onPickFail(String error) {
                ToastUtil.showToast(mContent, error);
            }

            @Override
            public void onPickCancle() {
                LogUtils.i("onPickCancle取消选择");
            }
        });
    }

    //使用鲁班压缩图片。
    private void compressImageList(List<File> mFileList) {
        Luban.get(mContent)
                .putGear(Luban.THIRD_GEAR)
                .load(mFileList)                     // load all images
                .launch(new OnMultiCompressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(List<File> fileList) {
                        FileList = fileList;
                        onBackgrounds();
                    }

                    @Override
                    public void onError(Throwable error) {
                        uploadhud.dismiss();
                        ToastUtil.showToast(mContent, "附件上传失败" + VolleyErrorHelper.getMessage(error, mContent));
                    }
                });     // passing an OnMultiCompress Listener
    }

    @Override
    public void onBackgrounds() {
        final String url = MoreUserDal.GetServerUrl() + "/api/resource/UploadFile";
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                List<UploadFile> upFile = JSON.parseArray(resultResponse, UploadFile.class);
                final UploadAccountFile jsonFile = new UploadAccountFile();
                jsonFile.setFiles(upFile);
                jsonFile.setFileCategoryId(fileCategoryId);
                jsonFile.setFileCategoryName(fileCategoryName);
                jsonFile.setAccountId(accountId);
                jsonFile.setAccountName(accountName);
                final String json = JSONObject.toJSONString(jsonFile);

                String AccountFileUrl = MoreUserDal.GetServerUrl() + "/api/platformaccount/UploadAccountFile";
                LogUtils.d("文件上传" + AccountFileUrl);
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, AccountFileUrl, new Response.Listener<org.json.JSONArray>() {
                    @Override
                    public void onResponse(org.json.JSONArray response) {
                        List<CustomerDetailsEntity.fileGroups> fileEntity = JSON.parseArray(response.toString(), CustomerDetailsEntity.fileGroups.class);
                        uploadhud.dismiss();
                        showView(mAttachRecycler);
                        hideView(tv_no_data);
                        mConAdapter.alterList(fileEntity);
                        LocalBroadcastManager.getInstance(mContent).sendBroadcast(new Intent(AppDelegate.CUSTOMER_ATTACHMENT_REFRESH));
                        ToastUtil.showToast(mContent, "附件上传成功");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LogUtils.d(":" + VolleyErrorHelper.getMessage(error, mContent));
                        uploadhud.dismiss();
                        ToastUtil.showToast(mContent, "附件绑定用户失败" + VolleyErrorHelper.getMessage(error, mContent));
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("Authorization", "bearer   " + MoreUserDal.GetAccessToken());
                        map.put("Content-Type", "application/json");
                        return map;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json";
                    }

                    @Override
                    public byte[] getBody() {
                        return json.getBytes();
                    }
                };
                jsonArrayRequest.setTag(this);
                jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 1, 1.0f));
                mRequestQueue.add(jsonArrayRequest);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d(":" + VolleyErrorHelper.getMessage(error, mContent));
                uploadhud.dismiss();
                ToastUtil.showToast(mContent, "附件上传失败" + VolleyErrorHelper.getMessage(error, mContent));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "bearer   " + MoreUserDal.GetAccessToken());
                return map;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws IOException {
                Map<String, DataPart> params = new HashMap<>();
                for (int i = 0; i < FileList.size(); i++) {
                    params.put("file" + i, new DataPart(StringUtil.FilePathTOString(FileList.get(i).getCanonicalPath()), StringUtil.getBytesFromFile(FileList.get(i))));
                }
                return params;
            }
        };
        multipartRequest.setTag(this);
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 1, 1.0f));
        mRequestQueue.add(multipartRequest);
    }

    @Override
    protected void onDestroy() {
        mRequestQueue.cancelAll(this);
        finish();
        super.onDestroy();
    }

    //在附件中是否还存在文件
    private boolean isFileInfos(List<CustomerDetailsEntity.fileGroups> list) {
        boolean isFiles = false;
        for (CustomerDetailsEntity.fileGroups f : list) {
            if (f.getFileInfos() != null && f.getFileInfos().size() != 0) {
                isFiles = true;
                break;
            }
        }
        return isFiles;
    }
}
