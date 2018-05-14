package com.devband.tronwalletforandroid.ui.backupaccount;

import com.devband.tronwalletforandroid.common.WalletAppManager;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BackupAccountPresenter extends BasePresenter<BackupAccountView> {

    public BackupAccountPresenter(BackupAccountView view) {
        super(view);
    }

    @Override
    public void onCreate() {
        String address = Tron.getInstance(mContext).getLoginAddress();
        String privateKey = Tron.getInstance(mContext).getLoginPrivateKey();

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
            WalletAppManager.getInstance(mContext).agreeTerms(true);
            return true;
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
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
