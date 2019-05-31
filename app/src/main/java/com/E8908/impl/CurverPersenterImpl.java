package com.E8908.impl;

import android.os.Handler;
import android.text.TextUtils;

import com.E8908.persenter.BasePersenter;
import com.E8908.view.BaseView;
import com.E8908.util.OkhttpManager;
import com.E8908.base.CurverBean;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CurverPersenterImpl  implements BasePersenter {
    private final Handler mHandler;
    private final Gson mGson;
    private BaseView<CurverBean> mBaseView;

    public CurverPersenterImpl(BaseView<CurverBean> beanBaseView){
        mBaseView = beanBaseView;
        mHandler = new Handler();
        mGson = new Gson();
    }
    @Override
    public void loadData(String url, int pageNum, int pageCounts) {
    }

    @Override
    public void loadData(String url, String equipmentID, String curnimber) {
        if (TextUtils.isEmpty(equipmentID) || TextUtils.isEmpty(curnimber))
            return;
        Map<String,String> pames = new HashMap<>();
        pames.put("deviceno",equipmentID);
        pames.put("carNumber",curnimber);
        OkhttpManager okhttpManager = OkhttpManager.getOkhttpManager();
        okhttpManager.doPost(url,pames,mCallback);
    }

    @Override
    public void loadData(String url, Map<String, String> pames) {

    }



    private Callback mCallback = new Callback() {
        @Override
        public void onFailure(Call call, final IOException e) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mBaseView.onFaild(e.getLocalizedMessage());
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String string = response.body().string();
            String substring = string.substring(8, 9);
            if ("0".equals(substring)) {
                final CurverBean bean = mGson.fromJson(string, CurverBean.class);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mBaseView.onSuccess(bean);
                    }
                });
            }
        }
    };
}
