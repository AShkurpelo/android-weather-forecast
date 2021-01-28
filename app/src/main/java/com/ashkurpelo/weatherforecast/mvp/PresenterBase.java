package com.ashkurpelo.weatherforecast.mvp;


public class PresenterBase<T extends MvpView> implements MvpPresenter<T> {
    private T view;
    @Override
    public void attachView(T view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public T getView(){
        return view;
    }

    public boolean isViewAttached(){
        return  view != null;
    }

    @Override
    public void destroy() {
        this.view = null;
    }
}
