package com.E8908.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.E8908.conf.Constants;

public class GasOpenHelper extends SQLiteOpenHelper {
    public GasOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + Constants.TB_CAR_CHECK_REPORT + " (_id  VARCHAR2(32)," +
                "  beforewpbfscore      VARCHAR2(10),beforewpbfpicurl     VARCHAR2(128),\n" +
                "  afterwpbfscore       VARCHAR2(10),\n" +
                "  afterwpbfpicurl      VARCHAR2(128),\n" +
                "  beforekqlxscore      VARCHAR2(10),\n" +
                "  beforekqlxpicurl     VARCHAR2(128),\n" +
                "  afterkqlxscore       VARCHAR2(10),\n" +
                "  afterkqlxpicurl      VARCHAR2(128),\n" +
                "  beforecnfcwrscore    VARCHAR2(10),\n" +
                "  beforecnfcwrpicurl   VARCHAR2(128),\n" +
                "  aftercnfcwrscore     VARCHAR2(10),\n" +
                "  aftercnfcwrpicurl    VARCHAR2(128),\n" +
                "  beforexdsjscore      VARCHAR2(10),\n" +
                "  beforexdsjpicurl     VARCHAR2(128),\n" +
                "  afterxdsjscore       VARCHAR2(10),\n" +
                "  afterxdsjpicurl      VARCHAR2(128),\n" +
                "  beforeswczscore      VARCHAR2(10),\n" +
                "  beforeswczpicurl     VARCHAR2(128),\n" +
                "  afterswczscore       VARCHAR2(10),\n" +
                "  afterswczpicurl      VARCHAR2(128),\n" +
                "  beforekqjhscore      VARCHAR2(10),\n" +
                "  beforekqjhpicurl     VARCHAR2(128),\n" +
                "  afterkqjhscore       VARCHAR2(10),\n" +
                "  afterkqjhpicurl      VARCHAR2(128),\n" +
                "  beforensjjdscore     VARCHAR2(10),\n" +
                "  beforensjjdpicurl    VARCHAR2(128),\n" +
                "  afternsjjdscore      VARCHAR2(10),\n" +
                "  afternsjjdpicurl     VARCHAR2(128),\n" +
                "  beforecnjxscore      VARCHAR2(10),\n" +
                "  beforecnjxpicurl     VARCHAR2(128),\n" +
                "  aftercnjxscore       VARCHAR2(10),\n" +
                "  aftercnjxpicurl      VARCHAR2(128),\n" +
                "  beforelxbmwgscore    VARCHAR2(10),\n" +
                "  beforelxbmwgpicurl   VARCHAR2(128),\n" +
                "  afterlxbmwgscore     VARCHAR2(10),\n" +
                "  afterlxbmwgpicurl    VARCHAR2(128),\n" +
                "  beforezfxbmwgscore   VARCHAR2(10),\n" +
                "  beforezfxbmwgpicurl  VARCHAR2(128),\n" +
                "  afterzfxbmwgscore    VARCHAR2(10),\n" +
                "  afterzfxbmwgpicurl   VARCHAR2(128),\n" +
                "  beforegfjbmwgscore   VARCHAR2(10),\n" +
                "  beforegfjbmwgpicurl  VARCHAR2(128),\n" +
                "  aftergfjbmwgscore    VARCHAR2(10),\n" +
                "  aftergfjbmwgpicurl   VARCHAR2(128),\n" +
                "  beforetfgdbmwgscore  VARCHAR2(10),\n" +
                "  beforetfgdbmwgpicurl VARCHAR2(128),\n" +
                "  aftertfgdbmwgscore   VARCHAR2(10),\n" +
                "  aftertfgdbmwgpicurl  VARCHAR2(128),\n" +
                "  beforelnqbmwgscore   VARCHAR2(10),\n" +
                "  beforelnqbmwgpicurl  VARCHAR2(128),\n" +
                "  afterlnqbmwgscore    VARCHAR2(10),\n" +
                "  afterlnqbmwgpicurl   VARCHAR2(128),\n" +
                "  beforektxtzlxnscore  VARCHAR2(10),\n" +
                "  beforektxtzlxnpicurl VARCHAR2(128),\n" +
                "  afterktxtzlxnscore   VARCHAR2(10),\n" +
                "  afterktxtzlxnpicurl  VARCHAR2(128),\n" +
                "  beforektkqyxscore    VARCHAR2(10),\n" +
                "  beforektkqyxpicurl   VARCHAR2(128),\n" +
                "  afterktkqyxscore     VARCHAR2(10),\n" +
                "  afterktkqyxpicurl    VARCHAR2(128),\n" +
                "  beforecfkyw          VARCHAR2(10),\n" +
                "  aftercfkyw           VARCHAR2(10),\n" +
                "  beforescwpyw         VARCHAR2(10),\n" +
                "  afterscwpyw          VARCHAR2(10),\n" +
                "  beforexcyw           VARCHAR2(10),\n" +
                "  afterxcyw            VARCHAR2(10),\n" +
                "  beforeesyyw          VARCHAR2(10),\n" +
                "  afteresyyw           VARCHAR2(10),\n" +
                "  beforenspgyw         VARCHAR2(10),\n" +
                "  afternspgyw         VARCHAR2(10),\n" +
                "  beforexjmbyw         VARCHAR2(10),\n" +
                "  afterxjmbyw          VARCHAR2(10),\n" +
                "  beforensxfyw         VARCHAR2(10),\n" +
                "  afternsxfyw          VARCHAR2(10),\n" +
                "  beforeqtyw           VARCHAR2(10),\n" +
                "  afterqtyw            VARCHAR2(10),\n" +
                "  beforejq             VARCHAR2(10),\n" +
                "  afterjq              VARCHAR2(10),\n" +
                "  beforetvoc           VARCHAR2(10),\n" +
                "  aftertvoc            VARCHAR2(10),\n" +
                "  beforepm25           VARCHAR2(10),\n" +
                "  afterpm25            VARCHAR2(10),\n" +
                "  beforewd             VARCHAR2(10),\n" +
                "  afterwd              VARCHAR2(10),\n" +
                "  beforesd             VARCHAR2(10),\n" +
                "  aftersd              VARCHAR2(10),\n" +
                "  carnumber            VARCHAR2(32))";
        db.execSQL(sql);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
