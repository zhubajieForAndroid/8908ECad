package com.E8908.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.E8908.widget.GasView;
import com.E8908.widget.MaintainView;

import static android.support.constraint.Constraints.TAG;

public class MaintainAdapter extends PagerAdapter implements View.OnClickListener {
    private Context mContext;
    private byte[] array;
    private byte[] mBuffer;
    private boolean misRoutine;
    private String[] title;
    private OnGasClickListener mOnGasClickListener;
    private int mTimeCount;

    public MaintainAdapter(Context context, byte[] arr, byte[] buffer, boolean isRoutine, String[] titles) {
        mContext = context;
        array = arr;
        misRoutine = isRoutine;
        title = titles;
        mBuffer = buffer;
    }

    @Override
    public int getCount() {
        if (title != null) {
            return title.length;
        } else {
            return 0;
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view;
        if (position == 0) {             //气体数据
            GasView gasView = new GasView(mContext);
            gasView.setData(mBuffer, misRoutine,mTimeCount);
            view = gasView;
            container.addView(gasView);
        } else {                         //设备数据
            MaintainView maintainView = new MaintainView(mContext);
            maintainView.setData(array, mContext);
            maintainView.setOnClickListener(this);
            view = maintainView;
            container.addView(view);
        }
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void setData(byte[] buffer, boolean isRoutine, boolean isEquipment,int countTime) {
        if (isEquipment) {       //设备数据
            mBuffer = buffer;
            mTimeCount = countTime;
        } else {                 //气体数据
            array = buffer;
        }
        misRoutine = isRoutine;
        notifyDataSetChanged();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (title != null) {
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    @Override
    public void onClick(View v) {
        mOnGasClickListener.onGasClick();
    }

    public interface OnGasClickListener {
        void onGasClick();
    }

    public void setOnGasClickListener(OnGasClickListener onGasClickListener) {
        mOnGasClickListener = onGasClickListener;
    }
}
