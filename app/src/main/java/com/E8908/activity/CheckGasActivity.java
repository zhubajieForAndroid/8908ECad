package com.E8908.activity;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.E8908.blueTooth.SendBleDataService;
import com.E8908.blueTooth.utils.DataBleUtil;
import com.E8908.R;
import com.E8908.base.BaseActivity;
import com.E8908.conf.Constants;
import com.E8908.util.DataUtil;
import com.E8908.util.SendUtil;
import com.E8908.widget.InputCarNumberHinDialog;
import com.E8908.widget.MyProgressBar;
import com.E8908.widget.ToastUtil;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CheckGasActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "CheckGasActivity";
    @Bind(R.id.battery_state)
    ImageView mBatteryState;
    @Bind(R.id.toobar_bg_image)
    ImageView mToobarBgImage;
    @Bind(R.id.temperature_state)
    TextView mTemperatureState;
    @Bind(R.id.message_state)
    TextView mMessageState;
    @Bind(R.id.tab_image_back_state)
    ImageView mTabImageBackState;
    @Bind(R.id.tab_image_front_state)
    ImageView mTabImageFrontState;
    @Bind(R.id.tab_image_communication_state)
    ImageView mTabImageCommunicationState;
    @Bind(R.id.tab_image_wifi)
    ImageView mTabImageWifi;
    @Bind(R.id.progress)
    MyProgressBar mProgress;
    @Bind(R.id.progress_tvoc)
    MyProgressBar mProgressTvoc;
    @Bind(R.id.progress_pm)
    MyProgressBar mProgressPm;
    @Bind(R.id.progress_wen)
    MyProgressBar mProgressWen;
    @Bind(R.id.progress_shi)
    MyProgressBar mProgressShi;
    @Bind(R.id.back_btn)
    ImageView mBackBtn;
    @Bind(R.id.start_check_gas)
    ImageView mStartCheckGas;
    @Bind(R.id.formaldehyde_tv)
    TextView mFormaldehydeTv;
    @Bind(R.id.tvoc_tv)
    TextView mTvocTv;
    @Bind(R.id.pm_tv)
    TextView mPmTv;
    @Bind(R.id.wen_tv)
    TextView mWenTv;
    @Bind(R.id.shi_tv)
    TextView mShiTv;
    @Bind(R.id.progress_bar)
    LinearLayout mProgressBar;
    @Bind(R.id.sensor_status)
    TextView mSensorStatus;
    private boolean mIsYesData = false;
    private int mCurrentState;
    private InputCarNumberHinDialog mInputCarNumberDialog;
    private BleDevice mDevice;
    private BleManager mBleManager;
    private String mUuid_service;
    private String mUuid_chara;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_gas);
        ButterKnife.bind(this);
        mInputCarNumberDialog = new InputCarNumberHinDialog(this, R.style.dialog);
        initData();
    }

    @Override
    protected void electricInfo(int percent, boolean isCharging) {
        if (!isCharging) {                //没有在充电
            if (percent <= 20) {
                mBatteryState.setImageResource(R.mipmap.battery_icon_20);
            } else if (percent <= 40) {
                mBatteryState.setImageResource(R.mipmap.battery_icon_40);
            } else if (percent <= 60) {
                mBatteryState.setImageResource(R.mipmap.battery_icon_60);
            } else if (percent <= 80) {
                mBatteryState.setImageResource(R.mipmap.battery_icon_80);
            } else {                                 //电流81到100
                mBatteryState.setImageResource(R.mipmap.battery_icon_100_white);
            }
        } else {                                  //正在充电
            mBatteryState.setImageResource(R.mipmap.battery_icon_charge);
        }
    }

    private void initData() {
        mToobarBgImage.setBackgroundResource(R.mipmap.qt_nav);
        mBackBtn.setOnClickListener(this);
        mStartCheckGas.setOnClickListener(this);
        if (mProgressBar.getVisibility() == View.GONE) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        //设置每个进度条的最大值
        mProgress.setMax(6250);      //甲醛检测范围甲醛:0-6.25mg/m3
        mProgressTvoc.setMax(1000);     //tvov:0-10mg/m3 读到的是放大100被
        mProgressPm.setMax(300);           //pm2.5:ug/m3读到的数值  0-300
        mProgressWen.setMax(800);            //温度:-40  到 80
        mProgressShi.setMax(999);           //湿度:0-99.9%Rh

        mDevice = getIntent().getParcelableExtra("device");
        //连接蓝牙
        mBleManager = BleManager.getInstance();
        mBleManager.connect(mDevice, mBleGattCallback);
        //注册广播接受者
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.BLE_DATA);//wifi状态，是否连上，密码
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void isWifiConnected(boolean b, int level) {
        if (b) {
            if (level <= 0 && level >= -50) {
                mTabImageWifi.setImageResource(R.mipmap.wifi_4);
            } else if (level < -50 && level >= -70) {
                mTabImageWifi.setImageResource(R.mipmap.wifi_3);
            } else if (level < -70 && level >= -80) {
                mTabImageWifi.setImageResource(R.mipmap.wifi_2);
            } else if (level < -80 && level >= -100) {
                mTabImageWifi.setImageResource(R.mipmap.wifi_1);
            } else {
                mTabImageWifi.setImageResource(R.mipmap.top_wifi_off);
            }
        } else {
            mTabImageWifi.setImageResource(R.mipmap.top_wifi_off);
        }
    }

    @Override
    protected void isYesData(boolean isdata) {
        if (isdata && mIsYesData) {        //成功
            mMessageState.setText("正常");
            mMessageState.setTextColor(Color.parseColor("#fd0fc602"));
        } else {             //失败
            mMessageState.setText("断开");
            mMessageState.setTextColor(Color.parseColor("#fdfa0310"));
        }
        mIsYesData = false;
    }

    @Override
    public void onDataReceived(byte[] buffer, int size) {
        mIsYesData = true;
        if (size == Constants.DATA_LONG && buffer[2] == 0x03) {
            analysisData(buffer);
        }
        if (size == Constants.SET_RESULT_LINGTH) {
            setResultData(buffer);
        }
    }


    private BleGattCallback mBleGattCallback = new BleGattCallback() {
        @Override
        public void onStartConnect() {
            //开始连接
        }

        @Override
        public void onConnectFail(BleDevice bleDevice, BleException exception) {
            //连接失败
            if (mStartCheckGas.getVisibility() == View.VISIBLE) {
                mStartCheckGas.setVisibility(View.GONE);
            }
            ToastUtil.showMessage("连接蓝牙失败");
        }

        @Override
        public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
            //连接成功并发现服务。
            List<BluetoothGattService> serviceList = gatt.getServices();
            for (BluetoothGattService service : serviceList) {
                mUuid_service = service.getUuid().toString();
                List<BluetoothGattCharacteristic> characteristicList= service.getCharacteristics();
                for(BluetoothGattCharacteristic characteristic : characteristicList) {
                    mUuid_chara = characteristic.getUuid().toString();
                }
            }
            SystemClock.sleep(100);
            Intent intent = new Intent(CheckGasActivity.this,SendBleDataService.class);
            intent.putExtra("device",mDevice);
            intent.putExtra("serviceUUID",mUuid_service);
            intent.putExtra("charaUUID",mUuid_chara);
            intent.putExtra("equipmentID","00000000");
            startService(intent);
        }

        @Override
        public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
            //连接断开，特指连接后再断开的情况          isActiveDisConnected == true表示主动断开连接
            if (isActiveDisConnected){
                //mLinkState.setText("主动断开连接");
            }else {
                //mLinkState.setText("断开连接");
                //休眠一段时间在进行重连
            }
            Log.d(TAG, "onDisConnected: 断开连接");
        }
    };
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constants.BLE_DATA.equals(action)){
                final byte[] buffer = intent.getByteArrayExtra("data");
                final int size = intent.getIntExtra("size", 0);
                gasData(buffer);
            }
        }
    };


    private void setResultData(byte[] buffer) {
        Boolean isSuccess = DataUtil.analysisSetResult(buffer);
        switch (mCurrentState) {
            case 1:
                if (isSuccess) {
                    mCurrentState = 0;
                    mStartCheckGas.setImageResource(R.mipmap.pause);
                } else {
                    ToastUtil.showMessage("与检测仪连接失败");
                }
                mInputCarNumberDialog.dismiss();
                break;
        }
    }

    private void analysisData(byte[] buffer) {

        String state = DataUtil.getState(buffer);                           //状态位
        //00011000
        String connectServerState = state.substring(3, 4);                  //连接服务器状态
        String afterLockState = state.substring(1, 2);                       //后门锁定状态
        String frontLockState = state.substring(2, 3);                       //前门锁定状态
        String temperatureState = state.substring(0, 1);                       //温度传感器连接状态
        int signalStrength = DataUtil.getSignalStrength(buffer);            //获取信号强度
        if (temperatureState.equals("1")) {
            mTemperatureState.setText("- -");
        } else {
            int temperature = DataUtil.getTemperature(buffer);                  //温度
            mTemperatureState.setText(temperature + "℃");
        }
        //00011001
        if (frontLockState.equals("1")) {
            mTabImageBackState.setImageResource(R.mipmap.icon_on);
        } else {
            mTabImageBackState.setImageResource(R.mipmap.icon_off);
        }
        if (afterLockState.equals("1")) {
            mTabImageFrontState.setImageResource(R.mipmap.icon_on);
        } else {
            mTabImageFrontState.setImageResource(R.mipmap.icon_off);
        }
        //是否连接服务器
        if (connectServerState.equals("0")) {
            mTabImageCommunicationState.setImageResource(R.mipmap.top_icon_4_off);
        } else {
            //设置信号强度
            switch (signalStrength) {
                case 1:
                    mTabImageCommunicationState.setImageResource(R.mipmap.top_icon_1);
                    break;
                case 2:
                    mTabImageCommunicationState.setImageResource(R.mipmap.top_icon_2);
                    break;
                case 3:
                    mTabImageCommunicationState.setImageResource(R.mipmap.top_icon_3);
                    break;
                case 4:
                    mTabImageCommunicationState.setImageResource(R.mipmap.top_icon_4);
                    break;
                default:
                    mTabImageCommunicationState.setImageResource(R.mipmap.top_icon_4_off);
                    break;
            }
        }
    }

    private void gasData(byte[] buffer) {
        if (buffer == null || buffer.length <= 0)
            return;
        String checkGasState = DataBleUtil.getBleCheckGasState(buffer);       //气体检测仪的状态
        String preheatState = checkGasState.substring(0,1);  //预热状态
        String temperatureForCheckState = checkGasState.substring(1,2);  //温湿度传感器状态
        String tvocState = checkGasState.substring(2, 3);  //TVOC传感器状态
        String formaldehydeState = checkGasState.substring(3,4);  //甲醛传感器状态
        String dustState = checkGasState.substring(4, 5);  //PM2.5传感器状态

        if ("1".equals(formaldehydeState)) {
            mSensorStatus.setVisibility(View.VISIBLE);
            mSensorStatus.setText("甲醛传感器故障");
            if ("1".equals(temperatureForCheckState) | "1".equals(dustState) | "1".equals(tvocState)) {
                mSensorStatus.setText("多个传感器故障");
            }
        } else if ("1".equals(temperatureForCheckState)) {
            mSensorStatus.setVisibility(View.VISIBLE);
            mSensorStatus.setText("温湿度传感器故障");
            if ("1".equals(formaldehydeState) | "1".equals(dustState) | "1".equals(tvocState)) {
                mSensorStatus.setText("多个传感器故障");
            }
        } else if ("1".equals(dustState)) {
            mSensorStatus.setVisibility(View.VISIBLE);
            mSensorStatus.setText("PM2.5传感器故障");
            if ("1".equals(formaldehydeState) | "1".equals(temperatureForCheckState) | "1".equals(tvocState)) {
                mSensorStatus.setText("多个传感器故障");
            }
        } else if ("1".equals(tvocState)) {
            mSensorStatus.setVisibility(View.VISIBLE);
            mSensorStatus.setText("TVOC传感器故障");
            if ("1".equals(formaldehydeState) | "1".equals(temperatureForCheckState) | "1".equals(dustState)) {
                mSensorStatus.setText("多个传感器故障");
            }
        } else {
            mSensorStatus.setVisibility(View.GONE);
            mSensorStatus.setText("");
        }

        if ("0".equals(preheatState)) {              //连接成功并且预热完成
            if (mStartCheckGas.getVisibility() == View.VISIBLE) {
                mStartCheckGas.setVisibility(View.GONE);
            }
            if (mProgressBar.getVisibility() == View.VISIBLE)
                mProgressBar.setVisibility(View.GONE);
            String tvoc = DataBleUtil.getTVOC(buffer);
            String formaldehyde = DataBleUtil.getFormaldehyde(buffer);//甲醛
            String pm = DataBleUtil.getPM(buffer);
            String humidity = DataBleUtil.getHumidity(buffer); //湿度
            String temperatureByCheck = DataBleUtil.getTemperatureByCheck(buffer);//温度

            float formaldehydeInt = Integer.parseInt(formaldehyde, 16);       //甲醛
            if (formaldehydeInt < 60) {
                mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors));
            }
            if (formaldehydeInt >= 60 && formaldehydeInt < 100) {
                mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors_blue));
            }
            if (formaldehydeInt >= 100 && formaldehydeInt < 300) {             //轻度污染
                mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors_yellow));
            }
            if (formaldehydeInt >= 300) {                                    //重度污染
                mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors_red));
            }
            //800
            mProgress.setProgress((int) formaldehydeInt);
            mFormaldehydeTv.setText(formaldehydeInt / 1000 + " mg/m³");

            float tvocInt = Integer.parseInt(tvoc, 16);             //tvoc
            if (tvocInt < 60) {
                mProgressTvoc.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors));
            }
            if (tvocInt >= 60 && tvocInt < 100) {
                mProgressTvoc.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors_blue));
            }
            if (tvocInt >= 100 && tvocInt < 160) {
                mProgressTvoc.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors_yellow));
            }
            if (tvocInt >= 160) {
                mProgressTvoc.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors_red));
            }
            mProgressTvoc.setProgress((int) tvocInt);
            mTvocTv.setText(tvocInt / 100 + " mg/m³");

            float pmInt = Integer.parseInt(pm, 16);               //pm
            if (pmInt < 35) {
                mProgressPm.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors));
            }
            if (pmInt >= 35 && pmInt < 75) {
                mProgressPm.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors_blue));
            }
            if (pmInt >= 75 && pmInt < 150) {
                mProgressPm.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors_yellow));
            }
            if (pmInt >= 150) {
                mProgressPm.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors_red));
            }
            //35
            mProgressPm.setProgress((int) pmInt);
            mPmTv.setText(pmInt + " ug/m³");

            float wenInt = Integer.parseInt(temperatureByCheck, 16);        //温度
            if (wenInt < 180) {
                mProgressWen.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors_red));
            }
            if (wenInt > 250 && wenInt <= 320) {
                mProgressWen.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors));
            }
            if (wenInt > 320) {
                mProgressWen.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors_red));
            }

            mProgressWen.setProgress((int) wenInt);
            mWenTv.setText(wenInt / 10 + " C°");

            float shiInt = Integer.parseInt(humidity, 16);//湿度
            if (wenInt >= 50 && wenInt <= 60) {
                mProgressShi.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors));
            } else {
                mProgressShi.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors_red));
            }
            mProgressShi.setProgress((int) shiInt);
            mShiTv.setText(shiInt / 10 + "% RH");
        } else {
            if (mStartCheckGas.getVisibility() == View.GONE) {
                mStartCheckGas.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                SendUtil.controlVoice();
                finish();
                break;
            case R.id.start_check_gas:          //开始建立连接
                if (!mInputCarNumberDialog.isShowing()) {
                    mInputCarNumberDialog.setBitmap(R.mipmap.home_yqlj);
                    mInputCarNumberDialog.show();
                    mInputCarNumberDialog.setOnMakeSureBtnClickListener(new InputCarNumberHinDialog.OnMakeSureBtnClickListener() {
                        @Override
                        public void onBtnClick() {
                            mCurrentState = 1;
                            SendUtil.setConnectForInstrument();
                        }
                    });
                }
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mBleManager.destroy();
        unregisterReceiver(mReceiver);
    }
}
