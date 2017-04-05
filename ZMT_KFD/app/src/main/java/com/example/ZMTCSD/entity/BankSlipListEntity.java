package com.example.ZMTCSD.entity;

import java.io.Serializable;

/**
 * 水单的列表实体类
 */
public class BankSlipListEntity implements Serializable {
    /**
     *  {
     "id": 3023100,
     "code": "SD2016021536",
     "payer": "UNION",
     "currency": "USD",
     "netReceiptAmount": 13000,
     "receiptDate": "2016-01-04T00:00:00",
     "bankRMBRate": 6.4936,
     "leftClaimAmount": 8381,
     "accountId": null,
     "accountCode": "宁波国泰陶瓷有限公司 委托方1 ",
     "accountName": null,
     "claimedAmount": 13000,
     "customerRMBRate": 6.4936,
     "claimedDate": "2016-02-04T10:11:41"
     }
     */
    private int id;

    private String code;

    private String payer;

    private String currency;

    private double netReceiptAmount;

    private String receiptDate;

    private double bankRMBRate;

    private double leftClaimAmount;

    private String accountId;

    private String accountCode;

    private String accountName;

    private double claimedAmount;

    private double customerRMBRate;

    private String claimedDate;

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
    public void setPayer(String payer){
        this.payer = payer;
    }
    public String getPayer(){
        return this.payer;
    }
    public void setCurrency(String currency){
        this.currency = currency;
    }
    public String getCurrency(){
        return this.currency;
    }
    public void setNetReceiptAmount(double netReceiptAmount){
        this.netReceiptAmount = netReceiptAmount;
    }
    public double getNetReceiptAmount(){
        return this.netReceiptAmount;
    }
    public void setReceiptDate(String receiptDate){
        this.receiptDate = receiptDate;
    }
    public String getReceiptDate(){
        return this.receiptDate;
    }
    public void setBankRMBRate(double bankRMBRate){
        this.bankRMBRate = bankRMBRate;
    }
    public double getBankRMBRate(){
        return this.bankRMBRate;
    }
    public void setLeftClaimAmount(double leftClaimAmount){
        this.leftClaimAmount = leftClaimAmount;
    }
    public double getLeftClaimAmount(){
        return this.leftClaimAmount;
    }
    public void setAccountId(String accountId){
        this.accountId = accountId;
    }
    public String getAccountId(){
        return this.accountId;
    }
    public void setAccountCode(String accountCode){
        this.accountCode = accountCode;
    }
    public String getAccountCode(){
        return this.accountCode;
    }
    public void setAccountName(String accountName){
        this.accountName = accountName;
    }
    public String getAccountName(){
        return this.accountName;
    }
    public void setClaimedAmount(double claimedAmount){
        this.claimedAmount = claimedAmount;
    }
    public double getClaimedAmount(){
        return this.claimedAmount;
    }
    public void setCustomerRMBRate(double customerRMBRate){
        this.customerRMBRate = customerRMBRate;
    }
    public double getCustomerRMBRate(){
        return this.customerRMBRate;
    }
    public void setClaimedDate(String claimedDate){
        this.claimedDate = claimedDate;
    }
    public String getClaimedDate(){
        return this.claimedDate;
    }


}
