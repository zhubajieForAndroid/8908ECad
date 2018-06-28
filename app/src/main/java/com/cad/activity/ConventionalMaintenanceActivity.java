package com.cad.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.cad.R;
import com.cad.base.BaseActivity;
import com.cad.base.MyApplication;
import com.cad.conf.Constants;
import com.cad.util.DataUtil;
import com.cad.util.SendUtil;
import com.cad.widget.AddErrorDialog;
import com.cad.widget.BitmapUtil;
import com.cad.widget.CompleteDialog;
import com.cad.widget.StopDialog;
import com.cad.widget.ToastUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhouzhuo.zzhorizontalprogressbar.ZzHorizontalProgressBar;

/**
 * 常规模式,和深度模式界面
 */
public class ConventionalMaintenanceActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MaintenanceActivity";
    @Bind(R.id.btn_start)
    ImageView mBtnStart;
    @Bind(R.id.btn_stop)
    ImageView mBtnStop;
    @Bind(R.id.btn_back)
    ImageView mBtnBack;
    @Bind(R.id.convertional_textview_title)
    TextView mConvertionalTextviewTitle;
    @Bind(R.id.convertional_textview_progress)
    TextView mConvertionalTextviewProgress;
    @Bind(R.id.toobar_bg_image)
    ImageView mToobarBgImage;
    @Bind(R.id.pb)
    ZzHorizontalProgressBar mPb;
    @Bind(R.id.tab_image_wifi)
    ImageView mTabImageWifi;
    @Bind(R.id.message_state)
    TextView mMessageState;
    @Bind(R.id.tab_image_back_state)
    ImageView mTabImageBackState;
    @Bind(R.id.tab_image_front_state)
    ImageView mTabImageFrontState;
    @Bind(R.id.tab_image_communication_state)
    ImageView mTabImageCommunicationState;
    @Bind(R.id.tv)
    TextView mTv;
    @Bind(R.id.tv1)
    TextView mTv1;
    @Bind(R.id.pb1)
    ZzHorizontalProgressBar mPb1;
    @Bind(R.id.tv2)
    TextView mTv2;
    @Bind(R.id.pb2)
    ZzHorizontalProgressBar mPb2;
    @Bind(R.id.progress_container)
    LinearLayout mProgressContainer;
    @Bind(R.id.dc_voltage)
    TextView mDcVoltage;
    @Bind(R.id.dc_voltage_value)
    TextView mDcVoltageValue;
    @Bind(R.id.ac_current)
    TextView mAcCurrent;
    @Bind(R.id.ac_current_value)
    TextView mAcCurrentValue;
    @Bind(R.id.total_number_of_work)
    TextView mTotalNumberOfWork;
    @Bind(R.id.total_number_of_work_value)
    TextView mTotalNumberOfWorkValue;
    @Bind(R.id.tab_Relativelayout_message)
    RelativeLayout mTabRelativelayoutMessage;
    @Bind(R.id.conventional_image_blue)
    ImageView mConventionalImageBlue;
    @Bind(R.id.conventional_image_blue2)
    ImageView mConventionalImageBlue2;
    @Bind(R.id.conventional_image_blue3)
    ImageView mConventionalImageBlue3;
    @Bind(R.id.conventional_image_blue4)
    ImageView mConventionalImageBlue4;
    @Bind(R.id.coventional_num_relativelayout)
    LinearLayout mCoventionalNumRelativelayout;
    @Bind(R.id.conventional_time_blue_image_1)
    ImageView mConventionalTimeBlueImage1;
    @Bind(R.id.conventional_time_blue_image_2)
    ImageView mConventionalTimeBlueImage2;
    @Bind(R.id.conventional_time_blue_image_3)
    ImageView mConventionalTimeBlueImage3;
    @Bind(R.id.conventional_time_blue_image_5)
    ImageView mConventionalTimeBlueImage5;
    @Bind(R.id.coventional_time_relativelayout)
    RelativeLayout mCoventionalTimeRelativelayout;
    @Bind(R.id.coventional_model)
    RelativeLayout mCoventionalModel;
    @Bind(R.id.relativeLayout_coventional_bt)
    RelativeLayout mRelativeLayoutCoventionalBt;
    @Bind(R.id.complete_iv_one)
    ImageView mCompleteIvOne;
    @Bind(R.id.progress_tv_one)
    TextView mProgressTvOne;
    @Bind(R.id.complete_iv_two)
    ImageView mCompleteIvTwo;
    @Bind(R.id.progress_tv_two)
    TextView mProgressTvTwo;
    @Bind(R.id.complete_iv_three)
    ImageView mCompleteIvThree;
    @Bind(R.id.progress_tv_three)
    TextView mProgressTvThree;
    @Bind(R.id.surplus)
    TextView mSurplus;
    @Bind(R.id.conventional_image_blue5)
    ImageView mConventionalImageBlue5;
    @Bind(R.id.conventional_image_blue6)
    ImageView mConventionalImageBlue6;
    @Bind(R.id.temperature_state)
    TextView mTemperatureState;
    @Bind(R.id.videoView_container)
    RelativeLayout mVideoViewContainer;
    @Bind(R.id.bai)
    TextView mBai;


    private boolean mIsRoutine;             //是否是常规模式
    private boolean mIsData = true;
    private boolean isStart = true;
    private boolean isYesData = false;
    private int currentState;
    private String mRiseNumbwe;
    private SharedPreferences sp;
    private Timer mRecordTimer;                 //更新时间的定时器
    private MyTimerTask mMyTimerTask;           //更新时间的任务
    private Timer mTime;                        //定时检查液体的定时器
    private CheckTimeTask mCheckTimeTask;       //定时检查液体的任务
    private boolean isUpdataProgress = true;    //是否更新进度
    private int mAddNumbwe;
    private Timer mChickTimer;
    private CheckTask mCheckTask;
    private int result;
    private int mRoutineOzoneRunTime;            //常规保养第一阶段的时间
    private int mDepthOneRunTime;                //深度保养第一阶段的时间
    private int oneRecordTimeSecond;            //1阶段运行记录的开始时间秒
    private int oneRecordTwoTimeSecond;            //2阶段运行记录的开始时间秒
    private int oneRecordThreeTimeSecond;            //3阶段运行记录的开始时间秒
    private int mRoutineTwoRunTime;
    private int mDepthTwoRunTime;
    private int mRoutineThreeRunTime;
    private int mDepthThreeRunTime;
    private String mDepthWorkNumbwe;
    private String mRoutineWorkNumbwe;
    private Timer mInOneTimer;
    private Timer mInTwoTimer;
    private Timer mInThreeTimer;
    private int totalTime; //常规模式总时间分
    private boolean mBoolean = true;
    private int totalTimecou = 0; //常规模式总时间秒
    private int depthTotalTime;//深度模式总时间分
    private int depthTotalTimeCou = 0;//深度模式总时间miao
    private int totalProgress;//总的进度
    private MyProgressTask mProgressTask;
    private Timer mProgressTimer;
    private boolean isOneStart = true; //是否第一次开始
    private OneTask mOneTask;
    private boolean isStartingTwo; //是否在运行第二阶段
    private boolean isStartingThree; //是否在运行第三阶段
    private boolean isStartingOne; //是否在运行第一阶段
    private TwoTask mTwoTask;
    private ThreeTask mThreeTask;
    private boolean controlOneTaskState = true;         //控制任务的标记
    private boolean controlTwoTaskState = true;
    private boolean controlThreeTaskState = true;
    private boolean controlProgressTaskState = true;
    private boolean controlTimeTaskState = true;
    private boolean isReadComplete = false; //是否准备完成
    private StopDialog mDialog;
    private CompleteDialog mCompleteDialog;
    private boolean isDiaplayTime = true;   //是否显示时间
    private CheckliquidTask mCheckliquidTask;
    private Timer mChickliquidTimer;
    private boolean isCheck = true;
    private AddErrorDialog mErrorDialog;
    private boolean isCompare = true;       //是否比较电子秤的数值
    private boolean isAppearError = false;       //是否出现过通讯异常

    private VideoView mPlear;
    private Dialog mVideoDialog;
    private boolean mIsExeccd = true;              //返回的时候时间是否超过三分钟true表示没有
    private AudioManager mManager;
    private VolumeReceiver mReceiver;
    private int mStreamMaxVolume;
    private int mCurrentVolume;
    private int mKeepTime;
    private File mTimeFile;
    private BufferedWriter mWriter;
    private BufferedReader mReader;
    private int mReadKeepTime;                  //读取的时间没有到3分钟保存的数值
    private int routineTime;                    //常规模式前三分钟记录的时间
    private int depthTime;                    //深度模式前三分钟记录的时间


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conventional_maintenance);
        ButterKnife.bind(this);
        mIsRoutine = getIntent().getBooleanExtra("isRoutine", false);
        sp = getSharedPreferences("old", 0);
        mPb2.setMax(100);
        mDialog = new StopDialog(this, R.style.dialog);
        mCompleteDialog = new CompleteDialog(this, R.style.dialog);
        mErrorDialog = new AddErrorDialog(this, R.style.dialog, R.mipmap.popovers_failed_01);

        initListener();
        initData();
    }


    @Override
    protected void isWifiConnected(boolean b, int level) {
        if (b) {
            if (level <= 0 && level >= -50) {
                mTabImageWifi.setImageResource(R.mipmap.wifi_4);
            } else if (level < -50 && level >= -70) {
                mTabImageWifi.setImageResource(R.mipmap.wifi_3);
            } else if (level < -70 && level >= -80) {
                mTabImageWifi.setImageResource(R.mipmap.wifi_2);
            } else if (level < -80 && level >= -100) {
                mTabImageWifi.setImageResource(R.mipmap.wifi_1);
            } else {
                mTabImageWifi.setImageResource(R.mipmap.top_wifi_off);
            }
        } else {
            mTabImageWifi.setImageResource(R.mipmap.top_wifi_off);
        }
    }

    @Override
    public void onDataReceived(final byte[] buffer, final int size) {
        isYesData = true;
        if (size == Constants.DATA_LONG && buffer[2] == 0x03) {
            analysisData(buffer);
        }
        if (size == Constants.SET_RESULT_LINGTH) {
            setResultData(buffer);
        }
    }

    /**
     * 设置结果
     *
     * @param resultData
     */
    public void setResultData(byte[] resultData) {
        Boolean isSuccess = DataUtil.analysisSetResult(resultData);
        switch (currentState) {
            case 1:                             //开启#4
                if (isSuccess) {
                    mBtnBack.setImageResource(R.mipmap.btn_back_normal_gray);
                    mBtnBack.setClickable(false);
                    isOneStart = false;
                    mPb.setMax(100); //设置回收最大值
                    mBtnStart.setImageResource(R.mipmap.btn_pause_normal);
                    //保存一次剩余的升数
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putInt("oldNumber", Integer.parseInt(mRiseNumbwe, 16));
                    edit.commit();
                    //开启定时器3秒之后比较剩余的升数
                    startCheckTimeTask(false);
                } else {
                    currentState = 1;
                    //开启4#触点
                    SendUtil.open4();
                }
                break;
            case 2:                         //暂停按钮关闭#4
                if (isSuccess) {
                    pauseProgressTask();            //暂停进度
                    pauseRecordTime();              //暂停时间
                    isStart = true;
                    closeOneTask();
                    stopThreeTask();
                    stopTwoTask();
                    mBtnStart.setImageResource(R.mipmap.btn_start_normal);
                } else {
                    currentState = 2;
                    SendUtil.closeAll();
                }
                break;
            case 3:                         //回收完成自动关闭#4
                if (isSuccess) {
                    stopCheckTimeTask();        //停止计时
                    mPb2.setProgress(50);               //回收自动完成之后设置准备的进度为50%
                    mProgressTvThree.setText("50%");
                    setCallBackProgress(100);          //回收完成设置进度为100
                    mCompleteIvOne.setVisibility(View.VISIBLE);
                    mProgressTvOne.setVisibility(View.GONE);
                    isUpdataProgress = false;           //不能更新回收的进度了
                    //延迟3秒执行
                    MyApplication.getmHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            currentState = 4;
                            SendUtil.open2();
                        }
                    }, 3000);
                } else {
                    currentState = 3;
                    SendUtil.closeAll();
                }
                break;
            case 4:                         //打开2#开始加注
                if (isSuccess) {
                    currentState = 11;
                    SendUtil.setWorkState(128);
                } else {
                    currentState = 4;
                    SendUtil.open2();
                }
                break;
            case 11:
                if (isSuccess) {
                    mPb1.setMax(100);
                    //开始加注成功,记录一次剩余的量
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("surplusNumber", mRiseNumbwe);
                    edit.commit();
                    startCheckTask();           //100毫秒检查一次加注量
                } else {
                    currentState = 11;
                    SendUtil.setWorkState(128);
                }
                break;
            case 5:                         //加注完成停止2#          准备工作已经完成,进入运行状态
                if (isSuccess) {
                    //开启第一阶段运行
                    startOne();
                } else {
                    currentState = 5;
                    SendUtil.closeAll();
                }
                break;
            case 6:                     //开启7#8#成功
                if (isSuccess) {
                    isReadComplete = true;
                    mBtnStart.setClickable(true);
                    //开启定时器
                    startRecordTime();
                    startProgressTask();                        //开启更新进度的任务
                    mPb2.setProgress(100);               //加注完成之后设置准备的进度为100%
                    mPb1.setProgress(100);
                    mConvertionalTextviewTitle.setText("正在为爱车清洗杀菌消毒");
                    mProgressTvThree.setText("100%");
                    mCompleteIvThree.setVisibility(View.VISIBLE);
                    mProgressTvThree.setVisibility(View.GONE);
                    mCompleteIvTwo.setVisibility(View.VISIBLE);
                    mProgressTvTwo.setVisibility(View.GONE);
                    currentState = 14;
                    //设置工作状态为正在臭氧杀菌和正在雾化
                    SendUtil.setWorkState(80);
                }
                break;
            case 14:
                if (isSuccess) {
                    if (mIsRoutine) {
                        startOneTask(mRoutineTwoRunTime);
                    } else {
                        startOneTask(mDepthTwoRunTime);
                    }
                } else {
                    currentState = 14;
                    //设置工作状态为正在臭氧杀菌和正在雾化
                    SendUtil.setWorkState(80);
                }
                break;
            case 7:                         //关闭7#成功
                if (isSuccess) {
                    startRecordTime();
                    startProgressTask();                        //开启更新进度的任务
                    mPb2.setProgress(100);               //加注完成之后设置准备的进度为100%
                    mPb1.setProgress(100);
                    mConvertionalTextviewTitle.setText("正在为爱车清洗蒸发箱");
                    mProgressTvThree.setText("100%");
                    mCompleteIvThree.setVisibility(View.VISIBLE);
                    mProgressTvThree.setVisibility(View.GONE);
                    mCompleteIvTwo.setVisibility(View.VISIBLE);
                    mProgressTvTwo.setVisibility(View.GONE);
                    currentState = 15;
                    SendUtil.setWorkState(16);
                } else {
                    currentState = 7;
                    SendUtil.close7();
                }
                break;
            case 15:
                if (isSuccess) {
                    if (mIsRoutine) {
                        startTwoTask();
                    } else {
                        startTwoTask();
                    }
                } else {
                    currentState = 15;
                    SendUtil.setWorkState(16);
                }
                break;
            case 8:
                if (isSuccess) {             //关闭7#8#成功
                    SystemClock.sleep(100);
                    currentState = 9;
                    //打开#6
                    SendUtil.open6();
                } else {
                    currentState = 8;
                    //关闭#7#8
                    SendUtil.closeAll();
                }
                break;
            case 9:                           //打开6#成功
                if (isSuccess) {
                    SystemClock.sleep(100);
                    mConvertionalTextviewTitle.setText("正在为爱车净化车内空气");
                    currentState = 16;
                    if (mIsRoutine) {
                        //设置工作状态正在净化状态
                        SendUtil.setWorkState(32);
                    } else {
                        //设置工作状态正在净化状态
                        SendUtil.setWorkState(32);
                    }
                } else {
                    currentState = 9;
                    //打开#6
                    SendUtil.open6();
                }
                break;
            case 16:
                if (isSuccess) {
                    if (mIsRoutine) {
                        startThreeTask(mRoutineThreeRunTime);
                    } else {
                        startThreeTask(mDepthThreeRunTime);
                    }
                } else {
                    currentState = 16;
                    if (mIsRoutine) {
                        //设置工作状态正在净化状态
                        SendUtil.setWorkState(32);
                    } else {
                        //设置工作状态正在净化状态
                        SendUtil.setWorkState(32);
                    }
                }
                break;
            case 10:
                if (isSuccess) {
                    stopThreeTask();
                    stoptProgressTask();
                    mConvertionalTextviewProgress.setText("100");
                    mBtnStart.setClickable(false);
                    currentState = 13;
                    SendUtil.setWorkState(0);
                } else {
                    currentState = 10;
                    SendUtil.closeAll();
                }
                break;
            case 13:
                if (isSuccess) {
                    mConvertionalTextviewTitle.setText("养护完成");
                    mConvertionalTextviewProgress.setVisibility(View.GONE);
                    mBai.setVisibility(View.GONE);
                    BitmapUtil.numberBlueToBItmapTwo(0, mConventionalTimeBlueImage3, mConventionalTimeBlueImage5);
                    if (mIsRoutine) {
                        if (totalTime >= 2) {
                            stoptProgressTask();
                            stopRecordTime();//停止计时
                            mBtnStart.setClickable(false);
                            mConvertionalTextviewProgress.setText("100");
                            isStartingThree = false;
                            totalProgress = 0;   //初始化总进度
                            isUpdataProgress = true;           //能更新回收的进度了
                            oneRecordTimeSecond = 0; //初始化记录的时间
                            mBtnStart.setImageResource(R.mipmap.btn_start_normal);
                            if (mIsRoutine) {
                                mCompleteDialog.setRes(R.mipmap.popovers_over_01);
                            } else {
                                mCompleteDialog.setRes(R.mipmap.popovers_over_0);
                            }
                            if (!mCompleteDialog.isShowing())
                                mCompleteDialog.show();
                        }
                    } else {
                        if (depthTotalTime >= 2) {
                            stoptProgressTask();
                            stopRecordTime();//停止计时
                            mBtnStart.setClickable(false);
                            mConvertionalTextviewProgress.setText("100");
                            isStartingThree = false;
                            totalProgress = 0;   //初始化总进度
                            isUpdataProgress = true;           //能更新回收的进度了
                            oneRecordTimeSecond = 0; //初始化记录的时间
                            mBtnStart.setImageResource(R.mipmap.btn_start_normal);
                            if (mIsRoutine) {
                                mCompleteDialog.setRes(R.mipmap.popovers_over_01);
                            } else {
                                mCompleteDialog.setRes(R.mipmap.popovers_over_0);
                            }
                            if (!mCompleteDialog.isShowing())
                                mCompleteDialog.show();

                        }
                    }
                    currentState = 0;
                    SendUtil.closeAll();
                    BitmapUtil.numberBlueToBItmapTwo(0, mConventionalTimeBlueImage3, mConventionalTimeBlueImage5);
                    mBtnBack.setImageResource(R.mipmap.btn_back_s);
                    mBtnBack.setClickable(true);
                    if (mPlear != null) {
                        mVideoViewContainer.setVisibility(View.GONE);
                        mPlear.stopPlayback();
                    }
                } else {
                    currentState = 13;
                    SendUtil.setWorkState(0);
                }
                break;
            case 17:
                if (isSuccess) {                 //净化时间段中,打开了4#进行回收
                    startCheckTimeTask(true);
                    if (mInThreeTimer == null)
                        mInThreeTimer = new Timer();
                    if (mThreeTask == null) {
                        if (mIsRoutine) {
                            mThreeTask = new ThreeTask(mRoutineThreeRunTime);
                            mInThreeTimer.schedule(mThreeTask, 0, 1000);
                        } else {
                            mThreeTask = new ThreeTask(mDepthThreeRunTime);
                            mInThreeTimer.schedule(mThreeTask, 0, 1000);
                        }
                    }
                }
                break;
            case 18:
                if (isSuccess) {            //停止
                    mBtnStart.setImageResource(R.drawable.start_btn_selcecter);
                    mDialog.dismiss();
                    //清掉所有状态
                    SendUtil.setWorkState(0);
                    stoptProgressTask();
                    stopRecordTime();//停止计时
                    mBtnStart.setImageResource(R.mipmap.btn_start_normal);
                    mBtnBack.setImageResource(R.mipmap.btn_back_s);
                    mBtnBack.setClickable(true);
                    finish();
                }
                break;
            case 24:
                if (isSuccess) {
                    currentState = 0;
                } else {
                    currentState = 24;
                    if (mIsRoutine) {
                        int i = Integer.parseInt(mRoutineWorkNumbwe, 16);
                        SendUtil.setRoutineNumber(i + 1);
                    } else {
                        int i = Integer.parseInt(mDepthWorkNumbwe, 16);
                        SendUtil.setDepthNumber(i + 1);
                    }
                }
                break;
            case 25:
                break;
            case 19:
                if (isSuccess) {
                    startProgressTask();    //开始进度任务
                    startRecordTime();      //开始时间任务
                    if (mIsRoutine) {            //常规保养模式
                        startOneTask(mRoutineTwoRunTime);
                    } else {                     //深度保养模式
                        startOneTask(mDepthTwoRunTime);
                    }
                    mBtnStart.setImageResource(R.mipmap.btn_pause_normal);
                } else {
                    currentState = 19;
                    //打开7,8
                    SendUtil.open7And8();
                }
                break;
            case 20:
                if (isSuccess) {
                    startProgressTask();    //开始进度任务
                    startRecordTime();      //开始时间任务
                    if (mIsRoutine) {
                        startTwoTask();
                    } else {
                        startTwoTask();
                    }
                    mBtnStart.setImageResource(R.mipmap.btn_pause_normal);
                } else {
                    currentState = 20;
                    SendUtil.open8();
                }
                break;
            case 21:
                if (isSuccess) {
                    startProgressTask();    //开始进度任务
                    startRecordTime();      //开始时间任务
                    if (mIsRoutine) {
                        startThreeTask(mRoutineThreeRunTime);
                    } else {
                        startThreeTask(mDepthThreeRunTime);
                    }
                    mBtnStart.setImageResource(R.mipmap.btn_pause_normal);
                } else {
                    currentState = 21;
                    //打开6*
                    SendUtil.open6();
                }
                break;
            case 22:
                if (isSuccess) {
                    stopCheckTimeTask();
                }
                break;
            case 23:
                if (isSuccess) {
                    mBtnStart.setImageResource(R.mipmap.btn_pause_normal);
                }
                break;
            case 26:
                if (isSuccess) {
                    stopCheckTask();
                    stopCheckliquidTask();
                    if (!mErrorDialog.isShowing()) {
                        mErrorDialog.show();
                        mErrorDialog.setMakeSuperListener(new AddErrorDialog.MakeSuperListener() {
                            @Override
                            public void isMakeSupter(boolean supter) {
                                if (supter) {
                                    mErrorDialog.dismiss();
                                    finish();
                                }
                            }
                        });
                    }
                } else {
                    currentState = 26;
                    SendUtil.closeAll();
                }
                break;
            case 27:
                if (isSuccess) {
                    mConvertionalTextviewTitle.setText("正在为爱车清洗蒸发箱");
                    currentState = 15;
                    SendUtil.setWorkState(16);
                } else {
                    currentState = 27;
                    SendUtil.open8();
                }
                break;
        }
    }
    /*-----------------第一阶段开始---------------------------*/

    /**
     * 开启第一阶段
     */
    private void startOne() {
        isCompare = false;     //开启第一阶段不比较电子秤的值了
        mBtnStart.setImageResource(R.mipmap.btn_pause_normal);
        if (mIsRoutine) {            //常规
            if ((mRoutineOzoneRunTime - mRoutineTwoRunTime) == 0) {             //没有臭氧时间
                currentState = 27;
                SendUtil.open8();
            } else {
                currentState = 6;
                SendUtil.open7And8();
            }
        } else {                     //深度
            if ((mDepthOneRunTime - mDepthTwoRunTime) == 0) {                //没有臭氧时间
                currentState = 27;
                SendUtil.open8();
            } else {
                currentState = 6;
                SendUtil.open7And8();
            }
        }

    }

    /**
     * 开启第一阶段的任务
     *
     * @param time
     */
    private void startOneTask(final int time) {
        if (mPlear != null) {
            mVideoViewContainer.setVisibility(View.VISIBLE);
            mPlear.start();
        }
        currentState = 0;    //防止在运行的同时发送心跳数据返回失败,至0为了在接受到数据时不做任何处理
        controlOneTaskState = true;
        if (mInOneTimer == null)
            mInOneTimer = new Timer();
        if (mOneTask == null) {
            mOneTask = new OneTask(time);
            mInOneTimer.schedule(mOneTask, 0, 1000);
        }
    }

    /**
     * 关闭第一阶段的任务
     */
    private void closeOneTask() {
        if (mInOneTimer != null) {
            mInOneTimer.cancel();
            mInOneTimer = null;
        }
        if (mOneTask != null) {
            mOneTask.cancel();
            mOneTask = null;
        }
    }

    /**
     * 暂停第1个任务
     */
    private void pauseOneTask() {
        controlOneTaskState = false;
    }

    /**
     * 第一阶段的任务
     */
    private class OneTask extends TimerTask {
        private int mTime;

        public OneTask(final int time) {
            mTime = time;
        }

        @Override
        public void run() {
            if (controlOneTaskState) {
                isStartingOne = true;
                if (oneRecordTimeSecond >= (mTime * 60)) {          //第一阶段雾化和臭氧运行时间到
                    //进入第二阶段
                    //关闭#7
                    currentState = 7;
                    SendUtil.close7();
                }
                oneRecordTimeSecond++;
            }
        }

    }
    /*-----------------第一阶段结束---------------------------*/
    /*------------------第二阶段开始--------------------------*/

    /**
     * 开启第二段任务
     */
    private void startTwoTask() {
        currentState = 0;    //防止在运行的同时发送心跳数据返回失败,至0为了在接受到数据时不做任何处理
        controlTwoTaskState = true;
        closeOneTask();
        //设置工作状态正在雾化状态
        if (mInTwoTimer == null)
            mInTwoTimer = new Timer();
        if (mTwoTask == null) {
            mTwoTask = new TwoTask();
            mInTwoTimer.schedule(mTwoTask, 0, 1000);
        }
    }

    /**
     * 停止第二阶段的任务
     */
    private void stopTwoTask() {
        if (mInTwoTimer != null) {
            mInTwoTimer.cancel();
            mInTwoTimer = null;
        }
        if (mTwoTask != null) {
            mTwoTask.cancel();
            mTwoTask = null;
        }
    }

    /**
     * 暂停第一个任务
     */
    private void pauseTwoTask() {
        controlTwoTaskState = false;
    }

    /**
     * 第二阶段的任务
     */
    private class TwoTask extends TimerTask {
        @Override
        public void run() {
            if (controlTwoTaskState) {
                isStartingOne = false;
                isStartingTwo = true;
                if (mIsData) {
                    mIsData = false;
                    if (mIsRoutine) {
                        oneRecordTwoTimeSecond = mRoutineTwoRunTime * 60;
                    } else {
                        oneRecordTwoTimeSecond = mDepthTwoRunTime * 60;
                    }
                }
                if (mIsRoutine) {
                    result = mRoutineOzoneRunTime * 60;
                } else {
                    result = mDepthOneRunTime * 60;
                }
                if (oneRecordTwoTimeSecond >= result) {          //第二阶段雾化运行时间到
                    //进入第三阶段
                    currentState = 8;
                    //关闭#7#8
                    SendUtil.closeAll();
                }
                oneRecordTwoTimeSecond++;
            }
        }
    }
     /*------------------第二阶段结束--------------------------*/
    /*------------------第三阶段开始--------------------------*/

    /**
     * 第三阶段的任务
     *
     * @param time 常规和深度的净化时间
     */
    private void startThreeTask(final int time) {
        currentState = 0;    //防止在运行的同时发送心跳数据返回失败,至0为了在接受到数据时不做任何处理
        controlThreeTaskState = true;
        stopTwoTask();
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt("oldNumber", Integer.parseInt(mRiseNumbwe, 16));
        edit.commit();
        if (!isStartingThree) {     //没有暂停过的逻辑
            currentState = 17;
            SendUtil.open4And6();                   //第三阶段开始之后开启4#进行回收
        } else {                    //暂停过之后的运行逻辑
            if (mInThreeTimer == null)
                mInThreeTimer = new Timer();
            if (mThreeTask == null) {
                mThreeTask = new ThreeTask(time);
                mInThreeTimer.schedule(mThreeTask, 0, 1000);
            }
        }
    }

    /**
     * 停止第三阶段的任务
     */
    private void stopThreeTask() {
        if (mInThreeTimer != null) {
            mInThreeTimer.cancel();
            mInThreeTimer = null;
        }
        if (mThreeTask != null) {
            mThreeTask.cancel();
            mThreeTask = null;
        }
    }

    /**
     * 暂停第三个任务
     */
    private void pauseThreeTask() {
        controlThreeTaskState = false;
    }

    /**
     * 第三阶段的任务
     */
    private class ThreeTask extends TimerTask {
        private int mTime;

        public ThreeTask(final int time) {
            mTime = time;
        }

        @Override
        public void run() {
            if (controlThreeTaskState) {
                isStartingTwo = false;
                isStartingThree = true;
                if (oneRecordThreeTimeSecond >= (mTime * 60)) {          //第三阶段净化运行时间到
                    currentState = 10;
                    SendUtil.closeAll();
                }
                oneRecordThreeTimeSecond++;
            }
        }
    }
    /*------------------第三阶段结束--------------------------*/


    /*--------------------全局进度定时器开始---------------------*/

    /**
     * 进度定时器的任务
     */
    private class MyProgressTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (controlProgressTaskState) {
                        totalProgress++;
                        if (mIsRoutine) {
                            float i = totalTime * 60;
                            mConvertionalTextviewProgress.setText((int) ((totalProgress / i) * 100) + "");
                        } else {
                            float i = depthTotalTime * 60;
                            mConvertionalTextviewProgress.setText((int) ((totalProgress / i) * 100) + "");
                        }
                    }

                }
            });

        }
    }

    /**
     * 开启更新进度的任务
     */
    private void startProgressTask() {
        controlProgressTaskState = true;
        if (mProgressTimer == null)
            mProgressTimer = new Timer();
        if (mProgressTask == null) {
            mProgressTask = new MyProgressTask();
            mProgressTimer.scheduleAtFixedRate(mProgressTask, 0, 1000);
        }
    }

    /**
     * 停止更新进度的任务
     */
    private void stoptProgressTask() {
        if (mProgressTimer != null) {
            mProgressTimer.cancel();
            mProgressTimer = null;
        }
        if (mProgressTask != null) {
            mProgressTask.cancel();
            mProgressTask = null;
        }
    }

    /**
     * 暂停进度的任务
     */
    private void pauseProgressTask() {
        controlProgressTaskState = false;
    }
    /*--------------------全局进度定时器结束---------------------*/
    /*--------------------全局时间定时器开始---------------------*/

    /**
     * 开启定时器
     */
    private void startRecordTime() {
        controlTimeTaskState = true;
        if (mRecordTimer == null)
            mRecordTimer = new Timer();
        if (mMyTimerTask == null) {
            mMyTimerTask = new MyTimerTask();
            mRecordTimer.schedule(mMyTimerTask, 0, 1000);
        }
    }

    /**
     * 停止定时器
     */
    private void stopRecordTime() {
        if (mRecordTimer != null) {
            mRecordTimer.cancel();
            mRecordTimer = null;
        }
        if (mMyTimerTask != null) {
            mMyTimerTask.cancel();
            mMyTimerTask = null;
        }
    }

    /**
     * 暂停时间
     */
    private void pauseRecordTime() {
        controlTimeTaskState = false;
    }

    /**
     * 时间定时器的任务
     */
    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (controlTimeTaskState) {
                        if (mIsRoutine) {
                            if (totalTimecou == 0) {
                                if (mBoolean) {
                                    mBoolean = false;
                                    mKeepTime = keepTime(totalTime);
                                }
                                if (mKeepTime > 0) {
                                    mKeepTime--;
                                    totalTimecou = 60;
                                    BitmapUtil.numberBlueToBItmapTwo(mKeepTime, mConventionalTimeBlueImage1, mConventionalTimeBlueImage2);
                                } else {
                                    totalTimecou = 0;
                                }
                            }
                            if (totalTimecou > 0)
                                totalTimecou--;
                            BitmapUtil.numberBlueToBItmapTwo(totalTimecou, mConventionalTimeBlueImage3, mConventionalTimeBlueImage5);
                            if ((totalTime - 3) < mKeepTime) {
                                routineTime++;
                                mIsExeccd = false;
                            }
                            if ((totalTime - 3) == mKeepTime && totalTimecou == 1) {
                                mIsExeccd = true;
                                currentState = 24;
                                //记录一次次数
                                int i = Integer.parseInt(mRoutineWorkNumbwe, 16);
                                SendUtil.setRoutineNumber(i + 1);
                            }
                            if (totalTimecou == 0 && mKeepTime == 0) {
                                //时间到
                                currentState = 10;
                                SendUtil.closeAll();
                                stoptProgressTask();
                                mConvertionalTextviewProgress.setText("100");
                            }
                        } else {
                            if (depthTotalTimeCou == 0) {
                                if (mBoolean) {
                                    mBoolean = false;
                                    mKeepTime = keepTime(depthTotalTime);
                                }
                                if (mKeepTime > 0) {
                                    mKeepTime--;
                                    BitmapUtil.numberBlueToBItmapTwo(mKeepTime, mConventionalTimeBlueImage1, mConventionalTimeBlueImage2);
                                    depthTotalTimeCou = 60;
                                } else {
                                    depthTotalTimeCou = 0;
                                }
                            }
                            if (depthTotalTimeCou > 0)
                                depthTotalTimeCou--;
                            BitmapUtil.numberBlueToBItmapTwo(depthTotalTimeCou, mConventionalTimeBlueImage3, mConventionalTimeBlueImage5);
                            if ((depthTotalTime - 3) < mKeepTime) {
                                depthTime++;
                                mIsExeccd = false;
                            }
                            if ((depthTotalTime - 3) == mKeepTime && depthTotalTimeCou == 1) {
                                mIsExeccd = true;
                                currentState = 24;
                                //记录一次次数
                                int i = Integer.parseInt(mDepthWorkNumbwe, 16);
                                SendUtil.setDepthNumber(i + 1);
                            }

                            if (depthTotalTimeCou == 0 && mKeepTime == 0) {
                                //时间到
                                currentState = 10;
                                SendUtil.closeAll();
                                stoptProgressTask();
                                mConvertionalTextviewProgress.setText("100");
                            }
                        }
                    }
                }
            });
        }

        private int keepTime(int time) {
            SharedPreferences tag = getSharedPreferences("tag", 0);
            SharedPreferences.Editor edit = tag.edit();
            edit.putInt("int", time);
            edit.commit();
            int anInt = tag.getInt("int", 0);
            return anInt;
        }
    }
    /*--------------------全局时间定时器结束---------------------*/
    /*--------------------定时检查加注任务开始---------------------*/

    /**
     * 定时检查加注是否完成的任务
     */
    private class CheckTask extends TimerTask {
        @Override
        public void run() {
            String surplusNumber = sp.getString("surplusNumber", "");
            if (!TextUtils.isEmpty(surplusNumber)) {
                final float parseInt = Integer.parseInt(surplusNumber, 16);     //剩余
                final float currentNumber = Integer.parseInt(mRiseNumbwe, 16);  //当前
                final float data = (parseInt - currentNumber) / mAddNumbwe * 100;
                if ((parseInt - currentNumber) >= mAddNumbwe) {
                    currentState = 5;
                    SendUtil.closeAll();
                    stopCheckTask();
                    stopCheckliquidTask();
                }else {
                    //第一次保存的量减去当前的剩余量的
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPb1.setProgress((int) data);
                            mProgressTvTwo.setText((int) data + "%");
                        }
                    });
                }
                if (isCheck) {
                    isCheck = false;
                    startCheckliquidTask();    //5秒一次检查电子秤数值是否有变化,如果没有变化就加注异常
                }
            }

        }
    }

    /**
     * 开始加注
     */
    private void startCheckTask() {
        currentState = 0;    //防止在运行的同时发送心跳数据返回失败,至0为了在接受到数据时不做任何处理
        mBtnStart.setClickable(false);
        if (mChickTimer == null)
            mChickTimer = new Timer();
        if (mCheckTask == null) {
            mCheckTask = new CheckTask();
            mChickTimer.scheduleAtFixedRate(mCheckTask, 0, 100);
        }
    }

    /**
     * 停止加注
     */
    private void stopCheckTask() {
        if (mChickTimer != null) {
            mChickTimer.cancel();
            mChickTimer = null;
        }
        if (mCheckTask != null) {
            mCheckTask.cancel();
            mCheckTask = null;
        }
    }
    /*--------------------定时检查加注任务结束---------------------*/
    /*--------------------加注的时候检查液体的任务开始---------------------*/

    /**
     * 定时检查电子秤的值是否有变化的任务
     */
    private class CheckliquidTask extends TimerTask {

        @Override
        public void run() {
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("checkNumber", mRiseNumbwe);
            edit.commit();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String surplusNumber = sp.getString("checkNumber", "");
            final float parseInt = Integer.parseInt(surplusNumber, 16);
            final float currentNumber = Integer.parseInt(mRiseNumbwe, 16);
            if (isCompare) {
                if ((parseInt - currentNumber) <= 10) {           //电子秤的数值5秒之后的变化量小于10毫升,加注异常
                    currentState = 26;
                    SendUtil.closeAll();
                }
            }

        }
    }

    /**
     * 开始检查电子秤的值是否有变化
     */
    private void startCheckliquidTask() {
        currentState = 0;    //防止在运行的同时发送心跳数据返回失败,至0为了在接受到数据时不做任何处理
        if (mChickliquidTimer == null)
            mChickliquidTimer = new Timer();
        if (mCheckliquidTask == null) {
            mCheckliquidTask = new CheckliquidTask();
            mChickliquidTimer.scheduleAtFixedRate(mCheckliquidTask, 2500, 2500);
        }
    }

    /**
     * 停止检查电子秤的值是否有变化
     */
    private void stopCheckliquidTask() {
        if (mChickliquidTimer != null) {
            mChickliquidTimer.cancel();
            mChickliquidTimer = null;
        }
        if (mCheckliquidTask != null) {
            mCheckliquidTask.cancel();
            mCheckliquidTask = null;
        }
    }

    /*--------------------加注的时候检查液体的任务结束---------------------*/
   /*--------------------定时检查排空任务开始---------------------*/

    /**
     * 开启检查液体定时器
     */
    private void startCheckTimeTask(boolean b) {
        currentState = 0;    //防止在运行的同时发送心跳数据返回失败,至0为了在接受到数据时不做任何处理
        if (b) {
            mBtnStart.setClickable(true);
        } else {
            mBtnStart.setClickable(false);
        }
        if (mTime == null)
            mTime = new Timer();
        if (mCheckTimeTask == null) {
            mCheckTimeTask = new CheckTimeTask(b);
            mTime.scheduleAtFixedRate(mCheckTimeTask, 8000, 3000);
        }
    }

    /**
     * 停止检查液体定时器
     */
    private void stopCheckTimeTask() {
        if (mTime != null) {
            mTime.cancel();
            mTime = null;
        }
        if (mCheckTimeTask != null) {
            mCheckTimeTask.cancel();
            mCheckTimeTask = null;
        }
    }


    /**
     * 定时检查液位的任务
     */
    private class CheckTimeTask extends TimerTask {
        private boolean isThree;

        public CheckTimeTask(boolean b) {
            isThree = b;
        }

        @Override
        public void run() {
            float oldNumber = sp.getInt("oldNumber", 0);
            float vount = Integer.parseInt(mRiseNumbwe, 16);
            final float result = (oldNumber / vount) * 100;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mPb.setProgress((int) result);
                    mProgressTvOne.setText((int) result + "%");
                }
            });
            if ((vount - oldNumber) < 10) {     //剩余的液体和3秒之前的一样,回收结束,关闭#4
                if (isThree) {
                    currentState = 22;
                    SendUtil.open6();
                } else {
                    currentState = 3;
                    SendUtil.closeAll();
                }
            } else {
                SharedPreferences.Editor edit = sp.edit();
                edit.putInt("oldNumber", (int) vount);
                edit.commit();
            }
        }
    }
    /*--------------------定时检查排空任务结束---------------------*/

    /**
     * 设置回收进度
     *
     * @param progress
     */
    private void setCallBackProgress(int progress) {
        if (isUpdataProgress) {
            mPb.setProgress(progress);
            mProgressTvOne.setText(progress + "%");
        }
    }

    /**
     * 数据是否发送成功
     *
     * @param isdata
     */
    @Override
    protected void isYesData(boolean isdata) {
        if (isdata && isYesData) {        //成功
            mMessageState.setText("正常");
            mMessageState.setTextColor(Color.parseColor("#fd0fc602"));
            if (isStartingOne && isAppearError) {             //第一阶段长在运行了
                currentState = 19;
                //打开7,8
                startOne();
            }
            if (isStartingTwo && isAppearError) {
                //打开8
                currentState = 20;
                SendUtil.open8();
            }
            if (isStartingThree && isAppearError) {
                currentState = 21;
                //打开6*
                SendUtil.open6();
            }
            isAppearError = false;
        } else {             //失败
            isAppearError = true;
            mMessageState.setText("断开");
            mMessageState.setTextColor(Color.parseColor("#fdfa0310"));
            if (isStartingOne) {
                pauseOneTask();         //暂停第一个任务
            }
            if (isStartingTwo) {
                //打开8,只要关闭7#就可以,前面7.8是一起开启的
                pauseTwoTask();
            }
            if (isStartingThree) {
                //打开6*
                pauseThreeTask();
            }
            pauseProgressTask();            //暂停进度
            pauseRecordTime();              //暂停时间
        }
        isYesData = false;
    }

    private void analysisData(byte[] buffer) {
        int electricPress = DataUtil.directElectricPress(buffer);      //直流电压
        float communionFlow = DataUtil.directCommunionFlow(buffer);       //交流电流
        String state = DataUtil.getState(buffer);                           //状态位
        String adNumbwe = DataUtil.getADNumbwe(buffer);                     //电子秤AD值
        String ratioNumbwe = DataUtil.getRatioNumbwe(buffer);               //电子秤系数值
        //获取液体剩余升数
        mRiseNumbwe = DataUtil.getRiseNumbwe(buffer);
        //药液量
        int mResultRatioNumbwe = Integer.parseInt(mRiseNumbwe, 16);
        //设置剩余量
        mSurplus.setText(mResultRatioNumbwe + "ml");
        String riseTotelNumbwe = DataUtil.getRiseTotelNumbwe(buffer);       //液体加注总升数
        //获取深度保养次数
        mDepthWorkNumbwe = DataUtil.getDepthWorkNumbwe(buffer);
        //获取常规保养次数
        mRoutineWorkNumbwe = DataUtil.getRoutineWorkNumbwe(buffer);

        //获取加注量
        mAddNumbwe = DataUtil.getAddNumbwe(buffer) * 10;
        //常规模式第一阶段臭氧和雾化共同运行时间
        mRoutineOzoneRunTime = DataUtil.getRoutineOzoneRunTime(buffer);
        //常规模式第二阶段雾化运行时间
        mRoutineTwoRunTime = DataUtil.getRoutineTwoRunTime(buffer);
        //常规模式第三阶段净化运行时间
        mRoutineThreeRunTime = DataUtil.getRoutineThreeRunTime(buffer);
        totalTime = mRoutineOzoneRunTime + mRoutineThreeRunTime;

        //深度模式第一阶段臭氧和雾化共同运行时间
        mDepthOneRunTime = DataUtil.getDepthOneRunTime(buffer);
        //深度模式第二阶段雾化运行时间
        mDepthTwoRunTime = DataUtil.getDepthTwoRunTime(buffer);
        //深度模式第三阶段净化运行时间
        mDepthThreeRunTime = DataUtil.getDepthThreeRunTime(buffer);
        depthTotalTime = mDepthOneRunTime + mDepthThreeRunTime;
        if (isDiaplayTime) {
            isDiaplayTime = false;
            try {
                mReader = new BufferedReader(new FileReader(mTimeFile));
                String s = mReader.readLine();
                if (!TextUtils.isEmpty(s) && !"".equals(s)) {
                    mReadKeepTime = Integer.parseInt(s);
                    if (mReadKeepTime >= 180){          //多次3分钟之前停止累计时间大于或等于三分钟就记录一次次数
                        if (mIsRoutine){
                            currentState = 24;
                            //记录一次次数
                            int i = Integer.parseInt(mRoutineWorkNumbwe, 16);
                            SendUtil.setRoutineNumber(i + 1);
                        }else {
                            currentState = 24;
                            //记录一次次数
                            int i = Integer.parseInt(mDepthWorkNumbwe, 16);
                            SendUtil.setDepthNumber(i + 1);
                        }
                        //删除记录次数的文件
                        if (mTimeFile.exists()){
                            mTimeFile.delete();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (mReader != null) {
                    try {
                        mReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (mIsRoutine) {
                BitmapUtil.numberBlueToBItmapTwo(totalTime, mConventionalTimeBlueImage1, mConventionalTimeBlueImage2);
            } else {
                BitmapUtil.numberBlueToBItmapTwo(depthTotalTime, mConventionalTimeBlueImage1, mConventionalTimeBlueImage2);
            }
        }
        int signalStrength = DataUtil.getSignalStrength(buffer);            //获取信号强度

        //设置电压
        mDcVoltageValue.setText((electricPress) + "V");
        //交流电流
        mAcCurrentValue.setText((communionFlow / 10) + "A");
        if (mIsRoutine) {
            mTotalNumberOfWorkValue.setText(Integer.parseInt(mRoutineWorkNumbwe, 16) + "次");
        } else {
            mTotalNumberOfWorkValue.setText(Integer.parseInt(mDepthWorkNumbwe, 16) + "次");
        }
        String depthWorkNumbwe = DataUtil.getDepthWorkNumbwe(buffer);       //获取深度保养次数
        String routineWorkNumbwe = DataUtil.getRoutineWorkNumbwe(buffer);   //获取常规保养次数
        if (mIsRoutine) {
            BitmapUtil.numberToBItmapBlaueSix(Integer.parseInt(routineWorkNumbwe, 16), mConventionalImageBlue6, mConventionalImageBlue5, mConventionalImageBlue4,
                    mConventionalImageBlue3, mConventionalImageBlue2, mConventionalImageBlue);
        } else {
            BitmapUtil.numberToBItmapBlaueSix(Integer.parseInt(depthWorkNumbwe, 16), mConventionalImageBlue6, mConventionalImageBlue5, mConventionalImageBlue4,
                    mConventionalImageBlue3, mConventionalImageBlue2, mConventionalImageBlue);
        }
        String connectServerState = state.substring(3, 4);                  //连接服务器状态
        String activationState = state.substring(4, 5);                     //设备激活状态
        String lockState = state.substring(5, 6);                           //设备锁定状态
        //00011000
        String afterLockState = state.substring(1, 2);                       //后门锁定状态
        String frontLockState = state.substring(2, 3);                       //前门锁定状态
        String temperatureState = state.substring(0, 1);                       //温度传感器连接状态
        if (temperatureState.equals("1")) {
            mTemperatureState.setText("- -");
        } else {
            int temperature = DataUtil.getTemperature(buffer);                  //温度
            mTemperatureState.setText(temperature + "℃");
        }
        //00011001
        if (frontLockState.equals("1")) {
            mTabImageBackState.setImageResource(R.mipmap.icon_on);
        } else {
            mTabImageBackState.setImageResource(R.mipmap.icon_off);
        }
        if (afterLockState.equals("1")) {
            mTabImageFrontState.setImageResource(R.mipmap.icon_on);
        } else {
            mTabImageFrontState.setImageResource(R.mipmap.icon_off);
        }
        if (connectServerState.equals("1")) {
            //TODO 已经连接服务器
        } else {
            //TODO 未连接
        }
        if (activationState.equals("1")) {
            //TODO 已经激活
        } else {
            //TODO 未激活
        }
        if (lockState.equals("1")) {
            //TODO 已经锁定
        } else {
            //TODO 未锁定
        }
        //是否连接服务器
        if (connectServerState.equals("0")) {
            mTabImageCommunicationState.setImageResource(R.mipmap.top_icon_4_off);
        } else {
            //设置信号强度
            switch (signalStrength) {
                case 1:
                    mTabImageCommunicationState.setImageResource(R.mipmap.top_icon_1);
                    break;
                case 2:
                    mTabImageCommunicationState.setImageResource(R.mipmap.top_icon_2);
                    break;
                case 3:
                    mTabImageCommunicationState.setImageResource(R.mipmap.top_icon_3);
                    break;
                case 4:
                    mTabImageCommunicationState.setImageResource(R.mipmap.top_icon_4);
                    break;
                default:
                    mTabImageCommunicationState.setImageResource(R.mipmap.top_icon_4_off);
                    break;
            }
        }
    }

    private void initData() {
        if (mIsRoutine) {
            mToobarBgImage.setImageResource(R.mipmap.top_bar_1);
            mConvertionalTextviewTitle.setText("请点击开始按钮项目开始施工");
        } else {
            mToobarBgImage.setImageResource(R.mipmap.top_bar_2);
            mConvertionalTextviewTitle.setText("请点击开始按钮项目开始施工");
        }
        mReceiver = new VolumeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        registerReceiver(mReceiver, filter);

        initVideoView();
        initSystemAudio();
    }

    /**
     * 设置系统音量
     */
    private void initSystemAudio() {
        mManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //最大音量
        mStreamMaxVolume = mManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        //获取当前音量
        mCurrentVolume = mManager.getStreamVolume(AudioManager.STREAM_MUSIC);

    }

    private class VolumeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
                mCurrentVolume = mManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            }
        }
    }

    /**
     * 视屏播放
     */
    private void initVideoView() {
        mPlear = (VideoView) this.findViewById(R.id.videoView);
        final String uri = "";//android.resource://" + getPackageName() + "/" + R.raw.info;

        mPlear.setVideoURI(Uri.parse(uri));
        mPlear.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlear.setVideoPath(uri);
                mPlear.start();
            }
        });
        mPlear.setOnTouchListener(new View.OnTouchListener() {
            public float mUpX;
            public float mUpY;
            private float mMoveX;
            private float mMoveY;
            private float mDownY;
            private float mDownX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mDownY = event.getY();
                        mDownX = event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mMoveY = event.getY();
                        mMoveX = event.getX();
                        if ((mMoveY - mDownY) < 0) {    //手指向上滑动
                            if (mCurrentVolume < mStreamMaxVolume) {
                                mCurrentVolume++;
                                mManager.setStreamVolume(AudioManager.STREAM_MUSIC, mCurrentVolume, 0);
                            } else {
                                mManager.setStreamVolume(AudioManager.STREAM_MUSIC, mStreamMaxVolume, 0);
                            }
                        } else {                          //手指向下滑动
                            if (mCurrentVolume > 0) {
                                mCurrentVolume--;
                                mManager.setStreamVolume(AudioManager.STREAM_MUSIC, mCurrentVolume, 0);
                            } else {
                                mManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        mUpY = event.getY();
                        mUpX = event.getX();
                        if ((mDownY - mUpY) > 50) {        //手指向上滑动


                        } else if ((mUpY - mDownY) > 50) {  //手指向下滑动


                        } else {
                            if (mVideoDialog != null && mVideoDialog.isShowing()) {
                                if (mPlear.isPlaying()) {
                                    mPlear.pause();
                                } else {
                                    mPlear.start();
                                }

                            } else {
                                RelativeLayout parent = (RelativeLayout) mPlear.getParent();
                                parent.removeView(mPlear);
                                mVideoDialog = new Dialog(ConventionalMaintenanceActivity.this, R.style.dialog);
                                mVideoDialog.setContentView(mPlear);
                                Window mwindow = mVideoDialog.getWindow();
                                WindowManager.LayoutParams lp = mwindow.getAttributes();
                                lp.dimAmount = 0f;
                                lp.height = 630;
                                lp.width = 1055;
                                mwindow.setAttributes(lp);
                                mVideoDialog.show();
                                mPlear.start();
                                mVideoDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        mPlear.stopPlayback();
                                        ViewGroup parent = (ViewGroup) mPlear.getParent();
                                        parent.removeView(mPlear);
                                        mVideoViewContainer.addView(mPlear);
                                        if (mPlear.isPlaying()) {
                                            mPlear.pause();
                                        } else {
                                            mPlear.start();
                                        }
                                    }
                                });

                            }
                        }

                        break;
                }
                return true;
            }
        });
    }

    private void initListener() {
        mBtnStart.setOnClickListener(this);
        mBtnStop.setOnClickListener(this);
        mBtnBack.setOnClickListener(this);
        mTimeFile = new File(Environment.getExternalStorageDirectory(), "time.txt");
        if (!mTimeFile.exists()) {
            try {
                mTimeFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                if (isStart) {
                    if (!TextUtils.isEmpty(mRiseNumbwe)) {
                        isStart = false;
                        if (isOneStart) {           //是否第一次点击开始按钮   ,true表示没有暂停过
                            if (mIsRoutine) {
                                mConvertionalTextviewTitle.setText("正在常规养护模式");
                            } else {
                                mConvertionalTextviewTitle.setText("正在深度养护模式");
                            }
                            mConvertionalTextviewProgress.setVisibility(View.VISIBLE);
                            mConvertionalTextviewProgress.setText("0");
                            mBai.setVisibility(View.VISIBLE);
                            currentState = 1;
                            //开启4#触点
                            SendUtil.open4();
                        } else {
                            if (isStartingOne) {             //第一阶段长在运行了
                                currentState = 19;
                                //打开7,8
                                startOne();
                            }
                            if (isStartingTwo) {
                                //打开8
                                currentState = 20;
                                SendUtil.open8();
                            }
                            if (isStartingThree) {
                                currentState = 21;
                                //打开6*
                                SendUtil.open6();
                            }
                        }
                    }
                } else {
                    currentState = 2;
                    if (isStartingOne) {
                        pauseOneTask();         //暂停第一个任务
                    }
                    if (isStartingTwo) {
                        //打开8,只要关闭7#就可以,前面7.8是一起开启的
                        pauseTwoTask();
                    }
                    if (isStartingThree) {
                        //打开6*
                        pauseThreeTask();
                    }
                    SendUtil.closeAll();
                }
                break;
            case R.id.btn_stop:
                if (isReadComplete) {
                    mDialog.setBitmap(R.mipmap.popovers_hint);
                    mDialog.show();
                    mDialog.setOnMakeSuerListener(new StopDialog.OnMakeSuerListener() {
                        @Override
                        public void isMakeUser(boolean b) {
                            if (b) {
                                if (mPlear != null)
                                    mPlear.stopPlayback();
                                currentState = 18;
                                SendUtil.closeAll();
                            }
                        }
                    });
                } else {
                    ToastUtil.showMessage("等待准备完成");
                }
                break;
            case R.id.btn_back:
                finish();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        stopRecordTime();
        stopCheckTimeTask();
        stopCheckTask();
        stoptProgressTask();
        stopTwoTask();
        closeOneTask();
        stopThreeTask();
        stopCheckliquidTask();
        SendUtil.closeAll();
        if (mInThreeTimer != null) {
            mInThreeTimer.cancel();
            mInThreeTimer = null;
        }
        if (mInOneTimer != null) {
            mInOneTimer.cancel();
            mInOneTimer = null;
        }
        if (mInTwoTimer != null) {
            mInTwoTimer.cancel();
            mInTwoTimer = null;
        }
        isStart = true;
        isOneStart = true;
        try {
            if (!mIsExeccd) {                        //返回的时候时间没有超过三分钟,就是没有到记录次数的时间
                if (mIsRoutine) {                    //常规模式
                    int keepResultTime = routineTime + mReadKeepTime;
                    mWriter = new BufferedWriter(new FileWriter(mTimeFile));
                    mWriter.write(keepResultTime + "");
                } else {                             //深度模式
                    int keepResultTime = depthTime + mReadKeepTime;
                    mWriter = new BufferedWriter(new FileWriter(mTimeFile));
                    mWriter.write(keepResultTime + "");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (mWriter != null) {
                try {
                    mWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
