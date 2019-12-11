package com.E8908.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.base.BaseActivity;
import com.E8908.conf.Constants;
import com.E8908.util.DataUtil;
import com.E8908.util.SendUtil;
import com.E8908.widget.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FillingTwoActivity extends BaseActivity implements View.OnTouchListener {

    private static final String TAG = "FillingActivity";
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
    @Bind(R.id.add_bg)
    ImageView mAddBg;
    @Bind(R.id.ratio_numbwe)
    TextView mRatioNumbwe;
    @Bind(R.id.temperature_state)
    TextView mTemperatureState;
    @Bind(R.id.battery_state)
    ImageView mBatteryState;
    private boolean mIsYesData = false;
    private int currentState;
    private int mAnInt;
    private boolean isClear = false;//电子秤是否清零
    private int mResultRatioNumbwe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filling_two);
        ButterKnife.bind(this);
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
    }

    /**
     * 设置结果
     *
     * @param resultData
     */
    public void setResultData(byte[] resultData) {
        Boolean isSuccess = DataUtil.analysisSetResult(resultData);
        switch (currentState) {
            case 1:
                if (!isSuccess){
                    currentState = 1;
                    //一进来就设置加注液体为工作状态
                    SendUtil.setWorkState(128);
                }else {
                    currentState = 0;
                }
                break;
            case 2:
                if (isSuccess){
                    currentState = 0;
                    SendUtil.controlVoice();
                    finish();
                }else {
                    currentState = 2;
                    //返回清除加注液体状态
                    SendUtil.setWorkState(0);
                }
                break;
            case 3:
                if (isSuccess) {
                    currentState = 0;
                    isClear = true;
                    ToastUtil.showMessage("清零成功");
                } else {
                    ToastUtil.showMessage("清零失败");
                }
                break;
        }

    }

    /**
     * 数据是否发送成功
     *
     * @param isdata
     */
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

    private void analysisData(byte[] buffer) {
        String state = DataUtil.getState(buffer);                           //状态位
        String adNumbwe = DataUtil.getADNumbwe(buffer);                     //电子秤AD值

        mAnInt = Integer.parseInt(adNumbwe, 16);
        String riseNumbwe = DataUtil.getRiseNumbwe(buffer);                 //获取液体升数
        mResultRatioNumbwe = Integer.parseInt(riseNumbwe, 16);
        if (isClear) {           //已经电子秤清零了
            mRatioNumbwe.setText(mResultRatioNumbwe + "");
        } else {
            mRatioNumbwe.setText(mAnInt + "");
        }

        int signalStrength = DataUtil.getSignalStrength(buffer);            //获取信号强度
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

    private void initData() {
        mAddBg.setImageResource(R.mipmap.bg_3_2);
        mToobarBgImage.setImageResource(R.mipmap.top_bar_3_2);
        mAddBg.setOnTouchListener(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTIVITY_STATE);
        registerReceiver(receiver, intentFilter);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if ((x >= 280 && x <= 947) && (y >= 655 && y <= 798)) {
           currentState = 4;
            SendUtil.controlVoice();
            Intent intent = new Intent(this, FillingThreeActivityDemo24.class);
            startActivity(intent);
        } else if ((x >= 990 && x <= 1255) && (y >= 660 && y <= 798)) {
            currentState = 2;
            //返回清除加注液体状态
            SendUtil.setWorkState(0);
        } else if ((x >= 853 && x <= 1125) && (y >= 515 && y <= 609)) {       //电子秤清零
            currentState = 3;
            SendUtil.clear(mAnInt);
        }
        return false;
    }
    @Override
    protected void onStart() {
        super.onStart();
        currentState = 1;
        //一进来就设置加注液体为工作状态
        SendUtil.setWorkState(128);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);

    }
}
