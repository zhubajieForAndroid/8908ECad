package com.cad.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import com.cad.conf.Constants;

/**
 * Created by lenovo on 2018/3/15.
 */
public abstract class BaseActivity extends Activity {
    private static final String TAG = "BaseActivity";
    private BroadcastReceiver mBroadcastReceiver;
    private boolean isYesData = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        hideWindow();
        super.onCreate(savedInstanceState);
    }

    private void hideWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
    }

    //监听wifi状态
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiInfo.isConnected()) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = wifiManager.getConnectionInfo();
                int rssi = info.getRssi();
                isWifiConnected(true, rssi);
            } else {
                isWifiConnected(false, 1);
            }
        }

    };

    protected abstract void isWifiConnected(boolean b, int level);

    private void regWifi() {
        //注册Wifi监听
        IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
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
            isYesData(isYesData);
            isYesData = false;
        }
    };

    protected abstract void isYesData(boolean isdata);

    public abstract void onDataReceived(byte[] buffer, int size);


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
}
