package com.devband.tronwalletforandroid.ui.main;

import com.devband.tronlib.TronNetwork;
import com.devband.tronwalletforandroid.common.CustomPreference;
import com.devband.tronwalletforandroid.database.AppDatabase;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class MainActivityModule {

    @Binds
    public abstract MainView view(MainActivity mainActivity);

    @Provides
    static MainPresenter provideMainPresenter(MainView mainView, Tron tron, WalletAppManager walletAppManager,
            TronNetwork tronNetwork, RxJavaSchedulers rxJavaSchedulers, CustomPreference customPreference,
            AppDatabase appDatabase) {
        return new MainPresenter(mainView, tron, walletAppManager, tronNetwork, rxJavaSchedulers,
                customPreference, appDatabase);
    }
}
