package com.devband.tronwalletforandroid.ui.myaccount;

import android.support.annotation.NonNull;

import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.common.WalletAppManager;
import com.devband.tronwalletforandroid.database.model.AccountModel;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import org.tron.protos.Protocol;

import java.net.ConnectException;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyAccountPresenter extends BasePresenter<MyAccountView> {

    public MyAccountPresenter(MyAccountView view) {
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
        getAccountAccountInfo();
    }

    @Override
    public void onDestroy() {

    }

    public Single<List<AccountModel>> getAccountList() {
        return Tron.getInstance(mContext).getAccountList();
    }

    public void getAccountAccountInfo() {
        final String address = Tron.getInstance(mContext).getLoginAddress();

        mView.showLoadingDialog();

        Tron.getInstance(mContext).queryAccount(address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Protocol.Account>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Protocol.Account account) {
                        mView.hideDialog();
                        mView.displayAccountInfo(address, account);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showServerError();
                        e.printStackTrace();
                        // todo - error msg
                        if (e instanceof ConnectException) {
                            // internet error
                        }
                    }
                });
    }

    public boolean matchPassword(@NonNull String password) {
        return WalletAppManager.getInstance(mContext).login(password) == WalletAppManager.SUCCESS;
    }

    public String getLoginPrivateKey() {
        return Tron.getInstance(mContext).getLoginPrivateKey();
    }

    public void changeLoginAccount(@NonNull AccountModel accountModel) {
        Tron.getInstance(mContext).changeLoginAccount(accountModel);
    }

    public void freezeBalance(long freezeBalance) {
        mView.showLoadingDialog();

        Tron.getInstance(mContext).freezeBalance(freezeBalance, Constants.FREEZE_DURATION)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleObserver<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    mView.successFreezeBalance();
                } else {
                    mView.showServerError();
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mView.showServerError();
            }
        });
    }

    public void unfreezeBalance() {
        mView.showLoadingDialog();
        Tron.getInstance(mContext).unfreezeBalance()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleObserver<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    mView.successFreezeBalance();
                } else {
                    mView.showServerError();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof RuntimeException) {
                    mView.unableToUnfreeze();
                } else {
                    mView.showServerError();
                }
            }
        });
    }

    public int getLoginAccountIndex() {
        return Tron.getInstance(mContext).getLoginAccount().getId();
    }
}
