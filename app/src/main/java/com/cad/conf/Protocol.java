package com.cad.conf;



import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by dell on 2017/3/15.
 */

public abstract class Protocol {
    private static final String TAG = "Protocol";
    private Map<String,Object> params = new HashMap<>();
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private Request mRequest;

    /**
     * 设置参数
     * @param params
     */
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public void loadDataFromNet() {
        List<String> list = new ArrayList<>();
        List<String> imageList = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);
        if (params.size() > 1) {
            for (String s : params.keySet()) {
                if (!s.equals("url") && !s.contains("file")){
                    list.add(s);

                }else if (s.contains("file")){
                    imageList.add(s);
                }
            }

            for (int i = 0; i < list.size(); i++) {
                multipartBodyBuilder.addFormDataPart(list.get(i), (String) params.get(list.get(i)));
            }
            for (int i = 0; i < imageList.size(); i++) {
                File f = new File((String) params.get(imageList.get(i)));
                multipartBodyBuilder.addFormDataPart("iamge", "user.jpg", RequestBody.create(MEDIA_TYPE_PNG, f));
            }

            //构建请求体
            RequestBody requestBody = multipartBodyBuilder.build();

            Request.Builder RequestBuilder = new Request.Builder();
            RequestBuilder.url((String) params.get("url"));// 添加URL地址
            RequestBuilder.post(requestBody);

            mRequest = RequestBuilder.build();

        } else {
            mRequest = new Request.Builder()
                    .url((String) params.get("url"))
                    .build();
        }

        client.newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                errorManage(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                String string = response.body().string();
                parseData(gson,string);
            }
        });
    }

    /**
     * 异常处理
     * @param e
     */
    public abstract void errorManage(IOException e);

    /**
     * 解析数据
     * @param gson
     * @param s
     */
    public abstract void parseData(Gson gson, String s);


}
