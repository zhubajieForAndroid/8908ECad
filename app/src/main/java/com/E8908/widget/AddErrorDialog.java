package com.E8908.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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

public class AddErrorDialog extends Dialog implements View.OnTouchListener {

    private static final String TAG = "AddErrorDialog";
    private MakeSuperListener mMakeSuperListener;
    private int mRes ;
    private Window mWindow;

    public AddErrorDialog(Context context, int themeResId,int res) {
        super(context, themeResId);
        mRes = res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_error_dialog);
        ImageView iv = findViewById(R.id.error_bg);
        iv.setImageResource(mRes);
        iv.setOnTouchListener(this);
        setCanceledOnTouchOutside(false);
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
        float x = event.getX();
        float y = event.getY();
        if ((x >= 90 && x <= 480) && (y >= 525 && y <= 590)) {
            mMakeSuperListener.isMakeSupter(true);
        }
        return false;
    }
    public interface MakeSuperListener{
        void isMakeSupter(boolean supter);
    }
    public void setMakeSuperListener(MakeSuperListener m){
        mMakeSuperListener = m;
    }
    @Override
    public void show() {
        NavigationBarUtil.focusNotAle(mWindow);
        super.show();
        NavigationBarUtil.hideNavigationBar(mWindow);
        NavigationBarUtil.clearFocusNotAle(mWindow);
    }
}
