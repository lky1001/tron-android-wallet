package com.devband.tronwalletforandroid.ui.accountdetail.overview;

import com.devband.tronlib.TronNetwork;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class OverviewFragmentModule {

    @Binds
    public abstract OverviewView view(OverviewFragment fragment);

    @Provides
    static OverviewPresenter provideOverviewPresenter(OverviewView view, Tron tron,  TronNetwork tronNetwork,
            RxJavaSchedulers rxJavaSchedulers) {
        return new OverviewPresenter(view, tron, tronNetwork, rxJavaSchedulers);
    }
}