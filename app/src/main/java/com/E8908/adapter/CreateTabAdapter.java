package com.E8908.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.E8908.db.DaoQueryBean;
import com.E8908.fragment.CheneiYiweiView;
import com.E8908.fragment.KongtiaoView;
import com.E8908.fragment.YouHaiqitiView;
import com.E8908.fragment.ZhengjieView;


public class CreateTabAdapter extends PagerAdapter {
    private Context mContext;
    private String[] mTitles;
    private DaoQueryBean mDaoQueryBean;
    public ZhengjieView mZhengjieView;
    private OnCreateWpbfView mOnCreateWpbfView;
    private OnCreateKtxtView mOnCreateKtxtView;
    private OnCheneiYiweiCreatedListener mOnCheneiYiweiCreatedListener;

    public CreateTabAdapter(Context context, String[] titles, DaoQueryBean daoQueryBean) {
        mContext = context;
        mTitles = titles;
        mDaoQueryBean = daoQueryBean;
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        switch (position) {
            case 0:                                                     //整洁卫生
                mZhengjieView = new ZhengjieView(mContext);
                mZhengjieView.setData(mDaoQueryBean);
                container.addView(mZhengjieView);
                mOnCreateWpbfView.onViewCreated(mZhengjieView);
                return mZhengjieView;
            case 1:                                                     //空调系统检测
                KongtiaoView kongtiaoView = new KongtiaoView(mContext);
                kongtiaoView.setData(mDaoQueryBean);
                container.addView(kongtiaoView);
                mOnCreateKtxtView.onWpbfViewCreated(kongtiaoView);
                return kongtiaoView;
            case 2:                                                      //车内异味检测
                CheneiYiweiView cheneiYiweiView = new CheneiYiweiView(mContext);
                cheneiYiweiView.setData(mDaoQueryBean);
                container.addView(cheneiYiweiView);
                mOnCheneiYiweiCreatedListener.onCheneiYiweiCreated(cheneiYiweiView);
                return cheneiYiweiView;
            case 3:
                YouHaiqitiView youHaiqitiView = new YouHaiqitiView(mContext);
                container.addView(youHaiqitiView);
                return youHaiqitiView;
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


    public void setData(DaoQueryBean daoQueryBean) {
        mDaoQueryBean = daoQueryBean;
        notifyDataSetChanged();
    }

    public interface OnCreateWpbfView {
        void onViewCreated(ZhengjieView view);
    }

    public void setOnCreateWpbfView(OnCreateWpbfView onCreateWpbfView) {
        mOnCreateWpbfView = onCreateWpbfView;
    }

    public interface OnCreateKtxtView {
        void onWpbfViewCreated(KongtiaoView view);
    }

    public void setOnCreateKtxtView(OnCreateKtxtView onCreateKtxtView) {
        mOnCreateKtxtView = onCreateKtxtView;
    }

    public interface OnCheneiYiweiCreatedListener {
        void onCheneiYiweiCreated(CheneiYiweiView view);
    }

    public void setOnCheneiYiweiCreatedListener(OnCheneiYiweiCreatedListener onCheneiYiweiCreatedListener) {
        mOnCheneiYiweiCreatedListener = onCheneiYiweiCreatedListener;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
