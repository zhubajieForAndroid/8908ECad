package com.E8908.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.E8908.R;
import com.E8908.widget.GasDialog;
import com.E8908.widget.HomeBleData;
import com.E8908.widget.HomeEquipmentDataView;

import java.util.Map;



public class HomeDataAdapter extends PagerAdapter implements View.OnClickListener {
    private static final String TAG = "HomeDataAdapter";
    private byte[] mBuffer;
    private byte[] mBleData;
    private Context mContext;
    private HomeEquipmentDataView mHomeEquipmentDataView;
    private HomeBleData mHomeBleData;
    private Map<String,Object> rankingMap;
    private String mBleID;

    public HomeDataAdapter(Context context) {
        mContext = context;
    }


    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view;
        if (position == 0) {             //设备数据
            if (mHomeEquipmentDataView == null)
                mHomeEquipmentDataView = new HomeEquipmentDataView(mContext);
            mHomeEquipmentDataView.setData(mBuffer,rankingMap);
            view = mHomeEquipmentDataView;
            container.addView(mHomeEquipmentDataView);
        } else {                         //气体数据
            if (mHomeBleData == null)
                mHomeBleData = new HomeBleData(mContext);
            mHomeBleData.setData(mBleData,mBleID);
            view = mHomeBleData;
            container.addView(mHomeBleData);
        }
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "设备运行";
            case 1:
                return "气体检测";
        }
        return "";
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    public void setData(byte[] buffer, Map<String,Object> ranksInfo, boolean isBle,String bleID) {
        if (buffer == null || buffer.length <= 0)
            return;
        if (isBle) {
            mBleData = buffer;
        } else {
            mBuffer = buffer;
        }
        rankingMap = ranksInfo;
        mBleID = bleID;
        notifyDataSetChanged();

    }

    public void setLinkBleState(boolean isLinkBle) {
        if (mHomeBleData != null)
            mHomeBleData.setLinkBleState(isLinkBle);
        if (isLinkBle && mHomeBleData != null){         //蓝牙已经连接,可以弹窗
            mHomeBleData.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        GasDialog mGasDialog = new GasDialog(mContext, R.style.dialog);
        if (!mGasDialog.isShowing()) {
            mGasDialog.show();
        }
    }
}
