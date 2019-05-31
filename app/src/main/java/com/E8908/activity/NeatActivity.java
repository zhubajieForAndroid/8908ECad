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


public class NeatActivity extends BaseActivity implements View.OnClickListener {

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
    @Bind(R.id.baifang_before)
    RelativeLayout mBaifangBefore;
    @Bind(R.id.baifang_after)
    RelativeLayout mBaifangAfter;
    @Bind(R.id.kong_luxin_before)
    RelativeLayout mKongLuxinBefore;
    @Bind(R.id.kong_luxin_after)
    RelativeLayout mKongLuxinAfter;
    @Bind(R.id.shiwu_before)
    RelativeLayout mShiwuBefore;
    @Bind(R.id.shiwu_after)
    RelativeLayout mShiwuAfter;
    @Bind(R.id.neishi_before)
    RelativeLayout mNeishiBefore;
    @Bind(R.id.neishi_after)
    RelativeLayout mNeishiAfter;
    @Bind(R.id.fencheng_before)
    RelativeLayout mFenchengBefore;
    @Bind(R.id.fencheng_after)
    RelativeLayout mFenchengAfter;
    @Bind(R.id.shajun_before)
    RelativeLayout mShajunBefore;
    @Bind(R.id.shajun_after)
    RelativeLayout mShajunAfter;
    @Bind(R.id.kongqi_before)
    RelativeLayout mKongqiBefore;
    @Bind(R.id.kongqi_after)
    RelativeLayout mKongqiAfter;
    @Bind(R.id.clear_before)
    RelativeLayout mClearBefore;
    @Bind(R.id.clear_after)
    RelativeLayout mClearAfter;
    @Bind(R.id.before_wpbf_iv)
    ImageView mBeforeWpbfIv;
    @Bind(R.id.before_wpbf_tv)
    TextView mBeforeWpbfTv;
    @Bind(R.id.after_wpbf_iv)
    ImageView mAfterWpbfIv;
    @Bind(R.id.after_wpbf_tv)
    TextView mAfterWpbfTv;
    @Bind(R.id.before_ktlx_iv)
    ImageView mBeforeKtlxIv;
    @Bind(R.id.before_ktlx_tv)
    TextView mBeforeKtlxTv;
    @Bind(R.id.after_ktlv_iv)
    ImageView mAfterKtlvIv;
    @Bind(R.id.after_ktlc_tv)
    TextView mAfterKtlcTv;
    @Bind(R.id.before_swcz_iv)
    ImageView mBeforeSwczIv;
    @Bind(R.id.before_swcz_tv)
    TextView mBeforeSwczTv;
    @Bind(R.id.after_swcz_iv)
    ImageView mAfterSwczIv;
    @Bind(R.id.after_swcz_tv)
    TextView mAfterSwczTv;
    @Bind(R.id.before_nsjjd_iv)
    ImageView mBeforeNsjjdIv;
    @Bind(R.id.before_nsjjd_tv)
    TextView mBeforeNsjjdTv;
    @Bind(R.id.after_nsjjd_iv)
    ImageView mAfterNsjjdIv;
    @Bind(R.id.after_nsjjd_tv)
    TextView mAfterNsjjdTv;
    @Bind(R.id.before_cnfcwr_iv)
    ImageView mBeforeCnfcwrIv;
    @Bind(R.id.before_cnfcwr_tv)
    TextView mBeforeCnfcwrTv;
    @Bind(R.id.after_cnfcwr_iv)
    ImageView mAfterCnfcwrIv;
    @Bind(R.id.after_cnfcwr_tv)
    TextView mAfterCnfcwrTv;
    @Bind(R.id.before_sjxd_iv)
    ImageView mBeforeSjxdIv;
    @Bind(R.id.before_sjxd_tv)
    TextView mBeforeSjxdTv;
    @Bind(R.id.after_sjxd_iv)
    ImageView mAfterSjxdIv;
    @Bind(R.id.after_sjxd_tv)
    TextView mAfterSjxdTv;
    @Bind(R.id.before_kqjh_iv)
    ImageView mBeforeKqjhIv;
    @Bind(R.id.before_kqjh_tv)
    TextView mBeforeKqjhTv;
    @Bind(R.id.after_kqjh_iv)
    ImageView mAfterKqjhIv;
    @Bind(R.id.after_kqjh_tv)
    TextView mAfterKqjhTv;
    @Bind(R.id.before_cnqx_iv)
    ImageView mBeforeCnqxIv;
    @Bind(R.id.before_cnqx_tv)
    TextView mBeforeCnqxTv;
    @Bind(R.id.after_cnqx_iv)
    ImageView mAfterCnqxIv;
    @Bind(R.id.after_cnqx_tv)
    TextView mAfterCnqxTv;
    private boolean mIsYesData = false;
    private Intent mIntent;
    private RowsBean mBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neat);
        ButterKnife.bind(this);
        initListener();
        initData();
    }

    private void initListener() {
        mBackBtn.setOnClickListener(this);
        mBaifangBefore.setOnClickListener(this);
        mBaifangAfter.setOnClickListener(this);
        mKongLuxinBefore.setOnClickListener(this);
        mKongLuxinAfter.setOnClickListener(this);
        mShiwuBefore.setOnClickListener(this);
        mShiwuAfter.setOnClickListener(this);
        mNeishiBefore.setOnClickListener(this);
        mNeishiAfter.setOnClickListener(this);
        mFenchengBefore.setOnClickListener(this);
        mFenchengAfter.setOnClickListener(this);
        mShajunBefore.setOnClickListener(this);
        mShajunAfter.setOnClickListener(this);
        mKongqiBefore.setOnClickListener(this);
        mKongqiAfter.setOnClickListener(this);
        mClearBefore.setOnClickListener(this);
        mClearAfter.setOnClickListener(this);
    }

    private void initData() {
        mToobarBgImage.setImageResource(R.mipmap.zjws_nav);
        Intent intent = getIntent();
        mBean = (RowsBean) intent.getSerializableExtra("bean");
        if (mBean != null) {
            //滤芯洁净度
            String beforeKtlxUrl = mBean.beforeKqlxPicUrl;
            String beforeKtlxScore = mBean.beforeKqlxScore;
            BItmapUtil.loadImageForIv(beforeKtlxUrl, mBeforeKtlxIv, beforeKtlxScore, mBeforeKtlxTv, true,this);
            String afterKtlxUrl = mBean.afterKqlxPicUrl;
            String afterktlxScore = mBean.afterKqlxScore;
            BItmapUtil.loadImageForIv(afterKtlxUrl, mAfterKtlvIv, afterktlxScore, mAfterKtlcTv, false,this);

            //物品摆放
            String beforeWpbfUrl = mBean.beforeWpbfPicUrl;
            String beforeWpbfScore = mBean.beforeWpbfScore;
            BItmapUtil.loadImageForIv(beforeWpbfUrl, mBeforeWpbfIv, beforeWpbfScore, mBeforeWpbfTv, true,this);
            String afterWpbfUrl = mBean.afterWpbfPicUrl;
            String afterWpbfScore = mBean.afterWpbfScore;
            BItmapUtil.loadImageForIv(afterWpbfUrl, mAfterWpbfIv, afterWpbfScore, mAfterWpbfTv, false,this);

            //食物残渣
            String beforeSwczUrl = mBean.beforeSwczPicUrl;
            String beforeSwczScore = mBean.beforeSwczScore;
            BItmapUtil.loadImageForIv(beforeSwczUrl, mBeforeSwczIv, beforeSwczScore, mBeforeSwczTv, true,this);
            String afterSwczUrl = mBean.afterSwczPicUrl;
            String afterSwczScore = mBean.afterSwczScore;
            BItmapUtil.loadImageForIv(afterSwczUrl, mAfterSwczIv, afterSwczScore, mAfterSwczTv, false,this);

            //内饰洁净度
            String beforeNsjjdUrl = mBean.beforeNsjjdPicUrl;
            String beforeNsjjdScore = mBean.beforeNsjjdScore;
            BItmapUtil.loadImageForIv(beforeNsjjdUrl, mBeforeNsjjdIv, beforeNsjjdScore, mBeforeNsjjdTv, true,this);
            String afterNsjjdUrl = mBean.afterNsjjdPicUrl;
            String afterNsjjdScore = mBean.afterNsjjdScore;
            BItmapUtil.loadImageForIv(afterNsjjdUrl, mAfterNsjjdIv, afterNsjjdScore, mAfterNsjjdTv, false,this);

            //车内粉尘污染
            String beforeCnfcwrUrl = mBean.beforeCnfcwrPicUrl;
            String beforeCnfcwrScore = mBean.beforeCnfcwrScore;
            BItmapUtil.loadImageForIv(beforeCnfcwrUrl, mBeforeCnfcwrIv, beforeCnfcwrScore, mBeforeCnfcwrTv, true,this);
            String afterCnfcwrUrl = mBean.afterCnfcwrPicUrl;
            String afterCnfcwrScore = mBean.afterCnfcwrScore;
            BItmapUtil.loadImageForIv(afterCnfcwrUrl, mAfterCnfcwrIv, afterCnfcwrScore, mAfterCnfcwrTv, false,this);

            //定期车内杀菌消毒
            String beforeXdsjUrl = mBean.beforeXdsjPicUrl;
            String beforeXdsjScore = mBean.beforeXdsjScore;
            BItmapUtil.loadImageForIv(beforeXdsjUrl, mBeforeSjxdIv, beforeXdsjScore, mBeforeSjxdTv, true,this);
            String afterXdsjUrl = mBean.afterXdsjPicUrl;
            String afterXdsjScore = mBean.afterXdsjScore;
            BItmapUtil.loadImageForIv(afterXdsjUrl, mAfterSjxdIv, afterXdsjScore, mAfterSjxdTv, false,this);

            //空气净化
            String beforeKqjhUrl = mBean.beforeKqjhPicUrl;
            String beforeKqjhScore = mBean.beforeKqjhScore;
            BItmapUtil.loadImageForIv(beforeKqjhUrl, mBeforeKqjhIv, beforeKqjhScore, mBeforeKqjhTv, true,this);
            String afterKqjiUrl = mBean.afterKqjhPicUrl;
            String afterKqjiScore = mBean.afterKqjhScore;
            BItmapUtil.loadImageForIv(afterKqjiUrl, mAfterKqjhIv, afterKqjiScore, mAfterKqjhTv, false,this);

            //定期车内清洗
            String beforeCnqxUrl = mBean.beforeCnjxPicUrl;
            String beforeCnqxScore = mBean.beforeCnjxScore;
            BItmapUtil.loadImageForIv(beforeCnqxUrl, mBeforeCnqxIv, beforeCnqxScore, mBeforeCnqxTv, true,this);
            String afterCnqxUrl = mBean.afterCnjxPicUrl;
            String afterCnqxScore = mBean.afterCnjxScore;
            BItmapUtil.loadImageForIv(afterCnqxUrl, mAfterCnqxIv, afterCnqxScore, mAfterCnqxTv, false,this);
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
            case R.id.baifang_before:           //车内物品摆放 施工前
                startActivityForInageUrl(mBean.beforeWpbfPicUrl);
                break;
            case R.id.baifang_after:           //车内物品摆放 施工后
                startActivityForInageUrl(mBean.afterWpbfPicUrl);
                break;
            case R.id.kong_luxin_before:        //空调滤芯洁净度 施工前
                startActivityForInageUrl(mBean.beforeKqlxPicUrl);
                break;
            case R.id.kong_luxin_after:        //空调滤芯洁净度 施工后
                startActivityForInageUrl(mBean.afterKqlxPicUrl);
                break;
            case R.id.shiwu_before:             //车内食物残渣  施工前
                startActivityForInageUrl(mBean.beforeSwczPicUrl);
                break;
            case R.id.shiwu_after:             //车内食物残渣  施工后
                startActivityForInageUrl(mBean.afterSwczPicUrl);
                break;
            case R.id.neishi_before:            //内饰洁净度  施工前
                startActivityForInageUrl(mBean.beforeNsjjdPicUrl);
                break;
            case R.id.neishi_after:            //内饰洁净度  施工后
                startActivityForInageUrl(mBean.afterNsjjdPicUrl);
                break;
            case R.id.fencheng_before:          //车内粉尘污染 施工前
                startActivityForInageUrl(mBean.beforeCnfcwrPicUrl);
                break;
            case R.id.fencheng_after:          //车内粉尘污染 施工后
                startActivityForInageUrl(mBean.afterCnfcwrPicUrl);
                break;
            case R.id.shajun_before:            //定期车内杀菌消毒 施工前
                startActivityForInageUrl(mBean.beforeXdsjPicUrl);
                break;
            case R.id.shajun_after:            //定期车内杀菌消毒 施工后
                startActivityForInageUrl(mBean.afterXdsjPicUrl);
                break;
            case R.id.kongqi_before:            //定期车内空气净化 施工前
                startActivityForInageUrl(mBean.beforeKqjhPicUrl);
                break;
            case R.id.kongqi_after:            //定期车内空气净化 施工后
                startActivityForInageUrl(mBean.afterKqjhPicUrl);
                break;
            case R.id.clear_before:             //定期车内清洗  施工前
                startActivityForInageUrl(mBean.beforeCnjxPicUrl);
                break;
            case R.id.clear_after:             //定期车内清洗  施工后
                startActivityForInageUrl(mBean.afterCnjxPicUrl);
                break;

        }
    }
    private void startActivityForInageUrl(String beforeAndAfterPicUrl) {
        mIntent = new Intent(this, ImageActivity.class);
        mIntent.putExtra("url", beforeAndAfterPicUrl);
        startActivity(mIntent);
    }
}
