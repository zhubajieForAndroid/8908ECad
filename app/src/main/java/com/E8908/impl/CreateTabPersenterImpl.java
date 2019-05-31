package com.E8908.impl;

import android.os.Handler;

import com.E8908.persenter.BasePersenter;
import com.E8908.util.OkhttpManager;
import com.E8908.view.BaseView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class CreateTabPersenterImpl implements BasePersenter {
    private BaseView<String> mBeanBaseView;
    private Handler mHandler;
    private Gson mGson;

    public CreateTabPersenterImpl(BaseView<String> view) {
        mBeanBaseView = view;
        mHandler = new Handler();
        mGson = new Gson();
    }

    @Override
    public void loadData(String url, int pageNum, int pageCounts) {
    }

    @Override
    public void loadData(String url, String equipmentID, String curnimber) {
    }

    @Override
    public void loadData(String url, Map<String, String> pames) {
        OkhttpManager okhttpManager = OkhttpManager.getOkhttpManager();
        okhttpManager.doPost(url, pames, mCallback);
    }

    private Callback mCallback = new Callback() {
        @Override
        public void onFailure(Call call, final IOException e) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mBeanBaseView.onFaild("网络异常");
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String string = response.body().string();
            try {
                JSONObject jsonObject = new JSONObject(string);
                int code = jsonObject.getInt("code");
                if (code == 0){
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mBeanBaseView.onSuccess("上传成功");
                        }
                    });
                }else {
                    final Object message = jsonObject.get("message");
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mBeanBaseView.onSuccess((String) message);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
}
