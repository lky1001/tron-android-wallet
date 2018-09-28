package com.devband.tronwalletforandroid.ui.sendtoken;

import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class SendTokenActivityModule {

    @Binds
    public abstract SendTokenView view(SendTokenActivity sendTokenActivity);

    @Provides
    static SendTokenPresenter provideRepresentativePresenter(SendTokenView view, Tron tron,
            RxJavaSchedulers rxJavaSchedulers) {
        return new SendTokenPresenter(view, tron, rxJavaSchedulers);
    }
}
