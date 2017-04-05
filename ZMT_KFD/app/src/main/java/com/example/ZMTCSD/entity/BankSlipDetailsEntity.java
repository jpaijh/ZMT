package com.example.ZMTCSD.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 水单详情的实体类
 */
public class BankSlipDetailsEntity implements Serializable {
    /**
     * "id": 3023100,
     "code": "SD2016021536",
     "currency": "USD",
     "netReceiptAmount": 13000,
     "rmbNetReceiptAmount": 84416.8,
     "payer": "UNION",
     "receiptAmount": 13000,
     "domesticCosts": 0,
     "foreignCosts": 0,
     "receiptDate": "2016-01-04T00:00:00",
     "bankRMBRate": 6.4936,
     "accounts": []
     */
    private int id;

    private String code;

    private String currency;

    private double netReceiptAmount;

    private double rmbNetReceiptAmount;

    private String payer;

    private double receiptAmount;

    private double domesticCosts;

    private double foreignCosts;

    private String receiptDate;

    private double bankRMBRate;

    private List<Accounts> accounts ;

    private  double remainingAmount ; //剩余金额

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
    public void setRmbNetReceiptAmount(double rmbNetReceiptAmount){
        this.rmbNetReceiptAmount = rmbNetReceiptAmount;
    }
    public double getRmbNetReceiptAmount(){
        return this.rmbNetReceiptAmount;
    }
    public void setPayer(String payer){
        this.payer = payer;
    }
    public String getPayer(){
        return this.payer;
    }
    public void setReceiptAmount(double receiptAmount){
        this.receiptAmount = receiptAmount;
    }
    public double getReceiptAmount(){
        return this.receiptAmount;
    }
    public void setDomesticCosts(double domesticCosts){
        this.domesticCosts = domesticCosts;
    }
    public double getDomesticCosts(){
        return this.domesticCosts;
    }
    public void setForeignCosts(double foreignCosts){
        this.foreignCosts = foreignCosts;
    }
    public double getForeignCosts(){
        return this.foreignCosts;
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
    public void setAccounts(List<Accounts> accounts){
        this.accounts = accounts;
    }
    public List<Accounts> getAccounts(){
        return this.accounts;
    }

    public double getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(double remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public static class  Accounts implements Serializable{
        /**
         * "id": 3066500,
         "accountId": 2826212,
         "accountCode": "9A17",
         "accountName": "宁波国泰陶瓷有限公司",
         "currency": "USD",
         "claimAmount": 4519,
         "rmbClaimAmount": 29344.58,
         "customerRMBRate": 6.4936,
         "claimDate": "0001-01-01T00:00:00",
         "orders": [
         {
         "id": 3065103,
         "orderId": 3064500,
         "orderCode": "16NUGA60041",
         "accountId": 2826212
         "propertyGroup":
         }
         ]
         */
        private int id;

        private int accountId;

        private String accountCode;

        private String accountName;

        private String currency;

        private double claimAmount;

        private double rmbClaimAmount;

        private double customerRMBRate;

        private String claimDate;

        private List<Orders> orders ;

        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public void setAccountId(int accountId){
            this.accountId = accountId;
        }
        public int getAccountId(){
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
        public void setCurrency(String currency){
            this.currency = currency;
        }
        public String getCurrency(){
            return this.currency;
        }
        public void setClaimAmount(double claimAmount){
            this.claimAmount = claimAmount;
        }
        public double getClaimAmount(){
            return this.claimAmount;
        }
        public void setRmbClaimAmount(double rmbClaimAmount){
            this.rmbClaimAmount = rmbClaimAmount;
        }
        public double getRmbClaimAmount(){
            return this.rmbClaimAmount;
        }
        public void setCustomerRMBRate(double customerRMBRate){
            this.customerRMBRate = customerRMBRate;
        }
        public double getCustomerRMBRate(){
            return this.customerRMBRate;
        }
        public void setClaimDate(String claimDate){
            this.claimDate = claimDate;
        }
        public String getClaimDate(){
            return this.claimDate;
        }
        public void setOrders(List<Orders> orders){
            this.orders = orders;
        }
        public List<Orders> getOrders(){
            return this.orders;
        }
    }

    public static class Orders implements  Serializable{
        private int id;

        private int orderId;

        private String orderCode;

        private int accountId;

        private PropertyGroupsEntity propertyGroup; //水单拆分到订单

        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public void setOrderId(int orderId){
            this.orderId = orderId;
        }
        public int getOrderId(){
            return this.orderId;
        }
        public void setOrderCode(String orderCode){
            this.orderCode = orderCode;
        }
        public String getOrderCode(){
            return this.orderCode;
        }
        public void setAccountId(int accountId){
            this.accountId = accountId;
        }
        public int getAccountId(){
            return this.accountId;
        }

        public PropertyGroupsEntity getPropertyGroup() {
            return propertyGroup;
        }

        public void setPropertyGroup(PropertyGroupsEntity propertyGroup) {
            this.propertyGroup = propertyGroup;
        }
    }

}
