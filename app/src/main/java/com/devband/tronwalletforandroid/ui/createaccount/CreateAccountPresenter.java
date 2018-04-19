package com.devband.tronwalletforandroid.ui.createaccount;

import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.Builder;

public class CreateAccountPresenter extends BasePresenter<CreateAccountView> {

    public CreateAccountPresenter(CreateAccountView view) {
        super(view);
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

    public void changedPassword(String password) {
        Single.fromCallable(() -> {
            if (password != null && password.length() >= Tron.MIN_PASSWORD_LENGTH) {
                if (Tron.getInstance(mContext).registerWaller(password) == Tron.SUCCESS) {
                    if (Tron.getInstance(mContext).login(password) == Tron.SUCCESS) {
                        String privKey = Tron.getInstance(mContext).getPrivateKey();
                        String address = Tron.getInstance(mContext).getAddress();

                        return AccountInfo.builder()
                                .privKey(privKey)
                                .address(address)
                                .build();
                    }
                } else {
                    // ERROR_ACCESS_STORAGE
                }
            }

            return null;
        })
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleObserver<AccountInfo>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(AccountInfo accountInfo) {
                if (accountInfo != null) {
                    mView.displayAccountInfo(accountInfo.privKey, accountInfo.address);
                } else {
                    mView.displayAccountInfo("", "");
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mView.displayAccountInfo("", "");
            }
        });
    }

    @Builder
    private static class AccountInfo {
        String privKey;
        String address;
    }
}
