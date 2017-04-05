package com.example.ZMTCSD.fragment;

import android.widget.LinearLayout;

import com.example.ZMTCSD.R;
import com.example.ZMTCSD.activity.IApproveActivity_;
import com.example.ZMTCSD.activity.InitiateActivity_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;


/**
 * 利贸通演示是的界面
 */

@EFragment(R.layout.fragment_home_resert)
public class HomeResertFragment extends BaseFragment {

    @ViewById(R.id.ll_sp)
    LinearLayout ll_sp;

    @ViewById(R.id.ll_fq)
    LinearLayout ll_fq;


    @Override
    public void onAfterViews() {
        super.onAfterViews();
    }

    @Click(R.id.ll_sp)
    void GoIApproveActivity(){
        IApproveActivity_.intent(getActivity()).start();
    }

    @Click(R.id.ll_fq)
    void GoInitiate(){
        InitiateActivity_.intent(getActivity()).start();
    }

    @Override
    public void onBackgrounds() {
        super.onBackgrounds();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
