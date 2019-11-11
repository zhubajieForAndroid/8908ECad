package com.E8908.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
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
import android.widget.VideoView;

import com.E8908.R;
import com.E8908.widget.VideoDialog;


public class VideoFragmentTwo extends Fragment implements View.OnClickListener, View.OnTouchListener, VideoDialog.OnCurrentPosition {

    private Context mContext;
    private VideoView mVideoView;
    private ImageView mStartTwo;
    private View mStartFull;
    private View mStoptwo;
    private View mControlContainer;
    private int mCurrentPosition;
    private String mUriTwo;
    private int callBackTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.view_video_two, container, false);
        mContext = getContext();
        init(inflate);
        return inflate;
    }

    private void init(View inflate) {
        mVideoView = inflate.findViewById(R.id.video_two);
        mStartTwo = inflate.findViewById(R.id.start_two);
        mStartFull = inflate.findViewById(R.id.start_full_two);
        mStoptwo = inflate.findViewById(R.id.stop_two);
        mControlContainer = inflate.findViewById(R.id.control_container);

        mStartTwo.setOnClickListener(this);
        mStartFull.setOnClickListener(this);
        mStoptwo.setOnClickListener(this);
        mVideoView.setOnTouchListener(this);
        mUriTwo = "android.resource://" + mContext.getPackageName() + "/" + R.raw.info_two;
        mVideoView.setVideoURI(Uri.parse(mUriTwo));
        mVideoView.setBackground(mContext.getResources().getDrawable(R.mipmap.home_icon));
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVideoView.setBackground(mContext.getResources().getDrawable(R.mipmap.home_icon));
                mVideoView.setVideoPath(mUriTwo);
                mVideoView.seekTo(0);
                mStartTwo.setVisibility(View.VISIBLE);
                mStoptwo.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser){
            if (mVideoView != null && mVideoView.isPlaying()) {
                mVideoView.pause();//暂停
                mStartTwo.setVisibility(View.VISIBLE);
                mStoptwo.setVisibility(View.GONE);

            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_two:
                if (mVideoView != null && !mVideoView.isPlaying()) {
                    mVideoView.setBackground(null);
                    if (callBackTime > 0)
                        mVideoView.seekTo(callBackTime);
                    mVideoView.start();
                    mStartTwo.setVisibility(View.GONE);
                    mStoptwo.setVisibility(View.VISIBLE);
                    mControlContainer.setVisibility(View.GONE);
                }
                break;
            case R.id.stop_two:                 //暂停
                if (mVideoView != null && mVideoView.isPlaying()) {
                    mVideoView.pause();
                    mStartTwo.setVisibility(View.VISIBLE);
                    mStoptwo.setVisibility(View.GONE);
                }
                break;
            case R.id.start_full_two:           //全屏
                if (mVideoView.isPlaying()){
                    mVideoView.pause();
                    mStartTwo.setVisibility(View.VISIBLE);
                    mStoptwo.setVisibility(View.GONE);
                }
                mCurrentPosition = mVideoView.getCurrentPosition();
                showDialog(mCurrentPosition);
                break;
        }
    }
    private void showDialog(int currentPosition) {
        VideoDialog videoDialog = new VideoDialog(mContext,R.style.dialog,mUriTwo,currentPosition);
        videoDialog.setOnCurrentPosition(this);
        videoDialog.show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mControlContainer.getVisibility() == View.GONE){
            mControlContainer.setVisibility(View.VISIBLE);
        }else if (mControlContainer.getVisibility() == View.VISIBLE){
            mControlContainer.setVisibility(View.GONE);
        }
        return false;
    }

    @Override
    public void onPosition(int time) {
        mStartTwo.performClick();
        callBackTime = time;
    }
}
