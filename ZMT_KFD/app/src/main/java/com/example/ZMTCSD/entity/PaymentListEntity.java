package com.example.ZMTCSD.entity;

import java.io.Serializable;

/**
 * 付款列表的实体类
 */
public class PaymentListEntity implements Serializable{
/**
 * {
 "id": 3240402,
 "code": "201651259218",
 "accountName": "太原钢铁集团有限公司",
 "accountId": 3123002,
 "currency": "RMB",
 "paymentAmount": 290.16,
 "createDate": "2016-08-25T08:51:59",
 "status": 2,
 "statusStr": "审批同意",
 "type": "费用",
 "subType": "中信保保费"
 }
 */

    private int id;

    private String code;

    private String accountName;

    private int accountId;

    private String currency;

    private double paymentAmount;

    private String createDate;

    private int status;

    private String statusStr;

    private String type;

    private String subType;

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
    public void setAccountName(String accountName){
        this.accountName = accountName;
    }
    public String getAccountName(){
        return this.accountName;
    }
    public void setAccountId(int accountId){
        this.accountId = accountId;
    }
    public int getAccountId(){
        return this.accountId;
    }
    public void setCurrency(String currency){
        this.currency = currency;
    }
    public String getCurrency(){
        return this.currency;
    }
    public void setPaymentAmount(double paymentAmount){
        this.paymentAmount = paymentAmount;
    }
    public double getPaymentAmount(){
        return this.paymentAmount;
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
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
    public void setSubType(String subType){
        this.subType = subType;
    }
    public String getSubType(){
        return this.subType;
    }
}
