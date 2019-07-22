package com.E8908.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.widget.ToastUtil;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.download.DownloadTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UpgradeActivity extends Activity implements View.OnClickListener {

    @Bind(R.id.tv_upgrade_feature)
    TextView mTvUpgradeFeature;                 //更新的内容
    @Bind(R.id.tv_upgrade_cancel)
    TextView mTvUpgradeCancel;                  //取消更新
    @Bind(R.id.tv_upgrade_confirm)
    TextView mTvUpgradeConfirm;                 //立即更新

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_upgrade);
        ButterKnife.bind(this);
        initData();
        initListener();
    }

    private void initData() {
        String info = getIntent().getStringExtra("info");
        if (!TextUtils.isEmpty(info))
            mTvUpgradeFeature.setText(info);
    }

    private void initListener() {
        mTvUpgradeCancel.setOnClickListener(this);
        mTvUpgradeConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_upgrade_cancel:                        //取消更新
                Beta.cancelDownload();
                finish();
                break;
            case R.id.tv_upgrade_confirm:                       //立即更新
                DownloadTask task = Beta.startDownload();
                if (task.getStatus() == DownloadTask.DOWNLOADING) {
                    ToastUtil.showMessage("后台下载中...");
                    finish();
                }
                break;
        }
    }
}
