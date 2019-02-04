package com.devband.tronwalletforandroid.ui.intro;

import android.annotation.SuppressLint;
import android.util.Log;

import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import io.reactivex.Single;

public class IntroPresenter extends BasePresenter<IntroView> {

    private static final int NO_WALLET = 0;
    private static final int NOT_AGREE = 1;
    private static final int NO_SUCH_ALGORITHM = 2;
    private static final int CONNECTION_ERROR = 3;
    private static final int SUCCESS = 4;

    private Tron mTron;
    private WalletAppManager mWalletAppManager;
    private RxJavaSchedulers mRxJavaSchedulers;

    public IntroPresenter(IntroView view, Tron tron, WalletAppManager walletAppManager,
            RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mTron = tron;
        this.mWalletAppManager = walletAppManager;
        this.mRxJavaSchedulers = rxJavaSchedulers;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onCreate() {
        Single.fromCallable(() -> {
            int tryCnt = 0;

            while (tryCnt < Constants.CONNECTION_RETRY) {
                try {
                    mTron.initTronNode();
                    long height = mTron.getBlockHeight().blockingGet();
                    Log.d(IntroPresenter.class.getSimpleName(), "block height : " + height);
                    break;
                } catch (Exception e) {
                    if (e instanceof IllegalStateException) {
                        return NO_SUCH_ALGORITHM;
                    }

                    if (tryCnt == Constants.CONNECTION_RETRY - 1) {
                        return CONNECTION_ERROR;
                    }

                    mTron.setFailConnectNode(true);
                    e.printStackTrace();
                }

                tryCnt++;
            }

            if (mWalletAppManager.hasWallet()) {
                if (mWalletAppManager.isAgree()) {
                    return SUCCESS;
                } else {
                    return NOT_AGREE;
                }
            } else {
                return NO_WALLET;
            }
        })
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers.getMainThread())
        .subscribe(result -> {
            if (result == SUCCESS) {
                mView.startLoginActivity();
            } else if (result == NOT_AGREE) {
                mView.startBackupAccountActivity();
            } else if (result == NO_SUCH_ALGORITHM) {
                mView.doesNotSupportAlgorithm();
            } else if (result == CONNECTION_ERROR) {
                if (mWalletAppManager.hasWallet()) {
                    if (mWalletAppManager.isAgree()) {
                        mView.connectionError();
                    } else {
                        mView.showErrorMsg();
                    }
                } else {
                    mView.startCreateAccountActivity();
                }
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
