package com.cad.bean;

import java.util.List;

/**
 * Created by dell on 2017/12/21.
 */

public class EquipmentOnlineBean {


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
        private String createUser;
        private String equipmentID;
        private String id;
        private String mcuid;
        private String simNum;
        private String simPassword;
        private String message;

        public void setMessagestr(String message) {
            this.message = message;
        }

        public String getMessagestr() {
            return message;
        }

        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public String getEquipmentID() {
            return equipmentID;
        }

        public void setEquipmentID(String equipmentID) {
            this.equipmentID = equipmentID;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMcuid() {
            return mcuid;
        }

        public void setMcuid(String mcuid) {
            this.mcuid = mcuid;
        }

        public String getSimNum() {
            return simNum;
        }

        public void setSimNum(String simNum) {
            this.simNum = simNum;
        }

        public String getSimPassword() {
            return simPassword;
        }

        public void setSimPassword(String simPassword) {
            this.simPassword = simPassword;
        }
    }
}
