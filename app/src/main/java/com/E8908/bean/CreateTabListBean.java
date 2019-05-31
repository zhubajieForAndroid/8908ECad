package com.E8908.bean;

import java.util.List;

public class CreateTabListBean {

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

        private int total;
        private List<RowsBean> rows;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<RowsBean> getRows() {
            return rows;
        }

        public void setRows(List<RowsBean> rows) {
            this.rows = rows;
        }

        public static class RowsBean {
            /**
             * temperature : 30.0
             * humidity : 40.7
             * formaldehyde : 0.0425
             * checkDate : 2018-11-14 18:48:45
             * carNum : 豫2 A12222
             * deviceno : 00170073
             * pm25 : 34.0
             * tvoc : 0.05
             */

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
