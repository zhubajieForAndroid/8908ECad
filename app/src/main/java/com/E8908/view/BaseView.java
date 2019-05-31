package com.E8908.view;

public interface BaseView<T> {
    void onFaild(String msg);
    void onSuccess(T result);
    void onNoData(String msg);
}
