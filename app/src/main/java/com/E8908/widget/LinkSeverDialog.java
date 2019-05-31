package com.E8908.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.E8908.R;
import com.E8908.base.MyApplication;
import com.E8908.conf.Constants;
import com.E8908.util.NavigationBarUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dell on 2018/3/19.
 */

public class LinkSeverDialog extends Dialog {

    @Bind(R.id.iv)
    ImageView mIv;
    @Bind(R.id.link_time_one)
    ImageView mLinkTimeOne;
    @Bind(R.id.link_time_two)
    ImageView mLinkTimeTwo;
    @Bind(R.id.link_time_three)
    ImageView mLinkTimeThree;
    @Bind(R.id.link_time_four)
    ImageView mLinkTimeFour;
    private int mRes;
    private boolean isStartTask = true;
    private int second;
    private Timer mTimer;
    private MyTask mTask;
    private OnLinkSeverTimeOutLIstener mOnLinkSeverTimeOutLIstener;
    private Window mWindow;

    public LinkSeverDialog(Context context, int themeResId, int res) {
        super(context, themeResId);
        mRes = res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_link_sever);
        ButterKnife.bind(this);
        mIv.setImageResource(mRes);
        mWindow = getWindow();
        if (mWindow != null) {
            WindowManager.LayoutParams wlp = mWindow.getAttributes();
            wlp.gravity = Gravity.CENTER;
            wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            mWindow.setAttributes(wlp);
        }


    }

    private class MyTask extends TimerTask {
        @Override
        public void run() {
            if (isStartTask) {
                if (second <= 14) {
                    second++;
                }
                MyApplication.getmHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        BitmapUtil.numberToBItmapTwo(second, mLinkTimeThree, mLinkTimeFour);
                    }
                });
                if (second == Constants.LINK_SEVER_TIME_OUT) {
                    stopTask();
                    mOnLinkSeverTimeOutLIstener.isTimeOout(true);
                }
            }
        }
    }

    private void startTask() {
        second = 0;
        isStartTask = true;
        mTimer = new Timer();
        mTask = new MyTask();
        mTimer.schedule(mTask, 0, 1000);
    }

    private void stopTask() {
        isStartTask = false;
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
    }

    public interface OnLinkSeverTimeOutLIstener {
        void isTimeOout(boolean b);
    }

    public void setOnLinkSeverTimeOutLIstener(OnLinkSeverTimeOutLIstener o) {
        mOnLinkSeverTimeOutLIstener = o;
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopTask();
    }


    @Override
    public void show() {
        NavigationBarUtil.focusNotAle(mWindow);
        super.show();
        NavigationBarUtil.hideNavigationBar(mWindow);
        NavigationBarUtil.clearFocusNotAle(mWindow);
        startTask();
    }
}
