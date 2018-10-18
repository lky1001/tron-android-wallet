package com.devband.tronwalletforandroid.ui.createwallet;

import android.os.Build;
import android.support.annotation.NonNull;

import com.crashlytics.android.Crashlytics;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.common.CustomPreference;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class CreateWalletPresenter extends BasePresenter<CreateWalletView> {

    private Tron mTron;
    private RxJavaSchedulers mRxJavaSchedulers;
    private CustomPreference mCustomPreference;

    public CreateWalletPresenter(CreateWalletView view, Tron tron, RxJavaSchedulers rxJavaSchedulers,
            CustomPreference customPreference) {
        super(view);
        this.mTron = tron;
        this.mRxJavaSchedulers = rxJavaSchedulers;
        this.mCustomPreference = customPreference;
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

    public void createWallet(@NonNull String password) {
        mTron.createWallet(password)
                .flatMap(createWalletResult -> {
                    if (createWalletResult == WalletAppManager.SUCCESS) {
                        return mTron.createAccount(Constants.PREFIX_ACCOUNT_NAME, password);
                    } else {
                        return Single.just(Tron.ERROR);
                    }
                })
                .map(registerAccountResult -> {
                    if (registerAccountResult != Tron.SUCCESS) {
                        return registerAccountResult;
                    } else {
                        int result = mTron.login(password);

                        if (result == WalletAppManager.SUCCESS) {
                            mCustomPreference.setInitWallet(true);
                            mCustomPreference.setKeyStoreVersion(Build.VERSION.SDK_INT);
                            return Tron.SUCCESS;
                        }

                        return Tron.ERROR_INVALID_PASSWORD;
                    }
                })
                .subscribeOn(mRxJavaSchedulers.getIo())
                .observeOn(mRxJavaSchedulers.getMainThread())
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
                            mView.createdWallet(WalletAppManager.getEncKey(password));
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
