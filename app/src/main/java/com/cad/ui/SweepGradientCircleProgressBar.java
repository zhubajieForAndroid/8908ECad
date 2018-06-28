package com.cad.ui;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.cad.util.ColorTools;


/**
 * 圆形颜色渐变的进度条
 *
 * @author by lenovo on 2018/3/16.
 */
public class SweepGradientCircleProgressBar extends View {
    private static final String TAG = "SweepGradientCircleProgressBar";
    private Paint pathPaint; //绘制大圆
    private Paint pathSmallPaint; //绘制小圆
    private Paint mTextPaint;//绘制标题文字画笔
    private Paint fillArcPaint;
    // 设置光源的方向
    private float[] direction = new float[]{1, 1, 1};

    // 设置环境光亮度
    private float light = 0.4f;
    //渐变数组
    private int[] arcColors = new int[]{ColorTools.RED, ColorTools.RED_TRANSLUCENT
    };
    // 选择要应用的反射等级
//	private float specular = 6;

    private float specular = 8;
    private EmbossMaskFilter emboss;
    private RectF oval;
    private BlurMaskFilter mBlur;
    // view重绘的标记
    private boolean reset = false;
    // 向 mask应用一定级别的模糊
    private float blur = 3.5f;
    private int arcradus = 10;
    //初始化进度
    private int progress = 0;
    private float x, y;

    //设置进度最大值
    private int max = 100;
    private String text = "药液箱剩余";
    private String numberText = "2000ml";
    private final int titleLength = text.length();
    private Paint mNumberTextPaint;
    private float mX1;

    public SweepGradientCircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        oval = new RectF();
        emboss = new EmbossMaskFilter(direction, light, specular, blur);
        mBlur = new BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL);
    }

    //初始化画笔操作
    private void initPaint() {
        //初始化画笔操作
        pathPaint = new Paint();
        // 设置是否抗锯齿
        pathPaint.setAntiAlias(true);
        // 帮助消除锯齿
        pathPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        // 设置中空的样式
        pathPaint.setStyle(Paint.Style.FILL);
        pathPaint.setDither(false);
        pathPaint.setStrokeJoin(Paint.Join.ROUND);
        pathPaint.setStrokeWidth(10);


        pathSmallPaint = new Paint();
        // 设置是否抗锯齿
        pathSmallPaint.setAntiAlias(true);
        // 帮助消除锯齿
        pathSmallPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        // 设置中空的样式
        pathSmallPaint.setStyle(Paint.Style.FILL);
        pathSmallPaint.setDither(false);
        pathSmallPaint.setStrokeJoin(Paint.Join.ROUND);
        pathSmallPaint.setStrokeWidth(10);

        fillArcPaint = new Paint();
        // 设置是否抗锯齿
        fillArcPaint.setAntiAlias(true);
        // 帮助消除锯齿
        fillArcPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        // 设置中空的样式
        fillArcPaint.setStyle(Paint.Style.STROKE);
        fillArcPaint.setDither(false);
        fillArcPaint.setStrokeJoin(Paint.Join.ROUND);
        fillArcPaint.setStrokeWidth(10);

        mTextPaint = new Paint();
        mTextPaint.setTextSize(30);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.WHITE);

        mNumberTextPaint = new Paint();
        mNumberTextPaint.setTextSize(30);
        mNumberTextPaint.setAntiAlias(true);
        mNumberTextPaint.setStyle(Paint.Style.FILL);
        mNumberTextPaint.setColor(Color.WHITE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (reset) {
            canvas.drawColor(Color.TRANSPARENT);
            reset = false;
        }
        drawcircle(canvas);
        drawTextView(canvas, x, y);
        drawNumTextView(canvas, mX1, y);


    }

    private void drawcircle(Canvas canvas) {
        int height = getMeasuredWidth();
        int width = getMeasuredWidth();
        //半径 = 宽/2-圆环的宽度
        int radius = width / 2 - arcradus;
        int cx = width / 2;
        int cy = height / 2;
        pathPaint.setStrokeWidth(20);
        pathPaint.setColor(Color.BLACK);
        //绘制大圆
        pathSmallPaint.setColor(Color.rgb(23, 46, 59));

        canvas.drawCircle(width / 2, height / 2, radius + arcradus / 2 - 0.5f, pathPaint);

//		canvas.drawCircle(width / 2, height / 2, 20, pathPaint);
        //绘制小圆

        canvas.drawCircle(width / 2, height / 2, radius - arcradus / 2 - 0.5f, pathSmallPaint);
        pathSmallPaint.setStrokeWidth(10);
        // 环形颜色填充
        SweepGradient sweepGradient = new SweepGradient(width / 2, height / 2, arcColors, null);
        fillArcPaint.setShader(sweepGradient);
        // 设置画笔为白色

        // 模糊效果
        fillArcPaint.setMaskFilter(mBlur);
        // 设置线的类型,边是圆的
        fillArcPaint.setStrokeCap(Paint.Cap.ROUND);

        //设置圆弧的宽度
//		fillArcPaint.setStrokeWidth(arcradus+1);
        fillArcPaint.setStrokeWidth(10);

        // 确定圆弧的绘制位置，也就是里面圆弧坐标和外面圆弧坐标
        oval.set(width / 2 - radius, height / 2 - radius, width / 2 + radius, height / 2 + radius);
        // 画圆弧，第二个参数为：起始角度，第三个为跨的角度，第四个为true的时候是实心，false的时候为空心
        canvas.drawArc(oval, 0, ((float) progress / max) * 360, false, fillArcPaint);
    }

    private void drawTextView(Canvas canvas, float floatX, float floatY) {
        canvas.drawText(text, floatX, floatY, mTextPaint);
    }

    private void drawNumTextView(Canvas canvas, float floatX, float floatY) {
        canvas.drawText(getText(), floatX, floatY+40, mNumberTextPaint);

    }

    @NonNull
    private String getText() {
        return progress + "ml";
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        this.invalidate();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int[] getArcColors() {
        return arcColors;
    }

    public void setArcColors(int[] arcColors) {
        this.arcColors = arcColors;
//		this.invalidate();
    }

    /**
     * 描述：重置进度
     *
     * @throws
     */
    public void reset() {
        reset = true;
        this.progress = 0;
        this.invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //获取测量大小
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //如果为确定大小值，则圆的半径为宽度/2
        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            float v = mTextPaint.measureText(text, 0, titleLength);
            float v1 = mNumberTextPaint.measureText(numberText, 0, numberText.length());
            x = (widthSize / 2) - (v / 2);
            mX1 = (widthSize / 2) - (v1 / 2);
            y = heightSize / 2;
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //设置视图的大小
    }

}
