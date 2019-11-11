package com.E8908.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.E8908.R;
import com.E8908.util.NavigationBarUtil;

/**
 * Created by dell on 2018/4/3.
 */

public class CompleteDialog extends Dialog implements View.OnTouchListener {

    private Window mWindow;

    public CompleteDialog(Context context, int themeResId) {
        super(context, themeResId);

    }
    public void setRes(int res){
        setContentView(R.layout.view_stop_complete);
        setCanceledOnTouchOutside(false);
        ImageView iv = findViewById(R.id.complete_bg);
        iv.setImageResource(res);
        iv.setOnTouchListener(this);
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
        dismiss();
        return false;
    }
}
