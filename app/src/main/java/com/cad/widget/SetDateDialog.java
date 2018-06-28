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

public class SetDateDialog extends Dialog implements View.OnTouchListener {

    private static final String TAG = "SetVersionDialog";
    private Context mContent;
    private SquareEditextThreeContainer mSquareEditextTwoContainer;



    private DateResultListener mDateResultListener;
    public SetDateDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContent = context;
    }

    public void setBitmap(int imageRes) {
        setContentView(R.layout.dialog_set_date);
        ImageView iv = (ImageView) findViewById(R.id.dialog_bg_image);
        iv.setImageResource(imageRes);
        iv.setOnTouchListener(this);
        mSquareEditextTwoContainer = (SquareEditextThreeContainer) findViewById(R.id.load_result_number);
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
                    ToastUtil.showMessage("正确格式:20180101");
                }
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
