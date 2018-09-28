package com.devband.tronwalletforandroid.ui.representative;

import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class RepresentativeActivityModule {

    @Binds
    public abstract RepresentativeView view(RepresentativeActivity representativeActivity);

    @Provides
    static RepresentativePresenter provideRepresentativePresenter(RepresentativeView view, Tron tron,
            WalletAppManager walletAppManager, RxJavaSchedulers rxJavaSchedulers) {
        return new RepresentativePresenter(view, tron, walletAppManager, rxJavaSchedulers);
    }
}
