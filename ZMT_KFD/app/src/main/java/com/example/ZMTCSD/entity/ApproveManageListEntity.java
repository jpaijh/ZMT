package com.example.ZMTCSD.entity;


import java.io.Serializable;
import java.util.List;

/**
 * 单据列表的信息
 */
public class ApproveManageListEntity {
    /**
     *
     * "approvalId": "3184825",
     * "approvalType": "工作联系单",
     * "approvalTypeId": "3181100",
     * "reporterName": "姚霞",
     * "keyword": "16E000170",
     * "reporteDate": "2016-06-01T13:41:23",
     *  "currentApproverId": "2794703",
       "currentApproverName": "总经理审批",
     * "flag": "3181100",
     * "status": 0,
     * "propertyInfos": [
     * {
     * "propertyName": "订单号",
     * "propertyValue": "DT125873184825",
     * "sequenceNo": 0,
     * "importance": 0
     * },
     * {
     * "propertyName": "创建时间",
     * "propertyValue": "2016-6-13",
     * "sequenceNo": 0,
     * "importance": 0
     * },
     * {
     * "propertyName": "订单金额",
     * "propertyValue": "13,254,00",
     * "sequenceNo": 0,
     * "importance": 0
     * },
     * {
     * "propertyName": "金额币别",
     * "propertyValue": "USD",
     * "sequenceNo": 0,
     * "importance": 0
     */
    private String approvalId;
    private String approvalType;
    private String approvalTypeId;
    private String reporterName;
    private String keyword;
    private String reporteDate;
    private String currentApproverId;
    private String currentApproverName;
    private String flag;
    private int status;
    private List<PropertyInfoList> propertyInfos;

    @Override
    public String toString() {
        return "ApproveManageListEntity{" +
                "approvalId='" + approvalId + '\'' +
                ", approvalType='" + approvalType + '\'' +
                ", approvalTypeId='" + approvalTypeId + '\'' +
                ", reporterName='" + reporterName + '\'' +
                ", keyword='" + keyword + '\'' +
                ", reporteDate='" + reporteDate + '\'' +
                ", currentApproverId='" + currentApproverId + '\'' +
                ", currentApproverName='" + currentApproverName + '\'' +
                ", flag='" + flag + '\'' +
                ", status=" + status +
                ", propertyInfos=" + propertyInfos +
                '}';
    }

    public String getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(String approvalId) {
        this.approvalId = approvalId;
    }

    public String getApprovalType() {
        return approvalType;
    }

    public void setApprovalType(String approvalType) {
        this.approvalType = approvalType;
    }

    public String getApprovalTypeId() {
        return approvalTypeId;
    }

    public void setApprovalTypeId(String approvalTypeId) {
        this.approvalTypeId = approvalTypeId;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getReporteDate() {
        return reporteDate;
    }

    public void setReporteDate(String reporteDate) {
        this.reporteDate = reporteDate;
    }

    public String getCurrentApproverId() {
        return currentApproverId;
    }

    public void setCurrentApproverId(String currentApproverId) {
        this.currentApproverId = currentApproverId;
    }

    public String getCurrentApproverName() {
        return currentApproverName;
    }

    public void setCurrentApproverName(String currentApproverName) {
        this.currentApproverName = currentApproverName;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public List<PropertyInfoList> getPropertyInfos() {
        return propertyInfos;
    }

    public void setPropertyInfos(List<PropertyInfoList> propertyInfos) {
        this.propertyInfos = propertyInfos;
    }

    public static class PropertyInfoList implements Serializable {
//        {
//            "propertyName": "收款单位",
//                "propertyValue": "保定市华优棉纺织厂",
//                "sequenceNo": 0,
//                "importance": 0
//        }
        private String propertyName;
        private String propertyValue;
        private String sequenceNo;
        private int importance;

        @Override
        public String toString() {
            return "PropertyInfoList{" +
                    "propertyName='" + propertyName + '\'' +
                    ", propertyValue='" + propertyValue + '\'' +
                    ", sequenceNo='" + sequenceNo + '\'' +
                    ", importance='" + importance + '\'' +
                    '}';
        }

        public String getPropertyName() {
            return propertyName;
        }

        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        public String getPropertyValue() {
            return propertyValue;
        }

        public void setPropertyValue(String propertyValue) {
            this.propertyValue = propertyValue;
        }

        public String getSequenceNo() {
            return sequenceNo;
        }

        public void setSequenceNo(String sequenceNo) {
            this.sequenceNo = sequenceNo;
        }

        public int getImportance() {
            return importance;
        }

        public void setImportance(int importance) {
            this.importance = importance;
        }
    }

}
