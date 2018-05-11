package com.devband.tronwalletforandroid.tron.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.devband.tronwalletforandroid.database.model.WalletModel;

import java.util.List;

public interface WalletRepository {

    boolean storeWallet(@NonNull WalletModel walletModel);

    boolean updateWallet(@NonNull WalletModel walletModel);

    @Nullable
    WalletModel loadWallet(int index);

    @Nullable
    List<WalletModel> loadAllWallets();

    int countWallets();
}
