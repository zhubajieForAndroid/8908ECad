package com.E8908.textFunction;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.E8908.R;

import java.math.BigDecimal;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.support.constraint.Constraints.TAG;

public class SettingsDialog extends Dialog implements View.OnClickListener {

    @Bind(R.id.add_time)
    TextView mAddTime;
    @Bind(R.id.add_count)
    TextView mAddCount;
    @Bind(R.id.add_time_reduce)
    ImageView mAddTimeReduce;
    @Bind(R.id.add_time_add)
    ImageView mAddTimeAdd;
    @Bind(R.id.add_count_reduce)
    ImageView mAddCountReduce;
    @Bind(R.id.add_count_add)
    ImageView mAddCountAdd;

    @Bind(R.id.back_time)
    TextView mBackTime;
    @Bind(R.id.back_count)
    TextView mBackCount;
    @Bind(R.id.back_time_reduce)
    ImageView mBackTimeReduce;
    @Bind(R.id.back_time_add)
    ImageView mBackTimeAdd;
    @Bind(R.id.back_count_reduce)
    ImageView mBackCountReduce;
    @Bind(R.id.back_count_add)
    ImageView mBackCountAdd;
    @Bind(R.id.wuhua_time)
    TextView mWuhuaTime;
    @Bind(R.id.wuhua_time_reduce)
    ImageView mWuhuaTimeReduce;
    @Bind(R.id.wuhua_time_add)
    ImageView mWuhuaTimeAdd;
    @Bind(R.id.shajun_time)
    TextView mShajunTime;
    @Bind(R.id.shajun_time_reduce)
    ImageView mShajunTimeReduce;
    @Bind(R.id.shajun_time_add)
    ImageView mShajunTimeAdd;
    @Bind(R.id.jinghua_time)
    TextView mJinghuaTime;
    @Bind(R.id.jinghua_time_reduce)
    ImageView mJinghuaTimeReduce;
    @Bind(R.id.jinghua_time_add)
    ImageView mJinghuaTimeAdd;
    @Bind(R.id.wuhua_a)
    TextView mWuhuaA;
    @Bind(R.id.wuhua_a_reduce)
    ImageView mWuhuaAReduce;
    @Bind(R.id.wuhua_a_add)
    ImageView mWuhuaAAdd;
    @Bind(R.id.shajun_a)
    TextView mShajunA;
    @Bind(R.id.shajun_a_reduce)
    ImageView mShajunAReduce;
    @Bind(R.id.shajun_a_add)
    ImageView mShajunAAdd;
    @Bind(R.id.jinghua_a)
    TextView mJinghuaA;
    @Bind(R.id.jinghua_a_reduce)
    ImageView mJinghuaAReduce;
    @Bind(R.id.jinghua_a_add)
    ImageView mJinghuaAAdd;
    private int mAddTimeInt;
    private int mAddCountInt;
    private int mBackTimeInt;
    private int mBackCountInt;
    private int mWuhuaTimeInt;
    private int mShaJunTimeInt;
    private int mJingHuaTimeInt;
    private float mWuhuaAF;
    private float mShajunAF;
    private float mJinghuaAF;

    private OnDataChangeListener mOnDataChangeListener;

