package com.E8908.blueTooth.bean;

import java.util.List;

public class YunInfoBean {

    /**
     * code : 0
     * response : {"tatol":2,"rows":[{"receiveTime":"2019-04-01 09:20:10","sequenceCode":null,"storeName":"车安达","orgPk":"01","projectName":"车内小健康","checkTechnician":"肖宝瑞测试","constructionTechnician":"肖宝瑞测试","receiveCarPk":"14","checkTypePk":"2","carOwnerRead":0,"id":560,"carNumber":"粤B12346","deviceno":null,"readCount":0,"beforeWpbfScore":"57","beforeWpbfPicUrl":null,"afterWpbfScore":"100","afterWpbfPicUrl":null,"beforeKqlxScore":"100","beforeKqlxPicUrl":null,"afterKqlxScore":"100","afterKqlxPicUrl":null,"beforeCnfcwrScore":"100","beforeCnfcwrPicUrl":null,"afterCnfcwrScore":"100","afterCnfcwrPicUrl":null,"beforeXdsjScore":"100","beforeXdsjPicUrl":null,"afterXdsjScore":"100","afterXdsjPicUrl":null,"beforeSwczScore":"100","beforeSwczPicUrl":null,"afterSwczScore":"100","afterSwczPicUrl":null,"beforeKqjhScore":"100","beforeKqjhPicUrl":null,"afterKqjhScore":"100","afterKqjhPicUrl":null,"beforeNsjjdScore":"100","beforeNsjjdPicUrl":null,"afterNsjjdScore":"100","afterNsjjdPicUrl":null,"beforeCnjxScore":"100","beforeCnjxPicUrl":null,"afterCnjxScore":"100","afterCnjxPicUrl":null,"beforeLxbmwgScore":"100","beforeLxbmwgPicUrl":null,"afterLxbmwgScore":"36","afterLxbmwgPicUrl":null,"beforeZfxbmwgScore":"100","beforeZfxbmwgPicUrl":null,"afterZfxbmwgScore":"100","afterZfxbmwgPicUrl":null,"beforeGfjbmwgScore":"100","beforeGfjbmwgPicUrl":null,"afterGfjbmwgScore":"100","afterGfjbmwgPicUrl":null,"beforeTfgdbmwgScore":"100","beforeTfgdbmwgPicUrl":null,"afterTfgdbmwgScore":"100","afterTfgdbmwgPicUrl":null,"beforeLnqbmwgScore":"100","beforeLnqbmwgPicUrl":null,"afterLnqbmwgScore":"100","afterLnqbmwgPicUrl":null,"beforeKtxtzlxnScore":"100","beforeKtxtzlxnPicUrl":null,"afterKtxtzlxnScore":"100","afterKtxtzlxnPicUrl":null,"beforeKtkqyxScore":"100","beforeKtkqyxPicUrl":null,"afterKtkqyxScore":"100","afterKtkqyxPicUrl":null,"beforeCfkyw":"100","afterCfkyw":"100","beforeScwpyw":"100","afterScwpyw":"100","beforeXcyw":"100","afterXcyw":"100","beforeEsyyw":"100","afterEsyyw":"100","beforeNspgyw":"100","afterNspgyw":"100","beforeXjmbyw":"100","afterXjmbyw":"100","beforeNsxfyw":"100","afterNsxfyw":"100","beforeQtyw":"100","afterQtyw":"100","beforeJq":"0","afterJq":null,"beforeTvoc":"0","afterTvoc":null,"beforePm25":"0","afterPm25":null,"beforeWd":"0","afterWd":null,"beforeSd":"0","afterSd":null,"createUser":"肖宝瑞测试","updateUser":null,"createTime":1554111458000,"updateTime":null,"beforeZjwsScore":null,"afterZjwsScore":null,"beforeYwjcScore":null,"afterYwjcScore":null,"beforeKtxtScore":null,"afterKtxtScore":null,"beforeYhqtScore":null,"afterYhqtScore":null,"beforeScore":null,"afterScore":null,"recommends":null,"rn":"1","pushmessage":1}]}
     * message : success
     */

