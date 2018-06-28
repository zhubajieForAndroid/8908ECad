package com.cad.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cad.R;
import com.cad.util.BItmapUtil;


/**
 * Created by dell on 2017/5/11.
 */

public class LoginDialog extends Dialog implements View.OnTouchListener {
    private Context mContext;
    private ImageView mIv;
    private static final String TAG = "LoginDialog";
    private EditText mEt;
    private OnLonInListener mOnLonInListener;
    private String mPassworld;

    public LoginDialog(Context context, int themeResId, String passworld) {
        super(context, themeResId);
        mContext = context;
        mPassworld = passworld;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_login_dialog);
        mIv = (ImageView) findViewById(R.id.iv);
        mEt = (EditText) findViewById(R.id.et);

        Bitmap bitmap = BItmapUtil.compressBItmap(mContext, R.mipmap.login_img_normal_19);
        mIv.setImageBitmap(bitmap);
        initListener();

    }

    private void initListener() {
        mIv.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float y = event.getY();
        float x = event.getX();
        if (x> 85 && x<230 &&y>180 &&y<230){
            dismiss();
        }else if (x> 350 && x<505 &&y>180 &&y<235){
            String passworld = mEt.getText().toString().trim();
            if (TextUtils.isEmpty(mPassworld)){
                //校验登录系统设置密码
                if (passworld.equals("581828")){
                    mOnLonInListener.loginListener(true);
                    dismiss();
                }else {
                    ToastUtil.showMessage("密码错误");
                    mOnLonInListener.loginListener(false);
                    dismiss();
                }
            }
        }
        return false;
    }


    public interface OnLonInListener{
        void loginListener(Boolean b);
    }
    public void setOnLoninnListener(OnLonInListener listener){
        mOnLonInListener = listener;
    }

}
