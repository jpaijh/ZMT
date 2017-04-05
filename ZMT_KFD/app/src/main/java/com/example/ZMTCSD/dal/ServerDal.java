package com.example.ZMTCSD.dal;

import com.alibaba.fastjson.JSON;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.MyApplication_;
import com.example.ZMTCSD.entity.ServerDeployEntity;
import java.util.ArrayList;
import java.util.List;
public class ServerDal {

    /**
     * 配置服务器的信息
     * @return
     */
    public  static  List<ServerDeployEntity>  SetServerDeploy (){
        List<ServerDeployEntity> list=new ArrayList<>();
        list.add(new ServerDeployEntity(0,"ZMT" ,"自贸通", "自贸通服务器" ,AppDelegate.BASE_URL));
//        list.add(new ServerDeployEntity(0,"ZMT" ,"自贸通", "自贸通服务器" , " https://api.tjlimaotong.com:10086"));
        return list;
    }

    /**
     *  将所有服务器的信息保存到 AppDelegate.SP_SERVER_DATE
     */
    public static void SaveListServer(){
        String jsonString= JSON.toJSONString(SetServerDeploy());
        MyApplication_.getInstance().getUserInfoSp().edit().putString
                (AppDelegate.SP_SERVER_DATE,jsonString).commit();
    }

    /**
     * 获取保存在AppDelegate.SP_SERVER_DATE 的服务器集合的信息
     * @return
     */
    public static List<ServerDeployEntity> GetListServer(){
        String jsonString= MyApplication_.getInstance().getUserInfoSp().getString(AppDelegate.SP_SERVER_DATE,"");
//        LogUtils.e("bcu服务器"+jsonString);
        List<ServerDeployEntity> list=JSON.parseArray(jsonString.toString() ,ServerDeployEntity.class);
        return  list;
    }
    /**
     * 从保存在服务器集合信息中获取所有服务器的名称的 数组
     */
    public static String[] GetServerName(){
        List<String> stringList=new ArrayList<>();
        List<ServerDeployEntity> list=GetListServer();
        for(int i=0;i<list.size() ;i++){
            stringList.add( list.get(i).getServerName() );
        }
        final int size = stringList.size();
        String[] arr = stringList.toArray(new String[size]);
        return  arr;
    }

    /**
     *  从保存到服务器list集合中获取其中一个服务器
     *  根据position
     */
    public static ServerDeployEntity GetServerPosition(int position){
        return GetListServer().get(position);
    }

    //获取其中服务器的服务器地址
    public static String GetServerUrl(int position){
        return GetServerPosition(position).getServerAddress();
    }
    //获取其中服务器的服务器名称
    public static String GetServerName(int position){
        return GetServerPosition(position).getServerName();
    }
    //获取其中服务器的服务器id
    public static int GetServerId(int position){
        return GetServerPosition(position).getServerId();
    }
    //获取其中服务器的服务器简称
    public static String GetServerCode(int position){
        return GetServerPosition(position).getServerCode();
    }
}
