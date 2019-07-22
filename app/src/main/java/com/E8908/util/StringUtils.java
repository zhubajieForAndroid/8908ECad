package com.E8908.util;


import android.text.TextUtils;
import android.util.Log;


import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class StringUtils {
    private static BigDecimal KEY_ENCRYPT = new BigDecimal("9");
    /**
     * 校验蓝牙名字和蓝牙地址
     * @param text
     * @return
     */
    public static boolean checkBleNameAndMac(String text){
        if (!TextUtils.isEmpty(text) && !"".equals(text) && text.length() >7) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 校验车牌
     *
     * @param carNumber 车牌号
     * @return
     */
    public static boolean ckeckCarNumber(String carNumber) {
        String str = "^([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[a-zA-Z](([DF]((?![IO])[a-zA-Z0-9](?![IO]))[0-9]{4})|([0-9]{5}[DF]))|[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1})$";
        return carNumber.matches(str);
    }

    public static String getTimeStr(String code,String equipmentID){
        String decrypt = decrypt(code);
        if (decrypt.length() < 3)
            return null;
        decrypt = decrypt.substring(0, decrypt.length()-3);
        return getTime(decrypt,equipmentID);
    }
    public static String getEquipmentID(String code,String resultTime,String equipmentID){
        if (TextUtils.isEmpty(resultTime))
            return null;
        String decrypt = decrypt(code);
        if (decrypt.length() < 3)
            return null;
        decrypt = decrypt.substring(0, decrypt.length()-3);
        return (Integer.parseInt(decrypt)-Integer.parseInt(resultTime))+"";
    }


    /**
     * 校验授权码的合法性
     * 合法返回true
     * 非法返回false
     */
    public static boolean checkEncryptValue(String encryptValue){
        BigDecimal value = new BigDecimal(encryptValue);
        String valueStr = value.divide(KEY_ENCRYPT,2,BigDecimal.ROUND_HALF_UP).toString();
        if(valueStr.indexOf(".00")!=-1){
            return true;
        }
        return false;
    }

    /**
     * 解密算法

     * @return
     */
    public static String decrypt(String cipherStr){
        BigDecimal baseBD = new BigDecimal(cipherStr);
        return baseBD.divide(KEY_ENCRYPT, 2, BigDecimal.ROUND_HALF_UP).toString();
    }
    /**
     * 把字母设备编号位变成0
     * @return
     */
    public static String disposeChar(String equipmentId){
        if(null!=equipmentId){
            char[] car = equipmentId.toCharArray();
            String newId = "";
            for(char c :car){
                if(isLetter(c)){
                    newId=newId+"0";
                }else{
                    newId=newId+c+"";
                }
            }
            return newId;
        }
        return null;
    }

    /**
     * 取时间值
     * @param baseValue 解密后串
     * @param equipmentId 设备号
     * @return 13717421.00
     */
    public static String getTime(String baseValue,String equipmentId){
        int baseValueInt = Integer.parseInt(baseValue);
        int equipmentIdInt = Integer.parseInt(disposeChar(equipmentId));
        int time = (baseValueInt -equipmentIdInt);
        String timeValue = time+"";
        //处理01——09小时区间首位值为零的
        if(timeValue.length()==3){
            timeValue = "0"+timeValue;
        }
        //处理00小时时前两位为零值的
        if(timeValue.length()==2){
            timeValue = "00"+timeValue;
        }
        return timeValue;
    }
    /**
     * 判断是否是字母
     * @param str 传入字符串
     * @return 是字母返回true，否则返回false
     */
    public static boolean isLetter(char str) {
        String v = str+"";
        return v.matches("[a-zA-Z]+");
    }

    /**
     * 判断是否纯数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }


}
