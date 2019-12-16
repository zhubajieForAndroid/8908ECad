package com.E8908.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.E8908.R;

public class YnnLoginDialog extends Dialog implements View.OnClickListener {
    private OnBtnClickListener mOnBtnClickListener;
    private String mTitle;
    private String mYesBtnStr;
    private String mNoBtnStr;
    private boolean mIsLogin;
    public YnnLoginDialog(@NonNull Context context, int themeResId,String title,String yesBtnStr,String noBtnStr,boolean isLogin) {
        super(context, themeResId);
        mTitle = title;
        mYesBtnStr = yesBtnStr;
        mNoBtnStr = noBtnStr;
        mIsLogin = isLogin;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_login_by_yun_dialog);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            params.width = 700;
            params.height = 278;
            window.setAttributes(params);
        }
        Button yesBtn = findViewById(R.id.yes);
        Button noBtn = findViewById(R.id.no);
        TextView textView = findViewById(R.id.title);

        yesBtn.setOnClickListener(this);
        noBtn.setOnClickListener(this);
        if (!TextUtils.isEmpty(mTitle))
            textView.setText(mTitle);
        if (!TextUtils.isEmpty(mYesBtnStr))
            yesBtn.setText(mYesBtnStr);
        if (!TextUtils.isEmpty(mNoBtnStr))
            noBtn.setText(mNoBtnStr);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.yes){
            mOnBtnClickListener.clickBtn(true,mIsLogin);
            dismiss();
        }
        if (id == R.id.no){
            mOnBtnClickListener.clickBtn(false,mIsLogin);
            dismiss();
        }
    }
    public interface OnBtnClickListener{
        void clickBtn(boolean isCancle,boolean isLogin);
    }

    public void setOnBtnClickListener(OnBtnClickListener onBtnClickListener) {
        mOnBtnClickListener = onBtnClickListener;
    }
}
