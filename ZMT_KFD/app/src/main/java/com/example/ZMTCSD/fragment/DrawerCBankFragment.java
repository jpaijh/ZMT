package com.example.ZMTCSD.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.adapter.DrawerStatusAdapter;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 *  中信保的银行  侧边栏
 */
@EFragment(R.layout.fragment_drawer_bank)
public class DrawerCBankFragment extends BaseFragment {
    private Context mContext;
    private DrawerStatusAdapter mAdapter;
    private DrawerStatusAdapter mItemAdapter;

    //侧边栏
    @ViewById(R.id.ed_drawer_keyword)
    MaterialEditText mKeyword;

    @ViewById(R.id.tv_drawer_stateName)
    AppCompatTextView stateName;

    @ViewById(R.id.recycler_drawer_itemId)
    RecyclerView mRecyclerItem;

    @ViewById(R.id.recycler_drawer_state)
    RecyclerView mRecyclerState;

    private String keyword = "";
    private String  applyStatus="";
    private String CGSwiftCodeStatus="";
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onAfterViews() {
        stateName.setText("中信保上传状态");
        mRecyclerState.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
        mRecyclerState.setHasFixedSize(true);
        mRecyclerState.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .colorResId(R.color.white).sizeResId(R.dimen.left_16).build());
        mAdapter = new DrawerStatusAdapter(mContext, getsStrings(R.array.buyer_code_status));
        mAdapter.setmOnItemClickListener(new DrawerStatusAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, String name) {
                ConfirmApplyStatus(name);
            }
        });
        mRecyclerState.setAdapter(mAdapter);

        mRecyclerItem.setLayoutManager(new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false));
        mRecyclerItem.setHasFixedSize(true);
        mRecyclerItem.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .colorResId(R.color.white).sizeResId(R.dimen.left_16).build());
        mItemAdapter = new DrawerStatusAdapter(mContext, getsStrings(R.array.buyer_code_item));
        mItemAdapter.setmOnItemClickListener(new DrawerStatusAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, String name) {
                ConFirmCGSwiftStatus(name);
            }
        });
        mRecyclerItem.setAdapter(mItemAdapter);
        super.onAfterViews();
    }
    //    &Keyword=&applyStatus=1&CGSwiftCodeStatus=1
    @Click(R.id.tv_drawer_confirm)
    void ConFirm() {
        try {
            keyword = String.valueOf(mKeyword.getText());
            String keyUTF = URLEncoder.encode(keyword, "UTF-8");
            Intent intent = new Intent(AppDelegate.COMPANY_BANk_DRAWER_SCREEN);
            String str = "&keyword=" + keyUTF + "&applyStatus=" + applyStatus + "&CGSwiftCodeStatus=" + CGSwiftCodeStatus;
            intent.putExtra(AppDelegate.COMPANY_BANk_LIST_SCREEN, str);
            LogUtils.e("中信保银行搜索" + str);
            LocalBroadcastManager.getInstance(mContext).sendBroadcastSync(intent);
            //sendBroadcastSync 与sendBroadCast. 一个同步和异步广播
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.tv_drawer_reset)
    void Reset() {
        mKeyword.setText("");
        mAdapter.ResetPosition();
        mItemAdapter.ResetPosition();
    }

    public void ConfirmApplyStatus(String name){
        switch (name) {
            case "已上传":
                applyStatus ="1";
                break;
            case "未上传":
                applyStatus = "0";
                break;
            default:
                applyStatus = "";
                break;
        }
    }
    public void ConFirmCGSwiftStatus(String name){
        switch (name) {
            case "有":
                CGSwiftCodeStatus ="1";
                break;
            case "无":
                CGSwiftCodeStatus = "0";
                break;
            default:
                CGSwiftCodeStatus = "";
                break;
        }
    }
    @Override
    public void onBackgrounds() {
        super.onBackgrounds();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
