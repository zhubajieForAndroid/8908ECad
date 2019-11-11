package com.E8908.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CpVideoFileThread extends Thread {
    private File mFile;
    private InputStream mInputStream;
    public CpVideoFileThread(File file, InputStream inputStream){
        mFile = file;
        mInputStream = inputStream;
    }
    @Override
    public void run() {
        try {
            OutputStream myOutput = new FileOutputStream(mFile);
            byte[] buffer = new byte[1024];
            int length = mInputStream.read(buffer);
            while (length > 0) {
                myOutput.write(buffer, 0, length);
                length = mInputStream.read(buffer);
            }
            myOutput.flush();
            mInputStream.close();
            myOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
