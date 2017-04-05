package com.example.ZMTCSD.entity;

/**
 *  这是水单认领时需要发送实体类
 */

public class BankSlipClaimEntity {
    public BankSlipClaimEntity() {
    }

    /**{
 "claimAmount": 2,
 "customerRMBRate": 6.5,
 "bankSlipId": 3246500,
 "accountId": 3290701,
 "accountName": "WE122",
 "accountCode": "WE122"
 }
 */


    private String  claimAmount;
    private String  customerRMBRate;
    private int bankSlipId;
    private int accountId;
    private String accountName;
    private String accountCode;

    public String getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(String claimAmount) {
        this.claimAmount = claimAmount;
    }

    public String getCustomerRMBRate() {
        return customerRMBRate;
    }

    public void setCustomerRMBRate(String customerRMBRate) {
        this.customerRMBRate = customerRMBRate;
    }

    public int getBankSlipId() {
        return bankSlipId;
    }

    public void setBankSlipId(int bankSlipId) {
        this.bankSlipId = bankSlipId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }
}
