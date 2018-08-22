package com.devband.tronwalletforandroid.ui.backupaccount;

import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public abstract class BackupAccountActivityModule {

    @Binds
    public abstract BackupAccountView view(BackupAccountActivity backupAccountActivity);

    @Provides
    static BackupAccountPresenter provideBackupAccountPresenter(BackupAccountView backupAccountView,
            Tron tron, WalletAppManager walletAppManager) {
        return new BackupAccountPresenter(backupAccountView, tron, walletAppManager,
                Schedulers.io(), AndroidSchedulers.mainThread());
    }
}
