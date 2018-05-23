package com.devband.tronwalletforandroid.tron.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.devband.tronwalletforandroid.database.model.AccountModel;

import java.util.List;

import io.reactivex.Single;

public interface AccountRepository {

    Single<Boolean> storeAccount(@NonNull AccountModel accountModel);

    Single<Boolean> updateAccount(@NonNull AccountModel accountModel);

    Single<AccountModel> loadAccount(int index);

    Single<List<AccountModel>> loadAllAccounts();

    Single<Integer> countAccount();

    Single<AccountModel> loadByAccountKey(String accountKey);
}
