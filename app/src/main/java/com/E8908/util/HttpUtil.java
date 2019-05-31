package com.E8908.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.E8908.R;
import com.E8908.conf.Constants;
import com.E8908.conf.Protocol;
import com.E8908.ui.UpdataDialog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by dell on 2017/9/20.
 */

public class HttpUtil {
    private static final String TAG = "HttpUtil";
    private static String mServiceVersion;
    private static String mUpdatecontent;
    private static String mAppurl;
    private static File mApkFile;
    private static boolean isCancle = false;
    private static boolean result;

    public static boolean isResult() {
        return result;
    }

    //执行网络请求
    public static void performNetRequest(final Context activity, final Handler mHander, final ProgressDialog dialog1) {
        Map<String, Object> params = new HashMap<>();
        params.put("appname",Constants.UPLOAD_NAME);
        params.put("url", Constants.URLS.CHECKAPK_URL);
        Protocol protocol = new Protocol() {
            @Override
            public void errorManage(IOException e) {
               mHander.post(new Runnable() {
                   @Override
                   public void run() {
                       dialog1.dismiss();
                       Toast.makeText(activity,"网络异常",Toast.LENGTH_SHORT).show();
                   }
               });
                result = false;
            }

            @Override
            public void parseData(Gson gson, String s) {
                try {
                    JSONArray jsonArray = new JSONObject(s).getJSONArray("response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        mServiceVersion = jsonObject.getString("appver");
                        mUpdatecontent = jsonObject.getString("updatecontent");
                        mAppurl = jsonObject.getString("appurl");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (!TextUtils.isEmpty(mAppurl)) {
                    mHander.post(new Runnable() {
                        @Override
                        public void run() {
                            result = checkVersion(activity,dialog1);
                        }
                    });

                }else {
                    result = false;
                    mHander.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog1.dismiss();
                            Toast.makeText(activity,"获取下载地址失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        };
        protocol.setParams(params);
        protocol.loadDataFromNet();
    }

    private static boolean checkVersion(final Context activity, final ProgressDialog dialog1) {
        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            float localVersion = Float.parseFloat(info.versionName);
            float mServerVersionInt = Float.parseFloat(mServiceVersion);
            if (mServerVersionInt > localVersion) {
                //需要更新,弹出更新对话框
                final UpdataDialog dialog = new UpdataDialog(activity, R.style.dialog, mUpdatecontent);
                dialog.setCanceledOnTouchOutside(false);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                dialog.show();
                dialog.getWindow().setAttributes(lp);
                dialog.setDownLaodButtonListener(new UpdataDialog.OnDownloadButtonListener() {
                    @Override
                    public void isDownload(boolean b) {
                        if (b) {
                            dialog.dismiss();
                            dialog1.dismiss();
                            //下载Apk
                            downloadApk(activity);
                        } else {
                            dialog.dismiss();
                            dialog1.dismiss();

                        }
                    }
                });
                return  true;
            }else {
                dialog1.dismiss();
                Toast.makeText(activity,"已经是最新版本", Toast.LENGTH_SHORT).show();
                return  false;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    //下载Apk
    private static void downloadApk(final Context activity) {
        final ProgressDialog dialog = new ProgressDialog(activity);
        //设置进度条风格，风格为圆形，旋转的
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        final OkHttpClient.Builder client = new OkHttpClient.Builder().readTimeout(100, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS);
        final OkHttpClient build = client.build();
        Request request = new Request.Builder().get().url(mAppurl).build();
        build.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                File path = new File(Environment.getExternalStorageDirectory(), "cad");
                if (!path.exists()) {
                    path.mkdir();
                }
                //Apk下载路径
                mApkFile = new File(path, "cheanda.apk");

                long length = response.body().contentLength();
                float resultKB = length / 1024;
                dialog.setMax((int) resultKB);
                try {
                    //请求成功
                    InputStream inputStream = response.body().byteStream();
                    int count = 0;
                    OutputStream os = new FileOutputStream(mApkFile);
                    byte[] buffer = new byte[1024];
                    int len;

                    while ((len = inputStream.read(buffer)) != -1) {
                        count += len;
                        os.write(buffer, 0, len);
                        float result = count / 1024;
                        dialog.setProgress((int) result);
                        if (dialog.getProgress() == resultKB) {
                            isCancle = true;
                        }
                    }
                    inputStream.close();
                    os.close();
                    if (isCancle) {
                        dialog.dismiss();
                        //下载完成,调用安装
                        installApk(activity);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    dialog.dismiss();

                }
            }
        });
    }

    public static void installApk(Context context) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(mApkFile), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
