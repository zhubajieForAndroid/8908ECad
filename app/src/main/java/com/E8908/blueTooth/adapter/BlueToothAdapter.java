package com.E8908.blueTooth.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.E8908.R;
import com.E8908.widget.ToastUtil;
import com.clj.fastble.data.BleDevice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;

public class BlueToothAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<BleDevice> mBluetoothDevices;
    private OnBleItemClcikListener mOnBleItemClcikListener;
    private Map<Integer, Boolean> tempState = new HashMap<>();
    private int checkedPosition = -1;
    private boolean onBind;

    public BlueToothAdapter(Context context, List<BleDevice> noBondBlues) {
        mContext = context;
        mBluetoothDevices = noBondBlues;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View inflate = View.inflate(mContext, R.layout.item_view_blue_list, null);
        return new BlueToothItemViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        if (viewHolder instanceof BlueToothItemViewHolder) {
            final BlueToothItemViewHolder blueToothItemViewHolder = (BlueToothItemViewHolder) viewHolder;
            BleDevice bleDevice = mBluetoothDevices.get(i);
            String address = bleDevice.getMac();
            final String name = bleDevice.getName();
            blueToothItemViewHolder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (blueToothItemViewHolder.mCheckBox.isChecked()) {
                        blueToothItemViewHolder.mCheckBox.setChecked(false);
                        tempState.remove(i);
                        if (tempState.size() == 0) {
                            checkedPosition = -1; //-1 代表一个都未选择
                        }
                    } else {
                        //得到扫描设备集合
                        String substring = name.substring(2, 4);
                        if ("3".equals(substring)) {
                            ToastUtil.showMessageLong("该选中的蓝牙仪器不是气体检测仪器,请选择ID号第4位为3的蓝牙仪器");
                        }
                        blueToothItemViewHolder.mCheckBox.setChecked(true);
                        tempState.clear();
                        tempState.put(i, true);
                        checkedPosition = i;
                    }
                    if (!onBind) {
                        notifyDataSetChanged();
                    }
                    mOnBleItemClcikListener.bleItemClick(checkedPosition);
                }
            });

            blueToothItemViewHolder.mBlueName.setText("CAD-QT"+name);

            onBind = true;
            if (tempState != null && tempState.containsKey(i)) {
                blueToothItemViewHolder.mCheckBox.setChecked(true);
            } else {
                blueToothItemViewHolder.mCheckBox.setChecked(false);
            }
            onBind = false;
        }
    }

    @Override
    public int getItemCount() {
        if (mBluetoothDevices != null) {
            return mBluetoothDevices.size();
        }
        return 0;
    }

    public BleDevice getDevice(int position) {
        return mBluetoothDevices.get(position);
    }

    private class BlueToothItemViewHolder extends RecyclerView.ViewHolder {
        public TextView mBlueName;
        public LinearLayout mLinearLayout;
        public CheckBox mCheckBox;

        public BlueToothItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mBlueName = itemView.findViewById(R.id.blue_name);
            mLinearLayout = itemView.findViewById(R.id.ble_container);
            mCheckBox = itemView.findViewById(R.id.ble_radio);
        }
    }

    public interface OnBleItemClcikListener {
        void bleItemClick(int selectPosition);
    }

    public void setOnBleItemClcikListener(OnBleItemClcikListener onBleItemClcikListener) {
        mOnBleItemClcikListener = onBleItemClcikListener;
    }
}
