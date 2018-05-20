package com.devband.tronwalletforandroid.ui.myaccount;

import android.support.annotation.NonNull;

import com.devband.tronwalletforandroid.common.WalletAppManager;
import com.devband.tronwalletforandroid.database.model.AccountModel;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import org.tron.protos.Protocol;

import java.net.ConnectException;
import java.util.List;

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

    public List<AccountModel> getAccountList() {
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
                        mView.hideDialog();
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
}
