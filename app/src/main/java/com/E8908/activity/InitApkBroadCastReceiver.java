package com.E8908.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.E8908.conf.Constants;

import java.io.File;

/**
 * Created by dell on 2018/5/7.
 */

public class InitApkBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {
            File path = Environment.getExternalStorageDirectory();
            File apkFile = new File(path, Constants.DOWN_NAME+".apk");
            if (apkFile.exists()){
                apkFile.delete();
            }
        }
    }
}
