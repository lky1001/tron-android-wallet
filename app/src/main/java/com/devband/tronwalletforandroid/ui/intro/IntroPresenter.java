package com.devband.tronwalletforandroid.ui.intro;

import android.annotation.SuppressLint;

import com.devband.tronwalletforandroid.common.WalletAppManager;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class IntroPresenter extends BasePresenter<IntroView> {

    private static final int NO_WALLET = 0;
    private static final int NOT_AGREE = 1;
    private static final int SUCCESS = 2;

    public IntroPresenter(IntroView view) {
        super(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onCreate() {
        Single.fromCallable(() -> {
            if (WalletAppManager.getInstance(mContext).hasWallet()) {
                if (WalletAppManager.getInstance(mContext).isAgree()) {
                    return SUCCESS;
                } else {
                    return NOT_AGREE;
                }
            } else {
                return NO_WALLET;
            }
        })
        .subscribeOn(Schedulers.io())
        .delay(2, TimeUnit.SECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
            if (result == SUCCESS) {
                mView.startLoginActivity();
            } else if (result == NOT_AGREE) {
                mView.startBackupAccountActivity();
            } else if (result == NO_WALLET) {
                mView.startCreateAccountActivity();
            }
        }, e -> {
            mView.startCreateAccountActivity();
        });
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
