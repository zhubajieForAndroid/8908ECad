package com.E8908.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.E8908.base.CurverBean;
import com.E8908.fragment.Formaldehyde;
import com.E8908.fragment.PM;
import com.E8908.fragment.ShiDu;
import com.E8908.fragment.TVOC;
import com.E8908.fragment.WenDu;

public class CurvePagerAdapter extends PagerAdapter {
    private String[] mTitles;
    private CurverBean.ResponseBean mResponseBean;
    private Context mContext;
    public CurvePagerAdapter(Context context, String[] titles, CurverBean.ResponseBean response) {
        mTitles = titles;
        mContext = context;
        mResponseBean = response;
    }

    @Override
    public int getCount() {
        if (mResponseBean == null){
            return 0;
        }else {
            return mTitles.length;
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        switch (position) {
            case 0:
                Formaldehyde formaldehyde = new Formaldehyde(mContext);
                formaldehyde.setData(mResponseBean);
                container.addView(formaldehyde);
                return formaldehyde;
            case 1:
                TVOC tvoc = new TVOC(mContext);
                tvoc.setData(mResponseBean);
                container.addView(tvoc);
                return tvoc;
            case 2:
                PM pm = new PM(mContext);
                pm.setData(mResponseBean);
                container.addView(pm);
                return pm;
            case 3:
                WenDu wenDu = new WenDu(mContext);
                wenDu.setData(mResponseBean);
                container.addView(wenDu);
                return wenDu;
            case 4:
                ShiDu shiDu = new ShiDu(mContext);
                shiDu.setData(mResponseBean);
                container.addView(shiDu);
                return shiDu;
            default:
                return null;
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    public void setData(CurverBean.ResponseBean response) {
        mResponseBean = response;
        notifyDataSetChanged();
    }
}