    public SettingsDialog(Context context, int themeResId, int time, int count, int backTime, int backCount,
                          int wuhuaTime, int shajun, int jingHua, float wuhuaA, float shajunA, float jinghuaA) {
        super(context, themeResId);
        mAddCountInt = count;
        mAddTimeInt = time;
        mBackTimeInt = backTime;
        mBackCountInt = backCount;
        mWuhuaTimeInt = wuhuaTime;
        mShaJunTimeInt = shajun;
        mJingHuaTimeInt = jingHua;
        mWuhuaAF = wuhuaA*10;
        mShajunAF = shajunA*10;
        mJinghuaAF = jinghuaA*10;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_view_settings);
        ButterKnife.bind(this);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = 600;
            attributes.height = 400;
            window.setAttributes(attributes);
        }
        mAddTimeReduce.setOnClickListener(this);
        mAddTimeAdd.setOnClickListener(this);
        mAddCountReduce.setOnClickListener(this);
        mAddCountAdd.setOnClickListener(this);

        mBackTimeReduce.setOnClickListener(this);
        mBackTimeAdd.setOnClickListener(this);
        mBackCountReduce.setOnClickListener(this);
        mBackCountAdd.setOnClickListener(this);

        mWuhuaTimeReduce.setOnClickListener(this);
        mWuhuaTimeAdd.setOnClickListener(this);
        mShajunTimeReduce.setOnClickListener(this);
        mShajunTimeAdd.setOnClickListener(this);
        mJinghuaTimeReduce.setOnClickListener(this);
        mJinghuaTimeAdd.setOnClickListener(this);
        mWuhuaAReduce.setOnClickListener(this);
        mWuhuaAAdd.setOnClickListener(this);
        mShajunAReduce.setOnClickListener(this);
        mShajunAAdd.setOnClickListener(this);
        mJinghuaAReduce.setOnClickListener(this);
        mJinghuaAAdd.setOnClickListener(this);

        mAddTime.setText(mAddTimeInt + "");
        mAddCount.setText(mAddCountInt + "");
        mBackTime.setText(mBackTimeInt + "");
        mBackCount.setText(mBackCountInt + "");
        mWuhuaTime.setText(mWuhuaTimeInt + "");
        mShajunTime.setText(mShaJunTimeInt + "");
        mJinghuaTime.setText(mJingHuaTimeInt + "");
        mWuhuaA.setText(mWuhuaAF/10 + "");
        mShajunA.setText(mShajunAF/10 + "");
        mJinghuaA.setText(mJinghuaAF/10 + "");
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mOnDataChangeListener.onChange(mAddTimeInt, mAddCountInt, mBackTimeInt, mBackCountInt,
                mWuhuaTimeInt, mShaJunTimeInt, mJingHuaTimeInt,mWuhuaAF/10,mShajunAF/10,mJinghuaAF/10);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_time_reduce:          //加注时间减
                if (mAddTimeInt > 5) {
                    mAddTimeInt--;
                    mAddTime.setText(mAddTimeInt + "");
                }
                break;
            case R.id.add_time_add:             //加注时间加
                if (mAddTimeInt < 120) {
                    mAddTimeInt++;
                    mAddTime.setText(mAddTimeInt + "");
                }
                break;
            case R.id.add_count_reduce:         //加注升数减
                if (mAddCountInt > 100) {
                    mAddCountInt -= 10;
                    mAddCount.setText(mAddCountInt + "");
                }
                break;
            case R.id.add_count_add:            //加注升数加
                if (mAddCountInt < 2000) {
                    mAddCountInt += 10;
                    mAddCount.setText(mAddCountInt + "");
                }
                break;
            case R.id.back_time_reduce:          //回收时间减
                if (mBackTimeInt > 5) {
                    mBackTimeInt--;
                    mBackTime.setText(mBackTimeInt + "");
                }
                break;
            case R.id.back_time_add:             //回收时间加
                if (mBackTimeInt < 120) {
                    mBackTimeInt++;
                    mBackTime.setText(mBackTimeInt + "");
                }
                break;
            case R.id.back_count_reduce:         //回收升数减
                if (mBackCountInt > 100) {
                    mBackCountInt -= 10;
                    mBackCount.setText(mBackCountInt + "");
                }
                break;
            case R.id.back_count_add:            //回收升数加
                if (mBackCountInt < 2000) {
                    mBackCountInt += 10;
                    mBackCount.setText(mBackCountInt + "");
                }
                break;
            case R.id.wuhua_time_reduce:         //雾化时间减
                if (mWuhuaTimeInt > 5) {
                    mWuhuaTimeInt--;
                    mWuhuaTime.setText(mWuhuaTimeInt + "");
                }
                break;
            case R.id.wuhua_time_add:            //雾化时间加
                if (mWuhuaTimeInt < 120) {
                    mWuhuaTimeInt++;
                    mWuhuaTime.setText(mWuhuaTimeInt + "");
                }
                break;
            case R.id.shajun_time_reduce:         //杀菌时间减
                if (mShaJunTimeInt > 5) {
                    mShaJunTimeInt--;
                    mShajunTime.setText(mShaJunTimeInt + "");
                }
                break;
            case R.id.shajun_time_add:            //杀菌时间加
                if (mShaJunTimeInt < 120) {
                    mShaJunTimeInt++;
                    mShajunTime.setText(mShaJunTimeInt + "");
                }
                break;
            case R.id.jinghua_time_reduce:         //净化时间减
                if (mJingHuaTimeInt > 5) {
                    mJingHuaTimeInt--;
                    mJinghuaTime.setText(mJingHuaTimeInt + "");
                }
                break;
            case R.id.jinghua_time_add:            //净化时间加
                if (mJingHuaTimeInt < 120) {
                    mJingHuaTimeInt++;
                    mJinghuaTime.setText(mJingHuaTimeInt + "");
                }
                break;
            case R.id.wuhua_a_add:                  //雾化电流加
                if (mWuhuaAF < 25){
                    mWuhuaAF++;
                    mWuhuaA.setText(mWuhuaAF/10 + "");
                }
                break;
            case R.id.wuhua_a_reduce:               //雾化电流减
                if (mWuhuaAF > 21){
                    mWuhuaAF--;
                    mWuhuaA.setText(mWuhuaAF/10 + "");
                }
                break;
            case R.id.shajun_a_add:                 //杀菌电流加
                if (mShajunAF < 10){
                    mShajunAF++;
                    mShajunA.setText(mShajunAF/10 + "");
                }
                break;
            case R.id.shajun_a_reduce:              //杀菌电流减
                if (mShajunAF > 8){
                    mShajunAF--;
                    mShajunA.setText(mShajunAF/10 + "");
                }
                break;
            case R.id.jinghua_a_add:                 //净化电流加
                if (mJinghuaAF < 4){
                    mJinghuaAF++;
                    mJinghuaA.setText(mJinghuaAF/10 + "");
                }
                break;
            case R.id.jinghua_a_reduce:              //净化电流减
                if (mJinghuaAF > 2){
                    mJinghuaAF--;
                    mJinghuaA.setText(mJinghuaAF/10 + "");
                }
                break;
        }
    }

    public interface OnDataChangeListener {
        /**
         * @param addTime   加注时间
         * @param addCount  加注量
         * @param backTime  回收时间
         * @param backCount 回收量
         */
        void onChange(int addTime, int addCount, int backTime, int backCount,
                      int wuhuaTime, int shajunTime, int jingHuaTime,float wuhuaA,float shajunA,float jinghuaA);
    }

    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener) {
        mOnDataChangeListener = onDataChangeListener;
    }
}
