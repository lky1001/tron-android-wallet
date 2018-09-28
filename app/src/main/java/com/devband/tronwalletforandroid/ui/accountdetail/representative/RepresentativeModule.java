package com.devband.tronwalletforandroid.ui.accountdetail.representative;

import com.devband.tronwalletforandroid.di.FragmentScoped;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class RepresentativeModule {

    @FragmentScoped
    @ContributesAndroidInjector(modules = {RepresentativeFragmentModule.class})
    public abstract RepresentativeFragment contributeRepresentativeFragment();
}