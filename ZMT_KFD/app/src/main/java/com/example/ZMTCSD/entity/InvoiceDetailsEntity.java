package com.example.ZMTCSD.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 增票详情的实体类
 */
public class InvoiceDetailsEntity  implements Serializable{
    /**
     * {
     "id": 2822700,
     "code": "34839843",
     "no": null,
     "amountWithTax": 18720,
     "amountWithOutTax": 16000,
     "tax": 2720,
     "invoiceDate": "2015-05-06T00:00:00",
     "invoiceItems": [
     {
     "id": 2822600,
     "productName": "圆珠笔",
     "unit": "枝",
     "unitPrice": 0.64,
     "productQuantity": 25000,
     "amountWithoutTax": 16000,
     "amountWithTax": 18720,
     "tax": 2720,
     "taxRate": 0.17
     }
     ],
     "invoiceCorporation": "绍兴市宇航塑业包装有限公司",
     "orderCode": "16ZMT14801"
     }
     */

    private int id;

    private String code;

    private String no;

    private double amountWithTax;

    private double amountWithOutTax;

    private double tax;

    private String invoiceDate;

    private List<InvoiceItems> invoiceItems ;

    private String invoiceCorporation;
    private String orderCode;

    @Override
    public String toString() {
        return "InvoiceDetailsEntity{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", no='" + no + '\'' +
                ", amountWithTax=" + amountWithTax +
                ", amountWithOutTax=" + amountWithOutTax +
                ", tax=" + tax +
                ", invoiceDate='" + invoiceDate + '\'' +
                ", invoiceItems=" + invoiceItems +
                ", invoiceCorporation='" + invoiceCorporation + '\'' +
                ", orderCode='" + orderCode + '\'' +
                '}';
    }

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
    public void setNo(String no){
        this.no = no;
    }
    public String getNo(){
        return this.no;
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
    public void setInvoiceDate(String invoiceDate){
        this.invoiceDate = invoiceDate;
    }
    public String getInvoiceDate(){
        return this.invoiceDate;
    }
    public void setInvoiceItems(List<InvoiceItems> invoiceItems){
        this.invoiceItems = invoiceItems;
    }
    public List<InvoiceItems> getInvoiceItems(){
        return this.invoiceItems;
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

    public static class InvoiceItems  implements Serializable{
        private int id;

        private String productName;

        private String unit;

        private double unitPrice;

        private int productQuantity; //数量

        private double amountWithoutTax;

        private double amountWithTax;

        private double tax;

        private double taxRate;

        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public void setProductName(String productName){
            this.productName = productName;
        }
        public String getProductName(){
            return this.productName;
        }
        public void setUnit(String unit){
            this.unit = unit;
        }
        public String getUnit(){
            return this.unit;
        }
        public void setUnitPrice(double unitPrice){
            this.unitPrice = unitPrice;
        }
        public double getUnitPrice(){
            return this.unitPrice;
        }
        public void setProductQuantity(int productQuantity){
            this.productQuantity = productQuantity;
        }
        public int getProductQuantity(){
            return this.productQuantity;
        }
        public void setAmountWithoutTax(double amountWithoutTax){
            this.amountWithoutTax = amountWithoutTax;
        }
        public double getAmountWithoutTax(){
            return this.amountWithoutTax;
        }
        public void setAmountWithTax(double amountWithTax){
            this.amountWithTax = amountWithTax;
        }
        public double getAmountWithTax(){
            return this.amountWithTax;
        }
        public void setTax(double tax){
            this.tax = tax;
        }
        public double getTax(){
            return this.tax;
        }
        public void setTaxRate(double taxRate){
            this.taxRate = taxRate;
        }
        public double getTaxRate(){
            return this.taxRate;
        }

    }

}
