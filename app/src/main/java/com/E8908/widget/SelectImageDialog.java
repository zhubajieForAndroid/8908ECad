package com.E8908.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.E8908.R;
import com.E8908.util.NavigationBarUtil;


/**
 * 上传图片是选择是拍照还是从相册选的弹窗
 */
public class SelectImageDialog extends Dialog implements View.OnTouchListener {
    private OnSelectCameraBtnListener mOnSelectCameraBtnListener;
    private OnSelectAlbumBtnListener mOnSelectAlbumBtnListener;
    private Window mWindow;

    public SelectImageDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setImg(int popup_photo) {
        setContentView(R.layout.view_select_dialog);
        ImageView imageView = findViewById(R.id.select_image);
        imageView.setImageResource(popup_photo);
        imageView.setOnTouchListener(this);
        mWindow = getWindow();
        if (mWindow != null) {
            WindowManager.LayoutParams wlp = mWindow.getAttributes();
            wlp.gravity = Gravity.CENTER;
            wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            mWindow.setAttributes(wlp);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if ((x >= 10 && x <= 495) && (y >= 10 && y <= 110)) {       //拍照
            mOnSelectCameraBtnListener.onSelectCamera();
            dismiss();
        } else if ((x >= 5 && x <= 495) && (y >= 120 && y <= 208)) {        //相册
            mOnSelectAlbumBtnListener.onSelectAlbum();
            dismiss();
        } else if ((x >= 6 && x <= 495) && (y >= 128 && y <= 302)) {        //取消
            dismiss();
        }
        return false;
    }


    public interface OnSelectCameraBtnListener {
        void onSelectCamera();
    }

    public interface OnSelectAlbumBtnListener {
        void onSelectAlbum();
    }

    public void setOnSelectCameraBtnListener(OnSelectCameraBtnListener onSelectCameraBtnListener) {
        mOnSelectCameraBtnListener = onSelectCameraBtnListener;
    }

    public void setOnSelectAlbumBtnListener(OnSelectAlbumBtnListener onSelectAlbumBtnListener) {
        mOnSelectAlbumBtnListener = onSelectAlbumBtnListener;
    }
}
