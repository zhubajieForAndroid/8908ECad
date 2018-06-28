package com.cad.conf;


/**
 * Created by dell on 2017/3/15.
 */

public class Constants {
    public static final String HOST = "https://www.haoanda.cn";
    public static final String DATA = "sockData";
    public static final int DATA_LONG = 47;        //数据长度
    public static final int SET_RESULT_LINGTH = 7;     //数据长度
    public static final int MAX_NUMBER = 20001;        //最大药液量
    public static final int MAX_WORK_COUNT = 65535;        //最大工作总次数
    public static final int MAX_ADD_NUMBER = 65535;        //最大添加的药液量(累加的)单位升
    public static final int MAX_ONE_DAY_DEPTH_WORK_COUNT = 65535;        //最大一天深度养护次数
    public static final int MAX_ONE_DAY_ROUTINE_WORK_COUNT = 65535;        //最大一天常规养护次数
    public static final int MAX_CHANGE_OIL_COUNT = 8388608;        //换油最大的升数
    public static final String ACTIVITY_STATE = "finish";
    public static final String USER_NAME = "equipment";
    public static final int LINK_SEVER_TIME_OUT = 1;            //连接服务器超时时间(分钟)
    public static final String UPLOAD_NAME = "8908eNew中性";            //更新项目名字
    public static final String DOWN_NAME = "8908eNew中性";            //下载APk名字

    public static final class URLS {
        //获取设备列表
        public static final String GET_EQUIPMENT_TYPE = HOST + "/weixin/manage/getEquipmentTypeList";
        //获取公司列表
        public static final String GET_COMAPNY_TYPE = HOST + "/weixin/manage/getCompanyList";
        //开启上线
        public static final String START_ONLINE = HOST + "/weixin/manage/createNewEquipment";
        //检查版本
        public static final String CHECKAPK_URL = HOST + "/Cadapi/appConfigGet.action";
        //获取设备已熟人
        public static final String GET_EQUIPMENT_USER = HOST + "/weixin/manage/getEquipmentDetail";
        //删除已有设备
        public static final String DELETE_EQUIPMENT = HOST + "/weixin/manage/cancelAllocation";
        //激活的ID根据id(公司标记)取激活二维码
        public static final String ID = "001";
        //请求设备排名数据
        public static final String EQUIPMENT_RANK_DATA = HOST + "/weixin/manage/getRankingOf8908E";
        //获取设备历史的加注量和常规深度的保养次数
        public static final String GET_EQUIPMENT_STORY = HOST + "/weixin/manage/getEquipmentDetail";
    }


}
