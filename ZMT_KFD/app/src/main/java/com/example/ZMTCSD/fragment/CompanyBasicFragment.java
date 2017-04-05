package com.example.ZMTCSD.fragment;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.ZMTCSD.R;
import com.example.ZMTCSD.adapter.MyPropetyGroupsAdapter;
import com.example.ZMTCSD.entity.PropertyGroupsEntity;
import com.example.ZMTCSD.AppDelegate;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * 往来单位详细信息中的 基本信息
 */
@EFragment(R.layout.my_basic_recycler)
public class CompanyBasicFragment extends BaseFragment {
    private Context mContext;
    private MyPropetyGroupsAdapter mGroupAdapter;
    private List<PropertyGroupsEntity> mGroupsList;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.recyclerView)
    RecyclerView mDetailsRecycler;

    @ViewById(R.id.tv_no_data)
    AppCompatTextView tv_no_data;

    @Override
    public void onAfterViews() {
        hideView(mToolbar);
        mContext = getActivity();
        mGroupsList = (List<PropertyGroupsEntity>) getArguments().getSerializable(AppDelegate.CUSTOMER_BASIC_COMPANY);
        mDetailsRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mDetailsRecycler.setHasFixedSize(true);
        if (mGroupsList != null && mGroupsList.size() != 0) {
            mGroupAdapter = new MyPropetyGroupsAdapter(mContext, mGroupsList);
            mDetailsRecycler.setAdapter(mGroupAdapter);
        } else {
            showView(tv_no_data);
        }
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
