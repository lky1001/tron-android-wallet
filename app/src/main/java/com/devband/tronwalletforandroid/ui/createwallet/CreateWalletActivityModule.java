package com.devband.tronwalletforandroid.ui.createwallet;

import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class CreateWalletActivityModule {

    @Binds
    public abstract CreateWalletView view(CreateWalletActivity createWalletActivity);

    @Provides
    static CreateWalletPresenter provideCreateWalletPresenter(CreateWalletView createWalletView,
            Tron tron, WalletAppManager walletAppManager, RxJavaSchedulers rxJavaSchedulers) {
        return new CreateWalletPresenter(createWalletView, tron, walletAppManager,
                rxJavaSchedulers);
    }
}
