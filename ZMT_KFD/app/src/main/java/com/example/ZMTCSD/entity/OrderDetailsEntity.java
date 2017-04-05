package com.example.ZMTCSD.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 订单详细实体类
 */
public class OrderDetailsEntity {
    /**
     * {
     "id": 3214400,
     "code": "201607211120100001",
     "accountCode": "9B98",
     "accountId": "2804200",
     "accountName": "宁波高新区恒拓户外休闲用品有限公司",
     "accountContact": {
     "name": "蒋云峰",
     "phoneNumber": "28900265"
     },
     "declarationAmount": 0,
     "currency": "USD",
     "status": 2,
     "statusStr": "进行中",
     "createDate": "2016-07-21T11:20:10",
     "orderItems": [
     {
     "serviceItem": {
     "name": "一般贸易出口通关",
     "id": 3214600,
     "actions": [
     {
     "id": 11,
     "sequenceNo": 1,
     "actionName": "制单中",
     "creator": null,
     "crrateDate": null,
     "status": 1
     },
     "propertyGroups": [
     {
     "groupName": "相关方信息",
     "sequenceNo": 1,
     "propertyInfos": [
     {
     "propertyName": "开票公司",
     "propertyValue": "",
     "sequenceNo": 0,
     "importance": 0
     },
     {
     "propertyName": "发货人",
     "propertyValue": null,
     "sequenceNo": 1,
     "importance": 0
     },
     {
     "propertyName": "外币付款人",
     "propertyValue": "",
     "sequenceNo": 2,
     "importance": 0
     },
     {
     "propertyName": "通知人",
     "propertyValue": null,
     "sequenceNo": 3,
     "importance": 0
     }
     ]
     },
     "files": [
     {
     "fileId": "3223303",
     "fileName": "QQ20150105-1@2x.png",
     "uploader": "管理员",
     "uploadDate": "2016-08-03T10:47:00",
     "fileUrl": "http://183.129.133.147:18201/core/downloadFile/down?inlineShow=true&fileName=2016/08/03/4479911f-bb7a-4083-9bc3-17203fc045de.png"
     },
     */
    private int id;

    private String code;

    private String accountCode;

    private String accountId;

    private String accountName;

    private ContactEntity accountContact;//电话

    private double declarationAmount; //报关金额

    private String currency;

    private int status;

    private String statusStr;

    private String createDate;

    private List<OrderItems> orderItems;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setCode(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
    public void setAccountCode(String accountCode){
        this.accountCode = accountCode;
    }
    public String getAccountCode(){
        return this.accountCode;
    }
    public void setAccountId(String accountId){
        this.accountId = accountId;
    }
    public String getAccountId(){
        return this.accountId;
    }
    public void setAccountName(String accountName){
        this.accountName = accountName;
    }
    public String getAccountName(){
        return this.accountName;
    }
    public void setAccountContact(ContactEntity accountContact){
        this.accountContact = accountContact;
    }
    public ContactEntity getAccountContact(){
        return this.accountContact;
    }
    public void setDeclarationAmount(double declarationAmount){
        this.declarationAmount = declarationAmount;
    }
    public double getDeclarationAmount(){
        return this.declarationAmount;
    }
    public void setCurrency(String currency){
        this.currency = currency;
    }
    public String getCurrency(){
        return this.currency;
    }
    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }
    public void setStatusStr(String statusStr){
        this.statusStr = statusStr;
    }
    public String getStatusStr(){
        return this.statusStr;
    }
    public void setCreateDate(String createDate){
        this.createDate = createDate;
    }
    public String getCreateDate(){
        return this.createDate;
    }

    public List<OrderItems> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItems> orderItems) {
        this.orderItems = orderItems;
    }
    //订单的服务型
    public static class OrderItems implements Serializable {
        private ServiceItem serviceItem;

        private List<PropertyGroupsEntity> propertyGroups; //详细信息

        private List<FileInfoEntity> files; //文件
        private List<SubGroupList> subGroups; //和并前后的商品

        public void setServiceItem(ServiceItem serviceItem) {
            this.serviceItem = serviceItem;
        }

        public ServiceItem getServiceItem() {
            return this.serviceItem;
        }

        public List<PropertyGroupsEntity> getPropertyGroups() {
            return propertyGroups;
        }

        public void setPropertyGroups(List<PropertyGroupsEntity> propertyGroups) {
            this.propertyGroups = propertyGroups;
        }

        public void setFiles(List<FileInfoEntity> files) {
            this.files = files;
        }

        public List<FileInfoEntity> getFiles() {
            return this.files;
        }

        public List<SubGroupList> getSubGroups() {
            return subGroups;
        }

        public void setSubGroups(List<SubGroupList> subGroups) {
            this.subGroups = subGroups;
        }
    }

    public static class ServiceItem implements Serializable {
        private String name;

        private int id;

        private List<Actions> actions;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public void setActions(List<Actions> actions) {
            this.actions = actions;
        }

        public List<Actions> getActions() {
            return this.actions;
        }
    }
    //订单的服务项状态
    public static class Actions implements Serializable {
        private int id;

        private int sequenceNo;

        private String actionName;

        private String creator;

        private String crrateDate;

        private int status;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public void setSequenceNo(int sequenceNo) {
            this.sequenceNo = sequenceNo;
        }

        public int getSequenceNo() {
            return this.sequenceNo;
        }

        public void setActionName(String actionName) {
            this.actionName = actionName;
        }

        public String getActionName() {
            return this.actionName;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public String getCreator() {
            return this.creator;
        }

        public void setCrrateDate(String crrateDate) {
            this.crrateDate = crrateDate;
        }

        public String getCrrateDate() {
            return this.crrateDate;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return this.status;
        }
    }

    public static class SubGroupList implements Serializable{
        /**
         * "subGroupName": "合并前商品",
         "subGroupSubName": "共1条信息",
         "propertyGroups":
         */
        private String subGroupName;
        private String subGroupSubName;
        private List<PropertyGroupsEntity> propertyGroups; //商品详细信息

        public void setSubGroupName(String subGroupName){
            this.subGroupName = subGroupName;
        }
        public String getSubGroupName(){
            return this.subGroupName;
        }
        public void setSubGroupSubName(String subGroupSubName){
            this.subGroupSubName = subGroupSubName;
        }
        public String getSubGroupSubName(){
            return this.subGroupSubName;
        }

        public List<PropertyGroupsEntity> getPropertyGroups() {
            return propertyGroups;
        }

        public void setPropertyGroups(List<PropertyGroupsEntity> propertyGroups) {
            this.propertyGroups = propertyGroups;
        }
    }


}
