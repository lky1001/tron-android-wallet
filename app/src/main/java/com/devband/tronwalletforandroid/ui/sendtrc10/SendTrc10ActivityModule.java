package com.devband.tronwalletforandroid.ui.sendtrc10;

import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class SendTrc10ActivityModule {

    @Binds
    public abstract SendTrc10View view(SendTrc10Activity sendTrc10Activity);

    @Provides
    static SendTrc10Presenter provideRepresentativePresenter(SendTrc10View view, Tron tron,
            RxJavaSchedulers rxJavaSchedulers) {
        return new SendTrc10Presenter(view, tron, rxJavaSchedulers);
    }
}
