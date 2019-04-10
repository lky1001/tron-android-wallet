package com.devband.tronwalletforandroid.ui.balancecheck;

import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

public class BalanceCheckingPresenter extends BasePresenter<BalanceCheckingView> {

    private RxJavaSchedulers mRxJavaSchedulers;

    public BalanceCheckingPresenter(BalanceCheckingView view, RxJavaSchedulers rxJavaSchedulers) {
        super(view);
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
