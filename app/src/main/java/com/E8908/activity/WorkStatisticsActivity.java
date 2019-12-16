package com.E8908.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.base.BaseActivity;
import com.E8908.conf.Constants;
import com.E8908.textFunction.TextFunctionActivity;
import com.E8908.textFunction.TextResultActivity;
import com.E8908.util.DataUtil;
import com.E8908.util.SendUtil;
import com.E8908.widget.JurisdictionDialog;
import com.E8908.widget.LoginDialog;
import com.E8908.widget.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhouzhuo.zzhorizontalprogressbar.ZzHorizontalProgressBar;

/**
 * 工作统计界面
 */
public class WorkStatisticsActivity extends BaseActivity implements View.OnClickListener, JurisdictionDialog.OnCheckJListener {
    private static final String TAG = "WorkStatisticsActivity";
    @Bind(R.id.toobar_bg_image)
    ImageView mToobarBgImage;
    @Bind(R.id.tab_image_wifi)
    ImageView mTabImageWifi;
    @Bind(R.id.pb)
    ZzHorizontalProgressBar mPb;
    @Bind(R.id.pb1)
    ZzHorizontalProgressBar mPb1;
    @Bind(R.id.pb2)
    ZzHorizontalProgressBar mPb2;
    @Bind(R.id.pb3)
    ZzHorizontalProgressBar mPb3;
    @Bind(R.id.pb4)
    ZzHorizontalProgressBar mPb4;
    @Bind(R.id.message_state)
    TextView mMessageState;
    @Bind(R.id.tab_image_back_state)
    ImageView mTabImageBackState;
    @Bind(R.id.tab_image_front_state)
    ImageView mTabImageFrontState;
    @Bind(R.id.tab_image_communication_state)
    ImageView mTabImageCommunicationState;
    @Bind(R.id.bt_back)
    Button mBtBack;
    @Bind(R.id.pd2_tv)
    TextView mPd2Tv;
    @Bind(R.id.pd4_tv)
    TextView mPd4Tv;
    @Bind(R.id.depth_tv)
    TextView mDepthTv;
    @Bind(R.id.routine_tv)
    TextView mRoutineTv;
    @Bind(R.id.add_totel)
    TextView mAddTotel;
    @Bind(R.id.temperature_state)
    TextView mTemperatureState;
    @Bind(R.id.text_function)
    Button mTextFunction;
    @Bind(R.id.text_result)
    Button mTextResult;
    @Bind(R.id.battery_state)
    ImageView mBatteryState;
    private boolean mIsYesData = false;
    private String mEquipmentNumber;
    private JurisdictionDialog mJuDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_statistics);
        ButterKnife.bind(this);
        initView();
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
        String ratioNumbwe = DataUtil.getRatioNumbwe(buffer);               //电子秤系数值
        String riseNumbwe = DataUtil.getRiseNumbwe(buffer);                 //获取液体升数
        String riseTotelNumbwe = DataUtil.getRiseTotelNumbwe(buffer);       //液体加注总升数
        String depthWorkNumbwe = DataUtil.getDepthWorkNumbwe(buffer);       //获取深度保养次数
        String routineWorkNumbwe = DataUtil.getRoutineWorkNumbwe(buffer);   //获取常规保养次数
        //获取序列号
        mEquipmentNumber = DataUtil.getEquipmentNumber(buffer);
        int addNumbwe = DataUtil.getAddNumbwe(buffer);                   //获取加注量
        int signalStrength = DataUtil.getSignalStrength(buffer);            //获取信号强度
        //剩余药液量
        float i = Integer.parseInt(riseNumbwe, 16);
        if (i <= Constants.MAX_NUMBER) {
            mPd2Tv.setText((i / 1000) + "L");
            mPb2.setProgress((int) i);
        } else {
            mPd2Tv.setText((Constants.MAX_NUMBER / 1000) + "L");
            mPb2.setProgress(Constants.MAX_NUMBER);
        }
        //深度保养次数
        int workCountd = Integer.parseInt(depthWorkNumbwe, 16);
        mPb.setProgress(workCountd);
        mDepthTv.setText(workCountd + "次");
        //常规保养次数
        int workCount = Integer.parseInt(routineWorkNumbwe, 16);
        mPb1.setProgress(workCount);
        mRoutineTv.setText(workCount + "次");
        float anInt = Integer.parseInt(riseTotelNumbwe, 16);
        mPb3.setProgress((int) anInt);
        mAddTotel.setText((anInt / 1000) + "L");
        if (workCount <= Constants.MAX_WORK_COUNT) {
            mPd4Tv.setText((workCount + workCountd) + "次");
            mPb4.setProgress(workCount + workCountd);
        } else {
            mPd4Tv.setText("∞次");
            mPb4.setProgress(Constants.MAX_WORK_COUNT);
        }
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


    private void initData() {
        mToobarBgImage.setImageResource(R.mipmap.top_bar_4);
        mJuDialog = new JurisdictionDialog(this,R.style.dialog);
        mJuDialog.setOnCheckJListener(this);
    }

    private void initView() {
        mPb.setMax(Constants.MAX_ONE_DAY_DEPTH_WORK_COUNT);                     //设置一天的深度养护总次数
        mPb1.setMax(Constants.MAX_ONE_DAY_ROUTINE_WORK_COUNT);                    //设置一天的常规养护总次数
        mPb2.setMax(Constants.MAX_NUMBER);                    //剩余药液总升数单位毫升
        mPb3.setMax(Constants.MAX_CHANGE_OIL_COUNT);                    //添加的药液升数单位升(累加)
        mPb4.setMax(Constants.MAX_WORK_COUNT);                    //工作总次数


        mBtBack.setOnClickListener(this);
        mTextFunction.setOnClickListener(this);
        mTextResult.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.text_function:        //功能测试
                if (!TextUtils.isEmpty(mEquipmentNumber)){
                    if ("00000000".equals(mEquipmentNumber)){
                        Intent intent = new Intent(this, TextFunctionActivity.class);
                        startActivity(intent);
                    }else {
                        mJuDialog.setEquipmentId(mEquipmentNumber,2);
                        mJuDialog.show();
                    }
                }
                break;
            case R.id.text_result:          //查看测试结果
                if (!TextUtils.isEmpty(mEquipmentNumber)) {
                    Intent intent = new Intent(this, TextResultActivity.class);
                    intent.putExtra("id",mEquipmentNumber);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onCkcekState(int state, boolean isScurress, String mesg) {
        if (isScurress){
            Intent intent = new Intent(this, TextFunctionActivity.class);
            startActivity(intent);
        }else {
            ToastUtil.showMessage(mesg);
        }
    }
}
