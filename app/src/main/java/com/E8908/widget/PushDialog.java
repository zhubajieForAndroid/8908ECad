package com.E8908.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.E8908.R;
import com.E8908.util.NavigationBarUtil;
import com.squareup.picasso.Picasso;

/**
 * Created by dell on 2018/8/9.
 */

public class PushDialog extends Dialog {
    private Context mContext;
    private Window mWindow;

    public PushDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    public void setImageUrl(String imageUrl) {
        setContentView(R.layout.view_push_dialog);
        ImageView iv = findViewById(R.id.push_iv);
        mWindow = getWindow();
        WindowManager.LayoutParams attributes = mWindow.getAttributes();
        attributes.gravity = Gravity.CENTER;
        attributes.width = 800;
        attributes.height = 294;
        mWindow.setAttributes(attributes);
        Picasso.with(mContext).load(imageUrl).into(iv);
    }

}
