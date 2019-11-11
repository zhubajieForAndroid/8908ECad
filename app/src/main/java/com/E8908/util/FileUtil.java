package com.E8908.util;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;

import com.E8908.base.MyApplication;
import com.E8908.conf.Constants;

import java.io.File;
import java.io.IOException;


/**
 * Created by dell on 2018/3/23.
 */

public class FileUtil {
    /**
     * 创建新文件
     * @param parent    文件夹名字
     * @param name      文件名字
     * @return
     */
    public static File createNewFile(String parent,String name){
        File file = new File(makeDirToSD(parent),name);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
    public static void deleteFile(File name){
        if (name != null && name.exists() && name.isFile()){
            name.delete();
        }
    }
    /**
     * 获取更新Apk的保存路径
     * @return
     */
    public static File getUpdataPath(){
        return new File(Environment.getExternalStorageDirectory(), Constants.UPLOAD_NAME + ".apk");
    }
    /**
     * 保存发送查询数据的标记
     * @param state
     */
    public static void putSendState(boolean state){
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences("send", 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("sendState",state);
        edit.apply();
    }

    /**
     * 取出发送查询数据的标记
     * @return
     */
    public static boolean getSendState(){
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences("send", 0);
        return sp.getBoolean("sendState", true);
    }

    /**
     * sd卡跟目录创建文件夹
     * @return
     */
    private static File makeDirToSD(String parent){
        File file = new File(getSDPath(),parent);
        if (!file.exists()){
            file.mkdir();
        }
        return file;
    }


    /**
     * 获取SD卡跟目录
     * @return
     */
    private static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);// 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        }
        return sdDir.toString();
    }

    public static String getFilePathByUri(Context context, Uri uri) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;

    }
    public static File getDownloadApkFile(){
        File path = new File(Environment.getExternalStorageDirectory(), "cad");
        if (!path.exists()) {
            path.mkdir();
        }
        return new File(path,Constants.DOWN_NAME + ".apk");
    }
    public static void deleteDownloadApkFile(){
        File downloadApkFile = getDownloadApkFile();
        if (downloadApkFile.exists()){
            downloadApkFile.delete();
        }
    }
    /**
     * 安装APK
     */
    public static void installApk(Context context, File loadFile) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PackageManager packageManager = context.getPackageManager();
            boolean installs = packageManager.canRequestPackageInstalls();
            if (installs) {
                intent.setAction(Intent.ACTION_INSTALL_PACKAGE);
                intent.setDataAndType(FileProvider.getUriForFile(context, "com.E8908.fileprovider", loadFile), "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(intent);
            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 111);
            }
        } else {
            intent.setAction("android.intent.action.VIEW");
            intent.setDataAndType(Uri.fromFile(loadFile), "application/vnd.android.package-archive");
            context.startActivity(intent);
        }
    }

    /**
     * 获取视频1本地地址
     * @return
     */
    public static String getVideoOnePath(){
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/info_one.mp4";
    }
    /**
     * 获取视频2本地地址
     * @return
     */
    public static String getVideoTwoPath(){
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/info_two.mp4";
    }
}
