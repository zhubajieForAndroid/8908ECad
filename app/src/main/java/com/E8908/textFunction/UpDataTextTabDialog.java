package com.E8908.textFunction;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.conf.Constants;
import com.E8908.util.OkhttpManager;
import com.E8908.widget.ToastUtil;

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

import static android.support.constraint.Constraints.TAG;

/**
 * 测试程序上传报告
 */
public class UpDataTextTabDialog extends Dialog implements View.OnClickListener {


    @Bind(R.id.updata_time)
    TextView mUpdataTime;
    @Bind(R.id.state_add)
    TextView mStateAdd;
    @Bind(R.id.state_back)
    TextView mStateBack;
    @Bind(R.id.state_wuhua)
    TextView mStateWuhua;
    @Bind(R.id.state_shajun)
    TextView mStateShajun;
    @Bind(R.id.state_jinghua)
    TextView mStateJinghua;
    @Bind(R.id.state_dtu)
    TextView mStateDtu;
    @Bind(R.id.state_service)
    TextView mStateService;
    @Bind(R.id.loop_count)
    TextView mLoopCount;
    @Bind(R.id.cancle)
    Button mCancle;
    @Bind(R.id.updata)
    Button mUpdata;
    private boolean isAddFail = false;              //加注功能是否失败
    private boolean isBackFail = false;              //回收功能是否失败
    private boolean isWuhuaFail = false;              //雾化功能是否失败
    private boolean isShajunFail = false;              //杀菌功能是否失败
    private boolean isJinghuaFail = false;              //净化功能是否失败
    private boolean isServiceFail = false;              //服务器通讯是否正常
    private boolean isDTUFail = false;              //DTU通讯是否正常
    private String mEquipmentId;
    private int mLoopCountInt;
    private Handler mHandler;

    public UpDataTextTabDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_updata_text_dialog);
        ButterKnife.bind(this);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = 600;
            attributes.height = 500;
            window.setAttributes(attributes);
        }
        mHandler = new Handler();
        mCancle.setOnClickListener(this);
        mUpdata.setOnClickListener(this);
        Date currentTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = dateFormat.format(currentTime);
        mUpdataTime.setText(dateString);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancle:
                dismiss();
                break;
            case R.id.updata:
                upData();
                break;
        }

    }

    private void upData() {
        Map<String,String> pames = new HashMap<>();
        pames.put("equipmentType","ACC-8908E");
        pames.put("equipmentCode",mEquipmentId);
        if (isAddFail) {
            pames.put("fillStatus", "1");
        }else {
            pames.put("fillStatus", "0");
        }
        if (isBackFail) {
            pames.put("recycleStatus", "1");
        }else {
            pames.put("recycleStatus", "0");
        }
        if (isWuhuaFail) {
            pames.put("atomizeStatus", "1");
        }else {
            pames.put("atomizeStatus", "0");
        }
        if (isShajunFail) {
            pames.put("sterilizeStatus", "1");
        }else {
            pames.put("sterilizeStatus", "0");
        }
        if (isJinghuaFail) {
            pames.put("purifyStatus", "1");
        }else {
            pames.put("purifyStatus", "0");
        }
        if (isServiceFail) {
            pames.put("connectStatus", "1");
        }else {
            pames.put("connectStatus", "0");
        }
        if (isDTUFail) {
            pames.put("DTUStatus", "1");
        }else {
            pames.put("DTUStatus", "0");
        }
        pames.put("cycleTestTimes",mLoopCountInt+"");
        OkhttpManager.getOkhttpManager().doPost(Constants.URLS.UP_TEXT_TAB,pames,mCallback);
    }
    private Callback mCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                  dismiss();
                    ToastUtil.showMessage("网络异常");
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful()) {
                String string = response.body().string();
                if (!TextUtils.isEmpty(string) && string.length() > 9) {
                    String substring = string.substring(8, 9);
                    if ("0".equals(substring)) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                dismiss();
                                ToastUtil.showMessage("保存成功");
                            }
                        });
                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                dismiss();
                                ToastUtil.showMessage("保存失败");
                            }
                        });
                    }
                }else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dismiss();
                            ToastUtil.showMessage("保存失败");
                        }
                    });
                }
            }else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                        ToastUtil.showMessage("保存失败");
                    }
                });
            }
        }
    };
    public void setData(boolean isAddFail, boolean isBackFail, boolean isWuhuaFail, boolean isShajunFail, boolean isJinghuaFail, boolean isServiceFail, boolean isDTUFail, String equipmentNumber,int loopCount) {
        this.isAddFail = isAddFail;
        this.isBackFail = isBackFail;
        this.isWuhuaFail = isWuhuaFail;
        this.isShajunFail = isShajunFail;
        this.isJinghuaFail = isJinghuaFail;
        this.isServiceFail = isServiceFail;
        this.isDTUFail = isDTUFail;
        mEquipmentId = equipmentNumber;
        mLoopCountInt = loopCount;
        mLoopCount.setText(mLoopCountInt+"");
        if (isAddFail){
            mStateAdd.setTextColor(getContext().getResources().getColor(R.color.red));
            mStateAdd.setText("异常");
        }else {
            mStateAdd.setTextColor(getContext().getResources().getColor(R.color.white));
            mStateAdd.setText("正常");
        }
        if (isBackFail){
            mStateBack.setTextColor(getContext().getResources().getColor(R.color.red));
            mStateBack.setText("异常");
        }else {
            mStateBack.setTextColor(getContext().getResources().getColor(R.color.white));
            mStateBack.setText("正常");
        }
        if (isWuhuaFail){
            mStateWuhua.setTextColor(getContext().getResources().getColor(R.color.red));
            mStateWuhua.setText("异常");
        }else {
            mStateWuhua.setTextColor(getContext().getResources().getColor(R.color.white));
            mStateWuhua.setText("正常");
        }
        if (isShajunFail){
            mStateShajun.setTextColor(getContext().getResources().getColor(R.color.red));
            mStateShajun.setText("异常");
        }else {
            mStateShajun.setTextColor(getContext().getResources().getColor(R.color.white));
            mStateShajun.setText("正常");
        }
        if (isJinghuaFail){
            mStateJinghua.setTextColor(getContext().getResources().getColor(R.color.red));
            mStateJinghua.setText("异常");
        }else {
            mStateJinghua.setTextColor(getContext().getResources().getColor(R.color.white));
            mStateJinghua.setText("正常");
        }
        if (isServiceFail){
            mStateService.setTextColor(getContext().getResources().getColor(R.color.red));
            mStateService.setText("异常");
        }else {
            mStateService.setTextColor(getContext().getResources().getColor(R.color.white));
            mStateService.setText("正常");
        }
        if (isServiceFail){
            mStateService.setTextColor(getContext().getResources().getColor(R.color.red));
            mStateService.setText("异常");
        }else {
            mStateService.setTextColor(getContext().getResources().getColor(R.color.white));
            mStateService.setText("正常");
        }
        if (isDTUFail){
            mStateDtu.setTextColor(getContext().getResources().getColor(R.color.red));
            mStateDtu.setText("异常");
        }else {
            mStateDtu.setTextColor(getContext().getResources().getColor(R.color.white));
            mStateDtu.setText("正常");
        }
    }


}
