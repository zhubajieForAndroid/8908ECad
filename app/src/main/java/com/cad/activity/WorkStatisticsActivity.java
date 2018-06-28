package com.cad.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cad.R;
import com.cad.base.BaseActivity;
import com.cad.base.MyApplication;
import com.cad.conf.Constants;
import com.cad.util.DataUtil;
import com.cad.util.FileUtil;
import com.cad.util.SendUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhouzhuo.zzhorizontalprogressbar.ZzHorizontalProgressBar;

/**
 * 工作统计界面
 */
public class WorkStatisticsActivity extends BaseActivity implements View.OnClickListener {
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
    @Bind(R.id.work_linearlayout_title)
    LinearLayout mWorkLinearlayoutTitle;
    @Bind(R.id.bt_back)
    ImageButton mBtBack;
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

    private ImageButton bt_back;
    private ImageButton mBtnBack;
    private boolean mIsYesData = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_statistics);
        ButterKnife.bind(this);
        initView();
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

        int addNumbwe = DataUtil.getAddNumbwe(buffer);                   //获取加注量
        int signalStrength = DataUtil.getSignalStrength(buffer);            //获取信号强度
        //剩余药液量
        float i = Integer.parseInt(riseNumbwe, 16);
        if (i <= Constants.MAX_NUMBER) {
            mPd2Tv.setText((i/1000) + "L");
            mPb2.setProgress((int)i);
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
        mPb3.setProgress((int)anInt);
        mAddTotel.setText((anInt/1000) + "L");
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
    }

    private void initView() {
        mPb.setMax(Constants.MAX_ONE_DAY_DEPTH_WORK_COUNT);                     //设置一天的深度养护总次数
        mPb1.setMax(Constants.MAX_ONE_DAY_ROUTINE_WORK_COUNT);                    //设置一天的常规养护总次数
        mPb2.setMax(Constants.MAX_NUMBER);                    //剩余药液总升数单位毫升
        mPb3.setMax(Constants.MAX_CHANGE_OIL_COUNT);                    //添加的药液升数单位升(累加)
        mPb4.setMax(Constants.MAX_WORK_COUNT);                    //工作总次数

        mBtnBack = (ImageButton) findViewById(R.id.bt_back);

        mBtnBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                int[] arr = {0x2a, 0x07, 0x04, 0x01, 0x14, 0x3c, 0x23};
                SendUtil.sendMessage(arr, MyApplication.getOutputStream());
                finish();
                break;
        }
    }

}
