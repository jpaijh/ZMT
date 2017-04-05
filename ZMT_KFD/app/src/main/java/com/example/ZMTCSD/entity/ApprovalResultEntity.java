package com.example.ZMTCSD.entity;

import java.util.List;

/**
 * 单据的审批信息
 */
public class ApprovalResultEntity  {
    /**
     * [
     {
     "id": "3852471",
     "nodeName": "部门经理审批",
     "approvalName": "周正",
     "result": 1,
     "approveDate": "2016-07-06T14:32:36",
     "remark": null,
     "contacts": [
     {
     "name": "周正-",
     "phoneNumber": "13888888888"
     }
     ]
     },
     {
     "id": "3852473",
     "nodeName": "总经理审批",
     "approvalName": null,
     "result": 1,
     "approveDate": "2016-07-06T14:32:37",
     "remark": "自动批准",
     "contacts": null
     },
     {
     "id": "3852474",
     "nodeName": "董事长审批",
     "approvalName": null,
     "result": 1,
     "approveDate": "2016-07-06T14:32:37",
     "remark": "自动批准",
     "contacts": null
     }
     ]
     */

    private String id;
    private String nodeName;
    private String approvalName;
    private int result;
    private String approveDate;
    private String remark;
    private List<ContactEntity> contacts;


    @Override
    public String toString() {
        return "ApprovalResultEntity{" +
                "id='" + id + '\'' +
                ", nodeName='" + nodeName + '\'' +
                ", approvalName='" + approvalName + '\'' +
                ", result='" + result + '\'' +
                ", approveDate='" + approveDate + '\'' +
                ", remark='" + remark + '\'' +
                ", contacts=" + contacts +
                '}';
    }

    public List<ContactEntity> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactEntity> contacts) {
        this.contacts = contacts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getApprovalName() {
        return approvalName;
    }

    public void setApprovalName(String approvalName) {
        this.approvalName = approvalName;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(String approveDate) {
        this.approveDate = approveDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


}
