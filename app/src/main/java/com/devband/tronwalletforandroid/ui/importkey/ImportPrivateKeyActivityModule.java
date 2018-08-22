package com.devband.tronwalletforandroid.ui.importkey;

import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;
import com.devband.tronwalletforandroid.ui.createwallet.CreateWalletActivity;
import com.devband.tronwalletforandroid.ui.createwallet.CreateWalletPresenter;
import com.devband.tronwalletforandroid.ui.createwallet.CreateWalletView;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public abstract class ImportPrivateKeyActivityModule {

    @Binds
    public abstract ImportPrivateKeyView view(ImportPrivateKeyActivity importPrivateKeyActivity);

    @Provides
    static ImportPrivateKeyPresenter provideImportPrivateKeyPresenter(ImportPrivateKeyView importPrivateKeyView,
            Tron tron, WalletAppManager walletAppManager) {
        return new ImportPrivateKeyPresenter(importPrivateKeyView, tron, walletAppManager,
                Schedulers.io(), AndroidSchedulers.mainThread());
    }
}
