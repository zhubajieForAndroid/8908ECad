package com.E8908.util;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.support.constraint.Constraints.TAG;


public class OkhttpManager {
    private static OkhttpManager mOkhttpManager;
    private FormBody.Builder mBuilder;
    private OkHttpClient mOkHttpClient;
    private final Request.Builder mRequest;

    private OkhttpManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(20, TimeUnit.SECONDS);//设置超时时间
        builder.readTimeout(20, TimeUnit.SECONDS);//设置读取超时时间
        builder.writeTimeout(20, TimeUnit.SECONDS);//设置写入超时时间
        mOkHttpClient = builder.build();

        mBuilder = new FormBody.Builder();

        mRequest = new Request.Builder();
    }

    public static OkhttpManager getOkhttpManager() {
        if (mOkhttpManager == null) {
            synchronized (OkhttpManager.class) {
                if (mOkhttpManager == null) {
                    mOkhttpManager = new OkhttpManager();
                }
            }
        }
        return mOkhttpManager;
    }

    public void doPost(String url, Map<String, String> mapParams, Callback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : mapParams.keySet()) {
            String s = mapParams.get(key);
            if (!TextUtils.isEmpty(s))
                builder.add(key, s);
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 循环读取设备开机状态
     * @param url
     * @param id
     * @param callback
     */
    public void doPost(String url, String id, Callback callback) {
        mBuilder.add("deviceno", id);
        Request build = mRequest.url(url).post(mBuilder.build()).build();

        mOkHttpClient.newCall(build).enqueue(callback);
    }

    public void doFile(String url, File file, Callback callback) {

        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        //添加图片数据
        //判断文件类型
        MediaType MEDIA_TYPE = MediaType.parse(judgeType(file.getPath()));
        requestBody.addFormDataPart("img", file.getName(), RequestBody.create(MEDIA_TYPE, file));
        //构建请求
        Request request = new Request.Builder()
                .url(url)//地址
                .post(requestBody.build())//添加请求体
                .build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }


    /**
     * 根据文件路径判断MediaType
     *
     * @param path
     * @return
     */
    public static String judgeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    public void upDateGasData(String url, Map<String, String> pames, Callback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : pames.keySet()) {
            String s = pames.get(key);
            if (!TextUtils.isEmpty(s))
                builder.add(key, s);
        }
        Request build = mRequest.url(url).post(builder.build()).build();

        mOkHttpClient.newCall(build).enqueue(callback);
    }
}
