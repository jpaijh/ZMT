package com.example.ZMTCSD.entity;

import java.util.List;
/**
 * 单据的详细信息
 */
public class ApproveDetailsEntity {
    /**
     * {"approvalId": "3869301",approvalType": "付款审批","approvalTypeId": "3178700","reporterName": "孙思宇",
     "reporteDate": "2016-07-07T16:41:54","keyword": "2016400737890","currentApproverId": "2962903","currentApproverName": "周正","status": 0,
     "contacts": [
     {"name": "客户-全顺科技","phoneNumber": "83938453"},
     {"name": "申报人-孙思宇","phoneNumber": "8848487557"}],
     "files": [
     {"fileId": "1","fileName": "外销合同","uploader": "小王","uploadDate": "2016-07-12T01:50:53.6721433Z",
     "fileUrl": "https://www.baidu.com/img/bd_logo1.png"
     },
     {
     "fileId": "2",
     "fileName": "客户列表",
     "uploader": "小王",
     "uploadDate": "2016-07-12T01:50:53.6721433Z",
     "fileUrl": "http://sales.coamc.com.cn/uploadfiles/%BD%D8%D6%B9%B5%BD09%C4%EA6%D4%C218%C8%D5%BF%C9%D2%C9%C0%E0%B9%AB%B8%E6(%B9%AB%BF%AA%BE%BA%D5%F9%D0%D4%B4%A6%D6%C3)20096181359818.xls"
     },
     {
     "fileId": "3",
     "fileName": "说明书",
     "uploader": "小王",
     "uploadDate": "2016-07-12T01:50:53.6721433Z",
     "fileUrl": "http://www.mlr.gov.cn/zwgk/zfcg/cgzbxx/200907/P020090731571871325622.doc"
     }
     "propetyGroups": [
     {
     "groupName": "付款信息",
     "sequenceNo": 0,
     "propertyInfos": [
     {
     "propertyName": "付款单号",
     "propertyValue": "2016400737890",
     "sequenceNo": 0,
     "importance": 0
     },
     {
     "propertyName": "外销发票号",
     "propertyValue": "16ZMT12058D",
     "sequenceNo": 1,
     "importance": 0
     },
     {
     "propertyName": "款项付至",
     "propertyValue": "安吉万宝家具有限公司",
     "sequenceNo": 2,
     "importance": 0
     }
     ]
     },
     {
     "groupName": "支付信息",
     "sequenceNo": 1,
     "propertyInfos": [
     {
     "propertyName": "币别",
     "propertyValue": "RMB",
     "sequenceNo": 0,
     "importance": 0
     },
     {
     "propertyName": "金额",
     "propertyValue": "8,138.79",
     "sequenceNo": 1,
     "importance": 1
     },
     {
     "propertyName": "款项类型",
     "propertyValue": "货款",
     "sequenceNo": 2,
     "importance": 0
     },
     {
     "propertyName": "支付方式",
     "propertyValue": "电汇",
     "sequenceNo": 3,
     "importance": 0
     },
     {
     "propertyName": "开户银行",
     "propertyValue": "杭州联合农村商业银行安吉支行",
     "sequenceNo": 4,
     "importance": 0
     },
     {
     "propertyName": "银行账号",
     "propertyValue": null,
     "sequenceNo": 5,
     "importance": 0
     }
     "groupName": "其他信息",
     "sequenceNo": 2,
     "propertyInfos": [
     {
     "propertyName": "备注",
     "propertyValue": null,
     "sequenceNo": 0,
     "importance": 0}
     */


    private String approvalId;
    private String approvalType;
    private String approvalTypeId;
    private String reporterName;
    private String reporteDate;
    private String keyword;
    private String currentApproverId;
    private String currentApproverName;
    private int status;

    private List<ContactEntity> contacts;
    private List<FileInfoEntity> files;
    private List<PropertyGroupsEntity> propertyGroups;

    @Override
    public String toString() {
        return "ApproveDetailsEntity{" +
                "approvalId='" + approvalId + '\'' +
                ", approvalType='" + approvalType + '\'' +
                ", approvalTypeId='" + approvalTypeId + '\'' +
                ", reporterName='" + reporterName + '\'' +
                ", reporteDate='" + reporteDate + '\'' +
                ", keyword='" + keyword + '\'' +
                ", currentApproverId='" + currentApproverId + '\'' +
                ", currentApproverName='" + currentApproverName + '\'' +
                ", status=" + status +
                ", contacts=" + contacts +
                ", files=" + files +
                ", propetyGroups=" + propertyGroups +
                '}';
    }

