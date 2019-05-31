package com.E8908.base;

import com.E8908.db.DaoQueryBean;

public interface QuerySuccess {
    void onSuccess(DaoQueryBean bean);
    void onUpdataSuccess(int result);
    void onDeleteSuccess(int result);
    void onInsertSuccess(int result);

}
