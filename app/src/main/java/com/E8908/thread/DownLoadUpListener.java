package com.E8908.thread;

import java.io.File;

public interface DownLoadUpListener {
    void onSuccess(File apk);
    void onFail();
    void onProgress(long currentProgress, int tot);
}
