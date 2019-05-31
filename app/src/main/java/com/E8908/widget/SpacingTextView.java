package com.E8908.widget;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by dell on 2017/7/19.
 */

public class SpacingTextView extends TextView {
    private float spacing = 0;
    private CharSequence originalText = "";
    public SpacingTextView(Context context) {
        super(context);
    }

    public SpacingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpacingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public float getSpacing(){
        return spacing;
    }
    public void setSpacing(float spac){
        spacing = spac;
    }
    public void setText(CharSequence text, BufferType type){
        originalText = text;
        applySpacing();
    }
    public CharSequence getText(){
        return originalText;
    }
    private void applySpacing() {
        if (this == null || this.originalText == null) return;
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < originalText.length(); i++) {
            builder.append(originalText.charAt(i));
            if(i+1 < originalText.length()) {
                builder.append("\u00A0");
            }
        }
        SpannableString finalText = new SpannableString(builder.toString());
        if(builder.toString().length() > 1) {
            for(int i = 1; i < builder.toString().length(); i+=2) {
                finalText.setSpan(new ScaleXSpan((spacing+1)/10), i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        super.setText(finalText, BufferType.SPANNABLE);
    }
}
