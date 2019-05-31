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

public class SquareEditextContainer extends RelativeLayout {
    private static final String TAG = "SquareEditextContainer";

    public EditText mBelowEt;
    private ImageView mImOne;
    private ImageView mImTwo;

    public SquareEditextContainer(Context context) {
        super(context);
    }

    public SquareEditextContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.view_editext_custom, this);
        mImOne = (ImageView) findViewById(R.id.text_one);
        mImTwo = (ImageView) findViewById(R.id.text_two);
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
                    if (s.length() == 1) {
                        BitmapUtil.numberToBItmapOneRed(Integer.parseInt(s.toString()), mImOne);
                        BitmapUtil.numberToBItmapOne(0, mImTwo);
                    }
                    if (s.length() == 2) {
                        BitmapUtil.numberToBItmapTwoRed(Integer.parseInt(s.toString()), mImOne, mImTwo);
                    }
                }else {
                    BitmapUtil.numberToBItmapTwo(0, mImOne, mImTwo);
                }
            }

            //该方法是在文本改变结束后调用
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

}
