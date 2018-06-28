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
import com.cad.activity.WifiActivity;
import com.cad.util.SendUtil;

/**
 * Created by dell on 2018/4/3.
 */

public class AddErrorDialog extends Dialog implements View.OnTouchListener {

    private static final String TAG = "AddErrorDialog";
    private MakeSuperListener mMakeSuperListener;
    private int mRes ;

    public AddErrorDialog(Context context, int themeResId,int res) {
        super(context, themeResId);
        mRes = res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_error_dialog);
        ImageView iv = (ImageView) findViewById(R.id.error_bg);
        iv.setImageResource(mRes);
        iv.setOnTouchListener(this);
        setCanceledOnTouchOutside(false);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if ((x >= 90 && x <= 480) && (y >= 525 && y <= 590)) {
            mMakeSuperListener.isMakeSupter(true);
        }
        return false;
    }
    public interface MakeSuperListener{
        void isMakeSupter(boolean supter);
    }
    public void setMakeSuperListener(MakeSuperListener m){
        mMakeSuperListener = m;
    }
}
