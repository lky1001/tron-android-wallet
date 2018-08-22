package com.devband.tronwalletforandroid.ui.createwallet;

import com.crashlytics.android.Crashlytics;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.tron.WalletAppManager;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CreateWalletPresenter extends BasePresenter<CreateWalletView> {

    private Tron mTron;
    private WalletAppManager mWalletAppManager;
    private Scheduler mProcessScheduler;
    private Scheduler mObserverScheduler;

    public CreateWalletPresenter(CreateWalletView view, Tron tron, WalletAppManager walletAppManager,
            Scheduler processScheduler, Scheduler observerScheduler) {
        super(view);
        this.mTron = tron;
        this.mWalletAppManager = walletAppManager;
        this.mProcessScheduler = processScheduler;
        this.mObserverScheduler = observerScheduler;
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

                return Tron.SUCCESS;
            } else if (result == WalletAppManager.ERROR) {
                return Tron.ERROR;
            }

            return result;
        })
        .subscribeOn(mProcessScheduler)
        .observeOn(mObserverScheduler)
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
