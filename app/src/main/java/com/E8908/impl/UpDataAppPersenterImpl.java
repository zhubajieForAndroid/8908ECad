package com.E8908.impl;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.text.TextUtils;

import com.E8908.base.MyApplication;
import com.E8908.conf.Constants;
import com.E8908.util.FileUtil;
import com.E8908.util.OkhttpManager;
import com.E8908.view.UpdataView;
import com.E8908.widget.ToastUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UpDataAppPersenterImpl {
    private UpdataView mUpdataView;
    private Handler mHandler;
    private Gson mGson;
    public UpDataAppPersenterImpl(UpdataView updataView){
        mUpdataView = updataView;
        mHandler = new Handler();
        mGson = new Gson();
    }
    public void loadData() {
        Map<String,String> pames = new HashMap<>();
        pames.put("appname",Constants.UPLOAD_NAME);
        OkhttpManager okhttpManager = OkhttpManager.getOkhttpManager();
        okhttpManager.doPost(Constants.URLS.CHECKAPK_URL, pames, mCallback);
    }
    private Callback mCallback = new Callback() {
        @Override
        public void onFailure(Call call, final IOException e) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mUpdataView.onFaild("网络异常");
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String string = response.body().string();
            try {
                JSONArray jsonArray = new JSONObject(string).getJSONArray("response");
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
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mUpdataView.onSuccess(appurl,updatecontent);
                                    }
                                });
                            } else {
                                //如果是最新的版本,就去删除已经下载好的Apk文件
                                FileUtil.deleteFile(FileUtil.getUpdataPath());
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mUpdataView.onFaild("已经是最新版本");
                                    }
                                });
                            }
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mUpdataView.onFaild("获取下载地址失败");
                            }
                        });
                    }
                }
            } catch (JSONException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mUpdataView.onFaild("网络异常");
                    }
                });
                e.printStackTrace();
            }
        }
    };
}
