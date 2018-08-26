package com.devband.tronwalletforandroid.ui.backupaccount;

import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class BackupAccountActivityModule {

    @Binds
    public abstract BackupAccountView view(BackupAccountActivity backupAccountActivity);

    @Provides
    static BackupAccountPresenter provideBackupAccountPresenter(BackupAccountView backupAccountView,
            Tron tron, WalletAppManager walletAppManager, RxJavaSchedulers rxJavaSchedulers) {
        return new BackupAccountPresenter(backupAccountView, tron, walletAppManager,
                rxJavaSchedulers);
    }
}
