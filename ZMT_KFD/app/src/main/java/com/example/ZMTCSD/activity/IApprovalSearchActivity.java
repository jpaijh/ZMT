package com.example.ZMTCSD.activity;

import android.content.SharedPreferences;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.MyApplication_;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.adapter.ListPopAdapter;
import com.example.ZMTCSD.utils.DeviceUtil;
import com.example.ZMTCSD.view.AppCompatAutoCompleteClearTextView;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Set;

@EActivity(R.layout.activity_iapprove_search)
public class IApprovalSearchActivity extends BaseActivity {

    private ListPopupWindow mListPop;
    private String[] types = new String[]{"全部单据", "付款申请", "内控合同", "工作联系单", "盖章合同"};
    private ListPopupWindow mTimePop;
    private String[] times = new String[]{"请选择时间段", "最近三天", "最近一周", "最近一月", " 最近三月", "最近半年", "最近一年"};

    @ViewById(R.id.acct_keyword)
    AppCompatAutoCompleteClearTextView acct_keyword; // 关键字

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;


    @Override
    public void onAfterViews() {
        initToolbar();
        // 设置关键字搜索的历史记录数据
        SharedPreferences searchHistorySp = MyApplication_.getInstance().getSearchHistorySp();
        Set<String> KeyHistory = searchHistorySp.getStringSet(AppDelegate.HISTORY_APPROVAL_MANAGE_KEY_SEARCH, null);
        if (KeyHistory != null) {
            ArrayList<String> arrayList = new ArrayList<>(KeyHistory);
            acct_keyword.setAdapter(new ListPopAdapter(this, R.layout.item_listpop_with_delete, R.id.tv_text, arrayList,
                    searchHistorySp.edit(), AppDelegate.HISTORY_APPROVAL_MANAGE_KEY_SEARCH));
        }

        acct_keyword.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DeviceUtil.hideSoft(getApplicationContext(), acct_keyword); //隐藏软键盘
            }
        });

        mListPop = new ListPopupWindow(this);

//        acct_type.setText("全部单据");
//        // 目的是无法输入,因为只能选择状态
//        acct_type.setInputType(InputType.TYPE_NULL);
//        acct_type.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListPop.setAdapter(new ArrayAdapter<>(IApprovalSearchActivity.this, R.layout.item_listpop, types));
//                mListPop.setAnchorView(acct_type);
//                mListPop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        acct_type.setText(types[position]);
//                        mListPop.dismiss();
//                    }
//                });
//                mListPop.show();
//            }
//        });
//
//        mTimePop = new ListPopupWindow(this);
//        acct_time.setText("请选择时间段");
//        // 目的是无法输入,因为只能选择状态
//        acct_time.setInputType(InputType.TYPE_NULL);
//        acct_time.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mTimePop.setAdapter(new ArrayAdapter<>(IApprovalSearchActivity.this, R.layout.item_listpop, times));
//                mTimePop.setAnchorView(acct_time);
//                mTimePop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        acct_time.setText(times[position]);
//                        mTimePop.dismiss();
//                    }
//                });
//                mTimePop.show();
//            }
//        });
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_title.setText(getStrings(R.string.toolbar_IApproveSearch));
    }

    @Click(R.id.btn_search)
    void search() {
        try {
//            // 获取单据类型
//            String statusStr = acct_type.getText().toString().trim();
//            String docType = "";
//            switch (statusStr) {
//                case "全部单据":
//                    docType = "";
//                    break;
//                case "付款申请":
//                    docType = "1";
//                    break;
//                case "内控合同":
//                    docType = "2";
//                    break;
//                case "工作联系单":
//                    docType = "3";
//                    break;
//                case "盖章合同":
//                    docType = "4";
//                    break;
//            }
            //TODO 获取时间类型的数据。

            // 获取关键字
            String keyword = acct_keyword.getText().toString().trim();
            String keywordURLEncoder = URLEncoder.encode(keyword, "utf-8");

//            Intent intent = new Intent(AppDelegate.ACTION_SEARCH_KEYWORD);
            //TODO 在这里我们将关键字和类型集合在一起
//            String ACTION_SEARCH_KEYWORD = "&keyword=" + keywordURLEncoder + "&docType=" + docType; // &keyword=&docType

            String ACYION_KEYWORD="&keyword="+keywordURLEncoder;
//            intent.putExtra(AppDelegate.ACTION_SEARCH_KEYWORD,ACYION_KEYWORD);
//            intent.putExtra(AppDelegate.ACTION_SEARCH_KEYWORD, ACTION_SEARCH_KEYWORD);
            if (!TextUtils.isEmpty(keyword))
//                intent.putExtra(AppDelegate.KEYWORD, keyword);
            LogUtils.d("关键字和类型集合在一起"+ACYION_KEYWORD+":"+keyword);
            // 发送广播，将搜索条件和关键字传递过去，然后请求数据
//            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            finish();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reset, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_reset:
                clearDate();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 清除数据
     */
    private void clearDate() {
        acct_keyword.setText("");
//        acct_type.setText("全部单据");
    }


}
