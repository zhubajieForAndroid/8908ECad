package com.E8908.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.E8908.R;
import com.E8908.util.NavigationBarUtil;

/**
 * Created by dell on 2018/4/2.
 */

public class LinkErrorDialog extends Dialog {

    private Context mContext;
    private Window mWindow;

    public LinkErrorDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.link_error);
        mWindow = getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = 700;
        lp.height = 278;
        mWindow.setAttributes(lp);
        setCanceledOnTouchOutside(false);
    }
    @Override
    public void show() {
        NavigationBarUtil.focusNotAle(mWindow);
        super.show();
        NavigationBarUtil.hideNavigationBar(mWindow);
        NavigationBarUtil.clearFocusNotAle(mWindow);
    }
}
