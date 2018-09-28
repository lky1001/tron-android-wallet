package com.devband.tronwalletforandroid.ui.login;

import android.support.annotation.Nullable;

import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class LoginPresenter extends BasePresenter<LoginView> {

    private Tron mTron;
    private WalletAppManager mWalletAppManager;
    private RxJavaSchedulers mRxJavaSchedulers;

    public LoginPresenter(LoginView view, Tron tron, WalletAppManager walletAppManager,
            RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mTron = tron;
        this.mWalletAppManager = walletAppManager;
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
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers.getMainThread())
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
