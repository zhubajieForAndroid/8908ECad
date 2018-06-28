package com.cad.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.cad.R;
import com.cad.util.SendUtil;

/**
 * Created by dell on 2018/3/19.
 */

public class SetIDDialog extends Dialog implements View.OnTouchListener {

    private static final String TAG = "SetVersionDialog";
    private Context mContent;
    private SquareEditextTwoContainer mSquareEditextTwoContainer;



    private DateResultListener mDateResultListener;
    public SetIDDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContent = context;
    }

    public void setBitmap(int imageRes) {
        setContentView(R.layout.dialog_set_id);
        ImageView iv = (ImageView) findViewById(R.id.dialog_bg_image);
        iv.setImageResource(imageRes);
        iv.setOnTouchListener(this);
        mSquareEditextTwoContainer = (SquareEditextTwoContainer) findViewById(R.id.load_result_number);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if ((x >= 8 && x <= 284) && (y >= 145 && y <= 195)) {                 //确定
            EditText belowEt = mSquareEditextTwoContainer.mBelowEt;
            String idResult = belowEt.getText().toString().trim();              //日期
            if (!TextUtils.isEmpty(idResult)){
                if (idResult.length() == 8){
                    mDateResultListener.dateResult(idResult);
                    dismiss();
                }else {
                    ToastUtil.showMessage("ID号必须为8位");
                }
            }else {
                ToastUtil.showMessage("ID号不能为空");
            }

        } else if ((x >= 295 && x <= 575) && (y >= 147 && y <= 193)) {         //取消
           SendUtil.controlVoice();
            dismiss();
        }
        return false;
    }
    public interface  DateResultListener{
        void dateResult(String date);
    }
    public void setDateResultListener(DateResultListener dateResultListener) {
        mDateResultListener = dateResultListener;
    }
}
