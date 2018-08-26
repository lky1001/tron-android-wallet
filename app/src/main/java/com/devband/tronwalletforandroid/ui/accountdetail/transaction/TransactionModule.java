package com.devband.tronwalletforandroid.ui.accountdetail.transaction;

import com.devband.tronwalletforandroid.di.FragmentScoped;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class TransactionModule {

    @FragmentScoped
    @ContributesAndroidInjector(modules = {TransactionFragmentModule.class})
    public abstract TransactionFragment contributeTransactionFragment();
}