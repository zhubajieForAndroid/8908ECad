package com.E8908.widget;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class MySeekbar extends AppCompatSeekBar {
    public MySeekbar(Context context) {
        this(context,null);
    }

    public MySeekbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (Build.VERSION.SDK_INT >= 21)
            setSplitTrack(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onTouchEvent(event);
    }

}
