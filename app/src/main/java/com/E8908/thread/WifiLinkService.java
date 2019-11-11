package com.E8908.thread;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.E8908.conf.Constants;
import com.E8908.manage.PulseData;
import com.E8908.manage.SocketManage;
import com.E8908.manage.TestSendData;
import com.E8908.util.DataUtil;
import com.E8908.util.OkhttpManager;
import com.E8908.util.SendUtil;
import com.E8908.util.SharedPreferencesUtils;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.socket.client.impl.client.PulseManager;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.support.constraint.Constraints.TAG;

public class WifiLinkService extends Service {

    private BroadcastReceiver mBroadcastReceiver;
    private String mEquipmentId;
    private boolean mIsWifiLinkServiceState;                                        //wifi连接服务器状态
    private long mLinkServicetime = System.currentTimeMillis();                          //连接服务器超时时间
    private int mCurrentState;
    private SocketManage mSocketManage;
    private Timer mTimer;
    private boolean isStartTask = true;
    private boolean isWifiLinkState = false;
    private byte[] mBuffer;
    private int mRssi;
    private List<Byte> mByteArrayList;
    private List<Byte> mTempList;
    private boolean isSaveTime = true;      //是否可以保存当前的升数和当前的时间
    private long mOpenTime;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        regWifi();
        regBroadcast();
    }

    /**
     * 注册串口通讯广播
     */
    public void regBroadcast() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                byte[] responses = intent.getByteArrayExtra("response");
                int size = intent.getIntExtra("size", 0);
                if (responses != null) {
                    if (responses.length == Constants.DATA_LONG) {
                        mEquipmentId = DataUtil.getEquipmentNumber(responses);
                        //一秒判断一次
                        pauseLinkServiceState(responses);
                        if (!"00000000".equals(mEquipmentId)) {
                            //上传加注量
                            upDataAddCount(responses);
                        }
                    }
                    if (size == 6) {             //返回的版本信息数据
                        int versionInfo = responses[3];
                        switch (versionInfo) {
                            case 0:                 //修理厂的版本
                                if (!TextUtils.isEmpty(mEquipmentId) && mSocketManage != null) {
                                    byte[] bytes = DataUtil.up4SAndData(mEquipmentId, 1);
                                    mSocketManage.sendData(new TestSendData(bytes));
                                }
                                break;
                            case 1:                 //4s点的配置
                                if (!TextUtils.isEmpty(mEquipmentId) && mSocketManage != null) {
                                    byte[] bytes1 = DataUtil.up4SAndData(mEquipmentId, 0);
                                    mSocketManage.sendData(new TestSendData(bytes1));
                                }
                                break;
                        }
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter(Constants.DATA);
        registerReceiver(mBroadcastReceiver, filter);
    }

    private void upDataAddCount(byte[] responses) {
        String state = DataUtil.getState(responses);                           //状态位
        String frontLockState = state.substring(2, 3);                       //前门锁定状态
        if (frontLockState.equals("1") && isSaveTime){
            //前门已经打开了
            String riseNumbwe = DataUtil.getRiseNumbwe(responses);                 //获取液体升数
            int resultNumber = Integer.parseInt(riseNumbwe, 16);
            SharedPreferences addNumbersAndAddTimeSp = SharedPreferencesUtils.getAddNumbersAndAddTimeSp();
            SharedPreferences.Editor edit = addNumbersAndAddTimeSp.edit();
            edit.putInt("numbers",resultNumber);
            edit.apply();
            isSaveTime = false;
            mOpenTime = System.currentTimeMillis();
        }else if (frontLockState.equals("0") && !isSaveTime){
            //前门已经关闭了
            isSaveTime = true;
            String riseNumbwe = DataUtil.getRiseNumbwe(responses);                 //获取液体升数
            int resultNumber = Integer.parseInt(riseNumbwe, 16);
            SharedPreferences addNumbersAndAddTimeSp = SharedPreferencesUtils.getAddNumbersAndAddTimeSp();
            int numbers = addNumbersAndAddTimeSp.getInt("numbers", 0);

            int changeCount = resultNumber - numbers;

            long l = System.currentTimeMillis();
            long resultTime = (l - mOpenTime)/1000;

            if (changeCount >= 100){
                //在前门开着的时候,药液的升数有变化了,上传数据
                Map<String,String> pames = new HashMap<>();
                pames.put("dataType","1");
                pames.put("equipmentCode",mEquipmentId);
                pames.put("beforeFillingVolume",numbers+"");
                pames.put("fillingVolume",changeCount+"");
                pames.put("afterFillingVolume",resultNumber+"");
                pames.put("frontOrback","1");
                pames.put("fillingTime",resultTime+"");
                OkhttpManager okhttpManager = OkhttpManager.getOkhttpManager();
                okhttpManager.doPost(Constants.URLS.UPDATA_ADD_NUMBER,pames,mUpdataCallback);
            }
        }
    }
    private Callback mUpdataCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {}

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful()){
            }
        }
    };

    private void pauseLinkServiceState(byte[] resole) {
        mBuffer = resole;
        if (isWifiLinkState) {                       //wifi已连接
            mSocketManage = SocketManage.getSocketManage();
            mSocketManage.setOnSocketLinkListener(mOnSocketLinkListener);
            if (!"00000000".equals(mEquipmentId) && !mIsWifiLinkServiceState) {
                //不允许下位机连接服务器了
                SendUtil.setLinkServiceState(false);
                mSocketManage.connect();
                Log.d(TAG, "pauseLinkServiceState: wifi连接了");
            }
        } else {                                     //wifi未连接
            //允许下位机连接服务器了
            SendUtil.setLinkServiceState(true);
            if (mSocketManage != null && mIsWifiLinkServiceState) {
                mIsWifiLinkServiceState = false;
                mSocketManage.getConnectionManager().disconnect();
            }
            //}
        }
    }


    private SocketManage.OnSocketLinkListener mOnSocketLinkListener = new SocketManage.OnSocketLinkListener() {
        @Override
        public void socketReadResponse(ConnectionInfo connectionInfo, String s, OriginalData originalData) {
            //Socket从服务器读取到字节回调
            //解析
            byte[] bodyBytes = originalData.getBodyBytes();
            byte[] headBytes = originalData.getHeadBytes();
            byte[] resultBytes = new byte[bodyBytes.length + headBytes.length];
            System.arraycopy(headBytes, 0, resultBytes, 0, headBytes.length);
            System.arraycopy(bodyBytes, 0, resultBytes, headBytes.length, bodyBytes.length);

            if (mByteArrayList == null) {
                mByteArrayList = new ArrayList<>();
            }
            for (int i = 0; i < resultBytes.length; i++) {
                mByteArrayList.add(resultBytes[i]);
            }
            int length = mByteArrayList.get(1);
            if (mByteArrayList.size() >= length + 2 && mByteArrayList.get(0) == 42 && mByteArrayList.get(length + 1) == 35) {
                mTempList = mByteArrayList.subList(0, length + 2);
                mByteArrayList = mByteArrayList.subList(length + 2, mByteArrayList.size());
                //取出命令字
                Log.d(TAG, "socketReadResponse: 收到服务器数据" + Arrays.toString(resultBytes) + "  " + mTempList.get(6));

                //完整数据
                switch (mTempList.get(6)) {
                    case 0x07:                                              //登录命令字
                        Log.d(TAG, "onSocketReadResponse: 登录成功");
                        SendUtil.controlVoiceLong();
                        //登录成功发送心跳
                        if (!TextUtils.isEmpty(mEquipmentId) && mSocketManage != null) {
                            mSocketManage.sendHeartbeat(new PulseData(DataUtil.getLinkData(mEquipmentId)));
                            SystemClock.sleep(500);
                            //主动上报数据
                            startUpTask();
                        }
                        break;
                    case 0x08:                                              //链路命令字
                        //喂狗操作
                        if (mSocketManage != null) {
                            IConnectionManager manager = mSocketManage.getConnectionManager();
                            PulseManager pulseManager = manager.getPulseManager();
                            pulseManager.feed();
                        }
                        break;
                    case 80:  //激活
                        SendUtil.setActionState(true);
                        break;
                    case 100:  //有无故障
                        String hasError = mTempList.get(18) + "";
                        switch (hasError) {
                            case "48":  //解锁
                                SendUtil.setLock(false);
                                break;
                            case "49":  //锁定
                                SendUtil.setLock(true);
                                break;
                            case "50":  //常规关
                                SendUtil.setChengguiState(true);
                                break;
                            case "51":  //常规开
                                SendUtil.setChengguiState(false);
                                break;
                        }
                        break;
                    case 18:                        //收到该指令马上上报一次数据
                        //先停止主动上报的定时器
                        pauseUpTask();
                        SystemClock.sleep(500);
                        compareData(false);
                        break;
                    case 66:                        //开后盖
                        SendUtil.openHou();
                        break;
                    case 67:                        //开前盖
                        SendUtil.openQian();
                        break;
                    case 99:                        //设置版本,4s和修理厂
                        String code = mTempList.get(18) + "";
                        switch (code) {
                            case "48":  //修理厂
                                SendUtil.setVersionState(false);
                                break;
                            case "49":  //4S
                                SendUtil.setVersionState(true);
                                break;
                        }
                        break;
                    case 0x01:                      //上传终端版本号
                        if (mBuffer.length == Constants.DATA_LONG && mSocketManage != null && !TextUtils.isEmpty(mEquipmentId)) {
                            byte[] ints = DataUtil.upDataVersionCode(mEquipmentId, DataUtil.getVwesionToSoftware(mBuffer));

                            mSocketManage.sendData(new TestSendData(ints));
                        }
                        break;
                    case 53:                        //上传4S点或修理厂
                        SendUtil.queryVersionInfo();
                        break;
                    case 6:                         //上传自定义和常规的时间
                        if (mBuffer.length == Constants.DATA_LONG && mSocketManage != null && !TextUtils.isEmpty(mEquipmentId)) {
                            int routineOzoneRunTime = DataUtil.getRoutineOzoneRunTime(mBuffer); //常规模式第一阶
                            int routineTwoRunTime = DataUtil.getRoutineTwoRunTime(mBuffer);     //常规模式第二阶段
                            int routineThreeRunTime = DataUtil.getRoutineThreeRunTime(mBuffer); //常规模式第三阶段

                            int depthOneRunTime = DataUtil.getDepthOneRunTime(mBuffer);         //深度模式第一阶段
                            int depthTwoRunTime = DataUtil.getDepthTwoRunTime(mBuffer);         //深度模式第二阶段
                            int depthThreeRunTime = DataUtil.getDepthThreeRunTime(mBuffer);     //深度模式第三阶段
                            byte[] upTimeData = DataUtil.getUpTimeData(mEquipmentId, depthOneRunTime, depthTwoRunTime, depthThreeRunTime, routineOzoneRunTime, routineTwoRunTime, routineThreeRunTime);
                            mSocketManage.sendData(new TestSendData(upTimeData));
                        }
                        break;
                    case 3:                         //设置常规和自定义的时间   3 2 1

                        //0x2A,0x17,0x00,0x02,0x03,0x00,0x03,0x00,0x17,0x01,0x14,
                        //0x65,0x65,0x66,0x73,0x24,0x54,
                        // 0x41,0x35,0x34,0x33,0x32,0x31,
                        // 0x28,0x23,

                        //0x2A,0x17,0x00,0x01,0x03,0x00,0x03,0x00,0x17,0x01,0x14,
                        // 0x65,0x65,0x66,0x73,0x24,0x54,0x41,0x35,0x34,0x33,0x32,0x31,0x2B,0x23,
                        char cOne = (char) resultBytes[17];
                        char cTwo = (char) resultBytes[18];
                        char cThree = (char) resultBytes[19];

                        char zOne = (char) resultBytes[20];
                        char zTwo = (char) resultBytes[21];
                        char zThree = (char) resultBytes[22];
                        SendUtil.setRuningTimeData(zOne, zTwo, zThree, cOne, cTwo, cThree);
                        break;
                    case 51:                        //查询加注量
                       /* if (mBuffer.length == Constants.DATA_LONG && mSocketManage != null && !TextUtils.isEmpty(mEquipmentId)) {
                            int i = DataUtil.getAddNumbwe(mBuffer);
                            byte[] addNumberData = DataUtil.getAddNumberData(mEquipmentId, i);
                            Log.d(TAG, "socketReadResponse: 加注量="+i+"  "+Arrays.toString(addNumberData));
                            mSocketManage.sendData(new TestSendData(addNumberData));
                        }*/
                        break;


                }
            }
        }

        @Override
        public void onSocketDisconnection(ConnectionInfo connectionInfo, String s, Exception e) {
        }

        @Override
        public void onSocketConnectionSuccess(ConnectionInfo connectionInfo, String s) {
            mIsWifiLinkServiceState = true;
            //连接成功
            if (!TextUtils.isEmpty(mEquipmentId)) {
                byte[] loginData = DataUtil.getLoginData(mEquipmentId);
                mSocketManage.sendData(new TestSendData(loginData));
                Log.d(TAG, "onSocketConnectionSuccess: " + Arrays.toString(loginData));
            }
            //不允许DTU连接服务器了
            Log.d(TAG, "onSocketConnectionSuccess: 连接成功" + mEquipmentId);
        }

        @Override
        public void onSocketConnectionFailed(ConnectionInfo connectionInfo, String s, Exception e) {
            Log.d(TAG, "onSocketConnectionFailed: 连接失败");
            mIsWifiLinkServiceState = false;

        }
    };


    private int getStateThree(int d) {
        byte[] result = new byte[8];
        //设备锁定
        boolean lockState = DataUtil.getLockState(mBuffer);
        if (lockState) {
            result[0] = 1;
        } else {
            result[0] = 0;
        }
        //加注异常或正常
        SharedPreferences workValueSp = SharedPreferencesUtils.getWorkValueSp();
        int workStateValue = workValueSp.getInt("workStateValue", -1);
        if (workStateValue == 8) {
            result[1] = 1;
        } else {
            result[1] = 0;
        }
        //wifi状态
        if (isWifiLinkState) {
            result[2] = 1;
        } else {
            result[2] = 0;
        }
        if (d == 18935 || workStateValue != 4) {                //电子秤系数等于初始值表示没有标定,8908E初始值18935
            result[3] = 0;
        } else {
            //电子秤标定
            result[3] = 1;
        }

        //设备激活
        boolean actionState = DataUtil.getActionState(mBuffer);
        if (actionState) {
            result[4] = 1;
        } else {
            result[4] = 0;
        }
        //系数校准
        SharedPreferences coefficientPortState = SharedPreferencesUtils.getCoefficientPortState();
        boolean coefficientState = coefficientPortState.getBoolean("coefficientState", false);
        if (coefficientState) {
            result[5] = 1;
        } else {
            result[5] = 0;
        }
        //电子秤状态,有数值电子秤正常
        String ratioNumbwe = DataUtil.getRiseNumbwe(mBuffer);
        if ("0".equals(ratioNumbwe)) {
            result[6] = 1;
        } else {
            result[6] = 0;
        }
        //温度传感器
        boolean temperatureState = DataUtil.getTemperatureState(mBuffer);
        if (temperatureState) {
            result[7] = 0;
        } else {
            result[7] = 1;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(result[i]);
        }
        return Integer.valueOf(sb.toString(), 2);
    }

    private int getEquipment() {
        byte[] result = new byte[8];
        SharedPreferences readyStateValueSp = SharedPreferencesUtils.getReadyStateValueSp();
        int readyAndUnReadyState = readyStateValueSp.getInt("readyAndUnReadyState", -1);
        if (readyAndUnReadyState == 1) {
            result[0] = 1;
        }
        if (readyAndUnReadyState == 0) {
            result[0] = 0;
        }
        //获取后门状态
        String afterState = DataUtil.getAfterState(mBuffer);
        if ("1".equals(afterState)) {
            result[1] = 1;
        } else {
            result[1] = 0;
        }
        //前门状态
        String beforeState = DataUtil.getBeforeState(mBuffer);
        if ("1".equals(beforeState)) {
            result[2] = 1;
        } else {
            result[2] = 0;
        }
        //获取设置的工作状态(加注,臭氧,净化,雾化)
        SharedPreferences workValueSp = SharedPreferencesUtils.getWorkValueSp();
        int workStateValue = workValueSp.getInt("workStateValue", -1);
        switch (workStateValue) {
            case 128:            //正在加注药液
                result[3] = 1;
                break;
            case 80:            //臭氧和雾化
                result[6] = 1;
                result[4] = 1;
                break;
            case 64:            //杀菌消毒
                result[4] = 1;
                break;
            case 32:            //净化
                result[5] = 1;
                break;
            case 16:            //雾化
                result[6] = 1;
                break;
        }
        //通讯状态
        SharedPreferences stateSp = SharedPreferencesUtils.getCommunicationStateSp();
        boolean dataState = stateSp.getBoolean("dataState", false);
        if (dataState) {         //正常
            result[7] = 0;
        } else {                 //异常
            result[7] = 1;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(result[i]);
        }
        return Integer.valueOf(sb.toString(), 2);
    }


    //---------------------主动上报任务 开始------------------------//
    private void startUpTask() {
        isStartTask = true;
        if (mTimer == null) {
            mTimer = new Timer();
            mTimer.schedule(mTimerTask, 500, 3000);
        }
    }

    private void stopUpTask() {
        isStartTask = false;
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    private void pauseUpTask() {
        isStartTask = false;
    }

    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (isStartTask) {
                //对比数据,上传数据
                compareData(true);
            }
        }
    };

    private synchronized void compareData(boolean isCheck) {
        if (mBuffer.length == Constants.DATA_LONG) {
            SharedPreferences checkState = SharedPreferencesUtils.getCheckState();
            String checkDataByUp = checkState.getString("checkDataByUp", "");
            int oilData = checkState.getInt("oilData", -1);

            String result;
            //获取允许工作总次数
            String workCount = DataUtil.getWorkCount(mBuffer);
            int workCountInt = Integer.parseInt(workCount, 16);
            //获取电子秤系数
            String ratioNumbwe = DataUtil.getRatioNumbwe(mBuffer);
            int ratioNumbweInt = Integer.parseInt(ratioNumbwe, 16);
            //获取wifi信号强度
            int rssi = 0;
            if (mRssi <= 0 && mRssi >= -50) {
                rssi = 32;
            } else if (mRssi < -50 && mRssi >= -70) {
                rssi = 24;
            } else if (mRssi < -70 && mRssi >= -80) {
                rssi = 16;
            } else if (mRssi < -80 && mRssi >= -100) {
                rssi = 8;
            } else {
                rssi = 0;
            }
            //获取温度
            int temperature = DataUtil.getTemperature(mBuffer);
            //经纬度
            SharedPreferences locationState = SharedPreferencesUtils.getLocationState();
            //获取纬度信息
            String latitude = locationState.getString("latitude", "");
            //获取经度信息
            String longitude = locationState.getString("longitude", "");

            String re = latitude + longitude;
            //获取交流电压
            int pressInt = DataUtil.directElectricPress(mBuffer);
            //获取交流电流
            int flowInt = DataUtil.directElectricFlow(mBuffer);
            //获取常规工作次数
            String routineWorkNumbwe = DataUtil.getRoutineWorkNumbwe(mBuffer);
            int routineWorkNumbweInt = Integer.parseInt(routineWorkNumbwe, 16);
            //获取深度工作总次数
            String depthWorkNumbwe = DataUtil.getDepthWorkNumbwe(mBuffer);
            int depthWorkNumbweInt = Integer.parseInt(depthWorkNumbwe, 16);
            //获取加注总升数
            String riseTotelNumbwe = DataUtil.getRiseTotelNumbwe(mBuffer);
            int riseTotelNumbweInt = Integer.parseInt(riseTotelNumbwe, 16);
            //获取液体剩余升数
            String riseNumbwe = DataUtil.getRiseNumbwe(mBuffer);
            int riseNumbweInt = Integer.parseInt(riseNumbwe, 16);

            //状态1 最高位强制设置为0代表设备数据,  00000010 代表wifi 00000000代表DTU
            int stateOne = 2;

            //获取状态2
            int stateTwo = getEquipment();
            //获取状态3
            int stateThree = getStateThree(ratioNumbweInt);


            result = workCount + ratioNumbwe + rssi + temperature + re + pressInt + flowInt +
                    routineWorkNumbwe + depthWorkNumbwe + riseTotelNumbwe + stateOne + stateTwo + stateThree;
            if (isCheck) {
                if (!result.equals(checkDataByUp) || Math.abs(riseNumbweInt - oilData) >= 50) {
                    byte[] upDateData = DataUtil.getUpDateData(mEquipmentId, workCountInt, ratioNumbweInt, rssi, temperature,
                            latitude, longitude, pressInt, flowInt, routineWorkNumbweInt, depthWorkNumbweInt, riseTotelNumbweInt,
                            riseNumbweInt, stateOne, stateTwo, stateThree);
                    mSocketManage.sendData(new TestSendData(upDateData));
                }
            }else {
                byte[] upDateData = DataUtil.getUpDateData(mEquipmentId, workCountInt, ratioNumbweInt, rssi, temperature,
                        latitude, longitude, pressInt, flowInt, routineWorkNumbweInt, depthWorkNumbweInt, riseTotelNumbweInt,
                        riseNumbweInt, stateOne, stateTwo, stateThree);
                mSocketManage.sendData(new TestSendData(upDateData));
            }

            SharedPreferences.Editor edit = checkState.edit();
            edit.putString("checkDataByUp", result);
            edit.putInt("oilData", riseNumbweInt);
            edit.apply();
            //开启定时器
            if (!isStartTask) {
                startUpTask();
            }
        }

    }

    private void regWifi() {
        //注册Wifi监听
        IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(mReceiver, filter);
    }

    //监听wifi状态
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiInfo.isConnected()) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = wifiManager.getConnectionInfo();
                mRssi = info.getRssi();
                isWifiLinkState = true;
            } else {
                isWifiLinkState = false;
            }
        }

    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        //退出登录
        if (mSocketManage != null && !TextUtils.isEmpty(mEquipmentId)) {
            mSocketManage.disconnect(mEquipmentId);
        }
        unregisterReceiver(mBroadcastReceiver);
        unregisterReceiver(mReceiver);
        stopUpTask();
    }
}
