package com.E8908.textFunction;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.base.BaseActivity;
import com.E8908.conf.Constants;
import com.E8908.util.DataUtil;
import com.E8908.util.OkhttpManager;
import com.E8908.widget.SpaceItemDecoration;
import com.E8908.widget.ToastUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TextResultActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "TextResultActivity";
    @Bind(R.id.toobar_bg_image)
    ImageView mToobarBgImage;
    @Bind(R.id.temperature_state)
    TextView mTemperatureState;
    @Bind(R.id.message_state)
    TextView mMessageState;
    @Bind(R.id.tab_image_back_state)
    ImageView mTabImageBackState;
    @Bind(R.id.tab_image_front_state)
    ImageView mTabImageFrontState;
    @Bind(R.id.tab_image_communication_state)
    ImageView mTabImageCommunicationState;
    @Bind(R.id.tab_image_wifi)
    ImageView mTabImageWifi;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.bt_back)
    Button mBtBack;
    private boolean mIsYesData = false;
    private Handler mHandler;
    private List<TextTabBean.ResponseBean> mBeans;
    private TextResultAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_result);
        ButterKnife.bind(this);
        mHandler = new Handler();
        mBeans = new ArrayList<>();
        initData();
    }


    @Override
    protected void isWifiConnected(boolean b, int level) {
        if (b) {
            if (level <= 0 && level >= -50) {
                mTabImageWifi.setImageResource(R.mipmap.wifi_4);
            } else if (level < -50 && level >= -70) {
                mTabImageWifi.setImageResource(R.mipmap.wifi_3);
            } else if (level < -70 && level >= -80) {
                mTabImageWifi.setImageResource(R.mipmap.wifi_2);
            } else if (level < -80 && level >= -100) {
                mTabImageWifi.setImageResource(R.mipmap.wifi_1);
            } else {
                mTabImageWifi.setImageResource(R.mipmap.top_wifi_off);
            }
        } else {
            mTabImageWifi.setImageResource(R.mipmap.top_wifi_off);
        }
    }

    @Override
    protected void isYesData(boolean isdata,boolean isCharging) {
        if (isdata && mIsYesData) {        //成功
            if (isCharging){
                mMessageState.setText("正常");
                mMessageState.setTextColor(Color.parseColor("#fd0fc602"));
            }else {
                mMessageState.setText("正常");
                mMessageState.setTextColor(Color.parseColor("#fdfa0310"));
            }
        } else {             //失败
            mMessageState.setText("断开");
            mMessageState.setTextColor(Color.parseColor("#fdfa0310"));
        }
        mIsYesData = false;
    }

    @Override
    public void onDataReceived(byte[] buffer, int size) {
        mIsYesData = true;
        if (size == Constants.DATA_LONG && buffer[2] == 0x03) {
            analysisData(buffer);
        }
    }

    private void analysisData(byte[] buffer) {
        String state = DataUtil.getState(buffer);                           //状态位
        int signalStrength = DataUtil.getSignalStrength(buffer);            //获取信号强度

        String connectServerState = state.substring(3, 4);                  //连接服务器状态
        String activationState = state.substring(4, 5);                     //设备激活状态
        String lockState = state.substring(5, 6);                           //设备锁定状态
        //00011000
        String afterLockState = state.substring(1, 2);                       //后门锁定状态
        String frontLockState = state.substring(2, 3);                       //前门锁定状态
        String temperatureState = state.substring(0, 1);                       //温度传感器连接状态
        if (temperatureState.equals("1")) {
            mTemperatureState.setText("- -");
        } else {
            int temperature = DataUtil.getTemperature(buffer);                  //温度
            mTemperatureState.setText(temperature + "℃");
        }
        //00011001
        if (frontLockState.equals("1")) {
            mTabImageBackState.setImageResource(R.mipmap.icon_on);
        } else {
            mTabImageBackState.setImageResource(R.mipmap.icon_off);
        }
        if (afterLockState.equals("1")) {
            mTabImageFrontState.setImageResource(R.mipmap.icon_on);
        } else {
            mTabImageFrontState.setImageResource(R.mipmap.icon_off);
        }
        if (connectServerState.equals("1")) {
            //TODO 已经连接服务器
        } else {
            //TODO 未连接
        }
        if (activationState.equals("1")) {
            //TODO 已经激活
        } else {
            //TODO 未激活
        }
        if (lockState.equals("1")) {
            //TODO 已经锁定
        } else {
            //TODO 未锁定
        }
        //是否连接服务器
        if (connectServerState.equals("0")) {
            mTabImageCommunicationState.setImageResource(R.mipmap.top_icon_4_off);
        } else {
            //设置信号强度
            switch (signalStrength) {
                case 1:
                    mTabImageCommunicationState.setImageResource(R.mipmap.top_icon_1);
                    break;
                case 2:
                    mTabImageCommunicationState.setImageResource(R.mipmap.top_icon_2);
                    break;
                case 3:
                    mTabImageCommunicationState.setImageResource(R.mipmap.top_icon_3);
                    break;
                case 4:
                    mTabImageCommunicationState.setImageResource(R.mipmap.top_icon_4);
                    break;
                default:
                    mTabImageCommunicationState.setImageResource(R.mipmap.top_icon_4_off);
                    break;
            }
        }
    }

    private void initData() {
        String id = getIntent().getStringExtra("id");
        mToobarBgImage.setImageResource(R.mipmap.top_bar_function);
        mAdapter = new TextResultAdapter(this, mBeans);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(0, 20));
        mRecyclerView.setAdapter(mAdapter);
        mBtBack.setOnClickListener(this);

        //请求数据
        Map<String, String> pames = new HashMap<>();
        pames.put("equipmentCode", id);
        pames.put("equipmentType", "ACC-8908E");
        OkhttpManager.getOkhttpManager().doPost(Constants.URLS.QUERY_TEXT_TAB, pames, mCallback);
    }

    private Callback mCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showMessage("网络异常");
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful()) {
                String string = response.body().string();
                if (!TextUtils.isEmpty(string) && string.length() >9) {
                    String substring = string.substring(8, 9);
                    if ("0".equals(substring)) {
                        Gson gson = new Gson();
                        TextTabBean textTabBean = gson.fromJson(string, TextTabBean.class);
                        mBeans.addAll(textTabBean.getResponse());
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showMessage("没有数据");
                            }
                        });
                    }
                }
            }

        }
    };

    @Override
    public void onClick(View v) {
        finish();
    }
}
