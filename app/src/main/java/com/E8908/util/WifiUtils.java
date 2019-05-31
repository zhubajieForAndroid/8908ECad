package com.E8908.util;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/6/1.
 */

public class WifiUtils {
    private WifiManager mManager;
    private static WifiUtils mWifiUtils;
    private List<ScanResult> mWifiList;

    private WifiUtils(Context context) {
        mManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public static WifiUtils getInstance(Context context) {
        if (mWifiUtils == null) {
            synchronized (WifiUtils.class) {
                if (mWifiUtils == null) {
                    mWifiUtils = new WifiUtils(context);
                }
            }
        }
        return mWifiUtils;
    }

    //打开WiFi
    public boolean openWifi() {
        if (!mManager.isWifiEnabled()) {
            return mManager.setWifiEnabled(true);
        } else {
            return true;
        }
    }
    //关闭wifi
    public void closeWifi() {
        if (mManager.isWifiEnabled()) {
            mManager.setWifiEnabled(false);
        }
    }
    public boolean wifiIsEnabled() {
        return mManager.isWifiEnabled();
    }
    //扫描WiFi
    public void startScale() {

            mManager.startScan();

    }

    // 得到网络列表
    public List<ScanResult> getWifiList() {
        //得到扫描结果
        List<ScanResult> results = mManager.getScanResults();
        if (results != null) {
            mWifiList = new ArrayList();
            for (ScanResult result : results) {
                if (result.SSID == null || result.SSID.length() == 0 || result.capabilities.contains("[IBSS]")) {
                    continue;
                }
                boolean found = false;
                for (ScanResult item : mWifiList) {
                    if (item.SSID.equals(result.SSID) && item.capabilities.equals(result.capabilities)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    mWifiList.add(result);
                }
            }
        }
        return mWifiList;
    }


    // 添加一个网络并连接
    public Boolean addNetwork(WifiConfiguration wcg) {
        int wcgID = mManager.addNetwork(wcg);
        boolean b = mManager.enableNetwork(wcgID, true);
        return b;
    }

    //判断是否需要密码
    public int getSecurity(ScanResult result) {
        if (result.capabilities.contains("WEP")) {
            return 1;
        } else if (result.capabilities.contains("PSK")) {
            return 2;
        } else if (result.capabilities.contains("EAP")) {
            return 3;
        }
        return 0;
    }



    //保存WiFi配置
    public void scalWifiConfig() {
        mManager.saveConfiguration();
    }

    //然后是一个实际应用方法，只验证过没有密码的情况：

    public WifiConfiguration CreateWifiInfo(String SSID, String Password, int Type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        WifiConfiguration tempConfig = this.IsExsits(SSID);
        if (tempConfig != null) {
            mManager.removeNetwork(tempConfig.networkId);
        }

        if (Type == 1) { //WIFICIPHER_NOPASS

            //config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            //config.wepTxKeyIndex = 0;
        } else if (Type == 2) { //WIFICIPHER_WEP
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + Password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        } else if (Type == 3) { //WIFICIPHER_WPA
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            //config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }

    public WifiConfiguration IsExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = mManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }

}

