package com.example.ZMTCSD.entity;

import java.io.Serializable;

/**
 * 客户列表的实体类
 */
public class CustomerListEntity implements Serializable {
    /**
     *  {
     "id": 3144501,
     "name": "429测试",
     "status": 2,
     "code": "42901",
     "createDate": "2016-04-29T13:43:55",
     "accountType": 0,
     "statusStr": "批准"
     }
     */
    private int id;

    private String name;

    private int status;

    private String code;

    private String createDate;

    private int accountType;

    private String statusStr;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }
    public void setCode(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
    public void setCreateDate(String createDate){
        this.createDate = createDate;
    }
    public String getCreateDate(){
        return this.createDate;
    }
    public void setAccountType(int accountType){
        this.accountType = accountType;
    }
    public int getAccountType(){
        return this.accountType;
    }
    public void setStatusStr(String statusStr){
        this.statusStr = statusStr;
    }
    public String getStatusStr(){
        return this.statusStr;
    }

    @Override
    public String toString() {
        return "CustomerListEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", code='" + code + '\'' +
                ", createDate='" + createDate + '\'' +
                ", accountType=" + accountType +
                ", statusStr='" + statusStr + '\'' +
                '}';
    }
}
