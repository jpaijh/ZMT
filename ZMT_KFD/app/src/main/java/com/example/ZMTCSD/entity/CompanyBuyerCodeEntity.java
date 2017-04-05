package com.example.ZMTCSD.entity;

import java.io.Serializable;

/**
 * 中信保的外商代码 实体类
 */

public class CompanyBuyerCodeEntity  implements Serializable{
    /**
     *  {
     "name": "SHENZHEN BESSKY TECHNOLOGY CO",
     "country": "加拿大",
     "companyId": 4851410,
     "buyerCode": null,
     "applyKeyword": "4851410",
     "applyType": 0,
     "uploadedTime": null,
     "hasBuyerCode": false,
     "isUploaded": false
     "cgStatus": 0
     }
     */
    private String name;

    private String country;

    private int companyId;

    private String buyerCode;

    private String applyKeyword;

    private int applyType;

    private String uploadedTime;

    private boolean hasBuyerCode;

    private boolean isUploaded;
    private int cgStatus;

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setCountry(String country){
        this.country = country;
    }
    public String getCountry(){
        return this.country;
    }
    public void setCompanyId(int companyId){
        this.companyId = companyId;
    }
    public int getCompanyId(){
        return this.companyId;
    }
    public void setBuyerCode(String buyerCode){
        this.buyerCode = buyerCode;
    }
    public String getBuyerCode(){
        return this.buyerCode;
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
    public void setUploadedTime(String uploadedTime){
        this.uploadedTime = uploadedTime;
    }
    public String getUploadedTime(){
        return this.uploadedTime;
    }
    public void setHasBuyerCode(boolean hasBuyerCode){
        this.hasBuyerCode = hasBuyerCode;
    }
    public boolean getHasBuyerCode(){
        return this.hasBuyerCode;
    }
    public void setIsUploaded(boolean isUploaded){
        this.isUploaded = isUploaded;
    }
    public boolean getIsUploaded(){
        return this.isUploaded;
    }

    public boolean isHasBuyerCode() {
        return hasBuyerCode;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    public int getCgStatus() {
        return cgStatus;
    }

    public void setCgStatus(int cgStatus) {
        this.cgStatus = cgStatus;
    }
}
