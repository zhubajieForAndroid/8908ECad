package com.E8908.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.conf.Constants;
import com.E8908.util.DataUtil;
import com.E8908.util.SharedPreferencesUtils;
import com.E8908.widget.PushDialog;
import com.E8908.widget.ToastUtil;


/**
 * Created by lenovo on 2018/3/15.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    private BroadcastReceiver mBroadcastReceiver;
    private boolean isYesData = false;
    private PushDialog mPushdialog;
    private BroadcastReceiver mPushUrlBroadcastReceiver;
    private boolean pushIsInit;                 //是否设置别名
    private String mEquipmentNumber;
    private boolean mIsCharging;
    private boolean mMode = true;
    private SharedPreferences mStateSp;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        if (mPushdialog == null)
            mPushdialog = new PushDialog(this, R.style.dialog);

        mStateSp = SharedPreferencesUtils.getCommunicationStateSp();


    }


    //监听wifi状态
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
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
                            WifiInfo info = wifiManager.getConnectionInfo();
                            int rssi = info.getRssi();
                            isWifiConnected(true, rssi);
                        } else {
                            isWifiConnected(false, 1);
                        }
                    }
                } else if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {        //系统电量
                    int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                    mIsCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
                    Bundle extras = intent.getExtras();
                    if (extras != null) {
                        int current = extras.getInt("level");// 获得当前电量
                        int total = extras.getInt("scale");// 获得总电量
                        int percent = current * 100 / total;
                        TextView textView = findViewById(R.id.battery_num);
                        if (textView != null){
                            textView.setText(percent+"%");
                        }
                        electricInfo(percent, mIsCharging);
                    }
                }else if (action.equals("tyzc.SHOW")){
                    mMode = intent.getBooleanExtra("mode", false);
                    if (mMode){
                        Intent mintent = new Intent("tyzc.SHOW");
                        mintent.putExtra("mode",false);
                        sendBroadcast(mintent);
                    }
                }
            }
        }

    };

    protected abstract void electricInfo(int percent, boolean isCharging);


    protected abstract void isWifiConnected(boolean b, int level);

    private void regWifi() {
        //注册Wifi监听
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);//wifi状态，是否连上，密码
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);//网络状态变化
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);        //系统电量
        filter.addAction("tyzc.SHOW");//导航条
        registerReceiver(mReceiver, filter);
    }

    /**
     * 注册串口通讯广播
     */
    public void regBroadcast() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final byte[] buffer = intent.getByteArrayExtra("response");
                final int size = intent.getIntExtra("size", 0);
                if (size == Constants.DATA_LONG && buffer[2] == 0x03) {
                    //获取序列号
                    mEquipmentNumber = DataUtil.getEquipmentNumber(buffer);
                }
                onDataReceived(buffer, size);
                isYesData = true;
            }
        };
        IntentFilter filter = new IntentFilter(Constants.DATA);
        registerReceiver(mBroadcastReceiver, filter);
    }

    /**
     * 开启定时器循环检查有没有数据回来
     */
    private Runnable myRunnable = new Thread() {
        @Override
        public void run() {
            super.run();
            MyApplication.getmHandler().postDelayed(this, 3000);
            //保存通讯状态
            SharedPreferences.Editor edit = mStateSp.edit();
            edit.putBoolean("dataState",isYesData);
            edit.apply();

            //动态改变屏幕状态
            changeScreenFlags();

            isYesData(isYesData);
            isYesData = false;
        }
    };

    protected abstract void isYesData(boolean isdata);

    public abstract void onDataReceived(byte[] buffer, int size);
    /**
     * 改变屏幕亮度标记
     */
    private void changeScreenFlags() {
        if (isYesData){
            //屏幕常亮
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }else {
            //取消常亮
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }
    /**
     * 注册监听push广播
     */
    public void regPushUrlBroadcast() {
        mPushUrlBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String url = intent.getStringExtra("url");
                mPushdialog.setImageUrl(url);
                if (!mPushdialog.isShowing())
                    mPushdialog.show();
            }
        };
        IntentFilter intentFilter = new IntentFilter(Constants.PUSH_URL);
        registerReceiver(mPushUrlBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        regWifi();
        regBroadcast();//注册串口通讯广播接受者
        MyApplication.getmHandler().postDelayed(myRunnable, 3000);


    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBroadcastReceiver);
        unregisterReceiver(mReceiver);
        MyApplication.getmHandler().removeCallbacks(myRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hintWindow();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            hintWindow();
        }
    }

    private void hintWindow() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
