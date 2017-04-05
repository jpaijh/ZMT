package com.example.ZMTCSD.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 详细的分组信息  （单据中的， 客户基本信息，  往来单位的基本信息  ）
 * 订单中的服务项目   中信保中的限额申请， 水单拆分到订单
 */
public class PropertyGroupsEntity implements Serializable {

    private String groupName;
    private int sequenceNo;
    private List<PropertyInfos> propertyInfos ;

    @Override
    public String toString() {
        return "PropetyGroupsEntity{" +
                "groupName='" + groupName + '\'' +
                ", sequenceNo=" + sequenceNo +
                ", propertyInfos=" + propertyInfos +
                '}';
    }

    public void setGroupName(String groupName){
        this.groupName = groupName;
    }
    public String getGroupName(){
        return this.groupName;
    }
    public void setSequenceNo(int sequenceNo){
        this.sequenceNo = sequenceNo;
    }
    public int getSequenceNo(){
        return this.sequenceNo;
    }
    public void setPropertyInfos(List<PropertyInfos> propertyInfos){
        this.propertyInfos = propertyInfos;
    }
    public List<PropertyInfos> getPropertyInfos(){
        return this.propertyInfos;
    }

    /**
     * PropetyGroups 中的  PropertyInfos
     */
    public  static class PropertyInfos implements Serializable {
        private String propertyName;
        private String propertyValue;
        private int sequenceNo;
        private int importance;

        @Override
        public String toString() {
            return "PropertyInfos{" +
                    "propertyName='" + propertyName + '\'' +
                    ", propertyValue='" + propertyValue + '\'' +
                    ", sequenceNo=" + sequenceNo +
                    ", importance=" + importance +
                    '}';
        }

        public void setPropertyName(String propertyName){
            this.propertyName = propertyName;
        }
        public String getPropertyName(){
            return this.propertyName;
        }
        public void setPropertyValue(String propertyValue){
            this.propertyValue = propertyValue;
        }
        public String getPropertyValue(){
            return this.propertyValue;
        }
        public void setSequenceNo(int sequenceNo){
            this.sequenceNo = sequenceNo;
        }
        public int getSequenceNo(){
            return this.sequenceNo;
        }
        public void setImportance(int importance){
            this.importance = importance;
        }
        public int getImportance(){
            return this.importance;
        }

    }
}
