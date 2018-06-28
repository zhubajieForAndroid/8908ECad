package com.cad.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cad.R;
import com.cad.base.BaseActivity;
import com.cad.base.MyApplication;
import com.cad.conf.Constants;
import com.cad.conf.Protocol;
import com.cad.thread.SendService;
import com.cad.ui.SweepGradientCircleProgressBar;
import com.cad.util.CheckVersion;
import com.cad.util.DataUtil;
import com.cad.util.SendUtil;
import com.cad.widget.ActivationDialog;
import com.cad.widget.IsLockDialog;
import com.cad.widget.LinkSeverDialog;
import com.cad.widget.SpacingTextView;
import com.cad.widget.StopDialog;
import com.cad.widget.ToastUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    @Bind(R.id.message_state)
    TextView mMessageState;
    @Bind(R.id.tab_image_front_state)
    ImageView mTabImageFrontState;
    @Bind(R.id.tab_image_communication_state)
    ImageView mTabImageCommunicationState;
    @Bind(R.id.tab_image_back_state)
    ImageView mTabImageBackState;
    @Bind(R.id.tab_image_wifi)
    ImageView mTabImageWifi;

    @Bind(R.id.toobar_bg_image)
    ImageView mToobarBgImage;
    @Bind(R.id.temperature_state)
    TextView mTemperatureState;
    @Bind(R.id.mian_id)
    SpacingTextView mMianId;
    @Bind(R.id.work_ranking)
    TextView mWorkRanking;
    @Bind(R.id.work_ranking_month)
    TextView mWorkRankingMonth;
    @Bind(R.id.work_in_ranking)
    TextView mWorkInRanking;
    @Bind(R.id.work_in_ranking_month)
    TextView mWorkInRankingMonth;
    private boolean isYesData = false;

    private SweepGradientCircleProgressBar progress;
    private LinearLayout linearLayout_message, linearLayout_about_equipment, linearLayout_system_setup, linearLayout_routine, linearLayout_depth, linearLayout_infusion_solution;
    private Intent mIntent;
    private int mResultRatioNumbwe;
    private boolean isShowDialog = true;
    private boolean isDisDialog = true;
    private IsLockDialog mLockDialog;
    private boolean isShowLoack = true;
    private boolean isDisLoack = true;
    private ActivationDialog mDialog;
    private LinkSeverDialog mLinkSeverDialog;
    private boolean isNoLink = true;
    private boolean isAgoLock = false;
    private boolean isAfterLock = false;
    private StopDialog mStopDialog;
    private String mEquipmentNumber;
    private int mAddNumbwe;
    private String mIsProhibit;
    private AudioManager mManager;
    private int mChangeOilState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        progress = (SweepGradientCircleProgressBar) findViewById(R.id.progress);
        progress.setMax(Constants.MAX_NUMBER);
        int[] arcColors = new int[]{
                Color.parseColor("#1b6ffa"),
                Color.parseColor("#21caf5")
        };
        progress.setArcColors(arcColors);
        mLockDialog = new IsLockDialog(this, R.style.dialog, R.mipmap.suoding);
        mDialog = new ActivationDialog(this, R.style.dialog);
        mStopDialog = new StopDialog(this, R.style.dialog);
        mLinkSeverDialog = new LinkSeverDialog(this, R.style.dialog, R.mipmap.popovers_2);
        initView();
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


    //串口数据
    @Override
    public void onDataReceived(final byte[] buffer, int size) {
        isYesData = true;
        if (size == Constants.DATA_LONG && buffer[2] == 0x03) {
            analysisData(buffer);
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
        } else {             //失败
            mMessageState.setText("断开");
            mMessageState.setTextColor(Color.parseColor("#fdfa0310"));
        }
        isYesData = false;
    }

    private void analysisData(byte[] buffer) {
        String state = DataUtil.getState(buffer);                           //状态位
        String riseNumbwe = DataUtil.getRiseNumbwe(buffer);                 //获取液体升数
        //更换药液提醒
        mChangeOilState = DataUtil.getChangeOilState(buffer);

        //获取加注量
        mAddNumbwe = DataUtil.getAddNumbwe(buffer);

        int signalStrength = DataUtil.getSignalStrength(buffer);            //获取信号强度
        //获取序列号
        mEquipmentNumber = DataUtil.getEquipmentNumber(buffer);
        if (!TextUtils.isEmpty(mEquipmentNumber))
            mMianId.setText(mEquipmentNumber);

        //药液量
        mResultRatioNumbwe = Integer.parseInt(riseNumbwe, 16);
        //设置剩余量
        if (mResultRatioNumbwe < Constants.MAX_NUMBER) {
            progress.setProgress(mResultRatioNumbwe);
        } else {
            progress.setProgress(Constants.MAX_NUMBER);
        }
        String connectServerState = state.substring(3, 4);                  //连接服务器状态
        String activationState = state.substring(4, 5);                     //设备激活状态
        String lockState = state.substring(5, 6);                           //设备锁定状态
        //00011000
        String afterLockState = state.substring(1, 2);                       //后门锁定状态
        String frontLockState = state.substring(2, 3);                       //前门锁定状态
        String temperatureState = state.substring(0, 1);                       //温度传感器连接状态
        mIsProhibit = state.substring(7, 8);                                    //常规禁用状态
        if (temperatureState.equals("1")) {
            mTemperatureState.setText("- -");
        } else {
            int temperature = DataUtil.getTemperature(buffer);                  //温度
            mTemperatureState.setText(temperature + "℃");
        }
        //00011001
        if (frontLockState.equals("1")) {
            isAgoLock = false;
            mTabImageBackState.setImageResource(R.mipmap.icon_on);
        } else {
            isAgoLock = true;
            mTabImageBackState.setImageResource(R.mipmap.icon_off);
        }
        if (afterLockState.equals("1")) {
            isAfterLock = false;
            mTabImageFrontState.setImageResource(R.mipmap.icon_on);
        } else {
            isAfterLock = true;
            mTabImageFrontState.setImageResource(R.mipmap.icon_off);
        }


        if (!"00000000".equals(mEquipmentNumber)) {
            //是否连接服务器
            if (connectServerState.equals("0")) {
                mTabImageCommunicationState.setImageResource(R.mipmap.top_icon_4_off);
                if (isNoLink && !mLinkSeverDialog.isShowing()) {
                    isNoLink = false;
                    mLinkSeverDialog.show();
                }
            } else {
                if (mLinkSeverDialog != null) {
                    isNoLink = true;
                    mLinkSeverDialog.dismiss();
                }
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
            if (activationState.equals("1")) {
                if (isDisDialog) {
                    isDisDialog = false;
                    isShowDialog = true;
                    //请求排名数据
                    loadRankData();
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                }
            } else {
                if (isShowDialog) {
                    isDisDialog = true;
                    isShowDialog = false;
                    if (mDialog != null) {
                        mDialog.setID(mEquipmentNumber);
                        mDialog.show();
                    }
                }
            }
        }
        if (lockState.equals("1")) {
            if (isDisLoack) {
                isDisLoack = false;
                isShowLoack = true;
                if (mLockDialog != null && !mLockDialog.isShowing())
                    mLockDialog.show();
            }
        } else {
            if (isShowLoack) {
                isDisLoack = true;
                isShowLoack = false;
                if (mLockDialog != null)
                    mLockDialog.dismiss();
            }
        }

    }

    private void initData() {
        mMianId.setSpacing(10);
        mToobarBgImage.setImageResource(R.mipmap.top_bar);
        mLinkSeverDialog.setOnLinkSeverTimeOutLIstener(new LinkSeverDialog.OnLinkSeverTimeOutLIstener() {
            @Override
            public void isTimeOout(boolean b) {
                if (b) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showMessage("连接服务器超时");
                            mLinkSeverDialog.dismiss();
                        }
                    });
                }
            }
        });
        //静默下载APK
        CheckVersion checkVersion = CheckVersion.getInstance();
        checkVersion.getDownLoadInfo(null, false);
    }

    private void loadRankData() {
        Map<String, Object> pames = new HashMap<>();
        pames.put("url", Constants.URLS.EQUIPMENT_RANK_DATA);
        pames.put("equipmentID", mEquipmentNumber);
        Protocol protocol = new Protocol() {
            @Override
            public void errorManage(IOException e) {

            }

            @Override
            public void parseData(Gson gson, String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    int code = object.getInt("code");
                    if (code == 0) {
                        JSONArray jsonArray = object.getJSONArray("response");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            final String type = jsonObject.getString("TYPE");
                            final int rankings = jsonObject.getInt("RANKINGS");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if ("rankingOfInner".equals(type)) {
                                        mWorkInRanking.setText("第" + rankings + "名");
                                    }
                                    if ("curRankingOfInner".equals(type)) {
                                        mWorkInRankingMonth.setText("第" + rankings + "名");
                                    }
                                    if ("curRankingOfWorld".equals(type)) {
                                        mWorkRankingMonth.setText("第" + rankings + "名");
                                    }
                                    if ("rankingOfWorld".equals(type)) {
                                        mWorkRanking.setText("第" + rankings + "名");
                                    }
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        protocol.setParams(pames);
        protocol.loadDataFromNet();
    }

    private void initView() {
        linearLayout_message = (LinearLayout) findViewById(R.id.tab_linearlayout_work_statistics);
        linearLayout_about_equipment = (LinearLayout) findViewById(R.id.about_equipment);
        linearLayout_system_setup = (LinearLayout) findViewById(R.id.tag_lineatrlayout_system_setup);

        linearLayout_routine = (LinearLayout) findViewById(R.id.tab_linearlayout_routine); //常规
        linearLayout_depth = (LinearLayout) findViewById(R.id.tab_linearlayout_depth);//深度
        linearLayout_infusion_solution = (LinearLayout) findViewById(R.id.tab_linearlayout_infusion_solution);// 加注

        linearLayout_message.setOnClickListener(this);
        linearLayout_about_equipment.setOnClickListener(this);

        linearLayout_system_setup.setOnClickListener(this);
        linearLayout_routine.setOnClickListener(this);
        linearLayout_depth.setOnClickListener(this);
        linearLayout_infusion_solution.setOnClickListener(this);

        //设置音量
        mManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //最大音量
        int streamMaxVolume = mManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mManager.setStreamVolume(AudioManager.STREAM_MUSIC, streamMaxVolume, 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_linearlayout_work_statistics:         //工作统计
                if (mChangeOilState == 0) {
                    SendUtil.controlVoice();
                    mIntent = new Intent(MainActivity.this, WorkStatisticsActivity.class);
                    startActivity(mIntent);
                } else {
                    showHIntDialog();
                }
                break;
            case R.id.about_equipment:                           //关于设备
                if (mChangeOilState == 0) {
                    SendUtil.controlVoice();
                    mIntent = new Intent(MainActivity.this, AboutEquipmentActivity.class);
                    startActivity(mIntent);
                } else {
                    showHIntDialog();
                }
                break;
            case R.id.tag_lineatrlayout_system_setup:           //设置界面
                SendUtil.controlVoice();
                mIntent = new Intent(MainActivity.this, SystemSettingsActivity.class);
                startActivity(mIntent);
                break;
            case R.id.tab_linearlayout_routine:                 //常规模式
                if (mChangeOilState == 0) {
                    if ("0".equals(mIsProhibit)) {
                        if (mResultRatioNumbwe > 1000) {
                            if (isAfterLock && isAgoLock) {
                                SendUtil.controlVoice();
                                mIntent = new Intent(MainActivity.this, MaintainOneActivity.class);
                                mIntent.putExtra("isRoutine", true);
                                startActivity(mIntent);
                            } else {
                                if (!mStopDialog.isShowing()) {
                                    mStopDialog.setBitmap(R.mipmap.home_popovers_hint_1);
                                    mStopDialog.show();
                                    mStopDialog.setOnMakeSuerListener(new StopDialog.OnMakeSuerListener() {
                                        @Override
                                        public void isMakeUser(boolean b) {
                                            if (b) {
                                                mStopDialog.dismiss();
                                            }
                                        }
                                    });
                                }
                            }
                        } else {
                            ToastUtil.showMessage("药液不足1L,请加注");
                        }
                    } else {
                        ToastUtil.showMessage("此功能未获取权限");
                    }
                } else {
                    showHIntDialog();
                }
                break;
            case R.id.tab_linearlayout_depth:                    //深度模式
                if (mChangeOilState == 0) {
                    if (mResultRatioNumbwe > 1000) {
                        if (isAfterLock && isAgoLock) {
                            SendUtil.controlVoice();
                            mIntent = new Intent(MainActivity.this, MaintainOneActivity.class);
                            mIntent.putExtra("isRoutine", false);
                            startActivity(mIntent);
                        } else {
                            if (!mStopDialog.isShowing()) {
                                mStopDialog.setBitmap(R.mipmap.home_popovers_hint_1);
                                mStopDialog.show();
                                mStopDialog.setOnMakeSuerListener(new StopDialog.OnMakeSuerListener() {
                                    @Override
                                    public void isMakeUser(boolean b) {
                                        if (b) {
                                            mStopDialog.dismiss();
                                        }
                                    }
                                });
                            }
                        }
                    } else {
                        ToastUtil.showMessage("药液不足1L,请加注");
                    }
                } else {
                    showHIntDialog();
                }
                break;
            case R.id.tab_linearlayout_infusion_solution:       //加注药液
                SendUtil.controlVoice();
                mIntent = new Intent(MainActivity.this, FillingActivity.class);
                startActivity(mIntent);
                break;
        }
    }

    private void showHIntDialog() {
        if (!mStopDialog.isShowing()) {
            mStopDialog.setBitmap(R.mipmap.home_popovers_hint_01);
            mStopDialog.show();
            mStopDialog.setOnMakeSuerListener(new StopDialog.OnMakeSuerListener() {
                @Override
                public void isMakeUser(boolean b) {
                    if (b) {
                        mStopDialog.dismiss();
                    }
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!TextUtils.isEmpty(mEquipmentNumber) && !"".equals(mEquipmentNumber)) {
            //请求排名数据
            loadRankData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.closeSerialPort();
    }
}
