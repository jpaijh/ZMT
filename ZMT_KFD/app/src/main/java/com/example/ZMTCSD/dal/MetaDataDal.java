package com.example.ZMTCSD.dal;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.MyApplication_;
import com.example.ZMTCSD.entity.MetaDataEntity;

import java.util.ArrayList;
import java.util.List;


/**
 *  元数据的操作类
 */

public class MetaDataDal {


    public static String   getMetaData(){
        return MyApplication_.getInstance().getUserInfoSp().getString(AppDelegate.SP_META_DATE,"");
    }
    /**
     * 获取保存在AppDelegate.SP_SERVER_DATE 的服务器集合的信息
     * @return
     */
    public static List<MetaDataEntity> GetListMeta(){
        List<MetaDataEntity> list=new ArrayList<>();
        String jsonString= getMetaData();
        if(!TextUtils.isEmpty(jsonString)){
            list= JSON.parseArray(jsonString.toString() ,MetaDataEntity.class);
        }
        return  list;
    }

    /**
     * 获取serverItem的 value集合
     * @return
     */
    public static List<MetaDataEntity.Value> getServerItemList(){
        List<MetaDataEntity> list=GetListMeta();
        List<MetaDataEntity.Value> valueList=new ArrayList<>();
        for(MetaDataEntity entity: list ){
            if("serviceItem".equals(entity.getKey() )){
                valueList= entity.getValue();
                break;
            }
        }
        return valueList;
    }
    /**
     * 获取serverItem的 value集合 中的serverId 集合
     * @return
     */
    public static List<String> getServerIdList(){
        List<String> listId=new ArrayList<>();
        List<MetaDataEntity.Value> metaList=getServerItemList();
       for(int i=0;i<metaList.size();i++){
           listId.add(metaList.get(i).getServiceItemId());
       }
       return listId;
    }

    /**
     *  获取modulePermission value集合
     * @return
     */
    public static List<MetaDataEntity.Value> getPermissionList(){
        List<MetaDataEntity> list=GetListMeta();
        List<MetaDataEntity.Value> valueList=new ArrayList<>();
        for(MetaDataEntity entity: list ){
            if("modulePermission".equals(entity.getKey() )){
                valueList= entity.getValue();
                break;
            }
        }
        return valueList;
    }
    //获取 modulePermission 中的 moduleId
    public static List<String> GetModuleId(){
        List<String> list=new ArrayList<>();
        List<MetaDataEntity.Value> metaList=getPermissionList();
        for(MetaDataEntity.Value value : metaList){
            list.add(value.getModuleId());
        }
        return  list;
    }
    //获取 modulePermission 中的 moduleName
    public static List<String> GetmoduleName(){
        List<String> list=new ArrayList<>();
        List<MetaDataEntity.Value> metaList=getPermissionList();
        for(MetaDataEntity.Value value : metaList){
            list.add(value.getModuleName());
        }
        return  list;
    }




    //获取所有元数据中的serverid的集合
    public static List<String>  GetMetaServerId(){
        List<String> list=new ArrayList<>();
        List<MetaDataEntity.Value> metaList=getServerItemList();
        for(MetaDataEntity.Value value : metaList){
            list.add(value.getServiceItemId());
        }
        return  list;
    }
    //获取所有元数据中的name的集合
    public static List<String> GetMetaName(){
        List<String> list=new ArrayList<>();
        List<MetaDataEntity.Value> metaList=getServerItemList();
        for(MetaDataEntity.Value value : metaList){
            list.add(value.getName());
        }
        return  list;
    }

    //有serverId 来获取他的 shortName
    public static String GetShortName(String serverId){
        String str="";
        List<MetaDataEntity.Value> metaList=getServerItemList();
        for(MetaDataEntity.Value value : metaList){
            if(serverId.equalsIgnoreCase( value.getServiceItemId()) ){
                str=value.getShortName();
                break;
            }
        }
        return str;
    }

    //有serverId 来获取他的 list中的 下标
    public static int GetListPosition(String serverId){
        List<String> IdList=getServerIdList();
        return IdList.indexOf(serverId);
    }

    //由serverId的集合来 获取对应的name的结合
    public static List<String> GetNameList (List<String>  IdList ){
        List<String> shortList=new ArrayList<>();
        List<MetaDataEntity.Value> metaList=getServerItemList();
        for(MetaDataEntity.Value value : metaList ){
            String id=value.getServiceItemId();
            for(String str : IdList ){
                if(str.equals( id))
                    shortList.add(value.getName());
            }
        }
        return shortList;
    }



    /**
     *  获取value中的第一项中的name
     */
    public static String GetOneName(){
        return getServerItemList().get(0).getName();
    }
    public static String GetOneServerId(){
        return getServerItemList().get(0).getServiceItemId();
    }


}
