package com.cad.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.cad.R;
import com.cad.base.MyApplication;
import com.cad.util.SendUtil;

/**
 * Created by dell on 2018/3/19.
 */

public class IsLockDialog extends Dialog {

    private int mRes;
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
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = 777;
        attributes.height = 305;
        window.setAttributes(attributes);
        setCanceledOnTouchOutside(false);
    }

}
