package com.E8908.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.adapter.CarNumberAdapter;
import com.E8908.base.BaseActivity;
import com.E8908.bean.YunInfoBean;
import com.E8908.blueTooth.adapter.BlueToothAdapter;
import com.E8908.conf.Constants;
import com.E8908.util.DataUtil;
import com.E8908.util.NavigationBarUtil;
import com.E8908.util.OkhttpManager;
import com.E8908.util.SendUtil;
import com.E8908.widget.ToastUtil;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MaintainThreeReadActivityDemo extends BaseActivity implements View.OnTouchListener, BlueToothAdapter.OnBleItemClcikListener, CarNumberAdapter.OnCarNumberItemClickListener, View.OnClickListener {

    private static final String TAG = "MaintainThreeActivity";
    @Bind(R.id.toobar_bg_image)
    ImageView mToobarBgImage;
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
    @Bind(R.id.bg_touch)
    ImageView mBgTouch;
    @Bind(R.id.temperature_state)
    TextView mTemperatureState;
    @Bind(R.id.recycler_car_number)
    RecyclerView mRecyclerCarNumber;
    @Bind(R.id.recycler_ble)
    RecyclerView mRecyclerBle;
    @Bind(R.id.blue_name)
    TextView mBlueName;
    @Bind(R.id.no_scan)
    LinearLayout mNoScan;
    @Bind(R.id.scan_ble)
    Button mScanBle;
    @Bind(R.id.ble_radio)
    CheckBox mBleRadio;
    @Bind(R.id.progress_bar)
    LinearLayout mProgressBar;
    private boolean mIsRoutine;
    private boolean mIsYesData = false;
    private String mEquipmentId;
    private BleManager mBleManager;
    private ProgressDialog mProgressDialog;
    private List<BleDevice> mBleDevices;
    private List<YunInfoBean.ObjBean> mCarNumbers;
    private BlueToothAdapter mBlueToothAdapter;
    private CarNumberAdapter mCarNumberAdapter;
    private Handler mHandler = new Handler();
    private boolean isSelectCarNumber;          //是否选中车牌
    private boolean isSelectBle;          //是否选中蓝牙
    private int mSelectBlePosition = -1;
    private int mSelectCarNumberPosition = -1;
    private SharedPreferences mDeviceInfoSp;
    private boolean isScanBle = true;           //是否扫描了蓝牙
    private String mDeviceMac;
    private String mDeviceName;
    private boolean isLoadData = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintain_one_read);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mIsRoutine = intent.getBooleanExtra("isRoutine", false);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("数据加载中");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mBleDevices = new ArrayList<>();
        mCarNumbers = new ArrayList<>();
        initData();
    }

    private void initData() {
        mBgTouch.setOnTouchListener(this);
        mScanBle.setOnClickListener(this);
        mNoScan.setOnClickListener(this);
        mToobarBgImage.setImageResource(R.mipmap.bg_cg_top_3);
        mBgTouch.setImageResource(R.mipmap.three_work_read);

        mBlueToothAdapter = new BlueToothAdapter(this, mBleDevices);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 6);
        mRecyclerBle.setLayoutManager(layoutManager);
        mRecyclerBle.setAdapter(mBlueToothAdapter);
        mBlueToothAdapter.setOnBleItemClcikListener(this);

        GridLayoutManager carlayoutManager = new GridLayoutManager(this, 6);
        mCarNumberAdapter = new CarNumberAdapter(this, mCarNumbers);
        mRecyclerCarNumber.setLayoutManager(carlayoutManager);
        mRecyclerCarNumber.setAdapter(mCarNumberAdapter);
        mCarNumberAdapter.setOnitemClickListener(this);

        mDeviceInfoSp = getSharedPreferences("BledeviceInfo", 0);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTIVITY_STATE);
        registerReceiver(receiver, intentFilter);

        initBlue();
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    private void loadData() {
        mProgressBar.setVisibility(View.VISIBLE);
        Map<String, String> pames = new HashMap<>();
        pames.put("equipmentId", mEquipmentId);
        OkhttpManager.getOkhttpManager().doPost(Constants.URLS.GET_INFO_BY_EQUIPMENT, pames, mCallback);
    }

    private Callback mCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setVisibility(View.GONE);
                    ToastUtil.showMessage("网络异常");
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            boolean successful = response.isSuccessful();
            if (successful) {
                String string = response.body().string();
                if (!TextUtils.isEmpty(string) && !"".equals(string)) {
                    Gson gson = new Gson();
                    final YunInfoBean yunInfoBean = gson.fromJson(string, YunInfoBean.class);
                    boolean success = yunInfoBean.isSuccess();
                    if (success) {
                        mCarNumbers.addAll(yunInfoBean.getObj());
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mProgressBar.setVisibility(View.GONE);
                                mCarNumberAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mProgressBar.setVisibility(View.GONE);
                                ToastUtil.showMessage(yunInfoBean.getMsg());
                            }
                        });
                    }
                }
            }else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                        ToastUtil.showMessage("服务器错误");
                    }
                });
            }
        }
    };

    private void initBlue() {
        mDeviceName = mDeviceInfoSp.getString("deviceName", "");
        if (!TextUtils.isEmpty(mDeviceName) && !"".equals(mDeviceName)) {          //已经有蓝牙了
            isScanBle = false;
            isSelectBle = true;         //如果本地已经保存就默认选中了蓝牙
            mNoScan.setVisibility(View.VISIBLE);
            mRecyclerBle.setVisibility(View.GONE);
            mDeviceMac = mDeviceInfoSp.getString("deviceMac", "");

            mBlueName.setText(mDeviceName);
        } else {
            isScanBle = true;
            mNoScan.setVisibility(View.GONE);
            mRecyclerBle.setVisibility(View.VISIBLE);
            mBleManager = BleManager.getInstance();
            scanBle();
        }

    }

    private void scanBle() {
        //蓝牙扫描配置
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setScanTimeOut(3000)              // 扫描超时时间，可选，默认10秒
                .build();
        mBleManager.initScanRule(scanRuleConfig);

        //是否支持ble蓝牙
        boolean supportBle = mBleManager.isSupportBle();
        if (supportBle) {
            //蓝牙是否开启
            boolean blueEnable = mBleManager.isBlueEnable();
            if (!blueEnable) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, 10);
            } else {
                mBleManager.scan(mBleScanCallback);
            }
        }
    }

    private BleScanCallback mBleScanCallback = new BleScanCallback() {
        @Override
        public void onScanFinished(List<BleDevice> scanResultList) {
            mBleDevices.clear();
            for (int i = 0; i < scanResultList.size(); i++) {
                BleDevice bleDevice = scanResultList.get(i);
                String name = bleDevice.getName();
                if (!TextUtils.isEmpty(name) && name.length() == 8 && !isChinese(name) && !isSpecialChar(name)) {
                    mBleDevices.add(bleDevice);
                }
            }
            mBlueToothAdapter.notifyDataSetChanged();
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }

        @Override
        public void onScanStarted(boolean success) {
            if (success) {
                if (!mProgressDialog.isShowing()) {
                    NavigationBarUtil.focusNotAle(mProgressDialog.getWindow());
                    mProgressDialog.show();
                    //显示虚拟栏的时候 隐藏
                    NavigationBarUtil.hideNavigationBar(mProgressDialog.getWindow());
                    //再清理失能焦点
                    NavigationBarUtil.clearFocusNotAle(mProgressDialog.getWindow());
                }
            }
        }

        @Override
        public void onScanning(BleDevice bleDevice) {

        }
    };
    /**
     * 判断是否含有特殊字符
     *
     * @param str
     * @return true为包含，false为不包含
     */
    public static boolean isSpecialChar(String str) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }
    /**
     * 是否包含中文
     *
     * @param str
     * @return
     */
    public static boolean isChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
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
    public void onDataReceived(final byte[] buffer, int size) {
        mIsYesData = true;
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
        if (isdata && mIsYesData) {        //成功
            mMessageState.setText("正常");
            mMessageState.setTextColor(Color.parseColor("#fd0fc602"));
        } else {             //失败
            mMessageState.setText("断开");
            mMessageState.setTextColor(Color.parseColor("#fdfa0310"));
        }
        mIsYesData = false;
    }

    private void analysisData(byte[] buffer) {
        String state = DataUtil.getState(buffer);                           //状态位


        int signalStrength = DataUtil.getSignalStrength(buffer);            //获取信号强度

        mEquipmentId = DataUtil.getEquipmentNumber(buffer);
        if (isLoadData) {
            loadData();
            isLoadData = false;
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if ((x >= 25 && x <= 290) && (y >= 660 && y <= 795)) {                //返回
            finish();
        } else if ((x >= 540 && x <= 1230) && (y >= 709 && y <= 795)) {         //下一步
            //判断蓝牙和车牌号是否选中
            SendUtil.controlVoice();
            Intent intent = new Intent(this, MaintainThreeActivity.class);
            intent.putExtra("isRoutine", mIsRoutine);
            if (isSelectCarNumber) {                //是否选中了车牌
                intent.putExtra("pk", mCarNumbers.get(mSelectCarNumberPosition).getPk());
                intent.putExtra("shopName", mCarNumbers.get(mSelectCarNumberPosition).getSimpleName());
                intent.putExtra("carNumber", mCarNumbers.get(mSelectCarNumberPosition).getCarNo());
            }
            if (isSelectBle) {          //是否选中了蓝牙
                if (isScanBle) {
                    intent.putExtra("BleDeviceMac", mBleDevices.get(mSelectBlePosition).getMac());
                } else {
                    if (mBleRadio.isChecked()) {        //本地保存了蓝牙地址并且选中了
                        intent.putExtra("BleDeviceMac", mDeviceMac);
                    }
                }
            }
            intent.putExtra("equipmentID", mEquipmentId);
            startActivity(intent);

        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 10) {
            mBleManager.scan(mBleScanCallback);
        }
    }

    @Override
    public void bleItemClick(int selectPosition) {                  //蓝牙选中结果
        if (selectPosition != -1) {
            mSelectBlePosition = selectPosition;
            isSelectBle = true;
        } else {
            isSelectBle = false;
        }
    }

    @Override
    public void carNumberItemClick(int selectPosition) {            //车牌选中结果
        if (selectPosition != -1) {
            mSelectCarNumberPosition = selectPosition;
            isSelectCarNumber = true;
        } else {
            isSelectCarNumber = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View v) {           //扫描蓝牙
        switch (v.getId()) {
            case R.id.scan_ble:
                isScanBle = true;
                isSelectBle = false;
                mNoScan.setVisibility(View.GONE);
                mRecyclerBle.setVisibility(View.VISIBLE);
                mBleManager = BleManager.getInstance();
                scanBle();
                break;
            case R.id.no_scan:
                if (mBleRadio.isChecked()) {
                    mBleRadio.setChecked(false);
                } else {
                    mBleRadio.setChecked(true);
                }
                break;
        }

    }
}
