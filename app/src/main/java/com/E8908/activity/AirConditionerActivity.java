package com.E8908.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.base.BaseActivity;
import com.E8908.bean.RowsBean;
import com.E8908.conf.Constants;
import com.E8908.util.BItmapUtil;
import com.E8908.util.DataUtil;
import com.E8908.util.SendUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AirConditionerActivity extends BaseActivity implements View.OnClickListener {

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
    @Bind(R.id.lv_xin)
    RelativeLayout mLvXin;
    @Bind(R.id.back_btn)
    ImageView mBackBtn;
    @Bind(R.id.lv_xin_after)
    RelativeLayout mLvXinAfter;
    @Bind(R.id.zheng_faxiang_before)
    RelativeLayout mZhengFaxiangBefore;
    @Bind(R.id.zheng_faxiang_after)
    RelativeLayout mZhengFaxiangAfter;
    @Bind(R.id.gufengxiang_after)
    RelativeLayout mGufengxiangAfter;
    @Bind(R.id.gufengxiang_before)
    RelativeLayout mGufengxiangBefore;
    @Bind(R.id.tongfeng_after)
    RelativeLayout mTongfengAfter;
    @Bind(R.id.tongfeng_before)
    RelativeLayout mTongfengBefore;
    @Bind(R.id.lengniuqi_after)
    RelativeLayout mLengniuqiAfter;
    @Bind(R.id.lengniuqi_before)
    RelativeLayout mLengniuqiBefore;
    @Bind(R.id.zhieng_before)
    RelativeLayout mZhiengBefore;
    @Bind(R.id.zhieng_after)
    RelativeLayout mZhiengAfter;
    @Bind(R.id.yixiang_before)
    RelativeLayout mYixiangBefore;
    @Bind(R.id.yixiang_after)
    RelativeLayout mYixiangAfter;
    @Bind(R.id.before_ktlxbmwg_iv)
    ImageView mBeforeKtlxbmwgIv;
    @Bind(R.id.before_ktlxbmwg_tv)
    TextView mBeforeKtlxbmwgTv;
    @Bind(R.id.after_ktlxbmwg_iv)
    ImageView mAfterKtlxbmwgIv;
    @Bind(R.id.after_ktlxbmwg_tv)
    TextView mAfterKtlxbmwgTv;
    @Bind(R.id.before_zfx_iv)
    ImageView mBeforeZfxIv;
    @Bind(R.id.before_zfx_tv)
    TextView mBeforeZfxTv;
    @Bind(R.id.after_zfx_iv)
    ImageView mAfterZfxIv;
    @Bind(R.id.after_zfx_tv)
    TextView mAfterZfxTv;
    @Bind(R.id.before_gfj_iv)
    ImageView mBeforeGfjIv;
    @Bind(R.id.before_gfj_tv)
    TextView mBeforeGfjTv;
    @Bind(R.id.after_gfj_iv)
    ImageView mAfterGfjIv;
    @Bind(R.id.after_gfj_tv)
    TextView mAfterGfjTv;
    @Bind(R.id.before_tfgd_iv)
    ImageView mBeforeTfgdIv;
    @Bind(R.id.before_tfgd_tv)
    TextView mBeforeTfgdTv;
    @Bind(R.id.after_tfgd_iv)
    ImageView mAfterTfgdIv;
    @Bind(R.id.after_tfgd_tv)
    TextView mAfterTfgdTv;
    @Bind(R.id.before_lnq_iv)
    ImageView mBeforeLnqIv;
    @Bind(R.id.before_lnq_tv)
    TextView mBeforeLnqTv;
    @Bind(R.id.after_lnq_iv)
    ImageView mAfterLnqIv;
    @Bind(R.id.after_lnq_tv)
    TextView mAfterLnqTv;
    @Bind(R.id.before_ktxt_iv)
    ImageView mBeforeKtxtIv;
    @Bind(R.id.before_ktxt_tv)
    TextView mBeforeKtxtTv;
    @Bind(R.id.after_ktxt_iv)
    ImageView mAfterKtxtIv;
    @Bind(R.id.after_ktxt_tv)
    TextView mAfterKtxtTv;
    @Bind(R.id.before_kqyx_iv)
    ImageView mBeforeKqyxIv;
    @Bind(R.id.before_kqyx_tv)
    TextView mBeforeKqyxTv;
    @Bind(R.id.after_kqyx_iv)
    ImageView mAfterKqyxIv;
    @Bind(R.id.after_kqyx_tv)
    TextView mAfterKqyxTv;
    @Bind(R.id.battery_state)
    ImageView mBatteryState;
    private boolean mIsYesData = false;
    private Intent mIntent;
    private RowsBean mBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_conditioner);
        ButterKnife.bind(this);
        initListener();
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

    private void initListener() {
        mLvXin.setOnClickListener(this);
        mLvXinAfter.setOnClickListener(this);
        mZhengFaxiangBefore.setOnClickListener(this);
        mZhengFaxiangAfter.setOnClickListener(this);
        mGufengxiangAfter.setOnClickListener(this);
        mGufengxiangBefore.setOnClickListener(this);
        mTongfengAfter.setOnClickListener(this);
        mTongfengBefore.setOnClickListener(this);
        mLengniuqiAfter.setOnClickListener(this);
        mLengniuqiBefore.setOnClickListener(this);
        mZhiengBefore.setOnClickListener(this);
        mZhiengAfter.setOnClickListener(this);
        mYixiangBefore.setOnClickListener(this);
        mYixiangAfter.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
    }

    private void initData() {
        mToobarBgImage.setImageResource(R.mipmap.ktjc_nav);
        Intent intent = getIntent();
        mBean = (RowsBean) intent.getSerializableExtra("BlueToothInfoBean");
        if (mBean != null) {
            //空调滤芯表面污垢
            String beforeLxbmwgPicUrl = mBean.beforeLxbmwgPicUrl;
            String beforeLxbmwgScore = mBean.beforeLxbmwgScore;
            BItmapUtil.loadImageForIv(beforeLxbmwgPicUrl, mBeforeKtlxbmwgIv, beforeLxbmwgScore, mBeforeKtlxbmwgTv, true, this);
            String afterLxbmwgPicUrl = mBean.afterLxbmwgPicUrl;
            String afterLxbmwgScore = mBean.afterLxbmwgScore;
            BItmapUtil.loadImageForIv(afterLxbmwgPicUrl, mAfterKtlxbmwgIv, afterLxbmwgScore, mAfterKtlxbmwgTv, false, this);
            //蒸发箱表面污垢
            String beforeZfxbmwgPicUrl = mBean.beforeZfxbmwgPicUrl;
            String beforeZfxbmwgScore = mBean.beforeZfxbmwgScore;
            BItmapUtil.loadImageForIv(beforeZfxbmwgPicUrl, mBeforeZfxIv, beforeZfxbmwgScore, mBeforeZfxTv, true, this);
            String afterZfxbmwgPicUrl = mBean.afterZfxbmwgPicUrl;
            String afterZfxbmwgScore = mBean.afterZfxbmwgScore;
            BItmapUtil.loadImageForIv(afterZfxbmwgPicUrl, mAfterZfxIv, afterZfxbmwgScore, mAfterZfxTv, false, this);
            //鼓风机表面污垢
            String beforeGfjbmwgPicUrl = mBean.beforeGfjbmwgPicUrl;
            String beforeGfjbmwgScore = mBean.beforeGfjbmwgScore;
            BItmapUtil.loadImageForIv(beforeGfjbmwgPicUrl, mBeforeGfjIv, beforeGfjbmwgScore, mBeforeGfjTv, true, this);
            String afterGfjbmwgPicUrl = mBean.afterGfjbmwgPicUrl;
            String afterGfjbmwgScore = mBean.afterGfjbmwgScore;
            BItmapUtil.loadImageForIv(afterGfjbmwgPicUrl, mAfterGfjIv, afterGfjbmwgScore, mAfterGfjTv, false, this);
            //通风管道表面污垢
            String beforeTfgdbmwgPicUrl = mBean.beforeTfgdbmwgPicUrl;
            String beforeTfgdbmwgScore = mBean.beforeTfgdbmwgScore;
            BItmapUtil.loadImageForIv(beforeTfgdbmwgPicUrl, mBeforeTfgdIv, beforeTfgdbmwgScore, mBeforeTfgdTv, true, this);
            String afterTfgdbmwgPicUrl = mBean.afterTfgdbmwgPicUrl;
            String afterTfgdbmwgScore = mBean.afterTfgdbmwgScore;
            BItmapUtil.loadImageForIv(afterTfgdbmwgPicUrl, mAfterTfgdIv, afterTfgdbmwgScore, mAfterTfgdTv, false, this);
            //冷凝器表面污垢
            String beforeLnqbmwgPicUrl = mBean.beforeLnqbmwgPicUrl;
            String beforeLnqbmwgScore = mBean.beforeLnqbmwgScore;
            BItmapUtil.loadImageForIv(beforeLnqbmwgPicUrl, mBeforeLnqIv, beforeLnqbmwgScore, mBeforeLnqTv, true, this);
            String afterLnqbmwgPicUrl = mBean.afterLnqbmwgPicUrl;
            String afterLnqbmwgScore = mBean.afterLnqbmwgScore;
            BItmapUtil.loadImageForIv(afterLnqbmwgPicUrl, mAfterLnqIv, afterLnqbmwgScore, mAfterLnqTv, false, this);
            //空调系统制冷新能
            String beforeKtxtzlxnPicUrl = mBean.beforeKtxtzlxnPicUrl;
            String beforeKtxtzlxnScore = mBean.beforeKtxtzlxnScore;
            BItmapUtil.loadImageForIv(beforeKtxtzlxnPicUrl, mBeforeKtxtIv, beforeKtxtzlxnScore, mBeforeKtxtTv, true, this);
            String afterKtxtzlxnPicUrl = mBean.afterKtxtzlxnPicUrl;
            String afterKtxtzlxnScore = mBean.afterKtxtzlxnScore;
            BItmapUtil.loadImageForIv(afterKtxtzlxnPicUrl, mAfterKtxtIv, afterKtxtzlxnScore, mAfterKtxtTv, false, this);
            //开启空调时异向
            String beforeKtkqyxPicUrl = mBean.beforeKtkqyxPicUrl;
            String beforeKtkqyxScore = mBean.beforeKtkqyxScore;
            BItmapUtil.loadImageForIv(beforeKtkqyxPicUrl, mBeforeKqyxIv, beforeKtkqyxScore, mBeforeKqyxTv, true, this);
            String afterKtkqyxPicUrl = mBean.afterKtkqyxPicUrl;
            String afterKtkqyxScore = mBean.afterKtkqyxScore;
            BItmapUtil.loadImageForIv(afterKtkqyxPicUrl, mAfterKqyxIv, afterKtkqyxScore, mAfterKqyxTv, false, this);

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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.back_btn){
            SendUtil.controlVoice();
            finish();
        }
        if (mBean == null)
            return;
        switch (id) {
            case R.id.lv_xin:                   //空调滤芯表面污垢 施工前
                startActivityForInageUrl(mBean.beforeLxbmwgPicUrl);
                break;
            case R.id.lv_xin_after:             //空调滤芯表面污垢 施工后
                startActivityForInageUrl(mBean.afterLxbmwgPicUrl);
                break;
            case R.id.zheng_faxiang_before:     //蒸发箱表面污垢  施工前
                startActivityForInageUrl(mBean.beforeZfxbmwgPicUrl);
                break;
            case R.id.zheng_faxiang_after:      //蒸发箱表面污垢 施工后
                startActivityForInageUrl(mBean.afterZfxbmwgPicUrl);
                break;
            case R.id.gufengxiang_after:        //鼓风机表面污垢 施工后
                startActivityForInageUrl(mBean.afterGfjbmwgPicUrl);
                break;
            case R.id.gufengxiang_before:       //鼓风机表面污垢 施工前
                startActivityForInageUrl(mBean.beforeGfjbmwgPicUrl);
                break;
            case R.id.tongfeng_after:           //通风管道表面污垢 施工后
                startActivityForInageUrl(mBean.afterTfgdbmwgPicUrl);
                break;
            case R.id.tongfeng_before:          //通风管道表面污垢 施工前
                startActivityForInageUrl(mBean.beforeTfgdbmwgPicUrl);
                break;
            case R.id.lengniuqi_after:          //冷凝器表面污垢  施工后
                startActivityForInageUrl(mBean.afterLnqbmwgPicUrl);
                break;
            case R.id.lengniuqi_before:         //冷凝器表面污垢  施工前
                startActivityForInageUrl(mBean.beforeLnqbmwgPicUrl);
                break;
            case R.id.zhieng_before:            //空调系统制冷性能 施工前
                startActivityForInageUrl(mBean.beforeKtxtzlxnPicUrl);
                break;
            case R.id.zhieng_after:             //空调系统制冷性能 施工后
                startActivityForInageUrl(mBean.afterKtxtzlxnPicUrl);
                break;
            case R.id.yixiang_before:           //开启空调时异响  施工前
                startActivityForInageUrl(mBean.beforeKtkqyxPicUrl);
                break;
            case R.id.yixiang_after:             //开启空调时异响  施工后
                startActivityForInageUrl(mBean.afterKtkqyxPicUrl);
                break;
        }
    }

    private void startActivityForInageUrl(String beforeAndAfterPicUrl) {
        mIntent = new Intent(this, ImageActivity.class);
        mIntent.putExtra("url", beforeAndAfterPicUrl);
        startActivity(mIntent);
    }
}
