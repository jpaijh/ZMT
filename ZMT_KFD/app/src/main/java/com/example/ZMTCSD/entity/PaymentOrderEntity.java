package com.example.ZMTCSD.entity;

import java.io.Serializable;

/**
 * 待客提款中的资金账户 订单 的实体类
 */

public class PaymentOrderEntity implements Serializable {
    @Override
    public String toString() {
        return "PaymentOrderEntity{" +
                "id=" + id +
                ", orderCode='" + orderCode + '\'' +
                ", currency='" + currency + '\'' +
                ", invoiceAmount=" + invoiceAmount +
                ", paidAmount=" + paidAmount +
                ", unPaidAmount=" + unPaidAmount +
                ", paymentAmount=" + paymentAmount +
                ", settleAmount=" + settleAmount +
                '}';
    }

    /**
     *  {
     "id": 3964806,
     "orderCode": "16ZMT12063A",
     "currency": "USD",
     "invoiceAmount": 160440.00021,
     "paidAmount": 160440,
     "unPaidAmount": 21854.59,
     "paymentAmount": 0,
     "settleAmount": 0
     }
     */

    private int id;

    private String orderCode;

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
