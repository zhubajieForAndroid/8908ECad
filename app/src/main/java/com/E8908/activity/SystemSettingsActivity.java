package com.E8908.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.base.BaseActivity;
import com.E8908.base.MyApplication;
import com.E8908.conf.Constants;
import com.E8908.conf.Protocol;
import com.E8908.util.DataUtil;
import com.E8908.util.FileUtil;
import com.E8908.util.NavigationBarUtil;
import com.E8908.util.SendUtil;
import com.E8908.widget.JurisdictionDialog;
import com.E8908.widget.LoginDialog;
import com.E8908.widget.OnlineDialog;
import com.E8908.widget.SetDateDialog;
import com.E8908.widget.SetIDDialog;
import com.E8908.widget.SetVersionDialog;
import com.E8908.widget.StopDialog;
import com.E8908.widget.ToastUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SystemSettingsActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener, JurisdictionDialog.OnCheckJListener {

    private static final String TAG = "SystemSettingsActivity";
    @Bind(R.id.tab_image_back_state)
    ImageView mTabImageBackState;
    @Bind(R.id.about_system_back)
    ImageButton mAboutSystemBack;
    @Bind(R.id.toobar_bg_image)
    ImageView mToobarBgImage;
    @Bind(R.id.touch_bg)
    ImageView mTouchBg;
    @Bind(R.id.tab_image_wifi)
    ImageView mTabImageWifi;
    @Bind(R.id.message_state)
    TextView mMessageState;
    @Bind(R.id.tab_image_front_state)
    ImageView mTabImageFrontState;
    @Bind(R.id.tab_image_communication_state)
    ImageView mTabImageCommunicationState;
    @Bind(R.id.temperature_state)
    TextView mTemperatureState;
    @Bind(R.id.open_qian)
    ImageView mOpenQian;
    @Bind(R.id.open_hou)
    ImageView mOpenHou;
    @Bind(R.id.open_conner)
    LinearLayout mOpenConner;
    @Bind(R.id.back_oil)
    ImageView mBackOil;
    private SetVersionDialog mDialog;
    private SetDateDialog mDateDialog;
    private int mVwesionToHardware;     //硬件版本
    private int mVwesionToSoftware;     //软件版本
    private int mDateYear;
    private int mDateMonth;
    private int mDateDay;
    private boolean mIsYesData = false;
    private boolean isFinish = false;
    private int currentState;
    private boolean isPower = false;//是否有权限
    private List<String> mList;
    private int mResultInt;
    private String mResultData;
    private StopDialog mStopDialog;
    private String mEquipmentNumber;
    private boolean isOpen = true;
    private String mDepthWorkNumbwe;
    private String mRoutineWorkNumbwe;
    private String mRiseTotelNumbwe;
    private String mDepthWorkResult;
    private String mRoutineResult;
    private String mAddTaotle;
    private JurisdictionDialog mJuDialog;
    private boolean isData = true;          //ID是否有历史数据


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_settings);
        ButterKnife.bind(this);
        mStopDialog = new StopDialog(this, R.style.dialog);
        mJuDialog = new JurisdictionDialog(this,R.style.dialog);
        mJuDialog.setOnCheckJListener(this);
        initListener();
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
    public void onDataReceived(final byte[] buffer, int size) {
        mIsYesData = true;
        if (size == Constants.SET_RESULT_LINGTH) {
            if (!isFinish)
                setResultData(buffer);
        }
        if (size == Constants.DATA_LONG && buffer[2] == 0x03) {
            analysisData(buffer);
        }
    }

    /**
     * 数据是否发送成功
     *
     * @param isdata
     */
    @Override
    protected void isYesData(boolean isdata) {
        if (isdata && mIsYesData) {        //成功
            mMessageState.setText("正常");
            mMessageState.setTextColor(Color.parseColor("#fd0fc602"));
        } else {             //失败
            mMessageState.setText("断开");
            mMessageState.setTextColor(Color.parseColor("#fdfa0310"));
        }
        mIsYesData = false;
    }

    private void analysisData(byte[] buffer) {
        String state = DataUtil.getState(buffer);                           //状态位
        int signalStrength = DataUtil.getSignalStrength(buffer);            //获取信号强度
        //液体加注总升数
        mRiseTotelNumbwe = DataUtil.getRiseTotelNumbwe(buffer);

        //获取深度保养次数
        mDepthWorkNumbwe = DataUtil.getDepthWorkNumbwe(buffer);
        //获取常规保养次数
        mRoutineWorkNumbwe = DataUtil.getRoutineWorkNumbwe(buffer);
        //获取序列号
        mEquipmentNumber = DataUtil.getEquipmentNumber(buffer);
        if ("00000000".equals(mEquipmentNumber)) {
            mOpenConner.setVisibility(View.VISIBLE);
        } else {
            mOpenConner.setVisibility(View.GONE);
        }
        //获取硬件版本
        mVwesionToHardware = DataUtil.getVwesionToHardware(buffer);
        //获取软件版本
        mVwesionToSoftware = DataUtil.getVwesionToSoftware(buffer);
        //获取出厂日期年
        mDateYear = DataUtil.getDateYear(buffer);
        //获取出厂日期月
        mDateMonth = DataUtil.getDateMonth(buffer);
        //获取出厂日期日
        mDateDay = DataUtil.getDateDay(buffer);
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

    /**
     * 设置结果
     *
     * @param resultData
     */
    public void setResultData(byte[] resultData) {
        Boolean isSuccess = DataUtil.analysisSetResult(resultData);
        switch (currentState) {
            case 1:
                if (isSuccess) {
                    currentState = 0;
                    ToastUtil.showMessage("设置版本成功");
                } else {
                    currentState = 1;
                    int resultXOR = 0x2a ^ 0x0a ^ 0x07 ^ mResultInt ^ mVwesionToHardware ^ mDateYear ^ mDateMonth ^ mDateDay;
                    int[] arr = {0x2a, 0x0a, 0x07, mResultInt, mVwesionToHardware, mDateYear, mDateMonth, mDateDay, resultXOR, 0x23};
                    SendUtil.sendMessage(arr, MyApplication.getOutputStream());
                }
                break;
            case 2:
                if (isSuccess) {
                    currentState = 0;
                    ToastUtil.showMessage("设置版本成功");
                } else {
                    currentState = 2;
                    int resultXOR = 0x2a ^ 0x0a ^ 0x07 ^ mVwesionToSoftware ^ mResultInt ^ mDateYear ^ mDateMonth ^ mDateDay;
                    int[] arr = {0x2a, 0x0a, 0x07, mVwesionToSoftware, mResultInt, mDateYear, mDateMonth, mDateDay, resultXOR, 0x23};
                    SendUtil.sendMessage(arr, MyApplication.getOutputStream());
                }
                break;
            case 3:
                if (isSuccess) {
                    currentState = 0;
                    ToastUtil.showMessage("设置日期成功");
                } else {
                    if (!TextUtils.isEmpty(mResultData)) {
                        currentState = 3;
                        String year = mResultData.substring(2, 4);
                        String month = mResultData.substring(4, 6);
                        String day = mResultData.substring(6);
                        int yearInt = Integer.parseInt(year);
                        int monthInt = Integer.parseInt(month);
                        int dayInt = Integer.parseInt(day);
                        int resultXOR = 0x2a ^ 0x0a ^ 0x07 ^ mVwesionToHardware ^ mVwesionToSoftware ^ yearInt ^ monthInt ^ dayInt;
                        int[] arr = {0x2a, 0x0a, 0x07, mVwesionToHardware, mVwesionToSoftware, yearInt, monthInt, dayInt, resultXOR, 0x23};
                        SendUtil.sendMessage(arr, MyApplication.getOutputStream());
                    }
                }
                break;
            case 4:
                if (isSuccess) {        //上线成功
                    currentState = 0;
                    mOpenConner.setVisibility(View.GONE);
                } else {
                    currentState = 4;
                    if (mList != null)
                        SendUtil.setEquipentId(mList);
                }
                break;
            case 5:
                if (isSuccess) {
                    currentState = 0;
                    if (mStopDialog != null)
                        mStopDialog.dismiss();
                    ToastUtil.showMessage("设置成功");
                } else {
                    currentState = 5;
                    SendUtil.renewData();
                }
                break;
            case 6:
                if (isSuccess) {
                    currentState = 0;
                } else {
                    currentState = 6;
                    SendUtil.openQian();
                }
                break;
            case 7:
                if (isSuccess) {
                    currentState = 0;
                } else {
                    currentState = 6;
                    SendUtil.openQian();
                }
                break;
            case 8:
                if (isSuccess) {
                    currentState = 0;
                    isOpen = false;
                    mBackOil.setImageResource(R.mipmap.btn_old_right_2);
                } else {
                    currentState = 8;
                    SendUtil.open4();
                }
                break;
            case 9:
                if (isSuccess) {
                    currentState = 0;
                    isOpen = true;
                    mBackOil.setImageResource(R.mipmap.btn_old_right_1);
                } else {
                    currentState = 9;
                    SendUtil.closeAll();
                }
                break;
            case 10:
                if (isSuccess) {
                    if (isData){
                        currentState = 11;
                        //设置常规保养次数
                        SystemClock.sleep(100);
                        if (!TextUtils.isEmpty(mRoutineResult)) {
                            int i = Integer.parseInt(mRoutineResult, 16);
                            SendUtil.setRoutineNumber(i);
                        }
                    }else {
                        currentState = 0;
                        FileUtil.putSendState(true);
                        ToastUtil.showMessage("设置成功,重启生效");
                    }

                } else {
                    currentState = 10;
                    SystemClock.sleep(100);
                    if (mList != null)
                        SendUtil.setEquipentId(mList);
                }
                break;
            case 11:
                if (isSuccess) {
                    currentState = 12;
                    //设置加注量
                    SystemClock.sleep(100);
                    if (!TextUtils.isEmpty(mAddTaotle)) {
                        int resultNumber = Integer.parseInt(mAddTaotle, 16);
                        SendUtil.setChangeOilNumber(resultNumber);
                    }
                } else {
                    currentState = 11;
                    //设置常规保养次数
                    SystemClock.sleep(100);
                    if (!TextUtils.isEmpty(mRoutineResult)) {
                        int i = Integer.parseInt(mRoutineResult, 16);
                        SendUtil.setRoutineNumber(i);
                    }
                }
                break;
            case 12:
                if (isSuccess) {
                    currentState = 13;
                    //设置深度保养次数
                    SystemClock.sleep(100);
                    if (!TextUtils.isEmpty(mDepthWorkResult)) {
                        int i = Integer.parseInt(mDepthWorkResult, 16);
                        SendUtil.setDepthNumber(i);
                    }
                } else {
                    currentState = 12;
                    //设置加注量
                    SystemClock.sleep(100);
                    if (!TextUtils.isEmpty(mAddTaotle)) {
                        int resultNumber = Integer.parseInt(mAddTaotle, 16);
                        SendUtil.setChangeOilNumber(resultNumber);
                    }
                }
                break;
            case 13:
                if (isSuccess) {
                    currentState = 0;
                    FileUtil.putSendState(true);
                    ToastUtil.showMessage("设置成功,重启生效");
                } else {
                    currentState = 13;
                    //设置深度保养次数
                    SystemClock.sleep(100);
                    if (!TextUtils.isEmpty(mDepthWorkResult)) {
                        int i = Integer.parseInt(mDepthWorkResult, 16);
                        SendUtil.setDepthNumber(i);
                    }
                }
                break;

        }

    }

    private void initData() {
        mToobarBgImage.setImageResource(R.mipmap.top_bar_6);
        //弹窗设置dialog
        mDialog = new SetVersionDialog(this, R.style.dialog);
        mDateDialog = new SetDateDialog(this, R.style.dialog);

        mDialog.setDataResultListener(new SetVersionDialog.DataResultListener() {       //设置硬件版本,主控版本
            @Override
            public void resultData(String data, boolean isHardwareVersion) {
                mResultInt = Integer.parseInt(data);
                if (isHardwareVersion) {             //设置硬件
                    currentState = 2;
                    int resultXOR = 0x2a ^ 0x0a ^ 0x07 ^ mVwesionToSoftware ^ mResultInt ^ mDateYear ^ mDateMonth ^ mDateDay;
                    int[] arr = {0x2a, 0x0a, 0x07, mVwesionToSoftware, mResultInt, mDateYear, mDateMonth, mDateDay, resultXOR, 0x23};
                    SendUtil.sendMessage(arr, MyApplication.getOutputStream());
                } else {                             //设置主控
                    currentState = 1;
                    int resultXOR = 0x2a ^ 0x0a ^ 0x07 ^ mResultInt ^ mVwesionToHardware ^ mDateYear ^ mDateMonth ^ mDateDay;
                    int[] arr = {0x2a, 0x0a, 0x07, mResultInt, mVwesionToHardware, mDateYear, mDateMonth, mDateDay, resultXOR, 0x23};
                    SendUtil.sendMessage(arr, MyApplication.getOutputStream());
                }

            }
        });
        mDateDialog.setDateResultListener(new SetDateDialog.DateResultListener() {          //设置日期
            @Override
            public void dateResult(String date) {
                mResultData = date;
                currentState = 3;
                String year = mResultData.substring(2, 4);
                String month = mResultData.substring(4, 6);
                String day = mResultData.substring(6);
                int yearInt = Integer.parseInt(year);
                int monthInt = Integer.parseInt(month);
                int dayInt = Integer.parseInt(day);
                int resultXOR = 0x2a ^ 0x0a ^ 0x07 ^ mVwesionToHardware ^ mVwesionToSoftware ^ yearInt ^ monthInt ^ dayInt;
                int[] arr = {0x2a, 0x0a, 0x07, mVwesionToHardware, mVwesionToSoftware, yearInt, monthInt, dayInt, resultXOR, 0x23};
                SendUtil.sendMessage(arr, MyApplication.getOutputStream());
            }
        });
    }

    private void initListener() {
        mAboutSystemBack.setOnClickListener(this);
        mOpenQian.setOnClickListener(this);
        mBackOil.setOnClickListener(this);
        mOpenHou.setOnClickListener(this);
        mTouchBg.setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_qian:
                currentState = 6;
                SendUtil.openQian();
                break;
            case R.id.open_hou:
                currentState = 7;
                SendUtil.openHou();
                break;
            case R.id.about_system_back:
                isFinish = true;
                SendUtil.controlVoice();
                finish();
                break;
            case R.id.back_oil:
                if (isOpen) {
                    currentState = 8;
                    SendUtil.open4();
                } else {
                    currentState = 9;
                    SendUtil.closeAll();
                }
                break;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if ((x >= 32 && x <= 1223) && (y >= 6 && y <= 56)) {                //电子秤校准
            if (!TextUtils.isEmpty(mEquipmentNumber)){
                mJuDialog.setEquipmentId(mEquipmentNumber,2);
                mJuDialog.show();
            }
        } else if ((x >= 31 && x <= 1220) && (y >= 70 && y <= 120)) {         //设备上线
            if (!TextUtils.isEmpty(mEquipmentNumber)){
                mJuDialog.setEquipmentId(mEquipmentNumber,3);
                mJuDialog.show();
            }
        } else if ((x >= 36 && x <= 1225) && (y >= 138 && y <= 190)) {         //WiFi
            SendUtil.controlVoice();
            Intent i = new Intent(this, WifiActivity.class);
            startActivity(i);
        } else if ((x >= 34 && x <= 1230) && (y >= 203 && y <= 254)) {         //硬件版本
            if (!TextUtils.isEmpty(mEquipmentNumber)){
                mJuDialog.setEquipmentId(mEquipmentNumber,4);
                mJuDialog.show();
            }
        } else if ((x >= 34 && x <= 1226) && (y >= 333 && y <= 387)) {         //主控版本
            if (!TextUtils.isEmpty(mEquipmentNumber)){
                mJuDialog.setEquipmentId(mEquipmentNumber,5);
                mJuDialog.show();
            }
        } else if ((x >= 33 && x <= 1230) && (y >= 400 && y <= 450)) {         //日期
            if (!TextUtils.isEmpty(mEquipmentNumber)){
                mJuDialog.setEquipmentId(mEquipmentNumber,6);
                mJuDialog.show();
            }
        } else if ((x >= 33 && x <= 1223) && (y >= 268 && y <= 322)) {         //回复出厂设置
            if (!TextUtils.isEmpty(mEquipmentNumber)){
                mJuDialog.setEquipmentId(mEquipmentNumber,7);
                mJuDialog.show();
            }
        } else if ((x >= 35 && x <= 1227) && (y >= 466 && y <= 516)) {                             //设置ID号
            if (!TextUtils.isEmpty(mEquipmentNumber)){
                mJuDialog.setEquipmentId(mEquipmentNumber,8);
                mJuDialog.show();
            }

        }else if ((x >= 33 && x <= 1227) && (y >= 533 && y <= 583)) {                             //关于设备
            SendUtil.controlVoice();
            Intent intent = new Intent(this, AboutEquipmentActivity.class);
            startActivity(intent);
        }
        return false;
    }

    /**
     * 请求ID号的历史记录
     */
    private void loadDataForOldID(final String id) {
        FileUtil.putSendState(false);
        final Map<String, Object> pames = new HashMap<>();
        pames.put("url", Constants.URLS.GET_EQUIPMENT_STORY);
        pames.put("equipmentSerial", id);
        pames.put("source", "cur");
        final Protocol protocol = new Protocol() {
            @Override
            public void errorManage(IOException e) {
                MyApplication.getmHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        FileUtil.putSendState(true);
                        ToastUtil.showMessage("网络异常,无法设置ID");
                    }
                });
            }
            @Override
            public void parseData(Gson gson, String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String total = jsonObject.getString("total");
                    if ("1".equals(total)) {
                        isData = true;
                        JSONArray jsonArray = jsonObject.getJSONArray("rows");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String jarInfo = jsonObject1.getString("jarInfo");
                            String vehicleStatus = jsonObject1.getString("vehicleStatus");
                            if (jarInfo.length() >= 12 && vehicleStatus.length() >= 2) {
                                //历史记录的深度养护次数
                                mDepthWorkResult = jarInfo.substring(0, 4);
                                //历史记录的常规养护次数
                                mRoutineResult = jarInfo.substring(4, 8);
                                //历史记录的加注总升数
                                mAddTaotle = vehicleStatus.substring(0, 2) + jarInfo.substring(8);
                            }
                        }
                    }else {
                        isData = false;
                    }
                    SystemClock.sleep(1000);
                    mList = DataUtil.stringToA(id);
                    currentState = 10;
                    SendUtil.setEquipentId(mList);
                } catch (JSONException e) {
                    e.printStackTrace();
                    FileUtil.putSendState(true);
                }
            }
        };
        protocol.setParams(pames);
        protocol.loadDataFromNet();
    }

    /**
     * 获取设备已属人
     */
    private void getEquipmentUser() {
        if (!TextUtils.isEmpty(mEquipmentNumber)) {
            Map<String, Object> pames = new HashMap<>();
            pames.put("url", Constants.URLS.GET_EQUIPMENT_USER);
            pames.put("equipmentSerial", mEquipmentNumber);
            pames.put("source", "cur");
            Protocol protocol = new Protocol() {
                @Override
                public void errorManage(IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showMessage("网络异常");
                        }
                    });

                }
                @Override
                public void parseData(Gson gson, String s) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray rows = jsonObject.getJSONArray("rows");
                        for (int i = 0; i < rows.length(); i++) {
                            JSONObject object = rows.getJSONObject(i);
                            String faccount = object.getString("faccount");
                            if (!TextUtils.isEmpty(faccount))
                                deletteEquipment(faccount);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            protocol.setParams(pames);
            protocol.loadDataFromNet();
        }
    }

    /**
     * 删除设备
     *
     * @param faccount
     */
    private void deletteEquipment(String faccount) {
        Map<String, Object> pames = new HashMap<>();
        pames.put("url", Constants.URLS.DELETE_EQUIPMENT);
        pames.put("equipmentIds", mEquipmentNumber);
        pames.put("lowerFaccount", faccount);
        Protocol protocol = new Protocol() {
            @Override
            public void errorManage(IOException e) {

            }

            @Override
            public void parseData(Gson gson, String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    int code = object.getInt("code");
                    if (code == 0) {
                        String message = object.getString("message");
                        if ("删除成功".equals(message)) {
                            currentState = 5;
                            SendUtil.renewData();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        protocol.setParams(pames);
        protocol.loadDataFromNet();
    }


    @Override
    public void onCkcekState(int state, boolean isScurress,String msg) {
        if (isScurress){
            switch (state){
                case 2:                     //电子秤校准
                    SendUtil.controlVoice();
                    Intent intent = new Intent(this, CalibrationActivity.class);
                    startActivity(intent);
                    break;
                case 3:                     //设备上线
                    SendUtil.controlVoice();
                    OnlineDialog dialog = new OnlineDialog(this, R.style.dialog);
                    dialog.setBitmap(R.mipmap.popovers_onlin_1);
                    dialog.show();
                    dialog.setOnEquipmentIDlistener(new OnlineDialog.OnEquipmentIDlistener() {
                        @Override
                        public void idListener(String id) {
                            if (!TextUtils.isEmpty(id) && id.length() == 8) {
                                mList = DataUtil.stringToA(id);
                                currentState = 4;
                                SendUtil.setEquipentId(mList);
                            }
                        }
                    });
                    break;
                case 4:                     //硬件版本
                    SendUtil.controlVoice();
                    mDialog.setBitmap(R.mipmap.popovers_3, true);
                    mDialog.show();
                    break;
                case 5:                     //主控版本
                    SendUtil.controlVoice();
                    mDialog.setBitmap(R.mipmap.popovers_5, false);
                    mDialog.show();
                    break;
                case 6:                     //生产日期
                    SendUtil.controlVoice();
                    mDateDialog.setBitmap(R.mipmap.popovers_8);
                    mDateDialog.show();
                    break;
                case 7:                     //恢复出厂设置
                    final LoginDialog systemDialog = new LoginDialog(this, R.style.dialog, "");
                    NavigationBarUtil.focusNotAle(systemDialog.getWindow());
                    systemDialog.show();
                    //显示虚拟栏的时候 隐藏
                    NavigationBarUtil.hideNavigationBar(systemDialog.getWindow());
                    //再清理失能焦点
                    NavigationBarUtil.clearFocusNotAle(systemDialog.getWindow());
                    systemDialog.setOnLoninnListener(new LoginDialog.OnLonInListener() {
                        @Override
                        public void loginListener(Boolean b) {
                            if (b) {
                                mStopDialog.setBitmap(R.mipmap.popovers_danger_1);
                                mStopDialog.setOnMakeSuerListener(new StopDialog.OnMakeSuerListener() {
                                    @Override
                                    public void isMakeUser(boolean b) {
                                        if (b) {
                                            if (!"00000000".equals(mEquipmentNumber)) {
                                                getEquipmentUser();
                                            } else {
                                                currentState = 5;
                                                SendUtil.renewData();
                                            }
                                        }
                                    }
                                });

                                mStopDialog.show();
                            }
                        }
                    });
                    break;
                case 8:                 //设置ID好
                    if ("00000000".equals(mEquipmentNumber)) {
                        SetIDDialog d = new SetIDDialog(this, R.style.dialog);
                        d.setBitmap(R.mipmap.popovers_7);
                        d.show();
                        d.setDateResultListener(new SetIDDialog.DateResultListener() {
                            @Override
                            public void dateResult(String date) {
                                //请求旧ID号的历史记录
                                loadDataForOldID(date);
                            }
                        });
                    } else {
                        ToastUtil.showMessage("已经有ID号了");
                    }
                    break;
            }
        }else {
            ToastUtil.showMessage(msg);
        }
    }
}
