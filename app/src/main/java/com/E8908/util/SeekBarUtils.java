package com.E8908.util;

import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.E8908.widget.MySeekBarChangeListener;

public class SeekBarUtils {
    //TextView跟随SeekBar移动
    public static void initSeekBarProgress(SeekBar seekBar, TextView textView, int number) {
        seekBar.setProgress(number);
        seekBar.setOnSeekBarChangeListener(new MySeekBarChangeListener(textView,true));
        LinearLayout.LayoutParams paramsStrength = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsStrength.leftMargin = (int) (number*1.8);
        textView.setLayoutParams(paramsStrength);
        textView.setText(number+"");

    }
    public static void setTextByProgress(SeekBar seekBar, TextView textView, int number){
        seekBar.setProgress(number);
        seekBar.setOnSeekBarChangeListener(new MySeekBarChangeListener(textView,false));
        textView.setText(number+"分");
    }

}
