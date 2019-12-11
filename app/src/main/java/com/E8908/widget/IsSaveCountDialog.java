package com.E8908.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.E8908.R;


public class IsSaveCountDialog extends Dialog implements View.OnClickListener {
    private OnYesBtnListener mOnYesBtnListener;
    public IsSaveCountDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_savecount);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            params.width = 700;
            params.height = 278;
            window.setAttributes(params);
        }
        setCanceledOnTouchOutside(false);

        findViewById(R.id.yes).setOnClickListener(this);
        findViewById(R.id.no).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.yes){
            mOnYesBtnListener.isYesBtnClick(true);
        }
        if (id == R.id.no){
            mOnYesBtnListener.isYesBtnClick(false);
            dismiss();
        }
    }
    public interface OnYesBtnListener{
        void isYesBtnClick(boolean isYes);
    }

    public void setOnYesBtnListener(OnYesBtnListener onYesBtnListener) {
        mOnYesBtnListener = onYesBtnListener;
    }
}
