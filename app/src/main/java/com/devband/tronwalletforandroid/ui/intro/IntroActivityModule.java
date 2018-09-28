package com.devband.tronwalletforandroid.ui.intro;

import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class IntroActivityModule {

    @Binds
    public abstract IntroView view(IntroActivity introActivity);

    @Provides
    static IntroPresenter provideIntroPresenter(IntroView introView, Tron tron,
            WalletAppManager walletAppManager, RxJavaSchedulers rxJavaSchedulers) {
        return new IntroPresenter(introView, tron, walletAppManager, rxJavaSchedulers);
    }
}
