package com.cad.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.cad.R;
import com.cad.base.BaseActivity;
import com.cad.base.MyApplication;

public class StartAppActivity extends BaseActivity {

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        MyApplication.initSerialPort();
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartAppActivity.this, StartAppTwoActivity.class);
                startActivity(intent);
                finish();
            }
        }, 4000);

    }

    @Override
    protected void isWifiConnected(boolean b, int level) {

    }

    @Override
    protected void isYesData(boolean isdata) {

    }

    @Override
    public void onDataReceived(byte[] buffer, int size) {

    }

}
