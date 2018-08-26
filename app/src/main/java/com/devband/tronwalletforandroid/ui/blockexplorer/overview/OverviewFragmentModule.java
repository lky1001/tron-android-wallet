package com.devband.tronwalletforandroid.ui.blockexplorer.overview;

import com.devband.tronlib.TronNetwork;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class OverviewFragmentModule {

    @Binds
    public abstract OverviewView view(OverviewFragment fragment);

    @Provides
    static OverviewPresenter provideAccountPresenter(OverviewView view, TronNetwork tronNetwork,
            RxJavaSchedulers rxJavaSchedulers) {
        return new OverviewPresenter(view, tronNetwork, rxJavaSchedulers);
    }
}