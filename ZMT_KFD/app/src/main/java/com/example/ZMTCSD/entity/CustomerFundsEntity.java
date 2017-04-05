package com.example.ZMTCSD.entity;

import java.io.Serializable;

/**
 * 客户详情中的资金实体类
 */
public class CustomerFundsEntity implements Serializable {
    /**
     * {
     "accountBalance": 173504,
     "financingAccountBanance": 0,
     "debtAccountBanance": 0,
     "invoiceAmount": 1173504
     }
     */

    private double accountBalance;
    private double financingAccountBanance;
    private double debtAccountBanance;
    private double invoiceAmount;



    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public double getFinancingAccountBanance() {
        return financingAccountBanance;
    }

    public void setFinancingAccountBanance(double financingAccountBanance) {
        this.financingAccountBanance = financingAccountBanance;
    }

    public double getDebtAccountBanance() {
        return debtAccountBanance;
    }

    public void setDebtAccountBanance(double debtAccountBanance) {
        this.debtAccountBanance = debtAccountBanance;
    }

    public double getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(double invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }
}
