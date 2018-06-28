package com.cad.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cad.R;
import com.cad.adapter.WifiAdapter;
import com.cad.base.BaseActivity;
import com.cad.base.MyApplication;
import com.cad.conf.Constants;
import com.cad.util.DataUtil;
import com.cad.util.SendUtil;
import com.cad.util.WifiUtils;
import com.cad.widget.AddNewWifiDialog;
import com.cad.widget.PassworldDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WifiActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = "WifiActivity";
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
    @Bind(R.id.activity_wifi)
    RelativeLayout mActivityWifi;
    @Bind(R.id.already_link_name)
    TextView mAlreadyLinkName;
    @Bind(R.id.already_link_strength)
    ImageView mAlreadyLinkStrength;
    @Bind(R.id.wifi_listview)
    ListView mWifiListview;
    @Bind(R.id.refresh_image)
    ImageView mRefreshImage;
    @Bind(R.id.about_system_back)
    ImageButton mAboutSystemBack;
    @Bind(R.id.already_link_refresh)
    ImageView mAlreadyLinkRefresh;
    @Bind(R.id.message_state)
    TextView mMessageState;
    @Bind(R.id.temperature_state)
    TextView mTemperatureState;
    private WifiUtils mWifiUtils;
    private List<ScanResult> mWifiList;
    private Animation mAnimation;
    private AddNewWifiDialog mWifiDialog;
    private boolean mIsYesData = false;
    private PassworldDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        ButterKnife.bind(this);
        mWifiDialog = new AddNewWifiDialog(this, R.style.dialog);
        mWifiUtils = new WifiUtils(this);
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

    private void initData() {
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.refreshing_animation);
        LinearInterpolator lir = new LinearInterpolator();
        mAnimation.setInterpolator(lir);

        mToobarBgImage.setImageResource(R.mipmap.top_bar_wifi_6_1);
        //注册Wifi监听
        IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(mReceiver, filter);
        View view = View.inflate(this, R.layout.foot_view, null);
        mWifiListview.addFooterView(view);
        //添加新网络
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUtil.controlVoice();
                mWifiDialog.show();
                mWifiDialog.setWifiInfoListener(new AddNewWifiDialog.WifiInfoListener() {
                    @Override
                    public void wifiInfo(String ssid, String passworld, int type) {
                        if (!TextUtils.isEmpty(ssid) && !TextUtils.isEmpty(passworld)) {
                            mWifiUtils.addNetwork(mWifiUtils.CreateWifiInfo(passworld, ssid, type));
                            mWifiUtils.scalWifiConfig();//保存Wifi设置

                            mAlreadyLinkRefresh.setVisibility(View.VISIBLE);
                            mAlreadyLinkName.setVisibility(View.GONE);
                            //执行动画
                            Animation animation = AnimationUtils.loadAnimation(WifiActivity.this, R.anim.refreshing_animation_notime);
                            LinearInterpolator lir = new LinearInterpolator();
                            animation.setInterpolator(lir);
                            mAlreadyLinkRefresh.startAnimation(animation);
                            animation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    //动画执行完成
                                    mAlreadyLinkRefresh.setVisibility(View.GONE);
                                    mAlreadyLinkName.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                        }
                    }
                });
            }
        });
        mAboutSystemBack.setOnClickListener(this);
        mWifiListview.setOnItemClickListener(this);
    }

    /**
     * 扫描Wifi
     */
    private void scaleWifi() {
        if (mWifiUtils != null) {
            mWifiUtils.startScale();
            mRefreshImage.startAnimation(mAnimation);
            mWifiList = mWifiUtils.getWifiList();
            if (mWifiList != null) {
                WifiAdapter adapter = new WifiAdapter(this, mWifiList, mWifiUtils);
                mWifiListview.setAdapter(adapter);
            }
        }
    }

    private Runnable mRunnable = new Thread() {
        @Override
        public void run() {
            scaleWifi();
            MyApplication.getmHandler().postDelayed(this, 3000);
        }
    };
    //监听wifi状态
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiInfo.isConnected()) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                String wifiSSID = wifiManager.getConnectionInfo().getSSID();
                mAlreadyLinkName.setVisibility(View.VISIBLE);
                mAlreadyLinkName.setText(wifiSSID.substring(1, wifiSSID.length() - 1));
                mAlreadyLinkRefresh.setVisibility(View.GONE);
            } else {
                mAlreadyLinkName.setText("wifi已断开连接");
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getmHandler().postDelayed(mRunnable, 3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWifiUtils = null;
        MyApplication.getmHandler().removeCallbacks(mRunnable);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_system_back:
                SendUtil.controlVoice();
                finish();
                break;
        }
    }

    /**
     * 加入网络
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SendUtil.controlVoice();
        if (mWifiList != null) {
            ScanResult result = mWifiList.get(position);
            if (result != null) {
                final String ssid = result.SSID;
                final int security = mWifiUtils.getSecurity(result);

                if (security == 0) {
                    //不需要密码
                    mDialog = new PassworldDialog(this, R.style.dialog, ssid, security);
                } else {
                    mDialog = new PassworldDialog(this, R.style.dialog, ssid, security);
                }
                mDialog.show();
                mDialog.setWifiInfoListener(new PassworldDialog.AddWifiInfoListener() {
                    @Override
                    public void wifiInfo(String passworld) {
                        if (security == 0) {
                            mWifiUtils.addNetwork(mWifiUtils.CreateWifiInfo(ssid, passworld, 1));
                            mWifiUtils.scalWifiConfig();
                            mDialog.dismiss();
                        } else {
                            mWifiUtils.addNetwork(mWifiUtils.CreateWifiInfo(ssid, passworld, 3));
                            mWifiUtils.scalWifiConfig();
                            mDialog.dismiss();
                        }
                    }
                });
            }
        }
    }
}
