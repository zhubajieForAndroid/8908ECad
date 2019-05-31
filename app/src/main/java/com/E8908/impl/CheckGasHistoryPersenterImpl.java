package com.E8908.impl;

import android.os.Handler;

import com.E8908.persenter.BasePersenter;
import com.E8908.view.CheckGasHistoryView;
import com.E8908.util.OkhttpManager;
import com.E8908.bean.CheckGasHistoryBean;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;




public class CheckGasHistoryPersenterImpl implements BasePersenter {
    private CheckGasHistoryView<CheckGasHistoryBean> mCheckGasHistoryView;
    private Handler mHandler;
    private Gson mGson;
    public CheckGasHistoryPersenterImpl(CheckGasHistoryView<CheckGasHistoryBean> checkGasHistoryView){
        mCheckGasHistoryView = checkGasHistoryView;
        mHandler = new Handler();
        mGson = new Gson();
    }

    @Override
    public void loadData(String url,int pageNum,int pageCounts) {

    }

    @Override
    public void loadData(String url, String equipmentID, String curnimber) {

    }

    @Override
    public void loadData(String url, Map<String, String> pames) {
        OkhttpManager okhttpManager = OkhttpManager.getOkhttpManager();
        okhttpManager.doPost(url,pames,mCallback);
    }

    private Callback mCallback = new Callback() {
        @Override
        public void onFailure(Call call, final IOException e) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCheckGasHistoryView.onFaild(e.getLocalizedMessage());
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String string = response.body().string();
            String substring = string.substring(8, 9);
            if ("0".equals(substring)) {
                final CheckGasHistoryBean bean = mGson.fromJson(string, CheckGasHistoryBean.class);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCheckGasHistoryView.onSuccess(bean);
                    }
                });
            }
        }
    };

}
