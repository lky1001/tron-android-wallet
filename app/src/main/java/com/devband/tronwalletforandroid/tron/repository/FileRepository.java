package com.devband.tronwalletforandroid.tron.repository;

import android.support.annotation.NonNull;

import com.devband.tronwalletforandroid.database.model.AccountModel;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

public class FileRepository implements AccountRepository {

    @Override
    public Single<Long> insertAccount(@NonNull AccountModel accountModel) {
        return null;
    }

    @Override
    public Single<Boolean> updateAccount(@NonNull AccountModel accountModel) {
        return null;
    }

    @Override
    public void delete(@NonNull AccountModel accountModel) {

    }

    @Override
    public AccountModel loadAccount() {
        return null;
    }

    @Override
    public Maybe<AccountModel> loadAccount(long index) {
        return null;
    }

    @Override
    public Single<List<AccountModel>> loadAllAccounts() {
        return null;
    }

    @Override
    public Integer countAccount() {
        return null;
    }

    @Override
    public AccountModel loadByAccountKey(String accountKey) {
        return null;
    }
}
