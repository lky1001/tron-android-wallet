package com.devband.tronwalletforandroid.ui.createwallet;

import com.crashlytics.android.Crashlytics;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class CreateWalletPresenter extends BasePresenter<CreateWalletView> {

    private Tron mTron;
    private WalletAppManager mWalletAppManager;
    private RxJavaSchedulers mRxJavaSchedulers;

    public CreateWalletPresenter(CreateWalletView view, Tron tron, WalletAppManager walletAppManager,
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

    public void createWallet(String password) {
        Single.fromCallable(() -> {
            int result = mWalletAppManager.createWallet(password);

            if (result == WalletAppManager.SUCCESS) {
                result = mTron.registerAccount(Constants.PREFIX_ACCOUNT_NAME, password).blockingGet();
                if (result != Tron.SUCCESS) {
                    return result;
                }

                result = mTron.login(password);

                if (result != Tron.SUCCESS) {
                    return result;
                }

                mWalletAppManager.login(password);

                return Tron.SUCCESS;
            } else if (result == WalletAppManager.ERROR) {
                return Tron.ERROR;
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
                if (result == Tron.ERROR_INVALID_PASSWORD) {
                    mView.passwordError();
                } else if (result == Tron.ERROR) {
                    mView.passwordError();
                } else {
                    mView.createdWallet();
                }
            }

            @Override
            public void onError(Throwable e) {
                Crashlytics.logException(e);
                mView.registerWalletError();
            }
        });
    }
}
