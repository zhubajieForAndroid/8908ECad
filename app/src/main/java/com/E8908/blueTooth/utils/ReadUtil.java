package com.E8908.blueTooth.utils;

public class ReadUtil {
    private static boolean isSaveBuffer = false;
    private static byte[] buffer = null;
    public static byte[] resole(byte[] sourceData) {
        if (sourceData == null)
            return null;
        if (sourceData.length >0 && sourceData[0] == 42 && !isSaveBuffer) {         //数据开头校验
            if (sourceData.length >= 2 && sourceData[1] == sourceData.length) {     //数据长度校验
                if (sourceData[sourceData.length-1] == 35){                         //数据的结尾校验
                    //返回正确数据
                    return sourceData;
                }else {                                            //无效数据
                   return null;
                }
            }else {                                                 //暂时保存在缓冲buffer
                buffer = sourceData;
                isSaveBuffer = true;
            }
        }else {
            //拼接成完整的数据
            if (buffer != null && isSaveBuffer){
                byte[] temp = new byte[buffer.length+sourceData.length];
                System.arraycopy(buffer, 0, temp, 0, buffer.length);
                System.arraycopy(sourceData, 0, temp, buffer.length, sourceData.length);
                isSaveBuffer = false;
                buffer = null;
                //验证最终的数据
                byte[] bytes = resoleResult(temp);
                if (bytes != null){
                    return bytes;
                }else {
                    return null;
                }
            }
        }
        return null;
    }
    private static byte[] resoleResult(byte[] arr){
        if (arr == null)
            return null;
        if (arr.length >0 && arr[0] == 42){
            if (arr.length >= 2 && arr[1] == arr.length){
                if (arr[arr.length-1] == 35){
                    return arr;
                }
            }
        }
        return null;
    }

    /**
     * 获取序列号
     *
     * @param array 一组数据
     * @return 设备的序列号
     */
    public static String getBleID(byte[] array) {
        return  Integer.toHexString(array[12]) + Integer.toHexString(array[13]) +  Integer.toHexString(array[14]) +Integer.toHexString(array[15])
                +Integer.toHexString(array[16]) + Integer.toHexString(array[17]) +Integer.toHexString(array[18]) +Integer.toHexString(array[19]);
    }


}
