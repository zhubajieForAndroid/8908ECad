package com.E8908.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.adapter.HomeDataAdapter;
import com.E8908.adapter.HomeVideoPagerAdapter;
import com.E8908.base.BaseActivity;
import com.E8908.base.MyApplication;
import com.E8908.bean.RowsBean;
import com.E8908.blueTooth.SendBleDataService;
import com.E8908.conf.Constants;
import com.E8908.manage.SocketManage;
import com.E8908.thread.WifiLinkService;
import com.E8908.util.DataUtil;
import com.E8908.util.OkhttpManager;
import com.E8908.util.SendUtil;
import com.E8908.util.StringUtils;
import com.E8908.widget.ActivationDialog;
import com.E8908.widget.IsLockDialog;
import com.E8908.widget.JurisdictionDialog;
import com.E8908.widget.StopDialog;
import com.E8908.widget.ToastUtil;
import com.E8908.widget.YnnLoginDialog;
import com.tencent.bugly.beta.Beta;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MainActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener, JurisdictionDialog.OnCheckJListener, ViewPager.OnPageChangeListener, YnnLoginDialog.OnBtnClickListener {

    private static final String TAG = "MainActivity";
    @Bind(R.id.message_state)
    TextView mMessageState;
    @Bind(R.id.battery_state)
    ImageView mBatteryState;
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
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    @Bind(R.id.indicator_container)
    LinearLayout mIndicatorContainer;


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
    //private LinkSeverDialog mLinkSeverDialog;
    private boolean isNoLink = true;
    private boolean isAgoLock = false;
    private boolean isAfterLock = false;
    private StopDialog mStopDialog;
    private String mEquipmentNumber;
    private int mAddNumbwe;
    private String mIsProhibit;
    private AudioManager mManager;
    private List<RowsBean> mResponse;
    private double mCurrentState;
    private SharedPreferences mJumpStateInfo;
    private JurisdictionDialog mJuDialog;
    private HomeDataAdapter mHomeDataAdapter;
    private String mType;
    private int mRankings;
    private boolean isLoadRank = true;          //是否请求排名
    private Map<String, Object> rankingMap = new HashMap<>();        //排名信息
    private String mName;
    private String mUserId;
    private int mVersionState = 1;              //默认是4S配置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mLockDialog = new IsLockDialog(this, R.style.dialog, R.mipmap.suoding);
        mDialog = new ActivationDialog(this, R.style.dialog);
        mStopDialog = new StopDialog(this, R.style.dialog);
        //mLinkSeverDialog = new LinkSeverDialog(this, R.style.dialog, R.mipmap.popovers_2);
        //输入权限码开启前门弹窗
        mJuDialog = new JurisdictionDialog(this, R.style.dialog);
        mJuDialog.setOnCheckJListener(this);

        //注册广播接受者接受蓝牙数据
        IntentFilter blefilter = new IntentFilter();
        blefilter.addAction(Constants.BLE_DATA);
        registerReceiver(mBleReceiver, blefilter);

        //技师登录的名字和ID
        Intent intent = getIntent();
        mName = intent.getStringExtra("name");
        mUserId = intent.getStringExtra("userId");

        initView();
        initData();
        //加载视频
        loadVideo();
    }

    @Override
    protected void electricInfo(int percent, boolean isCharging) {
        if (!isCharging) {                //没有在充电
            if (percent <= 20) {
                mBatteryState.setImageResource(R.mipmap.battery_icon_20);
            } else if (percent <= 40) {
                mBatteryState.setImageResource(R.mipmap.battery_icon_40);
            } else if (percent <= 60) {
                mBatteryState.setImageResource(R.mipmap.battery_icon_60);
            } else if (percent <= 80) {
                mBatteryState.setImageResource(R.mipmap.battery_icon_80);
            } else {                                 //电流81到100
                mBatteryState.setImageResource(R.mipmap.battery_icon_100_white);
            }
        } else {                                  //正在充电
            mBatteryState.setImageResource(R.mipmap.battery_icon_charge);
        }
    }

    private void loadVideo() {
        ViewPager viewPager = findViewById(R.id.home_viewpager);
        int videoCount;
        if (Constants.URLS.ID.equals("021")) {
            videoCount = 3;
            addVideoIndicator(videoCount);
        } else {
            videoCount = 2;
            addVideoIndicator(videoCount);
        }

        HomeVideoPagerAdapter adapter = new HomeVideoPagerAdapter(getSupportFragmentManager(), videoCount);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        //搞=316  宽=521
        //检查更新
        Beta.checkUpgrade();
    }

    //动态添加videoPager的指示器
    private void addVideoIndicator(int videoCount) {
        for (int i = 0; i < videoCount; i++) {
            View view = new View(this);
            if (i == 0) {
                view.setBackground(getResources().getDrawable(R.drawable.home_pager_select_shape));
            } else {
                view.setBackground(getResources().getDrawable(R.drawable.home_info_shape));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
            params.setMargins(10, 0, 0, 0);
            view.setLayoutParams(params);
            mIndicatorContainer.addView(view);
        }
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

    private BroadcastReceiver mBleReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constants.BLE_DATA.equals(action)) {
                boolean isLinkBle = intent.getBooleanExtra("isLinkBle", false);
                if (isLinkBle) {
                    byte[] buffer = intent.getByteArrayExtra("data");
                    String bleID = intent.getStringExtra("bleID");
                    mHomeDataAdapter.setData(buffer, rankingMap, true, bleID);
                } else {
                    //ToastUtil.showMessage("连接失败");
                }
                mHomeDataAdapter.setLinkBleState(isLinkBle);
            }
        }
    };

    //串口数据
    @Override
    public void onDataReceived(final byte[] buffer, int size) {
        isYesData = true;
        if (size == Constants.DATA_LONG && buffer[2] == 0x03) {
            analysisData(buffer);
            mHomeDataAdapter.setData(buffer, rankingMap, false, "");
        }
        if (size == Constants.SET_RESULT_LINGTH) {
            setResultData(buffer);
        }
        //6   [42, 6, 35, 0, 15, 35]
        if (size == 6) {             //返回的版本信息数据
            int versionInfo = buffer[3];
            switch (versionInfo) {
                case 0:                 //修理厂的版本
                    mVersionState = 0;
                    break;
                case 1:                 //4s点的配置
                    mVersionState = 1;
                    break;
            }
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
        if (!TextUtils.isEmpty(mEquipmentNumber) && !"00000000".equals(mEquipmentNumber) && isLoadRank) {
            isLoadRank = false;
            loadRankData();
        }
        //药液量
        mResultRatioNumbwe = Integer.parseInt(riseNumbwe, 16);
        //设置剩余量

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
                /*if (isNoLink && !mLinkSeverDialog.isShowing()) {
                    isNoLink = false;
                    mLinkSeverDialog.show();
                }*/
            } else {
                /*if (mLinkSeverDialog != null) {
                    isNoLink = true;
                    mLinkSeverDialog.dismiss();
                }*/
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
        //是否跳过第一二步骤的标记
        mJumpStateInfo = getSharedPreferences("jumpStateInfo", 0);


        mHomeDataAdapter = new HomeDataAdapter(this);

        mViewPager.setAdapter(mHomeDataAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }


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

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_linearlayout_work_statistics:         //工作统计
                SendUtil.controlVoice();
                mIntent = new Intent(MainActivity.this, WorkStatisticsActivity.class);
                startActivity(mIntent);
                break;
            case R.id.about_equipment:                           //退出
                finish();
                break;
            case R.id.tag_lineatrlayout_system_setup:           //设置界面
                // SendUtil.setConnectForInstrument();
                SendUtil.controlVoice();
                mIntent = new Intent(MainActivity.this, SystemSettingsActivity.class);
                startActivity(mIntent);
                break;
            case R.id.tab_linearlayout_routine:                 //常规模式
                if ("0".equals(mIsProhibit)) {
                    if (mVersionState == 0) {           //当前是修理厂版本,必须用户登录
                        if (!TextUtils.isEmpty(mUserId)) {                  //用户已经登录
                            String title = "当前登录的账号:" + mName;
                            String yesBtnStr = "继续操作";
                            String cancleBtnStr = "切换账号";
                            YnnLoginDialog dialog = new YnnLoginDialog(this, R.style.dialog, title, yesBtnStr, cancleBtnStr, true);
                            dialog.setOnBtnClickListener(this);
                            dialog.show();
                        } else {                                             //用户没有登录
                            String title = "请员工登录后再继续操作";
                            String yesBtnStr = "是";
                            String cancleBtnStr = "否";
                            YnnLoginDialog dialog = new YnnLoginDialog(this, R.style.dialog, title, yesBtnStr, cancleBtnStr, false);
                            dialog.setOnBtnClickListener(this);
                            dialog.show();
                        }
                    } else {
                        start(true);
                    }
                }else {
                    ToastUtil.showMessage("此功能未开放权限");
                }
                break;
            case R.id.tab_linearlayout_depth:                    //自定义模式
                if ("1".equals(mIsProhibit)) {
                    if (mVersionState == 0) {           //当前是修理厂版本,必须用户登录
                        if (!TextUtils.isEmpty(mUserId)) {                  //用户已经登录
                            String title = "当前登录的账号:" + mName;
                            String yesBtnStr = "继续操作";
                            String cancleBtnStr = "切换账号";
                            YnnLoginDialog dialog = new YnnLoginDialog(this, R.style.dialog, title, yesBtnStr, cancleBtnStr, true);
                            dialog.setOnBtnClickListener(this);
                            dialog.show();
                        } else {                                             //用户没有登录
                            String title = "请技师登录后再继续操作";
                            String yesBtnStr = "是";
                            String cancleBtnStr = "否";
                            YnnLoginDialog dialog = new YnnLoginDialog(this, R.style.dialog, title, yesBtnStr, cancleBtnStr, false);
                            dialog.setOnBtnClickListener(this);
                            dialog.show();
                        }
                    } else {
                        start(false);
                    }
                }else {
                    ToastUtil.showMessage("此功能未开放权限");
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
        if (!TextUtils.isEmpty(mEquipmentNumber) && !"00000000".equals(mEquipmentNumber)) {
            loadRankData();
        }
        //每次返回主界面都设置下状态为未就绪
        mCurrentState = 2;
        SendUtil.setReadyState(mEquipmentNumber, 0);
    }

    private void loadRankData() {
        rankingMap.clear();
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
            if (response.isSuccessful()) {
                String s = response.body().string();
                try {
                    JSONObject object = new JSONObject(s);
                    int code = object.getInt("code");
                    if (code == 0) {
                        JSONObject responseObj = object.getJSONObject("response");
                        String nationWideRanking = responseObj.getString("nationWideRanking");
                        rankingMap.put("nationWideRanking", nationWideRanking);
                        String interiorRanking = responseObj.getString("interiorRanking");
                        rankingMap.put("interiorRanking", interiorRanking);
                        String nationWideMonthRanking = responseObj.getString("nationWideMonthRanking");
                        rankingMap.put("nationWideMonthRanking", nationWideMonthRanking);
                        String interiorMonthRanking = responseObj.getString("interiorMonthRanking");
                        rankingMap.put("interiorMonthRanking", interiorMonthRanking);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

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
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if ((x >= 820 && x <= 915) && (y >= 12 && y <= 76)) {       //前门
            if (!TextUtils.isEmpty(mEquipmentNumber)) {
                mJuDialog.setEquipmentId(mEquipmentNumber, 1);
                mJuDialog.show();
            }

        } else if ((x >= 935 && x <= 1025) && (y >= 12 && y <= 76)) {    //后门
            if (!TextUtils.isEmpty(mEquipmentNumber)) {
                mJuDialog.setEquipmentId(mEquipmentNumber, 0);
                mJuDialog.show();
            }
        }
        return false;
    }


    @Override
    public void onCkcekState(int state, boolean isScurress, String msg) {
        if (isScurress) {
            if (state == 1) {        //前门
                SendUtil.openQian();
            } else {
                SendUtil.openHou();
            }
        } else {
            ToastUtil.showMessage(msg);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 121) {
            //扫描蓝牙
            ToastUtil.showMessage("请从新扫描气体检测仪");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //发送查询是4s店还是修理厂
        SendUtil.queryVersionInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.closeSerialPort();
        //停止wifi连接服务器的service
        if (!TextUtils.isEmpty(mEquipmentNumber)) {
            SocketManage.getSocketManage().disconnect(mEquipmentNumber);
            if (StringUtils.isServiceRunning(this, "com.E8908.thread.WifiLinkService")) {
                Intent serviceIntent = new Intent(this, WifiLinkService.class);
                stopService(serviceIntent);
            }
        }
        Intent bleIntent = new Intent(this, SendBleDataService.class);
        stopService(bleIntent);
        unregisterReceiver(mBleReceiver);
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter.isEnabled())
            defaultAdapter.disable();
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        int childCount = mIndicatorContainer.getChildCount();
        for (int j = 0; j < childCount; j++) {
            View childAt = mIndicatorContainer.getChildAt(j);
            childAt.setBackground(getResources().getDrawable(R.drawable.home_info_shape));
        }
        View childAt = mIndicatorContainer.getChildAt(i);
        childAt.setBackground(getResources().getDrawable(R.drawable.home_pager_select_shape));

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void clickBtn(boolean isCancle, boolean isLogin) {
        if (isLogin) {           //已经登录
            if (isCancle) {      //继续操作
                if ("0".equals(mIsProhibit)){
                    start(true);
                }else {
                    start(false);
                }
            } else {             //切换账号
                //发送广播通知技师App登录
                Intent intent = new Intent();
                intent.setAction("loginMsgBy8908E");
                sendBroadcast(intent);
                finish();
            }
        } else {                 //没有登录
            if (isCancle) {      //去登录
                //发送广播通知技师App登录
                //发送广播通知技师App登录
                Intent intent = new Intent();
                intent.setAction("loginMsgBy8908E");
                sendBroadcast(intent);
                finish();
            }
        }
    }

    private void start(final boolean isChanggui) {
            if (mResultRatioNumbwe < 20500) {
                if (mResultRatioNumbwe > 1000) {    //检测药液量是否大于1000ML
                    boolean isJump = mJumpStateInfo.getBoolean("isJump", false);
                    if (isJump) {                //跳过了第一二步骤
                        mIntent = new Intent(MainActivity.this, MaintainThreeReadActivityDemo.class);
                        mIntent.putExtra("isRoutine", isChanggui);
                        mIntent.putExtra("userID", mUserId);
                        startActivity(mIntent);
                    } else {
                        mIntent = new Intent(MainActivity.this, MaintainOneActivity.class);
                        mIntent.putExtra("isRoutine", isChanggui);
                        mIntent.putExtra("userID", mUserId);
                        startActivity(mIntent);
                    }
                } else {
                    ToastUtil.showMessage("药液不足1L,请加注");
                }
            }else {
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
                                    boolean isJump = mJumpStateInfo.getBoolean("isJump", false);
                                    if (isJump) {                //跳过了第一二步骤
                                        mIntent = new Intent(MainActivity.this, MaintainThreeReadActivityDemo.class);
                                        mIntent.putExtra("isRoutine", isChanggui);
                                        mIntent.putExtra("userID", mUserId);
                                        startActivity(mIntent);
                                    } else {
                                        mIntent = new Intent(MainActivity.this, MaintainOneActivity.class);
                                        mIntent.putExtra("isRoutine", isChanggui);
                                        mIntent.putExtra("userID", mUserId);
                                        startActivity(mIntent);
                                    }
                                } else {
                                    ToastUtil.showMessage("药液不足1L,请加注");
                                }
                            }
                        }
                    });
                }
            }
    }
}
