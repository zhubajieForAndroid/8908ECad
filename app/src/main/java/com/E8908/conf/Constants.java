package com.E8908.conf;


/**
 * Created by dell on 2017/3/15.
 */

public class Constants {
    public static final String HOST = "https://www.haoanda.cn";
    public static final String LOCAL = "https://www.haoanda.cn";//"http://192.168.2.119:8080";
    public static final String YUN_HOST = "https://cykj.wlyinfo.com/base-api";//https://cykj.wlyinfo.com/base-api
    public static final String DATA = "sockData";
    public static final String BLE_DATA = "ble_data";           //蓝牙数据
    public static final String PUSH_URL = "pushUrl";
    public static final String PUSH_DATA = "data";
    public static final int DATA_LONG = 65;        //数据长度
    public static final int SET_RESULT_LINGTH = 7;     //数据长度
    public static final int MAX_NUMBER = 20501;        //最大药液量
    public static final int MAX_WORK_COUNT = 65535;        //最大工作总次数
    public static final int MAX_ADD_NUMBER = 65535;        //最大添加的药液量(累加的)单位升
    public static final int MAX_ONE_DAY_DEPTH_WORK_COUNT = 65535;        //最大一天深度养护次数
    public static final int MAX_ONE_DAY_ROUTINE_WORK_COUNT = 65535;        //最大一天常规养护次数
    public static final int MAX_CHANGE_OIL_COUNT = 8388608;        //换油最大的升数
    public static final String ACTIVITY_STATE = "finish";
    public static final String BLE_ACTIVITY_STATE = "finish_ble";
    public static final String USER_NAME = "equipment";
    public static final String CREATE_USER_NAME = "administrator1";     //创建报告的创建人
    public static final int LINK_SEVER_TIME_OUT = 15;            //连接服务器超时时间(15秒)
    //---------------------------------------------更新版本修改开始---------------------------------------------------
    public static final String UPLOAD_NAME = "new8908EBLE";            //更新项目名字
    public static final String DOWN_NAME = "new8908EBLE";            //下载APk名字
    //---------------------------------------------更新版本修改结束---------------------------------------------------
    public static final int PAGECOUNTS = 20;            //气体检测历史数据的一页的条目数
    public static final String PAGE_SIZE = "20";            //列表的每页条数
    public static final String _ID = "100";
    public static final String YUN_ID = "1000";
    public static final String DAO_NAME = "GAS_DAO";            //气体数据库名字
    public static final String YUN_NAME = "YUN_DAO";            //云店数据库名字
    public static final String TB_CAR_CHECK_REPORT = "TB_GAS";            //气体数据库表名字
    public static final String TB_CAR_YUN_REPORT = "TB_YUN";            //云店数据库表名字


    public static final class URLS {
        //上传测试报告
        public static final String UP_TEXT_TAB = HOST + "/weixin/equipmentSelfCheckService/addEquipmentSelfCheck";
        //查询测试报告
        public static final String QUERY_TEXT_TAB = HOST + "/weixin/equipmentSelfCheckService/queryEquipmentSelfCheck";
        //获取设备列表
        public static final String GET_EQUIPMENT_TYPE = HOST + "/weixin/manage/getEquipmentTypeList";
        //获取公司列表
        public static final String GET_COMAPNY_TYPE = HOST + "/weixin/manage/getCompanyList";
        //开启上线
        public static final String START_ONLINE = HOST + "/weixin/manage/createNewEquipment";
        //检查版本
        public static final String CHECKAPK_URL = HOST + "/Cadapi/appConfigGet.action";
        //获取设备已属人
        public static final String GET_EQUIPMENT_USER = HOST + "/weixin/manage/getEquipmentDetail";
        //删除已有设备
        public static final String DELETE_EQUIPMENT = HOST + "/weixin/manage/cancelAllocation";
        //激活的ID根据id(公司标记)取激活二维码
        public static final String ID = "014";
        //请求设备排名数据
        public static final String EQUIPMENT_RANK_DATA = HOST + "/weixin/manage/getRankingOf8908E";
        //获取设备历史的加注量和常规深度的保养次数
        public static final String GET_EQUIPMENT_STORY = HOST + "/weixin/manage/getEquipmentDetail";
        //判断预约码是否正确
        public static final String CHECK_VINCODE =  LOCAL+"/weixin/checkCodeRecordService/verifyCheckNumberByStaff";

        //请求所有的检测数据
        public static final String GET_CHECK_INFO = LOCAL+"/weixin/carReportService/getTopOneAllCarCheckReport";
        //设备下所有车牌最近一条汽体检测数据
        public static final String GET_ALL_CHICK_INFO = LOCAL+"/weixin/checkDataService/getTopAllCheckDataByDeviceNo";

        //请求根据车牌号所搜的数据
        public static final String GET_CHECK_SEARCH_INFO = LOCAL+"/weixin/carReportService/queryCarCheckReportByCarNumber";
        //请求根据车牌和设备ID请求有害气体施工前后的数据,曲线图
        public static final String GET_CHECK_SEARCH_BEFORE_AND_AFTER_INFo = LOCAL+"/weixin/checkDataService/getGasCheckDataByCarNumber";
        //创建报告
        public static final String CREATE_TAB =LOCAL+"/weixin/carReportService/createCarCheckReport";
        //根据车牌号获取气体检测数据最近一条的施工前数据和施工后数据
        public static final String GET_BEFORE_AND_AFTER_BY_CARNUMBER = LOCAL+"/weixin/checkDataService/getHistory365CarCheckDataByCarNumber";
        //图片上传接口
        public static final String UPLOAD_IMAGE = HOST+"/wxapp/app/upload/img";
        //推送
        public static final String PUSH_TAB = LOCAL+"/weixin/carReportService/updateCarCheckReportPushmessageById";
        //接口设置就绪和未就绪状态
        public static final String SET_READ = LOCAL+"/weixin/equipmentApiService/apiCheckEquipmentStatus";
        //循环调用接口查询是否启动设备
        public static final String QUERY_START_STATE = LOCAL+"/weixin/equipmentApiService/queryCheckEquipmentStatus";
        //循环调用接口上传蓝牙检测到的数据
        public static final String UPDATE_BLE_GAS_DATA = LOCAL+"/weixin/cadGasCheckDataService/saveCadGasCheckData";
        //根据车牌号查询云店信息
        public static final String GET_INFO_BY_EQUIPMENT = YUN_HOST+"/appReceiveCar/findTodayReceiveCarByEquipmentId";
    }
}
