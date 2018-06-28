package com.cad.util;

import android.util.Log;

import com.cad.base.MyApplication;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by dell on 2018/3/6.
 */

public class SendUtil {
    //发送数据
    public static void sendMessage(int[] arr, OutputStream os) {

        try {
            for (int i = 0; i < arr.length; i++) {
                os.write(arr[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 控制声音
     */
    public static void controlVoice(){
        int[] arr = {0x2a, 0x07, 0x04, 0x01, 0x14, 0x3c, 0x23};
        SendUtil.sendMessage(arr, MyApplication.getOutputStream());
    }
    /**
     * 开前盖
     */
    public static void openQian(){
        int[] arr = {0x2a, 0x06, 0x0c, 0x55,  0x75, 0x23};
        SendUtil.sendMessage(arr, MyApplication.getOutputStream());
    }
    /**
     * 开后盖
     */
    public static void openHou(){
        int[] arr = {0x2a, 0x06, 0x0c, 0xaa, 0x8a, 0x23};
        SendUtil.sendMessage(arr, MyApplication.getOutputStream());
    }
    /**
     * 打开4#触点
     */
    public static void open4(){
        int[] start = {0x2a, 0x06, 0x01, 0x08, 0x25, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }
    /**
     * 打开4#6#触点
     */
    public static void open4And6(){
        int[] start = {0x2a, 0x06, 0x01, 0x28, 0x05, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }
    /**
     * 打开2#触点
     */
    public static void open2(){
        int[] start = {0x2a, 0x06, 0x01, 0x02, 0x2F, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 打开6#触点
     */
    public static void open6(){
        int[] start = {0x2a, 0x06, 0x01, 0x20, 0x0d, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }


    /**
     * 打开7#8#触点
     */
    public static void open7And8(){
        int[] start = {0x2a, 0x06, 0x01, 0xc0, 0xED, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 关闭7#触点 8#是开着的
     */
    public static void close7(){
        int[] start = {0x2a, 0x06, 0x01, 0x80, 0xAD, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }
    /**
     * 打开#触点
     */
    public static void open8(){
        //2a 06 01 80 ad 23
        int[] start = {0x2a, 0x06, 0x01, 0x80, 0xAD, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }

    /**
     * 关闭所有触点
     */
    public static void closeAll(){
        int[] start = {0x2a, 0x06, 0x01, 0x00, 0x2D, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }
    /**
     * 设置工作状态
     * @param  state 工作的状态16进制
     */
    public static void setWorkState(int state){
        int result = 0x2a^0x06^0x08^state;
        int[] start = {0x2a, 0x06, 0x08, state, result, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }



    /**
     * 设置id
     * @param ids
     */
    public static void setEquipentId(List<String> ids){
        int result = 0x2a ^ 0x0d ^ 0x06 ^ Integer.parseInt(ids.get(0)) ^ Integer.parseInt(ids.get(1)) ^ Integer.parseInt(ids.get(2)) ^
                Integer.parseInt(ids.get(3)) ^ Integer.parseInt(ids.get(4)) ^ Integer.parseInt(ids.get(5)) ^ Integer.parseInt(ids.get(6)) ^ Integer.parseInt(ids.get(7)) ;
        int[] start = {0x2a, 0x0d, 0x06, Integer.parseInt(ids.get(0)), Integer.parseInt(ids.get(1)), Integer.parseInt(ids.get(2)),
                Integer.parseInt(ids.get(3)), Integer.parseInt(ids.get(4)), Integer.parseInt(ids.get(5)), Integer.parseInt(ids.get(6)),
                Integer.parseInt(ids.get(7)),result, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }
    /**
     * 设置常规保养的次数
     */
    public static void setRoutineNumber(int number){
        int gao = (number & 0x0000ff00)>>8;
        int di = number & 0x000000ff;
        int result = 0x2a^0x08^0x02^0xaa^gao^di;
        int[] start = {0x2a, 0x08, 0x02, 0xaa,gao,di, result, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }
    /**
     * 设置深度保养的次数
     */
    public static void setDepthNumber(int number){
        int gao = ((number & 0x0000ff00)>>8);
        int di = (number & 0x000000ff);
        int result = 0x2a^0x08^0x02^0x55^gao^di;
        int[] start = {0x2a, 0x08, 0x02, 0x55,gao,di, result, 0x23};
        for (int i = 0; i < start.length; i++) {
            Log.d(TAG, "setDepthNumber: "+start[i]);
        }
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }
    /**
     * 设置加注总量单位毫升
     * @param num
     */
    public static void setChangeOilNumber(int num){
        int gao = ((num & 0x00ff0000)>>16);
        int zhong = ((num & 0x0000ff00)>>8);
        int di = (num & 0x000000ff);
        int result = 0x2a^0x08^0x09^gao^zhong^di;
        int[] start = {0x2a, 0x08, 0x09, gao, zhong, di,result, 0x23};
        for (int i = 0; i < start.length; i++) {
            Log.d(TAG, "setChangeOilNumber: "+start[i]);
        }
        SendUtil.sendMessage(start,MyApplication.getOutputStream());
    }

    /**
     * 电子秤清零
     * @param num
     */
    public static void clear(int num){
        int iGao = ((num & 0x0000FF00) >> 8);
        int iDi = (num & 0x000000FF);
        int xorResult = 0x2a ^ 0x07 ^ 0x0a ^ iGao ^ iDi;
        int[] clear = {0x2a, 0x07, 0x0a, iGao, iDi, xorResult, 0x23};
        SendUtil.sendMessage(clear, MyApplication.getOutputStream());
    }

    /**
     * 恢复出厂设置
     */
    public static void renewData(){
        int[] start = {0x2a, 0x05, 0x0b, 0x24, 0x23};
        SendUtil.sendMessage(start, MyApplication.getOutputStream());
    }
}
