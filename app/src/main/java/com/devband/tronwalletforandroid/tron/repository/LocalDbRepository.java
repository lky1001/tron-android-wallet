package com.devband.tronwalletforandroid.tron.repository;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.devband.tronwalletforandroid.database.AppDatabase;
import com.devband.tronwalletforandroid.database.dao.AccountDao;
import com.devband.tronwalletforandroid.database.model.AccountModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LocalDbRepository implements AccountRepository {

    private AccountDao mAccountDao;

    public LocalDbRepository(@NonNull Context context) {
        mAccountDao = AppDatabase.getDatabase(context).accountDao();
    }

    @Override
    public boolean storeAccount(@NonNull AccountModel accountModel) {
        Date now = Calendar.getInstance().getTime();
        accountModel.setCreated(now);

        mAccountDao.insert(accountModel);

        return true;
    }

    @Override
    public boolean updateAccount(@NonNull AccountModel accountModel) {
        Date now = Calendar.getInstance().getTime();
        accountModel.setUpdated(now);

        mAccountDao.update(accountModel);

        return true;
    }

    @Nullable
    @Override
    public AccountModel loadAccount(int index) {
        return mAccountDao.loadAccountById(index);
    }

    @Nullable
    @Override
    public List<AccountModel> loadAllAccounts() {
        return mAccountDao.loadAllAccounts();
    }

    @Override
    public int countAccount() {
        return mAccountDao.countAccounts();
    }

    @Nullable
    @Override
    public AccountModel loadByAccountKey(String accountKey) {
        return mAccountDao.loadByAccountKey(accountKey);
    }
}
