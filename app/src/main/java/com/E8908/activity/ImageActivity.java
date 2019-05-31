package com.E8908.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.E8908.widget.AVLoadingIndicatorView;
import com.E8908.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ImageActivity extends Activity {

    @Bind(R.id.imageView)
    ImageView mImageView;
    @Bind(R.id.image_activity)
    RelativeLayout mImageActivity;
    @Bind(R.id.progress_bar)
    AVLoadingIndicatorView mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);
        initData();

    }

    private void initData() {
        String url = getIntent().getStringExtra("url");
        if (!TextUtils.isEmpty(url))
            Picasso.with(this).load(url).into(mTarget);
        mImageActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private Target mTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            //加载成功回调
            mImageView.setImageBitmap(bitmap);
            mProgressBar.setVisibility(View.GONE);
            mProgressBar.hide();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            //加载失败时回调
            mImageView.setImageResource(R.mipmap.load_failure);
            mProgressBar.setVisibility(View.GONE);
            mProgressBar.hide();
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            //开始请求加载时回调
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.show();
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        hintWindow();
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            hintWindow();
        }
    }
    private void hintWindow() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
