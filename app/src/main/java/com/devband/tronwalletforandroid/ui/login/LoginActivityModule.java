package com.devband.tronwalletforandroid.ui.login;

import com.devband.tronwalletforandroid.common.CustomPreference;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class LoginActivityModule {

    @Binds
    public abstract LoginView view(LoginActivity loginActivity);

    @Provides
    static LoginPresenter provideLoginPresenter(LoginView loginView, Tron tron,
            WalletAppManager walletAppManager, RxJavaSchedulers rxJavaSchedulers,
            CustomPreference customPreference) {
        return new LoginPresenter(loginView, tron, walletAppManager, rxJavaSchedulers, customPreference);
    }
}
