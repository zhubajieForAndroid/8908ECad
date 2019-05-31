package com.E8908.util;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.E8908.R;

public class KeyboardNumberUtil {
    private EditText mEditText;
    private KeyboardView mKeyboardView;
    private boolean mIsNumber = false;
    private boolean switchBtn = true;
    /**
     * 数字与大写字母键盘
     */
    private Keyboard numberKeyboard;

    public KeyboardNumberUtil(KeyboardView view, Context context, EditText editText) {
        mEditText = editText;
        numberKeyboard = new Keyboard(context, R.xml.number);
        mKeyboardView = view;
        mKeyboardView.setKeyboard(numberKeyboard);
        mKeyboardView.setEnabled(true);
        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.setOnKeyboardActionListener(listener);
    }

    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = mEditText.getText();
            int start = mEditText.getSelectionStart();
            if (!mIsNumber) {
                //判定是否是中文的正则表达式 [\\u4e00-\\u9fa5]判断一个中文 [\\u4e00-\\u9fa5]+多个中文
                String reg = "[\\u4e00-\\u9fa5]";
                if (primaryCode == -1) {// 省份简称与数字键盘切换
                    //if (mEditText.getText().toString().matches(reg)) {
                        //changeKeyboard(true);
                   // }
                    if (switchBtn){
                        switchBtn = false;
                        changeKeyboard(true);
                    }else {
                        switchBtn = true;
                        changeKeyboard(false);
                    }
                } else if (primaryCode == -3) {
                    if (editable != null && editable.length() > 0) {
                        //没有输入内容时软键盘重置为省份简称软键盘
                        if (editable.length() == 1) {
                            switchBtn = true;
                            changeKeyboard(false);
                        }
                        if (start > 0) {
                            editable.delete(start - 1, start);
                        }
                    }
                } else {
                    editable.insert(start, Character.toString((char) primaryCode));
                    // 判断第一个字符是否是中文,是，则自动切换到数字软键盘
                    if (mEditText.getText().toString().matches(reg)) {
                        switchBtn = false;
                        changeKeyboard(true);
                    }
                }
            } else {
                if (primaryCode == -3) {
                    if (editable != null && editable.length() > 0) {
                        if (start > 0) {
                            editable.delete(start - 1, start);
                        }
                    }
                }else {
                    editable.insert(start, Character.toString((char) primaryCode));
                }
            }
        }
    };


        /**
         * 指定切换软键盘 isNumber false表示要切换为省份简称软键盘 true表示要切换为数字软键盘
         */
        private void changeKeyboard(boolean isNumber) {
            if (isNumber) {
                mKeyboardView.setKeyboard(numberKeyboard);
            }
        }

        /**
         * 指定切换软键盘 isNumber false表示要切换为省份简称软键盘 true表示要切换为数字软键盘
         */
        public void changeKeyboardNumber(boolean isNumber) {
            mIsNumber = isNumber;
            if (isNumber) {
                mKeyboardView.setKeyboard(numberKeyboard);
            }
        }

        /**
         * 软键盘展示状态
         */
        public boolean isShow() {
            return mKeyboardView.getVisibility() == View.VISIBLE;
        }

        /**
         * 软键盘展示
         */
        public void showKeyboard() {
            int visibility = mKeyboardView.getVisibility();
            if (visibility == View.GONE || visibility == View.INVISIBLE) {
                mKeyboardView.setVisibility(View.VISIBLE);
            }
        }

        /**
         * 软键盘隐藏
         */
        public void hideKeyboard() {
            int visibility = mKeyboardView.getVisibility();
            if (visibility == View.VISIBLE) {
                mKeyboardView.setVisibility(View.INVISIBLE);
            }
        }

    }
