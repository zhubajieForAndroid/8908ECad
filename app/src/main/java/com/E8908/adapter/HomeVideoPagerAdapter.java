package com.E8908.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.E8908.fragment.VideoFragmentOne;
import com.E8908.fragment.VideoFragmentTwo;

public class HomeVideoPagerAdapter extends FragmentStatePagerAdapter {
    public HomeVideoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new VideoFragmentOne();
            case 1:
                return new VideoFragmentTwo();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "1";
            case 1:
                return "2";
            default:
                return null;
        }
    }
}
