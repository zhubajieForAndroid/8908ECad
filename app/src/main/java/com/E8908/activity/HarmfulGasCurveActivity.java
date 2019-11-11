package com.E8908.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.adapter.CurvePagerAdapter;
import com.E8908.base.BaseActivity;
import com.E8908.base.CurverBean;
import com.E8908.conf.Constants;
import com.E8908.impl.CurverPersenterImpl;
import com.E8908.util.DataUtil;
import com.E8908.view.BaseView;
import com.E8908.widget.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HarmfulGasCurveActivity extends BaseActivity implements BaseView<CurverBean>, ViewPager.OnPageChangeListener, View.OnClickListener {

    private static final String TAG = "HarmfulGasCurveActivity";
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
    @Bind(R.id.tab)
    TabLayout mTab;
    @Bind(R.id.viewpager)
    ViewPager mViewpager;
    @Bind(R.id.company)
    TextView mCompany;
    @Bind(R.id.progress_bar)
    LinearLayout mProgressBar;
    @Bind(R.id.back_btn)
    ImageView mBackBtn;
    @Bind(R.id.carnumber)
    TextView mCarnumber;
    private String[] titles = {"甲醛浓度", "TVOC", "PM2.5", "温度", "湿度"};
    private boolean mIsYesData = false;
    private CurverBean.ResponseBean mResponse;
    private CurvePagerAdapter mAdapter;
    private String mCarNumber;
    private String mEquipmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harmful_gas_curve);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mCarNumber = intent.getStringExtra("carNumber");
        mEquipmentId = intent.getStringExtra("equipmentId");
        initData();
    }

    private void initData() {
        mToobarBgImage.setImageResource(R.mipmap.yhqt_nav);
        mAdapter = new CurvePagerAdapter(this, titles, mResponse);
        mViewpager.setAdapter(mAdapter);
        mTab.setupWithViewPager(mViewpager);
        CurverPersenterImpl curverPersenter = new CurverPersenterImpl(this);
        curverPersenter.loadData(Constants.URLS.GET_CHECK_SEARCH_BEFORE_AND_AFTER_INFo, mEquipmentId, mCarNumber);
        mViewpager.addOnPageChangeListener(this);
        mBackBtn.setOnClickListener(this);
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
    public void onFaild(String msg) {
        mProgressBar.setVisibility(View.GONE);
        ToastUtil.showMessage("网络异常");
    }

    @Override
    public void onSuccess(CurverBean result) {
        mProgressBar.setVisibility(View.GONE);
        mCompany.setText("单位: mg/m³");
        mCarnumber.setText(mCarNumber);
        mResponse = result.getResponse();
        mAdapter.setData(mResponse);
    }

    @Override
    public void onNoData(String msg) {

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        switch (i) {
            case 0:
                mCompany.setText("单位: mg/m³");
                break;
            case 1:
                mCompany.setText("单位: mg/m³");
                break;
            case 2:
                mCompany.setText("单位: ug/m³");
                break;
            case 3:
                mCompany.setText("单位: C°");
                break;
            case 4:
                mCompany.setText("单位: % RH");
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onClick(View v) {
        finish();
    }

}
