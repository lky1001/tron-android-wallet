package com.devband.tronwalletforandroid.ui.myaccount;

import android.support.annotation.NonNull;

import com.devband.tronlib.dto.Account;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.tron.WalletAppManager;
import com.devband.tronwalletforandroid.database.model.AccountModel;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.main.dto.Asset;
import com.devband.tronwalletforandroid.ui.main.dto.Frozen;
import com.devband.tronwalletforandroid.ui.main.dto.TronAccount;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import java.net.ConnectException;
import java.util.ArrayList;
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

        Tron.getInstance(mContext).getAccount(Tron.getInstance(mContext).getLoginAddress())
                .subscribeOn(Schedulers.io())
                .map((account -> {
                    List<Frozen> frozenList = new ArrayList<>();

                    for (Account.FrozenTrx frozen : account.getFrozen().getBalances()) {
                        frozenList.add(Frozen.builder()
                                .frozenBalance(frozen.getAmount())
                                .expireTime(frozen.getExpires())
                                .build());
                    }

                    List<Asset> assetList = new ArrayList<>();

                    for (Account.Balance balance : account.getTokenBalances()) {
                        if (Constants.TRON_SYMBOL.equalsIgnoreCase(balance.getName())) {
                            continue;
                        }

                        assetList.add(Asset.builder()
                                .name(balance.getName())
                                .balance(balance.getBalance())
                                .build());
                    }

                    return TronAccount.builder()
                            .name(account.getName())
                            .balance(account.getBalance())
                            .bandwidth(account.getBandwidth().getNetRemaining())
                            .assetList(assetList)
                            .frozenList(frozenList)
                            .build();
                }))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<TronAccount>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(TronAccount account) {
                        mView.displayAccountInfo(address, account);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        // todo - error msg
                        if (e instanceof ConnectException) {
                            // internet error
                        }

                        mView.showServerError();
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

    public void changePassword(@NonNull String originPassword, @NonNull String newPassword) {
        mView.showLoadingDialog();

        Single.fromCallable(() -> {
            boolean result = WalletAppManager.getInstance(mContext).changePassword(originPassword, newPassword);

            if (result) {
                return Tron.getInstance(mContext).changePassword(newPassword);
            }

            return false;
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleObserver<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Boolean result) {
                mView.changePasswordResult(result);
            }

            @Override
            public void onError(Throwable e) {
                mView.changePasswordResult(false);
            }
        });
    }
}
