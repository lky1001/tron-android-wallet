package com.devband.tronwalletforandroid.ui.login;

import android.support.annotation.Nullable;

import com.devband.tronwalletforandroid.tron.WalletAppManager;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter extends BasePresenter<LoginView> {

    private Tron mTron;
    private WalletAppManager mWalletAppManager;
    private Scheduler mProcessScheduler;
    private Scheduler mObserverScheduler;

    public LoginPresenter(LoginView view, Tron tron, WalletAppManager walletAppManager,
            Scheduler io, Scheduler scheduler) {
        super(view);
        this.mTron = tron;
        this.mWalletAppManager = walletAppManager;
        this.mProcessScheduler = io;
        this.mObserverScheduler = scheduler;
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

    public void loginWallet(@Nullable String password) {
        Single.fromCallable(() -> {
            int result = mWalletAppManager.login(password);

            if (result == WalletAppManager.SUCCESS) {
                int res = mTron.login(password);
                if (res != Tron.SUCCESS) {
                    return WalletAppManager.ERROR;
                }
            }

            return result;
        })
        .subscribeOn(mProcessScheduler)
        .observeOn(mObserverScheduler)
        .subscribe(new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Integer result) {
                mView.loginResult(result);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mView.loginResult(Tron.ERROR_LOGIN);
            }
        });
    }
}
