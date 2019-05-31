package com.E8908.textFunction;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.conf.Constants;

import me.zhouzhuo.zzhorizontalprogressbar.ZzHorizontalProgressBar;

import static android.support.constraint.Constraints.TAG;

/**
 * 测试程序在运行时弹出的
 */
public class RuningStateDialog extends Dialog implements View.OnClickListener {

    private TextView mRunText;
    private TextView mRunData;
    private OnPauseBtnListener mOnPauseBtnListener;
    private boolean isPause = true;            //是否可暂停
    private Button mPauseBtn;
    private int mCurrentRuningState  = -1;
    private View mBackBtn;
    private ZzHorizontalProgressBar mPb;
    private TextView mNumber;

    public RuningStateDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_runing_state);
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        if (window != null){
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = 600;
            attributes.height = 300;
            window.setAttributes(attributes);
        }
        mRunText = findViewById(R.id.run_text);
        mRunData = findViewById(R.id.run_data);
        mPauseBtn = findViewById(R.id.pause);
        mBackBtn = findViewById(R.id.back);

        mPb = findViewById(R.id.pb);
        mNumber = findViewById(R.id.number);


        mPauseBtn.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
        mPb.setMax(Constants.MAX_NUMBER);                    //剩余药液总升数单位毫升
    }

    //设置电子秤重量
    public void setNumber(String text){
        if (!TextUtils.isEmpty(text)){
            //剩余药液量
            float i = Integer.parseInt(text, 16);
            if (i <= Constants.MAX_NUMBER) {
                mNumber.setText((i/1000) + "L");
                mPb.setProgress((int) i);
            }else {
                mNumber.setText((Constants.MAX_NUMBER/1000) + "L");
                mPb.setProgress(Constants.MAX_NUMBER);
            }
        }
    }
    public void setRunText(String text){
        if (mRunText != null){
            mRunText.setText(text);
        }
    }
    public void setRunData(String text){
        if (mRunData != null){
            mRunData.setText(text);
        }
    }
    //设置当前正在运行什么功能
    public void setCurrentRuningState(int state){
        mCurrentRuningState = state;
    }
    //设置暂停按钮是否可点击
    public void setPauseClickable(boolean b){
        mPauseBtn.setClickable(b);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pause:
                if (isPause){
                    isPause = false;
                    mPauseBtn.setBackground(getContext().getResources().getDrawable(R.drawable.conventional_background_shape));
                    mPauseBtn.setText("继续");
                    mOnPauseBtnListener.onClickPause();
                }else {
                    isPause = true;
                    mPauseBtn.setBackground(getContext().getResources().getDrawable(R.drawable.delete_btn_shape));
                    mPauseBtn.setText("暂停");
                    mOnPauseBtnListener.onContinue(mCurrentRuningState);
                }
                break;
            case R.id.back:
                mOnPauseBtnListener.onBack();
                isPause = true;
                mPauseBtn.setBackground(getContext().getResources().getDrawable(R.drawable.delete_btn_shape));
                mPauseBtn.setText("暂停");
                dismiss();
                break;
        }

    }
    public interface OnPauseBtnListener{
        void onClickPause();
        void onContinue(int state);
        void onBack();
    }

    public void setOnPauseBtnListener(OnPauseBtnListener onPauseBtnListener) {
        mOnPauseBtnListener = onPauseBtnListener;
    }
}
