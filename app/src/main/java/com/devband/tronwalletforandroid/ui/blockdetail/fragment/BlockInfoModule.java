package com.devband.tronwalletforandroid.ui.blockdetail.fragment;

import com.devband.tronwalletforandroid.di.FragmentScoped;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BlockInfoModule {

    @FragmentScoped
    @ContributesAndroidInjector(modules = {BlockInfoFragmentModule.class})
    public abstract BlockInfoFragment contributeBlockInfoFragment();
}