package com.devband.tronwalletforandroid.ui.sendtrc20;

import com.devband.tronwalletforandroid.database.AppDatabase;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class SendTrc20ActivityModule {

    @Binds
    public abstract SendTrc20View view(SendTrc20Activity sendTrc20Activity);

    @Provides
    static SendTrc20Presenter provideRepresentativePresenter(SendTrc20View view, Tron tron,
            AppDatabase appDatabase, RxJavaSchedulers rxJavaSchedulers) {
        return new SendTrc20Presenter(view, tron, appDatabase, rxJavaSchedulers);
    }
}
