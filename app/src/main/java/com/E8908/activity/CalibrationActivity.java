package com.E8908.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.base.BaseActivity;
import com.E8908.base.MyApplication;
import com.E8908.conf.Constants;
import com.E8908.util.DataUtil;
import com.E8908.util.SendUtil;
import com.E8908.widget.SetXishuDialog;
import com.E8908.widget.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CalibrationActivity extends BaseActivity implements View.OnClickListener, SetXishuDialog.OnSendOpenListener {

    private static final String TAG = "CalibrationActivity";
    @Bind(R.id.toobar_bg_image)
    ImageView mToobarBgImage;
    @Bind(R.id.tab_image_back_state)
    ImageView mTabImageBackState;
    @Bind(R.id.tab_image_front_state)
    ImageView mTabImageFrontState;
    @Bind(R.id.tab_image_communication_state)
    ImageView mTabImageCommunicationState;
    @Bind(R.id.tab_image_wifi)
    ImageView mTabImageWifi;
    @Bind(R.id.activity_calibration)
    RelativeLayout mActivityCalibration;
    @Bind(R.id.back_btn)
    ImageView mBackBtn;
    @Bind(R.id.step_one_data)
    TextView mStepOneData;
    @Bind(R.id.step_one_btn)
    ImageView mStepOneBtn;
    @Bind(R.id.step_two_data)
    TextView mStepTwoData;
    @Bind(R.id.step_two_btn)
    ImageView mStepTwoBtn;
    @Bind(R.id.step_three_data_result)
    TextView mStepThreeDataResult;
    @Bind(R.id.step_three_btn_result)
    ImageView mStepThreeBtnResult;
    @Bind(R.id.container)
    LinearLayout mContainer;
    @Bind(R.id.message_state)
    TextView mMessageState;
    @Bind(R.id.temperature_state)
    TextView mTemperatureState;
    @Bind(R.id.set_btn)
    Button mSetBtn;
    private boolean mIsYesData = false;
    private int mParseInt;      //电子秤AD值
    private boolean isOneKeep = false;//第一步是否保存
    private boolean isTwoKeep = false;//第二步是否保存
    private float mAddint;
    private float mResult;
    private float mAdInt;
    private boolean isComplete = false; //是否完成标定

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);
        ButterKnife.bind(this);
        initListener();
        mStepThreeBtnResult.setClickable(false);
        mStepTwoBtn.setClickable(false);

        SendUtil.setWorkState(4);
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
            if (isComplete)
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
        isComplete = false;
        if (isSuccess) {
            mStepOneBtn.setClickable(true);
            mStepOneBtn.setImageResource(R.mipmap.btn_save_s);
            isOneKeep = false;
            isTwoKeep = false;
            mStepTwoData.setText("");
            mStepThreeBtnResult.setImageResource(R.mipmap.btn_over_normal);
            ToastUtil.showMessage("设置成功");
        } else {
            ToastUtil.showMessage("设置失败");
        }
    }

    private void analysisData(byte[] buffer) {
        String state = DataUtil.getState(buffer);                           //状态位
        String adNumbwe = DataUtil.getADNumbwe(buffer);                     //电子秤AD值
        if (!TextUtils.isEmpty(adNumbwe) && !isOneKeep) {
            mParseInt = Integer.parseInt(adNumbwe, 16);
            mStepOneData.setText(mParseInt + "");
        }
        if (!TextUtils.isEmpty(adNumbwe) && !isTwoKeep && isOneKeep) {
            mParseInt = Integer.parseInt(adNumbwe, 16);
            mStepTwoData.setText(mParseInt + "");
        }
        int signalStrength = DataUtil.getSignalStrength(buffer);            //获取信号强度

        //设置状态00000011
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
        if (connectServerState.equals("1")) {
            //TODO 已经连接服务器
        } else {
            //TODO 未连接
        }
        if (activationState.equals("1")) {
            //TODO 已经激活
        } else {
            //TODO 未激活
        }
        if (lockState.equals("1")) {
            //TODO 已经锁定
        } else {
            //TODO 未锁定
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

    private void initListener() {
        mToobarBgImage.setImageResource(R.mipmap.top_bar_6_2);
        mBackBtn.setOnClickListener(this);
        mStepOneBtn.setOnClickListener(this);
        mStepTwoBtn.setOnClickListener(this);
        mStepThreeBtnResult.setOnClickListener(this);
        mSetBtn.setOnClickListener(this);
        mContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return false;
            }
        });
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                SendUtil.setWorkState(0);
                finish();
                break;
            case R.id.step_one_btn:             //保存电子秤AD值
                isOneKeep = true;
                SharedPreferences sp = getSharedPreferences("ad", 0);
                SharedPreferences.Editor edit = sp.edit();
                edit.putInt("adInt", mParseInt);
                edit.commit();
                mStepOneBtn.setImageResource(R.mipmap.btn_save_normal);
                mStepTwoBtn.setImageResource(R.mipmap.btn_save_s);
                mStepOneData.setText(sp.getInt("adInt", 0) + "");
                mStepOneBtn.setClickable(false);
                mStepTwoBtn.setClickable(true);
                mStepThreeDataResult.setText("");
                break;
            case R.id.step_two_btn:             //保存加入10升的Ad值
                SharedPreferences spadd = getSharedPreferences("ad", 0);
                int adInt = spadd.getInt("adInt", 0);
                if (mParseInt <= adInt) {
                    ToastUtil.showMessage("保存数值不正确");
                } else {
                    isTwoKeep = true;
                    SharedPreferences.Editor editadd = spadd.edit();
                    editadd.putInt("addint", mParseInt);
                    editadd.commit();
                    mAddint = spadd.getInt("addint", 0);
                    mAdInt = spadd.getInt("adInt", 0);

                    mStepOneBtn.setImageResource(R.mipmap.btn_save_normal);
                    mStepTwoBtn.setImageResource(R.mipmap.btn_save_normal);
                    mStepThreeBtnResult.setImageResource(R.mipmap.btn_over_s);
                    mStepTwoData.setText(spadd.getInt("addint", 0) + "");
                    mStepTwoBtn.setClickable(false);
                    mStepThreeBtnResult.setClickable(true);
                    mResult = 10000 / (mAddint - mAdInt);
                    mResult *= 10000;
                    mStepThreeDataResult.setText((int) mResult + "");
                }
                break;
            case R.id.step_three_btn_result:            //保存系数
                isComplete = true;
                int xishuGao = ((int) mResult & 0x0000FF00) >> 8;
                int xishuDi = (int) mResult & 0x000000FF;
                int kongGao = ((int) mAdInt & 0x0000FF00) >> 8;
                int kongDi = (int) mAdInt & 0x000000FF;
                int xorResult = 0x2a ^ 0x09 ^ 0x05 ^ xishuGao ^ xishuDi ^ kongGao ^ kongDi;
                int[] three = {0x2a, 0x09, 0x05, xishuGao, xishuDi, kongGao, kongDi, xorResult, 0x23};
                SendUtil.sendMessage(three, MyApplication.getOutputStream());
                break;
            case R.id.set_btn:                          //设置系数
                SetXishuDialog dialog = new SetXishuDialog(this,R.style.dialog);
                dialog.setOnSendOpenListener(this);
                dialog.show();
                break;
        }
    }

    @Override
    public void onOpen(int state) {
        mResult = state;
        isComplete = true;
        int xishuGao = ((int) mResult & 0x0000FF00) >> 8;
        int xishuDi = (int) mResult & 0x000000FF;
        int kongGao = ((int) mAdInt & 0x0000FF00) >> 8;
        int kongDi = (int) mAdInt & 0x000000FF;
        int xorResult = 0x2a ^ 0x09 ^ 0x05 ^ xishuGao ^ xishuDi ^ kongGao ^ kongDi;
        int[] three = {0x2a, 0x09, 0x05, xishuGao, xishuDi, kongGao, kongDi, xorResult, 0x23};
        SendUtil.sendMessage(three, MyApplication.getOutputStream());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SendUtil.setWorkState(0);
    }
}
