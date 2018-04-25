package com.devband.tronwalletforandroid.tron.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.devband.tronwalletforandroid.database.model.WalletModel;

public interface WalletRepository {

    boolean storeAddress(@NonNull WalletModel walletModel);

    @Nullable
    WalletModel loadAddress();
}
