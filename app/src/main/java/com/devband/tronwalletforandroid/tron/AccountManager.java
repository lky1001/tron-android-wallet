package com.devband.tronwalletforandroid.tron;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.devband.tronwalletforandroid.database.AppDatabase;
import com.devband.tronwalletforandroid.database.dao.AccountDao;
import com.devband.tronwalletforandroid.database.model.AccountModel;

import java.util.Calendar;

public class AccountManager {

    public static final int MIN_PASSWORD_LENGTH = 8;

    public static final int SUCCESS = 1;
    public static final int ERROR = -1;

    private static AccountManager instance;

    private Context mContext;

    private boolean mIsLogin;

    private AccountDao mAccountDao;

    public static synchronized AccountManager getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (AccountManager.class) {
                if (instance == null) {
                    instance = new AccountManager(context);
                }
            }
        }
        return instance;
    }

    private AccountManager() {}

    private AccountManager(@NonNull Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        mAccountDao = AppDatabase.getDatabase(mContext).accountDao();
    }

    public boolean isLogin() {
        return mIsLogin;
    }

    public boolean hasAccount() {
        return mAccountDao.countAccounts() > 0;
    }

    public int createAccount(@NonNull String password) {
        if (TextUtils.isEmpty(password) || password.length() < MIN_PASSWORD_LENGTH) {
            return ERROR;
        }

        mAccountDao.insert(AccountModel.builder()
                .password(PasswordUtil.getHashedPassword(password))
                .created(Calendar.getInstance().getTime())
                .build());

        return SUCCESS;
    }

    public int login(@NonNull String password) {
        if (TextUtils.isEmpty(password) || password.length() < MIN_PASSWORD_LENGTH) {
            return ERROR;
        }

        AccountModel account = mAccountDao.loadAccount();

        if (PasswordUtil.matches(password, account.getPassword())) {
            mIsLogin = true;
            return SUCCESS;
        }

        return ERROR;
    }

    public void logout() {
        mIsLogin = false;
    }
}
