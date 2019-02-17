package com.devband.tronwalletforandroid.ui.sendtrc20;

import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

public class SendTrc20Presenter extends BasePresenter<SendTrc20View> {

    private Tron mTron;
    private RxJavaSchedulers mRxJavaSchedulers;

    public SendTrc20Presenter(SendTrc20View view, Tron tron, RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mTron = tron;
        this.mRxJavaSchedulers = rxJavaSchedulers;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }
}
