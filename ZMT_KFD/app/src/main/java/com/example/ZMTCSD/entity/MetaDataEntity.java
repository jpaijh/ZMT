package com.example.ZMTCSD.entity;

import java.io.Serializable;
import java.util.List;

/**
 *  元数据
 * Created by 汤军平 on 2016/10/20.
 */

public class MetaDataEntity implements Serializable {
    /**
     * [
     {
     "key": "serviceItem",
     "value": [
     {
     "serviceItemId": 1001,
     "name": "一般贸易出口通关",
     "shortName": "出",
     "description": "一般贸易出口通关",
     "type": 1
     },
     {
     "serviceItemId": 1004,
     "name": "退税融资",
     "shortName": "退",
     "description": "退税融资",
     "type": 1
     },
     {
     "serviceItemId": 1005,
     "name": "赊销融资",
     "shortName": "赊",
     "description": "货物报关出口后垫付最高80%应收账款。 提前回笼资金，改善现金流，助您开拓出口赊销市场。",
     "type": 1
     },
     {
     "serviceItemId": 1007,
     "name": "货代订舱",
     "shortName": "订",
     "description": "货代订舱",
     "type": 1
     },
     {
     "serviceItemId": 1010,
     "name": "资信调查",
     "shortName": "资",
     "description": "资信调查服务,中信保调查外商",
     "type": 1
     },
     {
     "serviceItemId": 1020,
     "name": "中信保投保",
     "shortName": "保",
     "description": "中信保投保",
     "type": 1
     },
     {
     "serviceItemId": 2001,
     "name": "进口服务",
     "shortName": "进",
     "description": "进口服务",
     "type": 2
     }
     ]
     }
     ]
     */
    private String key;

    private List<Value> value ;

    @Override
    public String toString() {
        return "MetaDataEntity{" +
                "key='" + key + '\'' +
                ", value=" + value +
                '}';
    }

    public void setKey(String key){
        this.key = key;
    }
    public String getKey(){
        return this.key;
    }
    public void setValue(List<Value> value){
        this.value = value;
    }
    public List<Value> getValue(){
        return this.value;
    }

    public class Value implements Serializable{

        private String moduleId;

        private String permissionName;

        private String moduleName;

        public String getModuleId() {
            return moduleId;
        }

        public void setModuleId(String moduleId) {
            this.moduleId = moduleId;
        }

        public String getPermissionName() {
            return permissionName;
        }

        public void setPermissionName(String permissionName) {
            this.permissionName = permissionName;
        }

        public String getModuleName() {
            return moduleName;
        }

        public void setModuleName(String moduleName) {
            this.moduleName = moduleName;
        }

        //下面是 "key": "serviceItem", 里面的参数
        private String serviceItemId;

        private String name;

        private String shortName;

        private String description;

        private String Type;

        public void setServiceItemId(String serviceItemId){
            this.serviceItemId = serviceItemId;
        }
        public String getServiceItemId(){
            return this.serviceItemId;
        }
        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }
        public void setShortName(String shortName){
            this.shortName = shortName;
        }
        public String getShortName(){
            return this.shortName;
        }
        public void setDescription(String description){
            this.description = description;
        }
        public String getDescription(){
            return this.description;
        }

        public String getType() {
            return Type;
        }

        public void setType(String type) {
            Type = type;
        }
    }

}
