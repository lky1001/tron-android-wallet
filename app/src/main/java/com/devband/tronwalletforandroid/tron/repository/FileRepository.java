package com.devband.tronwalletforandroid.tron.repository;

import android.support.annotation.NonNull;

import com.devband.tronwalletforandroid.database.model.AccountModel;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

public class FileRepository implements AccountRepository {

    @Override
    public Single<Boolean> storeAccount(@NonNull AccountModel accountModel) {
        return null;
    }

    @Override
    public Single<Boolean> updateAccount(@NonNull AccountModel accountModel) {
        return null;
    }

    @Override
    public Maybe<AccountModel> loadAccount(int index) {
        return null;
    }

    @Override
    public Single<List<AccountModel>> loadAllAccounts() {
        return null;
    }

    @Override
    public Single<Integer> countAccount() {
        return null;
    }

    @Override
    public Maybe<AccountModel> loadByAccountKey(String accountKey) {
        return null;
    }
}
