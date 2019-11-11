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

public class InputCarNumberHinDialog extends Dialog implements View.OnTouchListener {
    private static final String TAG = "InputCarNumberHinDialog";
    private OnMakeSureBtnClickListener mOnMakeSureBtnClickListener;
    private Context mContext;
    private Thread mThread;
    private Window mWindow;

    public InputCarNumberHinDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    public void setBitmap(int res) {
        setContentView(R.layout.view_stop_dialog);
        ImageView iv = findViewById(R.id.top_bg);
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
        float x = event.getX();
        float y = event.getY();
        if ((x >= 64 && x <= 285) && (y >= 257 && y <= 310)) {                //取消
            dismiss();
        } else if ((x >= 345 && x <= 568) && (y >= 258 && y <= 312)) {            //确定
            mOnMakeSureBtnClickListener.onBtnClick();
        }
        return false;
    }

    public interface OnMakeSureBtnClickListener {
        void onBtnClick();
    }

    public void setOnMakeSureBtnClickListener(OnMakeSureBtnClickListener onMakeSureBtnClickListener) {
        mOnMakeSureBtnClickListener = onMakeSureBtnClickListener;
    }

}
