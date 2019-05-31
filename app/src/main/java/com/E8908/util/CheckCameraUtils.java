package com.E8908.util;

import android.hardware.Camera;

/**
 * Created by dell on 2018/5/23.
 */

public class CheckCameraUtils {
     private static boolean checkCameraFacing(final int facing) {
        final int cameraCount = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, info);
            if (facing == info.facing) {
                return true;
            }
        }
        return false;
    }
    /**
     * 检查设备是否有摄像头
     * @return
     */
    public static boolean hasCamera() {
        return hasBackFacingCamera() || hasFrontFacingCamera();
    }

    /**检查设备是否有后置摄像头
     * @return
     */
    public static boolean hasBackFacingCamera() {
        final int CAMERA_FACING_BACK = 0;
        return checkCameraFacing(CAMERA_FACING_BACK);
    }

    /**检查设备是否有前置摄像头
     * @return
     */
    public static boolean hasFrontFacingCamera() {
        final int CAMERA_FACING_BACK = 1;
        return checkCameraFacing(CAMERA_FACING_BACK);
    }
}
