package com.example.ZMTCSD.entity;

import java.io.Serializable;

/**
 * 信息中心信息的实体类
 */
public class MessageEntity implements Serializable {
//    "id": 3185137,
//    "messageTypeId": 0,
//    "messageTypeName": "审批中心",
//   "creatorName": "郑欣",
//    "creatorId": 51,
//    "title": "审批中心审批通知",
//    "content": "<p style=\"white-space:normal;\">\r\n\t审批中心审批通知：\r\n</p>\r\n<p style"
//    "sendDate": "2016-06-01T14:00:05"
    private int id;
    private int messageTypeId;
    private String messageTypeName;
    private String creatorName;
    private int  creatorId;
    private String title;
    private String content;
    private String sendDate;


    @Override
    public String toString() {
        return "MessageEntity{" +
                "id=" + id +
                ", messageTypeId=" + messageTypeId +
                ", messageTypeName='" + messageTypeName + '\'' +
                ", creatorName='" + creatorName + '\'' +
                ", creatorId=" + creatorId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", sendDate='" + sendDate + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMessageTypeId() {
        return messageTypeId;
    }

    public void setMessageTypeId(int messageTypeId) {
        this.messageTypeId = messageTypeId;
    }

    public String getMessageTypeName() {
        return messageTypeName;
    }

    public void setMessageTypeName(String messageTypeName) {
        this.messageTypeName = messageTypeName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }
}
