package com.devband.tronwalletforandroid.ui.accountdetail.overview;

import android.support.annotation.NonNull;

import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.dto.Account;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class OverviewPresenter extends BasePresenter<OverviewView> {

    public OverviewPresenter(OverviewView view) {
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

    public void getAccount(@NonNull String address) {
        mView.showLoadingDialog();

        TronNetwork.getInstance()
                .getAccount(address)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Account>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Account account) {
                        mView.finishLoading(account);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showServerError();
                    }
                });
    }
}
