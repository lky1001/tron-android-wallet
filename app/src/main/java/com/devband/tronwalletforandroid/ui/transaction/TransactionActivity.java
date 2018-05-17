package com.devband.tronwalletforandroid.ui.transaction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.devband.tronlib.Hosts;
import com.devband.tronlib.ServiceBuilder;
import com.devband.tronlib.dto.Transaction;
import com.devband.tronlib.dto.Transactions;
import com.devband.tronlib.services.TronScanService;
import com.devband.tronwalletforandroid.common.CommonActivity;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by user on 2018. 5. 17..
 */

public class TransactionActivity extends CommonActivity {

    private static final String TEST_ADDRESS = "27g7bUKnpYVxB8UHdJB8QzA9qSKKhwX5eqs";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TronScanService tronScanService = ServiceBuilder.createService(TronScanService.class, Hosts.TRONSCAM_API);

        tronScanService.getTransactions(TEST_ADDRESS, "TRX")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Transactions>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Transactions transactions) {
                        Log.d("hanseon--", "onSuccess");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("hanseon--", "onError : " + e.getMessage());
                    }
                });
    }
}
