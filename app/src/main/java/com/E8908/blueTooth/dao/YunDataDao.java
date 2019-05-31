package com.E8908.blueTooth.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.E8908.conf.Constants;

import java.util.Map;

public class YunDataDao {
    private  YunOpenHelper mHelper;
    public YunDataDao(Context context){
        mHelper = new YunOpenHelper(context,Constants.YUN_NAME,null,1);
    }
    public long insert(Map<String, String> datas) {
        SQLiteDatabase writableDatabase = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (Map.Entry<String, String> data : datas.entrySet()) {
            values.put(data.getKey(), data.getValue());
        }
        long insert = writableDatabase.insert(Constants.TB_CAR_YUN_REPORT, null, values);
        writableDatabase.close();
        return insert;
    }
    public int upData(Map<String, String> datas) {
        SQLiteDatabase writableDatabase = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (Map.Entry<String, String> data : datas.entrySet()) {
            values.put(data.getKey(), data.getValue());
        }
        int update = writableDatabase.update(Constants.TB_CAR_YUN_REPORT, values, "_id=?", new String[]{Constants.YUN_ID});
        writableDatabase.close();
        return update;
    }
    public ResultQueryYunBean query() {
        SQLiteDatabase readableDatabase = mHelper.getReadableDatabase();
        //查询所有行和列的数据
        Cursor query = readableDatabase.query(Constants.TB_CAR_YUN_REPORT, null, null, null, null, null, null);
        ResultQueryYunBean bean = new ResultQueryYunBean();
        while (query.moveToNext()) {
            bean.carnumber = query.getString(query.getColumnIndex("carnumber"));
            bean.storeName = query.getString(query.getColumnIndex("storeName"));
            bean.orgPk = query.getString(query.getColumnIndex("orgPk"));
            bean.projectName = query.getString(query.getColumnIndex("projectName"));
            bean.checkTechnician = query.getString(query.getColumnIndex("checkTechnician"));
            bean.constructionTechnician = query.getString(query.getColumnIndex("constructionTechnician"));
            bean.receiveCarPk = query.getString(query.getColumnIndex("receiveCarPk"));
            bean.checkTypePk = query.getString(query.getColumnIndex("checkTypePk"));
        }
        query.close();
        readableDatabase.close();
        return bean;
    }
    public int delete() {
        SQLiteDatabase writableDatabase = mHelper.getWritableDatabase();
        int delete = writableDatabase.delete(Constants.TB_CAR_YUN_REPORT, null, null);
        writableDatabase.close();
        return delete;
    }
}
