package com.E8908.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.E8908.R;
import com.E8908.util.NavigationBarUtil;

public class SetXishuDialog extends Dialog implements View.OnClickListener {
    private OnSendOpenListener mOnSendOpenListener;

    private EditText mInputIDEt;

    private Window mWindow;

    public SetXishuDialog(Context context, int dialog) {
        super(context,dialog);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_input_set_xishu);
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
        cancle.setOnClickListener(this);
        sure.setOnClickListener(this);


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
                    mOnSendOpenListener.onOpen(Integer.parseInt(trim));
                    dismiss();
                }else {
                    ToastUtil.showMessage("内容不能为空");
                }
                break;
        }
    }
    public interface OnSendOpenListener{
        void onOpen(int state);
    }

    public void setOnSendOpenListener(OnSendOpenListener onSendOpenListener) {
        mOnSendOpenListener = onSendOpenListener;
    }

}
