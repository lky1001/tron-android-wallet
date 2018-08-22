package com.devband.tronwalletforandroid.ui.createwallet;

import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;
import com.devband.tronwalletforandroid.ui.backupaccount.BackupAccountActivity;
import com.devband.tronwalletforandroid.ui.backupaccount.BackupAccountPresenter;
import com.devband.tronwalletforandroid.ui.backupaccount.BackupAccountView;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public abstract class CreateWalletActivityModule {

    @Binds
    public abstract CreateWalletView view(CreateWalletActivity createWalletActivity);

    @Provides
    static CreateWalletPresenter provideCreateWalletPresenter(CreateWalletView createWalletView,
            Tron tron, WalletAppManager walletAppManager) {
        return new CreateWalletPresenter(createWalletView, tron, walletAppManager,
                Schedulers.io(), AndroidSchedulers.mainThread());
    }
}
