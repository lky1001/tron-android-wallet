package com.devband.tronwalletforandroid.ui.representative;

import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public abstract class RepresentativeActivityModule {

    @Binds
    public abstract RepresentativeView view(RepresentativeActivity representativeActivity);

    @Provides
    static RepresentativePresenter provideRepresentativePresenter(RepresentativeView view, Tron tron,
            WalletAppManager walletAppManager) {
        return new RepresentativePresenter(view, tron, walletAppManager, Schedulers.io(), AndroidSchedulers.mainThread());
    }
}
