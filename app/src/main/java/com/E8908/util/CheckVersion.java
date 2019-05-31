package com.E8908.util;

import android.content.Context;
import android.os.Environment;

import com.E8908.conf.Constants;
import com.E8908.manage.DownloadUtil;
import com.E8908.thread.DownLoadUpListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by dell on 2018/5/9.
 */

public class CheckVersion {

    private static OkHttpClient build;
    private static CheckVersion mCheckVersion;
    private Context mContext;
    private DownLoadUpListener mDownLoadUpListener;

    public static CheckVersion getInstance() {
        if (mCheckVersion == null) {
            synchronized (CheckVersion.class) {
                if (mCheckVersion == null) {
                    mCheckVersion = new CheckVersion();
                }
            }
        }
        init();
        return mCheckVersion;
    }

    public static void init() {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .readTimeout(100, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS);
        build = client.build();
    }

    /**
     * 获取下载的信息
     */
    public void getDownLoadInfo(DownLoadUpListener downLoadListener, Context context, String apkUrl) {
        mContext = context;
        mDownLoadUpListener = downLoadListener;
        downloadFile(apkUrl);
    }


    /**
     * 下载文件
     *
     * @param appurl
     */
    private void downloadFile(String appurl) {

        File apkFile = new File(Environment.getExternalStorageDirectory(), Constants.DOWN_NAME + ".apk");

        DownloadUtil.getInstance().download(appurl, apkFile, new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File path) {
                mDownLoadUpListener.onSuccess(path);
            }

            @Override
            public void onDownloading(int progress,int tot) {
                mDownLoadUpListener.onProgress(progress,tot);
            }

            @Override
            public void onDownloadFailed() {
                mDownLoadUpListener.onFail();
            }
        });

    }
}
