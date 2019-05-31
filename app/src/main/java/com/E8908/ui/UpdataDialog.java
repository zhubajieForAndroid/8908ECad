package com.E8908.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.E8908.R;
/**
 * Created by dell on 2017/6/4.
 */

public class UpdataDialog extends Dialog {
    private String updataContent;
    private Context mContext;
    private OnDownloadButtonListener mOnDownloadButtonListener;
    public UpdataDialog(Context context, int themeResId, String text) {
        super(context, themeResId);
        updataContent = text;
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_updata_dialog);

        LinearLayout container = (LinearLayout) findViewById(R.id.container);

        Button cancle = (Button) findViewById(R.id.cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消下载
                mOnDownloadButtonListener.isDownload(false);
                dismiss();
            }
        });
        Button download = (Button) findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //下载
                mOnDownloadButtonListener.isDownload(true);
                dismiss();
            }
        });


        String[] split = updataContent.split(",");
        for (int i = 0; i < split.length; i++) {
            TextView tv = new TextView(mContext);
            tv.setTextColor(Color.parseColor("#464646"));
            tv.setTextSize(25);
            tv.setText(split[i]);
            container.addView(tv);
        }
    }

    public interface OnDownloadButtonListener{
        void isDownload(boolean b);
    }
    public void setDownLaodButtonListener(OnDownloadButtonListener o){
        mOnDownloadButtonListener = o;
    }

}
