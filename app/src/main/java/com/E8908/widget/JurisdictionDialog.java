package com.E8908.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.E8908.R;
import com.E8908.util.StringUtils;

import java.text.SimpleDateFormat;



public class JurisdictionDialog extends Dialog implements View.OnClickListener {

    private EditText mNumber;
    private String mEquipmentID;
    private OnCheckJListener mOnCheckJListener;
    private int  mState;

    public JurisdictionDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setEquipmentId(String equipmentNumber, int state) {
        setContentView(R.layout.view_jurisdiction);
        mEquipmentID = equipmentNumber;
        mState = state;
        EditText hintStr = findViewById(R.id.qeuipment_hint_id);
        hintStr.setText(equipmentNumber);

        mNumber = findViewById(R.id.number);
        Button cancle = findViewById(R.id.cancle);
        Button check = findViewById(R.id.ckeck);
        cancle.setOnClickListener(this);
        check.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancle:
                dismiss();
                break;
            case R.id.ckeck:
                //获取输入的权限码
                String code = mNumber.getText().toString().trim();
                if (!TextUtils.isEmpty(code) && StringUtils.isNumeric(code) && StringUtils.checkEncryptValue(code)){
                    String resultId = StringUtils.disposeChar(mEquipmentID);
                    //解析时间
                    String timeStr = StringUtils.getTimeStr(code, resultId);
                    if (!TextUtils.isEmpty(timeStr) && timeStr.length() != 4){
                        mOnCheckJListener.onCkcekState(mState,false,"权限码不合法");
                    }else {
                        String equipmentIDStr = StringUtils.getEquipmentID(code, timeStr, resultId);
                        String idInt = Integer.parseInt(resultId)+"";
                        if (idInt.equals(equipmentIDStr)){          //id验证成功
                            long timeMillis = System.currentTimeMillis();
                            SimpleDateFormat dateFormata = new SimpleDateFormat("HHmm");
                            String time = dateFormata.format(timeMillis);
                            int currentTime = Integer.parseInt(time);
                            int codeTime = Integer.parseInt(timeStr);

                            if ((codeTime+10) > currentTime){               //时间有效
                                mOnCheckJListener.onCkcekState(mState,true,"验证成功");
                                dismiss();
                            }else {
                                mOnCheckJListener.onCkcekState(mState,false,"已失效");
                            }
                        }else {
                            mOnCheckJListener.onCkcekState(mState,false,"验证失败");
                        }
                    }

                }else {
                    mOnCheckJListener.onCkcekState(mState,false,"权限码不合法");
                }
                break;
        }
    }
    public interface OnCheckJListener{
        void onCkcekState(int state,boolean isScurress,String mesg);
    }

    public void setOnCheckJListener(OnCheckJListener onCheckJListener) {
        mOnCheckJListener = onCheckJListener;
    }
}
