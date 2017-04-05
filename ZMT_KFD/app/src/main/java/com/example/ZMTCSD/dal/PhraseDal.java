package com.example.ZMTCSD.dal;

import com.alibaba.fastjson.JSON;
import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.MyApplication_;
import com.example.ZMTCSD.entity.PhraseEntity;
import com.example.ZMTCSD.AppDelegate;
import java.util.ArrayList;
import java.util.List;

//短语的操作类
public class PhraseDal {
    public static List<PhraseEntity>  SetPhraseTop(){
        List<PhraseEntity> list=new ArrayList<>();
        list.add(new PhraseEntity("同意",0));
        list.add(new PhraseEntity("信息准确无误",1));
        list.add(new PhraseEntity("金额信息准确",2));
        list.add(new PhraseEntity("数据很详细",3));
        list.add(new PhraseEntity("审批通过",4));
        return list;
    }
    //将所有服务器的信息保存到 AppDelegate.SP_SERVER_DATE
    public static void SaveListPhraseTop(){
        String jsonString= JSON.toJSONString(SetPhraseTop());
        MyApplication_.getInstance().getUserInfoSp().edit().putString
                (AppDelegate.ACACHE_LISTTOP,jsonString).commit();
    }


    public static List<PhraseEntity>  GetPhraseTop(){
        SaveListPhraseTop();
        String jsonMore= MyApplication_.getInstance().getUserInfoSp().getString(AppDelegate.ACACHE_LISTTOP,"[]");
        if(jsonMore.equals("[]")){
            List<PhraseEntity> phreseTop=new ArrayList<>();
            return phreseTop;
        }else{
            List<PhraseEntity> phreseToplist= JSON.parseArray(jsonMore,PhraseEntity.class);
            LogUtils.i(phreseToplist.toString());
            return phreseToplist;
        }
    }


    public static List<PhraseEntity>  SetPhraseButton(){
        List<PhraseEntity> list=new ArrayList<>();
        list.add(new PhraseEntity("不同意",0));
        list.add(new PhraseEntity("信息有所错误",1));
        list.add(new PhraseEntity("金额信息不准确",2));
        list.add(new PhraseEntity("没有填写附件",3));
        list.add(new PhraseEntity("审批不通过",4));
        return list;
    }
    //将所有服务器的信息保存到 AppDelegate.SP_SERVER_DATE
    public static void SaveListPhraseButton(){
        String jsonString= JSON.toJSONString(SetPhraseButton());
        MyApplication_.getInstance().getUserInfoSp().edit().putString
                (AppDelegate.ACACHE_LISTBUTTON,jsonString).commit();
    }


    public static List<PhraseEntity>  GetPhraseButton(){
        SaveListPhraseButton();
        String jsonMore= MyApplication_.getInstance().getUserInfoSp().getString(AppDelegate.ACACHE_LISTBUTTON,"[]");
        if(jsonMore.equals("[]")){
            List<PhraseEntity> phreseTop=new ArrayList<>();
            return phreseTop;
        }else{
            List<PhraseEntity> phreseToplist= JSON.parseArray(jsonMore,PhraseEntity.class);
            return phreseToplist;
        }
    }

    public static void SetPhraseTopSp(List<PhraseEntity> top){
        String jsonTop=JSON.toJSONString(top);
        MyApplication_.getInstance().getUserInfoSp().edit().putString(AppDelegate.ACACHE_LISTTOP,jsonTop).commit();
    }
    public static void SetPhraseButtonSp(List<PhraseEntity> button){
        String jsonTop=JSON.toJSONString(button);
        MyApplication_.getInstance().getUserInfoSp().edit().putString(AppDelegate.ACACHE_LISTBUTTON,jsonTop).commit();
    }


}
