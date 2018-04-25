package com.devband.tronwalletforandroid.ui.intro;

import android.annotation.SuppressLint;

import com.devband.tronwalletforandroid.database.model.WalletModel;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import java.util.List;
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

        Single.fromCallable(() -> {
            List<WalletModel> walletList = Tron.getInstance(mContext).loadWallWallets();

            return walletList;
        })
        .delay(3, TimeUnit.SECONDS)
        .subscribe(result -> {
            if (result != null && !result.isEmpty()) {
                // todo - create wallet activity
                // mView.startMainActivity();
            } else {
                // todo - login wallet activity
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
