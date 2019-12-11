package com.E8908.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.adapter.WifiAdapter;
import com.E8908.base.BaseToolBarActivity;
import com.E8908.base.MyApplication;
import com.E8908.util.SendUtil;
import com.E8908.util.WifiUtils;
import com.E8908.widget.AddNewWifiDialog;
import com.E8908.widget.PassworldDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StartAppTwoActivity extends BaseToolBarActivity implements View.OnClickListener, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "StartAppTwoActivity";
    @Bind(R.id.already_link_name)
    TextView mAlreadyLinkName;
    @Bind(R.id.refresh_image)
    ImageView mRefreshImage;
    @Bind(R.id.wifi_listview)
    ListView mWifiListview;
    @Bind(R.id.about_system_back)
    ImageView mAboutSystemBack;
    @Bind(R.id.checkbox_wifi)
    CheckBox mCheckboxWifi;
    private WifiUtils mWifiUtils;
    private List<ScanResult> mWifiList;
    private Animation mAnimation;
    private AddNewWifiDialog mWifiDialog;
    private PassworldDialog mDialog;
    private WifiAdapter mAdapter;
    private Timer mWifiScanTimer;
    private ScanTimerTask mScanTimerTask;
    private static final int REQUEST_CAMERA = 101;
    private String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.BLUETOOTH
            , Manifest.permission.BLUETOOTH_ADMIN};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_app_two);
        ButterKnife.bind(this);
        requestPermission();
        mWifiDialog = new AddNewWifiDialog(this, R.style.dialog);
        mWifiUtils = WifiUtils.getInstance(this);
        //注册Wifi监听
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);//wifi状态，是否连上，密码
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);//网络状态变化
        registerReceiver(mReceiver, filter);


        initData();
        initListener();
    }



    @Override
    protected void isYesData(boolean isdata) {

    }

    private void initListener() {
        mAboutSystemBack.setOnClickListener(this);
        mWifiListview.setOnItemClickListener(this);
        mCheckboxWifi.setOnCheckedChangeListener(this);
        mCheckboxWifi.setChecked(mWifiUtils.wifiIsEnabled());
    }

    private void initData() {
        mAdapter = new WifiAdapter(StartAppTwoActivity.this, mWifiList, mWifiUtils);
        mWifiListview.setAdapter(mAdapter);
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.refreshing_animation);
        LinearInterpolator lir = new LinearInterpolator();
        mAnimation.setInterpolator(lir);

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
                            mAlreadyLinkName.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        startScanWifiTask();
    }

    private void requestPermission() {
        List<String> mPermissionList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (ActivityCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                //没有授权的权限
                mPermissionList.add(permissions[i]);
            }
        }
        if (mPermissionList.isEmpty()) {
            //全部允许了

        } else {
            String[] strings = mPermissionList.toArray(new String[mPermissionList.size()]);
            ActivityCompat.requestPermissions(this, strings, REQUEST_CAMERA);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //判断是否勾选禁止后不再询问
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
                }
            }
        }
    }


    @Override
    public int getToolbarImage() {
        return R.mipmap.top_bar_wifi_6_1;
    }

    @Override
    protected void equipmentData(byte[] buffer) {

    }

    @Override
    protected void setResultData(byte[] buffer) {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, StartAppOneActivity.class);
        startActivity(intent);
        finish();
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

    private class ScanTimerTask extends TimerTask {
        @Override
        public void run() {
            scaleWifi();
        }
    }

    /**
     * 扫描Wifi
     */
    private void scaleWifi() {
        if (mWifiUtils != null  && mWifiUtils.wifiIsEnabled()) {
            mWifiUtils.startScale();
            //获取烧苗结果
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
        if (mAnimation != null) {
            mAnimation.cancel();
        }
        stopScanWifiTask();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SendUtil.controlVoice();
        if (mWifiList != null && position <= mWifiList.size() - 1) {
            ScanResult result = (ScanResult) mAdapter.getItem(position);
            if (result != null) {
                final int security = mWifiUtils.getSecurity(result);
                final String ssid = result.SSID;
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
