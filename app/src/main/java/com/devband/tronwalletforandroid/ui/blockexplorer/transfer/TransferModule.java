package com.devband.tronwalletforandroid.ui.blockexplorer.transfer;

import com.devband.tronwalletforandroid.di.FragmentScoped;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class TransferModule {

    @FragmentScoped
    @ContributesAndroidInjector(modules = {TransferFragmentModule.class})
    public abstract TransferFragment contributeTransferFragment();
}