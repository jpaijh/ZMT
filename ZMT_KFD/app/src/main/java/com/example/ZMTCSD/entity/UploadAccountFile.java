package com.example.ZMTCSD.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 将上传的文件与用户的附件进行联合  是发送的json数据
 */
public class UploadAccountFile implements Serializable {
    /**
     * {
     "files": [
     {
     "fileUrl": "http://183.129.133.147:18201/core/downloadFile/down?inlineShow=true&fileName=2016/08/25/5d7cf631-8d9d-4a6c-8c97-867779cf17dc.png",
     "fileName": "1467823329_Cancel.png"
     }
     ],
     "fileCategoryId": 1050,
     "fileCategoryName": "服务协议",
     "accountId": "3218700",
     "accountName": "小温",
     }
     */
    private List<UploadFile> files;
    private int  fileCategoryId;
    private String fileCategoryName;
    private int accountId;
    private  String accountName;

    public UploadAccountFile() {
    }

    @Override
    public String toString() {
        return "UploadAccountFile{" +
                "files=" + files +
                ", fileCategoryId=" + fileCategoryId +
                ", fileCategoryName='" + fileCategoryName + '\'' +
                ", accountId=" + accountId +
                ", accountName='" + accountName + '\'' +
                '}';
    }

    public List<UploadFile> getFiles() {
        return files;
    }

    public void setFiles(List<UploadFile> files) {
        this.files = files;
    }

    public int getFileCategoryId() {
        return fileCategoryId;
    }

    public void setFileCategoryId(int fileCategoryId) {
        this.fileCategoryId = fileCategoryId;
    }

    public String getFileCategoryName() {
        return fileCategoryName;
    }

    public void setFileCategoryName(String fileCategoryName) {
        this.fileCategoryName = fileCategoryName;
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
}
