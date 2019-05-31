package com.E8908.widget;

import android.widget.ImageView;

import com.E8908.R;

/**
 * Created by dell on 2018/3/19.
 */

public class BitmapUtil {
    private static int[] bitmapArr = {R.mipmap.digit_0, R.mipmap.digit_1, R.mipmap.digit_2,
            R.mipmap.digit_3, R.mipmap.digit_4, R.mipmap.digit_5, R.mipmap.digit_6,
            R.mipmap.digit_7, R.mipmap.digit_8, R.mipmap.digit_9};
    private static int[] blueImage = {R.mipmap.digit_blue_0, R.mipmap.digit_blue_1, R.mipmap.digit_blue_2,
            R.mipmap.digit_blue_3, R.mipmap.digit_blue_4, R.mipmap.digit_blue_5, R.mipmap.digit_blue_6,
            R.mipmap.digit_blue_7, R.mipmap.digit_blue_8, R.mipmap.digit_blue_9};
    private static int[] bitmapRedArr = {R.mipmap.digit_red_0, R.mipmap.digit_red_1, R.mipmap.digit_red_2,
            R.mipmap.digit_red_3, R.mipmap.digit_red_4, R.mipmap.digit_red_5, R.mipmap.digit_red_6,
            R.mipmap.digit_red_7, R.mipmap.digit_red_8, R.mipmap.digit_red_9};

    public static void numberToBItmapTwo(int num, ImageView imOne, ImageView imTwo) {
        if (num < 10) {
            imOne.setImageResource(bitmapArr[0]);
            imTwo.setImageResource(bitmapArr[num]);
        } else {
            int ge = num % 10;
            int shi = num / 10;
            imTwo.setImageResource(bitmapArr[ge]);
            imOne.setImageResource(bitmapArr[shi]);
        }
    }
    public static void numberBlueToBItmapTwo(int num, ImageView imOne, ImageView imTwo) {
        if (num <0)
            return;
        if (num < 10) {
            imOne.setImageResource(blueImage[0]);
            imTwo.setImageResource(blueImage[num]);
        } else {
            int ge = num % 10;
            int shi = num / 10;
            imTwo.setImageResource(blueImage[ge]);
            imOne.setImageResource(blueImage[shi]);
        }
    }

    public static void numberToBItmapThree(int num, ImageView imOne, ImageView imTwo, ImageView imThree) {
        int ge = num % 10;
        int shi = (num / 10) % 10;
        int bai = num / 100;
        imOne.setImageResource(bitmapArr[ge]);
        imTwo.setImageResource(bitmapArr[shi]);
        imThree.setImageResource(bitmapArr[bai]);
    }

    public static void numberToBItmapFour(int num, ImageView imOne, ImageView imTwo, ImageView imThree, ImageView imFour) {
        int ge = num % 10;
        int shi = (num / 10) % 10;
        int bai = (num / 100) % 10;
        int qian = num / 1000;
        imOne.setImageResource(bitmapArr[ge]);
        imTwo.setImageResource(bitmapArr[shi]);
        imThree.setImageResource(bitmapArr[bai]);
        imFour.setImageResource(bitmapArr[qian]);
    }
    public static void numberBlaueToBItmapFour(int num, ImageView imOne, ImageView imTwo, ImageView imThree, ImageView imFour) {
        int ge = num % 10;
        int shi = (num / 10) % 10;
        int bai = (num / 100) % 10;
        int qian = num / 1000;
        imOne.setImageResource(blueImage[ge]);
        imTwo.setImageResource(blueImage[shi]);
        imThree.setImageResource(blueImage[bai]);
        imFour.setImageResource(blueImage[qian]);
    }

    public static void numberToBItmapFive(int num, ImageView imOne, ImageView imTwo, ImageView imThree, ImageView imFour, ImageView imFive) {
        int ge = num % 10;
        int shi = (num / 10) % 10;
        int bai = (num / 100) % 10;
        int qian = (num / 1000) % 10;
        int wan = num / 10000;
        imOne.setImageResource(bitmapArr[ge]);
        imTwo.setImageResource(bitmapArr[shi]);
        imThree.setImageResource(bitmapArr[bai]);
        imFour.setImageResource(bitmapArr[qian]);
        imFive.setImageResource(bitmapArr[wan]);
    }

