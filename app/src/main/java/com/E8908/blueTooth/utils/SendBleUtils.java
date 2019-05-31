package com.E8908.blueTooth.utils;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;


public class SendBleUtils {
    /**
     * 读取设备数据
     *
     * @param manager            蓝牙管理
     * @param device             远程蓝牙设备
     * @param uuidService        远程蓝牙UUID
     * @param uuidCharacteristic 本地UUID
     * @param equipmentId        设备ID
     * @param callback           发送数据回调
     */
    public static void sendBleData(BleManager manager, BleDevice device, String uuidService, String uuidCharacteristic, String equipmentId, BleWriteCallback callback) {
        if (manager == null || uuidCharacteristic == null || uuidService == null || equipmentId == null || equipmentId.length() != 8)
            return;
        byte[] ids = hexString2Intger(equipmentId);
        int result = 0x2a ^ 0x0E ^ ids[0] ^ ids[1] ^ ids[2] ^ ids[3] ^ ids[4] ^ ids[5] ^ ids[6] ^ ids[7] ^ 0x55 ^ 0x01;
        byte[] data = {0x2a, 0x0E, ids[0], ids[1], ids[2], ids[3], ids[4], ids[5], ids[6], ids[7], 0x55, 0x01, (byte) result, 0x23};
        manager.write(device, uuidService, uuidCharacteristic, data, callback);

    }

    private static byte[] hexString2Intger(String str) {
        byte[] byteTarget = new byte[str.length()];
        for (int i = 0; i < str.length(); ++i)
            byteTarget[i] = (byte) (Integer.parseInt(str.substring(i, i + 1), 16) & 0xff);
        return byteTarget;
    }

    /**
     * 读取蓝牙ID
     */
    public static void readBleID(BleManager manager, BleDevice device, String uuidService, String uuidCharacteristic, BleWriteCallback callback) {
        if (manager == null || uuidCharacteristic == null || uuidService == null)
            return;
        int result = 0x2a ^ 0x0E ^ 0xff ^ 0xff ^ 0xff ^ 0xff ^ 0xff ^ 0xff ^ 0xff ^ 0xff ^ 0x55 ^ 0x03;
        byte[] data = {0x2a, 0x0E, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x55, 0x03, (byte) result, 0x23};
        manager.write(device, uuidService, uuidCharacteristic, data, callback);
    }
}
