package com.ashkurpelo.weatherforecast.mvp;

public interface MvpPresenter<T extends MvpView>{
    void attachView(T v);
    void detachView();
    void destroy();
}
