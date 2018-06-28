package com.cad.thread;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cad.base.MyApplication;
import com.cad.util.CheckVersion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.content.ContentValues.TAG;

/**
 * Created by dell on 2018/5/10.
 */

public class DownThread extends Thread {
    private Handler mHandler;
    private long mDownloadLength;
    private long mTotalLength;
    private InputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;
    private boolean mUserState;
    private File mApkFile;


    public void setData(Handler handler, long downloadLength, long totalLength, FileOutputStream fos, InputStream fis, boolean userState, File apkFile) {
        mHandler = handler;
        mDownloadLength = downloadLength;
        mTotalLength = totalLength;
        mFileInputStream = fis;
        mFileOutputStream = fos;
        mUserState = userState;
        mApkFile = apkFile;
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
        mUserState = true;
    }

    @Override
    public void run() {
        super.run();
        byte[] buffer = new byte[2048];
        int len;
        try {
            while ((len = mFileInputStream.read(buffer)) != -1) {
                mFileOutputStream.write(buffer, 0, len);
                mDownloadLength += len;
                if (mHandler != null) {
                    Message message = new Message();
                    message.what = 1;
                    message.arg2 = (int) (mTotalLength / 1024);
                    message.arg1 = (int) (mDownloadLength / 1024);
                    mHandler.sendMessage(message);
                }
                mFileOutputStream.flush();
            }
            if (mUserState) {
                installApk(mApkFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (mFileInputStream != null)
                    mFileInputStream.close();
                if (mFileOutputStream != null) {
                    mFileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 安装APK
     */
    private void installApk(File loadFile) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(loadFile), "application/vnd.android.package-archive");
        MyApplication.getContext().startActivity(intent);
    }
}
