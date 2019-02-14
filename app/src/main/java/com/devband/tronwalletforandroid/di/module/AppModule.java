package com.devband.tronwalletforandroid.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Build;

import com.devband.tronlib.Hosts;
import com.devband.tronlib.ServiceBuilder;
import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.services.AccountService;
import com.devband.tronlib.services.CoinMarketCapService;
import com.devband.tronlib.services.TokenService;
import com.devband.tronlib.services.TronGridService;
import com.devband.tronlib.services.TronScanService;
import com.devband.tronlib.services.VoteService;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.common.CustomPreference;
import com.devband.tronwalletforandroid.common.security.PasswordEncoder;
import com.devband.tronwalletforandroid.common.security.PasswordEncoderImpl;
import com.devband.tronwalletforandroid.common.security.UpdatableBCrypt;
import com.devband.tronwalletforandroid.common.security.keystore.KeyStore;
import com.devband.tronwalletforandroid.common.security.keystore.KeyStoreApi15Impl;
import com.devband.tronwalletforandroid.common.security.keystore.KeyStoreApi18Impl;
import com.devband.tronwalletforandroid.common.security.keystore.KeyStoreApi23Impl;
import com.devband.tronwalletforandroid.database.AppDatabase;
import com.devband.tronwalletforandroid.di.ApplicationContext;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulersImpl;
import com.devband.tronwalletforandroid.tron.AccountManager;
import com.devband.tronwalletforandroid.tron.TokenManager;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;
import com.devband.tronwalletforandroid.tron.repository.LocalDbRepository;

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
        return  new CustomPreference(context);
    }

    @Provides
    @Singleton
    static VoteService provideVoteService() {
        return ServiceBuilder.createService(VoteService.class, Hosts.TRONSCAN_API_LIST);
    }

    @Provides
    @Singleton
    static CoinMarketCapService provideCoinMarketCapService() {
        return ServiceBuilder.createService(CoinMarketCapService.class, Hosts.COINMARKETCAP_API);
    }

    @Provides
    @Singleton
    static TronScanService provideTronScanService() {
        return ServiceBuilder.createService(TronScanService.class, Hosts.TRONSCAN_API_LIST);
    }

    @Provides
    @Singleton
    static TokenService provideTokenService() {
        return ServiceBuilder.createService(TokenService.class, Hosts.TRONSCAN_API_LIST);
    }

    @Provides
    @Singleton
    static AccountService provideAccountService() {
        return ServiceBuilder.createService(AccountService.class, Hosts.TRONSCAN_API_LIST);
    }

    @Provides
    @Singleton
    static TronGridService provideTronGridService() {
        return ServiceBuilder.createService(TronGridService.class, Hosts.TRONGRID_API);
    }

    @Provides
    @Singleton
    static TronNetwork provideTronNetwork(VoteService voteService, CoinMarketCapService coinMarketCapService,
            TronScanService tronScanService, TokenService tokenService, AccountService accountService) {
        return new TronNetwork(voteService, coinMarketCapService, tronScanService,
                tokenService, accountService);
    }

    @Provides
    @Singleton
    static AccountManager provideAccountManager(AppDatabase appDatabase, KeyStore keyStore) {
        return new AccountManager(new LocalDbRepository(appDatabase), keyStore);
    }

    @Provides
    @Singleton
    static KeyStore provideKeyStore(@ApplicationContext Context context, CustomPreference customPreference) {
        KeyStore keyStore = null;

        int keyStoreVersion = customPreference.getInitWallet() ? customPreference.getKeyStoreVersion() : Build.VERSION.SDK_INT;

        if (keyStoreVersion >= Build.VERSION_CODES.M) {
            keyStore = new KeyStoreApi23Impl(customPreference);
        } else if (keyStoreVersion >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            keyStore = new KeyStoreApi18Impl(context);
        } else {
            keyStore = new KeyStoreApi15Impl(customPreference);
        }

        keyStore.init();
        keyStore.createKeys(Constants.ALIAS_SALT);
        keyStore.createKeys(Constants.ALIAS_ACCOUNT_KEY);
        keyStore.createKeys(Constants.ALIAS_PASSWORD_KEY);
        keyStore.createKeys(Constants.ALIAS_ADDRESS_KEY);

        return keyStore;
    }

    @Provides
    @Singleton
    static UpdatableBCrypt provideUpdatableBCrypt() {
        return new UpdatableBCrypt(Constants.SALT_LOG_ROUND);
    }

    @Provides
    @Singleton
    static PasswordEncoder providePasswordEncoder(CustomPreference customPreference, KeyStore keyStore,
            UpdatableBCrypt updatableBCrypt) {
        PasswordEncoderImpl passwordEncoder = new PasswordEncoderImpl(customPreference, keyStore, updatableBCrypt);
        passwordEncoder.init();

        return passwordEncoder;
    }

    @Provides
    @Singleton
    static WalletAppManager provideWalletAppManager(PasswordEncoder passwordEncoder, AppDatabase appDatabase) {
        return new WalletAppManager(passwordEncoder, appDatabase);
    }

    @Provides
    @Singleton
    static TokenManager provideTokenManager(AppDatabase appDatabase) {
        return new TokenManager(appDatabase.trc10AssetDao());
    }

    @Provides
    @Singleton
    static Tron provideTron(@ApplicationContext Context context, TronNetwork tronNetwork, TokenManager tokenManager,
            CustomPreference customPreference, AccountManager accountManager, WalletAppManager walletAppManager) {
        return new Tron(context, tronNetwork, customPreference, accountManager, walletAppManager, tokenManager);
    }

    @Provides
    @Singleton
    static AppDatabase provideAppDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, Constants.DB_NAME)
                .allowMainThreadQueries()
                .addMigrations(AppDatabase.MIGRATION_1_2)
                .addMigrations(AppDatabase.MIGRATION_2_3)
                .addMigrations(AppDatabase.MIGRATION_3_4)
                .addMigrations(AppDatabase.MIGRATION_4_5)
                .addMigrations(AppDatabase.MIGRATION_5_6)
                .build();
    }

    @Provides
    @Singleton
    static RxJavaSchedulers provideRxJavaSchedulers() {
        return new RxJavaSchedulersImpl();
    }
}
