package com.example.ZMTCSD.entity;

/**
 * 登陆用户的实体类
 */
public class UserClaimsEntity {
    /**
     {
     "subject": "2320200",
     "type": "identity",
     "value": "2320200"

     "subject": "2320200",
     "type": "userName",
     "value": "管理员"
     "subject": "2320200",
     "type": "loginName",
     "value": "yw_admin"
     "subject": "2320200",
     "type": "department",
     "value": ""
     "subject": "2320200",
     "type": "accountName",
     "value": "yw_admin"
     "subject": "2320200",
     "type": "accountId",
     "value": "0"
     "subject": "2320200",
     "type": "titleId",
     "value": "1569600"
     "subject": "2320200",
     "type": "titleName",
     "value": "天津自贸通外贸服务股份有限公司"
     "subject": "2320200",
     "type": "modulePermission",
     "value": "301"
     "subject": "2320200",
     "type": "modulePermission",
     "value": "501"
     "subject": "2320200",
     "type": "modulePermission",
     "value": "801"
     */

    private String subject;
    private String type;
    private String value;

    @Override
    public String toString() {
        return "UserClaimsEntity{" +
                "subject='" + subject + '\'' +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
