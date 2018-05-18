package com.devband.tronwalletforandroid.ui.transaction;

import android.util.Log;

import com.devband.tronlib.Hosts;
import com.devband.tronlib.ServiceBuilder;
import com.devband.tronlib.dto.Transaction;
import com.devband.tronlib.dto.Transactions;
import com.devband.tronlib.services.TronScanService;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;
import com.devband.tronwalletforandroid.ui.transaction.dto.TransactionInfo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by user on 2018. 5. 17..
 */

public class TransactionPresenter extends BasePresenter<TransactionView> {

    private static final String TEST_ADDRESS = "27g7bUKnpYVxB8UHdJB8QzA9qSKKhwX5eqs";

    public TransactionPresenter(TransactionView view) {
        super(view);
    }

    @Override
    public void onCreate() {
        TronScanService tronScanService = ServiceBuilder.createService(TronScanService.class, Hosts.TRONSCAM_API);
        tronScanService.getTransactions(TEST_ADDRESS, "TRX")
                .subscribeOn(Schedulers.io())
                .map(transactions -> {
                    List<TransactionInfo> infos = new ArrayList<>();

                    for (Transaction t : transactions.getData()) {
                        TransactionInfo info = new TransactionInfo();
                        info.setHash(t.getHash());
                        info.setAmount(t.getAmount());
                        info.setBlock(t.getBlock());
                        info.setTimestamp(t.getTimestamp());
                        info.setTokenName(t.getTokenName());
                        info.setTransferFromAddress(t.getTransferFromAddress());
                        info.setTransferToAddress(t.getTransferToAddress());
                        infos.add(info);
                    }
                    return infos;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<TransactionInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<TransactionInfo> transactionInfos) {
                        mView.transactionDataLoadSuccess(transactionInfos);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("hanseon--", "onError : " + e.getMessage());
                    }
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
