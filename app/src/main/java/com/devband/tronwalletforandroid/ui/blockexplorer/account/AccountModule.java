package com.devband.tronwalletforandroid.ui.blockexplorer.account;

import com.devband.tronwalletforandroid.di.FragmentScoped;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class AccountModule {

    @FragmentScoped
    @ContributesAndroidInjector(modules = {AccountFragmentModule.class})
    public abstract AccountFragment contributeAccountFragment();
}