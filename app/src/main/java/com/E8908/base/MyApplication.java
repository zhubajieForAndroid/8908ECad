package com.E8908.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.E8908.R;
import com.E8908.activity.MainActivity;
import com.E8908.activity.UpgradeActivity;
import com.E8908.factory.ThreadPoolProxyFactory;
import com.E8908.manage.MyLocationListener;
import com.E8908.manage.SocketManage;
import com.E8908.thread.ReadThread;
import com.E8908.thread.SendService;
import com.E8908.ui.ImageDownLoader;
import com.E8908.util.FileUtil;
import com.E8908.util.StringUtils;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.clj.fastble.BleManager;
import com.squareup.picasso.Picasso;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.upgrade.UpgradeListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android_serialport_api.SerialPort;
import okhttp3.OkHttpClient;

/**
 * Created by dell on 2016/12/26.
 */

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    private static Context mContext;
    private static Handler mHandler;
    private static SerialPort mSerialPort = null;
    private static InputStream mInputStream;
    private static OutputStream mOutputStream;

    private static ReadThread mReadThread;

    private static Intent startIntent;


    public static InputStream getInputStream() {
        return mInputStream;
    }

    public static OutputStream getOutputStream() {
        return mOutputStream;
    }

    /**
     * 获取全局上下文
     *
     * @return
     */
    public static Context getContext() {
        return mContext;
    }

    public static Handler getmHandler() {
        return mHandler;
    }

    public static SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
            mSerialPort = new SerialPort(new File("/dev/ttyS1"), 19200, 0);
        }
        return mSerialPort;
    }

    public static void closeSerialPort() {
        if (null != mReadThread) {
            ThreadPoolProxyFactory.getmThreadPoolProxy().removeTask(mReadThread);
            mReadThread.setInputStream(mInputStream,false);
            mReadThread = null;
        }
        if (null != startIntent) {
            mContext.stopService(startIntent);
        }
    }

    /**
     * 程序入口,初始化上下文
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
        mContext = getApplicationContext();

        //异步初始化
        ThreadPoolProxyFactory.getmThreadPoolProxy().submit(new InitTask());
    }
    public class InitTask  implements Runnable{
        @Override
        public void run() {
            Picasso.setSingletonInstance(new Picasso.Builder(mContext).
                    downloader(new ImageDownLoader(getUnsafeOkHttpClient()))
                    .build());
            //初始化蓝牙
            BleManager.getInstance().init(MyApplication.this);
            BleManager.getInstance()
                    .enableLog(true)
                    .setReConnectCount(1, 5000)
                    .setOperateTimeout(5000);

       /* if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);*/

            //初始化socket
            SocketManage.getSocketManage().init("116.62.209.62",20002);//192.168.2.166  116.62.209.62
            //腾讯升级
            initBug();
            //初始化百度定位
            initBaidu();
        }
    }

    private void initBaidu() {
        LocationClient client = new LocationClient(getApplicationContext());
        client.registerLocationListener(new MyLocationListener());
        LocationClientOption option = new LocationClientOption();
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标
        option.setCoorType("bd09ll");
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效 1分钟定位一次
        option.setScanSpan(60000);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true
        option.setOpenGps(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        option.setLocationNotify(true);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        option.setIgnoreKillProcess(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false
        option.SetIgnoreCacheException(false);
        //可选，V7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位
        option.setWifiCacheTimeOut(5*60*1000);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
        option.setEnableSimulateGps(false);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
        client.setLocOption(option);
        //开始定位
        client.start();
    }
    private void initBug() {
        Beta.upgradeListener = new UpgradeListener() {
            /**
             * 接收到更新策略
             * @param i  0:正常 －1:请求失败
             * @param upgradeInfo 更新策略
             * @param isManual true:手动请求 false:自动请求
             * @param isSilence true:不弹窗 false:弹窗
             * @return 是否放弃SDK处理此策略，true:SDK将不会弹窗，策略交由app自己处理
             */
            @Override
            public void onUpgrade(int i, UpgradeInfo upgradeInfo, boolean isManual, boolean isSilence) {
                if (i == 0 && !isSilence && upgradeInfo != null && !TextUtils.isEmpty(upgradeInfo.newFeature)) {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), UpgradeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("info",upgradeInfo.newFeature);
                    intent.putExtra("upType",upgradeInfo.upgradeType);
                    startActivity(intent);
                }
            }
        };
        //true表示初始化时自动检查升级; false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
        Beta.autoCheckUpgrade = false;
        //关闭热更新能力
        Beta.enableHotfix = false;
        //sau 826006474b
        //途康 00571ef987
        //托福士 13dab46de8
        /***** 统一初始化Bugly产品，包含Beta *****/
        Bugly.init(this, "00571ef987", false);

    }

    public static void initSerialPort() {
        try {
            FileUtil.putSendState(true);
            //启动发送数据的服务
            startIntent = new Intent(mContext, SendService.class);
            mContext.startService(startIntent);

            SerialPort serialPort = getSerialPort();
            mInputStream = serialPort.getInputStream();
            mOutputStream = serialPort.getOutputStream();
            if (mReadThread == null)
                mReadThread = new ReadThread();
            mReadThread.setInputStream(mInputStream,true);
            ThreadPoolProxyFactory.getmThreadPoolProxy().submit(mReadThread);
        } catch (SecurityException e) {
            Toast.makeText(getContext(), "没有串口权限", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getContext(), "无法打开一个未知的串口", Toast.LENGTH_SHORT).show();
        } catch (InvalidParameterException e) {
            Toast.makeText(getContext(), "请先配置串口", Toast.LENGTH_SHORT).show();
        }
    }

    public static OkHttpClient getUnsafeOkHttpClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }};

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts,
                    new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext
                    .getSocketFactory();
            OkHttpClient okHttpClient = new OkHttpClient.Builder().sslSocketFactory(sslSocketFactory, new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }).hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            }).build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
