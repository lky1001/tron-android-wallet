package com.devband.tronwalletforandroid.ui.token;

import com.devband.tronlib.TronNetwork;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class TokenActivityModule {

    @Binds
    public abstract TokenView view(TokenActivity tokenActivity);

    @Provides
    static TokenPresenter provideTokenPresenter(TokenView view, Tron tron, TronNetwork tronNetwork,
            WalletAppManager walletAppManager, RxJavaSchedulers rxJavaSchedulers) {
        return new TokenPresenter(view, tron, tronNetwork, walletAppManager, rxJavaSchedulers);
    }
}
