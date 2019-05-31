package com.E8908.blueTooth;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import com.E8908.blueTooth.utils.SendBleUtils;
import com.E8908.R;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleIndicateCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;

import java.util.Arrays;
import java.util.List;


public class LinkBlueToothActivity extends BlueToothBase {

    private static final String TAG = "LinkBlueToothActivity";
    private BleDevice mDevice;
    private TextView mLinkState;
    private String mUuid_service;
    private String mUuid_chara;
    private BleManager mBleManager;
    public static String HEART_RATE_MEASUREMENT = "0000ffe1-0000-1000-8000-00805f9b34fb";
    @Override
    protected int getResID() {
        return R.layout.activity_link_blue_tooth;
    }
    @Override
    protected void initViews() {
        mLinkState = findViewById(R.id.link_state);
    }

    @Override
    protected void initData() {
        mDevice = getIntent().getParcelableExtra("device");
        //连接蓝牙
        mBleManager = BleManager.getInstance();
        mBleManager.connect(mDevice, mBleGattCallback);

    }
    private BleGattCallback mBleGattCallback = new BleGattCallback() {
        @Override
        public void onStartConnect() {
            //开始连接
            mLinkState.setText("连接中-----");
        }

        @Override
        public void onConnectFail(BleDevice bleDevice, BleException exception) {
            //连接失败
            mLinkState.setText("连接失败");
        }

        @Override
        public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
            //连接成功并发现服务。
            mLinkState.setText("连接成功,发现可用服务");
            List<BluetoothGattService> serviceList = gatt.getServices();
            for (BluetoothGattService service : serviceList) {
                mUuid_service = service.getUuid().toString();
                List<BluetoothGattCharacteristic> characteristicList= service.getCharacteristics();
                for(BluetoothGattCharacteristic characteristic : characteristicList) {
                    mUuid_chara = characteristic.getUuid().toString();
                }
            }
            //订阅蓝牙
            SystemClock.sleep(200);
            mBleManager.indicate(bleDevice,mUuid_service,mUuid_chara,mBleIndicateCallback);
        }

        @Override
        public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
            //连接断开，特指连接后再断开的情况          isActiveDisConnected == true表示主动断开连接
            if (isActiveDisConnected){
                mLinkState.setText("主动断开连接");
            }else {
                mLinkState.setText("断开连接");
                //休眠一段时间在进行重连
            }
        }
    };

    private BleIndicateCallback mBleIndicateCallback = new BleIndicateCallback() {
        @Override
        public void onIndicateSuccess() {
            //开启线程定时查询气体数据
            SystemClock.sleep(200);
            SendBleUtils.sendBleData(mBleManager,mDevice,mUuid_service,mUuid_chara,"00000000",mBleWriteCallback);
        }

        @Override
        public void onIndicateFailure(BleException exception) {
            Log.d(TAG, "onIndicateFailure: 打开通知失败");
        }

        @Override
        public void onCharacteristicChanged(byte[] data) {
            Log.d(TAG, "onReadSuccess: 读取成功="+Arrays.toString(data));
        }
    };

    private BleWriteCallback mBleWriteCallback = new BleWriteCallback() {
        @Override
        public void onWriteSuccess(int current, int total, byte[] justWrite) {
            // 发送数据到设备成功（分包发送的情况下，可以通过方法中返回的参数可以查看发送进度）
            Log.d(TAG, "onWriteFailure: 发送数据到设备成功");
        }

        @Override
        public void onWriteFailure(BleException exception) {
            // 发送数据到设备失败
            Log.d(TAG, "onWriteFailure: 发送数据到设备失败");
        }
    };


}
