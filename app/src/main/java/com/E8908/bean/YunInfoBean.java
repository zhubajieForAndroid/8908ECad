package com.E8908.bean;

import java.util.List;

public class YunInfoBean {

    /**
     * obj : [{"carNo":"冀R85445","isCheck":"0","isUploadCase":"0","pk":"122","simpleName":"第二养护中心"}]
     * success : true
     */

    private boolean success;
    private List<ObjBean> obj;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<ObjBean> getObj() {
        return obj;
    }

    public void setObj(List<ObjBean> obj) {
        this.obj = obj;
    }

    public static class ObjBean {
        /**
         * carNo : 冀R85445
         * isCheck : 0
         * isUploadCase : 0
         * pk : 122
         * simpleName : 第二养护中心
         */

        private String carNo;
        private String isCheck;
        private String isUploadCase;
        private String pk;
        private String simpleName;

        public String getCarNo() {
            return carNo;
        }

        public void setCarNo(String carNo) {
            this.carNo = carNo;
        }

        public String getIsCheck() {
            return isCheck;
        }

        public void setIsCheck(String isCheck) {
            this.isCheck = isCheck;
        }

        public String getIsUploadCase() {
            return isUploadCase;
        }

        public void setIsUploadCase(String isUploadCase) {
            this.isUploadCase = isUploadCase;
        }

        public String getPk() {
            return pk;
        }

        public void setPk(String pk) {
            this.pk = pk;
        }

        public String getSimpleName() {
            return simpleName;
        }

        public void setSimpleName(String simpleName) {
            this.simpleName = simpleName;
        }
    }
}
