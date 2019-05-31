package com.E8908.base;

import java.util.List;

public class CurverBean {



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
        private List<BeforeBean> before;
        private List<AfterBean> after;

        public List<BeforeBean> getBefore() {
            return before;
        }

        public void setBefore(List<BeforeBean> before) {
            this.before = before;
        }

        public List<AfterBean> getAfter() {
            return after;
        }

        public void setAfter(List<AfterBean> after) {
            this.after = after;
        }

        public static class BeforeBean {


            private String temperature;
            private String humidity;
            private String formaldehyde;
            private String checkDate;
            private String carNum;
            private String deviceno;
            private String pm25;
            private String tvoc;

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getHumidity() {
                return humidity;
            }

            public void setHumidity(String humidity) {
                this.humidity = humidity;
            }

            public String getFormaldehyde() {
                return formaldehyde;
            }

            public void setFormaldehyde(String formaldehyde) {
                this.formaldehyde = formaldehyde;
            }

            public String getCheckDate() {
                return checkDate;
            }

            public void setCheckDate(String checkDate) {
                this.checkDate = checkDate;
            }

            public String getCarNum() {
                return carNum;
            }

            public void setCarNum(String carNum) {
                this.carNum = carNum;
            }

            public String getDeviceno() {
                return deviceno;
            }

            public void setDeviceno(String deviceno) {
                this.deviceno = deviceno;
            }

            public String getPm25() {
                return pm25;
            }

            public void setPm25(String pm25) {
                this.pm25 = pm25;
            }

            public String getTvoc() {
                return tvoc;
            }

            public void setTvoc(String tvoc) {
                this.tvoc = tvoc;
            }
        }

        public static class AfterBean {


            private String temperature;
            private String humidity;
            private String formaldehyde;
            private String checkDate;
            private String carNum;
            private String deviceno;
            private String pm25;
            private String tvoc;

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getHumidity() {
                return humidity;
            }

            public void setHumidity(String humidity) {
                this.humidity = humidity;
            }

            public String getFormaldehyde() {
                return formaldehyde;
            }

            public void setFormaldehyde(String formaldehyde) {
                this.formaldehyde = formaldehyde;
            }

            public String getCheckDate() {
                return checkDate;
            }

            public void setCheckDate(String checkDate) {
                this.checkDate = checkDate;
            }

            public String getCarNum() {
                return carNum;
            }

            public void setCarNum(String carNum) {
                this.carNum = carNum;
            }

            public String getDeviceno() {
                return deviceno;
            }

            public void setDeviceno(String deviceno) {
                this.deviceno = deviceno;
            }

            public String getPm25() {
                return pm25;
            }

            public void setPm25(String pm25) {
                this.pm25 = pm25;
            }

            public String getTvoc() {
                return tvoc;
            }

            public void setTvoc(String tvoc) {
                this.tvoc = tvoc;
            }
        }
    }
}
