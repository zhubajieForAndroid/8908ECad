package com.E8908.textFunction;

import java.util.List;

public class TextTabBean {

    /**
     * code : 0
     * response : [{"id":12,"equipmentType":"ACC-8908E","equipmentCode":"01870018","uploadTime":1559205233000,"fillStatus":0,"recycleStatus":0,"atomizeStatus":0,"sterilizeStatus":0,"purifyStatus":0,"connectStatus":0,"cycleTestTimes":0,"wifiStatus":0,"dtustatus":0},{"id":11,"equipmentType":"ACC-8908E","equipmentCode":"01870018","uploadTime":1559204998000,"fillStatus":0,"recycleStatus":0,"atomizeStatus":0,"sterilizeStatus":0,"purifyStatus":0,"connectStatus":0,"cycleTestTimes":0,"wifiStatus":0,"dtustatus":0}]
     * message : success
     */

    private int code;
    private String message;
    private List<ResponseBean> response;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ResponseBean> getResponse() {
        return response;
    }

    public void setResponse(List<ResponseBean> response) {
        this.response = response;
    }

    public static class ResponseBean {
        /**
         * id : 12
         * equipmentType : ACC-8908E
         * equipmentCode : 01870018
         * uploadTime : 1559205233000
         * fillStatus : 0
         * recycleStatus : 0
         * atomizeStatus : 0
         * sterilizeStatus : 0
         * purifyStatus : 0
         * connectStatus : 0
         * cycleTestTimes : 0
         * wifiStatus : 0
         * dtustatus : 0
         */

        private int id;
        private String equipmentType;
        private String equipmentCode;
        private long uploadTime;
        private int fillStatus;
        private int recycleStatus;
        private int atomizeStatus;
        private int sterilizeStatus;
        private int purifyStatus;
        private int connectStatus;
        private int cycleTestTimes;
        private int wifiStatus;
        private int dtustatus;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getEquipmentType() {
            return equipmentType;
        }

        public void setEquipmentType(String equipmentType) {
            this.equipmentType = equipmentType;
        }

        public String getEquipmentCode() {
            return equipmentCode;
        }

        public void setEquipmentCode(String equipmentCode) {
            this.equipmentCode = equipmentCode;
        }

        public long getUploadTime() {
            return uploadTime;
        }

        public void setUploadTime(long uploadTime) {
            this.uploadTime = uploadTime;
        }

        public int getFillStatus() {
            return fillStatus;
        }

        public void setFillStatus(int fillStatus) {
            this.fillStatus = fillStatus;
        }

        public int getRecycleStatus() {
            return recycleStatus;
        }

        public void setRecycleStatus(int recycleStatus) {
            this.recycleStatus = recycleStatus;
        }

        public int getAtomizeStatus() {
            return atomizeStatus;
        }

        public void setAtomizeStatus(int atomizeStatus) {
            this.atomizeStatus = atomizeStatus;
        }

        public int getSterilizeStatus() {
            return sterilizeStatus;
        }

        public void setSterilizeStatus(int sterilizeStatus) {
            this.sterilizeStatus = sterilizeStatus;
        }

        public int getPurifyStatus() {
            return purifyStatus;
        }

        public void setPurifyStatus(int purifyStatus) {
            this.purifyStatus = purifyStatus;
        }

        public int getConnectStatus() {
            return connectStatus;
        }

        public void setConnectStatus(int connectStatus) {
            this.connectStatus = connectStatus;
        }

        public int getCycleTestTimes() {
            return cycleTestTimes;
        }

        public void setCycleTestTimes(int cycleTestTimes) {
            this.cycleTestTimes = cycleTestTimes;
        }

        public int getWifiStatus() {
            return wifiStatus;
        }

        public void setWifiStatus(int wifiStatus) {
            this.wifiStatus = wifiStatus;
        }

        public int getDtustatus() {
            return dtustatus;
        }

        public void setDtustatus(int dtustatus) {
            this.dtustatus = dtustatus;
        }
    }
}
