package com.devband.tronwalletforandroid.tron.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.devband.tronwalletforandroid.database.model.AccountModel;

import java.util.List;

public interface AccountRepository {

    boolean storeAccount(@NonNull AccountModel accountModel);

    boolean updateAccount(@NonNull AccountModel accountModel);

    @Nullable
    AccountModel loadAccount(int index);

    @Nullable
    List<AccountModel> loadAllAccounts();

    int countAccount();

    @Nullable
    AccountModel loadByAccountKey(String accountKey);
}
