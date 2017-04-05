package com.example.ZMTCSD.entity;

import java.io.Serializable;
import java.util.List;

/**
 *  待客提款时提交成功时的 实体类
 */

public class PaymentAddApplyEntity implements Serializable{
    public PaymentAddApplyEntity() {
    }
    /**
     * {
     "accountId": 3143903,
     "accountCode": "CNZJ0002",
     "accountName": "永康市华达恒工贸有限公司",
     "PaymentMethodId": "183",
     "PaymentMethodName": "电汇",
     "PaymentTypeId": "0-1196403",
     "PaymentTypeName": "货款",
     "GuaranteeTypeId": "1010",
     "GuaranteeTypeName": "无",
     "companyName": "武义保罗户外用品有限公司",
     "companyId": 3542604,
     "bankName": "浙江武义农村合作银行白洋支行",
     "bankAccount": "201000150420138",
     "currency": "RMB",
     "RMBRate": 1,
     "paymentAmount": 2,
     "settleAmount": 3,
     "fundSource": 1,
     "paymentOrders": [
     {
     "settleAmount": 3,
     "id": 3709417,
     "paymentAmount": 2,
     "orderCode": "16ZMT12124"
     }
     ]
     }
     */
    private int accountId;  //选择客户时的id
    private String accountCode;//选择客户时的code
    private String accountName;//选择客户时的名称

    private String PaymentMethodId; //付款方式的id
    private String PaymentMethodName; //付款方式的名称

    private String PaymentTypeName; //付款的类型子类型 的名称
    private String PaymentTypeId; //付款的类型中的子类型的id

    private String GuaranteeTypeId; //担保方式的id
    private String GuaranteeTypeName;//担保方式的 名称

    private String companyName; //收款单位的名称
    private int companyId; //收款单位的id
    private String bankName; //提款银行的 名称
    private String bankAccount;  //提款银行的账号

    private String currency; //币别
    private String RMBRate;  //汇率
    private String PaymentAmount;  //支付金额
    private String SettleAmount;  //结算金额

    private int fundSource; //资金来源
    private List<PaymentOrderList> paymentOrders ;

    @Override
    public String toString() {
        return "PaymentAddApplyEntity{" +
                "accountId=" + accountId +
                ", accountCode='" + accountCode + '\'' +
                ", accountName='" + accountName + '\'' +
                ", PaymentMethodId='" + PaymentMethodId + '\'' +
                ", PaymentMethodName='" + PaymentMethodName + '\'' +
                ", PaymentTypeName='" + PaymentTypeName + '\'' +
                ", PaymentTypeId='" + PaymentTypeId + '\'' +
                ", GuaranteeTypeId='" + GuaranteeTypeId + '\'' +
                ", GuaranteeTypeName='" + GuaranteeTypeName + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyId=" + companyId +
                ", bankName='" + bankName + '\'' +
                ", bankAccount='" + bankAccount + '\'' +
                ", currency='" + currency + '\'' +
                ", RMBRate='" + RMBRate + '\'' +
                ", PaymentAmount='" + PaymentAmount + '\'' +
                ", SettleAmount='" + SettleAmount + '\'' +
                ", fundSource=" + fundSource +
                ", paymentOrders=" + paymentOrders +
                '}';
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPaymentMethodId() {
        return PaymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        PaymentMethodId = paymentMethodId;
    }

    public String getPaymentMethodName() {
        return PaymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        PaymentMethodName = paymentMethodName;
    }

    public String getPaymentTypeName() {
        return PaymentTypeName;
    }

    public void setPaymentTypeName(String paymentTypeName) {
        PaymentTypeName = paymentTypeName;
    }

    public String getPaymentTypeId() {
        return PaymentTypeId;
    }

    public void setPaymentTypeId(String paymentTypeId) {
        PaymentTypeId = paymentTypeId;
    }

    public String getGuaranteeTypeName() {
        return GuaranteeTypeName;
    }

    public void setGuaranteeTypeName(String guaranteeTypeName) {
        GuaranteeTypeName = guaranteeTypeName;
    }

    public String getGuaranteeTypeId() {
        return GuaranteeTypeId;
    }

    public void setGuaranteeTypeId(String guaranteeTypeId) {
        GuaranteeTypeId = guaranteeTypeId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getPaymentAmount() {
        return PaymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        PaymentAmount = paymentAmount;
    }

    public String getRMBRate() {
        return RMBRate;
    }

    public void setRMBRate(String RMBRate) {
        this.RMBRate = RMBRate;
    }

    public String getSettleAmount() {
        return SettleAmount;
    }

    public void setSettleAmount(String settleAmount) {
        SettleAmount = settleAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getFundSource() {
        return fundSource;
    }

    public void setFundSource(int fundSource) {
        this.fundSource = fundSource;
    }

    public List<PaymentOrderList> getPaymentOrders() {
        return paymentOrders;
    }

    public void setPaymentOrders(List<PaymentOrderList> paymentOrders) {
        this.paymentOrders = paymentOrders;
    }

    public static class PaymentOrderList implements Serializable{

        public PaymentOrderList( int id, String orderCode, double paymentAmount,double settleAmount) {
            this.settleAmount = settleAmount;
            this.id = id;
            this.paymentAmount = paymentAmount;
            this.orderCode = orderCode;
        }

        @Override
        public String toString() {
            return "PaymentOrderList{" +
                    "settleAmount=" + settleAmount +
                    ", id=" + id +
                    ", paymentAmount=" + paymentAmount +
                    ", orderCode='" + orderCode + '\'' +
                    '}';
        }

        /**
         * {"settleAmount":6,
         * "id":3975037,
         * "paymentAmount":5,
         * "orderCode":"16ZMT12227B"}
         */

        private double settleAmount;
        private int id;
        private double paymentAmount;
        private String orderCode;

        public double getSettleAmount() {
            return settleAmount;
        }

        public void setSettleAmount(double settleAmount) {
            this.settleAmount = settleAmount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getPaymentAmount() {
            return paymentAmount;
        }

        public void setPaymentAmount(double paymentAmount) {
            this.paymentAmount = paymentAmount;
        }

        public String getOrderCode() {
            return orderCode;
        }

        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }
    }
}
