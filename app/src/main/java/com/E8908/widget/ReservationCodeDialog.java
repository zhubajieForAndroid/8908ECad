package com.E8908.widget;

import android.app.Dialog;
import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.E8908.R;
import com.E8908.util.KeyboardNumberUtil;
import com.E8908.util.NavigationBarUtil;

/**
 * Created by dell on 2017/5/11.
 */

public class ReservationCodeDialog extends Dialog implements View.OnTouchListener {
    private Context mContext;
    private ImageView mIv;
    private static final String TAG = "LoginDialog";
    private EditText mEt;
    private OnLonInListener mOnLonInListener;
    private KeyboardNumberUtil mKeyboardUtil;
    private Window mWindow;


    public ReservationCodeDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        init();
    }

    private void init() {
        setContentView(R.layout.view_open_login_dialog);
        mIv = findViewById(R.id.iv);
        mEt = findViewById(R.id.et);
        KeyboardView keyboardView = findViewById(R.id.keyboard_view);
        mKeyboardUtil = new KeyboardNumberUtil(keyboardView, mContext, mEt);

        //Bitmap bitmap = BItmapUtil.compressBItmap(mContext, R.mipmap.login_img_normal_19);
        mIv.setImageResource(R.mipmap.login_img_normal_19);
        mWindow = getWindow();
        if (mWindow != null) {
            WindowManager.LayoutParams wlp = mWindow.getAttributes();
            wlp.gravity = Gravity.CENTER;
            wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            mWindow.setAttributes(wlp);
        }
        initListener();
    }


    private void initListener() {
        mIv.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float y = event.getY();
        float x = event.getX();
        if (x > 65 && x < 241 && y > 330 && y < 385) {
            if (mKeyboardUtil.isShow()) {
                mKeyboardUtil.hideKeyboard();
            }
            dismiss();
        } else if (x > 290 && x < 470 && y > 330 && y < 385) {
            String reservationCode = mEt.getText().toString().trim();
            if (!TextUtils.isEmpty(reservationCode)){
                mOnLonInListener.loginListener(reservationCode);
                if (mKeyboardUtil.isShow()) {
                    mKeyboardUtil.hideKeyboard();
                }
                dismiss();
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


    public interface OnLonInListener {
        void loginListener(String code);
    }

    public void setOnLoninnListener(OnLonInListener listener) {
        mOnLonInListener = listener;
    }

}