    public List<ContactEntity> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactEntity> contacts) {
        this.contacts = contacts;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<FileInfoEntity> getFiles() {
        return files;
    }

    public void setFiles(List<FileInfoEntity> files) {
        this.files = files;
    }

    public List<PropertyGroupsEntity> getPropetyGroups() {
        return propertyGroups;
    }

    public void setPropetyGroups(List<PropertyGroupsEntity> propetyGroups) {
        this.propertyGroups = propetyGroups;
    }

    /**
     * 单据中的电话 信息
     */
//    public static class ContactEntity{
////    {"name": "客户-全顺科技","phoneNumber": "83938453"},
//        private String name;
//        private String phoneNumber;
//
//        @Override
//        public String toString() {
//            return "{" +
//                    "name='" + name + '\'' +
//                    ", phoneNumber='" + phoneNumber + '\'' +
//                    '}';
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getPhoneNumber() {
//            return phoneNumber;
//        }
//
//        public void setPhoneNumber(String phoneNumber) {
//            this.phoneNumber = phoneNumber;
//        }
//    }

//    /**
//     * 这是审批单据中的附件信息
//     */
//    public static class FileInfo implements Serializable {
////        "fileId": "2",
////        "fileName": "客户列表",
////        "uploader": "小王",
////        "uploadDate": "2016-07-12T01:50:53.6721433Z",
////        "fileUrl": "http://sales.coamc.com.cn/uploadfiles/%BD%D8%D6%B9%B5%BD09%C4%EA6%D4%C218%C8%D5%BF%C9%D2%C9%C0%E0%B9%AB%B8%E6(%B9%AB%BF%AA%BE%BA%D5%F9%D0%D4%B4%A6%D6%C3)20096181359818.xls"
//        private String fileId;
//        private String fileName;
//        private String uploader;
//        private String uploadDate;
//        private String fileUrl;
//
//        @Override
//        public String toString() {
//            return "Files{" +
//                    "fileId='" + fileId + '\'' +
//                    ", fileName='" + fileName + '\'' +
//                    ", uploader='" + uploader + '\'' +
//                    ", uploadDate='" + uploadDate + '\'' +
//                    ", fileUrl='" + fileUrl + '\'' +
//                    '}';
//        }
//
//        public String getFileId() {
//            return fileId;
//        }
//
//        public void setFileId(String fileId) {
//            this.fileId = fileId;
//        }
//
//        public String getFileName() {
//            return fileName;
//        }
//
//        public void setFileName(String fileName) {
//            this.fileName = fileName;
//        }
//
//        public String getUploader() {
//            return uploader;
//        }
//
//        public void setUploader(String uploader) {
//            this.uploader = uploader;
//        }
//
//        public String getUploadDate() {
//            return uploadDate;
//        }
//
//        public void setUploadDate(String uploadDate) {
//            this.uploadDate = uploadDate;
//        }
//
//        public String getFileUrl() {
//            return fileUrl;
//        }
//
//        public void setFileUrl(String fileUrl) {
//            this.fileUrl = fileUrl;
//        }
//    }

//    /**
//     * 这是审批单据中的分组信息
//     */
//    public static class propetyGroups {
//        /**
//         * "propetyGroups": [
//         {
//         "groupName": "付款信息",
//         "sequenceNo": 0,
//         "propertyInfos": [
//         {
//         "propertyName": "付款单号",
//         "propertyValue": "2016400737890",
//         "sequenceNo": 0,
//         "importance": 0
//         */
//        private String groupName;
//        private String sequenceNo;
//        private List<PropertyInfosEntity> propertyInfos;
//
//        @Override
//        public String toString() {
//            return "propetyGroups{" +
//                    "groupName='" + groupName + '\'' +
//                    ", sequenceNo='" + sequenceNo + '\'' +
//                    ", propertyInfos=" + propertyInfos +
//                    '}';
//        }
//
//        public String getGroupName() {
//            return groupName;
//        }
//
//        public void setGroupName(String groupName) {
//            this.groupName = groupName;
//        }
//
//        public List<PropertyInfosEntity> getPropertyInfos() {
//            return propertyInfos;
//        }
//
//        public void setPropertyInfos(List<PropertyInfosEntity> propertyInfos) {
//            this.propertyInfos = propertyInfos;
//        }
//
//        public String getSequenceNo() {
//            return sequenceNo;
//        }
//
//        public void setSequenceNo(String sequenceNo) {
//            this.sequenceNo = sequenceNo;
//        }
//
//        public static class PropertyInfosEntity implements Serializable {
//
//            /**
//             * "propertyName": "备注",
//             //   "propertyValue": "\r\n最新审批说明：null",
//             //  "sequenceNo": 0,
//             //  "importance": 0  //重要性
//             */
//            private String propertyName;
//            private String propertyValue;
//            private String sequenceNo;
//            private int importance;
//
//            @Override
//            public String toString() {
//                return "PropertyInfosEntity{" +
//                        "propertyName='" + propertyName + '\'' +
//                        ", propertyValue='" + propertyValue + '\'' +
//                        ", sequenceNo='" + sequenceNo + '\'' +
//                        ", importance='" + importance + '\'' +
//                        '}';
//            }
//
//            public String getPropertyName() {
//                return propertyName;
//            }
//
//            public void setPropertyName(String propertyName) {
//                this.propertyName = propertyName;
//            }
//
//            public String getPropertyValue() {
//                return propertyValue;
//            }
//
//            public void setPropertyValue(String propertyValue) {
//                this.propertyValue = propertyValue;
//            }
//
//            public String getSequenceNo() {
//                return sequenceNo;
//            }
//
//            public void setSequenceNo(String sequenceNo) {
//                this.sequenceNo = sequenceNo;
//            }
//
//            public int getImportance() {
//                return importance;
//            }
//
//            public void setImportance(int importance) {
//                this.importance = importance;
//            }
//        }
//    }





}
