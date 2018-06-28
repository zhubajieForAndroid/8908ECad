package com.cad.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cad.R;


/**
 * Created by dell on 2017/6/21.
 */

public class MyToast {

    private  Toast mToast;


    private MyToast(Context context, CharSequence text, int duration){
        View inflate = LayoutInflater.from(context).inflate(R.layout.view_toast, null);
        TextView tv = (TextView) inflate.findViewById(R.id.toast_text);
        tv.setText(text);
        mToast = new Toast(context);
        mToast.setDuration(duration);
        mToast.setView(inflate);

    }
    public static MyToast makeText(Context context, CharSequence text, int duration){
        return new MyToast(context, text,duration);
    }
    public void show(){
        if (mToast != null){
            mToast.show();
        }
    }
}
