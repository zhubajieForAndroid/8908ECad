package com.E8908.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.E8908.util.NavigationBarUtil;
import com.E8908.util.OkhttpManager;
import com.E8908.R;
import com.E8908.base.MyApplication;
import com.E8908.conf.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by dell on 2018/4/2.
 */

public class OpenDialog extends Dialog implements View.OnTouchListener, ReservationCodeDialog.OnLonInListener, DialogInterface.OnDismissListener{

    private static final String TAG = "OpenDialog";
    private Context mContext;
    private ImageView mEr_iv;
    private OnOpenListener mOnOpenListener;
    private String id;
    private ReservationCodeDialog mDialog;
    private LinearLayout mLinearLayout;
    private OnCancelButtonListener mOnCancelButtonListener;
    private Timer mTimer;
    private OkhttpManager mOkhttpManager;
    private String mSubstring;
    private Window mWindow;


    public OpenDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;

    }
    public void setinit(String equipmentNumber) {
        setContentView(R.layout.view_open);
        mWindow = getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = 763;
        lp.height = 580;
        mWindow.setAttributes(lp);
        setCanceledOnTouchOutside(false);

        id = equipmentNumber;
        ImageView bg = findViewById(R.id.touch_bg);
        mEr_iv = findViewById(R.id.er_iv);
        bg.setOnTouchListener(this);
        mDialog = new ReservationCodeDialog(mContext, R.style.dialog);
        mDialog.setOnLoninnListener(this);
        mLinearLayout = findViewById(R.id.progress_bar);

        mOkhttpManager = OkhttpManager.getOkhttpManager();



        startTask();
        setOnDismissListener(this);

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if ((x >= 63 && x <= 353) && (y >= 485 && y <= 535)) {                //取消
            mOnCancelButtonListener.onCancelClick();
            dismiss();
        }else if ((x >= 420 && x <= 714) && (y >= 481 && y <= 536)) {            //预约码
            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        }
        return false;
    }

    private void performNetRequest(String reservationCode) {
        Map<String, String> pames = new HashMap<>();
        pames.put("deviceno", id+"");
        pames.put("codeValue", reservationCode+"");
        mOkhttpManager.doPost(Constants.URLS.CHECK_VINCODE, pames, mCallback);

    }

    private Callback mCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            mOnOpenListener.errorMsg("网络异常,无法验证");
            MyApplication.getmHandler().post(new Runnable() {
                @Override
                public void run() {
                    mLinearLayout.setVisibility(View.GONE);
                    dismiss();
                }
            });

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful()) {
                String string = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    int errno = jsonObject.getInt("code");
                    if (errno == 0) {
                        mOnOpenListener.startOpen("0");
                    } else {
                        mOnOpenListener.errorMsg(jsonObject.getString("message"));
                    }
                    MyApplication.getmHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            mLinearLayout.setVisibility(View.GONE);
                        }
                    });
                    dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                mOnOpenListener.errorMsg("服务器错误");
                MyApplication.getmHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        mLinearLayout.setVisibility(View.GONE);
                        dismiss();
                    }
                });

            }
        }
    };

    private void startTask(){
        if (mTimer == null) {
            mTimer = new Timer();
            mTimer.schedule(new MyTask(), 1000, 1000);
        }
    }
    private void stopTask(){
        if (mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
    }
    public class MyTask extends TimerTask{
        @Override
        public void run() {
            mOkhttpManager.doPost(Constants.URLS.QUERY_START_STATE,id,mStartCallback);
        }
    }

    private Callback mStartCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {}
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String string = response.body().string();
            if (!TextUtils.isEmpty(string) && string.length()>9){
                mSubstring = string.substring(8,9);
                if ("9".equals(mSubstring)){         //收到开机了
                    dismiss();
                    mOnOpenListener.startOpen("9");
                }
            }
        }
    };
    @Override
    public void loginListener(String code) {           //开始验证
        mLinearLayout.setVisibility(View.VISIBLE);
        performNetRequest(code);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        stopTask();
    }
    @Override
    public void show() {
        NavigationBarUtil.focusNotAle(mWindow);
        super.show();
        NavigationBarUtil.hideNavigationBar(mWindow);
        NavigationBarUtil.clearFocusNotAle(mWindow);
    }


    public interface OnOpenListener {
        void startOpen(String state);
        void errorMsg(String msg);
    }

    public void setOnOpenListener(OnOpenListener onOpenListener) {
        mOnOpenListener = onOpenListener;
    }

    public interface OnCancelButtonListener {
        void onCancelClick();
    }

    public void setOnCancelButtonListener(OnCancelButtonListener onCancelButtonListener) {
        mOnCancelButtonListener = onCancelButtonListener;
    }

}
