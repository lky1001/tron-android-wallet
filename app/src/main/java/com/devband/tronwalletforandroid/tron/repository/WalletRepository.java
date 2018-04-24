package com.devband.tronwalletforandroid.tron.repository;

import com.devband.tronwalletforandroid.database.model.WalletModel;

public interface WalletRepository {

    boolean storeAddress(WalletModel addressModel);

    WalletModel loadAddress();
}
