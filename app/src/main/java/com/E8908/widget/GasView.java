package com.E8908.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.E8908.R;
import com.E8908.conf.Constants;
import com.E8908.util.DataUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GasView extends RelativeLayout {
    @Bind(R.id.dc_voltage)
    TextView mDcVoltage;
    @Bind(R.id.dc_voltage_value)
    TextView mDcVoltageValue;
    @Bind(R.id.ac_current)
    TextView mAcCurrent;
    @Bind(R.id.ac_current_value)
    TextView mAcCurrentValue;
    @Bind(R.id.total_number_of_work)
    TextView mTotalNumberOfWork;
    @Bind(R.id.total_number_of_work_value)
    TextView mTotalNumberOfWorkValue;
    @Bind(R.id.surplus)
    TextView mSurplus;
    private String mDepthWorkNumbwe;
    private String mRoutineWorkNumbwe;
    private String mRiseNumbwe;

    public GasView(Context context) {
        this(context, null);
    }

    public GasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.view_gas, this);
        ButterKnife.bind(this,this);
    }

    public void setData(byte[] arr, boolean misRoutine) {
        if (arr == null || arr.length <=0)
            return;
        int electricPress = DataUtil.directElectricPress(arr);      //直流电压
        float communionFlow = DataUtil.directCommunionFlow(arr);       //交流电流
        //获取深度保养次数
        mDepthWorkNumbwe = DataUtil.getDepthWorkNumbwe(arr);
        //获取常规保养次数
        mRoutineWorkNumbwe = DataUtil.getRoutineWorkNumbwe(arr);
        //获取液体剩余升数
        mRiseNumbwe = DataUtil.getRiseNumbwe(arr);
        //药液量
        int mResultRatioNumbwe = Integer.parseInt(mRiseNumbwe, 16);

        //设置剩余量
        if (mResultRatioNumbwe < Constants.MAX_NUMBER) {
            mSurplus.setText(mResultRatioNumbwe + "ML ("+(mResultRatioNumbwe/250)+")次");
        } else {
            mSurplus.setText(Constants.MAX_NUMBER - 1 + "ML(80次)");
        }
        mDcVoltageValue.setText((electricPress) + "V");
        //交流电流
        mAcCurrentValue.setText((communionFlow / 10) + "A");
        if (misRoutine) {
            mTotalNumberOfWorkValue.setText(Integer.parseInt(mRoutineWorkNumbwe, 16) + "次");
        } else {
            mTotalNumberOfWorkValue.setText(Integer.parseInt(mDepthWorkNumbwe, 16) + "次");
        }
    }

}
