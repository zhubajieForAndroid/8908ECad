package com.E8908.util;


import android.text.TextUtils;
import android.util.Log;

import com.E8908.base.MyApplication;
import com.E8908.conf.Constants;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;


public class DataUtil {
    private static final String TAG = "DataUtil";
    private static DataOutputStream os;
    //获取网络时间获取时间,
    public static void testDate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;//取得资源对象
                try {
                    url = new URL("http://www.baidu.com");
                    URLConnection uc = url.openConnection();//生成连接对象
                    uc.connect(); //发出连接
                    long time = uc.getDate(); //取得网站日期时间
                    SimpleDateFormat dff = new SimpleDateFormat("yyyyMMdd.HHmmss");
                    dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
                    String format = dff.format(time);
                    //修改系统时间
                    Process process = Runtime.getRuntime().exec("su");
                    String datetime = format;
                    os = new DataOutputStream(process.getOutputStream());
                    os.writeBytes("setprop persist.sys.timezone GMT\n");
                    os.writeBytes("/system/bin/date -s " + datetime + "\n");
                    os.writeBytes("clock -w\n");
                    os.writeBytes("exit\n");
                    os.flush();
                    os.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if (os != null){
                        try {
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

    }
    //字符串转换为ascii
    public static List<String> stringToA(String content){
        String result = "";
        int max = content.length();
        for (int i=0; i<max; i++){
            char c = content.charAt(i);
            int b = (int)c;
            result = result + b;
        }
        List<String> stringList = stringToList(result);
        return stringList;
    }
    //2个分组
    public static List<String> stringToList(String data){
        List<String> resultList = new ArrayList<>();
        int length = data.length();
        for (int i = 0; i < length; i+=2) {
            resultList.add(data.charAt(i)+""+data.charAt(i+1));
        }
        return resultList;
    }
    /**
     * 吧byte数组转成16进制形式的字符串
     *
     * @param byeData 一组数据
     * @return 返回2 2组合的16进制字符串
     */
    private static String castBytesToHexString(byte[] byeData) {
        String strRet = null;
        int intLen = byeData.length;
        for (int i = 0; i < intLen; i++) {
            byte byeTemp = byeData[i];
            String strHexTemp = Integer.toHexString(byeTemp);
            if (strHexTemp.length() > 2) {
                strHexTemp = strHexTemp.substring(strHexTemp.length() - 2);
            } else if (strHexTemp.length() < 2) {
                strHexTemp = "0" + strHexTemp;
            }
            if (i == 0) {
                strRet = strHexTemp;
            } else {
                strRet = strRet + strHexTemp;
            }
        }
        if (!TextUtils.isEmpty(strRet))
            strRet = strRet.toUpperCase();
        return strRet;
    }

    /**
     * 解析设置的结果
     *
     * @param startArray 返回的数据
     * @return true 设置成功,false设置失败
     */
    public static Boolean analysisSetResult(byte[] startArray) {
        if (startArray[3] == 0x4f && startArray[4] == 0x4b) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取交流电压
     *
     * @param array 一组数据
     * @return 返回直流电压的String
     */
    public static int directElectricPress(byte[] array) {
        if (array.length != Constants.DATA_LONG)
            return 0;
        return array[3]&0xff;
    }

    /**
     * 获取直流电流
     *
     * @param array 一组数据
     * @return 返回直流电流的String
     */
    public static int directElectricFlow(byte[] array) {
        if (array.length != Constants.DATA_LONG)
            return 0;
        return (array[4]&0xff);
    }

    /**
     * 获取交流电流
     *
     * @param array 一组数据
     * @return 返回交流电流的String
     */
    public static int directCommunionFlow(byte[] array) {
        return array[5]&0xff;
    }

    /**
     * 获取状态位
     *
     * @param array 一组数据
     * @return 返回前门锁, 激活状态等状态位 2进制字符串
     */
    public static String getState(byte[] array) {
        if (array.length != Constants.DATA_LONG)
            return "0";
        return Integer.toBinaryString((array[6] & 0xFF) + 0x100).substring(1);
    }
    /**
     * 获取连接服务器状态
     *
     * @param array true 已连接
     */
    public static boolean getLinkServiceState(byte[] array) {
        String state = getState(array);
        String substring = state.substring(3, 4);
        if ("1".equals(substring)){
            return true;
        }
        return false;
    }

    /**
     * 获取温度传感器状态
     *
     * @param array true 已经连接
     */
    public static boolean getTemperatureState(byte[] array) {
        String state = getState(array);
        String substring = state.substring(0, 1);
        if ("1".equals(substring)){
            return false;
        }
        return true;
    }
    /**
     * 获取激活状态
     * @param array true 激活
     */
    public static boolean getActionState(byte[] array) {
        String state = getState(array);
        String substring = state.substring(4, 5);
        if ("1".equals(substring)){
            return true;
        }
        return false;
    }
    /**
     * 获取激活状态
     * @param array true 加锁
     */
    public static boolean getLockState(byte[] array) {
        String state = getState(array);
        String substring = state.substring(5, 6);
        if ("1".equals(substring)){
            return true;
        }
        return false;
    }
    /**
     * 获取前门状态
     * @param array 一组数据
     */
    public static String getBeforeState(byte[] array) {
        if (array.length != Constants.DATA_LONG)
            return "0";
        String state = getState(array);
        return state.substring(2, 3);
    }
    /**
     * 获取前门状态
     * @param array 一组数据
     */
    public static String getAfterState(byte[] array) {
        if (array.length != Constants.DATA_LONG)
            return "0";
        String state = getState(array);
        return state.substring(1, 2);
    }

    /**
     * 获取电子秤AD值
     *
     * @param array 一组数据
     * @return 返回电子秤AD值
     */
    public static String getADNumbwe(byte[] array) {
        String hexString = castBytesToHexString(array);
        return hexString.substring(14, 18);
    }

    /**
     * 获取电子秤系数值
     *
     * @param array 一组数据
     * @return 电子秤系数值
     */
    public static String getRatioNumbwe(byte[] array) {
        if (array.length != Constants.DATA_LONG)
            return "0";
        String hexString = castBytesToHexString(array);
        return hexString.substring(18, 22);
    }

    /**
     * 液体剩余升数
     *
     * @param array 一组数据
     * @return 液体升数
     */
    public static String getRiseNumbwe(byte[] array) {
        if (array.length != Constants.DATA_LONG)
            return "0";
        String hexString = castBytesToHexString(array);
        return hexString.substring(22, 26);
    }
    /**
     * 液体加注总升数
     *
     * @param array 一组数据
     * @return 液体升数
     */
    public static String getRiseTotelNumbwe(byte[] array) {
        if (array.length != Constants.DATA_LONG)
            return "0";
        //2A 2B 03 DC 00 00 90 09 EE 49 F7 07 30 48 96 00 96 00 00 50 0B 05 04 0B 08 04 14 30 30 31 37 30 30 32 34 25 0A 12 05 05 00 1C 23
        String hexString = castBytesToHexString(array);
        return hexString.substring(8,10)+hexString.substring(26, 30);
    }
    /**
     * 获取深度模式工作次数
     *
     * @param array 一组数据
     * @return 工作次数
     */
    public static String getDepthWorkNumbwe(byte[] array) {
        if (array.length != Constants.DATA_LONG)
            return "0";
        //2A 2B 03 DE 65 00 90 09 F0 49 F7 07 34 C2 3A 30 3A 00 00 50 0B 05 04 0B 08 04 13 30 30 31 37 30 30 31 34 25 0A 12 05 05 00 DF 23
        //2A 2B 03 DC 65 00 98 09 ED 49 F7 07 34 C2 3A 30 3A 00 00 50 0B 05 04 0B 08 04 13 30 30 31 37 30 30 31 34 25 0A 12 05 05 00 C8 23
        String hexString = castBytesToHexString(array);
        return hexString.substring(30, 34);
    }
    /**
     * 获取常规模式工作次数
     *
     * @param array 一组数据
     * @return 工作次数
     */
    public static String getRoutineWorkNumbwe(byte[] array) {
        if (array.length != Constants.DATA_LONG)
            return "0";
        String hexString = castBytesToHexString(array);
        return hexString.substring(34, 38);
    }
    /**
     * 获取加注量
     *
     * @param array 一组数据
     * @return 加注量
     */
    public static int getAddNumbwe(byte[] array) {
        return array[19];
    }

    /**
     * 常规模式第一阶段臭氧和雾化共同运行时间
     *
     * @param array 一组数据
     * @return 第一阶段运行的时间
     */
    public static int getRoutineOzoneRunTime(byte[] array) {
        return array[20];
    }

    /**
     * 常规模式第二阶段雾化运行时间
     *
     * @param array 一组数据
     * @return 第二阶段运行的时间
     */
    public static int getRoutineTwoRunTime(byte[] array) {
        return array[21];
    }

    /**
     * 常规模式第三阶段净化运行时间
     *
     * @param array 一组数据
     * @return 第三阶段运行的时间
     */
    public static int getRoutineThreeRunTime(byte[] array) {
        return array[22];
    }

    /**
     * 深度模式第一阶段臭氧和雾化共同运行时间
     *
     * @param array 一组数据
     * @return 第一阶段运行的时间
     */
    public static int getDepthOneRunTime(byte[] array) {
        return array[23];
    }

    /**
     * 深度模式第二阶段雾化运行时间
     *
     * @param array 一组数据
     * @return 第二阶段运行的时间
     */
    public static int getDepthTwoRunTime(byte[] array) {
        return array[24];
    }

    /**
     * 深度模式第三阶段净化运行时间
     *
     * @param array 一组数据
     * @return 第三阶段运行的时间
     */
    public static int getDepthThreeRunTime(byte[] array) {
        return array[25];
    }

    /**
     * 获取信号强度
     *
     * @param array 一组数据
     * @return 信号强度
     */
    public static int getSignalStrength(byte[] array) {
        return (array[26] / 8) + 1;
    }

    /**
     * 获取序列号
     *
     * @param array 一组数据
     * @return 设备的序列号
     */
    public static String getEquipmentNumber(byte[] array) {
        return (char) array[27] + "" + (char) array[28] + "" + (char) array[29] + "" + (char) array[30] + "" + (char) array[31] + "" + (char) array[32] +""+ (char) array[33] +""+ (char) array[34];
    }

    /**
     * 获取硬件版本
     *
     * @param array 一组数据
     * @return 硬件版本
     */
    public static int getVwesionToHardware(byte[] array) {
        return array[35];
    }

    /**
     * 获取软件版本
     *
     * @param array 一组数据
     * @return 软件版本
     */
    public static int getVwesionToSoftware(byte[] array) {
        return array[36];
    }

    /**
     * 获取出厂日期年
     *
     * @param array 一组数据
     * @return 年
     */
    public static int getDateYear(byte[] array) {
        return array[37];
    }

    /**
     * 获取出厂日期月
     *
     * @param array 一组数据
     * @return 月
     */
    public static int getDateMonth(byte[] array) {
        return array[38];
    }

    /**
     * 获取出厂日期日
     *
     * @param array 一组数据
     * @return 日
     */
    public static int getDateDay(byte[] array) {
        return array[39];
    }
    /**
     * 获取温度
     *
     * @param array 一组数据
     * @return 日
     */
    public static int getTemperature(byte[] array) {
        if (array.length != Constants.DATA_LONG)
            return 0;
        return array[40];
    }
    /**
     * 获取提醒更换药液状态
     *
     * @param array 一组数据
     * @return 日
     */
    public static int getChangeOilState(byte[] array) {
        return array[41];
    }
    /**
     * 获取允许工作总次数
     * @param array
     * @return
     */
    public static String getWorkCount(byte[] array){
        if (array.length != Constants.DATA_LONG)
            return "0";
        String hexString = castBytesToHexString(array);
        return hexString.substring(86, 90);
    }

    /**
     * 获取设备检测车辆的次数
     * @param array
     * @return
     */
    public static String getCarCheckCount(byte[] array){
        String hexString = castBytesToHexString(array);
        return hexString.substring(90, 94);
    }

    /**
     * 获取气体检测仪的状态位
     * @param array
     * @return
     */
    public static String getCheckGasState(byte[] array){
        return Integer.toBinaryString((array[47] & 0xFF) + 0x100).substring(1);
    }

    /**
     * 获取常规和自定义次数控制状态
     * @param array
     */
    public static String getCountControl(byte[] array){
        return Integer.toBinaryString((array[62] & 0xFF) + 0x100).substring(1);
    }
    /**
     * 获取检测仪检测的TVOC的值
     * @param array
     * @return
     */
    public static String getTVOC(byte[] array){
        //2A4003E000009802E049F70000000000000000500B05040B08041B3030313730303634250D120A120001040000000080014303760016001B001E019000051823
        String hexString = castBytesToHexString(array);
        return hexString.substring(120, 124);
    }
    /**
     * 获取检测仪检测甲醛的值
     * @param array
     * @return
     */
    public static String getFormaldehyde(byte[] array){
        String hexString = castBytesToHexString(array);
        return hexString.substring(116, 120);
    }

    /**
     * 获取检测仪检测PM2.5的值
     * @param array
     * @return
     */
    public static String getPM(byte[] array){
        String hexString = castBytesToHexString(array);
        return hexString.substring(108, 112);
    }

    /**
     * 获取检测仪检测的湿度的值
     * @param array
     * @return
     */
    public static String getHumidity(byte[] array){
        String hexString = castBytesToHexString(array);
        return hexString.substring(100, 104);
    }
    /**
     * 获取检测仪检测的温度的值
     * @param array
     * @return
     */
    public static String getTemperatureByCheck(byte[] array){
        String hexString = castBytesToHexString(array);
        return hexString.substring(96, 100);
    }

    /**
     * 获取DTU服务器的IP地址
     */
    public static String getServiceIp(byte[] array){
        //116.62.209.62
        //[42, 25, 33, 49, 49, 54, ., 54, 50, ., 50, 48, 57, ., 54, 50, 32, 32, 32, 32, 32, 32, 32, 17, 35]
        String data = ""+(char)array[3]+(char)array[4]+(char)array[5]+(char)array[6]+(char)array[7]+(char)array[8]+(char)array[9]
                +(char)array[10]+(char)array[11]+(char)array[12]+(char)array[13]+(char)array[14]+(char)array[15]+(char)array[16]+
                (char)array[17]+(char)array[18]+(char)array[19]+(char)array[20]+(char)array[21]+(char)array[22];
       return data.trim();
    }

    /**
     * 登录
     * @param id
     * @return
     */
    public static byte[] getLoginData(String id){
        if (TextUtils.isEmpty(id) || id.length() != 8)
            return null;
        int idOne = Integer.valueOf(id.substring(0, 2),16);
        int idTwo = Integer.valueOf(id.substring(2, 4),16);
        int idThree = Integer.valueOf(id.substring(4, 6),16);
        int idFore = Integer.valueOf(id.substring(6, 8),16);

        //[42, 17, 0, 1, 1, 0, 7, 0, 23, 1, 18, 0, 0, 0, 0, 0, 0, 58, 35]
        byte[] login = {0x2a, 0x11, 0x00, 0x01, 0x01, 0x00, 0x07, (byte) idOne, (byte) idTwo, (byte) idThree, (byte) idFore, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x3A, 0x23};
        return login;
    }

    /**
     * 登出
     * @param id
     * @return
     */
    public static byte[] getLogOutData(String id){
        if (TextUtils.isEmpty(id) || id.length() != 8)
            return null;
        int idOne = Integer.valueOf(id.substring(0, 2),16);
        int idTwo = Integer.valueOf(id.substring(2, 4),16);
        int idThree = Integer.valueOf(id.substring(4, 6),16);
        int idFore = Integer.valueOf(id.substring(6, 8),16);

        byte[] logout = {0x2a, 0x11, 0x00, 0x01, 0x01, 0x00, 0x09, (byte) idOne, (byte) idTwo, (byte) idThree, (byte) idFore, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x34, 0x23};
        return logout;
    }
    /**
     * 链路
     * @param id
     * @return
     */
    public static byte[] getLinkData(String id){
        if (TextUtils.isEmpty(id) || id.length() != 8)
            return null;
        int idOne = Integer.valueOf(id.substring(0, 2),16);
        int idTwo = Integer.valueOf(id.substring(2, 4),16);
        int idThree = Integer.valueOf(id.substring(4, 6),16);
        int idFore = Integer.valueOf(id.substring(6, 8),16);
        byte[] link = {0x2a, 0x11, 0x00, 0x01, 0x01, 0x00, 0x08, (byte) idOne, (byte) idTwo, (byte) idThree, (byte) idFore, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x35, 0x23};
        return link;
    }
    /**
     * 上传版本号
     */
    public static byte[] upDataVersionCode(String id,int version){
        //0x2A,0x1E,0x00,0x01,0x04,0x00,0x01,0x00,0x17,0x01,0x14,0x01,0x86,0x00,0x00,
        // 0x49,0xF7,0x41,0x43,0x43,0x38,0x39,0x30,0x38,0x45,0x2D,0x56,0x36,0x2E,0x31,0x54,0x23

        if (TextUtils.isEmpty(id) || id.length() != 8)
            return null;
        int idOne = Integer.valueOf(id.substring(0, 2),16);
        int idTwo = Integer.valueOf(id.substring(2, 4),16);
        int idThree = Integer.valueOf(id.substring(4, 6),16);
        int idFore = Integer.valueOf(id.substring(6, 8),16);

        int shi = Integer.valueOf(((version%100/10)+30)+"",16);
        int ge = Integer.valueOf(((version%10)+30)+"",16);

        byte[] arr = {0x2a,0x1E, 0x00, 0x01, 0x04, 0x00, 0x01, (byte) idOne, (byte) idTwo, (byte) idThree, (byte) idFore,0x01, (byte) 0x86,0x00,0x00,
                0x49, (byte) 0xF7,0x41,0x43,0x43,0x38,0x39,0x30,0x38,0x45,0x2D,0x56, (byte) shi,0x2E, (byte) ge,0x54,0x23};

        return arr;
    }

    /**
     * 上传版本信息
     * @param state
     */
    public static byte[] up4SAndData(String id,int state){
        if (TextUtils.isEmpty(id) || id.length() != 8)
            return null;
        int idOne = Integer.valueOf(id.substring(0, 2),16);
        int idTwo = Integer.valueOf(id.substring(2, 4),16);
        int idThree = Integer.valueOf(id.substring(4, 6),16);
        int idFore = Integer.valueOf(id.substring(6, 8),16);

        int result;
        if (state == 0){
            result = 31;
        }else {
            result = 30;
        }

        int shi = Integer.valueOf(result+"",16);

        //0x2A,0x13,0x00,0x01,0x04,0x00,0x35,0x00,0x17,0x01,0x14,0x01,0x86,0x00,0x00,0x49,0xF7,0x30,0x30,0x32,0x23,
        //0x2A,0x13,0x00,0x01,0x04,0x00,0x35,0x00,0x17,0x01,0x14,0x01,0x86,0x00,0x00,0x49,0xF7,0x30,0x31,0x33,0x23,     4S
        byte[] arr = {0x2a, 0x13, 0x00, 0x01, 0x04, 0x00, 0x35, (byte) idOne, (byte) idTwo, (byte) idThree, (byte) idFore, 0x01,(byte) 0x86,0x00,0x00,0x49, (byte) 0xf7,0x30, (byte) shi,0x00,0x23};
        return  arr;
    }

    /**
     * 获取上报常规和自定运行时间的数据
     * @param zOne
     * @param zTwo
     * @param zThree
     * @param cOne
     * @param cTwo
     * @param cThree
     * @return
     */
    public static byte[] getUpTimeData(String id,int zOne,int zTwo,int zThree,int cOne,int cTwo,int cThree){
        //0x2A,0x1C,0x00,0x01,0x04,0x00,0x06,
        // 0x00,0x17,0x01,0x14,                     id
        // 0x01,0x86,0x00,0x00,0x49,0xF7,
        // 0x0B,0x05,0x04,                          常规时间
        // 0x0B,0x08,0x04,                          自定义时间
        // 0x30,0x30,0x30,0x30,0x30,
        // 0x33,0x23
        //2 2分组
        if (TextUtils.isEmpty(id) || id.length() != 8)
            return null;
        int idOne = Integer.valueOf(id.substring(0, 2),16);
        int idTwo = Integer.valueOf(id.substring(2, 4),16);
        int idThree = Integer.valueOf(id.substring(4, 6),16);
        int idFore = Integer.valueOf(id.substring(6, 8),16);
        if (zTwo == 0){
            zTwo = 16;
        }
        if (zOne == 0){
            zOne = 16;
        }
        if (zThree == 0){
            zThree = 16;
        }
        if (cOne == 0){
            cOne = 16;
        }
        if (cTwo == 0){
            cTwo = 16;
        }
        if (cThree == 0){
            cThree = 16;
        }
        byte[] arr = {0x2a, 0x1c, 0x00, 0x01, 0x04, 0x00, 0x06, (byte) idOne, (byte) idTwo, (byte) idThree, (byte) idFore, 0x01,(byte) 0x86,0x00,0x00,0x49, (byte) 0xf7,
                (byte) cOne, (byte) cTwo, (byte) cThree, (byte) zOne, (byte) zTwo, (byte) zThree,0x30,0x30,0x30,0x30,0x30,0x33,0x23};
        return  arr;
    }
    /**
     * 主动上报
     * @param id                设备ID
     * @param workCount          允许工作次数
     * @param ratioNumbwe       电子秤系数
     * @param rssi              信号强度
     * @param temperature       温度
     * @param latitude      维度
     * @param longitude      经度
     * @param pressInt          电压
     * @param flowInt           电流
     * @param routineWorkNumbweInt        常规工作次数
     * @param depthWorkNumbweInt        深度工作次数
     * @param riseTotelNumbweInt            加注总升数
     * @param riseNumbweInt  液体剩余升数
     * @param stateOne
     * @param stateTwo
     * @param stateThree
     * @return
     */
    public static byte[] getUpDateData(String id,int workCount,int ratioNumbwe,int rssi,int temperature,String latitude,String longitude
            ,int pressInt,int flowInt,int routineWorkNumbweInt,int depthWorkNumbweInt,int riseTotelNumbweInt,int riseNumbweInt,
                                       int stateOne,int stateTwo,int stateThree){
        /*2A 2E 00 01 01 00 01 21 00 64 83 01 38 21 00 64 83
        09 09 07 06 1E 35 00 04 D7 9D 38 18 6F 4F 86 00 00 00 19 5D 0F 42 5E 40 00 02 00 00 00 CB 23*/
        if (TextUtils.isEmpty(id) || latitude.length() != 8 || longitude.length() != 8 || id.length() != 8)
            return null;
        int idOne = Integer.valueOf(id.substring(0, 2),16);
        int idTwo = Integer.valueOf(id.substring(2, 4),16);
        int idThree = Integer.valueOf(id.substring(4, 6),16);
        int idFore = Integer.valueOf(id.substring(6, 8),16);

        byte clearNumGao = (byte) ((workCount & 0x0000FF00) >> 8);
        byte clearNumDi = (byte) (workCount & 0x000000FF);

        byte ratioNumbweGao = (byte) ((ratioNumbwe & 0x0000FF00) >> 8);
        byte ratioNumbweDi = (byte) (ratioNumbwe & 0x000000FF);

        //String hexSign = Integer.toHexString(rssi);
        //String temperatureStr = Integer.toHexString(temperature);


        byte oldOilWeightGao = (byte) ((depthWorkNumbweInt & 0x0000FF00) >> 8);
        byte oldOilWeightDi = (byte) (depthWorkNumbweInt & 0x000000FF);

        byte routineWorkNumbweIntG = (byte) ((routineWorkNumbweInt & 0x0000FF00) >> 8);
        byte routineWorkNumbweIntD = (byte) (routineWorkNumbweInt & 0x000000FF);

        byte changeOilNumberGao = (byte) ((riseTotelNumbweInt & 0x00FF0000) >> 16);
        byte changeOilNumberZhong = (byte) ((riseTotelNumbweInt & 0x0000FF00) >> 8);
        byte changeOilNumberDi = (byte) (riseTotelNumbweInt & 0x000000FF);

        byte riseNumbweIntG = (byte) ((riseNumbweInt & 0x0000FF00) >> 8);
        byte riseNumbweIntD = (byte) (riseNumbweInt & 0x000000FF);

        byte[] link = {0x2a, 0x2E, 0x00, 0x01, 0x01, 0x00, 0x01, (byte) idOne, (byte) idTwo, (byte) idThree, (byte) idFore, 0x00, 0x00, clearNumGao, clearNumDi,
                ratioNumbweGao, ratioNumbweDi,19,07,15,15,50, (byte) rssi, (byte) temperature,
                (byte) Integer.parseInt(latitude.substring(0,2),16),
                (byte) Integer.parseInt(latitude.substring(2,4),16),
                (byte) Integer.parseInt(latitude.substring(4,6),16),
                (byte) Integer.parseInt(latitude.substring(6,latitude.length()),16),
                (byte) Integer.parseInt(longitude.substring(0,2),16),
                (byte) Integer.parseInt(longitude.substring(2,4),16),
                (byte) Integer.parseInt(longitude.substring(4,6),16),
                (byte) Integer.parseInt(longitude.substring(6,longitude.length()),16),
                (byte) pressInt, (byte) flowInt,oldOilWeightGao,oldOilWeightDi,routineWorkNumbweIntG,
                routineWorkNumbweIntD,
                changeOilNumberZhong,changeOilNumberDi, changeOilNumberGao,
                riseNumbweIntG,riseNumbweIntD,
                (byte) stateOne, (byte) stateTwo, (byte) stateThree,
                0x35, 0x23};
        return link;
    }

    /**
     * 获取上传加注量的数组
     * @param addNumber
     * @return
     */
    public static byte[] getAddNumberData(String id,int addNumber){
        if (TextUtils.isEmpty(id) || id.length() != 8)
            return null;
        int idOne = Integer.valueOf(id.substring(0, 2),16);
        int idTwo = Integer.valueOf(id.substring(2, 4),16);
        int idThree = Integer.valueOf(id.substring(4, 6),16);
        int idFore = Integer.valueOf(id.substring(6, 8),16);
        //0x2A,0x13,0x00,0x01,0x04,0x00,0x33,0x00,0x67,0x00,0x49,0x01,0x86,0x00,0x00,0x49,0xF7,0x35,0x30,0x1D,0x23,
        byte[] arr = {0x2a, 0x13, 0x00, 0x01, 0x04, 0x00, 0x33, (byte) idOne, (byte) idTwo, (byte) idThree, (byte) idFore, 0x01,(byte) 0x86,0x00,0x00,0x49, (byte) 0xf7,0x35, (byte) addNumber,0x00,0x23};
        return  arr;
    }
}
