package com.example.ZMTCSD.entity;

import java.io.Serializable;

/**
 *  中信保批复状态 实体类
 */

public class CompanyReplysEntity implements Serializable {
    /**
     *  {
     "id": 4611105,
     "reason": "中信保退回：请将母保单1200/201601038限额转移至子保单。",
     "updateTime": "2016-11-25T11:15:27",
     "keyword": "4591913",
     "type": 2,
     "agreed": false
     }
     */

    private int id;

    private String reason;

    private String updateTime;

    private String keyword;

    private int type;

    private boolean agreed;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setReason(String reason){
        this.reason = reason;
    }
    public String getReason(){
        return this.reason;
    }
    public void setUpdateTime(String updateTime){
        this.updateTime = updateTime;
    }
    public String getUpdateTime(){
        return this.updateTime;
    }
    public void setKeyword(String keyword){
        this.keyword = keyword;
    }
    public String getKeyword(){
        return this.keyword;
    }
    public void setType(int type){
        this.type = type;
    }
    public int getType(){
        return this.type;
    }
    public void setAgreed(boolean agreed){
        this.agreed = agreed;
    }
    public boolean getAgreed(){
        return this.agreed;
    }



}
