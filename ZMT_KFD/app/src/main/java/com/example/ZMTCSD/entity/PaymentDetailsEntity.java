package com.example.ZMTCSD.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 付款详情的实体类
 */
public class PaymentDetailsEntity implements Serializable {
    /**
     * {
     "id": 3212201,
     "code": "2016112118177",
     "paymentAmount": 123,
     "settleAmount": 123,
     "status": -1,
     "statusStr": "未申请审批",
     "type": "费用",
     "subType": "海运费",
     "createDate": "2016-07-21T10:12:21",
     "accountName": "宁波高新区恒拓户外休闲用品有限公司",
     "accountId": 2804200,
     "receivingCompany": "宁波高新区恒拓户外休闲用品有限公司",
     "receivingBank": "33101983679049000407",
     "receivingBankAccount": "建设银行宁波市分行",
     "currency": "RMB",
     "rmbRate": 1,
     "rmbAmount": 123,
     "orders": [{
     "id": 3247202,
     "orderCode": "15NUGA1022",
     "paymentAmount": 93.2,
     "settleAmount": 606.17,
     "type": "保险费"
     }]
     }
     */

    private int id;

    private String code;

    private double paymentAmount;

    private double settleAmount;

    private int status;

    private String statusStr;

    private String type;

    private String subType;

    private String createDate;

    private String accountName;

    private int accountId;

    private String receivingCompany;

    private String receivingBank;

    private String receivingBankAccount;

    private String currency;

    private double rmbRate;

    private double rmbAmount;

    private List<Orders> orders ;

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
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
    public void setSubType(String subType){
        this.subType = subType;
    }
    public String getSubType(){
        return this.subType;
    }
    public void setCreateDate(String createDate){
        this.createDate = createDate;
    }
    public String getCreateDate(){
        return this.createDate;
    }
    public void setAccountName(String accountName){
        this.accountName = accountName;
    }
    public String getAccountName(){
        return this.accountName;
    }
    public void setAccountId(int accountId){
        this.accountId = accountId;
    }
    public int getAccountId(){
        return this.accountId;
    }
    public void setReceivingCompany(String receivingCompany){
        this.receivingCompany = receivingCompany;
    }
    public String getReceivingCompany(){
        return this.receivingCompany;
    }
    public void setReceivingBank(String receivingBank){
        this.receivingBank = receivingBank;
    }
    public String getReceivingBank(){
        return this.receivingBank;
    }
    public void setReceivingBankAccount(String receivingBankAccount){
        this.receivingBankAccount = receivingBankAccount;
    }
    public String getReceivingBankAccount(){
        return this.receivingBankAccount;
    }
    public void setCurrency(String currency){
        this.currency = currency;
    }
    public String getCurrency(){
        return this.currency;
    }
    public void setRmbRate(double rmbRate){
        this.rmbRate = rmbRate;
    }
    public double getRmbRate(){
        return this.rmbRate;
    }
    public void setRmbAmount(double rmbAmount){
        this.rmbAmount = rmbAmount;
    }
    public double getRmbAmount(){
        return this.rmbAmount;
    }

    public List<Orders> getOrders() {
        return orders;
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }

    public static class Orders implements  Serializable{
        private int id;

        private String orderCode;

        private double paymentAmount;

        private double settleAmount;

        private String type;

        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public String getOrderCode() {
            return orderCode;
        }
        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
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
        public void setType(String type){
            this.type = type;
        }
        public String getType(){
            return this.type;
        }

    }
}
