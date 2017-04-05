package com.example.ZMTCSD.entity;

import java.io.Serializable;

/**
 *  往来单位的银行的实体类 和待客提款的 收款银行一样
 */
public class CompanyBanksEntity implements Serializable {
    /**
     *  "id": 3218102,
     "bankName": "开户银行",
     "bankAccount": "银行账号",
     "accountName": "小温",
     "swiftCode": "4343",
     "status": 2,
     "statusStr": "已审核",
     "companyId": 3219000,
     "companyName": "小温"
     */
    private int id;

    private String bankName;

    private String bankAccount;

    private String accountName;

    private String swiftCode;

    private int status;

    private String statusStr;

    private int companyId;

    private String companyName;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
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
    public void setAccountName(String accountName){
        this.accountName = accountName;
    }
    public String getAccountName(){
        return this.accountName;
    }
    public void setSwiftCode(String swiftCode){
        this.swiftCode = swiftCode;
    }
    public String getSwiftCode(){
        return this.swiftCode;
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
}
