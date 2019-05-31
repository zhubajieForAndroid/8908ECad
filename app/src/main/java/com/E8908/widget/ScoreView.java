package com.E8908.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

public class ScoreView extends View {
    private static final String TAG = "ScoreView";
    private Object lock = new Object();
    /**
     * 默认刻度间隔角度
     */
    private int TICK_SPLIT_DEFAULT_ANGLE = 5;
    /**
     * 默认刻度角度
     */
    private int TICK_BLOCK_DEFAULT_ANGLE = 2;
    /**
     * 默认刻度长度
     */
    private float NORMAL_TICK_DEFAULT_SIZE = 25;
    /**
     * 分数小于33的颜色
     */
    private int GRADIENT_START_DEFAULT_COLOR = Color.parseColor("#26b54b");
    /**
     * 分数大于33小于66颜色
     */
    private int GRADIENT_CENTER_DEFAULT_COLOR = Color.parseColor("#ff861b");
    /**
     * 分数大于66颜色
     */
    private int GRADIENT_END_DEFAULT_COLOR = Color.parseColor("#ff4141");
    /**
     * 默认的颜色
     */
    private int TICK_NORMAL_DEFAULT_COLOR = Color.WHITE;
    //优：#26b54b  良好：#ff861b   差：#ff4141
    private Paint mPaint;
    /**
     * 刻度数量
     */
    private int mTotalTickCount;
    /**
     * 当前进度
     */
    private int mCurrentProgressPercent;
    /**
     * 直径
     */
    private int mCircleWidth;
    private RectF mRect;
    private String bottomText = "施工后";

    private Thread uiThread;
    private SweepGradient mLg;
    private Paint mTextPaint;
    private Rect mTextRect;
    private Rect mBottomRect;
    private Paint mBottomText;

    public ScoreView(Context context) {
        this(context, null, 0);
    }

    public ScoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(56);
        mTextRect = new Rect();

        mBottomText = new Paint();
        mBottomText.setAntiAlias(true);
        mBottomText.setTextSize(36);
        mBottomText.setColor(Color.WHITE);
        mBottomRect = new Rect();

        mTotalTickCount = (int) (225f / (TICK_SPLIT_DEFAULT_ANGLE + TICK_BLOCK_DEFAULT_ANGLE));//确保刻度数量为整数
        uiThread = Thread.currentThread();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureHanlder(widthMeasureSpec);
        int height = measureHanlder(heightMeasureSpec);
        mCircleWidth = (width < height) ? width : height;
        setMeasuredDimension(mCircleWidth, mCircleWidth);
        float padding = NORMAL_TICK_DEFAULT_SIZE / 2;
        mRect = new RectF(padding, padding, mCircleWidth - padding, mCircleWidth - padding);
    }

    private int measureHanlder(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.max(result, specSize);
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        String text = mCurrentProgressPercent + "分";
        if (mCurrentProgressPercent == 0){
            mPaint.setColor(TICK_NORMAL_DEFAULT_COLOR);
            mTextPaint.setColor(TICK_NORMAL_DEFAULT_COLOR);
        }
        if (mCurrentProgressPercent <= 33 && mCurrentProgressPercent>0) {
            mPaint.setColor(GRADIENT_END_DEFAULT_COLOR);
            mTextPaint.setColor(GRADIENT_END_DEFAULT_COLOR);
        }
        if (mCurrentProgressPercent > 33 && mCurrentProgressPercent <= 66) {
            mPaint.setColor(GRADIENT_CENTER_DEFAULT_COLOR);
            mTextPaint.setColor(GRADIENT_CENTER_DEFAULT_COLOR);
        }
        if (mCurrentProgressPercent > 66) {
            mPaint.setColor(GRADIENT_START_DEFAULT_COLOR);
            mTextPaint.setColor(GRADIENT_START_DEFAULT_COLOR);
        }
        final int currentBlockIndex = (int) (mCurrentProgressPercent / 100f * mTotalTickCount);
        for (int i = 0; i < mTotalTickCount; i++) {
            if (i == currentBlockIndex - 1) {
                //当前刻度，刻度长度较长
                mPaint.setStrokeWidth(NORMAL_TICK_DEFAULT_SIZE);
                mPaint.setShader(mLg);
            } else if (i < currentBlockIndex) {
                //已选中的刻度
                mPaint.setStrokeWidth(NORMAL_TICK_DEFAULT_SIZE);
                mPaint.setShader(mLg);
            } else {
                //未选中的刻度
                mPaint.setStrokeWidth(NORMAL_TICK_DEFAULT_SIZE);
                mPaint.setColor(TICK_NORMAL_DEFAULT_COLOR);
            }
            canvas.drawArc(mRect, i * (TICK_BLOCK_DEFAULT_ANGLE + TICK_SPLIT_DEFAULT_ANGLE) + 160, TICK_BLOCK_DEFAULT_ANGLE, false, mPaint);
        }
        mTextPaint.getTextBounds(text, 0, text.length(), mTextRect);
        canvas.drawText(text, getMeasuredWidth() / 2 - mTextRect.width() / 2, getMeasuredHeight() / 2 - mTextRect.height() / 2 + NORMAL_TICK_DEFAULT_SIZE, mTextPaint);
        mBottomText.getTextBounds(bottomText, 0, bottomText.length(), mBottomRect);
        canvas.drawText(bottomText, getMeasuredWidth() / 2 - mBottomRect.width() / 2, getMeasuredHeight() / 2 + NORMAL_TICK_DEFAULT_SIZE + mBottomRect.height(), mBottomText);
    }

    /**
     * 设置进度
     *
     * @param percent 百分百
     */
    public void setProgress(int percent) {

        mCurrentProgressPercent = percent;
        synchronized (lock) {
            if (Thread.currentThread() != uiThread) {
                postInvalidate();
            } else {
                invalidate();
            }
        }

    }
}
