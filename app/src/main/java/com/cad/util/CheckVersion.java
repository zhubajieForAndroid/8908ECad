package com.cad.util;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.cad.base.MyApplication;
import com.cad.conf.Constants;
import com.cad.conf.Protocol;
import com.cad.thread.DownThread;
import com.cad.widget.ToastUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by dell on 2018/5/9.
 */

public class CheckVersion {

    private InputStream inputStream;
    private FileOutputStream fos;
    private static OkHttpClient build;
    private static CheckVersion mCheckVersion;
    private DownThread mThread;

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
    public void getDownLoadInfo(final Handler handler, final boolean userState) {
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
                        String appurl = jsonObject.getString("appurl");

                        if (!TextUtils.isEmpty(appurl)) {
                            checkVersion(appver, appurl, handler, userState);
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

    /**
     * 检查版本
     *
     * @param appver
     * @param appurl
     */
    private void checkVersion(String appver, final String appurl, final Handler handler, boolean userState) {
        try {
            PackageInfo info = MyApplication.getContext().getPackageManager().getPackageInfo(MyApplication.getContext().getPackageName(), 0);
            final float localVersion = Float.parseFloat(info.versionName);
            final float mServerVersionInt = Float.parseFloat(appver);
            if (mServerVersionInt > localVersion) {         //需要更新
                downloadFile(appurl, handler, userState);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件
     *
     * @param appurl
     */
    private void downloadFile(String appurl, Handler handler, boolean userState) {
        try {
            File apkFile = new File(Environment.getExternalStorageDirectory(), Constants.DOWN_NAME + ".apk");
            if (!apkFile.exists()) {
                apkFile.createNewFile();
            }
            long downloadLength = apkFile.length();

            long totalLength = getDownLoadLength(appurl, build);
            if (downloadLength == totalLength) {
                if (userState)
                    installApk(apkFile);
            } else {
                Request request = new Request.Builder()
                        .addHeader("RANGE", "bytes=" + downloadLength + "-" + totalLength)
                        .url(appurl)
                        .build();
                Response response = build.newCall(request).execute();
                ResponseBody body = response.body();
                inputStream = body.byteStream();
                fos = new FileOutputStream(apkFile, true);
                if (mThread == null) {
                    mThread = new DownThread();
                    mThread.setData(handler, downloadLength, totalLength, fos, inputStream, userState, apkFile);
                    mThread.start();
                }else {
                    mThread.setHandler(handler);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 安装APK
     */
    private void installApk(File loadFile) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(loadFile), "application/vnd.android.package-archive");
        MyApplication.getContext().startActivity(intent);
    }

    /**
     * 获取下载长度
     *
     * @param url
     * @param build
     * @return
     */
    private static long getDownLoadLength(String url, OkHttpClient build) {
        long contentLength = 0;
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = build.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                contentLength = response.body().contentLength();
                response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentLength;
    }
}
