package com.E8908.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.E8908.R;

/**
 * Created by dell on 2018/3/20.
 */

public class AddNewWifiDialog extends Dialog implements View.OnTouchListener {
    private static final String TAG = "AddNewWifiDialog";
    private EditText mWifiUser;
    private EditText mWifiPassworld;
    private Context mContext;
    private TextView mSafeTv;
    //WiFi的加密方式
    private int WIFICIPHER_NOPASS = 1;
    private int WIFICIPHER_WEP = 2;
    private int WIFICIPHER_WPA = 3;

    private int WIFICIPHER;
    private WifiInfoListener mWifiInfoListener;
    private Window mWindow;

    public AddNewWifiDialog(Context context) {
        super(context);
        mContext = context;
    }

    public AddNewWifiDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_add_wifi_dialog);
        ImageView imageView  = (ImageView) findViewById(R.id.dialog_bg_image);
        imageView.setOnTouchListener(this);
        mWifiUser = (EditText) findViewById(R.id.new_wifi_user);
        mWifiPassworld = (EditText) findViewById(R.id.new_wifi_passworld);
        mSafeTv = (TextView) findViewById(R.id.safe);
        mWindow = getWindow();
        if (mWindow != null) {
            WindowManager.LayoutParams wlp = mWindow.getAttributes();
            wlp.gravity = Gravity.CENTER;
            wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            mWindow.setAttributes(wlp);
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if ((x >= 192 && x <= 536) && (y >= 83 && y <= 132)){                //安全性
            showPopupMenu(mSafeTv);
        }else if ((x >= 24 && x <= 240) && (y >= 352 && y <= 400)){          //取消
            dismiss();
        }else if ((x >= 322 && x <= 540) && (y >= 352 && y <= 403)){          //加入
            String ssid = mWifiUser.getText().toString().trim();
            String passworld = mWifiPassworld.getText().toString().trim();
            if (!TextUtils.isEmpty(ssid) && !TextUtils.isEmpty(passworld)){
                if (passworld.length() >= 8) {
                    mWifiInfoListener.wifiInfo(ssid, passworld, WIFICIPHER);
                    dismiss();
                }else {
                    ToastUtil.showMessage("密码至少8位");
                }
            }else {
                ToastUtil.showMessage("名字或密码不能为空");
            }

        }
        return false;
    }
    //弹出菜单配置
    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置 第3个参数控制位置的，只能控制左右，不知道怎么控制上下，
        PopupMenu popupMenu = new PopupMenu(mContext, view, Gravity.END);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.layout, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String type = (String) item.getTitle();
                mSafeTv.setText(type);
                if ("WEP".equals(type)){
                    WIFICIPHER = WIFICIPHER_WEP;
                }
                if ("PA/WPA2 PSK".equals(type)){
                    WIFICIPHER = WIFICIPHER_WPA;
                }
                if ("开放".equals(type)){
                    WIFICIPHER = WIFICIPHER_NOPASS;
                }
                return false;
            }
        });
        popupMenu.show();
    }



    public interface WifiInfoListener{
        void wifiInfo(String ssid,String passworld,int type);
    }
    public void setWifiInfoListener(WifiInfoListener l){
        mWifiInfoListener = l;
    }


}
