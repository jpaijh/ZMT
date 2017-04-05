package com.example.ZMTCSD.entity;

import java.io.Serializable;

/**
 * 电话的实体类  (单据详细中的电话， 单据审批结果中的电话List)
 * 客户详情中的电话不是list
 */
public class ContactEntity  implements Serializable {
    private String name;
    private String phoneNumber;

    @Override
    public String toString() {
        return "ContactEntity{" +
                "name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
