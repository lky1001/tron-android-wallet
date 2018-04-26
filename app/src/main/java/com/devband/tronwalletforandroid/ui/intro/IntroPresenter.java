package com.devband.tronwalletforandroid.ui.intro;

import android.annotation.SuppressLint;

import com.devband.tronwalletforandroid.tron.AccountManager;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;

public class IntroPresenter extends BasePresenter<IntroView> {

    public IntroPresenter(IntroView view) {
        super(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onCreate() {
        Single.fromCallable(() -> AccountManager.getInstance(mContext).hasAccount())
        .delay(2, TimeUnit.SECONDS)
        .subscribe(result -> {
            if (result) {
                mView.startLoginActivity();
            } else {
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
