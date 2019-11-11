package com.E8908.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.E8908.R;
import com.E8908.util.NavigationBarUtil;
import com.E8908.util.SendUtil;

/**
 * Created by dell on 2018/3/19.
 */

public class SetVersionDialog extends Dialog implements View.OnTouchListener {

    private static final String TAG = "SetVersionDialog";
    private SquareEditextContainer mSquareEditextTwoContainer;
    private DataResultListener mDataResultListener;
    private boolean mIsHardwareVersion;         //区分硬件版本和主控版本
    private Window mWindow;

    public SetVersionDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setBitmap(int imageRes, boolean isHardwareVersion) {
        mIsHardwareVersion = isHardwareVersion;
        setContentView(R.layout.dialog_set_version);
        ImageView iv = (ImageView) findViewById(R.id.dialog_bg_image);
        iv.setImageResource(imageRes);
        iv.setOnTouchListener(this);
        mSquareEditextTwoContainer = (SquareEditextContainer) findViewById(R.id.load_result_number);
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
        if ((x >= 10 && x <= 223) && (y >= 148 && y <= 195)) {                //确定
            String resultVersion = mSquareEditextTwoContainer.mBelowEt.getText().toString().trim();
            if (!TextUtils.isEmpty(resultVersion)) {
                mDataResultListener.resultData(resultVersion, mIsHardwareVersion);
                dismiss();
            }
        } else if ((x >= 137 && x <= 456) && (y >= 146 && y <= 194)) {         //取消
            SendUtil.controlVoice();
            dismiss();
        }
        return false;
    }

    public interface DataResultListener {
        void resultData(String data, boolean isHardwareVersion);
    }

    public void setDataResultListener(DataResultListener d) {
        mDataResultListener = d;
    }
}
