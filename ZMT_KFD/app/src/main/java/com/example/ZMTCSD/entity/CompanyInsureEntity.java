package com.example.ZMTCSD.entity;

import java.io.Serializable;
import java.util.List;

/**
 *  中信保的投保详情 和 中信保的限额详情 一样
 */

public class CompanyInsureEntity implements Serializable {

    @Override
    public String toString() {
        return "CompanyInsureEntity{" +
                "lcId=" + lcId +
                ", propertyGroupInfos=" + propertyGroupInfos +
                ", fileInfos=" + fileInfos +
                '}';
    }

    /**
     * "lcId": 4001409,
     * "propertyGroupInfos":
     * "fileInfos": null
     */


    private int lcId;
    private List<PropertyGroupsEntity> propertyGroupInfos;
    private List<FileInfoEntity> fileInfos;

    public int getLcId() {
        return lcId;
    }

    public void setLcId(int lcId) {
        this.lcId = lcId;
    }

    public List<PropertyGroupsEntity> getPropertyGroupInfos() {
        return propertyGroupInfos;
    }

    public void setPropertyGroupInfos(List<PropertyGroupsEntity> propertyGroupInfos) {
        this.propertyGroupInfos = propertyGroupInfos;
    }

    public List<FileInfoEntity> getFileInfos() {
        return fileInfos;
    }

    public void setFileInfos(List<FileInfoEntity> fileInfos) {
        this.fileInfos = fileInfos;
    }
}
