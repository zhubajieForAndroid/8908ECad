package com.cad.thread;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.cad.base.MyApplication;
import com.cad.conf.Constants;
import com.cad.util.DataUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.content.ContentValues.TAG;

/**
 * Created by dell on 2018/1/2.
 */

public class ReadThread extends Thread {
    private int mIndex;
    private InputStream mInputStream;

    public void setInputStream(InputStream inputStream) {
        mInputStream = inputStream;
    }


    @Override
    public void run() {
        DataUtil.testDate();//获取网络时间
        while (!isInterrupted()) {
            try {
                byte[] resole = resole(mInputStream);
               if (resole.length != 1){
                   // 一个完整包即产生
                   Intent intent = new Intent();
                   intent.setAction(Constants.DATA);
                   intent.putExtra("response", resole);
                   intent.putExtra("size", resole.length);
                   MyApplication.getContext().sendBroadcast(intent);
               }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    /**
     * 入参：输入流
     * 返回：byte数组
     *
     * byte数组长度为1时，代表命令接收错误：
     * 	1.异或校验错误
     *  2.结尾错误
     *  3.命令长度错误
     */
    public byte[] resole(InputStream in) throws IOException {
        byte[] resultTemp = new byte[256]; // 临时接收数组
        // 记录当前读取到第几个字节
        mIndex = 0;
        int dataLength = -1; // 命令长度
        int xor = 0; // 异或值
        int valid = 0; // 有效标识
        int i = -1;
        while((i = in.read()) != -1) {
            if(i != 42 && mIndex == 0) continue; // 校验命令的开始，i == 42表示开始， index != 0表示已经开始
            mIndex++;
            if(mIndex == dataLength - 1)  // 校验异或位是否正确
                if(xor != i)	valid = 1; // 异或校验错误

            xor ^= i; // 并计算异或值
            //resultCommand.add(i); // 并保存当前字节到集合
            resultTemp[mIndex -1] = (byte) i;

            if(mIndex == 2) // 第2个字节时，记录当前返回命令的长度
                dataLength = i;

            if(mIndex == dataLength) { // 当前长度等于命令长度时结束。并校验结束位是否为23,不等于23则标记返回命令异常
                if(i != 35)	valid = 2; // 不是以23结尾
                break;
            }
        }
        if(mIndex != dataLength) valid = 3; // 接收长度错误

        byte[] result;
        if(valid == 0) { // 成功返回命令
            result = new byte[mIndex];
            System.arraycopy(resultTemp, 0, result, 0, mIndex);
        } else { // 失败返回错误码
            result = new byte[1];
            result[0] = (byte) valid;
        }
        return result;
    }
}
