package com.cad.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.cad.R;

/**
 * Created by dell on 2018/4/3.
 */

public class CompleteDialog extends Dialog {

    public CompleteDialog(Context context, int themeResId) {
        super(context, themeResId);

    }
    public void setRes(int res){
        setContentView(R.layout.view_stop_complete);
        ImageView iv = (ImageView) findViewById(R.id.complete_bg);
        iv.setImageResource(res);
    }


}
