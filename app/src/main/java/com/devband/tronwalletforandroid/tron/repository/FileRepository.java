package com.devband.tronwalletforandroid.tron.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.devband.tronwalletforandroid.database.model.AccountModel;

import java.util.List;

public class FileRepository implements AccountRepository {

    @Override
    public boolean storeAccount(@NonNull AccountModel accountModel) {
        return false;
    }

    @Override
    public boolean updateAccount(@NonNull AccountModel accountModel) {
        return false;
    }

    @Nullable
    @Override
    public AccountModel loadAccount(int index) {
        return null;
    }

    @Nullable
    @Override
    public List<AccountModel> loadAllAccounts() {
        return null;
    }

    @Override
    public int countAccount() {
        return 0;
    }

    @Nullable
    @Override
    public AccountModel loadByAccountKey(String accountKey) {
        return null;
    }
}
