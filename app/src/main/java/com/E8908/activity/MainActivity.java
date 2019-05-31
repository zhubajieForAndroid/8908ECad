package com.E8908.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.E8908.adapter.HomeVideoPagerAdapter;
import com.E8908.impl.CheckGasHistoryPersenterImpl;
import com.E8908.impl.UpDataAppPersenterImpl;
import com.E8908.util.FileUtil;
import com.E8908.view.CheckGasHistoryView;
import com.E8908.util.OkhttpManager;
import com.E8908.R;
import com.E8908.base.BaseActivity;
import com.E8908.base.MyApplication;
import com.E8908.bean.CheckGasHistoryBean;
import com.E8908.bean.RowsBean;
import com.E8908.conf.Constants;
import com.E8908.util.CheckVersion;
import com.E8908.util.DataUtil;
import com.E8908.util.SendUtil;
import com.E8908.view.UpdataView;
import com.E8908.widget.ActivationDialog;
import com.E8908.widget.InputIdDialog;
import com.E8908.widget.IsLockDialog;
import com.E8908.widget.LinkSeverDialog;
import com.E8908.widget.ScoreView;
import com.E8908.widget.StopDialog;
import com.E8908.widget.ToastUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MainActivity extends BaseActivity implements View.OnClickListener, InputIdDialog.OnSendOpenListener, View.OnTouchListener, UpdataView {

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
    @Bind(R.id.work_ranking)
    TextView mWorkRanking;
    @Bind(R.id.work_ranking_month)
    TextView mWorkRankingMonth;
    @Bind(R.id.work_in_ranking)
    TextView mWorkInRanking;
    @Bind(R.id.work_in_ranking_month)
    TextView mWorkInRankingMonth;


    private boolean isYesData = false;

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
    private TextView mEquipmentID;
    private TextView mSurplusOil;
    private List<RowsBean> mResponse;
    private double mCurrentState;
    final static int COUNTS = 5;//点击次数
    final static long DURATION = 3 * 1000;//规定有效时间
    long[] mHits = new long[COUNTS];
    private FileDownloader mFileDownloader;
    private UpDataAppPersenterImpl mUpDataAppPersenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mLockDialog = new IsLockDialog(this, R.style.dialog, R.mipmap.suoding);
        mDialog = new ActivationDialog(this, R.style.dialog);
        mStopDialog = new StopDialog(this, R.style.dialog);
        mLinkSeverDialog = new LinkSeverDialog(this, R.style.dialog, R.mipmap.popovers_2);
        FileDownloader.setup(this);
        mFileDownloader = FileDownloader.getImpl();



        initView();
        initData();
        //加载视频
        loadVideo();
    }

    private void loadVideo() {
        ViewPager viewPager = findViewById(R.id.home_viewpager);
        TabLayout tabLayout = findViewById(R.id.tab);

        HomeVideoPagerAdapter adapter = new HomeVideoPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
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
        if (size == Constants.SET_RESULT_LINGTH) {
            setResultData(buffer);
        }
    }

    private void setResultData(byte[] buffer) {
        Boolean isSuccess = DataUtil.analysisSetResult(buffer);
        if (mCurrentState == 2) {
            if (isSuccess) {
                mCurrentState = 0;
            } else {
                mCurrentState = 2;
                SendUtil.setReadyState(mEquipmentNumber, 0);
            }
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
            Log.d(TAG, "isYesData: 断开了连接");
        }
        isYesData = false;
    }

    private void analysisData(byte[] buffer) {
        String state = DataUtil.getState(buffer);                           //状态位
        String riseNumbwe = DataUtil.getRiseNumbwe(buffer);                 //获取液体升数

        String countControl = DataUtil.getCountControl(buffer);             //常规和自定义次数控制状态
        String changgui = countControl.substring(7, 8);                     //常规次数控制
        String shengdu = countControl.substring(6, 7);                     //自定义次数控制
        //更换药液提醒
        //mChangeOilState = DataUtil.getChangeOilState(buffer);

        //获取加注量
        mAddNumbwe = DataUtil.getAddNumbwe(buffer);

        int signalStrength = DataUtil.getSignalStrength(buffer);            //获取信号强度
        //获取序列号
        mEquipmentNumber = DataUtil.getEquipmentNumber(buffer);
        if (!TextUtils.isEmpty(mEquipmentNumber))
            mEquipmentID.setText(mEquipmentNumber);

        //药液量
        mResultRatioNumbwe = Integer.parseInt(riseNumbwe, 16);
        //设置剩余量
        if (mResultRatioNumbwe < Constants.MAX_NUMBER) {
            mSurplusOil.setText(mResultRatioNumbwe + "ML (" + (mResultRatioNumbwe / 250) + ")次");
        } else {
            mSurplusOil.setText(Constants.MAX_NUMBER - 1 + "ML(80次)");
        }
        String connectServerState = state.substring(3, 4);                  //连接服务器状态
        String activationState = state.substring(4, 5);                     //设备激活状态
        String lockState = state.substring(5, 6);                           //设备锁定状态
        //00011000
        String afterLockState = state.substring(1, 2);                       //后门锁定状态
        String frontLockState = state.substring(2, 3);                       //前门锁定状态
        String temperatureState = state.substring(0, 1);                       //温度传感器连接状态
        mIsProhibit = state.substring(7, 8);                                    //自定义禁用状态2019-4-28 18:54:30
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
        if (lockState.equals("1") || changgui.equals("1") || shengdu.equals("1")) {
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
        mToobarBgImage.setImageResource(R.mipmap.top_bar);
        mToobarBgImage.setOnTouchListener(this);
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

        //检查更新
        mUpDataAppPersenter = new UpDataAppPersenterImpl(this);
        mUpDataAppPersenter.loadData();
    }

    private void loadRankData() {
        OkhttpManager okhttpManager = OkhttpManager.getOkhttpManager();
        Map<String, String> pames = new HashMap<>();
        pames.put("equipmentID", mEquipmentNumber);
        okhttpManager.doPost(Constants.URLS.EQUIPMENT_RANK_DATA, pames, mRankingCallBack);
    }

    private Callback mRankingCallBack = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String s = response.body().string();
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

    private void initView() {
        linearLayout_message = findViewById(R.id.tab_linearlayout_work_statistics);
        linearLayout_about_equipment = findViewById(R.id.about_equipment);
        linearLayout_system_setup = findViewById(R.id.tag_lineatrlayout_system_setup);

        linearLayout_routine = findViewById(R.id.tab_linearlayout_routine); //常规
        linearLayout_depth = findViewById(R.id.tab_linearlayout_depth);//深度
        linearLayout_infusion_solution = findViewById(R.id.tab_linearlayout_infusion_solution);// 加注


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

        mEquipmentID = findViewById(R.id.eqeuipment_id);
        mSurplusOil = findViewById(R.id.surplus_oil);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_linearlayout_work_statistics:         //工作统计
                SendUtil.controlVoice();
                mIntent = new Intent(MainActivity.this, WorkStatisticsActivity.class);
                startActivity(mIntent);
                break;
            case R.id.about_equipment:                           //在线检测
                finish();
                break;
            case R.id.tag_lineatrlayout_system_setup:           //设置界面
                // SendUtil.setConnectForInstrument();
                SendUtil.controlVoice();
                mIntent = new Intent(MainActivity.this, SystemSettingsActivity.class);
                startActivity(mIntent);
                break;
            case R.id.tab_linearlayout_routine:                 //常规模式
                if ("0".equals(mIsProhibit)) {                  //检测到打开了常规,只能使用常规模式
                    //在点击常规和深度保养时检测当前药液量,如果大于20.5升就提示(新药液桶药液过多,不能再加注药液了)
                    if (mResultRatioNumbwe < 20500) {
                        if (mResultRatioNumbwe > 1000) {    //检测药液量是否大于1000ML
                            SendUtil.controlVoice();
                            mIntent = new Intent(MainActivity.this, MaintainOneActivity.class);
                            mIntent.putExtra("isRoutine", true);
                            startActivity(mIntent);
                        } else {
                            ToastUtil.showMessage("药液不足1L,请加注");
                        }
                    } else {
                        if (!mStopDialog.isShowing()) {
                            mStopDialog.setBitmap(R.mipmap.home_popovers_hint_2);
                            mStopDialog.show();
                            mStopDialog.setOnMakeSuerListener(new StopDialog.OnMakeSuerListener() {
                                @Override
                                public void isMakeUser(boolean b) {
                                    if (b) {
                                        mStopDialog.dismiss();
                                        if (mResultRatioNumbwe > 1000) {
                                            SendUtil.controlVoice();
                                            mIntent = new Intent(MainActivity.this, MaintainOneActivity.class);
                                            mIntent.putExtra("isRoutine", true);
                                            startActivity(mIntent);
                                        } else {
                                            ToastUtil.showMessage("药液不足1L,请加注");
                                        }
                                    }
                                }
                            });
                        }
                    }
                } else {
                    ToastUtil.showMessage("此功能未获取权限");
                }
                break;
            case R.id.tab_linearlayout_depth:                    //自定义模式
                if ("1".equals(mIsProhibit)) {      //检测关闭了常规,可以使用自定义模式
                    if (mResultRatioNumbwe < 20500) {
                        if (mResultRatioNumbwe > 1000) {
                            SendUtil.controlVoice();
                            mIntent = new Intent(MainActivity.this, MaintainOneActivity.class);
                            mIntent.putExtra("isRoutine", false);
                            startActivity(mIntent);
                        } else {
                            ToastUtil.showMessage("药液不足1L,请加注");
                        }
                    } else {
                        if (!mStopDialog.isShowing()) {
                            mStopDialog.setBitmap(R.mipmap.home_popovers_hint_2);
                            mStopDialog.show();
                            mStopDialog.setOnMakeSuerListener(new StopDialog.OnMakeSuerListener() {
                                @Override
                                public void isMakeUser(boolean b) {
                                    if (b) {
                                        mStopDialog.dismiss();
                                        if (mResultRatioNumbwe > 1000) {
                                            SendUtil.controlVoice();
                                            mIntent = new Intent(MainActivity.this, MaintainOneActivity.class);
                                            mIntent.putExtra("isRoutine", false);
                                            startActivity(mIntent);
                                        } else {
                                            ToastUtil.showMessage("药液不足1L,请加注");
                                        }
                                    }
                                }
                            });
                        }
                    }
                } else {
                    ToastUtil.showMessage("此功能未获取权限");
                }
                break;
            case R.id.tab_linearlayout_infusion_solution:       //加注药液
                SendUtil.controlVoice();
                mIntent = new Intent(MainActivity.this, FillingActivity.class);
                startActivity(mIntent);
                break;
            case R.id.progress:                 //气体检测历史数据
                SendUtil.controlVoice();
                mIntent = new Intent(this, HistoryActivity.class);
                mIntent.putExtra("equipmentId", mEquipmentNumber);
                startActivity(mIntent);
                break;

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!TextUtils.isEmpty(mEquipmentNumber) && !"".equals(mEquipmentNumber)) {
            //请求排名数据
            loadRankData();
        }
        //每次返回主界面都设置下状态为未就绪
        mCurrentState = 2;
        SendUtil.setReadyState(mEquipmentNumber, 0);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.closeSerialPort();
        if (mFileDownloader != null){
            mFileDownloader.pauseAll();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 111) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                //没有安装未知来源应用的权限
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                    startActivityForResult(intent, 15);
                }
            }
        }
    }


    @Override
    public void onOpen(int state) {
        if (state == 1) {        //前门
            SendUtil.openQian();
        } else {
            SendUtil.openHou();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if ((x >= 820 && x <= 915) && (y >= 12 && y <= 76)) {       //前门
            System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
            //实现左移，然后最后一个位置更新距离开机的时间，如果最后一个时间和最开始时间小于DURATION，即连续10次点击
            mHits[mHits.length - 1] = SystemClock.uptimeMillis();
            if (mHits[0] >= (SystemClock.uptimeMillis() - DURATION)) {
                InputIdDialog dialog = new InputIdDialog(this, R.style.dialog, TextUtils.isEmpty(mEquipmentNumber) ? "00000000" : mEquipmentNumber, 1);
                dialog.setOnSendOpenListener(this);
                dialog.show();
            }
        } else if ((x >= 935 && x <= 1025) && (y >= 12 && y <= 76)) {    //后门
            System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
            //实现左移，然后最后一个位置更新距离开机的时间，如果最后一个时间和最开始时间小于DURATION，即连续10次点击
            mHits[mHits.length - 1] = SystemClock.uptimeMillis();
            if (mHits[0] >= (SystemClock.uptimeMillis() - DURATION)) {
                InputIdDialog dialog = new InputIdDialog(this, R.style.dialog, TextUtils.isEmpty(mEquipmentNumber) ? "00000000" : mEquipmentNumber, 2);
                dialog.setOnSendOpenListener(this);
                dialog.show();
            }
        }
        return false;
    }

    @Override
    public void onFaild(String msg) {
        //ToastUtil.showMessage(msg);
    }

    @Override
    public void onSuccess(String downUrl, String upText) {

        //下载Apk
        mFileDownloader.create(downUrl).setPath(FileUtil.getUpdataPath().getAbsolutePath()).setListener(null).start();
    }

}
