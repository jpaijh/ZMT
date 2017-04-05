package com.example.ZMTCSD.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 往来单位的详情 和中信保的外商代码详情一样
 */
public class CompanyDetailsEntity implements Serializable{
    /**
     * {
     "id": 0,
     "name": "河北久盛车业有限公司",
     "propetyGroups": [
     {
     "groupName": "基本信息",
     "sequenceNo": 0,
     "propertyInfos": [
     {
     "propertyName": "名称",
     "propertyValue": "小温",
     "sequenceNo": 0,
     "importance": 0
     }
     ]
     }
     ],
     "files": null
     }
     */

    private int id;
    private String name;
    private List<PropertyGroupsEntity> propetyGroups ;
    private List<FileInfoEntity> fileInfos;

    @Override
    public String toString() {
        return "CompanyDetailsEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", propetyGroups=" + propetyGroups +
                ", fileInfos=" + fileInfos +
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

    public List<PropertyGroupsEntity> getPropetyGroups() {
        return propetyGroups;
    }

    public void setPropetyGroups(List<PropertyGroupsEntity> propetyGroups) {
        this.propetyGroups = propetyGroups;
    }

    public List<FileInfoEntity> getFileInfos() {
        return fileInfos;
    }

    public void setFileInfos(List<FileInfoEntity> fileInfos) {
        this.fileInfos = fileInfos;
    }
}
