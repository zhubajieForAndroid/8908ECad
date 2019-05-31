package com.E8908.widget;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.blueTooth.utils.DataBleUtil;
import com.E8908.conf.Constants;
import com.E8908.util.NavigationBarUtil;

import butterknife.Bind;
import butterknife.ButterKnife;


public class GasDialog extends Dialog {

    @Bind(R.id.formaldehyde_tv)
    TextView mFormaldehydeTv;
    @Bind(R.id.progress)
    MyProgressBar mProgress;
    @Bind(R.id.tvoc_tv)
    TextView mTvocTv;
    @Bind(R.id.progress_tvoc)
    MyProgressBar mProgressTvoc;
    @Bind(R.id.pm_tv)
    TextView mPmTv;
    @Bind(R.id.progress_pm)
    MyProgressBar mProgressPm;
    @Bind(R.id.wen_tv)
    TextView mWenTv;
    @Bind(R.id.progress_wen)
    MyProgressBar mProgressWen;
    @Bind(R.id.shi_tv)
    TextView mShiTv;
    @Bind(R.id.progress_shi)
    MyProgressBar mProgressShi;
    private Context mContext;
    private final Window mWindow;


    public GasDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        setContentView(R.layout.view_gas_dialog);
        regBroadcast();
        ButterKnife.bind(this);
        mWindow = getWindow();
        WindowManager.LayoutParams attributes = mWindow.getAttributes();
        attributes.gravity = Gravity.CENTER;
        attributes.width = 1000;
        attributes.height = 500;
        mWindow.setAttributes(attributes);
        //设置每个进度条的最大值
        mProgress.setMax(6250);      //甲醛检测范围甲醛:0-6.25mg/m3
        mProgressTvoc.setMax(1000);     //tvov:0-10mg/m3 读到的是放大100被
        mProgressPm.setMax(300);           //pm2.5:ug/m3读到的数值  0-300
        mProgressWen.setMax(800);            //温度:-40  到 80
        mProgressShi.setMax(999);           //湿度:0-99.9%Rh
    }


    /**
     * 注册串口通讯广播
     */
    public void regBroadcast() {
        //注册广播接受者接受蓝牙数据
        IntentFilter blefilter = new IntentFilter();
        blefilter.addAction(Constants.BLE_DATA);
        mContext.registerReceiver(mBleReceiver, blefilter);
    }
    private BroadcastReceiver mBleReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constants.BLE_DATA.equals(action)) {
                final byte[] buffer = intent.getByteArrayExtra("data");
                analysisData(buffer);
            }
        }
    };

    private void analysisData(byte[] buffer) {
        if (buffer == null)
            return;
        String checkGasState = DataBleUtil.getBleCheckGasState(buffer);       //气体检测仪的状态
        String preheatState = checkGasState.substring(7, 8);  //预热状态
        if ("0".equals(preheatState)) {              //预热完成
            String tvoc = DataBleUtil.getTVOC(buffer);
            String formaldehyde = DataBleUtil.getFormaldehyde(buffer);//甲醛
            String pm = DataBleUtil.getPM(buffer);
            String humidity = DataBleUtil.getHumidity(buffer); //湿度
            String temperatureByCheck = DataBleUtil.getTemperatureByCheck(buffer);//温度

            float formaldehydeInt = Integer.parseInt(formaldehyde, 16);       //甲醛
            if (formaldehydeInt < 60) {
                mProgress.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progress_colors));
            }
            if (formaldehydeInt >= 60 && formaldehydeInt < 100) {
                mProgress.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progress_colors_blue));
            }
            if (formaldehydeInt >= 100 && formaldehydeInt < 300) {             //轻度污染
                mProgress.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progress_colors_yellow));
            }
            if (formaldehydeInt >= 300) {                                    //重度污染
                mProgress.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progress_colors_red));
            }
            //800
            mProgress.setProgress((int) formaldehydeInt);
            mFormaldehydeTv.setText(formaldehydeInt / 1000 + " mg/m³");

            float tvocInt = Integer.parseInt(tvoc, 16);             //tvoc
            if (tvocInt < 60) {
                mProgressTvoc.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progress_colors));
            }
            if (tvocInt >= 60 && tvocInt < 100) {
                mProgressTvoc.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progress_colors_blue));
            }
            if (tvocInt >= 100 && tvocInt < 160) {
                mProgressTvoc.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progress_colors_yellow));
            }
            if (tvocInt >= 160) {
                mProgressTvoc.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progress_colors_red));
            }
            mProgressTvoc.setProgress((int) tvocInt);
            mTvocTv.setText(tvocInt / 100 + " mg/m³");

            float pmInt = Integer.parseInt(pm, 16);               //pm
            if (pmInt < 35) {
                mProgressPm.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progress_colors));
            }
            if (pmInt >= 35 && pmInt < 75) {
                mProgressPm.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progress_colors_blue));
            }
            if (pmInt >= 75 && pmInt < 150) {
                mProgressPm.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progress_colors_yellow));
            }
            if (pmInt >= 150) {
                mProgressPm.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progress_colors_red));
            }
            //35
            mProgressPm.setProgress((int) pmInt);
            mPmTv.setText(pmInt + " ug/m³");

            float wenInt = Integer.parseInt(temperatureByCheck, 16);        //温度
            if (wenInt < 180) {
                mProgressWen.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progress_colors_red));
            }
            if (wenInt > 250 && wenInt <= 320) {
                mProgressWen.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progress_colors));
            }
            if (wenInt > 320) {
                mProgressWen.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progress_colors_red));
            }

            mProgressWen.setProgress((int) wenInt);
            mWenTv.setText(wenInt / 10 + " C°");

            float shiInt = Integer.parseInt(humidity, 16);//湿度
            if (wenInt >= 50 && wenInt <= 60) {
                mProgressShi.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progress_colors));
            } else {
                mProgressShi.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progress_colors_red));
            }
            mProgressShi.setProgress((int) shiInt);
            mShiTv.setText(shiInt / 10 + "% RH");
        }
    }



    @Override
    public void dismiss() {
        mContext.unregisterReceiver(mBleReceiver);
        ButterKnife.unbind(this);
        super.dismiss();
    }
    @Override
    public void show() {
        NavigationBarUtil.focusNotAle(mWindow);
        super.show();
        NavigationBarUtil.hideNavigationBar(mWindow);
        NavigationBarUtil.clearFocusNotAle(mWindow);
    }
}
