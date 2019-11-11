package com.E8908.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.E8908.R;
import com.E8908.util.NavigationBarUtil;

import static android.support.constraint.Constraints.TAG;


public class VideoDialog extends Dialog implements View.OnTouchListener{
    private String mUrl;
    private VideoView mVideoView;
    private Context mContext;
    public float mUpX;
    public float mUpY;
    private float mMoveX;
    private float mMoveY;
    private float mDownY;
    private float mDownX;
    private AudioManager mManager;
    private int mStreamMaxVolume;
    private int mCurrentVolume;
    private int mCurrentPosition;
    private OnCurrentPosition mOnCurrentPosition;
    private Window mMwindow;

    public VideoDialog(Context context, int themeResId, String uriOne, int currentPosition) {
        super(context, themeResId);
        mUrl = uriOne;
        mContext =context;
        mCurrentPosition = currentPosition;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_video);
        mVideoView = findViewById(R.id.video_dialog);
        mMwindow = getWindow();
        WindowManager.LayoutParams lp = mMwindow.getAttributes();
        lp.dimAmount = 0f;
        lp.height = 800;
        lp.width = 1280;
        mMwindow.setAttributes(lp);
        mVideoView.setVideoPath(mUrl);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoView.seekTo(mCurrentPosition);
                mVideoView.start();
            }
        });

        mVideoView.setOnTouchListener(this);
        mManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        //最大音量
        mStreamMaxVolume = mManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        //获取当前音量
        mCurrentVolume = mManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mVideoView != null && mVideoView.isPlaying()){

            mVideoView.stopPlayback();
            mVideoView = null;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownY = event.getY();
                mDownX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveY = event.getY();
                mMoveX = event.getX();
                if ((mMoveY - mDownY) < 0) {    //手指向上滑动
                    if (mCurrentVolume < mStreamMaxVolume) {
                        mCurrentVolume++;
                        mManager.setStreamVolume(AudioManager.STREAM_MUSIC, mCurrentVolume, 0);
                    } else {
                        mManager.setStreamVolume(AudioManager.STREAM_MUSIC, mStreamMaxVolume, 0);
                    }
                } else if ((mMoveY - mDownY) > 0){                          //手指向下滑动
                    if (mCurrentVolume > 0) {
                        mCurrentVolume--;
                        mManager.setStreamVolume(AudioManager.STREAM_MUSIC, mCurrentVolume, 0);
                    } else {
                        mManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //判断按下和移动的距离
                if (Math.abs(mDownY - mMoveY) <= 3  && Math.abs(mDownX - mMoveX)<5){
                    mOnCurrentPosition.onPosition(mVideoView.getCurrentPosition());
                    dismiss();
                }
                break;
        }
        return true;
    }
    public interface OnCurrentPosition{
        void onPosition(int time);
    }

    public void setOnCurrentPosition(OnCurrentPosition onCurrentPosition) {
        mOnCurrentPosition = onCurrentPosition;
    }
}
