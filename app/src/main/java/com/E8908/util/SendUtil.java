package com.E8908.util;

import android.content.SharedPreferences;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.E8908.base.MyApplication;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

/**
 * Created by dell on 2018/3/6.
 */

public class SendUtil {
    //发送数据
    public static void sendMessage(int[] arr, OutputStream os) {
        synchronized (SendUtil.class) {
            if (os == null) {
                return;
            }
            try {
                for (int i = 0; i < arr.length; i++) {
                    os.write(arr[i]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            SystemClock.sleep(100);
        }
    }

    /**
     * 控制声音
     */
    public static void controlVoice() {
        int[] arr = {0x2a, 0x07, 0x04, 0x01, 0x14, 0x3c, 0x23};
        SendUtil.sendMessage(arr, MyApplication.getOutputStream());
    }

    /**
     * 控制声音5升
     */
    public static void controlVoiceFive() {
        int[] arr = {0x2a, 0x07, 0x04, 0x05, 0x14, 0x38, 0x23};
        sendMessage(arr, MyApplication.getOutputStream());
    }

    /**
     * 开前盖
     */
    public static void openQian() {
        int[] arr = {0x2a, 0x06, 0x0c, 0x55, 0x75, 0x23};
        SendUtil.sendMessage(arr, MyApplication.getOutputStream());
    }

    /**
     * 开后盖
     */
    public static void openHou() {
        int[] arr = {0x2a, 0x06, 0x0c, 0xaa, 0x8a, 0x23};
        SendUtil.sendMessage(arr, MyApplication.getOutputStream());
    }

    /**
     * 打开4#触点
     */
    public static void open4() {
        int[] start = {0x2a, 0x06, 0x01, 0x08, 0x25, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 打开4#6#触点
     */
    public static void open4And6() {
        int[] start = {0x2a, 0x06, 0x01, 0x28, 0x05, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 打开2#触点
     */
    public static void open2() {
        int[] start = {0x2a, 0x06, 0x01, 0x02, 0x2F, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 打开6#触点
     */
    public static void open6() {
        int[] start = {0x2a, 0x06, 0x01, 0x20, 0x0d, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 关闭其他所有的触点打开6#触点
     */
    public static void closeOthre() {
        int[] start = {0x2a, 0x06, 0x01, 0x20, 0x0d, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 打开7#8#触点
     */
    public static void open7And8() {
        int[] start = {0x2a, 0x06, 0x01, 0xc0, 0xED, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 关闭7#触点 8#是开着的
     */
    public static void close7() {
        int[] start = {0x2a, 0x06, 0x01, 0x80, 0xAD, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 臭氧
     */
    public static void open7() {
        //2a 06 01 80 ad 23
        int[] start = {0x2a, 0x06, 0x01, 0x40, 0x6D, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 打开#触点    雾化
     */
    public static void open8() {
        //2a 06 01 80 ad 23
        int[] start = {0x2a, 0x06, 0x01, 0x80, 0xAD, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 关闭所有触点
     */
    public static void closeAll() {
        int[] start = {0x2a, 0x06, 0x01, 0x00, 0x2D, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 设置工作状态
     *
     * @param state 工作的状态16进制
     */
    public static void setWorkState(int state) {
        int result = 0x2a ^ 0x06 ^ 0x08 ^ state;
        int[] start = {0x2a, 0x06, 0x08, state, result, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
        SharedPreferences workValueSp = SharedPreferencesUtils.getWorkValueSp();
        SharedPreferences.Editor edit = workValueSp.edit();
        edit.putInt("workStateValue", state);
        edit.apply();
    }


    /**
     * 设置id
     *
     * @param ids
     */
    public static void setEquipentId(List<String> ids) {
        if (ids.size() != 8)
            return;
        int result = 0x2a ^ 0x0d ^ 0x06 ^ Integer.parseInt(ids.get(0)) ^ Integer.parseInt(ids.get(1)) ^ Integer.parseInt(ids.get(2)) ^
                Integer.parseInt(ids.get(3)) ^ Integer.parseInt(ids.get(4)) ^ Integer.parseInt(ids.get(5)) ^ Integer.parseInt(ids.get(6)) ^ Integer.parseInt(ids.get(7));
        int[] start = {0x2a, 0x0d, 0x06, Integer.parseInt(ids.get(0)), Integer.parseInt(ids.get(1)), Integer.parseInt(ids.get(2)),
                Integer.parseInt(ids.get(3)), Integer.parseInt(ids.get(4)), Integer.parseInt(ids.get(5)), Integer.parseInt(ids.get(6)),
                Integer.parseInt(ids.get(7)), result, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 设置常规保养的次数
     */
    public static void setRoutineNumber(int number) {
        int gao = (number & 0x0000ff00) >> 8;
        int di = number & 0x000000ff;
        int result = 0x2a ^ 0x08 ^ 0x02 ^ 0xaa ^ gao ^ di;
        int[] start = {0x2a, 0x08, 0x02, 0xaa, gao, di, result, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 设置深度保养的次数
     */
    public static void setDepthNumber(int number) {
        int gao = ((number & 0x0000ff00) >> 8);
        int di = (number & 0x000000ff);
        int result = 0x2a ^ 0x08 ^ 0x02 ^ 0x55 ^ gao ^ di;
        int[] start = {0x2a, 0x08, 0x02, 0x55, gao, di, result, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }
    /*===========================新增start================================*/

    /**
     * 设置设备就绪状态 0未就绪 1已就绪
     *
     * @param state
     */
    public static void setReadyState(String equipment, int state) {
        int result = 0x2a ^ 0x06 ^ 0x13 ^ state;
        int[] start = {0x2a, 0x06, 0x13, state, result, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
        //调用接口设置设备是否就绪
        if (!TextUtils.isEmpty(equipment))
            SetReadStateUtil.setRead(equipment, "" + state);
        //保存设置的状态
        SharedPreferences readyStateValueSp = SharedPreferencesUtils.getReadyStateValueSp();
        SharedPreferences.Editor edit = readyStateValueSp.edit();
        edit.putInt("readyAndUnReadyState", state);
        edit.apply();
    }

    /**
     * 设置车台检测次数
     */
    public static void setCheckCarNumber(int number) {
        int gao = ((number & 0x0000ff00) >> 8);
        int di = (number & 0x000000ff);
        int result = 0x2a ^ 0x08 ^ 0x02 ^ 0x11 ^ gao ^ di;
        int[] start = {0x2a, 0x08, 0x02, 0x11, gao, di, result, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 设置是施工前还是施工后 0施工前,1施工后
     *
     * @param number
     */
    public static void setConstructionAfterAndBefore(int number) {
        int result = 0x2a ^ 0x06 ^ 0x11 ^ number;
        int[] start = {0x2a, 0x06, 0x11, number, result, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 设置与检测仪联立连接
     */
    public static void setConnectForInstrument() {
        int result = 0x2a ^ 0x05 ^ 0x0f;
        int[] start = {0x2a, 0x05, 0x0f, result, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 设置车牌号
     */
    public static void setCarNumber(String carNumber) {
        if (carNumber.length() < 7)
            return;
        String gbk = toGBK(carNumber);
        switch (carNumber.length()) {
            case 7:
                gbk = gbk + "202020";
                break;
            case 8:
                gbk = gbk + "2020";
                break;
            case 9:
                gbk = gbk + "20";
                break;
        }
        List<String> strings = DataUtil.stringToList(gbk);
        int[] arr = new int[strings.size()];
        for (int i = 0; i < strings.size(); i++) {
            arr[i] = Integer.parseInt(strings.get(i), 16);
        }
        int result = 0x2a ^ 0x0f ^ 0x10 ^ arr[0] ^ arr[1] ^ arr[2] ^ arr[3] ^ arr[4] ^ arr[5] ^ arr[6] ^ arr[7] ^ arr[8] ^ arr[9];
        int[] start = {0x2a, 0x0f, 0x10, arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], arr[6], arr[7], arr[8], arr[9], result, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    private static String toGBK(String source) {
        StringBuilder sb = new StringBuilder();
        try {
            byte[] bytes = source.getBytes("GBK");
            for (byte b : bytes) {
                sb.append(Integer.toHexString((b & 0xff)).toUpperCase());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    /*===========================新增stop================================*/

    /**
     * 设置加注总量单位毫升
     *
     * @param num
     */
    public static void setChangeOilNumber(int num) {
        int gao = ((num & 0x00ff0000) >> 16);
        int zhong = ((num & 0x0000ff00) >> 8);
        int di = (num & 0x000000ff);
        int result = 0x2a ^ 0x08 ^ 0x09 ^ gao ^ zhong ^ di;
        int[] start = {0x2a, 0x08, 0x09, gao, zhong, di, result, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 电子秤清零
     *
     * @param num
     */
    public static void clear(int num) {
        int iGao = ((num & 0x0000FF00) >> 8);
        int iDi = (num & 0x000000FF);
        int xorResult = 0x2a ^ 0x07 ^ 0x0a ^ iGao ^ iDi;
        int[] clear = {0x2a, 0x07, 0x0a, iGao, iDi, xorResult, 0x23};
        SendUtil.sendMessage(clear, MyApplication.getOutputStream());
    }

    /**
     * 恢复出厂设置
     */
    public static void renewData() {
        int[] start = {0x2a, 0x05, 0x0b, 0x24, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 检测DTU模块是否正常
     */
    public static void checkDtuIsScurress() {
        int[] start = {0x2a, 0x05, 0x20, 0x0f, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 查询DTU模块连接服务器的IP地址
     */
    public static void queryServiceIp() {
        int[] start = {0x2a, 0x19, 0x21, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x12, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 设置DTU模块的IP地址
     */
    public static void setDtuIp(String ip) {
        int count = 0;
        byte[] bytes = ip.getBytes();
        int[] start = new int[25];
        //固定前三位
        start[0] = 0x2a;
        start[1] = 0x19;
        start[2] = 0x22;

        int xor = 0;
        for (int i = 0; i < bytes.length; i++) {
            xor ^= bytes[i];
            int result = i + 3;                 //第三位开始赋值
            if (result < 25) {
                start[result] = bytes[i];
                count++;                        //记录赋值了多少位,剩余位补20
            }
        }

        for (int i = count + 3; i < start.length - 2; i++) {
            start[i] = 0x20;
            xor ^= 0x20;
        }
        xor ^= 0x2a ^ 0x19 ^ 0x22;
        //固定后2位
        start[23] = xor;
        start[24] = 0x23;
        Log.d(TAG, "setDtuIp: " + Arrays.toString(start));
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 查询版本信息
     */
    public static void queryVersionInfo() {
        int[] start = {0x2a, 0x05, 0x23, 0x0c, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 控制声音长一声
     */
    public static void controlVoiceLong() {
        int[] arr = {0x2a, 0x07, 0x04, 0x01, 0x50, 0x78, 0x23};
        SendUtil.sendMessage(arr, MyApplication.getOutputStream());
    }

    /**
     * 控制声音长2声
     */
    public static void controlVoiceLongTwo() {
        int[] arr = {0x2a, 0x07, 0x04, 0x02, 0x50, 0x7b, 0x23};
        SendUtil.sendMessage(arr, MyApplication.getOutputStream());
    }

    /**
     * 设置下位机是否连接服务器
     *
     * @param isLinkService true 可以连接
     */
    public static void setLinkServiceState(boolean isLinkService) {
        int[] start;
        if (isLinkService) {
            start = new int[]{0x2a, 0x06, 0x16, 0x00, 0x3A, 0x23};
        } else {
            start = new int[]{0x2a, 0x06, 0x16, 0x01, 0x3B, 0x23};
        }
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 设置加锁解锁状态
     */
    public static void setLock(boolean isLock) {
        int[] start;
        if (isLock) {            //加锁
            start = new int[]{0x2a, 0x06, 0x15, 0x01, 0x38, 0x23};
        } else {
            start = new int[]{0x2a, 0x06, 0x15, 0x00, 0x39, 0x23};
        }
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 设置激活状态
     */
    public static void setActionState(boolean isAction) {
        int[] start;
        if (isAction) {            //设置激活
            start = new int[]{0x2a, 0x06, 0x14, 0x01, 0x39, 0x23};
        } else {                   //设置未激活
            start = new int[]{0x2a, 0x06, 0x14, 0x00, 0x38, 0x23};
        }
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 设置4S/修理厂
     */
    public static void setVersionState(boolean isVersion) {
        int[] start;
        if (isVersion) {            //4S
            start = new int[]{0x2a, 0x06, 0x17, 0x01, 0x3A, 0x23};
        } else {                   //修理厂
            start = new int[]{0x2a, 0x06, 0x17, 0x00, 0x3B, 0x23};
        }
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 自定义常规模式
     */
    public static void setChengguiState(boolean isVersion) {
        int[] start;
        if (isVersion) {            //禁止
            start = new int[]{0x2a, 0x06, 0x30, 0x01, 0x1D, 0x23};
        } else {                   //允许
            start = new int[]{0x2a, 0x06, 0x30, 0x00, 0x1C, 0x23};
        }
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 获取服务器设置的运行时间,设置大主板上的数组
     *
     * @return
     */
    public static void setRuningTimeData(int zOne, int zTwo, int zThree, int cOne, int cTwo, int cThree) {
        int zOneR = checkData(zOne);
        int zTwoR = checkData(zTwo);
        int zThreeR = checkData(zThree);
        int cOneR = checkData(cOne);
        int cTwoR = checkData(cTwo);
        int cThreeR = checkData(cThree);
        int xor = 0x2a ^ 0x0B ^ 0x19 ^ zOneR ^ zTwoR ^ zThreeR ^ cOneR ^ cTwoR ^ cThreeR;
        int[] arr = {0x2a, 0x0B, 0x19, zOneR, zTwoR, zThreeR, cOneR, cTwoR, cThreeR, xor, 0x23};
        Log.d(TAG, "socketReadResponse: -------"+cOne+"  "+Arrays.toString(arr));
        SendUtil.sendMessage(arr, MyApplication.getOutputStream());
    }

    private static int checkData(int data) {
        int result;
        switch (data) {
            case 48:
                result = 0;
                break;
            case 49:
                result = 1;
                break;
            case 50:
                result = 2;
                break;
            case 51:
                result = 3;
                break;
            case 52:
                result = 4;
                break;
            case 53:
                result = 5;
                break;
            case 54:
                result = 6;
                break;
            case 55:
                result = 7;
                break;
            case 56:
                result = 8;
                break;
            case 57:
                result = 9;
                break;
            case 64:
                result = 10;
                break;
            case 65:
                result = 11;
                break;
            case 66:
                result = 12;
                break;
            case 67:
                result = 13;
                break;
            case 68:
                result = 14;
                break;
            case 69:
                result = 15;
                break;
            case 70:
                result = 0;
                break;
            default:
                result = 0;
                break;

        }
        return result;
    }
}
