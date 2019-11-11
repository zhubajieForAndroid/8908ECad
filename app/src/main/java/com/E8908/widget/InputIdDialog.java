package com.E8908.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.E8908.R;
import com.E8908.util.NavigationBarUtil;

public class InputIdDialog extends Dialog implements View.OnClickListener, View.OnTouchListener {
    private OnSendOpenListener mOnSendOpenListener;
    private String mEquipmentNumber;
    private EditText mInputIDEt;
    private int mState; //前门或是后门的标记
    private Window mWindow;

    public InputIdDialog(Context context, int dialog, String equipmentNumber, int i) {
        super(context,dialog);
        mEquipmentNumber = equipmentNumber;
        mState = i;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_input_id);
        mWindow = getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = 700;
        lp.height = 280;
        mWindow.setAttributes(lp);
        initViews();
    }

    private void initViews() {
        mInputIDEt = findViewById(R.id.input_id_str);
        Button cancle = findViewById(R.id.cancle);
        Button sure = findViewById(R.id.sure);
        LinearLayout linearLayout = findViewById(R.id.input_container);

        cancle.setOnClickListener(this);
        sure.setOnClickListener(this);
        linearLayout.setOnTouchListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancle:
                dismiss();
                break;
            case R.id.sure:
                String trim = mInputIDEt.getText().toString().trim();
                if (!TextUtils.isEmpty(trim)){
                    if (mEquipmentNumber.equals(trim)){
                        dismiss();
                        mOnSendOpenListener.onOpen(mState);
                    }else {
                        ToastUtil.showMessage("ID不正确");
                    }
                }else {
                    ToastUtil.showMessage("内容不能为空");
                }
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //隐藏键盘
        InputMethodManager inputMethodManager = (InputMethodManager)getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return false;
    }

    public interface OnSendOpenListener{
        void onOpen(int state);
    }

    public void setOnSendOpenListener(OnSendOpenListener onSendOpenListener) {
        mOnSendOpenListener = onSendOpenListener;
    }

}
