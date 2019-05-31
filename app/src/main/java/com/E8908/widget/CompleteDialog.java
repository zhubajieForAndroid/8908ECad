package com.E8908.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.E8908.R;
import com.E8908.util.NavigationBarUtil;

/**
 * Created by dell on 2018/4/3.
 */

public class CompleteDialog extends Dialog {

    private Window mWindow;

    public CompleteDialog(Context context, int themeResId) {
        super(context, themeResId);

    }
    public void setRes(int res){
        setContentView(R.layout.view_stop_complete);
        ImageView iv = (ImageView) findViewById(R.id.complete_bg);
        iv.setImageResource(res);
        mWindow = getWindow();
        if (mWindow != null) {
            WindowManager.LayoutParams wlp = mWindow.getAttributes();
            wlp.gravity = Gravity.CENTER;
            wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            mWindow.setAttributes(wlp);
        }
    }
    @Override
    public void show() {
        NavigationBarUtil.focusNotAle(mWindow);
        super.show();
        NavigationBarUtil.hideNavigationBar(mWindow);
        NavigationBarUtil.clearFocusNotAle(mWindow);
    }

}
