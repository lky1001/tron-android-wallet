package com.devband.tronwalletforandroid.tron.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.devband.tronwalletforandroid.database.model.WalletModel;

import java.util.List;

public class FileRepository implements WalletRepository {

    @Override
    public boolean storeWallet(@NonNull WalletModel walletModel) {
        return false;
    }

    @Override
    public boolean updateWallet(@NonNull WalletModel walletModel) {
        return false;
    }

    @Nullable
    @Override
    public WalletModel loadWallet(int index) {
        return null;
    }

    @Nullable
    @Override
    public List<WalletModel> loadAllWallets() {
        return null;
    }

    @Override
    public int countWallets() {
        return 0;
    }

    @Nullable
    @Override
    public WalletModel loadByWalletKey(String walletKey) {
        return null;
    }
}
