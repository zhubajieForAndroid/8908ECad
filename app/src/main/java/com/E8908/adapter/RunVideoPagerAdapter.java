package com.E8908.adapter;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.E8908.base.MyApplication;
import com.E8908.conf.Constants;
import com.E8908.fragment.VideoFragmentRunOne;
import com.E8908.fragment.VideoFragmentRunThree;
import com.E8908.fragment.VideoFragmentRunTwo;

public class RunVideoPagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "RunVideoPagerAdapter";
    private int mVideoCount;

    public RunVideoPagerAdapter(FragmentManager fm, int videoCount) {
        super(fm);
        mVideoCount = videoCount;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                if (Constants.URLS.ID.equals("021")){
                    return new VideoFragmentRunThree();
                }else {
                    return new VideoFragmentRunOne();
                }
            case 1:
                if (Constants.URLS.ID.equals("021")){
                    return new VideoFragmentRunOne();
                }else {
                    return new VideoFragmentRunTwo();
                }
            default:
                return new VideoFragmentRunTwo();
        }
    }

    @Override
    public int getCount() {
        return mVideoCount;
    }

    //停止播放
    public void stopPlay() {
        Intent intent = new Intent();
        intent.setAction("video_action");
        MyApplication.getContext().sendBroadcast(intent);
    }



    //开始播放
    public void startPlay() {

    }
}
