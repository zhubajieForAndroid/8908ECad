package com.E8908.db;

import com.E8908.base.QuerySuccess;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DaoUtil{
    private GasDataBaseDao mGasDataBaseDao;
    private ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private QuerySuccess mQuerySuccess;
    public DaoUtil (GasDataBaseDao dataBaseDao,QuerySuccess success){
        mGasDataBaseDao = dataBaseDao;
        mQuerySuccess = success;
    }
    public void insert(Map<String, String> datas) {
        DaoThread daoThread = new DaoThread(mGasDataBaseDao,1,datas,mQuerySuccess);
        mExecutor.execute(daoThread);
    }
    public void upData(Map<String, String> datas) {
        DaoThread daoThread = new DaoThread(mGasDataBaseDao,2,datas,mQuerySuccess);
        mExecutor.execute(daoThread);
    }
    public void delete() {
        DaoThread daoThread = new DaoThread(mGasDataBaseDao,3,null,mQuerySuccess);
        mExecutor.execute(daoThread);
    }
    public void query() {
        DaoThread daoThread = new DaoThread(mGasDataBaseDao,4,null,mQuerySuccess);
        mExecutor.execute(daoThread);
    }
}
