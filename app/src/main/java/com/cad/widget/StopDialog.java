package com.cad.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.cad.R;
import com.cad.activity.CalibrationActivity;
import com.cad.util.SendUtil;

import static android.content.ContentValues.TAG;

/**
 * Created by dell on 2018/4/3.
 */

public class StopDialog extends Dialog implements View.OnTouchListener {
    private OnMakeSuerListener mOnMakeSuerListener;
    public StopDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    public void setBitmap(int res){
        setContentView(R.layout.view_stop_dialog);
        ImageView iv = (ImageView) findViewById(R.id.top_bg);
        iv.setImageResource(res);
        iv.setOnTouchListener(this);
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if ((x >= 69 && x <= 283) && (y >= 195 && y <= 254)) {                //确定
            mOnMakeSuerListener.isMakeUser(true);
        }else if ((x >= 418 && x <= 632) && (y >= 196 && y <= 253)){            //取消
            dismiss();
        }
        return false;
    }
    public interface OnMakeSuerListener{
        void isMakeUser(boolean b);
    }
    public void setOnMakeSuerListener(OnMakeSuerListener o){
        mOnMakeSuerListener = o;
    }
}
