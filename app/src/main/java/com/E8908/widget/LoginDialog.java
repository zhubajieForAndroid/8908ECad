package com.E8908.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.E8908.R;
import com.E8908.util.BItmapUtil;
import com.E8908.util.KeyboardUtil;


/**
 * Created by dell on 2017/5/11.
 */

public class LoginDialog extends Dialog implements View.OnTouchListener {
    private Context mContext;
    private ImageView mIv;
    private EditText mEt;
    private OnLonInListener mOnLonInListener;
    private String mPassworld;
    private KeyboardUtil mKeyboardUtil;

    public LoginDialog(Context context, int themeResId, String passworld) {
        super(context, themeResId);
        mContext = context;
        mPassworld = passworld;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_login_dialog);
        mIv =  findViewById(R.id.iv);
        mEt = findViewById(R.id.et);
        KeyboardView keyboardView = findViewById(R.id.keyboard_view);
        mKeyboardUtil = new KeyboardUtil(keyboardView, mContext, mEt);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER;
            wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(wlp);
        }
        Bitmap bitmap = BItmapUtil.compressBItmap(mContext, R.mipmap.reset);
        mIv.setImageBitmap(bitmap);
        initListener();

    }

    private void initListener() {
        mIv.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float y = event.getY();
        float x = event.getX();
        if (x> 85 && x<230 &&y>180 &&y<230){
            if (mKeyboardUtil.isShow()) {
                mKeyboardUtil.hideKeyboard();
            }
            dismiss();
        }else if (x> 350 && x<505 &&y>180 &&y<235){
            String passworld = mEt.getText().toString().trim();
            if (TextUtils.isEmpty(mPassworld)){
                //校验登录系统设置密码
                if (passworld.equals("581828")){
                    mOnLonInListener.loginListener(true);
                    if (mKeyboardUtil.isShow()) {
                        mKeyboardUtil.hideKeyboard();
                    }
                    dismiss();
                }else {
                    ToastUtil.showMessage("密码错误");
                    mOnLonInListener.loginListener(false);
                    if (mKeyboardUtil.isShow()) {
                        mKeyboardUtil.hideKeyboard();
                    }
                    dismiss();
                }
            }
        }
        return false;
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mKeyboardUtil != null && !mKeyboardUtil.isShow()) {
            mKeyboardUtil.changeKeyboardNumber(true);
            mKeyboardUtil.showKeyboard();
        }
    }

    public interface OnLonInListener{
        void loginListener(Boolean b);
    }
    public void setOnLoninnListener(OnLonInListener listener){
        mOnLonInListener = listener;
    }

}
