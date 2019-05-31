package com.E8908.util;


import com.E8908.conf.Constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class SetReadStateUtil {
    /**
     * 设置设备就绪
     * @param equipmentId
     * @param state
     */
    public static void setRead(String equipmentId,String state){
        Map<String,String> pames = new HashMap<>();
        pames.put("deviceno",equipmentId);
        pames.put("status",state);
        OkhttpManager.getOkhttpManager().doPost(Constants.URLS.SET_READ, pames, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}
            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        });
    }
}
