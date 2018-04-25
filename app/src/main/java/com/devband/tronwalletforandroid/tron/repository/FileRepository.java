package com.devband.tronwalletforandroid.tron.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.devband.tronwalletforandroid.database.model.WalletModel;

public class FileRepository implements WalletRepository {

    @Override
    public boolean storeAddress(@NonNull WalletModel walletModel) {
        return false;
    }

    @Nullable
    @Override
    public WalletModel loadAddress() {
        return null;
    }
}
