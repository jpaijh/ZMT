package com.example.ZMTCSD.entity;

import java.io.Serializable;

/**
 * 往来单位列表的实体类  和待客提款的 收款单位界面一样
 */
public class CompanyListEntity implements Serializable{
    /**
     *   "id": 3211602,
     "name": "zhejiang .co .ltd",
     "status": 2,
     "statusStr": "审批通过"
     */

    private int  id;
    private String name;
    private String status;
    private String statusStr;

    @Override
    public String toString() {
        return "CompanyListEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", statusStr='" + statusStr + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }
}