    public static void numberToBItmapSix(int num, ImageView imOne, ImageView imTwo, ImageView imThree, ImageView imFour, ImageView imFive, ImageView imSix) {
        int ge = num % 10;
        int shi = (num / 10) % 10;
        int bai = (num / 100) % 10;
        int qian = (num / 1000) % 10;
        int wan = (num / 10000) % 10;
        int shiwan = num / 100000;
        imOne.setImageResource(bitmapArr[ge]);
        imTwo.setImageResource(bitmapArr[shi]);
        imThree.setImageResource(bitmapArr[bai]);
        imFour.setImageResource(bitmapArr[qian]);
        imFive.setImageResource(bitmapArr[wan]);
        imSix.setImageResource(bitmapArr[shiwan]);
    }
    public static void numberToBItmapBlaueSix(int num, ImageView imOne, ImageView imTwo, ImageView imThree, ImageView imFour, ImageView imFive, ImageView imSix) {
        int ge = num % 10;
        int shi = (num / 10) % 10;
        int bai = (num / 100) % 10;
        int qian = (num / 1000) % 10;
        int wan = (num / 10000) % 10;
        int shiwan = num / 100000;
        imOne.setImageResource(blueImage[ge]);
        imTwo.setImageResource(blueImage[shi]);
        imThree.setImageResource(blueImage[bai]);
        imFour.setImageResource(blueImage[qian]);
        imFive.setImageResource(blueImage[wan]);
        imSix.setImageResource(blueImage[shiwan]);
    }

    public static void numberToBItmapSeven(int num, ImageView imOne, ImageView imTwo, ImageView imThree, ImageView imFour,
                                           ImageView imFive, ImageView imSix, ImageView imSeven) {
        int ge = num % 10;
        int shi = (num / 10) % 10;
        int bai = (num / 100) % 10;
        int qian = (num / 1000) % 10;
        int wan = (num / 10000) % 10;
        int shiwan = (num / 100000) % 10;
        int baiwan = num / 1000000;
        imOne.setImageResource(bitmapArr[ge]);
        imTwo.setImageResource(bitmapArr[shi]);
        imThree.setImageResource(bitmapArr[bai]);
        imFour.setImageResource(bitmapArr[qian]);
        imFive.setImageResource(bitmapArr[wan]);
        imSix.setImageResource(bitmapArr[shiwan]);
        imSeven.setImageResource(bitmapArr[baiwan]);
    }

    public static void numberToBItmapEight(int num, ImageView imOne, ImageView imTwo, ImageView imThree, ImageView imFour,
                                           ImageView imFive, ImageView imSix, ImageView imSeven, ImageView imEight) {
        int ge = num % 10;
        int shi = (num / 10) % 10;
        int bai = (num / 100) % 10;
        int qian = (num / 1000) % 10;
        int wan = (num / 10000) % 10;
        int shiwan = (num / 100000) % 10;
        int baiwan = (num / 1000000) % 10;
        int qianwan = num / 10000000;
        imOne.setImageResource(bitmapArr[ge]);
        imTwo.setImageResource(bitmapArr[shi]);
        imThree.setImageResource(bitmapArr[bai]);
        imFour.setImageResource(bitmapArr[qian]);
        imFive.setImageResource(bitmapArr[wan]);
        imSix.setImageResource(bitmapArr[shiwan]);
        imSeven.setImageResource(bitmapArr[baiwan]);
        imEight.setImageResource(bitmapArr[qianwan]);
    }

