package com.example.ZMTCSD.entity;

import java.io.Serializable;
import java.util.List;

/**
 *  待客提款 的元数据 实体类
 */

public class PaymentDataEntity implements Serializable {
    /**
     * "paymentTypes": [
     {
     "paymentTypeId": 0,
     "paymentTypeName": "货款",
     "subTypes": [
     {
     "paymentSubTypeId": "0-1196403",
     "paymentTypeId": 0,
     "paymentSubTypeName": "货款"
     },
     {
     "paymentSubTypeId": "0-11",
     "paymentTypeId": 0,
     "paymentSubTypeName": "往来账"
     }
     ]
     }],
     "paymentMethods": [
     {
     "name": "电汇",
     "value": "1383301"
     }],
     "guaranteeTypes": [
     {
     "name": "国外定金",
     "value": "2966403"
     }],
     "currencys": [
     {
     "currency": "USD",
     "rmbRate": 6.8889
     }]
     */
    private List<PaymentTypeList> paymentTypes ;

    private List<PaymentMethodList> paymentMethods ;

    private List<GuaranteeTypeList> guaranteeTypes ;

    private List<CurrencyList> currencys ;

    public List<PaymentTypeList> getPaymentTypes() {
        return paymentTypes;
    }

    public void setPaymentTypes(List<PaymentTypeList> paymentTypes) {
        this.paymentTypes = paymentTypes;
    }

    public List<PaymentMethodList> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<PaymentMethodList> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public List<GuaranteeTypeList> getGuaranteeTypes() {
        return guaranteeTypes;
    }

    public void setGuaranteeTypes(List<GuaranteeTypeList> guaranteeTypes) {
        this.guaranteeTypes = guaranteeTypes;
    }

    public List<CurrencyList> getCurrencys() {
        return currencys;
    }

    public void setCurrencys(List<CurrencyList> currencys) {
        this.currencys = currencys;
    }

    //账户的类型
    public static class PaymentTypeList implements Serializable  {
        private int paymentTypeId;

        private String paymentTypeName;

        private List<SubTypeList> subTypes ;

        public int getPaymentTypeId() {
            return paymentTypeId;
        }

        public void setPaymentTypeId(int paymentTypeId) {
            this.paymentTypeId = paymentTypeId;
        }

        public String getPaymentTypeName() {
            return paymentTypeName;
        }

        public void setPaymentTypeName(String paymentTypeName) {
            this.paymentTypeName = paymentTypeName;
        }

        public List<SubTypeList> getSubTypes() {
            return subTypes;
        }

        public void setSubTypes(List<SubTypeList> subTypes) {
            this.subTypes = subTypes;
        }
    }

    //付款方式
    public static class PaymentMethodList implements Serializable {
        private String name;
        private String value;

        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }
        public void setValue(String value){
            this.value = value;
        }
        public String getValue(){
            return this.value;
        }

        @Override
        public String toString() {
            return "PaymentMethodList{" +
                    "name='" + name + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    //担保方式
    public static class GuaranteeTypeList implements Serializable {
        private String name;
        private String value;

        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }
        public void setValue(String value){
            this.value = value;
        }
        public String getValue(){
            return this.value;
        }
        @Override
        public String toString() {
            return "GuaranteeTypeList{" +
                    "name='" + name + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }
    //币别，金额
    public static class CurrencyList implements Serializable {
        private String currency;
        private double rmbRate;

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
        @Override
        public String toString() {
            return "CurrencyList{" +
                    "currency='" + currency + '\'' +
                    ", rmbRate=" + rmbRate +
                    '}';
        }
    }


    //账户的子类型
    public static class SubTypeList implements Serializable {
        private String paymentSubTypeId;

        private int paymentTypeId;

        private String paymentSubTypeName;

        @Override
        public String toString() {
            return "SubTypeList{" +
                    "paymentSubTypeId='" + paymentSubTypeId + '\'' +
                    ", paymentTypeId=" + paymentTypeId +
                    ", paymentSubTypeName='" + paymentSubTypeName + '\'' +
                    '}';
        }

        public void setPaymentSubTypeId(String paymentSubTypeId){
            this.paymentSubTypeId = paymentSubTypeId;
        }
        public String getPaymentSubTypeId(){
            return this.paymentSubTypeId;
        }
        public void setPaymentTypeId(int paymentTypeId){
            this.paymentTypeId = paymentTypeId;
        }
        public int getPaymentTypeId(){
            return this.paymentTypeId;
        }
        public void setPaymentSubTypeName(String paymentSubTypeName){
            this.paymentSubTypeName = paymentSubTypeName;
        }
        public String getPaymentSubTypeName(){
            return this.paymentSubTypeName;
        }

    }
}
