package com.example.ZMTCSD.entity;

import java.io.Serializable;
import java.util.List;

/**
 *  待客提款中的融资账户 订单 的实体类
  */

public class PaymentFinanceOrderEntity implements Serializable{
    /**
     *  "accountName": "天津市云动力科技有限公司",
     "accountCode": "CNTJ0065",
     "orderCode": "16ZMT121132",
     "accountId": 3618402,
     "orderId": 55,
     "paymentAmount": 58956.41,
     "invoiceAmount": 82119,
     paymentAccounts :
     paymentOrder:
     */
    private String accountName;

    private String accountCode;

    private String orderCode;

    private int accountId;

    private int orderId;

    private double paymentAmount;

    private int invoiceAmount;

    private List<PaymentAccountList> paymentAccounts ;

    private PaymentOrder paymentOrder;

    public void setAccountName(String accountName){
        this.accountName = accountName;
    }
    public String getAccountName(){
        return this.accountName;
    }
    public void setAccountCode(String accountCode){
        this.accountCode = accountCode;
    }
    public String getAccountCode(){
        return this.accountCode;
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
    public void setOrderId(int orderId){
        this.orderId = orderId;
    }
    public int getOrderId(){
        return this.orderId;
    }
    public void setPaymentAmount(double paymentAmount){
        this.paymentAmount = paymentAmount;
    }
    public double getPaymentAmount(){
        return this.paymentAmount;
    }
    public void setInvoiceAmount(int invoiceAmount){
        this.invoiceAmount = invoiceAmount;
    }
    public int getInvoiceAmount(){
        return this.invoiceAmount;
    }
    public void setPaymentAccounts(List<PaymentAccountList> paymentAccounts){
        this.paymentAccounts = paymentAccounts;
    }
    public List<PaymentAccountList> getPaymentAccounts(){
        return this.paymentAccounts;
    }
    public void setPaymentOrder(PaymentOrder paymentOrder){
        this.paymentOrder = paymentOrder;
    }
    public PaymentOrder getPaymentOrder(){
        return this.paymentOrder;
    }

    //订单银行的详细信息
    public static class PaymentAccountList implements  Serializable{
        /**
         * "companyId": 3240100,
         "companyName": "江苏乐贝帝家居用品有限公司",
         "bankName": "中国农业银行如东县掘港城南分理处",
         "bankAccount": "10710601040292886"
         */
        private int companyId;
        private String companyName;
        private String bankName;
        private String bankAccount;

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

    }

    public static class PaymentOrder implements Serializable{
        /**
         * "id": 4508620,
         "orderCode": "16ZMT121132",
         "code": "16ZMT121132",
         "currency": "USD",
         "invoiceAmount": 82119, 增票金额
         "paidAmount": 0,   已支付金额
         "unPaidAmount": 58956.41, 未支付金额
         "paymentAmount": 0,  支付金额
         "settleAmount": 58956.41  结算金额
         */
        private int id;
        private String orderCode;
        private String code;
        private String currency;
        private double invoiceAmount;
        private double paidAmount;
        private double unPaidAmount;
        private double paymentAmount;
        private double settleAmount;

        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public void setOrderCode(String orderCode){
            this.orderCode = orderCode;
        }
        public String getOrderCode(){
            return this.orderCode;
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
        public void setInvoiceAmount(double invoiceAmount){
            this.invoiceAmount = invoiceAmount;
        }
        public double getInvoiceAmount(){
            return this.invoiceAmount;
        }
        public void setPaidAmount(double paidAmount){
            this.paidAmount = paidAmount;
        }
        public double getPaidAmount(){
            return this.paidAmount;
        }
        public void setUnPaidAmount(double unPaidAmount){
            this.unPaidAmount = unPaidAmount;
        }
        public double getUnPaidAmount(){
            return this.unPaidAmount;
        }
        public void setPaymentAmount(double paymentAmount){
            this.paymentAmount = paymentAmount;
        }
        public double getPaymentAmount(){
            return this.paymentAmount;
        }
        public void setSettleAmount(double settleAmount){
            this.settleAmount = settleAmount;
        }
        public double getSettleAmount(){
            return this.settleAmount;
        }
    }
}
