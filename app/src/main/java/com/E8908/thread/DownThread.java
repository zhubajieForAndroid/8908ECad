package com.E8908.thread;

import android.os.Handler;
import android.os.Message;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private OnDownLoadSuccess mOnDownLoadSuccess;


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
                mOnDownLoadSuccess.downloading(mTotalLength / 1024,mDownloadLength / 1024);
                mFileOutputStream.flush();
            }
            if (mUserState) {
                mOnDownLoadSuccess.downloadSuccess(mApkFile);
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


    public interface OnDownLoadSuccess{
        void downloadSuccess(File file);
        void downloading(long totalLength,long progress);
    }

    public void setOnDownLoadSuccess(OnDownLoadSuccess onDownLoadSuccess) {
        mOnDownLoadSuccess = onDownLoadSuccess;
    }
}
