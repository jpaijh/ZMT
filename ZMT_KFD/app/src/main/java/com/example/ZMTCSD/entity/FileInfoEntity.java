package com.example.ZMTCSD.entity;

import java.io.Serializable;
import java.util.SimpleTimeZone;

/**
 * 附件的实体类  （单据中的附件，客户的详细信息中的附件， 往来单位详细中附件，订单详细中的附件，）
 */
public class FileInfoEntity implements Serializable {
    /**
     * "fileId": "3223303",
     * "fileName": "QQ20150105-1@2x.png",
     * "uploader": "管理员",
     * "uploadDate": "2016-08-03T10:47:00",
     * "fileSubTitle": "上传于 2017-01-23 01:23",
     * "fileUrl": "http://183.129.133.147:18201/core/downloadFile/down?inlineShow=true&fileName=
     * 2016/08/03/4479911f-bb7a-4083-9bc3-17203fc045de.png"
     */
    private String fileId;

    private String fileName;

    private String uploader;

    private String uploadDate;
    private String fileSubTitle;

    private String fileUrl;

    @Override
    public String toString() {
        return "FileInfoEntity{" +
                "fileId='" + fileId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", uploader='" + uploader + '\'' +
                ", uploadDate='" + uploadDate + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                '}';
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileId() {
        return this.fileId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getUploader() {
        return this.uploader;
    }

    public String getFileSubTitle() {
        return fileSubTitle;
    }

    public void setFileSubTitle(String fileSubTitle) {
        this.fileSubTitle = fileSubTitle;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getUploadDate() {
        return this.uploadDate;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }
}
