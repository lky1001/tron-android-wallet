package com.devband.tronwalletforandroid.ui.previewwallet;

import com.devband.tronlib.TronNetwork;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class PreviewWalletActivityModule {

    @Binds
    public abstract PreviewWalletView view(PreviewWalletActivity previewWalletActivity);

    @Provides
    static PreviewWalletPresenter providePreviewWalletPresenter(PreviewWalletView previewWalletView, Tron tron,
            TronNetwork tronNetwork, RxJavaSchedulers rxJavaSchedulers) {
        return new PreviewWalletPresenter(previewWalletView, tron, tronNetwork, rxJavaSchedulers);
    }
}
