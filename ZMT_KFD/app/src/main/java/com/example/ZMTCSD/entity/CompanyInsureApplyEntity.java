package com.example.ZMTCSD.entity;

import java.io.Serializable;

/**
 *   中信保的投保 实体类
 */

public class CompanyInsureApplyEntity implements Serializable {
    /**
     *  {
     "applyKeyword": "4001409",
     "applyType": 3,
     "orderId": 4001409,
     "insureCode": null,
     "buyerName": "BILL&FOX INVESTMENT.CO.LTD",
     "orderCode": "16ZMT12299",
     "currency": "USD",
     "declarationAmount": 1,
     "insureAmount": 1,
     "transportDate": "2016-12-16T00:00:00",
     "payTerm": "30",
     "applyStatus": 3,
     "cgStatus": 1,
     "applyStatusStr": "内部待审阅",
     "cgStatusStr": "信保已申请未提交"
     }
     */

    private String applyKeyword;

    private int applyType;

    private int orderId;

    private String insureCode;

    private String buyerName;

    private String orderCode;

    private String currency;

    private int declarationAmount;

    private double insureAmount;

    private String transportDate;

    private String payTerm;

    private int applyStatus;

    private int cgStatus;

    private String applyStatusStr;

    private String cgStatusStr;

    public void setApplyKeyword(String applyKeyword){
        this.applyKeyword = applyKeyword;
    }
    public String getApplyKeyword(){
        return this.applyKeyword;
    }
    public void setApplyType(int applyType){
        this.applyType = applyType;
    }
    public int getApplyType(){
        return this.applyType;
    }
    public void setOrderId(int orderId){
        this.orderId = orderId;
    }
    public int getOrderId(){
        return this.orderId;
    }
    public void setInsureCode(String insureCode){
        this.insureCode = insureCode;
    }
    public String getInsureCode(){
        return this.insureCode;
    }
    public void setBuyerName(String buyerName){
        this.buyerName = buyerName;
    }
    public String getBuyerName(){
        return this.buyerName;
    }
    public void setOrderCode(String orderCode){
        this.orderCode = orderCode;
    }
    public String getOrderCode(){
        return this.orderCode;
    }
    public void setCurrency(String currency){
        this.currency = currency;
    }
    public String getCurrency(){
        return this.currency;
    }
    public void setDeclarationAmount(int declarationAmount){
        this.declarationAmount = declarationAmount;
    }
    public int getDeclarationAmount(){
        return this.declarationAmount;
    }
    public void setInsureAmount(double insureAmount){
        this.insureAmount = insureAmount;
    }
    public double getInsureAmount(){
        return this.insureAmount;
    }
    public void setTransportDate(String transportDate){
        this.transportDate = transportDate;
    }
    public String getTransportDate(){
        return this.transportDate;
    }
    public void setPayTerm(String payTerm){
        this.payTerm = payTerm;
    }
    public String getPayTerm(){
        return this.payTerm;
    }
    public void setApplyStatus(int applyStatus){
        this.applyStatus = applyStatus;
    }
    public int getApplyStatus(){
        return this.applyStatus;
    }

    public int getCgStatus() {
        return cgStatus;
    }

    public void setCgStatus(int cgStatus) {
        this.cgStatus = cgStatus;
    }

    public void setApplyStatusStr(String applyStatusStr){
        this.applyStatusStr = applyStatusStr;
    }
    public String getApplyStatusStr(){
        return this.applyStatusStr;
    }

    public String getCgStatusStr() {
        return cgStatusStr;
    }

    public void setCgStatusStr(String cgStatusStr) {
        this.cgStatusStr = cgStatusStr;
    }
}
