package com.example.ZMTCSD.utils;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.ZMTCSD.R;

import static com.example.ZMTCSD.R.id.view;

/**
 * 动画效果的工具类
 */

public class AnimUtils {

    //删除fragment
    public static void removeFragment(FragmentManager fm, String name) {
        if (fm.findFragmentByTag(name) != null) {
            fm.beginTransaction().remove(fm.findFragmentByTag(name)).commit();
        }
    }

    public static void TransactionFragment(FragmentTransaction f) {
        f.setCustomAnimations(
                R.anim.push_left_in,
                R.anim.push_right_out
        );
    }
    public static void TransactionFragmentShow(FragmentTransaction f) {
        f.setCustomAnimations(
                R.anim.push_left_in,
                R.anim.push_left_out,
                R.anim.push_right_in,
                R.anim.push_right_out
        );
    }
    public static void TransactionFragmentHide(FragmentTransaction f) {
        f.setCustomAnimations(
                R.anim.push_right_in,
                R.anim.push_right_out,
                R.anim.push_right_in,
                R.anim.push_right_out
        );
    }


    //view 从中间往右移动，直到全部移出。
    public static void setLoadAnimationLeft(Context mContent, View view){
        Animation animation= AnimationUtils.loadAnimation(mContent,R.anim.push_left_out);
        view.startAnimation(animation);
    }

    //view 从右往中间移动，直到全部移出
    public static void setLoadAnimationRight(Context mContent, View view){
        Animation animation= AnimationUtils.loadAnimation(mContent,R.anim.push_right_in);
        view.startAnimation(animation);
    }

    public static void SwitchBtnAnimShow(Context mContent, View view){
        Animation animation= AnimationUtils.loadAnimation(mContent,R.anim.disappear_bottom_right_in);
        view.startAnimation(animation);
    }
    public static void SwitchBtnAnimHide(Context mContent, View view){
        Animation animation= AnimationUtils.loadAnimation(mContent,R.anim.appear_bottom_right_out);
        view.startAnimation(animation);
    }

}
