package com.devband.tronwalletforandroid.ui.main;

import com.devband.tronlib.TronNetwork;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class MainActivityModule {

    @Binds
    public abstract MainView view(MainActivity mainActivity);

    @Provides
    static MainPresenter provideMainPresenter(MainView mainView, Tron tron, TronNetwork tronNetwork,
            RxJavaSchedulers rxJavaSchedulers) {
        return new MainPresenter(mainView, tron, tronNetwork, rxJavaSchedulers);
    }
}
