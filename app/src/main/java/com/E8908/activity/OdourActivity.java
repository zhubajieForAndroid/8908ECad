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

public class OdourActivity extends BaseActivity implements View.OnClickListener {

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
    @Bind(R.id.before_cfk_score)
    TextView mBeforeCfkScore;
    @Bind(R.id.before_cfk_progress)
    MyProgressBar mBeforeCfkProgress;
    @Bind(R.id.after_cfk_progress)
    MyProgressBar mAfterCfkProgress;
    @Bind(R.id.after_cfk_score)
    TextView mAfterCfkScore;
    @Bind(R.id.before_xcyw_score)
    TextView mBeforeXcywScore;
    @Bind(R.id.before_xcyw_progress)
    MyProgressBar mBeforeXcywProgress;
    @Bind(R.id.after_xcyw_progress)
    MyProgressBar mAfterXcywProgress;
    @Bind(R.id.after_xcyw_score)
    TextView mAfterXcywScore;
    @Bind(R.id.before_nspgyw_score)
    TextView mBeforeNspgywScore;
    @Bind(R.id.before_nspgyw_pgoress)
    MyProgressBar mBeforeNspgywPgoress;
    @Bind(R.id.after_nspgyw_pgoress)
    MyProgressBar mAfterNspgywPgoress;
    @Bind(R.id.after_nspgyw_score)
    TextView mAfterNspgywScore;
    @Bind(R.id.before_nsxfyw_score)
    TextView mBeforeNsxfywScore;
    @Bind(R.id.before_nsxfyw_progress)
    MyProgressBar mBeforeNsxfywProgress;
    @Bind(R.id.after_nsxfyw_progress)
    MyProgressBar mAfterNsxfywProgress;
    @Bind(R.id.after_nsxfyw_score)
    TextView mAfterNsxfywScore;
    @Bind(R.id.before_scwpyw_score)
    TextView mBeforeScwpywScore;
    @Bind(R.id.before_scwpyw_progress)
    MyProgressBar mBeforeScwpywProgress;
    @Bind(R.id.after_scwpyw_progress)
    MyProgressBar mAfterScwpywProgress;
    @Bind(R.id.after_scwpyw_score)
    TextView mAfterScwpywScore;
    @Bind(R.id.before_esy_score)
    TextView mBeforeEsyScore;
    @Bind(R.id.before_esy_progress)
    MyProgressBar mBeforeEsyProgress;
    @Bind(R.id.after_esy_progress)
    MyProgressBar mAfterEsyProgress;
    @Bind(R.id.after_esy_score)
    TextView mAfterEsyScore;
    @Bind(R.id.before_xjmbyw_score)
    TextView mBeforeXjmbywScore;
    @Bind(R.id.before_xjmbyw_progress)
    MyProgressBar mBeforeXjmbywProgress;
    @Bind(R.id.after_xjmbyw_progress)
    MyProgressBar mAfterXjmbywProgress;
    @Bind(R.id.after_xjmbyw_score)
    TextView mAfterXjmbywScore;
    @Bind(R.id.before_qtyw_score)
    TextView mBeforeQtywScore;
    @Bind(R.id.before_qtyw_progress)
    MyProgressBar mBeforeQtywProgress;
    @Bind(R.id.after_qtyw_progress)
    MyProgressBar mAfterQtywProgress;
    @Bind(R.id.after_qtyw_score)
    TextView mAfterQtywScore;
    private boolean mIsYesData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odour);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        mToobarBgImage.setImageResource(R.mipmap.yw_nav);
        mBackBtn.setOnClickListener(this);
        Intent intent = getIntent();
        RowsBean bean = (RowsBean) intent.getSerializableExtra("BlueToothInfoBean");
        if (bean != null){
            //出风口异味
            String beforeCfkyw = bean.beforeCfkyw;
            String afterCdkyw = bean.afterCfkyw;
            checkEmpty(beforeCfkyw,mBeforeCfkProgress,mBeforeCfkScore);
            checkEmpty(afterCdkyw,mAfterCfkProgress,mAfterCfkScore);
            //新车异味
            String beforeXcyw = bean.beforeXcyw;
            String afterXcyw = bean.afterXcyw;
            checkEmpty(beforeXcyw,mBeforeXcywProgress,mBeforeXcywScore);
            checkEmpty(afterXcyw,mAfterXcywProgress,mAfterXcywScore);
            //内饰皮革味
            String beforeNspgyw = bean.beforeNspgyw;
            String afterNspgyw = bean.afterNspgyw;
            checkEmpty(beforeNspgyw,mBeforeNspgywPgoress,mBeforeNspgywScore);
            checkEmpty(afterNspgyw,mAfterNspgywPgoress,mAfterNspgywScore);
            //内饰吸附异味
            String beforeNsxfyw = bean.beforeNsxfyw;
            String afterNsxfyw = bean.afterNsxfyw;
            checkEmpty(beforeNsxfyw,mBeforeNsxfywProgress,mBeforeNsxfywScore);
            checkEmpty(afterNsxfyw,mAfterNsxfywProgress,mAfterNsxfywScore);
            //随车物品异味
            String beforeScwpyw = bean.beforeScwpyw;
            String afterScwpyw = bean.afterScwpyw;
            checkEmpty(beforeScwpyw,mBeforeScwpywProgress,mBeforeScwpywScore);
            checkEmpty(afterScwpyw,mAfterScwpywProgress,mAfterScwpywScore);
            //二手烟异味
            String beforeEsyyw = bean.beforeEsyyw;
            String afterEsyyw = bean.afterEsyyw;
            checkEmpty(beforeEsyyw,mBeforeEsyProgress,mBeforeEsyScore);
            checkEmpty(afterEsyyw,mAfterEsyProgress,mAfterEsyScore);
            //细菌霉变异味
            String beforeXjmbyw = bean.beforeXjmbyw;
            String afterXjmbyw = bean.afterXjmbyw;
            checkEmpty(beforeXjmbyw,mBeforeXjmbywProgress,mBeforeXjmbywScore);
            checkEmpty(afterXjmbyw,mAfterXjmbywProgress,mAfterXjmbywScore);
            //其他异味
            String beforeQtyw = bean.beforeQtyw;
            String afterQtyw = bean.afterQtyw;
            checkEmpty(beforeQtyw,mBeforeQtywProgress,mBeforeQtywScore);
            checkEmpty(afterQtyw,mAfterQtywProgress,mAfterQtywScore);

        }
    }

    private void checkEmpty(String beforeNsxfyw, MyProgressBar beforeNsxfywProgress, TextView beforeNsxfywScore) {
        if (!TextUtils.isEmpty(beforeNsxfyw)){
            setBeforeProgressAndColor(beforeNsxfyw,beforeNsxfywProgress,beforeNsxfywScore);
        }
    }

    private void setBeforeProgressAndColor(String result, MyProgressBar beforeScpreFormaldehyde,TextView textView) {
        float resultInt = Float.parseFloat(result);
        if (resultInt <= 25) {
            beforeScpreFormaldehyde.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors_red));
        }
        if (resultInt > 25 && resultInt <= 50) {
            beforeScpreFormaldehyde.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors_yellow));
        }
        if (resultInt > 50 && resultInt <= 75) {             //轻度污染
            beforeScpreFormaldehyde.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors_blue));
        }
        if (resultInt > 75) {                                    //重度污染
            beforeScpreFormaldehyde.setProgressDrawable(getResources().getDrawable(R.drawable.progress_colors));
        }
        textView.setText((int)resultInt+"分");
        beforeScpreFormaldehyde.setProgress((int) resultInt);
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