    private int code;
    private ResponseBean response;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class ResponseBean {
        /**
         * tatol : 2
         * rows : [{"receiveTime":"2019-04-01 09:20:10","sequenceCode":null,"storeName":"车安达","orgPk":"01","projectName":"车内小健康","checkTechnician":"肖宝瑞测试","constructionTechnician":"肖宝瑞测试","receiveCarPk":"14","checkTypePk":"2","carOwnerRead":0,"id":560,"carNumber":"粤B12346","deviceno":null,"readCount":0,"beforeWpbfScore":"57","beforeWpbfPicUrl":null,"afterWpbfScore":"100","afterWpbfPicUrl":null,"beforeKqlxScore":"100","beforeKqlxPicUrl":null,"afterKqlxScore":"100","afterKqlxPicUrl":null,"beforeCnfcwrScore":"100","beforeCnfcwrPicUrl":null,"afterCnfcwrScore":"100","afterCnfcwrPicUrl":null,"beforeXdsjScore":"100","beforeXdsjPicUrl":null,"afterXdsjScore":"100","afterXdsjPicUrl":null,"beforeSwczScore":"100","beforeSwczPicUrl":null,"afterSwczScore":"100","afterSwczPicUrl":null,"beforeKqjhScore":"100","beforeKqjhPicUrl":null,"afterKqjhScore":"100","afterKqjhPicUrl":null,"beforeNsjjdScore":"100","beforeNsjjdPicUrl":null,"afterNsjjdScore":"100","afterNsjjdPicUrl":null,"beforeCnjxScore":"100","beforeCnjxPicUrl":null,"afterCnjxScore":"100","afterCnjxPicUrl":null,"beforeLxbmwgScore":"100","beforeLxbmwgPicUrl":null,"afterLxbmwgScore":"36","afterLxbmwgPicUrl":null,"beforeZfxbmwgScore":"100","beforeZfxbmwgPicUrl":null,"afterZfxbmwgScore":"100","afterZfxbmwgPicUrl":null,"beforeGfjbmwgScore":"100","beforeGfjbmwgPicUrl":null,"afterGfjbmwgScore":"100","afterGfjbmwgPicUrl":null,"beforeTfgdbmwgScore":"100","beforeTfgdbmwgPicUrl":null,"afterTfgdbmwgScore":"100","afterTfgdbmwgPicUrl":null,"beforeLnqbmwgScore":"100","beforeLnqbmwgPicUrl":null,"afterLnqbmwgScore":"100","afterLnqbmwgPicUrl":null,"beforeKtxtzlxnScore":"100","beforeKtxtzlxnPicUrl":null,"afterKtxtzlxnScore":"100","afterKtxtzlxnPicUrl":null,"beforeKtkqyxScore":"100","beforeKtkqyxPicUrl":null,"afterKtkqyxScore":"100","afterKtkqyxPicUrl":null,"beforeCfkyw":"100","afterCfkyw":"100","beforeScwpyw":"100","afterScwpyw":"100","beforeXcyw":"100","afterXcyw":"100","beforeEsyyw":"100","afterEsyyw":"100","beforeNspgyw":"100","afterNspgyw":"100","beforeXjmbyw":"100","afterXjmbyw":"100","beforeNsxfyw":"100","afterNsxfyw":"100","beforeQtyw":"100","afterQtyw":"100","beforeJq":"0","afterJq":null,"beforeTvoc":"0","afterTvoc":null,"beforePm25":"0","afterPm25":null,"beforeWd":"0","afterWd":null,"beforeSd":"0","afterSd":null,"createUser":"肖宝瑞测试","updateUser":null,"createTime":1554111458000,"updateTime":null,"beforeZjwsScore":null,"afterZjwsScore":null,"beforeYwjcScore":null,"afterYwjcScore":null,"beforeKtxtScore":null,"afterKtxtScore":null,"beforeYhqtScore":null,"afterYhqtScore":null,"beforeScore":null,"afterScore":null,"recommends":null,"rn":"1","pushmessage":1}]
         */

        private int tatol;
        private List<RowsBean> rows;

        public int getTatol() {
            return tatol;
        }

        public void setTatol(int tatol) {
            this.tatol = tatol;
        }

        public List<RowsBean> getRows() {
            return rows;
        }

        public void setRows(List<RowsBean> rows) {
            this.rows = rows;
        }

        public static class RowsBean {
            /**
             * receiveTime : 2019-04-01 09:20:10
             * sequenceCode : null
             * storeName : 车安达
             * orgPk : 01
             * projectName : 车内小健康
             * checkTechnician : 肖宝瑞测试
             * constructionTechnician : 肖宝瑞测试
             * receiveCarPk : 14
             * checkTypePk : 2
             * carOwnerRead : 0
             * id : 560
             * carNumber : 粤B12346
             * deviceno : null
             * readCount : 0
             * beforeWpbfScore : 57
             * beforeWpbfPicUrl : null
             * afterWpbfScore : 100
             * afterWpbfPicUrl : null
             * beforeKqlxScore : 100
             * beforeKqlxPicUrl : null
             * afterKqlxScore : 100
             * afterKqlxPicUrl : null
             * beforeCnfcwrScore : 100
             * beforeCnfcwrPicUrl : null
             * afterCnfcwrScore : 100
             * afterCnfcwrPicUrl : null
             * beforeXdsjScore : 100
             * beforeXdsjPicUrl : null
             * afterXdsjScore : 100
             * afterXdsjPicUrl : null
             * beforeSwczScore : 100
             * beforeSwczPicUrl : null
             * afterSwczScore : 100
             * afterSwczPicUrl : null
             * beforeKqjhScore : 100
             * beforeKqjhPicUrl : null
             * afterKqjhScore : 100
             * afterKqjhPicUrl : null
             * beforeNsjjdScore : 100
             * beforeNsjjdPicUrl : null
             * afterNsjjdScore : 100
             * afterNsjjdPicUrl : null
             * beforeCnjxScore : 100
             * beforeCnjxPicUrl : null
             * afterCnjxScore : 100
             * afterCnjxPicUrl : null
             * beforeLxbmwgScore : 100
             * beforeLxbmwgPicUrl : null
             * afterLxbmwgScore : 36
             * afterLxbmwgPicUrl : null
             * beforeZfxbmwgScore : 100
             * beforeZfxbmwgPicUrl : null
             * afterZfxbmwgScore : 100
             * afterZfxbmwgPicUrl : null
             * beforeGfjbmwgScore : 100
             * beforeGfjbmwgPicUrl : null
             * afterGfjbmwgScore : 100
             * afterGfjbmwgPicUrl : null
             * beforeTfgdbmwgScore : 100
             * beforeTfgdbmwgPicUrl : null
             * afterTfgdbmwgScore : 100
             * afterTfgdbmwgPicUrl : null
             * beforeLnqbmwgScore : 100
             * beforeLnqbmwgPicUrl : null
             * afterLnqbmwgScore : 100
             * afterLnqbmwgPicUrl : null
             * beforeKtxtzlxnScore : 100
             * beforeKtxtzlxnPicUrl : null
             * afterKtxtzlxnScore : 100
             * afterKtxtzlxnPicUrl : null
             * beforeKtkqyxScore : 100
             * beforeKtkqyxPicUrl : null
             * afterKtkqyxScore : 100
             * afterKtkqyxPicUrl : null
             * beforeCfkyw : 100
             * afterCfkyw : 100
             * beforeScwpyw : 100
             * afterScwpyw : 100
             * beforeXcyw : 100
             * afterXcyw : 100
             * beforeEsyyw : 100
             * afterEsyyw : 100
             * beforeNspgyw : 100
             * afterNspgyw : 100
             * beforeXjmbyw : 100
             * afterXjmbyw : 100
             * beforeNsxfyw : 100
             * afterNsxfyw : 100
             * beforeQtyw : 100
             * afterQtyw : 100
             * beforeJq : 0
             * afterJq : null
             * beforeTvoc : 0
             * afterTvoc : null
             * beforePm25 : 0
             * afterPm25 : null
             * beforeWd : 0
             * afterWd : null
             * beforeSd : 0
             * afterSd : null
             * createUser : 肖宝瑞测试
             * updateUser : null
             * createTime : 1554111458000
             * updateTime : null
             * beforeZjwsScore : null
             * afterZjwsScore : null
             * beforeYwjcScore : null
             * afterYwjcScore : null
             * beforeKtxtScore : null
             * afterKtxtScore : null
             * beforeYhqtScore : null
             * afterYhqtScore : null
             * beforeScore : null
             * afterScore : null
             * recommends : null
             * rn : 1
             * pushmessage : 1
             */

            private String receiveTime;
            private Object sequenceCode;
            private String storeName;
            private String orgPk;
            private String projectName;
            private String checkTechnician;
            private String constructionTechnician;
            private String receiveCarPk;
            private String checkTypePk;
            private int carOwnerRead;
            private int id;
            private String carNumber;
            private Object deviceno;
            private int readCount;
            private String beforeWpbfScore;
            private Object beforeWpbfPicUrl;
            private String afterWpbfScore;
            private Object afterWpbfPicUrl;
            private String beforeKqlxScore;
            private Object beforeKqlxPicUrl;
            private String afterKqlxScore;
            private Object afterKqlxPicUrl;
            private String beforeCnfcwrScore;
            private Object beforeCnfcwrPicUrl;
            private String afterCnfcwrScore;
            private Object afterCnfcwrPicUrl;
            private String beforeXdsjScore;
            private Object beforeXdsjPicUrl;
            private String afterXdsjScore;
            private Object afterXdsjPicUrl;
            private String beforeSwczScore;
            private Object beforeSwczPicUrl;
            private String afterSwczScore;
            private Object afterSwczPicUrl;
            private String beforeKqjhScore;
            private Object beforeKqjhPicUrl;
            private String afterKqjhScore;
            private Object afterKqjhPicUrl;
            private String beforeNsjjdScore;
            private Object beforeNsjjdPicUrl;
            private String afterNsjjdScore;
            private Object afterNsjjdPicUrl;
            private String beforeCnjxScore;
            private Object beforeCnjxPicUrl;
            private String afterCnjxScore;
            private Object afterCnjxPicUrl;
            private String beforeLxbmwgScore;
            private Object beforeLxbmwgPicUrl;
            private String afterLxbmwgScore;
            private Object afterLxbmwgPicUrl;
            private String beforeZfxbmwgScore;
            private Object beforeZfxbmwgPicUrl;
            private String afterZfxbmwgScore;
            private Object afterZfxbmwgPicUrl;
            private String beforeGfjbmwgScore;
            private Object beforeGfjbmwgPicUrl;
            private String afterGfjbmwgScore;
            private Object afterGfjbmwgPicUrl;
            private String beforeTfgdbmwgScore;
            private Object beforeTfgdbmwgPicUrl;
            private String afterTfgdbmwgScore;
            private Object afterTfgdbmwgPicUrl;
            private String beforeLnqbmwgScore;
            private Object beforeLnqbmwgPicUrl;
            private String afterLnqbmwgScore;
            private Object afterLnqbmwgPicUrl;
            private String beforeKtxtzlxnScore;
            private Object beforeKtxtzlxnPicUrl;
            private String afterKtxtzlxnScore;
            private Object afterKtxtzlxnPicUrl;
            private String beforeKtkqyxScore;
            private Object beforeKtkqyxPicUrl;
            private String afterKtkqyxScore;
            private Object afterKtkqyxPicUrl;
            private String beforeCfkyw;
            private String afterCfkyw;
            private String beforeScwpyw;
            private String afterScwpyw;
            private String beforeXcyw;
            private String afterXcyw;
            private String beforeEsyyw;
            private String afterEsyyw;
            private String beforeNspgyw;
            private String afterNspgyw;
            private String beforeXjmbyw;
            private String afterXjmbyw;
            private String beforeNsxfyw;
            private String afterNsxfyw;
            private String beforeQtyw;
            private String afterQtyw;
            private String beforeJq;
            private Object afterJq;
            private String beforeTvoc;
            private Object afterTvoc;
            private String beforePm25;
            private Object afterPm25;
            private String beforeWd;
            private Object afterWd;
            private String beforeSd;
            private Object afterSd;
            private String createUser;
            private Object updateUser;
            private long createTime;
            private Object updateTime;
            private Object beforeZjwsScore;
            private Object afterZjwsScore;
            private Object beforeYwjcScore;
            private Object afterYwjcScore;
            private Object beforeKtxtScore;
            private Object afterKtxtScore;
            private Object beforeYhqtScore;
            private Object afterYhqtScore;
            private Object beforeScore;
            private Object afterScore;
            private Object recommends;
            private String rn;
            private int pushmessage;

            public String getReceiveTime() {
                return receiveTime;
            }

            public void setReceiveTime(String receiveTime) {
                this.receiveTime = receiveTime;
            }

            public Object getSequenceCode() {
                return sequenceCode;
            }

            public void setSequenceCode(Object sequenceCode) {
                this.sequenceCode = sequenceCode;
            }

            public String getStoreName() {
                return storeName;
            }

            public void setStoreName(String storeName) {
                this.storeName = storeName;
            }

            public String getOrgPk() {
                return orgPk;
            }

            public void setOrgPk(String orgPk) {
                this.orgPk = orgPk;
            }

            public String getProjectName() {
                return projectName;
            }

            public void setProjectName(String projectName) {
                this.projectName = projectName;
            }

            public String getCheckTechnician() {
                return checkTechnician;
            }

            public void setCheckTechnician(String checkTechnician) {
                this.checkTechnician = checkTechnician;
            }

            public String getConstructionTechnician() {
                return constructionTechnician;
            }

            public void setConstructionTechnician(String constructionTechnician) {
                this.constructionTechnician = constructionTechnician;
            }

            public String getReceiveCarPk() {
                return receiveCarPk;
            }

            public void setReceiveCarPk(String receiveCarPk) {
                this.receiveCarPk = receiveCarPk;
            }

            public String getCheckTypePk() {
                return checkTypePk;
            }

            public void setCheckTypePk(String checkTypePk) {
                this.checkTypePk = checkTypePk;
            }

            public int getCarOwnerRead() {
                return carOwnerRead;
            }

            public void setCarOwnerRead(int carOwnerRead) {
                this.carOwnerRead = carOwnerRead;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCarNumber() {
                return carNumber;
            }

            public void setCarNumber(String carNumber) {
                this.carNumber = carNumber;
            }

            public Object getDeviceno() {
                return deviceno;
            }

            public void setDeviceno(Object deviceno) {
                this.deviceno = deviceno;
            }

            public int getReadCount() {
                return readCount;
            }

            public void setReadCount(int readCount) {
                this.readCount = readCount;
            }

            public String getBeforeWpbfScore() {
                return beforeWpbfScore;
            }

            public void setBeforeWpbfScore(String beforeWpbfScore) {
                this.beforeWpbfScore = beforeWpbfScore;
            }

            public Object getBeforeWpbfPicUrl() {
                return beforeWpbfPicUrl;
            }

            public void setBeforeWpbfPicUrl(Object beforeWpbfPicUrl) {
                this.beforeWpbfPicUrl = beforeWpbfPicUrl;
            }

            public String getAfterWpbfScore() {
                return afterWpbfScore;
            }

            public void setAfterWpbfScore(String afterWpbfScore) {
                this.afterWpbfScore = afterWpbfScore;
            }

            public Object getAfterWpbfPicUrl() {
                return afterWpbfPicUrl;
            }

            public void setAfterWpbfPicUrl(Object afterWpbfPicUrl) {
                this.afterWpbfPicUrl = afterWpbfPicUrl;
            }

            public String getBeforeKqlxScore() {
                return beforeKqlxScore;
            }

            public void setBeforeKqlxScore(String beforeKqlxScore) {
                this.beforeKqlxScore = beforeKqlxScore;
            }

            public Object getBeforeKqlxPicUrl() {
                return beforeKqlxPicUrl;
            }

            public void setBeforeKqlxPicUrl(Object beforeKqlxPicUrl) {
                this.beforeKqlxPicUrl = beforeKqlxPicUrl;
            }

            public String getAfterKqlxScore() {
                return afterKqlxScore;
            }

            public void setAfterKqlxScore(String afterKqlxScore) {
                this.afterKqlxScore = afterKqlxScore;
            }

            public Object getAfterKqlxPicUrl() {
                return afterKqlxPicUrl;
            }

            public void setAfterKqlxPicUrl(Object afterKqlxPicUrl) {
                this.afterKqlxPicUrl = afterKqlxPicUrl;
            }

            public String getBeforeCnfcwrScore() {
                return beforeCnfcwrScore;
            }

            public void setBeforeCnfcwrScore(String beforeCnfcwrScore) {
                this.beforeCnfcwrScore = beforeCnfcwrScore;
            }

            public Object getBeforeCnfcwrPicUrl() {
                return beforeCnfcwrPicUrl;
            }

            public void setBeforeCnfcwrPicUrl(Object beforeCnfcwrPicUrl) {
                this.beforeCnfcwrPicUrl = beforeCnfcwrPicUrl;
            }

            public String getAfterCnfcwrScore() {
                return afterCnfcwrScore;
            }

            public void setAfterCnfcwrScore(String afterCnfcwrScore) {
                this.afterCnfcwrScore = afterCnfcwrScore;
            }

            public Object getAfterCnfcwrPicUrl() {
                return afterCnfcwrPicUrl;
            }

            public void setAfterCnfcwrPicUrl(Object afterCnfcwrPicUrl) {
                this.afterCnfcwrPicUrl = afterCnfcwrPicUrl;
            }

            public String getBeforeXdsjScore() {
                return beforeXdsjScore;
            }

            public void setBeforeXdsjScore(String beforeXdsjScore) {
                this.beforeXdsjScore = beforeXdsjScore;
            }

            public Object getBeforeXdsjPicUrl() {
                return beforeXdsjPicUrl;
            }

            public void setBeforeXdsjPicUrl(Object beforeXdsjPicUrl) {
                this.beforeXdsjPicUrl = beforeXdsjPicUrl;
            }

            public String getAfterXdsjScore() {
                return afterXdsjScore;
            }

            public void setAfterXdsjScore(String afterXdsjScore) {
                this.afterXdsjScore = afterXdsjScore;
            }

            public Object getAfterXdsjPicUrl() {
                return afterXdsjPicUrl;
            }

            public void setAfterXdsjPicUrl(Object afterXdsjPicUrl) {
                this.afterXdsjPicUrl = afterXdsjPicUrl;
            }

            public String getBeforeSwczScore() {
                return beforeSwczScore;
            }

            public void setBeforeSwczScore(String beforeSwczScore) {
                this.beforeSwczScore = beforeSwczScore;
            }

            public Object getBeforeSwczPicUrl() {
                return beforeSwczPicUrl;
            }

            public void setBeforeSwczPicUrl(Object beforeSwczPicUrl) {
                this.beforeSwczPicUrl = beforeSwczPicUrl;
            }

            public String getAfterSwczScore() {
                return afterSwczScore;
            }

            public void setAfterSwczScore(String afterSwczScore) {
                this.afterSwczScore = afterSwczScore;
            }

            public Object getAfterSwczPicUrl() {
                return afterSwczPicUrl;
            }

            public void setAfterSwczPicUrl(Object afterSwczPicUrl) {
                this.afterSwczPicUrl = afterSwczPicUrl;
            }

            public String getBeforeKqjhScore() {
                return beforeKqjhScore;
            }

            public void setBeforeKqjhScore(String beforeKqjhScore) {
                this.beforeKqjhScore = beforeKqjhScore;
            }

            public Object getBeforeKqjhPicUrl() {
                return beforeKqjhPicUrl;
            }

            public void setBeforeKqjhPicUrl(Object beforeKqjhPicUrl) {
                this.beforeKqjhPicUrl = beforeKqjhPicUrl;
            }

            public String getAfterKqjhScore() {
                return afterKqjhScore;
            }

            public void setAfterKqjhScore(String afterKqjhScore) {
                this.afterKqjhScore = afterKqjhScore;
            }

            public Object getAfterKqjhPicUrl() {
                return afterKqjhPicUrl;
            }

            public void setAfterKqjhPicUrl(Object afterKqjhPicUrl) {
                this.afterKqjhPicUrl = afterKqjhPicUrl;
            }

            public String getBeforeNsjjdScore() {
                return beforeNsjjdScore;
            }

            public void setBeforeNsjjdScore(String beforeNsjjdScore) {
                this.beforeNsjjdScore = beforeNsjjdScore;
            }

            public Object getBeforeNsjjdPicUrl() {
                return beforeNsjjdPicUrl;
            }

            public void setBeforeNsjjdPicUrl(Object beforeNsjjdPicUrl) {
                this.beforeNsjjdPicUrl = beforeNsjjdPicUrl;
            }

            public String getAfterNsjjdScore() {
                return afterNsjjdScore;
            }

            public void setAfterNsjjdScore(String afterNsjjdScore) {
                this.afterNsjjdScore = afterNsjjdScore;
            }

            public Object getAfterNsjjdPicUrl() {
                return afterNsjjdPicUrl;
            }

            public void setAfterNsjjdPicUrl(Object afterNsjjdPicUrl) {
                this.afterNsjjdPicUrl = afterNsjjdPicUrl;
            }

            public String getBeforeCnjxScore() {
                return beforeCnjxScore;
            }

            public void setBeforeCnjxScore(String beforeCnjxScore) {
                this.beforeCnjxScore = beforeCnjxScore;
            }

            public Object getBeforeCnjxPicUrl() {
                return beforeCnjxPicUrl;
            }

            public void setBeforeCnjxPicUrl(Object beforeCnjxPicUrl) {
                this.beforeCnjxPicUrl = beforeCnjxPicUrl;
            }

            public String getAfterCnjxScore() {
                return afterCnjxScore;
            }

            public void setAfterCnjxScore(String afterCnjxScore) {
                this.afterCnjxScore = afterCnjxScore;
            }

            public Object getAfterCnjxPicUrl() {
                return afterCnjxPicUrl;
            }

            public void setAfterCnjxPicUrl(Object afterCnjxPicUrl) {
                this.afterCnjxPicUrl = afterCnjxPicUrl;
            }

            public String getBeforeLxbmwgScore() {
                return beforeLxbmwgScore;
            }

            public void setBeforeLxbmwgScore(String beforeLxbmwgScore) {
                this.beforeLxbmwgScore = beforeLxbmwgScore;
            }

            public Object getBeforeLxbmwgPicUrl() {
                return beforeLxbmwgPicUrl;
            }

            public void setBeforeLxbmwgPicUrl(Object beforeLxbmwgPicUrl) {
                this.beforeLxbmwgPicUrl = beforeLxbmwgPicUrl;
            }

            public String getAfterLxbmwgScore() {
                return afterLxbmwgScore;
            }

            public void setAfterLxbmwgScore(String afterLxbmwgScore) {
                this.afterLxbmwgScore = afterLxbmwgScore;
            }

            public Object getAfterLxbmwgPicUrl() {
                return afterLxbmwgPicUrl;
            }

            public void setAfterLxbmwgPicUrl(Object afterLxbmwgPicUrl) {
                this.afterLxbmwgPicUrl = afterLxbmwgPicUrl;
            }

            public String getBeforeZfxbmwgScore() {
                return beforeZfxbmwgScore;
            }

            public void setBeforeZfxbmwgScore(String beforeZfxbmwgScore) {
                this.beforeZfxbmwgScore = beforeZfxbmwgScore;
            }

            public Object getBeforeZfxbmwgPicUrl() {
                return beforeZfxbmwgPicUrl;
            }

            public void setBeforeZfxbmwgPicUrl(Object beforeZfxbmwgPicUrl) {
                this.beforeZfxbmwgPicUrl = beforeZfxbmwgPicUrl;
            }

            public String getAfterZfxbmwgScore() {
                return afterZfxbmwgScore;
            }

            public void setAfterZfxbmwgScore(String afterZfxbmwgScore) {
                this.afterZfxbmwgScore = afterZfxbmwgScore;
            }

            public Object getAfterZfxbmwgPicUrl() {
                return afterZfxbmwgPicUrl;
            }

            public void setAfterZfxbmwgPicUrl(Object afterZfxbmwgPicUrl) {
                this.afterZfxbmwgPicUrl = afterZfxbmwgPicUrl;
            }

            public String getBeforeGfjbmwgScore() {
                return beforeGfjbmwgScore;
            }

            public void setBeforeGfjbmwgScore(String beforeGfjbmwgScore) {
                this.beforeGfjbmwgScore = beforeGfjbmwgScore;
            }

            public Object getBeforeGfjbmwgPicUrl() {
                return beforeGfjbmwgPicUrl;
            }

            public void setBeforeGfjbmwgPicUrl(Object beforeGfjbmwgPicUrl) {
                this.beforeGfjbmwgPicUrl = beforeGfjbmwgPicUrl;
            }

            public String getAfterGfjbmwgScore() {
                return afterGfjbmwgScore;
            }

            public void setAfterGfjbmwgScore(String afterGfjbmwgScore) {
                this.afterGfjbmwgScore = afterGfjbmwgScore;
            }

            public Object getAfterGfjbmwgPicUrl() {
                return afterGfjbmwgPicUrl;
            }

            public void setAfterGfjbmwgPicUrl(Object afterGfjbmwgPicUrl) {
                this.afterGfjbmwgPicUrl = afterGfjbmwgPicUrl;
            }

            public String getBeforeTfgdbmwgScore() {
                return beforeTfgdbmwgScore;
            }

            public void setBeforeTfgdbmwgScore(String beforeTfgdbmwgScore) {
                this.beforeTfgdbmwgScore = beforeTfgdbmwgScore;
            }

            public Object getBeforeTfgdbmwgPicUrl() {
                return beforeTfgdbmwgPicUrl;
            }

            public void setBeforeTfgdbmwgPicUrl(Object beforeTfgdbmwgPicUrl) {
                this.beforeTfgdbmwgPicUrl = beforeTfgdbmwgPicUrl;
            }

            public String getAfterTfgdbmwgScore() {
                return afterTfgdbmwgScore;
            }

            public void setAfterTfgdbmwgScore(String afterTfgdbmwgScore) {
                this.afterTfgdbmwgScore = afterTfgdbmwgScore;
            }

            public Object getAfterTfgdbmwgPicUrl() {
                return afterTfgdbmwgPicUrl;
            }

            public void setAfterTfgdbmwgPicUrl(Object afterTfgdbmwgPicUrl) {
                this.afterTfgdbmwgPicUrl = afterTfgdbmwgPicUrl;
            }

            public String getBeforeLnqbmwgScore() {
                return beforeLnqbmwgScore;
            }

            public void setBeforeLnqbmwgScore(String beforeLnqbmwgScore) {
                this.beforeLnqbmwgScore = beforeLnqbmwgScore;
            }

            public Object getBeforeLnqbmwgPicUrl() {
                return beforeLnqbmwgPicUrl;
            }

            public void setBeforeLnqbmwgPicUrl(Object beforeLnqbmwgPicUrl) {
                this.beforeLnqbmwgPicUrl = beforeLnqbmwgPicUrl;
            }

            public String getAfterLnqbmwgScore() {
                return afterLnqbmwgScore;
            }

            public void setAfterLnqbmwgScore(String afterLnqbmwgScore) {
                this.afterLnqbmwgScore = afterLnqbmwgScore;
            }

            public Object getAfterLnqbmwgPicUrl() {
                return afterLnqbmwgPicUrl;
            }

            public void setAfterLnqbmwgPicUrl(Object afterLnqbmwgPicUrl) {
                this.afterLnqbmwgPicUrl = afterLnqbmwgPicUrl;
            }

            public String getBeforeKtxtzlxnScore() {
                return beforeKtxtzlxnScore;
            }

            public void setBeforeKtxtzlxnScore(String beforeKtxtzlxnScore) {
                this.beforeKtxtzlxnScore = beforeKtxtzlxnScore;
            }

            public Object getBeforeKtxtzlxnPicUrl() {
                return beforeKtxtzlxnPicUrl;
            }

            public void setBeforeKtxtzlxnPicUrl(Object beforeKtxtzlxnPicUrl) {
                this.beforeKtxtzlxnPicUrl = beforeKtxtzlxnPicUrl;
            }

            public String getAfterKtxtzlxnScore() {
                return afterKtxtzlxnScore;
            }

            public void setAfterKtxtzlxnScore(String afterKtxtzlxnScore) {
                this.afterKtxtzlxnScore = afterKtxtzlxnScore;
            }

            public Object getAfterKtxtzlxnPicUrl() {
                return afterKtxtzlxnPicUrl;
            }

            public void setAfterKtxtzlxnPicUrl(Object afterKtxtzlxnPicUrl) {
                this.afterKtxtzlxnPicUrl = afterKtxtzlxnPicUrl;
            }

            public String getBeforeKtkqyxScore() {
                return beforeKtkqyxScore;
            }

            public void setBeforeKtkqyxScore(String beforeKtkqyxScore) {
                this.beforeKtkqyxScore = beforeKtkqyxScore;
            }

            public Object getBeforeKtkqyxPicUrl() {
                return beforeKtkqyxPicUrl;
            }

            public void setBeforeKtkqyxPicUrl(Object beforeKtkqyxPicUrl) {
                this.beforeKtkqyxPicUrl = beforeKtkqyxPicUrl;
            }

            public String getAfterKtkqyxScore() {
                return afterKtkqyxScore;
            }

            public void setAfterKtkqyxScore(String afterKtkqyxScore) {
                this.afterKtkqyxScore = afterKtkqyxScore;
            }

            public Object getAfterKtkqyxPicUrl() {
                return afterKtkqyxPicUrl;
            }

            public void setAfterKtkqyxPicUrl(Object afterKtkqyxPicUrl) {
                this.afterKtkqyxPicUrl = afterKtkqyxPicUrl;
            }

            public String getBeforeCfkyw() {
                return beforeCfkyw;
            }

            public void setBeforeCfkyw(String beforeCfkyw) {
                this.beforeCfkyw = beforeCfkyw;
            }

            public String getAfterCfkyw() {
                return afterCfkyw;
            }

            public void setAfterCfkyw(String afterCfkyw) {
                this.afterCfkyw = afterCfkyw;
            }

            public String getBeforeScwpyw() {
                return beforeScwpyw;
            }

            public void setBeforeScwpyw(String beforeScwpyw) {
                this.beforeScwpyw = beforeScwpyw;
            }

            public String getAfterScwpyw() {
                return afterScwpyw;
            }

            public void setAfterScwpyw(String afterScwpyw) {
                this.afterScwpyw = afterScwpyw;
            }

            public String getBeforeXcyw() {
                return beforeXcyw;
            }

            public void setBeforeXcyw(String beforeXcyw) {
                this.beforeXcyw = beforeXcyw;
            }

            public String getAfterXcyw() {
                return afterXcyw;
            }

            public void setAfterXcyw(String afterXcyw) {
                this.afterXcyw = afterXcyw;
            }

            public String getBeforeEsyyw() {
                return beforeEsyyw;
            }

            public void setBeforeEsyyw(String beforeEsyyw) {
                this.beforeEsyyw = beforeEsyyw;
            }

            public String getAfterEsyyw() {
                return afterEsyyw;
            }

            public void setAfterEsyyw(String afterEsyyw) {
                this.afterEsyyw = afterEsyyw;
            }

            public String getBeforeNspgyw() {
                return beforeNspgyw;
            }

            public void setBeforeNspgyw(String beforeNspgyw) {
                this.beforeNspgyw = beforeNspgyw;
            }

            public String getAfterNspgyw() {
                return afterNspgyw;
            }

            public void setAfterNspgyw(String afterNspgyw) {
                this.afterNspgyw = afterNspgyw;
            }

            public String getBeforeXjmbyw() {
                return beforeXjmbyw;
            }

            public void setBeforeXjmbyw(String beforeXjmbyw) {
                this.beforeXjmbyw = beforeXjmbyw;
            }

            public String getAfterXjmbyw() {
                return afterXjmbyw;
            }

            public void setAfterXjmbyw(String afterXjmbyw) {
                this.afterXjmbyw = afterXjmbyw;
            }

            public String getBeforeNsxfyw() {
                return beforeNsxfyw;
            }

            public void setBeforeNsxfyw(String beforeNsxfyw) {
                this.beforeNsxfyw = beforeNsxfyw;
            }

            public String getAfterNsxfyw() {
                return afterNsxfyw;
            }

            public void setAfterNsxfyw(String afterNsxfyw) {
                this.afterNsxfyw = afterNsxfyw;
            }

            public String getBeforeQtyw() {
                return beforeQtyw;
            }

            public void setBeforeQtyw(String beforeQtyw) {
                this.beforeQtyw = beforeQtyw;
            }

            public String getAfterQtyw() {
                return afterQtyw;
            }

            public void setAfterQtyw(String afterQtyw) {
                this.afterQtyw = afterQtyw;
            }

            public String getBeforeJq() {
                return beforeJq;
            }

            public void setBeforeJq(String beforeJq) {
                this.beforeJq = beforeJq;
            }

            public Object getAfterJq() {
                return afterJq;
            }

            public void setAfterJq(Object afterJq) {
                this.afterJq = afterJq;
            }

            public String getBeforeTvoc() {
                return beforeTvoc;
            }

            public void setBeforeTvoc(String beforeTvoc) {
                this.beforeTvoc = beforeTvoc;
            }

            public Object getAfterTvoc() {
                return afterTvoc;
            }

            public void setAfterTvoc(Object afterTvoc) {
                this.afterTvoc = afterTvoc;
            }

            public String getBeforePm25() {
                return beforePm25;
            }

            public void setBeforePm25(String beforePm25) {
                this.beforePm25 = beforePm25;
            }

            public Object getAfterPm25() {
                return afterPm25;
            }

            public void setAfterPm25(Object afterPm25) {
                this.afterPm25 = afterPm25;
            }

            public String getBeforeWd() {
                return beforeWd;
            }

            public void setBeforeWd(String beforeWd) {
                this.beforeWd = beforeWd;
            }

            public Object getAfterWd() {
                return afterWd;
            }

            public void setAfterWd(Object afterWd) {
                this.afterWd = afterWd;
            }

            public String getBeforeSd() {
                return beforeSd;
            }

            public void setBeforeSd(String beforeSd) {
                this.beforeSd = beforeSd;
            }

            public Object getAfterSd() {
                return afterSd;
            }

            public void setAfterSd(Object afterSd) {
                this.afterSd = afterSd;
            }

            public String getCreateUser() {
                return createUser;
            }

            public void setCreateUser(String createUser) {
                this.createUser = createUser;
            }

            public Object getUpdateUser() {
                return updateUser;
            }

            public void setUpdateUser(Object updateUser) {
                this.updateUser = updateUser;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public Object getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(Object updateTime) {
                this.updateTime = updateTime;
            }

            public Object getBeforeZjwsScore() {
                return beforeZjwsScore;
            }

            public void setBeforeZjwsScore(Object beforeZjwsScore) {
                this.beforeZjwsScore = beforeZjwsScore;
            }

            public Object getAfterZjwsScore() {
                return afterZjwsScore;
            }

            public void setAfterZjwsScore(Object afterZjwsScore) {
                this.afterZjwsScore = afterZjwsScore;
            }

            public Object getBeforeYwjcScore() {
                return beforeYwjcScore;
            }

            public void setBeforeYwjcScore(Object beforeYwjcScore) {
                this.beforeYwjcScore = beforeYwjcScore;
            }

            public Object getAfterYwjcScore() {
                return afterYwjcScore;
            }

            public void setAfterYwjcScore(Object afterYwjcScore) {
                this.afterYwjcScore = afterYwjcScore;
            }

            public Object getBeforeKtxtScore() {
                return beforeKtxtScore;
            }

            public void setBeforeKtxtScore(Object beforeKtxtScore) {
                this.beforeKtxtScore = beforeKtxtScore;
            }

            public Object getAfterKtxtScore() {
                return afterKtxtScore;
            }

            public void setAfterKtxtScore(Object afterKtxtScore) {
                this.afterKtxtScore = afterKtxtScore;
            }

            public Object getBeforeYhqtScore() {
                return beforeYhqtScore;
            }

            public void setBeforeYhqtScore(Object beforeYhqtScore) {
                this.beforeYhqtScore = beforeYhqtScore;
            }

            public Object getAfterYhqtScore() {
                return afterYhqtScore;
            }

            public void setAfterYhqtScore(Object afterYhqtScore) {
                this.afterYhqtScore = afterYhqtScore;
            }

            public Object getBeforeScore() {
                return beforeScore;
            }

            public void setBeforeScore(Object beforeScore) {
                this.beforeScore = beforeScore;
            }

            public Object getAfterScore() {
                return afterScore;
            }

            public void setAfterScore(Object afterScore) {
                this.afterScore = afterScore;
            }

            public Object getRecommends() {
                return recommends;
            }

            public void setRecommends(Object recommends) {
                this.recommends = recommends;
            }

            public String getRn() {
                return rn;
            }

            public void setRn(String rn) {
                this.rn = rn;
            }

            public int getPushmessage() {
                return pushmessage;
            }

            public void setPushmessage(int pushmessage) {
                this.pushmessage = pushmessage;
            }
        }
    }
}
