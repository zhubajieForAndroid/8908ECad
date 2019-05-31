package com.E8908.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.E8908.R;
import com.squareup.picasso.Picasso;

import java.io.InputStream;


/**
 * Created by dell on 2017/5/3.
 */

public class BItmapUtil {
    //压缩2/1bitmap图片
    public static Bitmap compressBItmap(Context context,int resId){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = null;
        //.inPurgeable = true表示使用bitmapFactory创建bitmap对象，用于存储Pixel的内存空间可以被回收，如果已经被回收掉了，
        // 应用再次访问pixel时系统会调用
        //bitmapFactory的decoder方法从新创建bitmap的Pixel数据,为了能够重新解码图像,bitmap要能够访问存储Bitmap的原始数据.
        options.inPurgeable = true;
        //如果上面设置的为false该属性可以忽略
        options.inInputShareable = true;
        options.inSampleSize = 2;
        InputStream stream = context.getResources().openRawResource(resId);
        //在设置图片的时候尽量不要使用setImageBitmap或setImageResource 　　或BitmapFactory.decodeResource来设置一张大图
        //因为这些函数在完成decode后，最终都是通过java层的createBitmap来完成的，需要消耗更多内存. 因此，改用先通过BitmapFactory.decodeStream方法
        //创建出一个bitmap，再将其设为ImageView的 source，decodeStream最大的秘密在于其直接调用 JNI >> nativeDecodeAsset（） 来完成decode， 　　
        // 无需再使用java层的createBitmap，从而节省了java层的空间. 如果在读取时加上图片的Config参数，可以更有效减少加载的内存
        Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
        return bitmap;
    }
    public static void loadImageForIv(String url, ImageView beforeKtlxIv, String score, TextView tv, boolean isBfore, Context context) {
        if (!TextUtils.isEmpty(url)) {
            Picasso.with(context).load(url).error(R.mipmap.load_failure).resize(200,200).centerCrop().into(beforeKtlxIv);
        } else {
            beforeKtlxIv.setImageResource(R.mipmap.no_pictures);
        }
        if (!TextUtils.isEmpty(score)) {
            float scoreInt = Float.parseFloat(score);
            if (isBfore) {
                tv.setText("施工前:" + ((int) scoreInt) + "分");
            } else {
                tv.setText("施工后:" + ((int) scoreInt) + "分");
            }
        }
    }
    public static void loadImageForIv(String url, ImageView beforeKtlxIv, String score, SeekBar seekBar,Context context) {
        if (!TextUtils.isEmpty(url)) {
            Picasso.with(context).load(url).error(R.mipmap.load_failure).resize(200,200).centerCrop().into(beforeKtlxIv);
        } else {
            beforeKtlxIv.setImageResource(R.mipmap.addphotos);
        }
        if (!TextUtils.isEmpty(score)) {
            float scoreInt = Float.parseFloat(score);
            seekBar.setProgress((int) scoreInt);
        }else {
            seekBar.setProgress(100);
        }
    }


}
