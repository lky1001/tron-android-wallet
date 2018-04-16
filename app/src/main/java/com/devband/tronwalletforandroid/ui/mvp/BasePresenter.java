package com.devband.tronwalletforandroid.ui.mvp;

public abstract class BasePresenter<T extends IView> {

    protected T mView;

    public BasePresenter(T view) {
        this.mView = view;
    }

    public abstract void onCreate();
    public abstract void onPause();
    public abstract void onResume();
    public abstract void onDestroy();

}
