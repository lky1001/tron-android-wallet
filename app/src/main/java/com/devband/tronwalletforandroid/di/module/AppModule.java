package com.devband.tronwalletforandroid.di.module;

import android.app.Application;
import android.content.Context;

import com.devband.tronlib.TronNetwork;
import com.devband.tronwalletforandroid.di.ApplicationContext;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulersImpl;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class AppModule {

    @Binds
    @ApplicationContext
    abstract Context bindContext(Application application);

    @Provides
    @Singleton
    static Tron provideTron(@ApplicationContext Context context) {
        return Tron.getInstance(context);
    }

    @Provides
    @Singleton
    static TronNetwork provideTronNetwork() {
        return TronNetwork.getInstance();
    }

    @Provides
    @Singleton
    static WalletAppManager provideWalletAppManager(@ApplicationContext Context context) {
        return WalletAppManager.getInstance(context);
    }

    @Provides
    @Singleton
    static RxJavaSchedulers provideRxJavaSchedulers() {
        return new RxJavaSchedulersImpl();
    }
}
