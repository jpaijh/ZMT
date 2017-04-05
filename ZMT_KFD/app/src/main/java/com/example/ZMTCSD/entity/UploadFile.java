package com.example.ZMTCSD.entity;

import java.io.Serializable;

/**
 * 上传文件到服务器 返回的连接
 */
public class UploadFile implements Serializable {
/**
 * [
 {
 "fileUrl": "http://183.129.133.147:18201/core/downloadFile/down?inlineShow=true&fileName=2016/08/25/5d7cf631-8d9d-4a6c-8c97-867779cf17dc.png",
 "fileName": "1467823329_Cancel.png"
 }
 ]
 */
    private String fileUrl;
    private String fileName;

    @Override
    public String toString() {
        return "UploadFile{" +
                "fileUrl='" + fileUrl + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
