package com.cad.thread;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cad.base.MyApplication;
import com.cad.conf.Constants;
import com.cad.util.FileUtil;
import com.cad.util.SendUtil;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by dell on 2018/3/23.
 */

public class SendService extends Service {
    private static final String TAG = "SendService";
    boolean wifiIsConnect;

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(mReceiver, filter);
        MyApplication.getmHandler().postDelayed(mRunnable, 1000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        MyApplication.getmHandler().removeCallbacks(mRunnable);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private Runnable mRunnable = new Thread() {
        @Override
        public void run() {
            boolean sendState = FileUtil.getSendState();
            if (sendState) {
                MyApplication.getmHandler().postDelayed(this, 1000);
                int[] bytes;
                if (wifiIsConnect) {                 //wifi连接
                    bytes = new int[]{0x2a, 0x06, 0x03, 0x01, 0x2E, 0x23};
                } else {                             //Wifi未连接
                    bytes = new int[]{0x2A, 0x06, 0x03, 0x02, 0x2D, 0x23};
                }
                SendUtil.sendMessage(bytes, MyApplication.getOutputStream());
            }
        }
    };
    //监听wifi状态
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiInfo.isConnected()) {
                wifiIsConnect = true;
            } else {
                wifiIsConnect = false;
            }
        }
    };
}
