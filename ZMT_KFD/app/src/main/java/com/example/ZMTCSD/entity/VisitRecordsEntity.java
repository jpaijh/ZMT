package com.example.ZMTCSD.entity;

import java.io.Serializable;

/**
 *  来访记录的实体类
 */
public class VisitRecordsEntity  implements Serializable {
    /**
     *  {
     "id": 3235512,
     "accountId": 3218700,
     "accountName": "小温",
     "visitDate": "2016-09-02T16:39:50",
     "userName": "管理员",
     "userId": 2320200,
     "content": "I don't think it's time "
     }
     */
    private int id;

    private int accountId;

    private String accountName;

    private String visitDate;

    private String userName;

    private int userId;

    private String content;

    public VisitRecordsEntity() {
    }

//    //专门为了添加来访记录。
//    public VisitRecordsEntity(int accountId, String accountName, String content) {
//        this.accountId = accountId;
//        this.accountName = accountName;
//        this.content = content;
//    }

    @Override
    public String toString() {
        return "VisitRecordsEntity{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", accountName='" + accountName + '\'' +
                ", visitDate='" + visitDate + '\'' +
                ", userName='" + userName + '\'' +
                ", userId=" + userId +
                ", content='" + content + '\'' +
                '}';
    }

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setAccountId(int accountId){
        this.accountId = accountId;
    }
    public int getAccountId(){
        return this.accountId;
    }
    public void setAccountName(String accountName){
        this.accountName = accountName;
    }
    public String getAccountName(){
        return this.accountName;
    }
    public void setVisitDate(String visitDate){
        this.visitDate = visitDate;
    }
    public String getVisitDate(){
        return this.visitDate;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public String getUserName(){
        return this.userName;
    }
    public void setUserId(int userId){
        this.userId = userId;
    }
    public int getUserId(){
        return this.userId;
    }
    public void setContent(String content){
        this.content = content;
    }
    public String getContent(){
        return this.content;
    }

}
