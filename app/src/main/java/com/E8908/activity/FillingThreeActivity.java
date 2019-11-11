package com.E8908.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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
import com.E8908.widget.AddErrorDialog;
import com.E8908.widget.BitmapUtil;
import com.E8908.widget.ToastUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FillingThreeActivity extends BaseActivity implements View.OnTouchListener, View.OnClickListener {

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
    @Bind(R.id.perform_state)
    ImageView mPerformState;
    @Bind(R.id.start_btn)
    ImageView mStartBtn;
    @Bind(R.id.minute_two)
    ImageView mMinuteTwo;
    @Bind(R.id.minute_one)
    ImageView mMinuteOne;
    @Bind(R.id.second_two)
    ImageView mSecondTwo;
    @Bind(R.id.second_one)
    ImageView mSecondOne;
    @Bind(R.id.temperature_state)
    TextView mTemperatureState;
    @Bind(R.id.plus_number)
    ImageView mPlusNumber;
    @Bind(R.id.minus_number)
    ImageView mMinusNumber;
    private int second = 0;     //秒
    private int minute = 1;     //分钟
    private boolean mIsYesData = false;
    private boolean beforLock = false;//前门锁打开状态
    private boolean afterLock = false;//前门锁打开状态
    private boolean isClick = true;//是否点击开始了
    private Timer timer;
    private int currentState;
    private boolean isSetTotalAddNumber = true;//是否设置加注重量
    private int mResultRatioNumbwe;
    private SharedPreferences mSp;
    private String mRiseNumbwe;
    private AddErrorDialog mErrorDialog;
    private TimeTask mTimeTask;
    private CheckTimeTask mCheckTimeTask;
    private Timer mTimer;
    private int mResultNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filling_three);
        mSp = getSharedPreferences("stir", 0);
        mErrorDialog = new AddErrorDialog(this, R.style.dialog, R.mipmap.popovers_failed_02);
        ButterKnife.bind(this);
        initData();
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

    public void setResultData(byte[] resultData) {
        Boolean isSuccess = DataUtil.analysisSetResult(resultData);
        switch (currentState) {
            case 3:
                if (isSuccess) {
                    Intent intent = new Intent();
                    intent.setAction(Constants.ACTIVITY_STATE);
                    sendBroadcast(intent);
                    finish();
                }
                break;
            case 4:
                if (isSuccess) {
                    mStartBtn.setImageResource(R.mipmap.btn_s_3);
                    mStartBtn.setClickable(false);
                    isClick = false;
                    startTime();
                    mPerformState.setImageResource(R.mipmap.underway);
                    //开始搅拌成功之后,设置正在搅拌
                    currentState = 12;
                    SendUtil.setWorkState(130);
                    SharedPreferences.Editor edit = mSp.edit();
                    edit.putString("stirNumber", mRiseNumbwe);
                    edit.commit();
                } else {
                    currentState = 4;
                    //打开2#触点
                    int[] arr = {0x2a, 0x06, 0x01, 0x02, 0x2f, 0x23};
                    SendUtil.sendMessage(arr, MyApplication.getOutputStream());
                }
                break;
            case 5:
                if (isSuccess) {
                    stopCheckTimeTask();
                    currentState = 13;
                    mPerformState.setImageResource(R.mipmap.accomplish);
                    SendUtil.setWorkState(0);
                } else {
                    currentState = 5;
                    int[] arr = {0x2a, 0x06, 0x01, 0x00, 0x2d, 0x23};
                    SendUtil.sendMessage(arr, MyApplication.getOutputStream());
                }
                break;
            case 8:
                if (isSuccess) {
                    currentState = 0;
                } else {
                    currentState = 8;
                    //关闭2#触点,开启4#
                    //int[] arr = {0x2a, 0x06, 0x01, 0x08, 0x25, 0x23};
                    //SendUtils.sendMessage(arr, MyApplication.getOutputStream());
                    SendUtil.open4();
                }
                break;
            case 9:
                if (isSuccess) {
                    currentState = 0;
                } else {
                    currentState = 9;
                    //打开2#触点,关闭4#
                    int[] arr = {0x2a, 0x06, 0x01, 0x02, 0x2f, 0x23};
                    SendUtil.sendMessage(arr, MyApplication.getOutputStream());
                }
                break;
            case 11:            //加注出现错误
                if (isSuccess) {
                    stopCheckTimeTask();
                    stopTime();
                    currentState = 14;
                    SendUtil.setWorkState(8);
                    if (!mErrorDialog.isShowing()) {
                        mErrorDialog.show();
                        mErrorDialog.setMakeSuperListener(new AddErrorDialog.MakeSuperListener() {
                            @Override
                            public void isMakeSupter(boolean supter) {
                                if (supter) {
                                    Intent intent = new Intent();
                                    intent.setAction(Constants.ACTIVITY_STATE);
                                    sendBroadcast(intent);
                                    mErrorDialog.dismiss();
                                    finish();
                                }
                            }
                        });
                    } else {
                        currentState = 11;
                        SendUtil.closeAll();
                    }
                }
                break;
            case 12:
                if (isSuccess) {
                    currentState = 0;
                    startCheckTimeTask();
                } else {
                    //开始搅拌成功之后,设置正在搅拌
                    currentState = 12;
                    SendUtil.setWorkState(128);
                }
                break;
            case 13:
                if (isSuccess) {
                    currentState = 0;
                } else {
                    currentState = 13;
                    SendUtil.setWorkState(0);
                }
                break;
            case 14:
                if (isSuccess) {
                    currentState = 0;
                } else {
                    currentState = 14;
                    SendUtil.setWorkState(8);
                }
                break;
            case 15:
                if (isSuccess) {
                    currentState = 0;
                } else {
                    currentState = 15;
                    //设置换油总升数
                    int result = mResultNumber + mResultRatioNumbwe;
                    SendUtil.setChangeOilNumber(result);
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
        String state = DataUtil.getState(buffer);                           //状态位
        int signalStrength = DataUtil.getSignalStrength(buffer);            //获取信号强度
        String connectServerState = state.substring(3, 4);                  //连接服务器状态
        String activationState = state.substring(4, 5);                     //设备激活状态
        String lockState = state.substring(5, 6);                           //设备锁定状态
        String riseTotelNumbwe = DataUtil.getRiseTotelNumbwe(buffer);       //液体加注总升数
        //获取液体升数
        mRiseNumbwe = DataUtil.getRiseNumbwe(buffer);

        mResultRatioNumbwe = Integer.parseInt(mRiseNumbwe, 16);
        //00011000
        String afterLockState = state.substring(1, 2);                       //后门锁定状态
        String frontLockState = state.substring(2, 3);                       //前门锁定状态
        String temperatureState = state.substring(0, 1);                       //温度传感器连接状态
        mResultNumber = Integer.parseInt(riseTotelNumbwe, 16);
        if (isSetTotalAddNumber) {
            currentState = 15;
            isSetTotalAddNumber = false;
            //设置换油总升数
            int result = mResultNumber + mResultRatioNumbwe;
            SendUtil.setChangeOilNumber(result);
        }
        if (temperatureState.equals("1")) {
            mTemperatureState.setText("- -");
        } else {
            int temperature = DataUtil.getTemperature(buffer);                  //温度
            mTemperatureState.setText(temperature + "℃");
        }
        if (frontLockState.equals("1")) {
            beforLock = true;
            mTabImageBackState.setImageResource(R.mipmap.icon_on);
        } else {
            beforLock = false;
            mTabImageBackState.setImageResource(R.mipmap.icon_off);
        }
        if (afterLockState.equals("1")) {
            afterLock = true;
            mTabImageFrontState.setImageResource(R.mipmap.icon_on);
        } else {
            afterLock = false;
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
        BitmapUtil.numberToBItmapTwo(minute, mMinuteTwo, mMinuteOne);
        mToobarBgImage.setImageResource(R.mipmap.top_bar_3_3);
        mAddBg.setOnTouchListener(this);
        mStartBtn.setOnClickListener(this);
        mPlusNumber.setOnClickListener(this);
        mMinusNumber.setOnClickListener(this);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if ((x >= 273 && x <= 947) && (y >= 713 && y <= 775)) {
            if (isClick) {
                currentState = 1;
                SendUtil.controlVoice();
                finish();
            } else {
                ToastUtil.showMessage("等待搅拌完成");
            }
        } else if ((x >= 987 && x <= 1248) && (y >= 713 && y <= 775)) {
            if (isClick) {
                currentState = 3;
                //返回清除加注液体状态
                SendUtil.setWorkState(0);
            } else {
                ToastUtil.showMessage("等待搅拌完成");
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_btn:
                if (!beforLock && !afterLock) {
                    if (mResultRatioNumbwe > 1000) {
                        if (isClick) {
                            currentState = 4;
                            //打开2#触点
                            int[] arr = {0x2a, 0x06, 0x01, 0x02, 0x2f, 0x23};
                            SendUtil.sendMessage(arr, MyApplication.getOutputStream());
                        }
                    } else {
                        ToastUtil.showMessage("药液不足一升,无法搅拌");
                    }
                } else {
                    ToastUtil.showMessage("请先关好门");
                }
                break;
            case R.id.plus_number:
                if (minute < 10) {
                    minute++;
                    BitmapUtil.numberToBItmapTwo(minute, mMinuteTwo, mMinuteOne);
                }
                break;
            case R.id.minus_number:
                if (minute > 1) {
                    minute--;
                    BitmapUtil.numberToBItmapTwo(minute, mMinuteTwo, mMinuteOne);
                }
                break;
        }

    }

    /*--------------------时间任务开始---------------------*/
    private class TimeTask extends TimerTask {
        @Override
        public void run() {
            if (second > 0) {
                second--;
                if (second == 40) {//加注20秒完成,开始回收40秒
                    currentState = 8;
                    SendUtil.open4();
                }
            } else {
                if (minute == 0 && second == 0) {
                    isClick = true;
                    //时间执行完成全部关闭
                    timer.cancel();
                    currentState = 5;
                    int[] arr = {0x2a, 0x06, 0x01, 0x00, 0x2d, 0x23};
                    SendUtil.sendMessage(arr, MyApplication.getOutputStream());
                } else {
                    second = 59;
                }
                if (minute > 0)
                    minute--;
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BitmapUtil.numberToBItmapTwo(second, mSecondTwo, mSecondOne);
                    BitmapUtil.numberToBItmapTwo(minute, mMinuteTwo, mMinuteOne);
                }
            });

        }
    }

    private void startTime() {
        if (timer == null)
            timer = new Timer();
        if (mTimeTask == null) {
            mTimeTask = new TimeTask();
            timer.schedule(mTimeTask, 0, 1000);
        }
    }

    private void stopTime() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (mTimeTask != null) {
            mTimeTask.cancel();
            mTimeTask = null;
        }
    }
    /*--------------------时间任务结束---------------------*/

    /*--------------------加注的时候检查液体的任务开始---------------------*/

    /**
     * 定时检查电子秤的值是否有变化的任务
     */
    private class CheckTimeTask extends TimerTask {
        @Override
        public void run() {
            String surplusNumber = mSp.getString("stirNumber", "");
            final float parseInt = Integer.parseInt(surplusNumber, 16);
            final float currentNumber = Integer.parseInt(mRiseNumbwe, 16);
            if (Math.abs((parseInt - currentNumber)) <= 50) {
                currentState = 11;
                SendUtil.closeAll();
            } else {
                SharedPreferences.Editor edit = mSp.edit();
                edit.putString("stirNumber", mRiseNumbwe);
                edit.commit();
            }

        }
    }

    /**
     * 开始检查电子秤的值是否有变化
     */
    private void startCheckTimeTask() {
        if (mTimer == null)
            mTimer = new Timer();
        if (mCheckTimeTask == null) {
            mCheckTimeTask = new CheckTimeTask();
            mTimer.scheduleAtFixedRate(mCheckTimeTask, 10000, 10000);
        }
    }

    /**
     * 停止检查电子秤的值是否有变化
     */
    private void stopCheckTimeTask() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mCheckTimeTask != null) {
            mCheckTimeTask.cancel();
            mCheckTimeTask = null;
        }
    }

    /*--------------------加注的时候检查液体的任务结束---------------------*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTime();
        stopCheckTimeTask();
        //返回清除加注液体状态
        SendUtil.setWorkState(0);
    }
}
