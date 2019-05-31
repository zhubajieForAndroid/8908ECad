package com.E8908.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.E8908.conf.Constants;

import java.util.Map;

public class GasDataBaseDao {
    private static final String TAG = "GasDataBaseDao";
    private GasOpenHelper mGasOpenHelper;

    public GasDataBaseDao(Context context) {
        mGasOpenHelper = new GasOpenHelper(context, Constants.DAO_NAME, null, 1);
    }

    public long insert(Map<String, String> datas) {
        SQLiteDatabase writableDatabase = mGasOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (Map.Entry<String, String> data : datas.entrySet()) {
            values.put(data.getKey(), data.getValue());
        }
        long insert = writableDatabase.insert(Constants.TB_CAR_CHECK_REPORT, null, values);
        writableDatabase.close();
        return insert;
    }

    public int upData(Map<String, String> datas) {
        SQLiteDatabase writableDatabase = mGasOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (Map.Entry<String, String> data : datas.entrySet()) {
            values.put(data.getKey(), data.getValue());
        }
        int update = writableDatabase.update(Constants.TB_CAR_CHECK_REPORT, values, "_id=?", new String[]{Constants._ID});
        writableDatabase.close();
        return update;
    }

    public int delete() {
        SQLiteDatabase writableDatabase = mGasOpenHelper.getWritableDatabase();
        int delete = writableDatabase.delete(Constants.TB_CAR_CHECK_REPORT, null, null);
        writableDatabase.close();
        return delete;
    }

