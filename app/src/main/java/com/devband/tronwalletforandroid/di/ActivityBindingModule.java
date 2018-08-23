package com.devband.tronwalletforandroid.di;

import com.devband.tronwalletforandroid.ui.about.AboutActivity;
import com.devband.tronwalletforandroid.ui.backupaccount.BackupAccountActivity;
import com.devband.tronwalletforandroid.ui.backupaccount.BackupAccountActivityModule;
import com.devband.tronwalletforandroid.ui.createwallet.CreateWalletActivity;
import com.devband.tronwalletforandroid.ui.createwallet.CreateWalletActivityModule;
import com.devband.tronwalletforandroid.ui.importkey.ImportPrivateKeyActivity;
import com.devband.tronwalletforandroid.ui.importkey.ImportPrivateKeyActivityModule;
import com.devband.tronwalletforandroid.ui.intro.IntroActivity;
import com.devband.tronwalletforandroid.ui.intro.IntroActivityModule;
import com.devband.tronwalletforandroid.ui.login.LoginActivity;
import com.devband.tronwalletforandroid.ui.login.LoginActivityModule;
import com.devband.tronwalletforandroid.ui.main.MainActivity;
import com.devband.tronwalletforandroid.ui.main.MainActivityModule;
import com.devband.tronwalletforandroid.ui.market.MarketActivity;
import com.devband.tronwalletforandroid.ui.market.MarketActivityModule;

import dagger.Module;
import dagger.android.AndroidInjectionModule;
import dagger.android.ContributesAndroidInjector;

@Module(includes = AndroidInjectionModule.class)
public abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = { IntroActivityModule.class })
    abstract IntroActivity bindIntroActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { LoginActivityModule.class })
    abstract LoginActivity bindLoginActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract AboutActivity bindAboutActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { BackupAccountActivityModule.class })
    abstract BackupAccountActivity bindBackupAccountActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { CreateWalletActivityModule.class })
    abstract CreateWalletActivity bindCreateWalletActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { ImportPrivateKeyActivityModule.class })
    abstract ImportPrivateKeyActivity bindImportPrivateKeyActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { MainActivityModule.class })
    abstract MainActivity bindMainActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = { MarketActivityModule.class })
    abstract MarketActivity bindMarketActivity();
}
