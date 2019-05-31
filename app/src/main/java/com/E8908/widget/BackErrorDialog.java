package com.E8908.widget;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.E8908.R;
import com.E8908.util.NavigationBarUtil;

import static android.support.constraint.Constraints.TAG;

/**
 * Created by dell on 2018/4/2.
 */

public class BackErrorDialog extends Dialog implements View.OnTouchListener {

    private Context mContext;
    private Window mWindow;

    public BackErrorDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    public void setID(int resid){
        setContentView(R.layout.back_error);
        mWindow = getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = 556;
        lp.height = 308;
        mWindow.setAttributes(lp);
        ImageView tv = findViewById(R.id.back_error);
        tv.setImageResource(resid);
        tv.setOnTouchListener(this);
    }
    @Override
    public void show() {
        NavigationBarUtil.focusNotAle(mWindow);
        super.show();
        NavigationBarUtil.hideNavigationBar(mWindow);
        NavigationBarUtil.clearFocusNotAle(mWindow);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if ((x >= 133 && x <= 420) && (y >= 222 && y <= 280)) {
            dismiss();
        }
        return false;
    }
}
