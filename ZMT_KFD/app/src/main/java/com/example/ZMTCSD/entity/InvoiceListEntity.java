package com.example.ZMTCSD.entity;

import java.io.Serializable;

/**
 * 增票的实体类
 */
public class InvoiceListEntity implements Serializable {
    /**
     *  {
     "id": 3255000,
     "code": "3664633",
     "invoiceCorporation": "太原钢铁集团有限公司",
     "orderCode": "16ZMT14801",
     "invoiceDate": "2016-09-09T00:00:00",
     "amountWithTax": 1500,
     "amountWithOutTax": 1282.05,
     "tax": 217.95,
     "status": 2,
     "statusStr": "受理"
     }
     */
    private int id;

    private String code;

    private String invoiceCorporation;
    private String orderCode;

    private String invoiceDate;

    private double amountWithTax;

    private double amountWithOutTax;

    private double tax;

    private int status;

    private String statusStr;

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
    public void setInvoiceCorporation(String invoiceCorporation){
        this.invoiceCorporation = invoiceCorporation;
    }
    public String getInvoiceCorporation(){
        return this.invoiceCorporation;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public void setInvoiceDate(String invoiceDate){
        this.invoiceDate = invoiceDate;
    }
    public String getInvoiceDate(){
        return this.invoiceDate;
    }
    public void setAmountWithTax(double amountWithTax){
        this.amountWithTax = amountWithTax;
    }
    public double getAmountWithTax(){
        return this.amountWithTax;
    }
    public void setAmountWithOutTax(double amountWithOutTax){
        this.amountWithOutTax = amountWithOutTax;
    }
    public double getAmountWithOutTax(){
        return this.amountWithOutTax;
    }
    public void setTax(double tax){
        this.tax = tax;
    }
    public double getTax(){
        return this.tax;
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
}
