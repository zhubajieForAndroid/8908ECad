package com.cad.widget;

import android.widget.Toast;

import com.cad.base.MyApplication;


/**
 * Created by dell on 2017/12/5.
 */

public class ToastUtil {
    public static void showMessage(String data) {
        MyToast.makeText(MyApplication.getContext(), data, Toast.LENGTH_SHORT).show();
    }

}
