package com.E8908.widget;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.E8908.R;
import com.E8908.activity.MainActivity;
import com.E8908.bean.BleInfoBean;
import com.E8908.blueTooth.SendBleDataService;
import com.E8908.blueTooth.adapter.BlueToothAdapter;
import com.E8908.util.StringUtils;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.scan.BleScanRuleConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LinkBleDialog extends Dialog implements BlueToothAdapter.OnBleItemClcikListener {
    private Context mContext;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.radar)
    RadarView mRadar;
    private BlueToothAdapter mAdapter;
    private ArrayList<BleDevice> mBleDevices;

    public LinkBleDialog( Context context) {
        super(context);
        mContext = context;
    }

    public LinkBleDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_link_ble_dialog);
        ButterKnife.bind(this);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.gravity = Gravity.CENTER;
            attributes.width = 1000;
            attributes.height = 500;
            window.setAttributes(attributes);
        }
        initData();
    }

    private void initData() {
        mBleDevices = new ArrayList<>();
        mAdapter = new BlueToothAdapter(mContext, mBleDevices);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 4);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnBleItemClcikListener(this);

        mRadar.setVisibility(View.VISIBLE);
        mRadar.start();
        //初始化蓝牙
        initBlue();
    }
    private void initBlue() {

        //蓝牙扫描配置
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setScanTimeOut(3000)              // 扫描超时时间，可选，默认10秒
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);

        //是否支持ble蓝牙
        boolean supportBle = BleManager.getInstance().isSupportBle();
        if (supportBle) {
            //蓝牙是否开启
            boolean blueEnable = BleManager.getInstance().isBlueEnable();
            if (!blueEnable) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                Activity ownerActivity = (MainActivity)mContext;
                if (ownerActivity != null)
                    ownerActivity.startActivityForResult(intent, 121);
            } else {
                BleManager.getInstance().scan(mBleScanCallback);
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
                if (!TextUtils.isEmpty(name) && name.length() == 8 && !isChinese(name) && !isSpecialChar(name)){
                    mBleDevices.add(bleDevice);
                }
            }
            if (mRadar != null){
                mRadar.stop();
                mRadar.setVisibility(View.GONE);
            }
            mRecyclerView.setVisibility(View.VISIBLE);
            if (mBleDevices.size() >0 ) {
                mAdapter.notifyDataSetChanged();
            }else {
                ToastUtil.showMessage("没有发现检测仪");
            }
        }

        @Override
        public void onScanStarted(boolean success) {}

        @Override
        public void onScanning(BleDevice bleDevice) {}
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
    public void dismiss() {
        ButterKnife.unbind(this);
        if (mRadar != null){
            mRadar.stop();
            mRadar.setVisibility(View.GONE);
        }
        super.dismiss();
    }

    @Override
    public void bleItemClick(int selectPosition) {
        dismiss();
        ToastUtil.showMessage("正在连接");
        BleDevice device = mAdapter.getDevice(selectPosition);
        if (device == null)
            return;
        Intent bleIntent = new Intent(mContext, SendBleDataService.class);
        bleIntent.putExtra("BleDeviceMac", device.getMac());
        mContext.startService(bleIntent);
    }
}
