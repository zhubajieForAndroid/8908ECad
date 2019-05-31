package com.E8908.activity;

import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.E8908.R;
import com.E8908.base.BaseActivity;
import com.E8908.base.MyApplication;
import com.E8908.conf.Constants;
import com.E8908.impl.UpDataAppPersenterImpl;
import com.E8908.thread.DownLoadUpListener;
import com.E8908.ui.UpdataDialog;
import com.E8908.util.CheckVersion;
import com.E8908.util.DataUtil;
import com.E8908.util.FileUtil;
import com.E8908.util.NavigationBarUtil;
import com.E8908.util.OkhttpManager;
import com.E8908.util.SendUtil;
import com.E8908.view.UpdataView;
import com.E8908.widget.BitmapUtil;
import com.E8908.widget.ToastUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 关于设备页面
 */

public class AboutEquipmentActivity extends BaseActivity implements View.OnClickListener, UpdataView {
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
    private ProgressDialog mHintDialog;
    private FileDownloader mFileDownloader;
    private UpDataAppPersenterImpl mUpDataAppPersenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_equipment);
        ButterKnife.bind(this);
        mHintDialog = new ProgressDialog(this);
        mHintDialog.setMessage("加载中,请稍后");
        Window window = mHintDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER;
            wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(wlp);
        }
        FileDownloader.setup(this);
        mFileDownloader = FileDownloader.getImpl();
        mHandler = new Handler();
        mUpDataAppPersenter = new UpDataAppPersenterImpl(this);
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

    }

    private void initView() {
        bt_checkUpdate = findViewById(R.id.about_system_cheack_update);
        bt_back = findViewById(R.id.about_system_back);
        bt_checkUpdate.setOnClickListener(this);
        bt_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.about_system_cheack_update:               //检查更新
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);
                } else {
                    NavigationBarUtil.focusNotAle(mHintDialog.getWindow());
                    mHintDialog.show();
                    //显示虚拟栏的时候 隐藏
                    NavigationBarUtil.hideNavigationBar(mHintDialog.getWindow());
                    //再清理失能焦点
                    NavigationBarUtil.clearFocusNotAle(mHintDialog.getWindow());
                    SendUtil.controlVoice();
                    mUpDataAppPersenter.loadData();
                }
                break;
            case R.id.about_system_back:
                SendUtil.controlVoice();
                finish();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 12) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                NavigationBarUtil.focusNotAle(mHintDialog.getWindow());
                mHintDialog.show();
                //显示虚拟栏的时候 隐藏
                NavigationBarUtil.hideNavigationBar(mHintDialog.getWindow());
                //再清理失能焦点
                NavigationBarUtil.clearFocusNotAle(mHintDialog.getWindow());
                SendUtil.controlVoice();
                mUpDataAppPersenter.loadData();
            } else {
                ToastUtil.showMessage("无权限无法更新");
            }
        }

    }


    private void showDilog(String text, final String apkUrl) {
        UpdataDialog dialog = new UpdataDialog(this, R.style.dialog, text);
        dialog.setDownLaodButtonListener(new UpdataDialog.OnDownloadButtonListener() {
            @Override
            public void isDownload(boolean b) {
                if (b) {
                    mFileDownloader.pauseAll();
                    mFileDownloader.create(apkUrl).setPath(FileUtil.getUpdataPath().getAbsolutePath()).setListener(mFileDownloadListener).start();
                }
            }
        });
        dialog.show();
    }

    private FileDownloadListener mFileDownloadListener = new FileDownloadListener() {
        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            //等待，已经进入下载队列
            Log.d(TAG, "pending: 已经进入下载队列");
        }

        @Override
        protected void started(BaseDownloadTask task) {
            //结束了pending，并且开始当前任务的Runnable
            Log.d(TAG, "pending: 开始当前任务的Runnable");
        }

        @Override
        protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
            //已经连接上
            Log.d(TAG, "pending: 已经连接上");
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            mProgressDialog.setMax(totalBytes / 1024);
            mProgressDialog.setProgress(soFarBytes / 1024);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (!mProgressDialog.isShowing())
                        mProgressDialog.show();
                }
            });
            //下载进度回调
            Log.d(TAG, "pending: 下载中,文件大小=" + totalBytes + " 当前下载进度=" + soFarBytes);
        }

        @Override
        protected void blockComplete(BaseDownloadTask task) {
            //在完成前同步调用该方法，此时已经下载完成
            Log.d(TAG, "pending: 在完成前同步调用该方法，此时已经下载完成");
        }

        @Override
        protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
            //重试之前把将要重试是第几次回调回来
            Log.d(TAG, "pending: 重试之前把将要重试是第几次回调回来");
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            FileUtil.installApk(AboutEquipmentActivity.this, new File(task.getTargetFilePath()));
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            //暂停下载
            Log.d(TAG, "pending: 暂停下载释放所有资源");
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            //下载出现错误
            Log.d(TAG, "pending: 下载出现错误");
            Toast.makeText(AboutEquipmentActivity.this, "网络断开,请重新下载", Toast.LENGTH_SHORT).show();
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }

        @Override
        protected void warn(BaseDownloadTask task) {
            //在下载队列中(正在等待/正在下载)已经存在相同下载连接与相同存储路径的任务
            Log.d(TAG, "pending: 在下载队列中(正在等待/正在下载)已经存在相同下载连接与相同存储路径的任务");
        }
    };

    @Override
    public void onFaild(String msg) {
        ToastUtil.showMessage(msg);
        if (mHintDialog.isShowing())
            mHintDialog.dismiss();
    }

    @Override
    public void onSuccess(String downUrl, String upText) {
        showDilog(upText, downUrl);
        if (mHintDialog.isShowing())
            mHintDialog.dismiss();
    }
}
