package com.devband.tronwalletforandroid.ui.backupaccount;

import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class BackupAccountPresenter extends BasePresenter<BackupAccountView> {

    private Tron mTron;
    private WalletAppManager mWalletAppManager;
    private RxJavaSchedulers mRxJavaSchedulers;

    public BackupAccountPresenter(BackupAccountView view, Tron tron, WalletAppManager walletAppManager,
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

    public void agreeTerms(boolean isAgree) {
        Single.fromCallable(() -> {
            mWalletAppManager.agreeTerms(true);
            return true;
        })
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers.getMainThread())
        .subscribe(new SingleObserver<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                mView.startMainActivity();
            }

            @Override
            public void onError(Throwable e) {
                mView.startMainActivity();
            }
        });
    }

    public void getAccountAndPrivateKey(byte[] aesKey) {
        String address = mTron.getLoginAddress();
        String privateKey = mTron.getLoginPrivateKey(aesKey);

        mView.displayAccountInfo(address, privateKey);
    }
}
