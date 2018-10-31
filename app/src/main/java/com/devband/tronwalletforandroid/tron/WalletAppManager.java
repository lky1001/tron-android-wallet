package com.devband.tronwalletforandroid.tron;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.devband.tronwalletforandroid.common.security.PasswordEncoder;
import com.devband.tronwalletforandroid.database.AppDatabase;
import com.devband.tronwalletforandroid.database.dao.WalletDao;
import com.devband.tronwalletforandroid.database.model.WalletModel;

import org.tron.common.crypto.Hash;

import java.util.Arrays;
import java.util.Calendar;

public class WalletAppManager {

    private static final int KEY_SIZE = 16;

    public static final int MIN_PASSWORD_LENGTH = 8;

    public static final int SUCCESS = 1;
    public static final int ERROR = -1;

    private boolean mIsLogin;

    private WalletDao mWalletDao;

    private PasswordEncoder mPasswordEncoder;

    public WalletAppManager(@NonNull PasswordEncoder passwordEncoder, @NonNull AppDatabase appDatabase) {
        this.mPasswordEncoder = passwordEncoder;
        this.mWalletDao = appDatabase.walletDao();
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
                .password(mPasswordEncoder.encode(password))
                .created(Calendar.getInstance().getTime())
                .build());

        return SUCCESS;
    }

    public void loginWithFingerPrint() {
        mIsLogin = true;
    }

    public boolean oldLogin(@NonNull String password) {
        WalletModel wallet = mWalletDao.loadWallet();

        if (PasswordUtil.matches(password, wallet.getPassword())) {
            mIsLogin = true;
            return true;
        }

        return false;
    }

    public int login(@NonNull String password) {
        if (TextUtils.isEmpty(password) || password.length() < MIN_PASSWORD_LENGTH) {
            return ERROR;
        }

        WalletModel wallet = mWalletDao.loadWallet();

        if (mPasswordEncoder.matches(password, wallet.getPassword())) {
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

    public boolean isLoginState() {
        return mIsLogin;
    }

    public boolean changePassword(@NonNull String oldPassword, @NonNull String newPassword) {
        WalletModel wallet = mWalletDao.loadWallet();

        if (mPasswordEncoder.matches(oldPassword, wallet.getPassword())) {
            wallet.setPassword(mPasswordEncoder.encode(newPassword));
            wallet.setUpdated(Calendar.getInstance().getTime());
            mWalletDao.update(wallet);
            return true;
        }

        return false;
    }

    public boolean checkPassword(String password) {
        WalletModel wallet = mWalletDao.loadWallet();
        return mPasswordEncoder.matches(password, wallet.getPassword());
    }

    @Nullable
    public static byte[] getEncKey(String password) {
        if (!passwordValid(password)) {
            return null;
        }
        byte[] encKey;
        encKey = Hash.sha256(password.getBytes());
        encKey = Arrays.copyOfRange(encKey, 0, KEY_SIZE);
        return encKey;
    }

    public static boolean passwordValid(String password) {
        if (TextUtils.isEmpty(password)) {
            return false;
        }

        if (password.length() < Tron.MIN_PASSWORD_LENGTH) {
            return false;
        }

        return true;
    }

    // todo - remove when all user updated above 1.2.5
    public boolean migrationPassword(@NonNull String password) {
        if (oldLogin(password)) {
            WalletModel wallet = mWalletDao.loadWallet();
            wallet.setPassword(mPasswordEncoder.encode(password));
            wallet.setUpdated(Calendar.getInstance().getTime());
            mWalletDao.update(wallet);
            return true;
        }

        return false;
    }
}
