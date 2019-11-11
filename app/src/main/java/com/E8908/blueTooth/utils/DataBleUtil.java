package com.E8908.blueTooth.utils;

import android.text.TextUtils;
import android.util.Log;

import java.util.Arrays;

import static android.support.constraint.Constraints.TAG;

public class DataBleUtil {
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

      /* 获取蓝牙气体检测仪的状态位
     * @param array
     * @return
             */
    public static String getBleCheckGasState(byte[] array){
        return Integer.toBinaryString((array[12] & 0xFF) + 0x100).substring(1);
    }

    /**
     * 获取检测仪检测的TVOC的值
     * @param array
     * @return
     */
    public static String getTVOC(byte[] array){
        //24  [42, 24, 0, 1, 0, 1, 0, 0, 0, 3, -86, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -102, 35]
        //34  [42, 34, 0, 0, 0, 0, 0, 0, 0, 0, -86, 1, 0, 1, 15, 3, 4, 0, 26, 0, 33, 0, 37, 0, 33, 0, 5, 0, 0, 0, 0, 0, -112, 35]
        if (array.length < 34)
            return "0";
        //[42, 34, 0, 0, 0, 0, 0, 0, 0, 0, -86, 1,&& 0, 0, -14, 2, -87, 0, 57, 0, 70, 0, 79, 0, 35, 0, 85, 0, 0, 0, 0, 0, -68, 35]
        //2A 22 00 00 00 00 00 00 00 00 AA 01 00 00 F2 02 A9 00 39    00 46    00 4F    00 23   00 55   00 00 00 00 00 BC 23
        String hexString = castBytesToHexString(array);
        return hexString.substring(50, 54);
    }
    /**
     * 获取检测仪检测甲醛的值
     * @param array
     * @return
     */
    public static String getFormaldehyde(byte[] array){
        if (array.length < 34)
            return "0";
        String hexString = castBytesToHexString(array);
        return hexString.substring(46,50);//10  42 46
    }

    /**
     * 获取检测仪检测PM2.5的值
     * @param array
     * @return
     */
    public static String getPM(byte[] array){
        if (array.length < 34)
            return "0";
        String hexString = castBytesToHexString(array);
        return hexString.substring(38, 42);//1.0  34 38
    }

    /**
     * 获取检测仪检测的湿度的值
     * @param array
     * @return
     */
    public static String getHumidity(byte[] array){
        if (array.length < 34)
            return "0";
        String hexString = castBytesToHexString(array);
        return hexString.substring(30, 34);
    }
    /**
     * 获取检测仪检测的温度的值
     * @param array
     * @return
     */
    public static String getTemperatureByCheck(byte[] array){
        if (array.length < 34)
            return "0";
        String hexString = castBytesToHexString(array);
        return hexString.substring(26, 30);
    }
}
