package com.E8908.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.E8908.R;
import com.E8908.base.BaseActivity;
import com.E8908.base.MyApplication;
import com.E8908.manage.SocketManage;

import java.util.Timer;
import java.util.TimerTask;

public class StartAppActivity extends BaseActivity {

    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(R.layout.activity_start_page);

        Intent intent = new Intent("tyzc.SHOW");
        intent.putExtra("mode", false);
        sendBroadcast(intent);

        mTimer = new Timer();
        mTimer.schedule(startTask, 500);
        MyApplication.initSerialPort();
    }

    @Override
    protected void isWifiConnected(boolean b, int level) {

    }

    @Override
    protected void isYesData(boolean isdata, boolean isCharging) {

    }

    @Override
    public void onDataReceived(byte[] buffer, int size) {

    }

    private TimerTask startTask = new TimerTask() {
        @Override
        public void run() {
            Intent intent = new Intent(StartAppActivity.this, StartAppTwoActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null)
            mTimer.cancel();
    }

}
