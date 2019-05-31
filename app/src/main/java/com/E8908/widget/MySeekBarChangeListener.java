package com.E8908.widget;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.base.MyApplication;

public class MySeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
    private TextView mTextView;
    private boolean mIsMove;            //textView是否跟随数值移动

    public MySeekBarChangeListener(TextView textView, boolean ismove) {
        mTextView = textView;
        mIsMove = ismove;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mIsMove) {
            LinearLayout.LayoutParams paramsStrength = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsStrength.leftMargin = (int) (progress * 1.8);
            mTextView.setLayoutParams(paramsStrength);
            mTextView.setText(progress + "");
            LayerDrawable layerDrawable = (LayerDrawable) seekBar.getProgressDrawable();
            Drawable dra = layerDrawable.getDrawable(1);
            if (progress < 25){
                dra.setColorFilter(MyApplication.getContext().getResources().getColor(R.color.red), PorterDuff.Mode.SRC);
                //seekBar.getThumb().setColorFilter(MyApplication.getContext().getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                mTextView.setBackground(MyApplication.getContext().getResources().getDrawable(R.mipmap.red));
            }
            if (progress >= 25 && progress< 50){
                dra.setColorFilter(MyApplication.getContext().getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC);
                //seekBar.getThumb().setColorFilter(MyApplication.getContext().getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
                mTextView.setBackground(MyApplication.getContext().getResources().getDrawable(R.mipmap.orange));
            }
            if (progress >= 50 && progress < 75){
                dra.setColorFilter(MyApplication.getContext().getResources().getColor(R.color.blue), PorterDuff.Mode.SRC);
                //seekBar.getThumb().setColorFilter(MyApplication.getContext().getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
                mTextView.setBackground(MyApplication.getContext().getResources().getDrawable(R.mipmap.blue));
            }
            if (progress >= 75){
                dra.setColorFilter(MyApplication.getContext().getResources().getColor(R.color.green), PorterDuff.Mode.SRC);
                //seekBar.getThumb().setColorFilter(MyApplication.getContext().getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);
                mTextView.setBackground(MyApplication.getContext().getResources().getDrawable(R.mipmap.green));
            }
        } else {
            LayerDrawable layerDrawable = (LayerDrawable) seekBar.getProgressDrawable();
            Drawable dra = layerDrawable.getDrawable(1);
            if (progress < 25){
                dra.setColorFilter(MyApplication.getContext().getResources().getColor(R.color.red), PorterDuff.Mode.SRC);
                //seekBar.getThumb().setColorFilter(MyApplication.getContext().getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            }
            if (progress >= 25 && progress< 50){
                dra.setColorFilter(MyApplication.getContext().getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC);
                //seekBar.getThumb().setColorFilter(MyApplication.getContext().getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
            }
            if (progress >= 50 && progress < 75){
                dra.setColorFilter(MyApplication.getContext().getResources().getColor(R.color.blue), PorterDuff.Mode.SRC);
                //seekBar.getThumb().setColorFilter(MyApplication.getContext().getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
            }
            if (progress >= 75){
                dra.setColorFilter(MyApplication.getContext().getResources().getColor(R.color.green), PorterDuff.Mode.SRC);
                //seekBar.getThumb().setColorFilter(MyApplication.getContext().getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);
            }
            mTextView.setText(progress + "分");
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}
