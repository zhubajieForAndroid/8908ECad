package com.E8908.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.E8908.R;

/**
 * Created by dell on 2018/3/19.
 */

public class SquareEditextTwoContainer extends RelativeLayout {
    private static final String TAG = "SquareEditextContainer";

    public EditText mBelowEt;
    public ImageView mImOne,mImTwo,mImThree,mImFour,mImFive,mImSix,mImSeven,mImEight;

    public SquareEditextTwoContainer(Context context) {
        super(context);
    }

    public SquareEditextTwoContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.view_editext_custom_id, this);
        mImOne = (ImageView) findViewById(R.id.text_one);
        mImTwo = (ImageView) findViewById(R.id.text_two);
        mImThree = (ImageView) findViewById(R.id.text_three);
        mImFour = (ImageView) findViewById(R.id.text_four);
        mImFive = (ImageView) findViewById(R.id.text_five);
        mImSix = (ImageView) findViewById(R.id.text_six);
        mImSeven = (ImageView) findViewById(R.id.text_seven);
        mImEight = (ImageView) findViewById(R.id.text_eight);
        mBelowEt = (EditText) findViewById(R.id.below_et);
        mBelowEt.setCursorVisible(false);//隐藏光标
        setLinstener();
    }

    private void setLinstener() {
        mBelowEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            //该方法是在当文本改变时被调用
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    int i = Integer.parseInt(s.toString());
                    if (s.length() == 1) {
                        BitmapUtil.numberToBItmapOneRed(i, mImOne);
                        BitmapUtil.numberToBItmapSeven(0,mImEight,mImSeven,mImSix,mImFive,mImFour,mImThree,mImTwo);
                    }
                    if (s.length() == 2) {
                        BitmapUtil.numberToBItmapTwoRed(i,mImOne, mImTwo);
                        BitmapUtil.numberToBItmapSix(0,mImEight,mImSeven,mImSix,mImFive,mImFour,mImThree);
                    }
                    if (s.length() == 3){
                        BitmapUtil.numberToBItmapThreeRed(i,mImThree,mImTwo,mImOne);
                        BitmapUtil.numberToBItmapFive(0,mImEight,mImSeven,mImSix,mImFive,mImFour);
                    }
                    if (s.length() == 4){
                        BitmapUtil.numberToBItmapFourRed(i,mImFour,mImThree,mImTwo,mImOne);
                        BitmapUtil.numberToBItmapFour(0,mImEight,mImSeven,mImSix,mImFive);
                    }
                    if (s.length() == 5){
                        BitmapUtil.numberToBItmapFiveRed(i,mImFive,mImFour,mImThree,mImTwo,mImOne);
                        BitmapUtil.numberToBItmapThree(0,mImEight,mImSeven,mImSix);
                    }
                    if (s.length() == 6){
                        BitmapUtil.numberToBItmapSixRed(i,mImSix,mImFive,mImFour,mImThree,mImTwo,mImOne);
                        BitmapUtil.numberToBItmapTwo(0,mImEight, mImSeven);
                    }
                    if (s.length() == 7){
                        BitmapUtil.numberToBItmapSevenRed(i,mImSeven,mImSix,mImFive,mImFour,mImThree,mImTwo,mImOne);
                        BitmapUtil.numberToBItmapOne(0, mImEight);
                    }
                    if (s.length() == 8){
                        BitmapUtil.numberToBItmapEightRed(i,mImEight,mImSeven,mImSix,mImFive,mImFour,mImThree,mImTwo,mImOne);
                    }
                }else {
                    BitmapUtil.numberToBItmapEight(0,mImEight,mImSeven,mImSix,mImFive,mImFour,mImThree,mImTwo,mImOne);
                }
            }

            //该方法是在文本改变结束后调用
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

}
