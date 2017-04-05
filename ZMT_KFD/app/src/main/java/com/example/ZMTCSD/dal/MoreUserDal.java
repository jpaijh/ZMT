package com.example.ZMTCSD.dal;

import com.alibaba.fastjson.JSON;
import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.MyApplication_;
import com.example.ZMTCSD.entity.MetaDataEntity;
import com.example.ZMTCSD.entity.MoreUserEntity;
import com.example.ZMTCSD.entity.UserClaimsEntity;
import com.example.ZMTCSD.entity.UserLoginEntity;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by
 * junping on 2016/8/1.
 */
public class MoreUserDal {
    private static final String Identity="identity";
    private static final String UserName="userName";
    private static final String LoginName="loginName";
    private static final String Department="department";
    private static final String AccountName="accountName";
    private static final String AccountId="accountId";
    private static final String TitleId="titleId";
    private static final String TitleName="titleName";
    private static final String ModulePermission="modulePermission";

    /**
     * 获取保存到AppDelegate.SP_LIST_MOREUSERENTITY中的 用户的所有信息。
     */
    public static List<MoreUserEntity> GetMorentity(){
        List<MoreUserEntity> MoreEntity;
        String jsonMore= MyApplication_.getInstance().getUserInfoSp().getString(AppDelegate.SP_LIST_MOREUSERENTITY,"");
        if(jsonMore.equals(""))
            MoreEntity= new ArrayList<>();
        else
           MoreEntity=JSON.parseArray(jsonMore,MoreUserEntity.class);

        return MoreEntity;
    }

    /**
     * 增加一个用户
     * @param more
     */
    public static void SetMoreEntity(MoreUserEntity more ){
        List<MoreUserEntity> MoreEntity=GetMorentity();
        if(MoreEntity.size() !=0 ){
            for(int i=0;i<MoreEntity.size();i++){
                if(! MoreEntity.contains(more) ) {
                    MoreEntity.add(more);
                    break;
                }else{
                    UpdateMoreUser(more.getLoginEntity(), more.getSecond());
                }
            }
        }else{
            MoreEntity.add(more);
        }
        MyApplication_.getInstance().getUserInfoSp().edit().putString
                    (AppDelegate.SP_LIST_MOREUSERENTITY,JSON.toJSONString(MoreEntity)).commit();
    }


