package com.devband.tronwalletforandroid.tron.repository;

import com.devband.tronwalletforandroid.database.model.AddressModel;

public interface WalletRepository {

    boolean storeAddress(AddressModel addressModel);

    AddressModel loadAddress();
}
