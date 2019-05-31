package com.E8908.blueTooth.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.E8908.conf.Constants;

public class YunOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "YunOpenHelper";

    public YunOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + Constants.TB_CAR_YUN_REPORT + " (_id text,carnumber text,storeName text,orgPk text,projectName text," +
                "  checkTechnician text," +
                "  constructionTechnician text," +
                "  receiveCarPk text," +
                "  checkTypePk  text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