    public DaoQueryBean query() {
        SQLiteDatabase readableDatabase = mGasOpenHelper.getReadableDatabase();
        //查询所有行和列的数据
        Cursor query = readableDatabase.query(Constants.TB_CAR_CHECK_REPORT, null, null, null, null, null, null);
        DaoQueryBean bean = new DaoQueryBean();
        while (query.moveToNext()) {
            bean.beforeWpbfScore = query.getString(query.getColumnIndex("beforewpbfscore"));
            bean.beforeWpbfPicUrl = query.getString(query.getColumnIndex("beforewpbfpicurl"));
            bean.afterWpbfScore = query.getString(query.getColumnIndex("afterwpbfscore"));
            bean.afterWpbfPicUrl = query.getString(query.getColumnIndex("afterwpbfpicurl"));
            bean.beforeKqlxScore = query.getString(query.getColumnIndex("beforekqlxscore"));
            bean.beforeKqlxPicUrl = query.getString(query.getColumnIndex("beforekqlxpicurl"));
            bean.afterKqlxScore = query.getString(query.getColumnIndex("afterkqlxscore"));
            bean.afterKqlxPicUrl = query.getString(query.getColumnIndex("afterkqlxpicurl"));
            bean.beforeNsjjdScore = query.getString(query.getColumnIndex("beforensjjdscore"));
            bean.beforeNsjjdPicUrl = query.getString(query.getColumnIndex("beforensjjdpicurl"));
            bean.afterNsjjdScore = query.getString(query.getColumnIndex("afternsjjdscore"));
            bean.afterNsjjdPicUrl = query.getString(query.getColumnIndex("afternsjjdpicurl"));
            bean.beforeSwczScore = query.getString(query.getColumnIndex("beforeswczscore"));
            bean.beforeSwczPicUrl = query.getString(query.getColumnIndex("beforeswczpicurl"));
            bean.afterSwczScore = query.getString(query.getColumnIndex("afterswczscore"));
            bean.afterSwczPicUrl = query.getString(query.getColumnIndex("afterswczpicurl"));
            bean.beforeCnfcwrScore = query.getString(query.getColumnIndex("beforecnfcwrscore"));
            bean.beforeCnfcwrPicUrl = query.getString(query.getColumnIndex("beforecnfcwrpicurl"));
            bean.afterCnfcwrScore = query.getString(query.getColumnIndex("aftercnfcwrscore"));
            bean.afterCnfcwrPicUrl = query.getString(query.getColumnIndex("aftercnfcwrpicurl"));
            bean.beforeXdsjScore = query.getString(query.getColumnIndex("beforexdsjscore"));
            bean.beforeXdsjPicUrl = query.getString(query.getColumnIndex("beforexdsjpicurl"));
            bean.afterXdsjScore = query.getString(query.getColumnIndex("afterxdsjscore"));
            bean.afterXdsjPicUrl = query.getString(query.getColumnIndex("afterxdsjpicurl"));
            bean.beforeKqjhScore = query.getString(query.getColumnIndex("beforekqjhscore"));
            bean.beforeKqjhPicUrl = query.getString(query.getColumnIndex("beforekqjhpicurl"));
            bean.afterKqjhScore = query.getString(query.getColumnIndex("afterkqjhscore"));
            bean.afterKqjhPicUrl = query.getString(query.getColumnIndex("afterkqjhpicurl"));
            bean.beforeCnjxScore = query.getString(query.getColumnIndex("beforecnjxscore"));
            bean.beforeCnjxPicUrl = query.getString(query.getColumnIndex("beforecnjxpicurl"));
            bean.afterCnjxScore = query.getString(query.getColumnIndex("aftercnjxscore"));
            bean.afterCnjxPicUrl = query.getString(query.getColumnIndex("aftercnjxpicurl"));

            bean.beforeLxbmwgScore = query.getString(query.getColumnIndex("beforelxbmwgscore"));
            bean.beforeLxbmwgPicUrl = query.getString(query.getColumnIndex("beforelxbmwgpicurl"));
            bean.afterLxbmwgScore = query.getString(query.getColumnIndex("afterlxbmwgscore"));
            bean.afterLxbmwgPicUrl = query.getString(query.getColumnIndex("afterlxbmwgpicurl"));
            bean.beforeZfxbmwgScore = query.getString(query.getColumnIndex("beforezfxbmwgscore"));
            bean.beforeZfxbmwgPicUrl = query.getString(query.getColumnIndex("beforezfxbmwgpicurl"));
            bean.afterZfxbmwgScore = query.getString(query.getColumnIndex("afterzfxbmwgscore"));
            bean.afterZfxbmwgPicUrl = query.getString(query.getColumnIndex("afterzfxbmwgpicurl"));
            bean.beforeGfjbmwgScore = query.getString(query.getColumnIndex("beforegfjbmwgscore"));
            bean.beforeGfjbmwgPicUrl = query.getString(query.getColumnIndex("beforegfjbmwgpicurl"));
            bean.afterGfjbmwgScore = query.getString(query.getColumnIndex("aftergfjbmwgscore"));
            bean.afterGfjbmwgPicUrl = query.getString(query.getColumnIndex("aftergfjbmwgpicurl"));
            bean.beforeTfgdbmwgScore = query.getString(query.getColumnIndex("beforetfgdbmwgscore"));
            bean.beforeTfgdbmwgPicUrl = query.getString(query.getColumnIndex("beforetfgdbmwgpicurl"));
            bean.afterTfgdbmwgScore = query.getString(query.getColumnIndex("aftertfgdbmwgscore"));
            bean.afterTfgdbmwgPicUrl = query.getString(query.getColumnIndex("aftertfgdbmwgpicurl"));
            bean.beforeLnqbmwgScore = query.getString(query.getColumnIndex("beforelnqbmwgscore"));
            bean.beforeLnqbmwgPicUrl = query.getString(query.getColumnIndex("beforelnqbmwgpicurl"));
            bean.afterLnqbmwgScore = query.getString(query.getColumnIndex("afterlnqbmwgscore"));
            bean.afterLnqbmwgPicUrl = query.getString(query.getColumnIndex("afterlnqbmwgpicurl"));
            bean.beforeKtxtzlxnScore = query.getString(query.getColumnIndex("beforektxtzlxnscore"));
            bean.beforeKtxtzlxnPicUrl = query.getString(query.getColumnIndex("beforektxtzlxnpicurl"));
            bean.afterKtxtzlxnScore = query.getString(query.getColumnIndex("afterktxtzlxnscore"));
            bean.afterKtxtzlxnPicUrl = query.getString(query.getColumnIndex("afterktxtzlxnpicurl"));
            bean.beforeKtkqyxScore = query.getString(query.getColumnIndex("beforektkqyxscore"));
            bean.beforeKtkqyxPicUrl = query.getString(query.getColumnIndex("beforektkqyxpicurl"));
            bean.afterKtkqyxScore = query.getString(query.getColumnIndex("afterktkqyxscore"));
            bean.afterKtkqyxPicUrl = query.getString(query.getColumnIndex("afterktkqyxpicurl"));

            bean.beforeCfkyw = query.getString(query.getColumnIndex("beforecfkyw"));
            bean.afterCfkyw = query.getString(query.getColumnIndex("aftercfkyw"));
            bean.beforeXcyw = query.getString(query.getColumnIndex("beforexcyw"));
            bean.afterXcyw = query.getString(query.getColumnIndex("afterxcyw"));
            bean.beforeNspgyw = query.getString(query.getColumnIndex("beforenspgyw"));
            bean.afterNspgyw = query.getString(query.getColumnIndex("afternspgyw"));
            bean.beforeNsxfyw = query.getString(query.getColumnIndex("beforensxfyw"));
            bean.afterNsxfyw = query.getString(query.getColumnIndex("afternsxfyw"));
            bean.beforeScwpyw = query.getString(query.getColumnIndex("beforescwpyw"));
            bean.afterScwpyw = query.getString(query.getColumnIndex("afterscwpyw"));
            bean.beforeEsyyw = query.getString(query.getColumnIndex("beforeesyyw"));
            bean.afterEsyyw = query.getString(query.getColumnIndex("afteresyyw"));

            bean.beforeXjmbyw = query.getString(query.getColumnIndex("beforexjmbyw"));
            bean.afterXjmbyw = query.getString(query.getColumnIndex("afterxjmbyw"))
            ;
            bean.beforeQtyw = query.getString(query.getColumnIndex("beforeqtyw"));
            bean.afterQtyw = query.getString(query.getColumnIndex("afterqtyw"));

            bean.beforeJq = query.getString(query.getColumnIndex("beforejq"));
            bean.afterJq = query.getString(query.getColumnIndex("afterjq"));
            bean.beforeTvoc = query.getString(query.getColumnIndex("beforetvoc"));
            bean.afterTvoc = query.getString(query.getColumnIndex("aftertvoc"));
            bean.beforePm25 = query.getString(query.getColumnIndex("beforepm25"));
            bean.afterPm25 = query.getString(query.getColumnIndex("afterpm25"));
            bean.beforeWd = query.getString(query.getColumnIndex("beforewd"));
            bean.afterWd = query.getString(query.getColumnIndex("afterwd"));
            bean.beforeSd = query.getString(query.getColumnIndex("beforesd"));
            bean.afterSd = query.getString(query.getColumnIndex("aftersd"));
            bean.carNumber = query.getString(query.getColumnIndex("carnumber"));
        }
        query.close();
        readableDatabase.close();
        return bean;
    }
}
