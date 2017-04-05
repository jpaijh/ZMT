package com.example.ZMTCSD.entity;

/**
 * 订单列表的实体类
 */
public class OrderListEntity {
    /**
     * {
     "id": 3238101,
     "code": "201608250836510001",
     "accountCode": "taiyuan1",
     "accountId": "3123002",
     "accountName": "太原钢铁集团有限公司",
     "declarationAmount": 9000,
     "currency": "USD",
     "createDate": "2016-08-25T08:36:51",
     "status": 2,
     "statusStr": "进行中"
     "serviceId": "1001,1004"
     }
     */

    private int id;

    private String code;

    private String accountCode;

    private String accountId;

    private String accountName;

    private double declarationAmount;

    private String currency;

    private String createDate;

    private int status;

    private String statusStr;

    private String serviceId;

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
    public void setCreateDate(String createDate){
        this.createDate = createDate;
    }
    public String getCreateDate(){
        return this.createDate;
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

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
