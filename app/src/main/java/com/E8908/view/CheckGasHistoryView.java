package com.E8908.view;

public interface CheckGasHistoryView<T> {
    void onFaild(String msg);
    void onSuccess(T result);

}
