package com.E8908.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.E8908.R;
import com.E8908.conf.Constants;
import com.E8908.widget.VideoDialog;


public class VideoFragmentRunOne extends Fragment implements View.OnClickListener, View.OnTouchListener, VideoDialog.OnCurrentPosition {
    private static final String TAG = "VideoFragmentRunOne";
    private Context mContext;
    private VideoView mVideoView;
    private ImageView mStartOne;
    private ImageView mStartFull;
    private ImageView mStopOne;
    private RelativeLayout mControlContainer;
    private String mUriOne;
    private int mCurrentPosition;
    private int callBackTime;
    private VideoDialog mVideoDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.view_video_one, container, false);
        mContext = getContext();
        init(inflate);
        return inflate;
    }

    private void init(View inflate) {
        mVideoView = inflate.findViewById(R.id.video_one);
        mStartOne = inflate.findViewById(R.id.start_one);
        mStartFull = inflate.findViewById(R.id.start_full_one);
        mStopOne = inflate.findViewById(R.id.stop_one);
        mControlContainer = inflate.findViewById(R.id.control_container);
        mStartOne.setOnClickListener(this);
        mStartFull.setOnClickListener(this);
        mStopOne.setOnClickListener(this);
        mVideoView.setOnTouchListener(this);

        mUriOne = "android.resource://" + mContext.getPackageName() + "/" + R.raw.info_one;


        mVideoView.setVideoPath(mUriOne);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("video_action");
        mContext.registerReceiver(registerReceiver,intentFilter);

       /* //播放视频完成监听
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (Constants.URLS.ID.equals("021")){
                    if (playCount == 3){
                        playCount = 1;
                        mVideoView.setVideoPath(mUriThree);
                    }else if (playCount == 2) {
                        playCount++;
                        mVideoView.setVideoPath(mUriTwo);
                    } else if (playCount == 1){
                        playCount++;
                        mVideoView.setVideoPath(mUriOne);
                    }
                }else {
                    if (playCount == 2){
                        playCount = 1;
                        mVideoView.setVideoPath(mUriOne);
                    }else {
                        playCount++;
                        mVideoView.setVideoPath(mUriTwo);
                    }
                }
                mVideoView.start();
            }
        });*/
        mVideoView.setBackground(mContext.getResources().getDrawable(R.mipmap.hone_icon_one));
        //准备视频完成监听
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (!Constants.URLS.ID.equals("021")) {
                    mVideoView.start();
                    mVideoView.setBackground(null);
                    mStartOne.setVisibility(View.GONE);
                    mStopOne.setVisibility(View.VISIBLE);
                    mControlContainer.setVisibility(View.GONE);
                }
            }
        });


        mVideoDialog = new VideoDialog(mContext, R.style.dialog, mUriOne);
        mVideoDialog.setOnCurrentPosition(this);


    }
    private BroadcastReceiver registerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mVideoView != null && mVideoView.isPlaying()) {
                mVideoView.stopPlayback();
            }
        }
    };
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (mVideoView != null && mVideoView.isPlaying()) {
                mVideoView.pause();//暂停
                mStartOne.setVisibility(View.VISIBLE);
                mStopOne.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_one:                //开始
                if (mVideoView != null && !mVideoView.isPlaying()) {
                    mVideoView.setBackground(null);
                    if (callBackTime > 0)
                        mVideoView.seekTo(callBackTime);
                    mVideoView.start();
                    mStartOne.setVisibility(View.GONE);
                    mStopOne.setVisibility(View.VISIBLE);
                    mControlContainer.setVisibility(View.GONE);
                }
                break;
            case R.id.stop_one:                 //暂停
                if (mVideoView != null && mVideoView.isPlaying()) {
                    mVideoView.pause();
                    mStartOne.setVisibility(View.VISIBLE);
                    mStopOne.setVisibility(View.GONE);
                }
                break;
            case R.id.start_full_one:           //全屏
                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                    mStartOne.setVisibility(View.VISIBLE);
                    mStopOne.setVisibility(View.GONE);
                }
                mCurrentPosition = mVideoView.getCurrentPosition();
                showDialog(mCurrentPosition);
                break;
        }

    }

    private void showDialog(int currentPosition) {
        mVideoDialog.setStartCurrentPosition(currentPosition);
        mVideoDialog.show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mControlContainer.getVisibility() == View.GONE) {
            mControlContainer.setVisibility(View.VISIBLE);
        } else if (mControlContainer.getVisibility() == View.VISIBLE) {
            mControlContainer.setVisibility(View.GONE);
        }
        return false;
    }

    @Override
    public void onPosition(int time) {
        mStartOne.performClick();
        callBackTime = time;
    }

    public void stopPlay() {
        if (mVideoView != null && mVideoView.isPlaying()) {
            mVideoView.stopPlayback();
        }
    }

    public boolean isPlaying() {
        if (mVideoView != null) {
            return mVideoView.isPlaying();
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(registerReceiver);
    }
}
