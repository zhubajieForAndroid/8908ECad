package com.E8908.util;

import android.content.SharedPreferences;

import com.E8908.base.MyApplication;


public class SharedPreferencesUtils {
    /**
     * 获取在设置就绪和未就绪保存的Sp
     * @return
     */
    public static SharedPreferences getReadyStateValueSp(){
        return MyApplication.getContext().getSharedPreferences("upReadyStateValue",0);
    }
    /**
     * 获取在设置工作状态保存的SP
     * @return
     */
    public static SharedPreferences getWorkValueSp(){
        return MyApplication.getContext().getSharedPreferences("upWorkStateValue",0);
    }
    /**
     * 获取在设置保存的系数的sp
     * @return
     */
    public static SharedPreferences getCoefficientPortState(){
        return MyApplication.getContext().getSharedPreferences("upCoefficientStateValue",0);
    }
    /**
     * 获取经纬度保存时需要的Sp
     * @return
     */
    public static SharedPreferences getLocationState(){
        return MyApplication.getContext().getSharedPreferences("upLocationStateValue",0);
    }
    /**
     * 获取对比数据
     * @return
     */
    public static SharedPreferences getCheckState(){
        return MyApplication.getContext().getSharedPreferences("upCheckDataStateValue",0);
    }
    /**
     * 获取常规和自定义在回收成功失败时标记的sp
     * @return
     */
    public static SharedPreferences getRunBackSuccess(){
        return MyApplication.getContext().getSharedPreferences("isRunBackSuccessValue",0);
    }
    /**
     * 保存回收失败的次数
     * @return
     */
    public static SharedPreferences getBackErrorNumberSp(){
        return MyApplication.getContext().getSharedPreferences("backErrorNumber",0);
    }
    /**
     * 保存通讯状态的sp
     * @return
     */
    public static SharedPreferences getCommunicationStateSp(){
        return MyApplication.getContext().getSharedPreferences("communicationState",0);
    }

    /**
     * 获取存储pk的sp
     * @return
     */
    public static SharedPreferences getBleUpdataPkSp(){
        return MyApplication.getContext().getSharedPreferences("updataPk",0);
    }

    /**
     * 获取加注量和加注时间临时保存的sp
     * @return
     */
    public static SharedPreferences getAddNumbersAndAddTimeSp(){
        return MyApplication.getContext().getSharedPreferences("numberAndTime",0);
    }
    /**
     * 获取保存工作完成时的次数
     */
    public static SharedPreferences getWorkCount(){
        return  MyApplication.getContext().getSharedPreferences("completeWorkCount",0);
    }
}
