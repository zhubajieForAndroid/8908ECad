package com.E8908.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.adapter.WifiAdapter;
import com.E8908.base.BaseActivity;
import com.E8908.base.MyApplication;
import com.E8908.conf.Constants;
import com.E8908.util.DataUtil;
import com.E8908.util.SendUtil;
import com.E8908.util.WifiUtils;
import com.E8908.widget.AddNewWifiDialog;
import com.E8908.widget.PassworldDialog;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WifiActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {

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
    @Bind(R.id.checkbox_wifi)
    CheckBox mCheckboxWifi;
    private WifiUtils mWifiUtils;
    private List<ScanResult> mWifiList;
    private Animation mAnimation;
    private AddNewWifiDialog mWifiDialog;
    private boolean mIsYesData = false;
    private PassworldDialog mDialog;
<<<<<<< Updated upstream:app/src/main/java/com/cad/activity/WifiActivity.java
    private WifiAdapter mAdapter;
    private Timer mWifiScanTimer;
    private ScanTimerTask mScanTimerTask;
=======
    private Timer mWifiScanTimer;
    private ScanTimerTask mScanTimerTask;
    private WifiAdapter mAdapter;
>>>>>>> Stashed changes:app/src/main/java/com/E8908/activity/WifiActivity.java

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        ButterKnife.bind(this);
        mWifiDialog = new AddNewWifiDialog(this, R.style.dialog);
        mWifiUtils = WifiUtils.getInstance(this);
        //注册Wifi监听
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);//wifi状态，是否连上，密码
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);//网络状态变化
        registerReceiver(mReceiver, filter);
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
<<<<<<< Updated upstream:app/src/main/java/com/cad/activity/WifiActivity.java
=======
        mCheckboxWifi.setOnCheckedChangeListener(this);
        mCheckboxWifi.setChecked(mWifiUtils.wifiIsEnabled());
>>>>>>> Stashed changes:app/src/main/java/com/E8908/activity/WifiActivity.java
        mAdapter = new WifiAdapter(this, mWifiList, mWifiUtils);
        mWifiListview.setAdapter(mAdapter);
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.refreshing_animation);
        LinearInterpolator lir = new LinearInterpolator();
        mAnimation.setInterpolator(lir);

        mToobarBgImage.setImageResource(R.mipmap.top_bar_wifi_6_1);

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
        startScanWifiTask();
    }

    /**
     * 子线程扫描Wifi
     */
    private void scaleWifi() {
        if (mWifiUtils != null && mWifiUtils.wifiIsEnabled()) {
            mWifiUtils.startScale();
            mWifiList = mWifiUtils.getWifiList();
            if (mWifiList != null) {
                MyApplication.getmHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshImage.startAnimation(mAnimation);
                        mAdapter.updataWifiList(mWifiList);
                    }
                });
            }
        }
    }
    private void startScanWifiTask() {
        if (mWifiScanTimer == null)
            mWifiScanTimer = new Timer();
        if (mScanTimerTask == null) {
            mScanTimerTask = new ScanTimerTask();
            mWifiScanTimer.schedule(mScanTimerTask, 0, 8000);
        }
    }

    private void stopScanWifiTask() {
        if (mWifiScanTimer != null) {
            mWifiScanTimer.cancel();
            mWifiScanTimer = null;
        }
        if (mScanTimerTask != null) {
            mScanTimerTask.cancel();
            mScanTimerTask = null;
        }
    }

<<<<<<< Updated upstream:app/src/main/java/com/cad/activity/WifiActivity.java
=======
    private void startScanWifiTask() {
        if (mWifiScanTimer == null)
            mWifiScanTimer = new Timer();
        if (mScanTimerTask == null) {
            mScanTimerTask = new ScanTimerTask();
            mWifiScanTimer.schedule(mScanTimerTask, 0, 8000);
        }
    }

    private void stopScanWifiTask() {
        if (mWifiScanTimer != null) {
            mWifiScanTimer.cancel();
            mWifiScanTimer = null;
        }
        if (mScanTimerTask != null) {
            mScanTimerTask.cancel();
            mScanTimerTask = null;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){                     //打开wifi
            mWifiUtils.openWifi();
        }else {                              //关闭wifi
            mWifiUtils.closeWifi();
            if (mWifiList != null) {
                mWifiList.clear();
                mAdapter.notifyDataSetChanged();
            }
        }
    }

>>>>>>> Stashed changes:app/src/main/java/com/E8908/activity/WifiActivity.java
    private class ScanTimerTask extends TimerTask {
        @Override
        public void run() {
            scaleWifi();
        }
    }
<<<<<<< Updated upstream:app/src/main/java/com/cad/activity/WifiActivity.java
=======

>>>>>>> Stashed changes:app/src/main/java/com/E8908/activity/WifiActivity.java
    //监听wifi状态
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                    Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    if (null != parcelableExtra) {
                        NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                        NetworkInfo.State state = networkInfo.getState();
                        boolean isConnected = state == NetworkInfo.State.CONNECTED;
                        if (isConnected) {
                            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                            String wifiSSID = wifiManager.getConnectionInfo().getSSID();
                            mAlreadyLinkName.setVisibility(View.VISIBLE);
                            mAlreadyLinkName.setText(wifiSSID.substring(1, wifiSSID.length() - 1));
                            mAlreadyLinkRefresh.setVisibility(View.GONE);
                        } else {
                            mAlreadyLinkName.setText("wifi断开连接");
                        }
                    }
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        stopScanWifiTask();
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
        if (mWifiList != null && position <= mWifiList.size() - 1) {
            ScanResult result = (ScanResult) mAdapter.getItem(position);
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
