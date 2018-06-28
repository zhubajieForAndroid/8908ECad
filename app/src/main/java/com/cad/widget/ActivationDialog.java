package com.cad.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.cad.R;
import com.cad.conf.Constants;
import com.squareup.picasso.Picasso;

/**
 * Created by dell on 2018/4/2.
 */

public class ActivationDialog extends Dialog {

    private Context mContext;

    public ActivationDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    public void setID(String id){
        setContentView(R.layout.view_activation);
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = 1195;
        lp.height = 570;
        window.setAttributes(lp);
        setCanceledOnTouchOutside(false);
        TextView tv = (TextView) findViewById(R.id.equipment_id);
        if (!TextUtils.isEmpty(id))
            tv.setText(id);
        ImageView iv = (ImageView) findViewById(R.id.load_image);
        Picasso.with(getContext()).load("https://www.haoanda.cn/activateQr/"+ Constants.URLS.ID+"/qr.jpg").error(R.mipmap.bg_001).into(iv);
        ImageView info = (ImageView) findViewById(R.id.info);
        Picasso.with(getContext()).load("https://www.haoanda.cn/activateQr/"+ Constants.URLS.ID+"/info.jpg").error(R.mipmap.jh_text).into(info);
    }
}
