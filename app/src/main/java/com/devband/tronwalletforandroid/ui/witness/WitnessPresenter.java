package com.devband.tronwalletforandroid.ui.witness;

import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import org.tron.api.GrpcAPI;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WitnessPresenter extends BasePresenter<WitnessView> {

    public WitnessPresenter(WitnessView view) {
        super(view);
    }

    @Override
    public void onCreate() {
        mView.showLoadingDialog();
        getWitnessNodeList();
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

    public void getWitnessNodeList() {
        Tron.getInstance(mContext).getWitnessList()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleObserver<GrpcAPI.WitnessList>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(GrpcAPI.WitnessList witnessList) {

            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }
}
