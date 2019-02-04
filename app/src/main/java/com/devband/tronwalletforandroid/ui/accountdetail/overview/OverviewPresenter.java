package com.devband.tronwalletforandroid.ui.accountdetail.overview;

import android.support.annotation.NonNull;

import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.tronscan.Balance;
import com.devband.tronlib.tronscan.FrozenTrx;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
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
import io.reactivex.disposables.Disposable;

public class OverviewPresenter extends BasePresenter<OverviewView> {

    private TronNetwork mTronNetwork;
    private Tron mTron;
    private RxJavaSchedulers mRxJavaSchedulers;

    public OverviewPresenter(OverviewView view, Tron tron, TronNetwork tronNetwork, RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mTron = tron;
        this.mTronNetwork = tronNetwork;
        this.mRxJavaSchedulers = rxJavaSchedulers;
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

        Single.zip(mTron.getAccount(address), mTronNetwork.getTransactionStats(address), ((account, transactionStats) -> {
            List<Frozen> frozenList = new ArrayList<>();

            for (FrozenTrx frozen : account.getFrozen().getBalances()) {
                frozenList.add(Frozen.builder()
                        .frozenBalance(frozen.getAmount())
                        .expireTime(frozen.getExpires())
                        .build());
            }

            List<Asset> assetList = new ArrayList<>();

            for (Balance balance : account.getTrc10TokenBalances()) {
                assetList.add(Asset.builder()
                        .name(balance.getDisplayName() + "(" + balance.getName() + "):")
                        .balance(balance.getBalance())
                        .build());
            }

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
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers.getMainThread())
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
