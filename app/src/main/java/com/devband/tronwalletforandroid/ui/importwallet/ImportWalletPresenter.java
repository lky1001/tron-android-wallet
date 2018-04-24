package com.devband.tronwalletforandroid.ui.importwallet;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import net.glxn.qrgen.android.QRCode;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.Builder;

public class ImportWalletPresenter extends BasePresenter<ImportWalletView> {

    public ImportWalletPresenter(ImportWalletView view) {
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

    public boolean importWallet(@NonNull String password, @NonNull String privateKey) {
        if (Tron.getInstance(mContext).importWallet(password, privateKey) != Tron.SUCCESS) {
            return false;
        }

        return true;
    }
}
