package com.E8908.manage;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.E8908.util.SharedPreferencesUtils;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

import static android.support.constraint.Constraints.TAG;


public class MyLocationListener extends BDAbstractLocationListener {
    @Override
    public void onReceiveLocation(BDLocation location){

        double latitude = location.getLatitude();    //获取纬度信息       正数是北纬,负数是南纬
        double longitude = location.getLongitude();    //获取经度信息     正数是东经,负数是西经
        try {
            String resultLatitude = locationToMS(latitude);
            String resultLongitude = locationToMS(longitude);

            if (!TextUtils.isEmpty(resultLatitude) && !TextUtils.isEmpty(resultLongitude)) {
                SharedPreferences locationState = SharedPreferencesUtils.getLocationState();
                SharedPreferences.Editor edit = locationState.edit();
                edit.putString("latitude", resultLatitude);
                edit.putString("longitude", resultLongitude);
                edit.apply();
            }
        }catch (Exception e){
            Log.d(TAG, "onReceiveLocation: 经纬度异常");
        }
    }


    //经纬度转化为4个byte的16进制
    private String locationToMS(double location) {
        double tempLocation = location;
        if (tempLocation < 0) {
            tempLocation = -tempLocation;
        }
        if (tempLocation != 0) {
            String[] splitValue = (tempLocation + "").split("\\.");
            if (splitValue.length != 2) {
                return "";
            }
            String front = splitValue[0];
            String back = splitValue[1];
            int frontValue = Integer.parseInt(front) * 3600000;
            int backValue = Integer.parseInt(back)/10 * 36;
            //10进制转2进制
            String binaryString = Integer.toBinaryString(frontValue + backValue);
            byte[] valueBrr = new byte[binaryString.length()];
            for (int i = 0; i < binaryString.length(); i++) {
                valueBrr[i] = (byte) Integer.parseInt(binaryString.charAt(i) + "");
            }
            //逻辑:4个比特位,32个字节,valueBrr一般肯定不是32个字节的,需要前面补字节
            if (valueBrr.length >= 32) {
                return "";
            } else {
                byte[] plusValue = new byte[32 - valueBrr.length];
                if (location < 0) {
                    plusValue[0] = 1;
                }
                //合并数组
                byte[] resultBrr = concatByteArray(plusValue, valueBrr);
                //转成16进制
                return bin2HexStr(resultBrr);
            }
        } else {
            return "";
        }
    }


    //合并2个byte数组的方法
    public static byte[] concatByteArray(byte[] first, byte[] second) {
        byte[] threeArr = new byte[first.length + second.length];
        //合并数组
        System.arraycopy(first, 0, threeArr, 0, first.length);
        System.arraycopy(second, 0, threeArr, first.length, second.length);
        return threeArr;
    }
    /**
     * @param bytes 必须是4的倍数
     * @return 将二进制数组转换为十六进制字符串  2-16
     */
    public static String bin2HexStr(byte[] bytes) {
        String result = "";
        String hex = "";
        for (int i = 0; i < bytes.length; i++) {
            //转换成字符串
            result = result + bytes[i];
        }
        int length = bytes.length / 4;
        for (int i = 0; i < length; i++) {
            int temp = Integer.parseInt(result.substring(i * 4, (i + 1) * 4), 2);
            hex = hex + Integer.toHexString(temp);
        }
        return hex;
    }


}
