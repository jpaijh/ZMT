package com.example.ZMTCSD.entity;

import java.io.Serializable;

/**
 * 中信保银行 实体类
 */

public class CompanyBankApplyEntity  implements Serializable {
    /**
     * {
     "accountId": 4507900,
     "accountName": "霸州市众诚玻璃制品有限公司",
     "companyId": 4508200,
     "companyName": "霸州市众诚玻璃制品有限公司",
     "cgSwiftCode": null,
     "swiftCode": null,
     "bankName": "中国农业银行廊坊市胜芳支行",
     "bankAccount": "50690001040010038",
     "uploadedTime": "2016-12-21T16:27:32",
     "isUploaded": true,
     "status": 0,
     "applyKeyword": "4510900",
     "applyType": 1
     }
     */
    private int accountId;

    private String accountName;

    private int companyId;

    private String companyName;

    private String cgSwiftCode;

    private String swiftCode;

    private String bankName;

    private String bankAccount;

    private String uploadedTime;

    private boolean isUploaded;

    private int status;

    private String applyKeyword;

    private int applyType;

    public void setAccountId(int accountId){
        this.accountId = accountId;
    }
    public int getAccountId(){
        return this.accountId;
    }
    public void setAccountName(String accountName){
        this.accountName = accountName;
    }
    public String getAccountName(){
        return this.accountName;
    }
    public void setCompanyId(int companyId){
        this.companyId = companyId;
    }
    public int getCompanyId(){
        return this.companyId;
    }
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }
    public String getCompanyName(){
        return this.companyName;
    }
    public void setCgSwiftCode(String cgSwiftCode){
        this.cgSwiftCode = cgSwiftCode;
    }
    public String getCgSwiftCode(){
        return this.cgSwiftCode;
    }
    public void setSwiftCode(String swiftCode){
        this.swiftCode = swiftCode;
    }
    public String getSwiftCode(){
        return this.swiftCode;
    }
    public void setBankName(String bankName){
        this.bankName = bankName;
    }
    public String getBankName(){
        return this.bankName;
    }
    public void setBankAccount(String bankAccount){
        this.bankAccount = bankAccount;
    }
    public String getBankAccount(){
        return this.bankAccount;
    }
    public void setUploadedTime(String uploadedTime){
        this.uploadedTime = uploadedTime;
    }
    public String getUploadedTime(){
        return this.uploadedTime;
    }
    public void setIsUploaded(boolean isUploaded){
        this.isUploaded = isUploaded;
    }
    public boolean getIsUploaded(){
        return this.isUploaded;
    }
    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }
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


}