    public static void numberToBItmapOne(int num, ImageView imOne) {
        imOne.setImageResource(bitmapArr[num]);
    }
    public static void setEquipmentNumber(String id,ImageView imOne, ImageView imTwo, ImageView imThree, ImageView imFour,
                                          ImageView imFive, ImageView imSix, ImageView imSeven, ImageView imEight){
        imOne.setImageResource(bitmapArr[Integer.parseInt(id.charAt(0)+"")]);
        imTwo.setImageResource(bitmapArr[Integer.parseInt(id.charAt(1)+"")]);
        imThree.setImageResource(bitmapArr[Integer.parseInt(id.charAt(2)+"")]);
        imFour.setImageResource(bitmapArr[Integer.parseInt(id.charAt(3)+"")]);
        imFive.setImageResource(bitmapArr[Integer.parseInt(id.charAt(4)+"")]);
        imSix.setImageResource(bitmapArr[Integer.parseInt(id.charAt(5)+"")]);
        imSeven.setImageResource(bitmapArr[Integer.parseInt(id.charAt(6)+"")]);
        imEight.setImageResource(bitmapArr[Integer.parseInt(id.charAt(7)+"")]);
    }
    public static void numberToBItmapOneRed(int num, ImageView imOne) {
        imOne.setImageResource(bitmapRedArr[num]);
    }
    public static void numberToBItmapTwoRed(int num, ImageView imOne, ImageView imTwo) {
        if (num < 10) {
            imOne.setImageResource(bitmapRedArr[0]);
            imTwo.setImageResource(bitmapRedArr[num]);
        } else {
            int ge = num % 10;
            int shi = num / 10;
            imTwo.setImageResource(bitmapRedArr[ge]);
            imOne.setImageResource(bitmapRedArr[shi]);
        }
    }
    public static void numberToBItmapThreeRed(int num, ImageView imOne, ImageView imTwo, ImageView imThree) {
        int ge = num % 10;
        int shi = (num / 10) % 10;
        int bai = num / 100;
        imOne.setImageResource(bitmapRedArr[ge]);
        imTwo.setImageResource(bitmapRedArr[shi]);
        imThree.setImageResource(bitmapRedArr[bai]);
    }
    public static void numberToBItmapFourRed(int num, ImageView imOne, ImageView imTwo, ImageView imThree, ImageView imFour) {
        int ge = num % 10;
        int shi = (num / 10) % 10;
        int bai = (num / 100) % 10;
        int qian = num / 1000;
        imOne.setImageResource(bitmapRedArr[ge]);
        imTwo.setImageResource(bitmapRedArr[shi]);
        imThree.setImageResource(bitmapRedArr[bai]);
        imFour.setImageResource(bitmapRedArr[qian]);
    }
    public static void numberToBItmapFiveRed(int num, ImageView imOne, ImageView imTwo, ImageView imThree, ImageView imFour, ImageView imFive) {
        int ge = num % 10;
        int shi = (num / 10) % 10;
        int bai = (num / 100) % 10;
        int qian = (num / 1000) % 10;
        int wan = num / 10000;
        imOne.setImageResource(bitmapRedArr[ge]);
        imTwo.setImageResource(bitmapRedArr[shi]);
        imThree.setImageResource(bitmapRedArr[bai]);
        imFour.setImageResource(bitmapRedArr[qian]);
        imFive.setImageResource(bitmapRedArr[wan]);
    }
    public static void numberToBItmapSixRed(int num, ImageView imOne, ImageView imTwo, ImageView imThree, ImageView imFour, ImageView imFive, ImageView imSix) {
        int ge = num % 10;
        int shi = (num / 10) % 10;
        int bai = (num / 100) % 10;
        int qian = (num / 1000) % 10;
        int wan = (num / 10000) % 10;
        int shiwan = num / 100000;
        imOne.setImageResource(bitmapRedArr[ge]);
        imTwo.setImageResource(bitmapRedArr[shi]);
        imThree.setImageResource(bitmapRedArr[bai]);
        imFour.setImageResource(bitmapRedArr[qian]);
        imFive.setImageResource(bitmapRedArr[wan]);
        imSix.setImageResource(bitmapRedArr[shiwan]);
    }
    public static void numberToBItmapSevenRed(int num, ImageView imOne, ImageView imTwo, ImageView imThree, ImageView imFour,
                                           ImageView imFive, ImageView imSix, ImageView imSeven) {
        int ge = num % 10;
        int shi = (num / 10) % 10;
        int bai = (num / 100) % 10;
        int qian = (num / 1000) % 10;
        int wan = (num / 10000) % 10;
        int shiwan = (num / 100000) % 10;
        int baiwan = num / 1000000;
        imOne.setImageResource(bitmapRedArr[ge]);
        imTwo.setImageResource(bitmapRedArr[shi]);
        imThree.setImageResource(bitmapRedArr[bai]);
        imFour.setImageResource(bitmapRedArr[qian]);
        imFive.setImageResource(bitmapRedArr[wan]);
        imSix.setImageResource(bitmapRedArr[shiwan]);
        imSeven.setImageResource(bitmapRedArr[baiwan]);
    }
    public static void numberToBItmapEightRed(int num, ImageView imOne, ImageView imTwo, ImageView imThree, ImageView imFour,
                                           ImageView imFive, ImageView imSix, ImageView imSeven, ImageView imEight) {
        int ge = num % 10;
        int shi = (num / 10) % 10;
        int bai = (num / 100) % 10;
        int qian = (num / 1000) % 10;
        int wan = (num / 10000) % 10;
        int shiwan = (num / 100000) % 10;
        int baiwan = (num / 1000000) % 10;
        int qianwan = num / 10000000;
        imOne.setImageResource(bitmapRedArr[ge]);
        imTwo.setImageResource(bitmapRedArr[shi]);
        imThree.setImageResource(bitmapRedArr[bai]);
        imFour.setImageResource(bitmapRedArr[qian]);
        imFive.setImageResource(bitmapRedArr[wan]);
        imSix.setImageResource(bitmapRedArr[shiwan]);
        imSeven.setImageResource(bitmapRedArr[baiwan]);
        imEight.setImageResource(bitmapRedArr[qianwan]);
    }
}
