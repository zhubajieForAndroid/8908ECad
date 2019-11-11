package com.E8908.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.base.BaseActivity;
import com.E8908.bean.RowsBean;
import com.E8908.bean.SearchResultBean;
import com.E8908.conf.Constants;
import com.E8908.util.DataUtil;
import com.E8908.util.KeyboardUtil;
import com.E8908.util.OkhttpManager;
import com.E8908.util.SendUtil;
import com.E8908.util.StringUtils;
import com.E8908.widget.ScoreView;
import com.E8908.widget.ToastUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchResultActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SearchResultActivity";
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
    @Bind(R.id.after_zjws_comprehensive_score)
    TextView mAfterZjwsComprehensiveScore;
    @Bind(R.id.hygiene)
    RelativeLayout mHygiene;
    @Bind(R.id.after_ktxt_comprehensive_score)
    TextView mAfterKtxtComprehensiveScore;
    @Bind(R.id.air_conditioner)
    RelativeLayout mAirConditioner;
    @Bind(R.id.after_ywjc_comprehensive_score)
    TextView mAfterYwjcComprehensiveScore;
    @Bind(R.id.odour)
    RelativeLayout mOdour;
    @Bind(R.id.after_yhqt_comprehensive_score)
    TextView mAfterYhqtComprehensiveScore;
    @Bind(R.id.harmful_gas)
    RelativeLayout mHarmfulGas;
    @Bind(R.id.serach_et)
    EditText mSerachEt;
    @Bind(R.id.search_iv)
    ImageView mSearchIv;
    @Bind(R.id.car_number)
    TextView mCarNumber;
    @Bind(R.id.before_score)
    TextView mBeforeScore;
    @Bind(R.id.after_score)
    TextView mAfterScore;
    @Bind(R.id.create_time)
    TextView mCreateTime;
    @Bind(R.id.progress)
    ScoreView mProgress;
    @Bind(R.id.keyboard)
    LinearLayout mKeyboard;
    @Bind(R.id.back_btn)
    ImageView mBackBtn;
    @Bind(R.id.touch_bg)
    RelativeLayout mTouchBg;
    @Bind(R.id.keyboard_view)
    KeyboardView mKeyboardView;
    @Bind(R.id.push_state)
    ImageView mPushState;
    @Bind(R.id.push_tv)
    TextView mPushTv;
    @Bind(R.id.progress_bar)
    LinearLayout mProgressBar;
    @Bind(R.id.car_read)
    TextView mCarRead;

    private boolean mIsYesData = false;
    private Handler mHandler;
    private Intent mIntent;
    private KeyboardUtil mKeyboardUtil;
    private String mCurrentTabId;
    private OkhttpManager mOkhttpManager;
    private String mCarnumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);
        mHandler = new Handler();
        initData();
    }

    private void initData() {
        mCarnumber = getIntent().getStringExtra("carNumber");
        Map<String, String> pames = new HashMap<>();
        pames.put("carNumber", mCarnumber);
        mOkhttpManager = OkhttpManager.getOkhttpManager();
        mOkhttpManager.doPost(Constants.URLS.GET_CHECK_SEARCH_INFO, pames, mCallback);
        mSearchIv.setOnClickListener(this);
        mOdour.setOnClickListener(this);
        mHarmfulGas.setOnClickListener(this);
        mAirConditioner.setOnClickListener(this);
        mHygiene.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
        mPushTv.setOnClickListener(this);

        mTouchBg.setOnClickListener(this);
        mToobarBgImage.setImageResource(R.mipmap.tesing_data);
        mKeyboardUtil = new KeyboardUtil(mKeyboardView, this, mSerachEt);
        mSerachEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!mKeyboardUtil.isShow()) {
                    mKeyboardUtil.showKeyboard();
                }
                return false;
            }
        });
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
        //00011000
        String connectServerState = state.substring(3, 4);                  //连接服务器状态
        String afterLockState = state.substring(1, 2);                       //后门锁定状态
        String frontLockState = state.substring(2, 3);                       //前门锁定状态
        String temperatureState = state.substring(0, 1);                       //温度传感器连接状态
        int signalStrength = DataUtil.getSignalStrength(buffer);            //获取信号强度
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                SendUtil.controlVoice();
                finish();
                break;
            case R.id.search_iv:                //搜索
                SendUtil.controlVoice();
                String carNumberResult = mSerachEt.getText().toString().trim();
                if (!TextUtils.isEmpty(carNumberResult)) {
                    boolean b = StringUtils.ckeckCarNumber(carNumberResult);
                    if (b) {
                        Map<String, String> pames = new HashMap<>();
                        pames.put("carNumber", carNumberResult);
                        OkhttpManager okhttpManager = OkhttpManager.getOkhttpManager();
                        okhttpManager.doPost(Constants.URLS.GET_CHECK_SEARCH_INFO, pames, mCallback);
                        if (mKeyboardUtil.isShow()) {
                            mKeyboardUtil.hideKeyboard();
                        }
                    } else {
                        ToastUtil.showMessage("非法车牌");
                    }
                } else {
                    ToastUtil.showMessage("车牌不能为空");
                }
                break;
            case R.id.odour:            //异味检查历史数据
                SendUtil.controlVoice();
                mIntent = new Intent(this, OdourActivity.class);
                Bundle odour = new Bundle();
                odour.putSerializable("BlueToothInfoBean", mResponseBean);
                mIntent.putExtras(odour);
                startActivity(mIntent);
                break;
            case R.id.harmful_gas:      //有害气体历史数据
                SendUtil.controlVoice();
                mIntent = new Intent(this, HarmfulGasActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("BlueToothInfoBean", mResponseBean);
                mIntent.putExtras(bundle);
                startActivity(mIntent);
                break;
            case R.id.air_conditioner:  //空调历史数据
                SendUtil.controlVoice();
                mIntent = new Intent(this, AirConditionerActivity.class);
                Bundle airConditioner = new Bundle();
                airConditioner.putSerializable("BlueToothInfoBean", mResponseBean);
                mIntent.putExtras(airConditioner);
                startActivity(mIntent);
                break;
            case R.id.hygiene:          //整洁卫生历史数据
                SendUtil.controlVoice();
                mIntent = new Intent(this, NeatActivity.class);
                Bundle hygiene = new Bundle();
                hygiene.putSerializable("BlueToothInfoBean", mResponseBean);
                mIntent.putExtras(hygiene);
                startActivity(mIntent);
                break;
            case R.id.touch_bg:
                if (mKeyboardUtil.isShow()) {
                    mKeyboardUtil.hideKeyboard();
                }
                break;
            case R.id.push_tv:                  //推送
                mProgressBar.setVisibility(View.VISIBLE);
                //更新报告
                Map<String, String> pames = new HashMap<>();
                pames.put("id", mCurrentTabId);
                pames.put("pushmessage", "1");
                OkhttpManager manager = OkhttpManager.getOkhttpManager();
                manager.doPost(Constants.URLS.PUSH_TAB, pames, mUpdataCallback);
                break;
        }
    }

    private RowsBean mResponseBean;
    public Callback mCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setVisibility(View.GONE);
                    ToastUtil.showMessage("网络异常");
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String string = response.body().string();
            String substring = string.substring(8, 9);
            if ("0".equals(substring)) {
                Gson gson = new Gson();
                final SearchResultBean resultBean = gson.fromJson(string, SearchResultBean.class);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                        mResponseBean = resultBean.response;
                        setDataForDat();
                    }
                });
            } else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                        ToastUtil.showMessage("该车牌下没数据！");
                    }
                });
            }
        }
    };
    private Callback mUpdataCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setVisibility(View.GONE);
                    ToastUtil.showMessage("网络异常");
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String string = response.body().string();
            String substring = string.substring(8, 9);
            if (substring.equals("0")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                        ToastUtil.showMessage("推送成功");
                        Map<String, String> pames = new HashMap<>();
                        pames.put("carNumber", mCarnumber);
                        mOkhttpManager.doPost(Constants.URLS.GET_CHECK_SEARCH_INFO, pames, mCallback);
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                        ToastUtil.showMessage("推送失败");
                    }
                });
            }
        }
    };

    private void setDataForDat() {
        if (mResponseBean != null) {
            String carNumber = mResponseBean.carNumber;
            mCurrentTabId = mResponseBean.id;
            if (!TextUtils.isEmpty(carNumber))
                mCarNumber.setText(carNumber);
            long createTime = mResponseBean.createTime;
            if (createTime > 0) {
                Date date = new Date(createTime);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd ");
                String format = simpleDateFormat.format(date);
                mCreateTime.setText("检测时间:" + format);
            }
            //施工后的评分
            String score = mResponseBean.afterScore;
            if (!TextUtils.isEmpty(score)) {
                float anInt = Float.parseFloat(score);
                mProgress.setProgress((int) anInt);
                if (anInt <= 33 && anInt > 0) {
                    mAfterScore.setTextColor(getResources().getColor(R.color.red));
                }
                if (anInt > 33 && anInt <= 66) {
                    mAfterScore.setTextColor(getResources().getColor(R.color.yellow));
                }
                if (anInt > 66) {
                    mAfterScore.setTextColor(getResources().getColor(R.color.green));
                }
                mAfterScore.setText((int) anInt + "");
            }
            //车主阅读次数
            int readCount = mResponseBean.readCount;
            mCarRead.setText("车主阅读: " + readCount + "次");
            String beforeScore = mResponseBean.beforeScore;
            if (!TextUtils.isEmpty(beforeScore)) {
                float anInt = Float.parseFloat(beforeScore);
                if (anInt <= 33 && anInt > 0) {
                    mBeforeScore.setTextColor(getResources().getColor(R.color.red));
                }
                if (anInt > 33 && anInt <= 66) {
                    mBeforeScore.setTextColor(getResources().getColor(R.color.yellow));
                }
                if (anInt > 66) {
                    mBeforeScore.setTextColor(getResources().getColor(R.color.green));
                }
                mBeforeScore.setText((int) anInt + "");
            }
            int pushmessage = mResponseBean.pushmessage;
            if (pushmessage == 1) {
                mPushTv.setVisibility(View.GONE);
                mPushState.setImageResource(R.mipmap.push);
            } else {
                mPushState.setImageResource(R.mipmap.nopush);
                mPushTv.setVisibility(View.VISIBLE);
                //设置下滑线
                mPushTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                mPushTv.setOnClickListener(this);
            }
            //整洁卫生综合评分
            String afterZjwsScore = mResponseBean.afterZjwsScore;
            setScoreByFore(afterZjwsScore, mAfterZjwsComprehensiveScore);
            //空调系统综合评分
            String afterKtxtScore = mResponseBean.afterKtxtScore;
            setScoreByFore(afterKtxtScore, mAfterKtxtComprehensiveScore);
            //异味检查综合评分
            String afterYwjcScore = mResponseBean.afterYwjcScore;
            setScoreByFore(afterYwjcScore, mAfterYwjcComprehensiveScore);
            //有害气体
            String afterYhqtScore = mResponseBean.afterYhqtScore;
            setScoreByFore(afterYhqtScore, mAfterYhqtComprehensiveScore);

        }
    }


    /**
     * 设置综合评分
     *
     * @param afterZjwsScore              评分
     * @param afterZjwsComprehensiveScore textView
     */
    private void setScoreByFore(String afterZjwsScore, TextView afterZjwsComprehensiveScore) {
        if (!TextUtils.isEmpty(afterZjwsScore)) {
            float afterZjwsInt = Float.parseFloat(afterZjwsScore);
            if (afterZjwsInt <= 25) {
                afterZjwsComprehensiveScore.setTextColor(getResources().getColor(R.color.red));
                afterZjwsComprehensiveScore.setText("极差");
            }
            if (afterZjwsInt > 25 && afterZjwsInt <= 50) {
                afterZjwsComprehensiveScore.setTextColor(getResources().getColor(R.color.yellow));
                afterZjwsComprehensiveScore.setText("差");
            }
            if (afterZjwsInt > 50 && afterZjwsInt <= 75) {
                afterZjwsComprehensiveScore.setTextColor(getResources().getColor(R.color.blue));
                afterZjwsComprehensiveScore.setText("良");
            }
            if (afterZjwsInt > 75) {
                afterZjwsComprehensiveScore.setTextColor(getResources().getColor(R.color.green));
                afterZjwsComprehensiveScore.setText("优");
            }
        }
    }
}
