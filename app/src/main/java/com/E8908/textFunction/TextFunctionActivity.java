package com.E8908.textFunction;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.base.BaseActivity;
import com.E8908.conf.Constants;
import com.E8908.util.DataUtil;
import com.E8908.util.SendUtil;
import com.E8908.widget.ToastUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TextFunctionActivity extends BaseActivity implements View.OnClickListener, RuningStateDialog.OnPauseBtnListener, SettingsDialog.OnDataChangeListener {

    private static final String TAG = "TextFunctionActivity";
    @Bind(R.id.toobar_bg_image)
    ImageView mToobarBgImage;
    @Bind(R.id.temperature_state)
    TextView mTemperatureState;
    @Bind(R.id.message_state)
    TextView mMessageState;
    @Bind(R.id.tab_image_back_state)
    ImageView mTabImageBackState;
    @Bind(R.id.tab_image_front_state)
    ImageView mTabImageFrontState;
    @Bind(R.id.tab_image_communication_state)
    ImageView mTabImageCommunicationState;
    @Bind(R.id.tab_image_wifi)
    ImageView mTabImageWifi;
    @Bind(R.id.back)
    LinearLayout mBack;
    @Bind(R.id.wuhua)
    LinearLayout mWuhua;
    @Bind(R.id.shajun)
    LinearLayout mShajun;
    @Bind(R.id.jinhua)
    LinearLayout mJinhua;
    @Bind(R.id.jiazhu)
    LinearLayout mJiazhu;
    @Bind(R.id.huishou)
    LinearLayout mHuishou;
    @Bind(R.id.xunhuan)
    LinearLayout mXunhuan;
    @Bind(R.id.before_lock)
    ImageView mBeforeLock;
    @Bind(R.id.after_lock)
    ImageView mAfterLock;
    @Bind(R.id.settings)
    LinearLayout mSettings;
    @Bind(R.id.loop_count)
    TextView mLoopCountTv;
    @Bind(R.id.function)
    TextView mFunctionTv;
    @Bind(R.id.updata_text_tab)
    Button mUpdataTextTab;
    @Bind(R.id.dtu)
    LinearLayout mDtu;
    private int mSendState;
    private int mJiazhuTime = 40;         //加注800ML30秒内完成
    private int mAddCount = 800;          //800ML
    private int mAddTimeTimp = mJiazhuTime;              //临时记录的加注时间,用于在每次点击加注时初始化

    private int mBackSuccessTime = 20;    //回收20秒有300ML表示成功
    private int mBackCount = 300;         //回收300ML
    private int mBackTimeTimp = mBackSuccessTime;              //临时记录的回收时间,用于在每次点击回收时初始化

    private int mWuhuaTime = 30;                //雾化时间
    private int mShaJun = 10;                   //杀菌时间
    private int mJIngHua = 10;                   //净化时间
    private float mWuhuaA = 2.1f;                //雾化时默认的电流
    private float mShajunA = 0.7f;                //杀菌时默认的电流
    private float mJinghuaA = 0.3f;                //净化时默认的电流

    private boolean mIsYesData = false;
    private RuningStateDialog mDialog;
    private String mAdNumbwe;
    private SharedPreferences mRecordAd;        //用于在在雾化时记录电子秤数值
    private Timer mCheckTimer;                  //定时检查电子秤
    private float mCommunionFlow;
    private boolean mIsBefore;
    private boolean mIsAfter;
    private SettingsDialog mSettingDialog;

    private boolean isLoopruning = false;           //是否是循环测试
    private int mLoopCount = 0;                     //循环测试的循环次数

    private boolean isAddFail = false;              //加注功能是否失败
    private boolean isBackFail = false;              //回收功能是否失败
    private boolean isWuhuaFail = false;              //雾化功能是否失败
    private boolean isShajunFail = false;              //杀菌功能是否失败
    private boolean isJinghuaFail = false;              //净化功能是否失败
    private boolean isServiceFail = false;              //服务器通讯是否正常
    private boolean isDTUFail = false;              //DTU通讯是否正常
    private UpDataTextTabDialog mUpDataTextTabDialog;
    private String mEquipmentNumber;
    private RunTaskManage mManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_function);
        ButterKnife.bind(this);
        //运行状态
        mDialog = new RuningStateDialog(this, R.style.dialog);
        //上传报告
        mUpDataTextTabDialog = new UpDataTextTabDialog(this, R.style.dialog);
        //参数设置
        mSettingDialog = new SettingsDialog(this, R.style.dialog, mJiazhuTime, mAddCount,
                mBackSuccessTime, mBackCount, mWuhuaTime, mShaJun, mJIngHua, mWuhuaA, mShajunA, mJinghuaA);
        mSettingDialog.setOnDataChangeListener(this);

        mDialog.setOnPauseBtnListener(this);
        mRecordAd = getSharedPreferences("adCound", 0);

        mManage = new RunTaskManage(mHandler);
        initData();
        initListener();
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
    protected void isYesData(boolean isdata,boolean isCharging) {
        if (isdata && mIsYesData) {        //成功
            if (isCharging){
                mMessageState.setText("正常");
                mMessageState.setTextColor(Color.parseColor("#fd0fc602"));
            }else {
                mMessageState.setText("正常");
                mMessageState.setTextColor(Color.parseColor("#fdfa0310"));
            }
        } else {             //失败
            mMessageState.setText("断开");
            mMessageState.setTextColor(Color.parseColor("#fdfa0310"));
        }
        mIsYesData = false;
    }

    @Override
    public void onDataReceived(byte[] buffer, int size) {
        mIsYesData = true;
        if (size == Constants.DATA_LONG && buffer[2] == 0x03) {
            analysisData(buffer);
        }
        if (size == Constants.SET_RESULT_LINGTH) {
            setResultData(buffer);
        }
    }

    private void analysisData(byte[] buffer) {

        String state = DataUtil.getState(buffer);                           //状态位
        //电子秤剩余药液升
        mAdNumbwe = DataUtil.getRiseNumbwe(buffer);                 //获取液体升数
        //获取序列号
        mEquipmentNumber = DataUtil.getEquipmentNumber(buffer);
        //交流电流
        mCommunionFlow = DataUtil.directCommunionFlow(buffer);
        mCommunionFlow = mCommunionFlow / 10;
        mManage.setCurrentA(mCommunionFlow);
        if (mDialog.isShowing())
            mDialog.setNumber(mAdNumbwe);

        int signalStrength = DataUtil.getSignalStrength(buffer);            //获取信号强度

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
            mIsBefore = true;
            mBeforeLock.setImageResource(R.mipmap.text_before);
            mTabImageBackState.setImageResource(R.mipmap.icon_on);
        } else {
            mIsBefore = false;
            mBeforeLock.setImageResource(R.mipmap.text_before_lock);
            mTabImageBackState.setImageResource(R.mipmap.icon_off);
        }
        if (afterLockState.equals("1")) {
            mIsAfter = true;
            mAfterLock.setImageResource(R.mipmap.text_before);
            mTabImageFrontState.setImageResource(R.mipmap.icon_on);
        } else {
            mIsAfter = false;
            mAfterLock.setImageResource(R.mipmap.text_before_lock);
            mTabImageFrontState.setImageResource(R.mipmap.icon_off);
        }
        if (connectServerState.equals("1")) {
            //TODO 已经连接服务器
            isServiceFail = false;
        } else {
            //TODO 未连接
            isServiceFail = true;
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

    private void setResultData(byte[] buffer) {
        Boolean isSuccess = DataUtil.analysisSetResult(buffer);
        switch (mSendState) {
            case 1:                         //开启雾化时的加注
                if (isSuccess) {
                    mSendState = 0;
                    mDialog.setCurrentRuningState(1);
                    if (!mDialog.isShowing()) {
                        mDialog.show();
                    }
                    //记录电子秤数值
                    SharedPreferences.Editor edit = mRecordAd.edit();
                    edit.putString("initAd", mAdNumbwe);
                    edit.apply();
                    startCheckAd(false);
                    mDialog.setRunText("正在进行加注");
                    mDialog.setPauseClickable(false);
                } else {
                    SendUtil.open2();
                }
                break;
            case 2:                         //加注完成开始雾化,雾化工作一分钟
                if (isSuccess) {
                    mSendState = 0;
                    mDialog.setCurrentRuningState(2);
                    mDialog.setPauseClickable(true);
                    mManage.startAddTask(mWuhuaTime, "雾化", mWuhuaA);
                } else {
                    SendUtil.open8();
                }
                break;
            case 3:                         //运行杀菌
                if (isSuccess) {
                    mSendState = 0;
                    mDialog.setCurrentRuningState(3);
                    if (!mDialog.isShowing()) {
                        mDialog.show();
                    }
                    mDialog.setPauseClickable(true);
                    mManage.startAddTask(mShaJun, "杀菌", mShajunA);
                } else {
                    SendUtil.open7();
                }
                break;
            case 4:                         //运行净化
                if (isSuccess) {
                    mSendState = 0;
                    mDialog.setCurrentRuningState(4);
                    if (!mDialog.isShowing()) {
                        mDialog.show();
                    }
                    mDialog.setPauseClickable(true);
                    mManage.startAddTask(mJIngHua, "净化", mJinghuaA);
                } else {
                    SendUtil.open6();
                }
                break;
            case 5:                         //运行加注
                if (isSuccess) {
                    mJiazhuTime = mAddTimeTimp; //初始化加注倒计时时间
                    mSendState = 0;
                    mDialog.setCurrentRuningState(5);
                    if (!mDialog.isShowing()) {
                        mDialog.show();
                    }
                    //记录电子秤数值
                    SharedPreferences.Editor edit = mRecordAd.edit();
                    edit.putString("initAd", mAdNumbwe);
                    edit.apply();
                    mDialog.setPauseClickable(true);
                    startCheckAd(true);
                } else {
                    SendUtil.open2();
                }
                break;
            case 6:                                     //重启杀菌
                if (isSuccess) {
                    mSendState = 0;
                    mManage.reStart();
                } else {
                    startShajun(6);
                }
                break;
            case 7:                                     //重启净化
                if (isSuccess) {
                    mSendState = 0;
                    mManage.reStart();
                } else {
                    startJinghua(7);
                }
                break;
            case 8:                                        //重启雾化
                if (isSuccess) {
                    mSendState = 0;
                    mManage.reStart();
                } else {
                    startWuhua(8);
                }
                break;
            case 9:
                if (isSuccess) {
                    mSendState = 0;
                    mDialog.setCurrentRuningState(6);
                    if (!mDialog.isShowing()) {
                        mDialog.show();
                        mDialog.setRunText("正在进行回收");
                    }
                    //记录电子秤数值
                    SharedPreferences.Editor edit = mRecordAd.edit();
                    edit.putString("initAd", mAdNumbwe);
                    edit.putString("checkAd", mAdNumbwe);
                    edit.apply();
                    startBack();
                } else {
                    startHuishou(9);
                }
                break;
            case 10:
                mSendState = 0;
                if (isSuccess) {
                    isDTUFail = false;
                    SendUtil.controlVoice();
                } else {
                    isDTUFail = true;
                    SendUtil.controlVoiceFive();
                }

                break;
        }

    }

    private void initData() {
        mToobarBgImage.setImageResource(R.mipmap.top_bar_function);
    }

    private void initListener() {
        mBack.setOnClickListener(this);
        mWuhua.setOnClickListener(this);
        mShajun.setOnClickListener(this);
        mJinhua.setOnClickListener(this);
        mJiazhu.setOnClickListener(this);
        mHuishou.setOnClickListener(this);
        mXunhuan.setOnClickListener(this);
        mBeforeLock.setOnClickListener(this);
        mAfterLock.setOnClickListener(this);
        mSettings.setOnClickListener(this);
        mUpdataTextTab.setOnClickListener(this);
        mDtu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.wuhua:                                    //先加注再雾化
                mLoopCount = 0;         //初始化循环次数
                //先加注,加注到800ML开始雾化
                mJiazhuTime = mAddTimeTimp; //初始化倒计加注时时间
                isLoopruning = false;
                startJiazhu(1);
                break;
            case R.id.shajun:                                    //杀菌
                mLoopCount = 0;         //初始化循环次数
                isLoopruning = false;
                startShajun(3);
                break;
            case R.id.jinhua:                                    //净化
                mLoopCount = 0;         //初始化循环次数
                isLoopruning = false;
                startJinghua(4);
                break;
            case R.id.jiazhu:                                    //加注
                mLoopCount = 0;         //初始化循环次数
                mJiazhuTime = mAddTimeTimp; //初始化加注倒计时时间
                isLoopruning = false;
                startJiazhu(5);
                break;
            case R.id.huishou:                                    //回收
                mLoopCount = 0;         //初始化循环次数
                mBackSuccessTime = mBackTimeTimp;//初始化回收倒计时时间
                isLoopruning = false;
                startHuishou(9);
                break;
            case R.id.xunhuan:                                    //智能循环测试
                mJiazhuTime = mAddTimeTimp; //初始化加注倒计时时间
                isLoopruning = true;
                startJiazhu(5);
                break;
            case R.id.before_lock:                                //前门锁
                if (!mIsBefore)
                    SendUtil.openQian();
                break;
            case R.id.after_lock:                                 //后门锁
                if (!mIsAfter)
                    SendUtil.openHou();
                break;
            case R.id.settings:                                     //参数设置
                if (!mSettingDialog.isShowing()) {
                    mSettingDialog.show();
                }
                break;
            case R.id.dtu:                                          //dtu模块
                mSendState = 10;
                SendUtil.checkDtuIsScurress();
                break;
            case R.id.updata_text_tab:                               //上传测试报告
                if (!mUpDataTextTabDialog.isShowing()) {
                    mUpDataTextTabDialog.show();
                    mUpDataTextTabDialog.setData(isAddFail, isBackFail, isWuhuaFail, isShajunFail, isJinghuaFail, isServiceFail, isDTUFail, mEquipmentNumber, mLoopCount);
                }
                break;

        }
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:                     //雾化前加注完成,开始雾化
                    if (mDialog != null)
                        mDialog.setCurrentRuningState(2);
                    startWuhua(2);
                    break;
                case 1:                     //雾化进行中
                    int arg1 = msg.arg1;
                    mDialog.setRunText("正在进行雾化" + arg1 + "秒");
                    mDialog.setRunData("电流 :" + mCommunionFlow);
                    break;
                case 3:                     //杀菌进行中
                    int shajun = msg.arg1;
                    mDialog.setRunText("正在进行杀菌" + shajun + "秒");
                    mDialog.setRunData("电流 :" + mCommunionFlow);
                    if (isLoopruning && shajun == 5) {   //检测DTU
                        mSendState = 10;
                        SendUtil.checkDtuIsScurress();
                    }
                    break;
                case 5:                     //净化进行中
                    int jinhua = msg.arg1;
                    mDialog.setRunText("正在进行净化" + jinhua + "秒");
                    mDialog.setRunData("电流 :" + mCommunionFlow);
                    break;
                case 2:                     //雾化完成
                    isWuhuaFail = false;
                    mFunctionTv.setText("无");                //如果出现过异常就清除掉状态,不正常进行不了下一步
                    if (isLoopruning) {
                        startShajun(3);
                    } else {
                        SendUtil.closeAll();
                        if (mDialog.isShowing())
                            mDialog.dismiss();
                    }
                    break;
                case 4:                     //杀菌完成
                    isShajunFail = false;
                    mFunctionTv.setText("无");
                    if (isLoopruning) {
                        startJinghua(4);
                    } else {
                        SendUtil.closeAll();
                        if (mDialog.isShowing())
                            mDialog.dismiss();
                    }
                    break;
                case 6:                     //净化完成
                    isJinghuaFail = false;
                    mFunctionTv.setText("无");
                    if (isLoopruning) {
                        mBackSuccessTime = mBackTimeTimp;//初始化回收倒计时时间
                        startHuishou(9);
                    } else {
                        SendUtil.closeAll();
                        if (mDialog.isShowing())
                            mDialog.dismiss();
                    }
                    break;
                case 8:                     //加注完成
                    isAddFail = false;
                    mFunctionTv.setText("无");
                    if (isLoopruning) {
                        startWuhua(2);
                    } else {
                        SendUtil.closeAll();
                        if (mDialog.isShowing())
                            mDialog.dismiss();
                    }
                    break;
                case 9:
                    int time = msg.arg1;
                    mDialog.setRunText("正在进行加注" + time + "秒");
                    mDialog.setRunData("电流 :" + mCommunionFlow);
                    break;
                case 11:                                //回收成功
                    isBackFail = false;
                    break;
                case 12:                                //回收中
                    mDialog.setRunText("正在进行回收");
                    mDialog.setRunData("电流 :" + mCommunionFlow);
                    break;
                case 10:                    //回收完成
                    isBackFail = false;
                    if (isLoopruning) {
                        mLoopCount++;
                        if (mLoopCount > 5) {
                            SendUtil.closeAll();
                            if (mDialog.isShowing())
                                mDialog.dismiss();
                        } else {
                            mLoopCountTv.setText(mLoopCount + "");                //循环次数
                            startJiazhu(5);
                            isLoopruning = true;
                        }
                    } else {
                        SendUtil.closeAll();
                        if (mDialog.isShowing())
                            mDialog.dismiss();
                    }
                    break;
                case 16:                                //回收失败
                    mFunctionTv.setText("回收");
                    isBackFail = true;
                    mDialog.setRunText("回收失败");
                    SendUtil.closeAll();
                    break;
                case 7:                                 //加注失败
                    mFunctionTv.setText("加注");
                    isAddFail = true;
                    mDialog.setRunText("加注失败");
                    SendUtil.closeAll();
                    break;
                case 13:                               //雾化失败
                    mFunctionTv.setText("雾化");
                    isWuhuaFail = true;
                    mDialog.setRunText("雾化失败");
                    SendUtil.closeAll();
                    if (!isLoopruning) {
                        mDialog.setPauseClickable(false);
                        mDialog.setRunText("雾化失败");
                        SendUtil.closeAll();
                    }
                    break;
                case 14:                                //杀菌失败
                    isShajunFail = true;
                    mFunctionTv.setText("杀菌");
                    mDialog.setRunText("杀菌失败");
                    SendUtil.closeAll();
                    if (!isLoopruning) {
                        mDialog.setPauseClickable(false);
                        mDialog.setRunText("杀菌失败");
                        SendUtil.closeAll();
                    }
                    break;
                case 15:                                //净化失败
                    isJinghuaFail = true;
                    mFunctionTv.setText("净化");
                    mDialog.setRunText("净化失败");
                    SendUtil.closeAll();
                    if (!isLoopruning) {
                        mDialog.setPauseClickable(false);
                        mDialog.setRunText("净化失败");
                        SendUtil.closeAll();
                    }
                    break;
            }

        }
    };

    //TODO----------------------------------加注 start---------------------------------//

    /**
     * @param isAfterAdd true 单独加注,fasle雾化前加注
     */
    private void startCheckAd(boolean isAfterAdd) {
        mCheckTimer = new Timer();
        mCheckTimer.schedule(new CheckAdTask(isAfterAdd), 0, 1000);
    }

    private void stopCheckAd() {
        if (mCheckTimer != null) {
            mCheckTimer.cancel();
        }
    }

    private class CheckAdTask extends TimerTask {
        private boolean mIsAfterAdd;

        public CheckAdTask(boolean isAfterAdd) {
            mIsAfterAdd = isAfterAdd;
        }

        @Override
        public void run() {
            String initAd = mRecordAd.getString("initAd", null);
            if (!TextUtils.isEmpty(initAd)) {
                if (mJiazhuTime > 0) {
                    mJiazhuTime--;
                }
                if (mIsAfterAdd) {       //单独加注发送倒计时
                    Message message = new Message();
                    message.what = 9;
                    message.arg1 = mJiazhuTime;
                    mHandler.sendMessage(message);
                }
                int initAdInt = Integer.parseInt(initAd, 16);
                int currentAdInt = Integer.parseInt(mAdNumbwe, 16);
                if (Math.abs(initAdInt - currentAdInt) >= mAddCount) {         //加注了800ML
                    stopCheckAd();
                    if (mIsAfterAdd) {
                        Message message = new Message();
                        message.what = 8;
                        mHandler.sendMessage(message);
                    } else {//开始雾化
                        Message message = new Message();
                        message.what = 0;
                        mHandler.sendMessage(message);
                    }
                } else {                 //时间已经到了还没加注800ML,加注失败
                    if (mJiazhuTime == 0) {
                        stopCheckAd();
                        Message message = new Message();
                        message.what = 7;
                        mHandler.sendMessage(message);
                    }
                }
            }
        }
    }

    //----------------------------------加注 stop---------------------------------//
    //TODO----------------------------------回收 start---------------------------------//

    private void startBack() {
        mCheckTimer = new Timer();
        mCheckTimer.schedule(new BackTask(), 5000, 1000);
    }

    private void stopBack() {
        if (mCheckTimer != null) {
            mCheckTimer.cancel();
        }
    }

    private class BackTask extends TimerTask {
        @Override
        public void run() {
            String initAd = mRecordAd.getString("initAd", null);
            String checkAd = mRecordAd.getString("checkAd", null);
            int initAdInt = Integer.parseInt(initAd, 16);
            int checkAdInt = Integer.parseInt(checkAd, 16);
            int currentAdInt = Integer.parseInt(mAdNumbwe, 16);
            if (mBackSuccessTime > 0) {
                mBackSuccessTime--;
                Message message = new Message();
                message.what = 12;
                mHandler.sendMessage(message);
            } else {             //20秒已到
                if (currentAdInt - initAdInt < 10) {
                    stopBack();
                    if (Math.abs(currentAdInt - checkAdInt) >= mBackCount) {     //回收目标完成
                        Message message = new Message();
                        message.what = 10;
                        mHandler.sendMessage(message);
                    } else {                     //回收失败
                        Message message = new Message();
                        message.what = 16;
                        mHandler.sendMessage(message);
                    }
                }
            }
            //初始化一次电子秤的保存的数值
            SharedPreferences.Editor edit = mRecordAd.edit();
            edit.putString("initAd", mAdNumbwe);
            edit.apply();
        }
    }

    //----------------------------------回收 stop---------------------------------//


    @Override
    public void onClickPause() {                //点击暂停按钮
        SendUtil.closeAll();
        stopCheckAd();
        stopBack();
        mManage.pauseTask();
    }

    @Override
    public void onContinue(int state) {                //点击继续按钮,state当前运行功能
        switch (state) {
            case 1:                                 //雾化前的加注
                startJiazhu(1);
                break;
            case 2:                                 //雾化
                startWuhua(8);
                break;
            case 3:                                 //杀菌
                startShajun(6);
                break;
            case 4:                                 //净化
                startJinghua(7);
                break;
            case 5:                                 //加注
                startJiazhu(5);
                break;
            case 6:                                 //回收
                startHuishou(9);
                break;
        }
    }

    @Override
    public void onBack() {
        SendUtil.closeAll();
        stopCheckAd();
        stopBack();
        mManage.stopAddTask();
    }

    /**
     * 设置参数时回调
     *
     * @param addTime   加注时间
     * @param addCount  加注量
     * @param backTime  回收时间
     * @param backCount 回收量
     */
    @Override
    public void onChange(int addTime, int addCount, int backTime, int backCount, int wuhuaTime, int shajunTime,
                         int jingHuatime, float wuhuaA, float hsaJunA, float jinghuaA) {
        mJiazhuTime = addTime;
        mAddTimeTimp = addTime;
        mAddCount = addCount;
        mBackSuccessTime = backTime;
        mBackTimeTimp = backTime;
        mBackCount = backCount;
        mWuhuaTime = wuhuaTime;
        mShaJun = shajunTime;
        mJIngHua = jingHuatime;
        mWuhuaA = wuhuaA;
        mShajunA = hsaJunA;
        mJinghuaA = jinghuaA;
    }

    /**
     * 开启加注
     *
     * @param state
     */
    private void startJiazhu(int state) {
        mSendState = state;
        SendUtil.open2();
    }

    /**
     * 开启雾化
     *
     * @param state
     */
    private void startWuhua(int state) {
        mSendState = state;
        SendUtil.open8();
    }

    /**
     * 开启杀菌
     *
     * @param state
     */
    public void startShajun(int state) {
        mSendState = state;
        SendUtil.open7();
    }

    /**
     * 开启净化
     *
     * @param state
     */
    public void startJinghua(int state) {
        mSendState = state;
        SendUtil.open6();
    }

    /**
     * 开启回收
     *
     * @param state
     */
    public void startHuishou(int state) {
        mSendState = state;
        SendUtil.open4();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SendUtil.closeAll();
        mHandler.removeCallbacksAndMessages(null);
    }
}
