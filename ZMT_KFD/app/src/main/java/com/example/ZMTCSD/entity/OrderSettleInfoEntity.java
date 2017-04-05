package com.example.ZMTCSD.entity;

import java.io.Serializable;

/**
 * 订单中的收支状态的实体类
 */
public class OrderSettleInfoEntity implements Serializable{
    /**{
     "orderId": 3124601,
     "accountId": 3123002,
     "receiptAmount": 1000000,
     "rebateAmount": 46250,
     "payAmount": 1000000,
     "costAmount": 2000,
     "profitAmount": 44250
     }
     */
    private int orderId;

    private int accountId;

    private double receiptAmount;

    private double rebateAmount;

    private double payAmount;

    private double costAmount;

    private double profitAmount;

    public void setOrderId(int orderId){
        this.orderId = orderId;
    }
    public int getOrderId(){
        return this.orderId;
    }
    public void setAccountId(int accountId){
        this.accountId = accountId;
    }
    public int getAccountId(){
        return this.accountId;
    }
    public void setReceiptAmount(double receiptAmount){
        this.receiptAmount = receiptAmount;
    }
    public double getReceiptAmount(){
        return this.receiptAmount;
    }
    public void setRebateAmount(double rebateAmount){
        this.rebateAmount = rebateAmount;
    }
    public double getRebateAmount(){
        return this.rebateAmount;
    }
    public void setPayAmount(double payAmount){
        this.payAmount = payAmount;
    }
    public double getPayAmount(){
        return this.payAmount;
    }
    public void setCostAmount(double costAmount){
        this.costAmount = costAmount;
    }
    public double getCostAmount(){
        return this.costAmount;
    }
    public void setProfitAmount(double profitAmount){
        this.profitAmount = profitAmount;
    }
    public double getProfitAmount(){
        return this.profitAmount;
    }
}
