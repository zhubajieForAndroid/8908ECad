package com.E8908.util;

import android.util.Log;

import com.E8908.conf.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UpLoadTimeUtils {

    public static void upLoadTimer(String id,String time){
        Map<String,String> pame = new HashMap<>();
        pame.put("equipmentId",id);
        pame.put("equipmentType","ACC-8908E");
        pame.put("times",time);
        OkhttpManager.getOkhttpManager().doPost(Constants.URLS.UPLOAD_WORK_TIME, pame, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("dada", "onResponse: "+response.body().string());
            }
        });
    }
    public static void loadData(String equipmentNumber,Callback callback) {
        Map<String,String> pames = new HashMap<>();
        pames.put("equipmentId",equipmentNumber);
        pames.put("equipmentType","ACC-8908E");
        OkhttpManager.getOkhttpManager().doPost(Constants.URLS.GET_WORK_TIME,pames,callback);
    }

}
