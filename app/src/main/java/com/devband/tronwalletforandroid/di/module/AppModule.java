package com.devband.tronwalletforandroid.di.module;

import android.app.Application;
import android.content.Context;

import com.devband.tronlib.Hosts;
import com.devband.tronlib.ServiceBuilder;
import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.services.AccountService;
import com.devband.tronlib.services.CoinMarketCapService;
import com.devband.tronlib.services.TokenService;
import com.devband.tronlib.services.TronScanService;
import com.devband.tronlib.services.VoteService;
import com.devband.tronwalletforandroid.common.CustomPreference;
import com.devband.tronwalletforandroid.di.ApplicationContext;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulersImpl;
import com.devband.tronwalletforandroid.tron.AccountManager;
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
    static CustomPreference provideCustomPreference(@ApplicationContext Context context) {
        CustomPreference customPreference = new CustomPreference(context);

        return customPreference;
    }

    @Provides
    @Singleton
    static VoteService provideVoteService() {
        return ServiceBuilder.createService(VoteService.class, Hosts.TRONSCAN_API);
    }

    @Provides
    @Singleton
    static CoinMarketCapService provideCoinMarketCapService() {
        return ServiceBuilder.createService(CoinMarketCapService.class, Hosts.TRONSCAN_API);
    }

    @Provides
    @Singleton
    static TronScanService provideTronScanService() {
        return ServiceBuilder.createService(TronScanService.class, Hosts.TRONSCAN_API);
    }

    @Provides
    @Singleton
    static TokenService provideTokenService() {
        return ServiceBuilder.createService(TokenService.class, Hosts.TRONSCAN_API);
    }

    @Provides
    @Singleton
    static AccountService provideAccountService() {
        return ServiceBuilder.createService(AccountService.class, Hosts.TRONSCAN_API);
    }

    @Provides
    @Singleton
    static TronNetwork provideTronNetwork(VoteService voteService, CoinMarketCapService coinMarketCapService,
            TronScanService tronScanService, TokenService tokenService, AccountService accountService) {
        TronNetwork tronNetwork = new TronNetwork();

        tronNetwork.setVoteService(voteService);
        tronNetwork.setCoinMarketCapService(coinMarketCapService);
        tronNetwork.setTronScanService(tronScanService);
        tronNetwork.setTokenService(tokenService);
        tronNetwork.setAccountService(accountService);

        return tronNetwork;
    }

    @Provides
    @Singleton
    static AccountManager provideAccountManager(@ApplicationContext Context context) {
        return new AccountManager(true, AccountManager.PERSISTENT_LOCAL_DB, context);
    }


    @Provides
    @Singleton
    static Tron provideTron(@ApplicationContext Context context, TronNetwork tronNetwork,
            CustomPreference customPreference, AccountManager accountManager) {
        Tron tron = new Tron(context, tronNetwork, customPreference, accountManager);

        return tron;
    }

    @Provides
    @Singleton
    static WalletAppManager provideWalletAppManager(@ApplicationContext Context context) {
        return new WalletAppManager(context);
    }

    @Provides
    @Singleton
    static RxJavaSchedulers provideRxJavaSchedulers() {
        return new RxJavaSchedulersImpl();
    }
}
