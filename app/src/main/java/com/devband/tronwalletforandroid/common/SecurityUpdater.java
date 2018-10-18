package com.devband.tronwalletforandroid.common;

import android.content.Context;

import com.devband.tronwalletforandroid.common.security.PasswordEncoder;
import com.devband.tronwalletforandroid.database.AppDatabase;
import com.devband.tronwalletforandroid.database.dao.WalletDao;

public class SecurityUpdater {

    private CustomPreference customPreference;
    private PasswordEncoder passwordEncoder;

    private WalletDao mWalletDao;

    public SecurityUpdater(Context context, CustomPreference customPreference, PasswordEncoder passwordEncoder, AppDatabase appDatabase) {
        this.customPreference = customPreference;
        this.passwordEncoder = passwordEncoder;
        mWalletDao = appDatabase.walletDao();
    }

    public boolean canUpdate() {
        return true;
    }

    public boolean doUpdate(String password) {
        return true;
    }
}
