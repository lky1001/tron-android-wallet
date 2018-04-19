package com.devband.tronwalletforandroid.ui.mvp;

import android.content.Context;

public abstract class BasePresenter<T extends IView> {

    protected T mView;
    protected Context mContext;

    public BasePresenter(T view) {
        this.mView = view;
        this.mContext = (Context) view;
    }

    public abstract void onCreate();
    public abstract void onPause();
    public abstract void onResume();
    public abstract void onDestroy();

}
