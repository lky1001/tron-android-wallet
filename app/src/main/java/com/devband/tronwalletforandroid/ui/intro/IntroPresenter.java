package com.devband.tronwalletforandroid.ui.intro;

import android.annotation.SuppressLint;
import android.util.Log;

import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import java.security.NoSuchAlgorithmException;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class IntroPresenter extends BasePresenter<IntroView> {

    private static final int NO_WALLET = 0;
    private static final int NOT_AGREE = 1;
    private static final int NO_SUCH_ALGORITHM = 2;
    private static final int SUCCESS = 3;

    public IntroPresenter(IntroView view) {
        super(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onCreate() {
        Single.fromCallable(() -> {
            int tryCnt = 0;

            while (tryCnt < Constants.CONNECTION_RETRY) {
                try {
                    Tron.getInstance(mContext).initTronNode();
                    long height = Tron.getInstance(mContext).getBlockHeight().blockingGet();
                    Log.d(IntroPresenter.class.getSimpleName(), "block height : " + height);
                    break;
                } catch (Exception e) {
                    if (e instanceof IllegalStateException || e instanceof NoSuchAlgorithmException) {
                        return NO_SUCH_ALGORITHM;
                    }

                    if (tryCnt == Constants.CONNECTION_RETRY - 1) {
                        throw e;
                    }

                    e.printStackTrace();
                }

                tryCnt++;
            }

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
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
            if (result == SUCCESS) {
                mView.startLoginActivity();
            } else if (result == NOT_AGREE) {
                mView.startBackupAccountActivity();
            } else if (result == NO_SUCH_ALGORITHM) {
                mView.doesNotSupportAlgorithm();
            } else if (result == NO_WALLET) {
                mView.startCreateAccountActivity();
            }
        }, e -> {
            mView.showErrorMsg();
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
