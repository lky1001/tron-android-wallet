package com.devband.tronwalletforandroid.ui.myaccount;

import com.devband.tronwalletforandroid.database.AppDatabase;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class MyAccountActivityModule {

    @Binds
    public abstract MyAccountView view(MyAccountActivity myAccountActivity);

    @Provides
    static MyAccountPresenter provideMyAccountPresenter(MyAccountView myAccountView, Tron tron,
            WalletAppManager walletAppManager, RxJavaSchedulers rxJavaSchedulers, AppDatabase appDatabase) {
        return new MyAccountPresenter(myAccountView, tron, walletAppManager, rxJavaSchedulers,
                appDatabase);
    }
}
