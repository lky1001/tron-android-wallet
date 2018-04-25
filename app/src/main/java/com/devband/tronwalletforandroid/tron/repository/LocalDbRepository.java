package com.devband.tronwalletforandroid.tron.repository;

import com.devband.tronwalletforandroid.database.model.WalletModel;

public class LocalDbRepository implements WalletRepository {

    @Override
    public boolean storeAddress(WalletModel addressModel) {
        return false;
    }

    @Override
    public WalletModel loadAddress() {
        return null;
    }
}
