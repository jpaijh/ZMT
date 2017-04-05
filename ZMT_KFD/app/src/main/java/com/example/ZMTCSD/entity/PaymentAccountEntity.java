package com.example.ZMTCSD.entity;

import java.io.Serializable;
import java.util.List;

/**
 *  待客提款 选择客户后 获取的客户资金 实体类
 */

public class PaymentAccountEntity implements Serializable {
    /**
     *  "accountName": "天津市云动力科技有限公司",
     "accountCode": "CNTJ0065",
     "accountId": 3618402,
     "amount": 2602186.6,
     "financialAmount": 143004.66,
     "defaultCompanyId": "3618704",
     "defaultCompanyName": "天津市云动力科技有限公司",
     "propertyItems": [
     {
     "propertyName": "客户编码",
     "propertyValue": "CNTJ0145",
     "sequenceNo": 0,
     "importance": 0
     }
     ]
     */

    private String accountName;
    private String accountCode;
    private int accountId;
    private  double amount;
    private double financialAmount;
    private String defaultCompanyId;
    private String defaultCompanyName;

    private List<PropertyItemList> propertyItems ;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getFinancialAmount() {
        return financialAmount;
    }

    public void setFinancialAmount(double financialAmount) {
        this.financialAmount = financialAmount;
    }

    public String getDefaultCompanyId() {
        return defaultCompanyId;
    }

    public void setDefaultCompanyId(String defaultCompanyId) {
        this.defaultCompanyId = defaultCompanyId;
    }

    public String getDefaultCompanyName() {
        return defaultCompanyName;
    }

    public void setDefaultCompanyName(String defaultCompanyName) {
        this.defaultCompanyName = defaultCompanyName;
    }

    public List<PropertyItemList> getPropertyItems() {
        return propertyItems;
    }

    public void setPropertyItems(List<PropertyItemList> propertyItems) {
        this.propertyItems = propertyItems;
    }

    public static class PropertyItemList implements Serializable{
        private String propertyName;
        private String propertyValue;
        private int sequenceNo;
        private int importance;

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
