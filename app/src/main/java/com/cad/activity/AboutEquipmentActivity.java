package com.cad.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cad.R;
import com.cad.base.BaseActivity;
import com.cad.base.MyApplication;
import com.cad.conf.Constants;
import com.cad.conf.Protocol;
import com.cad.ui.UpdataDialog;
import com.cad.util.CheckVersion;
import com.cad.util.DataUtil;
import com.cad.util.HttpUtil;
import com.cad.util.SendUtil;
import com.cad.widget.BitmapUtil;
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

/**
 * 关于设备页面
 */

public class AboutEquipmentActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "AboutEquipmentActivity";
    @Bind(R.id.toobar_bg_image)
    ImageView mToobarBgImage;
    @Bind(R.id.device_app_version_1)
    ImageView mDeviceAppVersion1;
    @Bind(R.id.device_app_version_2)
    ImageView mDeviceAppVersion2;
    //硬件版本

    @Bind(R.id.device_hardware_version_2)
    ImageView mDeviceHardwareVersion2;
    //主控板版本
    @Bind(R.id.device_software_version_2)
    ImageView mDeviceSoftwareVersion2;
    //设备序列号
    @Bind(R.id.devices_sequence_number2)
    ImageView mDevicesSequenceNumber2;
    @Bind(R.id.devices_sequence_number3)
    ImageView mDevicesSequenceNumber3;
    @Bind(R.id.devices_sequence_number4)
    ImageView mDevicesSequenceNumber4;
    @Bind(R.id.devices_sequence_number5)
    ImageView mDevicesSequenceNumber5;
    @Bind(R.id.devices_sequence_number6)
    ImageView mDevicesSequenceNumber6;
    @Bind(R.id.devices_sequence_number7)
    ImageView mDevicesSequenceNumber7;
    @Bind(R.id.devices_sequence_number8)
    ImageView mDevicesSequenceNumber8;
    //日期
    @Bind(R.id.devices_init_date2)
    ImageView mDevicesInitDate2;
    @Bind(R.id.devices_init_date3)
    ImageView mDevicesInitDate3;
    @Bind(R.id.devices_init_date4)
    ImageView mDevicesInitDate4;
    @Bind(R.id.devices_init_date5)
    ImageView mDevicesInitDate5;
    @Bind(R.id.devices_init_date6)
    ImageView mDevicesInitDate6;
    @Bind(R.id.devices_init_date7)
    ImageView mDevicesInitDate7;
    @Bind(R.id.devices_init_date8)
    ImageView mDevicesInitDate8;
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
    @Bind(R.id.device_hardware_version_1)
    ImageView mDeviceHardwareVersion1;
    @Bind(R.id.device_software_version_1)
    ImageView mDeviceSoftwareVersion1;
    @Bind(R.id.devices_sequence_number)
    ImageView mDevicesSequenceNumber;
    @Bind(R.id.devices_init_date)
    ImageView mDevicesInitDate;
    @Bind(R.id.system_many_data)
    RelativeLayout mSystemManyData;
    @Bind(R.id.about_system_cheack_update)
    ImageButton mAboutSystemCheackUpdate;
    @Bind(R.id.about_system_back)
    ImageButton mAboutSystemBack;
    @Bind(R.id.temperature_state)
    TextView mTemperatureState;
    private ImageButton bt_checkUpdate, bt_back;
    private boolean mIsYesData = false;
    private ProgressDialog mProgressDialog;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_equipment);
        ButterKnife.bind(this);
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
        //2A 28 03 8B 1E F7 08 03 98 46 50 00 00 00 00 00 00 28 05 06 04 08 07 05 FF 30 30 30 30 30 30 30 30 25 0A 12 03 10 12 23
        String state = DataUtil.getState(buffer);                           //状态位
        int signalStrength = DataUtil.getSignalStrength(buffer);            //获取信号强度
        String equipmentNumber = DataUtil.getEquipmentNumber(buffer);       //获取序列号
        int vwesionToHardware = DataUtil.getVwesionToHardware(buffer);      //获取硬件版本
        int vwesionToSoftware = DataUtil.getVwesionToSoftware(buffer);      //获取主控版本
        int dateYear = DataUtil.getDateYear(buffer);                             //获取出厂日期年
        int dateMonth = DataUtil.getDateMonth(buffer);                             //获取出厂日期月
        int dateDay = DataUtil.getDateDay(buffer);                             //获取出厂日期日
        //设置硬件版本图片
        BitmapUtil.numberToBItmapTwo(vwesionToHardware, mDeviceHardwareVersion1, mDeviceHardwareVersion2);
        //设置主控版本图片
        BitmapUtil.numberToBItmapTwo(vwesionToSoftware, mDeviceSoftwareVersion1, mDeviceSoftwareVersion2);
        //设置序列号
        BitmapUtil.setEquipmentNumber(equipmentNumber, mDevicesSequenceNumber, mDevicesSequenceNumber2, mDevicesSequenceNumber3, mDevicesSequenceNumber4, mDevicesSequenceNumber5
                , mDevicesSequenceNumber6, mDevicesSequenceNumber7, mDevicesSequenceNumber8);
        //设置日期年
        BitmapUtil.numberToBItmapTwo(dateYear, mDevicesInitDate3, mDevicesInitDate4);
        //设置日期月
        BitmapUtil.numberToBItmapTwo(dateMonth, mDevicesInitDate5, mDevicesInitDate6);
        //设置日期日
        BitmapUtil.numberToBItmapTwo(dateDay, mDevicesInitDate7, mDevicesInitDate8);
        String connectServerState = state.substring(3, 4);                  //连接服务器状态
        String activationState = state.substring(4, 5);                     //设备激活状态
        String lockState = state.substring(5, 6);                           //设备锁定状态
        //00011000
        String afterLockState = state.substring(1, 2);                       //后门锁定状态
        String frontLockState = state.substring(2, 3);                       //前门锁定状态
        String temperatureState = state.substring(0, 1);                       //温度传感器连接状态
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
    }

    private void initData() {
        mToobarBgImage.setImageResource(R.mipmap.top_bar_5);
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            String versionName = packageInfo.versionName;
            String[] split = versionName.split("\\.");
            BitmapUtil.numberToBItmapOne(Integer.parseInt(split[1]), mDeviceAppVersion2);
            BitmapUtil.numberToBItmapOne(Integer.parseInt(split[0]), mDeviceAppVersion1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        mProgressDialog.setMax(msg.arg2);
                        mProgressDialog.setProgress(msg.arg1);
                        mProgressDialog.show();
                        break;
                }
            }
        };

    }

    private void initView() {
        bt_checkUpdate = (ImageButton) findViewById(R.id.about_system_cheack_update);
        bt_back = (ImageButton) findViewById(R.id.about_system_back);
        bt_checkUpdate.setOnClickListener(this);
        bt_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.about_system_cheack_update:               //检查更新
                SendUtil.controlVoice();
                checkversion(true);
                //HttpUtil.performNetRequest(this,mHandler,mProgressDialog);
                break;
            case R.id.about_system_back:
                SendUtil.controlVoice();
                finish();
                break;
        }
    }
    private void checkversion(final boolean userState){
        Map<String, Object> params = new HashMap<>();
        params.put("appname", Constants.UPLOAD_NAME);
        params.put("url", Constants.URLS.CHECKAPK_URL);
        Protocol protocol = new Protocol() {
            @Override
            public void errorManage(IOException e) {
            }

            @Override
            public void parseData(Gson gson, String s) {
                try {
                    JSONArray jsonArray = new JSONObject(s).getJSONArray("response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String appver = jsonObject.getString("appver");
                        final String appurl = jsonObject.getString("appurl");
                        final String updatecontent = jsonObject.getString("updatecontent");
                        if (!TextUtils.isEmpty(appurl)) {
                            try {
                                PackageInfo info = MyApplication.getContext().getPackageManager().getPackageInfo(MyApplication.getContext().getPackageName(), 0);
                                final float localVersion = Float.parseFloat(info.versionName);
                                final float mServerVersionInt = Float.parseFloat(appver);
                                if (mServerVersionInt > localVersion) {         //需要更新
                                    if (userState){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                showDilog(updatecontent,userState);
                                            }
                                        });
                                    }
                                }else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtil.showMessage("已经是最新版本");
                                        }
                                    });
                                }
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        protocol.setParams(params);
        protocol.loadDataFromNet();
    }

    private void showDilog(String text, final boolean userState) {
        UpdataDialog dialog = new UpdataDialog(this,R.style.dialog,text);
        dialog.setDownLaodButtonListener(new UpdataDialog.OnDownloadButtonListener() {
            @Override
            public void isDownload(boolean b) {
                if (b){
                    CheckVersion checkVersion = CheckVersion.getInstance();
                    checkVersion.getDownLoadInfo(mHandler,userState);
                }
            }
        });
        dialog.show();
    }
}
