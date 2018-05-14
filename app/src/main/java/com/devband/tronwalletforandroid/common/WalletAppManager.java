package com.devband.tronwalletforandroid.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.devband.tronwalletforandroid.database.AppDatabase;
import com.devband.tronwalletforandroid.database.dao.WalletDao;
import com.devband.tronwalletforandroid.database.model.WalletModel;
import com.devband.tronwalletforandroid.tron.PasswordUtil;

import java.util.Calendar;

public class WalletAppManager {

    public static final int MIN_PASSWORD_LENGTH = 8;

    public static final int SUCCESS = 1;
    public static final int ERROR = -1;

    private static WalletAppManager instance;

    private Context mContext;

    private boolean mIsLogin;

    private WalletDao mWalletDao;

    public static synchronized WalletAppManager getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (WalletAppManager.class) {
                if (instance == null) {
                    instance = new WalletAppManager(context);
                }
            }
        }
        return instance;
    }

    private WalletAppManager() {}

    private WalletAppManager(@NonNull Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        mWalletDao = AppDatabase.getDatabase(mContext).walletDao();
    }

    public boolean isLogin() {
        return mIsLogin;
    }

    public boolean hasWallet() {
        return mWalletDao.countWallets() > 0;
    }

    public int createWallet(@NonNull String password) {
        if (TextUtils.isEmpty(password) || password.length() < MIN_PASSWORD_LENGTH) {
            return ERROR;
        }

        mWalletDao.insert(WalletModel.builder()
                .password(PasswordUtil.getHashedPassword(password))
                .created(Calendar.getInstance().getTime())
                .build());

        return SUCCESS;
    }

    public int login(@NonNull String password) {
        if (TextUtils.isEmpty(password) || password.length() < MIN_PASSWORD_LENGTH) {
            return ERROR;
        }

        WalletModel wallet = mWalletDao.loadWallet();

        if (PasswordUtil.matches(password, wallet.getPassword())) {
            mIsLogin = true;
            return SUCCESS;
        }

        return ERROR;
    }

    public void logout() {
        mIsLogin = false;
    }

    public void agreeTerms(boolean isAgree) {
        WalletModel wallet = mWalletDao.loadWallet();
        wallet.setAgree(isAgree);

        mWalletDao.update(wallet);
    }

    public boolean isAgree() {
        WalletModel wallet = mWalletDao.loadWallet();

        return wallet.isAgree();
    }
}
