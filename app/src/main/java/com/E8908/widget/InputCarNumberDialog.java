package com.E8908.widget;

import android.app.Dialog;
import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.util.FileUtil;
import com.E8908.util.IoUtils;
import com.E8908.util.KeyboardUtil;
import com.E8908.util.NavigationBarUtil;
import com.E8908.util.StringUtils;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class InputCarNumberDialog extends Dialog implements View.OnTouchListener, View.OnClickListener{
    private OnMakeSureBtnClickListener mOnMakeSureBtnClickListener;
    private EditText mInputNumber;
    private Context mContext;
    private KeyboardView mKeyboardView;
    private KeyboardUtil mKeyboardUtil;
    private final File mCarNumberRecord;
    private List<String> carNumbers = new ArrayList<>();
    private Thread mThread;
    private TextView mCarNumberOne;
    private TextView mCarNumberTwo;
    private TextView mCarNumberThree;
    private boolean isDisplayHistory = true;
    private OnStartActivityListener mOnStartActivityListener;
    private Window mWindow;

    public InputCarNumberDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        //创建记录输入的车牌号文件
        mCarNumberRecord = FileUtil.createNewFile("caeNumberRecord", "carNumberList.txt");

    }

    public void setBitmap() {
        mWindow = getWindow();
        setContentView(R.layout.view_input_car_number_dialog);
        ImageView iv = findViewById(R.id.top_bg);
        mInputNumber = findViewById(R.id.input_number);
        mKeyboardView = findViewById(R.id.keyboard_view);
        mCarNumberOne = findViewById(R.id.carnumber_one);
        mCarNumberTwo = findViewById(R.id.carnumber_two);
        mCarNumberThree = findViewById(R.id.carnumber_three);
        mCarNumberOne.setOnClickListener(this);
        mCarNumberTwo.setOnClickListener(this);
        mCarNumberThree.setOnClickListener(this);
        mInputNumber.setOnClickListener(this);
        iv.setOnTouchListener(this);

        if (mWindow != null) {
            WindowManager.LayoutParams wlp = mWindow.getAttributes();
            wlp.gravity = Gravity.CENTER;
            wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            mWindow.setAttributes(wlp);
        }
        mKeyboardUtil = new KeyboardUtil(mKeyboardView, mContext, mInputNumber);
        if (!mKeyboardUtil.isShow()) {
            mKeyboardUtil.showKeyboard();
        }
        try {
            InputStreamReader inStream = new InputStreamReader(new FileInputStream(mCarNumberRecord), Charset.forName("gbk"));
            BufferedReader reader = new BufferedReader(inStream);
            String readLine = null;
            while ((readLine = reader.readLine()) != null) {
                carNumbers.add(readLine);
            }

            IoUtils.closeFileStream(reader);
            //mThread = null;
        } catch (IOException e) {
            //mThread = null;
            e.printStackTrace();
        }
        //readData();
        setVis();
    }

    private void readData() {
        mThread = new Thread() {
            @Override
            public void run() {
                super.run();

            }
        };
        mThread.start();
    }

    private void setVis() {
        switch (carNumbers.size()) {
            case 1:
                mCarNumberOne.setText(carNumbers.get(0));
                mCarNumberTwo.setVisibility(View.GONE);
                mCarNumberThree.setVisibility(View.GONE);
                mCarNumberOne.setVisibility(View.VISIBLE);
                break;
            case 2:
                mCarNumberOne.setText(carNumbers.get(0));
                mCarNumberTwo.setText(carNumbers.get(1));
                mCarNumberThree.setVisibility(View.GONE);
                mCarNumberOne.setVisibility(View.VISIBLE);
                mCarNumberTwo.setVisibility(View.VISIBLE);
                break;
            case 3:
                mCarNumberOne.setText(carNumbers.get(0));
                mCarNumberTwo.setText(carNumbers.get(1));
                mCarNumberThree.setText(carNumbers.get(2));
                mCarNumberOne.setVisibility(View.VISIBLE);
                mCarNumberTwo.setVisibility(View.VISIBLE);
                mCarNumberThree.setVisibility(View.VISIBLE);
                break;
            default:
                mCarNumberOne.setVisibility(View.GONE);
                mCarNumberTwo.setVisibility(View.GONE);
                mCarNumberThree.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if ((x >= 64 && x <= 285) && (y >= 257 && y <= 310)) {                //取消
            dismiss();
            mKeyboardUtil.hideKeyboard();
        } else if ((x >= 345 && x <= 568) && (y >= 258 && y <= 312)) {            //确定
            String carNumberStr = mInputNumber.getText().toString().trim();
            if (!TextUtils.isEmpty(carNumberStr)) {
                boolean b = StringUtils.ckeckCarNumber(carNumberStr);
                if (b) {
                    mOnMakeSureBtnClickListener.onBtnClick(carNumberStr);
                    //写车牌到文件
                    writerData(carNumberStr);
                    dismiss();
                    mKeyboardUtil.hideKeyboard();
                } else {
                    ToastUtil.showMessage("非法车牌");
                }
            } else {
                ToastUtil.showMessage("车牌不为空");
            }
        }else if ((x >= 485 && x <= 550) && (y >= 128 && y <= 187)) {                //扫取车牌号
            mOnStartActivityListener.onStartActiity();
        }
        return false;
    }

    private void writerData(final String carNumberStr) {
        mThread = new Thread() {
            @Override
            public void run() {
                try {
                    if (carNumbers.size() >= 3) {
                        carNumbers.add(0, carNumberStr);
                        carNumbers.remove(3);
                    } else {
                        carNumbers.add(0, carNumberStr);
                    }
                    OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(mCarNumberRecord), Charset.forName("gbk"));
                    BufferedWriter writer = new BufferedWriter(writerStream);
                    for (int i = 0; i < carNumbers.size(); i++) {
                        writer.write(carNumbers.get(i));
                        writer.newLine();
                    }
                    writer.flush();
                    IoUtils.closeFileStream(writer);
                    mThread = null;     //任务执行完置空任务
                } catch (IOException e) {
                    mThread = null;
                    e.printStackTrace();
                }
            }
        };
        mThread.start();
    }

    @Override
    public void onClick(View v) {
        Editable editable = mInputNumber.getText();
        switch (v.getId()) {
            case R.id.carnumber_one:
                String oneCarNumber = mCarNumberOne.getText().toString();
                editable.insert(0, oneCarNumber);
                mCarNumberOne.setVisibility(View.GONE);
                mCarNumberTwo.setVisibility(View.GONE);
                mCarNumberThree.setVisibility(View.GONE);
                break;
            case R.id.carnumber_two:
                String twoCarNumber = mCarNumberTwo.getText().toString();
                editable.insert(0, twoCarNumber);
                mCarNumberOne.setVisibility(View.GONE);
                mCarNumberTwo.setVisibility(View.GONE);
                mCarNumberThree.setVisibility(View.GONE);
                break;
            case R.id.carnumber_three:
                String threeCarNumber = mCarNumberThree.getText().toString();
                editable.insert(0, threeCarNumber);
                mCarNumberOne.setVisibility(View.GONE);
                mCarNumberTwo.setVisibility(View.GONE);
                mCarNumberThree.setVisibility(View.GONE);
                break;
            case R.id.input_number:
                if (!isDisplayHistory) {
                    isDisplayHistory = true;
                    setVis();
                } else {
                    isDisplayHistory = false;
                    mCarNumberOne.setVisibility(View.GONE);
                    mCarNumberTwo.setVisibility(View.GONE);
                    mCarNumberThree.setVisibility(View.GONE);
                }
                break;
        }

    }

    public void setResultCarNumber(String carNumber) {
        if (TextUtils.isEmpty(carNumber))
            return;
        Editable editable = mInputNumber.getText();
        editable.insert(0,carNumber);
    }



    public interface OnMakeSureBtnClickListener {
        void onBtnClick(String carNumber);
    }

    public void setOnMakeSureBtnClickListener(OnMakeSureBtnClickListener onMakeSureBtnClickListener) {
        mOnMakeSureBtnClickListener = onMakeSureBtnClickListener;
    }
    public interface OnStartActivityListener{
        void onStartActiity();
    }

    public void setOnStartActivityListener(OnStartActivityListener onStartActivityListener) {
        mOnStartActivityListener = onStartActivityListener;
    }


}
