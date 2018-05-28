package com.devband.tronwalletforandroid.ui.detail_block;

import android.util.Log;

import com.devband.tronlib.TronNetwork;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by user on 2018. 5. 28..
 */

public class DetailBlockPresenter extends BasePresenter<DetailBlockView> {

    public DetailBlockPresenter(DetailBlockView view) {
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

    public void loadTransactionStats(String address) {
        mView.showLoadingDilaog();
        Log.d("hanseon--", "address : " + address);
        TronNetwork.getInstance().getTransactionStats(address)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mView::transactionStatsLoadSuccess, t -> mView.showServerError());
    }
}
