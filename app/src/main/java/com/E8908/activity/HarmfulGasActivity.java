package com.E8908.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.base.BaseActivity;
import com.E8908.bean.RowsBean;
import com.E8908.conf.Constants;
import com.E8908.util.DataUtil;
import com.E8908.util.SendUtil;
import com.E8908.widget.MyProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HarmfulGasActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "HarmfulGasActivity";
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
    @Bind(R.id.back_btn)
    ImageView mBackBtn;
    @Bind(R.id.before_number_formaldehyde)
    TextView mBeforeNumberFormaldehyde;
    @Bind(R.id.after_number_formaldehyde)
    TextView mAfterNumberFormaldehyde;
    @Bind(R.id.before_scpre_formaldehyde)
    MyProgressBar mBeforeScpreFormaldehyde;
    @Bind(R.id.after_scpre_formaldehyde)
    MyProgressBar mAfterScpreFormaldehyde;
    @Bind(R.id.before_score_tvoc)
    TextView mBeforeScoreTvoc;
    @Bind(R.id.after_score_tvoc)
    TextView mAfterScoreTvoc;
    @Bind(R.id.before_progress_tvoc)
    MyProgressBar mBeforeProgressTvoc;
    @Bind(R.id.after_progress_tvoc)
    MyProgressBar mAfterProgressTvoc;
    @Bind(R.id.before_score_pm)
    TextView mBeforeScorePm;
    @Bind(R.id.after_score_pm)
    TextView mAfterScorePm;
    @Bind(R.id.before_progress_pm)
    MyProgressBar mBeforeProgressPm;
    @Bind(R.id.after_progress_pm)
    MyProgressBar mAfterProgressPm;
    @Bind(R.id.before_score_wen)
    TextView mBeforeScoreWen;
    @Bind(R.id.after_score_wen)
    TextView mAfterScoreWen;
    @Bind(R.id.before_progress_wen)
    MyProgressBar mBeforeProgressWen;
    @Bind(R.id.after_progress_wen)
    MyProgressBar mAfterProgressWen;
    @Bind(R.id.before_score_shi)
    TextView mBeforeScoreShi;
    @Bind(R.id.after_score_shi)
    TextView mAfterScoreShi;
    @Bind(R.id.before_progress_shi)
    MyProgressBar mBeforeProgressShi;
    @Bind(R.id.after_progress_shi)
    MyProgressBar mAfterProgressShi;
    private boolean mIsYesData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harmful_gas);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        mBeforeScpreFormaldehyde.setMax(6250);      //甲醛检测范围甲醛:0-6.25mg/m3
        mAfterScpreFormaldehyde.setMax(6250);      //甲醛检测范围甲醛:0-6.25mg/m3
        mBeforeProgressTvoc.setMax(1000);     //tvov:0-10mg/m3 读到的是放大100被
        mAfterProgressTvoc.setMax(1000);     //tvov:0-10mg/m3 读到的是放大100被
        mBeforeProgressPm.setMax(300);           //pm2.5:ug/m3读到的数值  0-300
        mAfterProgressPm.setMax(300);           //pm2.5:ug/m3读到的数值  0-300
        mBeforeProgressWen.setMax(800);            //温度:-40  到 80
        mAfterProgressWen.setMax(800);            //温度:-40  到 80
        mBeforeProgressShi.setMax(999);           //湿度:0-99.9%Rh
        mAfterProgressShi.setMax(999);           //湿度:0-99.9%Rh
        Intent intent = getIntent();
        RowsBean bean = (RowsBean) intent.getSerializableExtra("BlueToothInfoBean");
        mToobarBgImage.setImageResource(R.mipmap.yhqt_nav);
        mBackBtn.setOnClickListener(this);
        if (bean != null) {
            String beforeJqStr = bean.beforeJq;
            String afterJqStr = bean.afterJq;
            if (!TextUtils.isEmpty(beforeJqStr)) {
                float beforeJqInt = Float.parseFloat(beforeJqStr);
                mBeforeNumberFormaldehyde.setText(beforeJqInt + "mg/m3");
                int resultJq = (int) (beforeJqInt * 1000);
                setBeforeProgressAndColor(resultJq, 60, 100, 200, mBeforeScpreFormaldehyde);
            }
            if (!TextUtils.isEmpty(afterJqStr)) {
                float afterJqInt = Float.parseFloat(afterJqStr);
                mAfterNumberFormaldehyde.setText(afterJqInt + "mg/m3");
                int resultJq = (int) (afterJqInt * 1000);
                setAfterProgressAndColor(resultJq, 60, 100, 200, mAfterScpreFormaldehyde);
            }
            String beforeTvoc = bean.beforeTvoc;
            String afterTvoc = bean.afterTvoc;
            if (!TextUtils.isEmpty(beforeTvoc)) {
                float beforeTvocInt = Float.parseFloat(beforeTvoc);
                mBeforeScoreTvoc.setText(beforeTvocInt + "mg/m3");
                int resultJq = (int) (beforeTvocInt * 100);
                setBeforeProgressAndColor(resultJq, 30, 60, 160, mBeforeProgressTvoc);
            }
            if (!TextUtils.isEmpty(afterTvoc)) {
                float afterTvocInt = Float.parseFloat(afterTvoc);
                mAfterScoreTvoc.setText(afterTvocInt + "mg/m3");
                int resultJq = (int) (afterTvocInt * 100);
                setAfterProgressAndColor(resultJq, 30, 60, 160, mAfterProgressTvoc);
            }
            String beforPm = bean.beforePm25;
            String afterPm = bean.afterPm25;
            if (!TextUtils.isEmpty(beforPm)) {
                float beforePmInt = Float.parseFloat(beforPm);
                mBeforeScorePm.setText(beforePmInt + "ug/m3");
                int resultJq = (int) (beforePmInt);
                setBeforeProgressAndColor(resultJq, 30, 60, 160, mBeforeProgressPm);
            }
            if (!TextUtils.isEmpty(afterPm)) {
                float afterPmInt = Float.parseFloat(afterPm);
                mAfterScorePm.setText(afterPmInt + "ug/m3");
                int resultJq = (int) (afterPmInt);
                setAfterProgressAndColor(resultJq, 35, 75, 150, mAfterProgressPm);
            }
            String beforWen = bean.beforeWd;
            String afterWen = bean.afterWd;
            if (!TextUtils.isEmpty(beforWen)) {
                float beforeWenInt = Float.parseFloat(beforWen);
                mBeforeScoreWen.setText(beforeWenInt + "C°");
                int resultWen = (int) (beforeWenInt * 10);
                if (resultWen < 180) {
                    mBeforeProgressWen.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors_red));
                }
                if (resultWen > 250 && resultWen <= 320) {
                    mBeforeProgressWen.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors));
                }
                if (resultWen > 320) {
                    mBeforeProgressWen.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors_red));
                }
                mBeforeProgressWen.setProgress(resultWen);
            }
            if (!TextUtils.isEmpty(afterWen)) {
                float afterWenInt = Float.parseFloat(afterWen);
                mAfterScoreWen.setText(afterWenInt + "C°");
                int resultWen = (int) (afterWenInt * 10);
                if (resultWen < 180) {
                    mAfterProgressWen.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors_red));
                }
                if (resultWen > 250 && resultWen <= 320) {
                    mAfterProgressWen.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors));
                }
                if (resultWen > 320) {
                    mAfterProgressWen.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors_red));
                }
                mAfterProgressWen.setProgress(resultWen);
            }
            String beforShi = bean.beforeSd;
            String afterShi = bean.afterSd;
            if (!TextUtils.isEmpty(beforShi)) {
                float beforeShiInt = Float.parseFloat(beforShi);
                mBeforeScoreShi.setText(beforeShiInt + "%RH");
                int resultJq = (int) (beforeShiInt*10);
                mBeforeProgressShi.setProgress(resultJq);
            }
            if (!TextUtils.isEmpty(afterShi)) {
                float afterShiInt = Float.parseFloat(afterShi);
                mAfterScoreShi.setText(afterShiInt + "%RH");
                int resultJq = (int) (afterShiInt*10);
                mAfterProgressShi.setProgress(resultJq);
            }
        }
    }

    private void setAfterProgressAndColor(int resultJq, int i, int i1, int i2, MyProgressBar afterScpreFormaldehyde) {
        if (resultJq <= i) {
            afterScpreFormaldehyde.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors));
        }
        if (resultJq > i && resultJq <= i1) {
            afterScpreFormaldehyde.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors_blue));
        }
        if (resultJq > i1 && resultJq <= i2) {             //轻度污染
            afterScpreFormaldehyde.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors_yellow));
        }
        if (resultJq > i2) {                                    //重度污染
            afterScpreFormaldehyde.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors_red));
        }
        afterScpreFormaldehyde.setProgress(resultJq);
    }

    private void setBeforeProgressAndColor(int resultJq, int i, int i1, int i2, MyProgressBar beforeScpreFormaldehyde) {
        if (resultJq <= i) {
            beforeScpreFormaldehyde.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors));
        }
        if (resultJq > i && resultJq <= i1) {
            beforeScpreFormaldehyde.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors_blue));
        }
        if (resultJq > i1 && resultJq <= i2) {             //轻度污染
            beforeScpreFormaldehyde.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors_yellow));
        }
        if (resultJq > i2) {                                    //重度污染
            beforeScpreFormaldehyde.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors_red));
        }
        beforeScpreFormaldehyde.setProgress(resultJq);
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
        switch (v.getId()) {
            case R.id.back_btn:
                SendUtil.controlVoice();
                finish();
                break;
        }
    }
}
