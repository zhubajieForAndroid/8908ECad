package com.E8908.db;


import com.E8908.base.QuerySuccess;

import java.util.Map;



public class DaoThread extends Thread  {
    private GasDataBaseDao mGasDataBaseDao;
    private int mInt;
    private Map<String, String> mStringMap;
    private QuerySuccess mQuerySuccess;

    public DaoThread(GasDataBaseDao dataBaseDao, int state, Map<String, String> map, QuerySuccess success) {
        mGasDataBaseDao = dataBaseDao;
        mInt = state;
        mStringMap = map;
        mQuerySuccess = success;
    }

    @Override
    public void run() {
        super.run();
        switch (mInt) {
            case 1:
                if (mStringMap != null) {
                    int insert = (int) mGasDataBaseDao.insert(mStringMap);
                    mQuerySuccess.onInsertSuccess(insert);
                }
                break;
            case 2:
                if (mStringMap != null) {
                    int i = mGasDataBaseDao.upData(mStringMap);
                    mQuerySuccess.onUpdataSuccess(i);
                }
                break;
            case 3:
                int delete = mGasDataBaseDao.delete();
                mQuerySuccess.onDeleteSuccess(delete);
                break;
            case 4:
                DaoQueryBean resultBean = mGasDataBaseDao.query();
                mQuerySuccess.onSuccess(resultBean);
                break;
        }
    }

}
