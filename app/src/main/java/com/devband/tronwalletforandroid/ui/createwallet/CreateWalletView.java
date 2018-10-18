package com.devband.tronwalletforandroid.ui.createwallet;

import android.support.annotation.NonNull;

import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface CreateWalletView extends IView {

    void createdWallet(@NonNull byte[] aesKey);

    void passwordError();

    void registerWalletError();
}
