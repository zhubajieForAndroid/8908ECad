package com.E8908.blueTooth;

import android.app.Service;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.E8908.blueTooth.dao.ResultQueryYunBean;
import com.E8908.blueTooth.dao.YunDataDao;
import com.E8908.blueTooth.utils.DataBleUtil;
import com.E8908.blueTooth.utils.ReadUtil;
import com.E8908.blueTooth.utils.SendBleUtils;
import com.E8908.conf.Constants;
import com.E8908.util.IoUtils;
import com.E8908.util.NavigationBarUtil;
import com.E8908.util.OkhttpManager;
import com.E8908.util.SharedPreferencesUtils;
import com.E8908.widget.ToastUtil;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleRssiCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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

public class SendBleDataService extends Service {

    private String mEquipmentID;
    private String mDeviceMac;
    private BleDevice mDevice;
    private Timer mTimer;
    private BleManager mBleManager;
    private String mUuid_service;
    private String mUuid_chara;
    private Map<String, String> mPames;
    private OkhttpManager mOkhttpManager;
    private SharedPreferences mSp;
    private SharedPreferences mPreferences;
    private String mPk;
    private String mShopName;
    private String mCarNumber;
    private SharedPreferences mBleIdSp;
    private MySendTask mMySendTask;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBleManager = BleManager.getInstance();
        mPames = new HashMap<>();
        mOkhttpManager = OkhttpManager.getOkhttpManager();
        mSp = getSharedPreferences("checkGasDate", 0);
        //保存的是施工状态
        mPreferences = getSharedPreferences("workStateByGas", 0);
        //保存蓝牙ID
        mBleIdSp = getSharedPreferences("BledeviceInfo",0);

