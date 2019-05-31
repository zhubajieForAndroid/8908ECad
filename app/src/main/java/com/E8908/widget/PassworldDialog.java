package com.E8908.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.base.MyApplication;
import com.E8908.util.SendUtil;

/**
 * Created by dell on 2018/3/20.
 */

public class PassworldDialog extends Dialog implements View.OnTouchListener {
    private static final String TAG = "PassworldDialog";
    private String mSsid;
    private EditText mPassworld;
    private AddWifiInfoListener mWifiInfoListener;
    private int mSecurity;

    public PassworldDialog(Context context, int themeResId, String ssid,int security) {
        super(context, themeResId);
        mSsid = ssid;
        mSecurity = security;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_passworld_dialog);
        TextView tvTitle = (TextView) findViewById(R.id.title);
        if (!TextUtils.isEmpty(mSsid))
            tvTitle.setText(mSsid);
        View viewById = findViewById(R.id.dialog_bg_image);
        viewById.setOnTouchListener(this);
        mPassworld = (EditText) findViewById(R.id.wifi_passworld);
        if (mSecurity == 0){        //无需密码
            mPassworld.setText("此WiFi无需密码");
            mPassworld.setFocusable(false);
        }
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER;
            wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(wlp);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if ((x >= 23 && x <= 240) && (y >= 179 && y <= 227)) {                //取消
            int[] arr = {0x2a, 0x07, 0x04, 0x01, 0x14, 0x3c, 0x23};
            SendUtil.sendMessage(arr, MyApplication.getOutputStream());
            dismiss();
        } else if ((x >= 323 && x <= 542) && (y >= 180 && y <= 230)) {         //加入
            int[] arr = {0x2a, 0x07, 0x04, 0x01, 0x14, 0x3c, 0x23};
            SendUtil.sendMessage(arr, MyApplication.getOutputStream());
            String pass = mPassworld.getText().toString().trim();
            if (!TextUtils.isEmpty(pass)) {
                if (pass.length() >= 8) {
                    mWifiInfoListener.wifiInfo(pass);
                } else {
                    ToastUtil.showMessage("密码至少8位");
                }

            } else {
                ToastUtil.showMessage("密码不能为空");
            }
        }
        return false;
    }

    public interface AddWifiInfoListener {
        void wifiInfo(String passworld);
    }

    public void setWifiInfoListener(AddWifiInfoListener l) {
        mWifiInfoListener = l;
    }
}
