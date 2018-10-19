package com.devband.tronwalletforandroid.ui.exchange;

import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ExchangeActivityModule {

    @Binds
    public abstract ExchangeView view(ExchangeActivity exchangeActivity);

    @Provides
    static ExchangePresenter provideExchangePresenter(ExchangeView view, Tron tron, RxJavaSchedulers rxJavaSchedulers) {
        return new ExchangePresenter(view, tron, rxJavaSchedulers);
    }
}
