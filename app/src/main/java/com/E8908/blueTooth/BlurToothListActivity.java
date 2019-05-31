package com.E8908.blueTooth;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.E8908.activity.MaintainThreeActivity;
import com.E8908.blueTooth.bean.YunInfoBean;
import com.E8908.blueTooth.dao.ResultQueryYunBean;
import com.E8908.blueTooth.dao.YunDataDao;
import com.E8908.blueTooth.utils.RadarView;
import com.E8908.R;

import com.E8908.blueTooth.adapter.BlueToothAdapter;
import com.E8908.activity.CheckGasActivity;
import com.E8908.conf.Constants;
import com.E8908.util.FileUtil;
import com.E8908.util.IoUtils;
import com.E8908.util.NavigationBarUtil;
import com.E8908.util.OkhttpManager;
import com.E8908.util.SendUtil;
import com.E8908.widget.ToastUtil;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BlurToothListActivity extends BlueToothBase implements View.OnClickListener, BlueToothAdapter.OnBleItemClcikListener {
    private static final String TAG = "BlurToothListActivity";
    private String[] permissions = {Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    private RadarView mRadarView;
    private RecyclerView mRecyclerView;
    private BlueToothAdapter mAdapter;
    private Button mBackBtn;
    private List<BleDevice> mBleDevices;

    private BleManager mBleManager;
    private String mUuid_service;
    private String mUuid_chara;
    private String mEquipmentId;
    private String mCarNumber = "粤B12346";
    private Button selectCarNumberBtn;
    private Button nextBtn;
    private boolean isLinkBle;
    private boolean mIsRoutine;
    private String macAddress;
    private File mE8908BleMac;
    private Button switchBle;
    private ProgressDialog mLinkBleDialog;

    @Override
    protected void initViews() {
        mBackBtn = findViewById(R.id.back);
        mBackBtn.setOnClickListener(this);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRadarView = findViewById(R.id.radar);
        selectCarNumberBtn = findViewById(R.id.selecet_carnumber);
        nextBtn = findViewById(R.id.next);
        switchBle = findViewById(R.id.switch_ble);
        selectCarNumberBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        switchBle.setOnClickListener(this);
        mBleDevices = new ArrayList<>();
        Intent intent = getIntent();
        mEquipmentId = intent.getStringExtra("equipmentId");
        mIsRoutine = intent.getBooleanExtra("isRoutine", false);
        mLinkBleDialog = new ProgressDialog(this);
        mLinkBleDialog.setMessage("蓝牙连接中");
        initBlue();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.BLE_ACTIVITY_STATE);
        registerReceiver(receiver, intentFilter);

    }
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };



    private void initBlue() {
        mBleManager = BleManager.getInstance();
        mE8908BleMac = FileUtil.createNewFile("E8908BleMac", "bleAddress.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(mE8908BleMac));
            macAddress = br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(macAddress)){                //已经连接过蓝牙了
            mBleManager.connect(macAddress, mBleGattCallback);
            mRadarView.stop();
            mRadarView.setVisibility(View.GONE);
        }else {
            scanBle();
        }

    }

    private void scanBle() {
        //蓝牙扫描配置
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setScanTimeOut(5000)              // 扫描超时时间，可选，默认10秒
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

    @Override
    protected void initData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //请求权限
            requestPhonePermission();
        }
        mAdapter = new BlueToothAdapter(this, mBleDevices);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnBleItemClcikListener(this);
    }

    private BleScanCallback mBleScanCallback = new BleScanCallback() {
        @Override
        public void onScanFinished(List<BleDevice> scanResultList) {
            for (int i = 0; i < scanResultList.size(); i++) {
                BleDevice bleDevice = scanResultList.get(i);
                String name = bleDevice.getName();
                if (!TextUtils.isEmpty(name) && name.startsWith("CAD")) {
                    //得到扫描设备集合
                    mBleDevices.add(bleDevice);
                }
            }
            mRadarView.stop();
            mRadarView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onScanStarted(boolean success) {
            if (success) {
                mRadarView.setVisibility(View.VISIBLE);
                mRadarView.start();
            }
        }

        @Override
        public void onScanning(BleDevice bleDevice) {

        }
    };

    @Override
    protected int getResID() {
        return R.layout.activity_bluetooth;
    }

    private void requestPhonePermission() {
        List<String> noPermissions = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (ActivityCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                noPermissions.add(permissions[i]);
            }
        }
        if (!noPermissions.isEmpty()) {
            //有没有授权的权限,去请求权限
            String[] strings = noPermissions.toArray(new String[noPermissions.size()]);
            ActivityCompat.requestPermissions(this, strings, 102);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Integer> hasYesPermission = new ArrayList<>();
        if (requestCode == 102) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    hasYesPermission.add(i);
                }
            }
            if (!hasYesPermission.isEmpty()) {
                Toast.makeText(this, "无权限有些功能将无法使用", Toast.LENGTH_SHORT).show();
                hasYesPermission.clear();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == -1) {
                BleManager.getInstance().scan(mBleScanCallback);
            } else {
                ToastUtil.showMessage("不开启蓝牙,无法使用");
                finish();
            }
        }
    }




    private BleGattCallback mBleGattCallback = new BleGattCallback() {
        @Override
        public void onStartConnect() {
            //开始连接
            if (!mLinkBleDialog.isShowing()) {
                NavigationBarUtil.focusNotAle(mLinkBleDialog.getWindow());
                mLinkBleDialog.show();
                //显示虚拟栏的时候 隐藏
                NavigationBarUtil.hideNavigationBar(mLinkBleDialog.getWindow());
                //再清理失能焦点
                NavigationBarUtil.clearFocusNotAle(mLinkBleDialog.getWindow());
            }
        }

        @Override
        public void onConnectFail(BleDevice bleDevice, BleException exception) {
            //连接失败
            ToastUtil.showMessage("连接蓝牙失败");
            isLinkBle = false;
            if (mLinkBleDialog.isShowing())
                mLinkBleDialog.dismiss();
        }

        @Override
        public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
            if (mLinkBleDialog.isShowing())
                mLinkBleDialog.dismiss();
            ToastUtil.showMessage("蓝牙连接成功");
            //连接成功并发现服务。
            List<BluetoothGattService> serviceList = gatt.getServices();
            for (BluetoothGattService service : serviceList) {
                mUuid_service = service.getUuid().toString();
                List<BluetoothGattCharacteristic> characteristicList = service.getCharacteristics();
                for (BluetoothGattCharacteristic characteristic : characteristicList) {
                    mUuid_chara = characteristic.getUuid().toString();
                }
            }
            SystemClock.sleep(100);
            Intent intent = new Intent(BlurToothListActivity.this, SendBleDataService.class);
            intent.putExtra("device", bleDevice);
            intent.putExtra("serviceUUID", mUuid_service);
            intent.putExtra("charaUUID", mUuid_chara);
            intent.putExtra("equipmentID", mEquipmentId);
            intent.putExtra("carNumber", mCarNumber);
            startService(intent);
            isLinkBle = true;
            String mac = bleDevice.getMac();
            //保存蓝牙地址到本地
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(mE8908BleMac));
                bw.write(mac);
                IoUtils.closeFileStream(bw);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
            ToastUtil.showMessage("蓝牙已断开");
            isLinkBle = false;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.selecet_carnumber:            //选择车牌
                //根据车牌请求云店信息

                break;
            case R.id.next:                         //下一步
                if (!TextUtils.isEmpty(mCarNumber)) {
                    if (isLinkBle) {
                        SendUtil.controlVoice();
                        Intent intent = new Intent(this, MaintainThreeActivity.class);
                        intent.putExtra("isRoutine", mIsRoutine);
                        startActivity(intent);
                    } else {
                        ToastUtil.showMessage("请连接蓝牙");
                    }
                } else {
                    ToastUtil.showMessage("请选择车牌号");
                }
                break;
            case R.id.switch_ble:               //切换蓝牙
                if (!TextUtils.isEmpty(macAddress)) {               //已经连接过才可以切换蓝牙
                    mBleDevices.clear();
                    mAdapter.notifyDataSetChanged();
                    Intent intent = new Intent(this, SendBleDataService.class);
                    stopService(intent);
                    mBleManager.destroy();
                    FileUtil.deleteFile(mE8908BleMac);
                    scanBle();
                }
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, SendBleDataService.class);
        stopService(intent);
        mBleManager.destroy();
    }

    @Override
    public void bleItemClick(int selectPosition) {
        BleDevice device = mAdapter.getDevice(selectPosition);
        if (device == null)
            return;
        //连接蓝牙
        mBleManager = BleManager.getInstance();
        mBleManager.connect(device, mBleGattCallback);
    }
}
