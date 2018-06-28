package com.cad.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import com.cad.R;
import com.cad.base.MyApplication;
import com.cad.conf.Constants;

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
    private int minute;
    private int second;
    private Timer mTimer;
    private MyTask mTask;
    private OnLinkSeverTimeOutLIstener mOnLinkSeverTimeOutLIstener;

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
        setCanceledOnTouchOutside(false);


    }

    private class MyTask extends TimerTask {
        @Override
        public void run() {
            if (isStartTask) {
                if (minute <= 98) {
                    if (second <= 59) {
                        second++;
                    }
                    if (second == 60) {
                        minute++;
                        second = 0;
                    }
                    MyApplication.getmHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            BitmapUtil.numberToBItmapTwo(second, mLinkTimeThree, mLinkTimeFour);
                            BitmapUtil.numberToBItmapTwo(minute, mLinkTimeOne, mLinkTimeTwo);
                        }
                    });
                    if (minute == Constants.LINK_SEVER_TIME_OUT) {
                        stopTask();
                        mOnLinkSeverTimeOutLIstener.isTimeOout(true);
                    }
                }
            }
        }
    }

    private void startTask() {
        minute = 0;
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
        super.show();
        startTask();
    }
}
