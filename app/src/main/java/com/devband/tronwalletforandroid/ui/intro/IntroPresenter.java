package com.devband.tronwalletforandroid.ui.intro;

import android.annotation.SuppressLint;

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
        // todo - app init

        Single.fromCallable(() -> true)
                .delay(3, TimeUnit.SECONDS)
                .subscribe(result -> {
                    if (result) {
                        mView.startMainActivity();
                    } else {
                        // todo - failed app init
                    }
                }, e -> {
                    // todo - failed app init
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
