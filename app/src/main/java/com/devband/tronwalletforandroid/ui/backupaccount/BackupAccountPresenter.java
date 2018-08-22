package com.devband.tronwalletforandroid.ui.backupaccount;

import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class BackupAccountPresenter extends BasePresenter<BackupAccountView> {

    private Tron mTron;
    private WalletAppManager mWalletAppManager;
    private Scheduler mProcessScheduler;
    private Scheduler mObserverScheduler;

    public BackupAccountPresenter(BackupAccountView view, Tron tron, WalletAppManager walletAppManager,
            Scheduler processScheduler, Scheduler observerScheduler) {
        super(view);
        this.mTron = tron;
        this.mWalletAppManager = walletAppManager;
        this.mProcessScheduler = processScheduler;
        this.mObserverScheduler = observerScheduler;
    }

    @Override
    public void onCreate() {
        String address = mTron.getLoginAddress();
        String privateKey = mTron.getLoginPrivateKey();

        mView.displayAccountInfo(address, privateKey);
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
        .subscribeOn(mProcessScheduler)
        .observeOn(mObserverScheduler)
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
}
