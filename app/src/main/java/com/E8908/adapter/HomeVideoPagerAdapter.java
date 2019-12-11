package com.E8908.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.E8908.conf.Constants;
import com.E8908.fragment.VideoFragmentOne;
import com.E8908.fragment.VideoFragmentThree;
import com.E8908.fragment.VideoFragmentTwo;

public class HomeVideoPagerAdapter extends FragmentStatePagerAdapter {
    private int mVideoCount;
    public HomeVideoPagerAdapter(FragmentManager fm,int videoCount) {
        super(fm);
        mVideoCount = videoCount;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                if (Constants.URLS.ID.equals("021")){
                    return new VideoFragmentThree();
                }else {
                    return new VideoFragmentOne();
                }
            case 1:
                if (Constants.URLS.ID.equals("021")){
                    return new VideoFragmentOne();
                }else {
                    return new VideoFragmentTwo();
                }
            default:
                return new VideoFragmentTwo();
        }
    }

    @Override
    public int getCount() {
        return mVideoCount;
    }

    //停止播放
    public void stopPlay() {

    }
    //是否在播放
    public boolean isPlaying() {
        return false;
    }
    //开始播放
    public void startPlay() {

    }
}
