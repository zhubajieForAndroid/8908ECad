package com.cad.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cad.R;
import com.cad.util.WifiUtils;

import java.util.List;

/**
 * Created by dell on 2018/3/19.
 */

public class WifiAdapter extends BaseAdapter {
    private static final String TAG = "WifiAdapter";
    private Context mContext;
    private List<ScanResult> mScanResultList;
    private WifiUtils mWifiUtils;

    public WifiAdapter(Context context, List<ScanResult> wifiList, WifiUtils wifiUtils) {
        mContext = context;
        mScanResultList = wifiList;
        mWifiUtils = wifiUtils;
    }

    @Override
    public int getCount() {
        if (mScanResultList != null) {
            return mScanResultList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.view_wifi, null);
            holder.wifiName = (TextView) convertView.findViewById(R.id.wifi_name);
            holder.wifiLinkSpeed = (ImageView) convertView.findViewById(R.id.wifi_link_speed);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ScanResult scanResult = mScanResultList.get(position);
        int security = mWifiUtils.getSecurity(scanResult);
        if (scanResult.level < 0 && scanResult.level >= -55) {
            //满格
            if (security != 0) {
                holder.wifiLinkSpeed.setImageResource(R.mipmap.wifi_5);
            } else {
                holder.wifiLinkSpeed.setImageResource(R.mipmap.wifi_4_4);
            }
        }
        if (scanResult.level < -55 && scanResult.level >= -70) {
            //三格
            if (security != 0) {
                holder.wifiLinkSpeed.setImageResource(R.mipmap.wifi_lock_3);
            } else {
                holder.wifiLinkSpeed.setImageResource(R.mipmap.wifi_3_3);
            }

        }
        if (scanResult.level < -70 && scanResult.level >= -85) {
            //2格
            if (security != 0) {
                holder.wifiLinkSpeed.setImageResource(R.mipmap.wifi_lock_2);
            } else {
                holder.wifiLinkSpeed.setImageResource(R.mipmap.wifi_2_2);
            }

        }
        if (scanResult.level < -85 && scanResult.level >= -100) {
            //一格
            if (security != 0) {
                holder.wifiLinkSpeed.setImageResource(R.mipmap.wifi_lock_1);
            } else {
                holder.wifiLinkSpeed.setImageResource(R.mipmap.wifi_1_1);
            }

        }
        //过滤无信号的WiFi
        if (!(scanResult.level < -100)) {
            if (!scanResult.SSID.equals(" "))
                holder.wifiName.setText(scanResult.SSID);
        }
        return convertView;
    }

    public void updataWifiList(List<ScanResult> wifiList) {
        mScanResultList = wifiList;
        notifyDataSetChanged();
    }

    public class ViewHolder {
        TextView wifiName;
        ImageView wifiLinkSpeed;
    }
}
