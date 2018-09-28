package com.devband.tronwalletforandroid.ui.token.holder;

import com.devband.tronwalletforandroid.di.FragmentScoped;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class HolderModule {

    @FragmentScoped
    @ContributesAndroidInjector(modules = {HolderFragmentModule.class})
    public abstract HolderFragment contributeHolderFragment();
}