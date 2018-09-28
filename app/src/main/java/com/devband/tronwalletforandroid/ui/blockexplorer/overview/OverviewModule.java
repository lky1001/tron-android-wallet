package com.devband.tronwalletforandroid.ui.blockexplorer.overview;

import com.devband.tronwalletforandroid.di.FragmentScoped;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class OverviewModule {

    @FragmentScoped
    @ContributesAndroidInjector(modules = {OverviewFragmentModule.class})
    public abstract OverviewFragment contributeOverviewFragment();
}