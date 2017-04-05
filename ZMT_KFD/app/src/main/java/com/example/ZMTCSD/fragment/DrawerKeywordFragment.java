package com.example.ZMTCSD.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bigkoo.pickerview.TimePickerView;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.adapter.DrawerStatusAdapter;
import com.example.ZMTCSD.utils.DateUtil;
import com.example.ZMTCSD.utils.DeviceUtil;
import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.github.ikidou.fragmentBackHandler.FragmentBackHandler;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.BreakIterator;
import java.util.Date;


/**
 *   关键字侧边栏
 */
@EFragment(R.layout.fragment_drawer_keyword)
public class DrawerKeywordFragment extends BaseFragment {
    private Context mContext;
    private String selectType;
    //侧边栏
    @ViewById(R.id.ed_drawer_keyword)
    MaterialEditText mKeyword;

    private String keyword = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onAfterViews() {
        this.mContext=getActivity();
        selectType= getArguments().getString(AppDelegate.PYNEW_SELECT_TYPE);
        super.onAfterViews();
    }

    @Click(R.id.tv_drawer_confirm)
    void ConFirm(){
        try {
            keyword= String.valueOf(mKeyword.getText());
            String keyUTF = URLEncoder.encode(keyword,"UTF-8");

            Intent intent = new Intent(AppDelegate.PAYMENT_NEW_SELECTOR);
            String  str="&keyword="+keyUTF;
            intent.putExtra(AppDelegate.PAYMENT_LIST_SCREEN,str);
            intent.putExtra(AppDelegate.PYNEW_SELECT_TYPE,selectType);
            LocalBroadcastManager.getInstance(mContext).sendBroadcastSync(intent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }



    @Click(R.id.tv_drawer_reset)
    void Reset(){
        mKeyword.setText("");
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
