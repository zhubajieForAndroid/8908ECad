package com.E8908.bean;

import java.util.List;

public class UpLoadResultBean {

    /**
     * code : 9
     * response : []
     * message : 设备ID未绑定门店
     */

    private int code;
    private String message;
    private List<?> response;

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

    public List<?> getResponse() {
        return response;
    }

    public void setResponse(List<?> response) {
        this.response = response;
    }
}