    //将用户的信息 根据serverPosition 来进行分类
    public static  List<MoreUserEntity>  SortMoreEntity(int serverPosition){
        List<MoreUserEntity> list=GetMorentity();
        List<MoreUserEntity> listone=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            if(list.get(i).getServerId() == serverPosition){
                listone.add(list.get(i));
            }
        }
        return listone;
    }
    //将用户的信息 根据ServerID 来进行分类
    public static List<MoreUserEntity> SortMoreEntityAndId(String Serverid){
        List<MoreUserEntity> list=GetMorentity();
        List<MoreUserEntity> Morelist=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            if(Serverid.equals( list.get(i).getServerId())){
                Morelist.add(list.get(i));
            }
        }
        return Morelist;
    }

    //删除某个用户
    public static void CleanMoreUser(String name, int position){
        List<MoreUserEntity> list=GetMorentity();
        list.remove(selectMoreUser(name,position));
        MyApplication_.getInstance().getUserInfoSp().edit().putString
                (AppDelegate.SP_LIST_MOREUSERENTITY,JSON.toJSONString(list)).commit();
    }

    //由loginName 和ServerPosition 来获取到是那个用户
    public static int  selectMoreUser(String name, int position){
        List<MoreUserEntity> list=GetMorentity();
        int listposition=-1;
        for(int i=0;i<list.size();i++){
            if(list.get(i).getServerId() ==position && list.get(i).getLoginName().equals(name)){
                listposition=i;
                LogUtils.i(list.get(i).getLoginName()+":"+list.get(i).getServerId());
            }
        }
        return listposition;
    }


    //判断是否为同一个用户
    public static boolean SameMoreUser(MoreUserEntity entity){
       if(GetMoreUser().getLoginName().equals(entity.getLoginName())&&
               GetMoreUser().getServerUrl().equals(entity.getServerUrl()) ){
           return true;
       }
        return false;
    }

    /**
     *  //更新某个用户的token 和时间
     * @param userLoginEntity 请求来的token
     * @param second  当前时间
     */
    public static void UpdateMoreUser(UserLoginEntity userLoginEntity,long second){
        MoreUserEntity more=GetMoreUser();
        more.setLoginEntity(userLoginEntity);
        more.setSecond(second);
        String json=JSON.toJSONString(more);
        MyApplication_.getInstance().getUserInfoSp().edit().putString(AppDelegate.SP_MOREUSERENTITY,json).commit();
    }

    /**
     *  更新某个用户的 用户信息
     * @return
     */
    public static void UpdateMoreUser(List<UserClaimsEntity> claimsList){
        MoreUserEntity more=GetMoreUser();
        more.setClaimsList(claimsList);
        String json=JSON.toJSONString(more);
        MyApplication_.getInstance().getUserInfoSp().edit().putString(AppDelegate.SP_MOREUSERENTITY,json).commit();
    }

    //获取保存的到sp的当前用户的信息
    public static MoreUserEntity GetMoreUser(){
        MoreUserEntity more;
        String jsonString=MyApplication_.getInstance().getUserInfoSp().getString(AppDelegate.SP_MOREUSERENTITY,"");
        if(jsonString.equals("")){
             more=new MoreUserEntity();
        }else{
            more=JSON.parseObject(jsonString,MoreUserEntity.class);
        }
        return  more;
    }


    //获取用户的serverUrl
    public static String GetServerUrl(){
       return  GetMoreUser().getServerUrl();
    }
    //获取用户信息
    public static List<UserClaimsEntity> GetUserClaim(){
        return GetMoreUser().getClaimsList();
    }

    //获取用户的id;
    public static String GetUserClaimsID(){
        return getUserName(GetUserClaim(),Identity);
    }
    //获取用户的 name;
    public static String GetUserClaimsName(){
        return getUserName(GetUserClaim(),UserName);
    }
    //获取用户的 loginName
    public static String GetUserClaimslogin(){
        return getUserName(GetUserClaim(),LoginName);
    }
    //获取用户的 department
    public static String GetUserClaimsDepart(){
        return getUserName(GetUserClaim(),Department);
    }
    //获取用户的权限集合
    public static  List<String> GetUserClaimsPermiss(){
       return getPermission(GetUserClaim(),ModulePermission);
    }


    //由用户的权限来获取用户权限集合 对应的 权限集合
    public static List<MetaDataEntity.Value> GetUserPermissToValue(){
        List<MetaDataEntity.Value> list=new ArrayList<>();
        List<MetaDataEntity.Value> moduleList=MetaDataDal.getPermissionList(); //元数据的权限集合2
        List<String> userPermission=MoreUserDal.GetUserClaimsPermiss(); //用户的权限集合
        for(String permiss: userPermission){
            for(MetaDataEntity.Value value : moduleList){
                if(permiss.equals(value.getModuleId())){
                    list.add(value);
                }
            }
        }
        return list;
    }
    //由用户的权限来获取用户权限 对应的 权限名称
    public static String GetUserPermissToName(){
        String s="";
        List<MetaDataEntity.Value> moduleList=MetaDataDal.getPermissionList(); //元数据的权限集合2
        List<String> userPermission=MoreUserDal.GetUserClaimsPermiss(); //用户的权限集合
        for(String permiss: userPermission){
            for(MetaDataEntity.Value value : moduleList){
                if(permiss.equals(value.getModuleId())){
                    s=value.getModuleName();
                    break;
                }
            }
        }
        return s;
    }




    //获取 access_token
    public static String GetAccessToken(){
        return GetMoreUser().getLoginEntity().getAccess_token();
    }
    //获取 token_type
    public static String GetTokenType(){
        return GetMoreUser().getLoginEntity().getToken_type();
    }

    //获取 expires_in
    public static int GetExpiresin(){
        return GetMoreUser().getLoginEntity().getExpires_in();
    }

    //获取 refresh_token
    public static String GetRefreshToken(){
        return GetMoreUser().getLoginEntity().getRefresh_token();
    }
    //获取登陆的时间
    public static long GetSecond(){
        return GetMoreUser().getSecond();
    }

    //获取服务器的名称
    public static String GetServerName(){
        return ServerDal.GetServerName(GetMoreUser().getServerId());
    }


    //从list集合中获取一个用户 并且保存到sp中
    public static void  RandomMoreUser(){
        MoreUserEntity more=GetMorentity().get(GetMorentity().size()-1);
        MyApplication_.getInstance().getUserInfoSp().edit().
                putString(AppDelegate.SP_MOREUSERENTITY,JSON.toJSONString(more)).commit();
    }



    //获取对应的type的value 不能用来获取（"type": "modulePermission"） 这个字段
    public   static String getUserName(List<UserClaimsEntity> list,String type){
        String Name="";
        for(UserClaimsEntity entity : list){
            if(type.equals(entity.getType())){
                Name=entity.getValue();
            }
        }
        return Name;
    }

    //获取用户的所有的权限
    private  static List<String> getPermission( List<UserClaimsEntity> list,String type){
        List<String> StrList=new ArrayList<>();
        for(UserClaimsEntity entity : list){
            if(type.equals(entity.getType())){
                StrList.add(entity.getValue());
            }
        }
        return StrList;
    }
}