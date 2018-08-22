package com.devband.tronwalletforandroid.ui.intro;

import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public abstract class IntroActivityModule {

    @Binds
    public abstract IntroView view(IntroActivity introActivity);

    @Provides
    static IntroPresenter provideIntroPresenter(IntroView introView, Tron tron,
            WalletAppManager walletAppManager) {
        return new IntroPresenter(introView, tron, walletAppManager, Schedulers.io(), AndroidSchedulers.mainThread());
    }
}
