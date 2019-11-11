package com.E8908.textFunction;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.E8908.widget.ToastUtil;

import java.util.Timer;
import java.util.TimerTask;

import static android.support.constraint.Constraints.TAG;


public class RunTaskManage {
    private Handler mHandler;
    private Timer mWuhuaTimer;
    private boolean isStart;
    private int mCheckCount;
    private float  mCurrentA;
    private RunTaskManage(Handler handler) {
        mHandler = handler;
    }

    private static RunTaskManage manage;

    public static RunTaskManage getManage(Handler handler) {
        if (manage == null) {
            synchronized (RunTaskManage.class) {
                if (manage == null) {
                    manage = new RunTaskManage(handler);
                }
            }
        }
        return manage;
    }
    //TODO----------------------------------雾化,净化,杀菌 start---------------------------------//

    /**
     * @param time  倒计时时间
     * @param runA  运行时最低电流
     * @param state 区分雾化,净化,杀菌
     */
    public void startAddTask(int time, String state, float runA) {
        if (TextUtils.isEmpty(state))
            return;
        //清除所有任务
        stopAddTask();
        isStart = true;
        mCheckCount = 0;
        mWuhuaTimer = new Timer();
        mWuhuaTimer.schedule(new AddTimerTask(time, state, runA), 0, 1000);
    }

    //停止
    public void stopAddTask() {
        if (mWuhuaTimer != null) {
            isStart = false;
            mWuhuaTimer.cancel();
        }
    }

    //暂停
    public void pauseTask() {
        isStart = false;
    }

    //继续
    public void reStart() {
        mCheckCount = 0;
        isStart = true;
    }

    public void setCurrentA(float communionFlow) {
        mCurrentA = communionFlow;
    }

    private class AddTimerTask extends TimerTask {
        private int mWuhuaTime;
        private String mState;
        private Float mRunA;
        public AddTimerTask(int time, String state, float runA) {
            mWuhuaTime = time;
            mState = state;
            mRunA = runA;
        }

        @Override
        public void run() {
            if (isStart) {
                if (mWuhuaTime > 0) {
                    mWuhuaTime--;
                    mCheckCount++;
                    Message message = new Message();
                    switch (mState) {
                        case "雾化":
                            if (mCheckCount >= 5) {
                                if (mCurrentA < mRunA){     //雾化失败
                                    pauseTask();
                                    message.what = 13;
                                }else {
                                    message.what = 1;
                                    message.arg1 = mWuhuaTime;
                                }
                            } else {
                                message.what = 1;
                                message.arg1 = mWuhuaTime;

                            }
                            break;
                        case "杀菌":
                            if (mCheckCount >= 5) {
                                if (mCurrentA < mRunA){     //杀菌失败
                                    pauseTask();
                                    message.what = 14;
                                }else {
                                    message.what = 3;
                                    message.arg1 = mWuhuaTime;
                                }
                            } else {
                                message.what = 3;
                                message.arg1 = mWuhuaTime;
                            }
                            break;
                        case "净化":
                            if (mCheckCount >= 5) {
                                if (mCurrentA < mRunA){     //净化失败
                                    pauseTask();
                                    message.what = 15;
                                }else {
                                    message.what = 5;
                                    message.arg1 = mWuhuaTime;
                                }
                            } else {
                                message.what = 5;
                                message.arg1 = mWuhuaTime;
                            }
                            break;
                        default:
                            message.what = -1;
                            break;

                    }
                    mHandler.sendMessage(message);
                } else {     //倒计时结束
                    stopAddTask();
                    Message message = new Message();
                    switch (mState) {
                        case "雾化":
                            message.what = 2;
                            break;
                        case "杀菌":
                            message.what = 4;
                            break;
                        case "净化":
                            message.what = 6;
                            break;
                        default:
                            message.what = -1;
                            break;
                    }
                    mHandler.sendMessage(message);
                }
            }
        }
    }
    //----------------------------------雾化,净化,杀菌 stop---------------------------------//

}
