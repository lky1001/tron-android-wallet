package com.devband.tronwalletforandroid.ui.importkey;

import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ImportPrivateKeyActivityModule {

    @Binds
    public abstract ImportPrivateKeyView view(ImportPrivateKeyActivity importPrivateKeyActivity);

    @Provides
    static ImportPrivateKeyPresenter provideImportPrivateKeyPresenter(ImportPrivateKeyView importPrivateKeyView,
            Tron tron, RxJavaSchedulers rxJavaSchedulers) {
        return new ImportPrivateKeyPresenter(importPrivateKeyView, tron, rxJavaSchedulers);
    }
}
