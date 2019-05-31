package com.E8908.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.E8908.thread.ReadThread;
import com.E8908.thread.SendService;
import com.E8908.ui.ImageDownLoader;
import com.E8908.util.FileUtil;
import com.clj.fastble.BleManager;
import com.liulishuo.filedownloader.FileDownloader;
import com.squareup.picasso.Picasso;

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
    private static ExecutorService mExecutor = Executors.newSingleThreadExecutor();
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
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
        /*if (null != mInputStream) {
            IoUtils.closeFileStream(mInputStream);
            mInputStream = null;
        }
        if (null != mOutputStream) {
            IoUtils.closeFileStream(mOutputStream);
            mOutputStream = null;
        }*/
        if (null != mReadThread) {
            mReadThread.interrupt();
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
       /* JPushInterface.setDebugMode(false); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush*/
        FileUtil.deleteDownloadApkFile();
        Picasso.setSingletonInstance(new Picasso.Builder(mContext).
                downloader(new ImageDownLoader(getUnsafeOkHttpClient()))
                .build());
        //初始化蓝牙
        BleManager.getInstance().init(this);
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setOperateTimeout(5000);
       /* if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);*/
        //初始化文件下载器
        FileDownloader.setupOnApplicationOnCreate(this);
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
            mReadThread.setInputStream(mInputStream);
            mExecutor.execute(mReadThread);
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
