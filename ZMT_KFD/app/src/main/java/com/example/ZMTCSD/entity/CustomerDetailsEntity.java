package com.example.ZMTCSD.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 客户详情的实体类
 */
public class CustomerDetailsEntity implements Serializable {
    /**
     * {
     * "id": 0,
     * "contact": {
     * "name": "小温",
     * "phoneNumber": "13148470171"
     * },
     * "name": "小温",
     * "code": "wtyTTT",
     * "status": 2,
     * "statusStr": "批准",
     * "propetyGroups": [
     * {
     * "groupName": "基本信息",
     * "sequenceNo": 0,
     * "propertyInfos": [
     * {
     * "propertyName": "客户名称",
     * "propertyValue": "429测试",
     * "sequenceNo": 0,
     * "importance": 0
     * }
     * ]
     * }
     * ],
     * "fileGroups": [
     * {
     * "fileInfo": {
     * "fileId": "0",
     * "fileName": "中山丝绸_ERP_需求调研报告V0.71.docx",
     * "uploader": "赵兰兰",
     * "uploadDate": "2016-04-29T13:44:14",
     * "fileUrl": "http://183.129.133.147:18201/core/downloadFile/down?inlineShow=true&fileName=2016/04/29/c8c4c635-b44a-4103-bc87-68a66a9fd4db.docx"
     * },
     * "fileCategoryName": "一般贸易出口通关",
     * "fileCategoryId": 1001
     * },
     * {
     * "fileInfo": {
     * "fileId": "0",
     * "fileName": "中山丝绸_ERP_需求调研报告V0.71.docx",
     * "uploader": "赵兰兰",
     * "uploadDate": "2016-04-29T13:44:19",
     * "fileUrl": "http://183.129.133.147:18201/core/downloadFile/down?inlineShow=true&fileName=2016/04/29/29327404-1ed2-4ce7-ba0b-ec4f9e16e60b.docx"
     * },
     * "fileCategoryName": "一般贸易出口通关",
     * "fileCategoryId": 1001
     * }
     * ]
     * }
     */

    private int id;
    private ContactEntity contact;
    private String name;
    private String code;
    private int status;
    private String statusStr;
    private List<PropertyGroupsEntity> propetyGroups;
    private List<fileGroups> fileGroups;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public ContactEntity getContact() {
        return contact;
    }

    public void setContact(ContactEntity contact) {
        this.contact = contact;
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getCode() {return code;}

    public void setCode(String code) {this.code = code;}

    public int getStatus() {return status;}

    public void setStatus(int status) {this.status = status;}

    public String getStatusStr() {return statusStr;}

    public void setStatusStr(String statusStr) {this.statusStr = statusStr;}

    public void setPropetyGroups(List<PropertyGroupsEntity> propetyGroups) {
        this.propetyGroups = propetyGroups;
    }

    public List<PropertyGroupsEntity> getPropetyGroups() {
        return this.propetyGroups;
    }

    public List<CustomerDetailsEntity.fileGroups> getFileGroups() {
        return fileGroups;
    }

    public void setFileGroups(List<CustomerDetailsEntity.fileGroups> fileGroups) {
        this.fileGroups = fileGroups;
    }

    public static class fileGroups implements Serializable {
        private List<FileInfoEntity> fileInfos ;

        private String fileCategoryName;

        private int fileCategoryId;

        @Override
        public String toString() {
            return
                    ", fileCategoryName='" + fileCategoryName + '\'' +
                    ", fileCategoryId=" + fileCategoryId+
                    "fileInfos=" + fileInfos;
        }

        public void setFileInfos(List<FileInfoEntity> fileInfos){
            this.fileInfos = fileInfos;
        }
        public List<FileInfoEntity> getFileInfos(){
            return this.fileInfos;
        }

        public void setFileCategoryName(String fileCategoryName) {
            this.fileCategoryName = fileCategoryName;
        }

        public String getFileCategoryName() {
            return this.fileCategoryName;
        }

        public void setFileCategoryId(int fileCategoryId) {
            this.fileCategoryId = fileCategoryId;
        }

        public int getFileCategoryId() {
            return this.fileCategoryId;
        }

    }

}
