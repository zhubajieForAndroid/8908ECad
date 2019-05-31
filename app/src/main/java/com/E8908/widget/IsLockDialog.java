package com.E8908.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.E8908.R;
import com.E8908.util.NavigationBarUtil;

/**
 * Created by dell on 2018/3/19.
 */

public class IsLockDialog extends Dialog {

    private int mRes;
    private Window mWindow;

    public IsLockDialog(Context context, int themeResId,int res) {
        super(context, themeResId);
        mRes = res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_lock);
        ImageView iv = (ImageView) findViewById(R.id.iv);
        iv.setImageResource(mRes);
        mWindow = getWindow();
        WindowManager.LayoutParams attributes = mWindow.getAttributes();
        attributes.width = 777;
        attributes.height = 305;
        attributes.gravity = Gravity.CENTER;
        mWindow.setAttributes(attributes);
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
