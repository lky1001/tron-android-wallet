package com.devband.tronwalletforandroid.tron.repository;

import android.content.Context;
import android.support.annotation.NonNull;

import com.devband.tronwalletforandroid.database.AppDatabase;
import com.devband.tronwalletforandroid.database.dao.AccountDao;
import com.devband.tronwalletforandroid.database.model.AccountModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

public class LocalDbRepository implements AccountRepository {

    private AccountDao mAccountDao;

    public LocalDbRepository(@NonNull AppDatabase appDatabase) {
        mAccountDao = appDatabase.accountDao();
    }

    @Override
    public Single<Boolean> insertAccount(@NonNull AccountModel accountModel) {
        return Single.fromCallable(() -> {
            Date now = Calendar.getInstance().getTime();
            accountModel.setCreated(now);

            mAccountDao.insert(accountModel);

            return true;
        });
    }

    @Override
    public Single<Boolean> updateAccount(@NonNull AccountModel accountModel) {
        return Single.fromCallable(() -> {
            Date now = Calendar.getInstance().getTime();
            accountModel.setUpdated(now);

            mAccountDao.update(accountModel);

            return true;
        });
    }

    @Override
    public Maybe<AccountModel> loadAccount(int index) {
        return Maybe.fromCallable(() -> mAccountDao.loadAccountById(index));
    }

    @Override
    public Single<List<AccountModel>> loadAllAccounts() {
        return Single.fromCallable(() -> mAccountDao.loadAllAccounts());
    }

    @Override
    public Single<Integer> countAccount() {
        return Single.fromCallable(() -> mAccountDao.countAccounts());
    }

    @Override
    public Maybe<AccountModel> loadByAccountKey(String accountKey) {
        return Maybe.fromCallable(() -> mAccountDao.loadByAccountKey(accountKey));
    }
}
