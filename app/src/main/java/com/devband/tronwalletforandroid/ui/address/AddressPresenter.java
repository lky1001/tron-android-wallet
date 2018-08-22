package com.devband.tronwalletforandroid.ui.address;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import net.glxn.qrgen.android.QRCode;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.Builder;

public class AddressPresenter extends BasePresenter<AddressView> {

    private Tron mTron;
    private Scheduler mProcessScheduler;
    private Scheduler mObserverScheduler;

    public AddressPresenter(AddressView view, Tron tron, Scheduler processScheduler, Scheduler observerScheduler) {
        super(view);
        this.mTron = tron;
        this.mProcessScheduler = processScheduler;
        this.mObserverScheduler = observerScheduler;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {
        Single.fromCallable(() -> {
            String address = mTron.getLoginAddress();

            if (address == null || address.isEmpty()) {
                return null;
            }

            Bitmap addressImage = QRCode.from(address).bitmap();

            return AddressInfo.builder()
                    .addressBitmap(addressImage)
                    .address(address)
                    .build();
        })
        .subscribeOn(mProcessScheduler)
        .observeOn(mObserverScheduler)
        .subscribe(new SingleObserver<AddressInfo>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(@Nullable AddressInfo addressInfo) {
                mView.addressResult(addressInfo);
            }

            @Override
            public void onError(Throwable e) {
                mView.addressResult(null);
            }
        });
    }

    @Override
    public void onDestroy() {

    }

    @Builder
    public static class AddressInfo {

        public Bitmap addressBitmap;
        public String address;
    }
}
