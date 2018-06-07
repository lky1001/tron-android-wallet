package com.devband.tronwalletforandroid.ui.accountdetail.overview;

import android.support.annotation.NonNull;

import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.dto.Account;
import com.devband.tronlib.dto.TransactionStats;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.ui.main.dto.Asset;
import com.devband.tronwalletforandroid.ui.main.dto.Frozen;
import com.devband.tronwalletforandroid.ui.main.dto.TronAccount;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

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

        TronNetwork.getInstance().getAccount(address)
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

                    TransactionStats transactionStats = TronNetwork.getInstance().getTransactionStats(address).blockingGet();

                    return TronAccount.builder()
                            .balance(account.getBalance())
                            .bandwidth(account.getBandwidth().getNetRemaining())
                            .assetList(assetList)
                            .frozenList(frozenList)
                            .transactions(transactionStats.getTransactions())
                            .transactionIn(transactionStats.getTransactionsIn())
                            .transactionOut(transactionStats.getTransactionsOut())
                            .account(account)
                            .build();
                }))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<TronAccount>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(TronAccount account) {
                        mView.finishLoading(account);
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
}
