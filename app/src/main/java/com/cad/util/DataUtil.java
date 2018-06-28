package com.cad.util;


import android.text.TextUtils;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        return array[3]&0xff;
    }

    /**
     * 获取直流电流
     *
     * @param array 一组数据
     * @return 返回直流电流的String
     */
    public static int directElectricFlow(byte[] array) {
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
        return Integer.toBinaryString((array[6] & 0xFF) + 0x100).substring(1);
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
        String hexString = castBytesToHexString(array);
        return hexString.substring(19, 23);
    }

    /**
     * 液体剩余升数
     *
     * @param array 一组数据
     * @return 液体升数
     */
    public static String getRiseNumbwe(byte[] array) {
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
}
