package com.example.ZMTCSD.entity;

/**
 *  新建客户是需要发送的实体类
 */

public class CustomerAccountAddEntity {
    /**
     * {
     "contactName": "wo我 ",
     "contactPhoneNumber": "123445566",
     "email": "123456@.qq.com",
     "loginName":"gjortw",
     "accountType": 5,
     "serviceItemIds": [1,2],
     "customerName": "中国铁1塔通讯有限公司"
     }
     */
    private String contactName;

    private String contactPhoneNumber;

    private String email;

    private String loginName;

    private String accountType;

    private String[] serviceItemIds ;

    private String customerName;

    public void setContactName(String contactName){
        this.contactName = contactName;
    }
    public String getContactName(){
        return this.contactName;
    }
    public void setContactPhoneNumber(String contactPhoneNumber){
        this.contactPhoneNumber = contactPhoneNumber;
    }
    public String getContactPhoneNumber(){
        return this.contactPhoneNumber;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return this.email;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setAccountType(String accountType){
        this.accountType = accountType;
    }
    public String getAccountType(){
        return this.accountType;
    }

    public String[] getServiceItemIds() {
        return serviceItemIds;
    }

    public void setServiceItemIds(String[] serviceItemIds) {
        this.serviceItemIds = serviceItemIds;
    }

    public void setCustomerName(String customerName){
        this.customerName = customerName;
    }
    public String getCustomerName(){
        return this.customerName;
    }


}
