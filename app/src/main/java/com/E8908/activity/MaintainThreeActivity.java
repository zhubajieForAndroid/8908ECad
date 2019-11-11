package com.E8908.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.base.BaseActivity;
import com.E8908.base.MyApplication;
import com.E8908.conf.Constants;
import com.E8908.util.DataUtil;
import com.E8908.util.SendUtil;
import com.E8908.util.SharedPreferencesUtils;
import com.E8908.widget.OpenDialog;
import com.E8908.widget.ToastUtil;
import com.clj.fastble.data.BleDevice;

import java.util.Arrays;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MaintainThreeActivity extends BaseActivity implements View.OnTouchListener, OpenDialog.OnOpenListener, OpenDialog.OnCancelButtonListener {

    private static final String TAG = "MaintainThreeActivity";
    @Bind(R.id.toobar_bg_image)
    ImageView mToobarBgImage;
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
    @Bind(R.id.bg_touch)
    ImageView mBgTouch;
    @Bind(R.id.temperature_state)
    TextView mTemperatureState;
    private boolean mIsRoutine;
    private boolean mIsYesData = false;
    private String mEquipmentNumber;
    private OpenDialog mOpenDialog;
    private int mCurrentState;
    private boolean isByReady;    //是否准备就绪
    private boolean isStartActivity = true;
    private HashMap<String, String> mPames;
    private String mPk;
    private String mShopName;
    private String mBleDeviceMac;
    private String mEquipmentID;
    private String mCarNumber;
    private boolean isQueryVersion = true;          //是否查询版本信息
    private int mVersionState = 1;              //默认是4S配置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintain_one);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mIsRoutine = intent.getBooleanExtra("isRoutine", false);
        mPk = intent.getStringExtra("pk");
        mShopName = intent.getStringExtra("shopName");
        mBleDeviceMac = intent.getStringExtra("BleDeviceMac");
        mEquipmentID = intent.getStringExtra("equipmentID");
        mCarNumber = intent.getStringExtra("carNumber");
        mOpenDialog = new OpenDialog(this, R.style.dialog);
        mOpenDialog.setOnOpenListener(this);
        mOpenDialog.setOnCancelButtonListener(this);
        SharedPreferences bleUpdataPkSp = SharedPreferencesUtils.getBleUpdataPkSp();
        SharedPreferences.Editor edit = bleUpdataPkSp.edit();
        if (!TextUtils.isEmpty(mPk)){
            edit.putString("upPk",mPk);
        }else {
            edit.putString("upPk","");
        }
        edit.apply();

        initData();
    }


    private void initData() {
        mBgTouch.setOnTouchListener(this);
        mToobarBgImage.setImageResource(R.mipmap.bg_cg_top_4);
        mBgTouch.setImageResource(R.mipmap.bg_cg_3);
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
    public void onDataReceived(final byte[] buffer, int size) {
        mIsYesData = true;
        if (size == Constants.DATA_LONG && buffer[2] == 0x03) {
            analysisData(buffer);
        }
        if (size == Constants.SET_RESULT_LINGTH) {
            setResultData(buffer);
        }
        //6   [42, 6, 35, 0, 15, 35]
        if (size == 6) {             //返回的版本信息数据
            int versionInfo = buffer[3];
            switch (versionInfo) {
                case 0:                 //修理厂的版本
                    mVersionState = 0;
                    break;
                case 1:                 //4s点的配置
                    mVersionState = 1;
                    break;
            }
        }
    }

    private void setResultData(byte[] buffer) {
        Boolean isSuccess = DataUtil.analysisSetResult(buffer);
        if (mCurrentState == 1) {
            if (isSuccess) {
                mCurrentState = 0;
                isByReady = true;
            } else {
                mCurrentState = 1;
                SendUtil.setReadyState(mEquipmentNumber, 1);
            }
        }
        if (mCurrentState == 2) {
            if (isSuccess) {
                mCurrentState = 0;
                isByReady = false;
                finish();
            } else {
                mCurrentState = 2;
                SendUtil.setReadyState(mEquipmentNumber, 0);
            }
        }
        if (mCurrentState == 3) {
            if (isSuccess) {
                mCurrentState = 0;
                isByReady = false;
            } else {
                mCurrentState = 3;
                SendUtil.setReadyState(mEquipmentNumber, 0);
            }
        }

    }

    /**
     * 数据是否发送成功
     *
     * @param isdata
     */
    @Override
    protected void isYesData(boolean isdata,boolean isCharging) {
        if (isdata && mIsYesData) {        //成功
            if (isCharging){
                mMessageState.setText("正常");
                mMessageState.setTextColor(Color.parseColor("#fd0fc602"));
            }else {
                mMessageState.setText("正常");
                mMessageState.setTextColor(Color.parseColor("#fdfa0310"));
            }
        } else {             //失败
            mMessageState.setText("断开");
            mMessageState.setTextColor(Color.parseColor("#fdfa0310"));
        }
        mIsYesData = false;
    }

    private void analysisData(byte[] buffer) {
        //查询版本信息
        if (isQueryVersion) {
            isQueryVersion = false;
            mCurrentState = 4;
            SendUtil.queryVersionInfo();
        }
        String state = DataUtil.getState(buffer);                           //状态位
        String checkGasState = DataUtil.getCheckGasState(buffer);       //气体检测仪的状态
        String startState = checkGasState.substring(2, 3);              //设备自启动状态
        if ("1".equals(startState) && isByReady && isStartActivity && mVersionState == 0) {      //mVersionState是修理厂的版本才有自启动状态
            isStartActivity = false;
            Intent brd = new Intent();
            brd.setAction(Constants.ACTIVITY_STATE);
            sendBroadcast(brd);
            if (mOpenDialog != null && mOpenDialog.isShowing()) {
                mOpenDialog.dismiss();
            }
            SystemClock.sleep(500);
            Intent intent = new Intent(this, ConventionalMaintenanceActivityDemo22.class);
            intent.putExtra("isRoutine", mIsRoutine);
            intent.putExtra("isStart", true);
            intent.putExtra("pk", mPk);
            intent.putExtra("shopName", mShopName);
            intent.putExtra("BleDeviceMac", mBleDeviceMac);
            intent.putExtra("carNumber", mCarNumber);
            intent.putExtra("equipmentID", mEquipmentID);
            startActivity(intent);
            finish();
        }


        int signalStrength = DataUtil.getSignalStrength(buffer);            //获取信号强度

        mEquipmentNumber = DataUtil.getEquipmentNumber(buffer);
        String connectServerState = state.substring(3, 4);                  //连接服务器状态
        String activationState = state.substring(4, 5);                     //设备激活状态
        String lockState = state.substring(5, 6);                           //设备锁定状态
        //00011000
        String afterLockState = state.substring(1, 2);                       //后门锁定状态
        String frontLockState = state.substring(2, 3);                       //前门锁定状态
        String temperatureState = state.substring(0, 1);                       //温度传感器连接状态
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if ((x >= 25 && x <= 290) && (y >= 660 && y <= 795)) {                //返回
            mCurrentState = 2;
            SendUtil.setReadyState(mEquipmentNumber, 0);
        } else if ((x >= 540 && x <= 794) && (y >= 709 && y <= 785)) {         //下一步
            if (!TextUtils.isEmpty(mEquipmentNumber)) {
                if (mVersionState == 0) {       //修理厂的版本
                    if (!mOpenDialog.isShowing()) {
                        mOpenDialog.setinit(mEquipmentNumber);
                        mOpenDialog.show();
                        //设置设备已经就绪
                        mCurrentState = 1;
                        SendUtil.setReadyState(mEquipmentNumber, 1);
                    }
                } else if (mVersionState == 1){                         //4S点的版本
                    Intent brd = new Intent();
                    brd.setAction(Constants.ACTIVITY_STATE);
                    sendBroadcast(brd);
                    Intent intent = new Intent(this, ConventionalMaintenanceActivityDemo22.class);
                    intent.putExtra("isRoutine", mIsRoutine);
                    intent.putExtra("isStart", false);
                    intent.putExtra("pk", mPk);
                    intent.putExtra("shopName", mShopName);
                    intent.putExtra("BleDeviceMac", mBleDeviceMac);
                    intent.putExtra("carNumber", mCarNumber);
                    intent.putExtra("equipmentID", mEquipmentID);
                    startActivity(intent);
                    finish();
                }
            }

        }
        return false;
    }

    @Override
    public void startOpen(String state) {           //校验成功
        if ("9".equals(state)) {
            SystemClock.sleep(500);
            Intent brd = new Intent();
            brd.setAction(Constants.ACTIVITY_STATE);
            sendBroadcast(brd);
            //mOpenDialog.dismiss();
            Intent intent = new Intent(this, ConventionalMaintenanceActivityDemo22.class);
            intent.putExtra("isRoutine", mIsRoutine);
            intent.putExtra("isStart", true);
            intent.putExtra("pk", mPk);
            intent.putExtra("shopName", mShopName);
            intent.putExtra("BleDeviceMac", mBleDeviceMac);
            intent.putExtra("carNumber", mCarNumber);
            intent.putExtra("equipmentID", mEquipmentID);
            startActivity(intent);
            finish();
        } else {
            SystemClock.sleep(500);
            Intent brd = new Intent();
            brd.setAction(Constants.ACTIVITY_STATE);
            sendBroadcast(brd);
            mOpenDialog.dismiss();
            Intent intent = new Intent(this, ConventionalMaintenanceActivityDemo22.class);
            intent.putExtra("isRoutine", mIsRoutine);
            intent.putExtra("isStart", false);
            intent.putExtra("pk", mPk);
            intent.putExtra("shopName", mShopName);
            intent.putExtra("BleDeviceMac", mBleDeviceMac);
            intent.putExtra("carNumber", mCarNumber);
            intent.putExtra("equipmentID", mEquipmentID);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void errorMsg(final String msg) {          //校验失败
        mCurrentState = 3;
        SendUtil.setReadyState(mEquipmentNumber, 0);
        MyApplication.getmHandler().post(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showMessage(msg);
            }
        });
    }


    @Override
    protected void onDestroy() {
        mCurrentState = 2;
        SendUtil.setReadyState(mEquipmentNumber, 0);
        super.onDestroy();
    }

    @Override
    public void onCancelClick() {
        mCurrentState = 3;
        SendUtil.setReadyState(mEquipmentNumber, 0);
    }
}
