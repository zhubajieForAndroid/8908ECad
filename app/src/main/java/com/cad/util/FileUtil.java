package com.cad.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.cad.base.MyApplication;

import java.io.File;
import java.io.IOException;

/**
 * Created by dell on 2018/3/23.
 */

public class FileUtil {
    /**
     * 创建新文件
     * @param parent    文件夹名字
     * @param name      文件名字
     * @return
     */
    public static File createNewFile(String parent,String name){
        File file = new File(makeDirToSD(parent),name);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 保存发送查询数据的标记
     * @param state
     */
    public static void putSendState(boolean state){
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences("send", 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("sendState",state);
        edit.commit();
    }

    /**
     * 取出发送查询数据的标记
     * @return
     */
    public static boolean getSendState(){
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences("send", 0);
        return sp.getBoolean("sendState", true);
    }

    /**
     * sd卡跟目录创建文件夹
     * @return
     */
    private static File makeDirToSD(String parent){
        File file = new File(getSDPath(),parent);
        if (!file.exists()){
            file.mkdir();
        }
        return file;
    }
    /**
     * 获取SD卡跟目录
     * @return
     */
    private static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);// 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        }
        return sdDir.toString();
    }
}
