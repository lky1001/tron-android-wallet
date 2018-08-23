package com.devband.tronwalletforandroid.ui.main;

import com.devband.tronlib.TronNetwork;
import com.devband.tronwalletforandroid.tron.Tron;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public abstract class MainActivityModule {

    @Binds
    public abstract MainView view(MainActivity mainActivity);

    @Provides
    static MainPresenter provideMainPresenter(MainView mainView, Tron tron, TronNetwork tronNetwork) {
        return new MainPresenter(mainView, tron, tronNetwork, Schedulers.io(), AndroidSchedulers.mainThread());
    }
}