        //取出保存的pk
        SharedPreferences bleUpdataPkSp = SharedPreferencesUtils.getBleUpdataPkSp();
        mPk = bleUpdataPkSp.getString("upPk",null);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            mShopName = intent.getStringExtra("shopName");
            mCarNumber = intent.getStringExtra("carNumber");
            mDeviceMac = intent.getStringExtra("BleDeviceMac");
        }
        if (!TextUtils.isEmpty(mDeviceMac))
            mBleManager.connect(mDeviceMac, mBleGattCallback);
        return super.onStartCommand(intent, flags, startId);
    }


    private BleGattCallback mBleGattCallback = new BleGattCallback() {
        @Override
        public void onStartConnect() {
            Log.d(TAG, "onStartConnect: 开始连接");
        }

        @Override
        public void onConnectFail(BleDevice bleDevice, BleException exception) {
            //连接失败
            Log.d(TAG, "onStartConnect: 连接失败");
            Intent intent = new Intent();
            intent.setAction(Constants.BLE_DATA);
            intent.putExtra("data", new byte[]{});
            intent.putExtra("isLinkBle", false);
            sendBroadcast(intent);
        }

        @Override
        public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
            Log.d(TAG, "onConnectSuccess: 连接成功");
            mDevice = bleDevice;
            //连接成功并发现服务。
            List<BluetoothGattService> serviceList = gatt.getServices();
            for (BluetoothGattService service : serviceList) {
                mUuid_service = service.getUuid().toString();
                List<BluetoothGattCharacteristic> characteristicList = service.getCharacteristics();
                for (BluetoothGattCharacteristic characteristic : characteristicList) {
                    mUuid_chara = characteristic.getUuid().toString();
                }
            }
            SystemClock.sleep(100);
            //订阅
            mBleManager.notify(bleDevice, mUuid_service, mUuid_chara, false, mBleNotifyCallback);
            Log.d(TAG, "onConnectSuccess: 连接成功");

            Intent intent = new Intent();
            intent.setAction(Constants.BLE_DATA);
            intent.putExtra("data", new byte[]{});
            intent.putExtra("isLinkBle", true);
            sendBroadcast(intent);

        }

        @Override
        public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
            Log.d(TAG, "onStartConnect: 主动断开连接"+isActiveDisConnected);
            //连接断开，特指连接后再断开的情况          isActiveDisConnected == true表示主动断开连接
            if (isActiveDisConnected) {

            } else {                //被动断开连接之后,从新链接
                //休眠一段时间在进行重连
                SystemClock.sleep(100);
                mBleManager.connect(mDeviceMac, this);
            }

        }
    };


    private void initSendTask() {
        if (mTimer == null){
            mTimer = new Timer();
        }
        if (mMySendTask == null){
            mMySendTask = new MySendTask();
            mTimer.schedule(mMySendTask, 0, 1000);
        }
    }
   
    private class MySendTask extends TimerTask {
        @Override
        public void run() {
            SendBleUtils.sendBleData(mBleManager, mDevice, mUuid_service, mUuid_chara, mEquipmentID, mBleWriteCallback);
        }
    }

    private BleNotifyCallback mBleNotifyCallback = new BleNotifyCallback() {
        @Override
        public void onNotifySuccess() {
            //读取蓝牙ID
            SendBleUtils.readBleID(mBleManager, mDevice, mUuid_service, mUuid_chara, mBleWriteCallback);
        }

        @Override
        public void onNotifyFailure(BleException exception) {

        }

        @Override
        public void onCharacteristicChanged(byte[] data) {
            byte[] resole = ReadUtil.resole(data);
            //[42, 22, 0, 2, 0, 3, 0, 0, 0, 3, -86, 3, 0, 2, 0, 3, 0, 0, 0, 3, -107, 35]   读取蓝牙ID
            if (resole != null) {
                //判断功能码
                if (resole[11] == 0x03) {
                    //获取蓝牙ID
                    mEquipmentID = ReadUtil.getBleID(resole);
                    //保存蓝牙ID到本地,为了在蓝牙列表中显示出来
                    SharedPreferences.Editor edit = mBleIdSp.edit();
                    edit.putString("deviceName","CAD-QT:"+mDevice.getName());
                    edit.putString("deviceMac",mDevice.getMac());
                    edit.apply();
                    //开启服务定时查询气体数据
                    initSendTask();
                } else {
                    int workState = mPreferences.getInt("workState", 1);
                    //发送数据广播
                    Intent intent = new Intent();
                    intent.setAction(Constants.BLE_DATA);
                    intent.putExtra("data", resole);
                    intent.putExtra("isLinkBle", true);
                    intent.putExtra("bleID", mEquipmentID);
                    sendBroadcast(intent);
                    if (!TextUtils.isEmpty(mPk)) {          //pk不等于空才去上传数据
                        //上传数据
                        upDataByGas(resole, workState);
                    }
                }
            }
        }
    };

    /**
     * 定时上传气体数据
     *
     * @param buffer
     * @param workState
     */
    private void upDataByGas(byte[] buffer, int workState) {
        String tvoc = DataBleUtil.getTVOC(buffer);
        String formaldehyde = DataBleUtil.getFormaldehyde(buffer);//甲醛
        String pm = DataBleUtil.getPM(buffer);

        float tvocInt = Integer.parseInt(tvoc, 16);             //tvoc
        float formaldehydeInt = Integer.parseInt(formaldehyde, 16);       //甲醛
        float pmInt = Integer.parseInt(pm, 16);               //pm
        String humidity = DataBleUtil.getHumidity(buffer); //湿度
        String temperatureByCheck = DataBleUtil.getTemperatureByCheck(buffer);//温度
        float v = formaldehydeInt / 1000;
        float v1 = tvocInt / 100;
        String format = String.format("%.2f", v);
        String format1 = String.format("%.2f", v1);

        //验证数据是否改变
        String changeData = mSp.getString("changeDataByGas", "");
        if (!(format + format1 + pmInt).equals(changeData)) {//数据有变化
            float shiInt = Integer.parseInt(humidity, 16) / 10;//湿度
            float wenInt = Integer.parseInt(temperatureByCheck, 16) / 10;        //温度
            mPames.put("deviceno", mEquipmentID);
            mPames.put("carnumber", mCarNumber);
            mPames.put("methanal", format);
            mPames.put("tvoc", format1);
            mPames.put("pmvalue", pmInt + "");
            mPames.put("temperature", wenInt + "");
            mPames.put("humidity", shiInt + "");
            if (!TextUtils.isEmpty(mPk))
                mPames.put("receivecarpk", mPk);
            if (!TextUtils.isEmpty(mShopName))
                mPames.put("storename", mShopName);
            mPames.put("location", "");                          //门店地址
            mPames.put("dataStage", workState + "");                            //施工阶段 1施工前2施工中3施工后
            mOkhttpManager.upDateGasData(Constants.URLS.UPDATE_BLE_GAS_DATA, mPames, mCallback);
            //更新数据
            SharedPreferences.Editor edit = mSp.edit();
            edit.putString("changeDataByGas", format + format1 + pmInt+"");
            edit.commit();
        }

    }

    private Callback mCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d(TAG, "onFailure: 网络异常");
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Log.d(TAG, "onStartConnect: "+response.body().string());
        }
    };
    private BleWriteCallback mBleWriteCallback = new BleWriteCallback() {
        @Override
        public void onWriteSuccess(int current, int total, byte[] justWrite) {
            // 发送数据到设备成功（分包发送的情况下，可以通过方法中返回的参数可以查看发送进度）
        }

        @Override
        public void onWriteFailure(BleException exception) {
            // 发送数据到设备失败
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
        }
        mBleManager.disconnect(mDevice);
        mBleManager.destroy();
        mBleManager.disconnectAllDevice();

    }
}
