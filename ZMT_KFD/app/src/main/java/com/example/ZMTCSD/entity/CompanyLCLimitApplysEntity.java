package com.example.ZMTCSD.entity;

import java.io.Serializable;

/**
 *   中信保的限额 实体类
 */

public class CompanyLCLimitApplysEntity implements Serializable {
    /**
     * {
     "id": 4860201,
     "name": "BICOMPANY CO.,LTD.",
     "companyId": 3837126,
     "buyerCode": "KOR/763869",
     "applyKeyword": "3837126",
     "applyType": 2,
     "hasBuyerCode": true,
     "lcId": 4860201,
     "lcCode": null,
     "lcTypeStr": "非LC",
     "lcLimitStr": "额度: $ 1,000,000.00;余额 $ 367,040.00",
     "lcType": 0,
     "lcLimit": 0,
     "lcLimitBalance": 367040,
     "cgStatus": 2,
     "validTime": null,
     "inValidTime": null
     }
     */
    private int id;

    private String name;

    private int companyId;

    private String buyerCode;

    private String applyKeyword;

    private int applyType;

    private boolean hasBuyerCode;

    private int lcId;

    private String lcCode;

    private String lcTypeStr;

    private String lcLimitStr;

    private int lcType;

    private double lcLimit;
    private double lcLimitBalance;

    private int cgStatus;

    private String validTime;

    private String inValidTime;


    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
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
    public void setHasBuyerCode(boolean hasBuyerCode){
        this.hasBuyerCode = hasBuyerCode;
    }
    public boolean getHasBuyerCode(){
        return this.hasBuyerCode;
    }
    public void setLcId(int lcId){
        this.lcId = lcId;
    }
    public int getLcId(){
        return this.lcId;
    }
    public void setLcCode(String lcCode){
        this.lcCode = lcCode;
    }
    public String getLcCode(){
        return this.lcCode;
    }
    public void setLcTypeStr(String lcTypeStr){
        this.lcTypeStr = lcTypeStr;
    }
    public String getLcTypeStr(){
        return this.lcTypeStr;
    }
    public void setLcType(int lcType){
        this.lcType = lcType;
    }
    public int getLcType(){
        return this.lcType;
    }
    public void setLcLimit(double lcLimit){
        this.lcLimit = lcLimit;
    }
    public double getLcLimit(){
        return this.lcLimit;
    }

    public double getLcLimitBalance() {
        return lcLimitBalance;
    }

    public void setLcLimitBalance(double lcLimitBalance) {
        this.lcLimitBalance = lcLimitBalance;
    }

    public boolean isHasBuyerCode() {
        return hasBuyerCode;
    }

    public String getLcLimitStr() {
        return lcLimitStr;
    }

    public void setLcLimitStr(String lcLimitStr) {
        this.lcLimitStr = lcLimitStr;
    }

    public int getCgStatus() {
        return cgStatus;
    }

    public void setCgStatus(int cgStatus) {
        this.cgStatus = cgStatus;
    }

    public void setValidTime(String validTime){
        this.validTime = validTime;
    }
    public String getValidTime(){
        return this.validTime;
    }
    public void setInValidTime(String inValidTime){
        this.inValidTime = inValidTime;
    }
    public String getInValidTime(){
        return this.inValidTime;
    }
}
