package com.devband.tronwalletforandroid.ui.address;

import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class AddressActivityModule {

    @Binds
    public abstract AddressView view(AddressActivity addressActivity);

    @Provides
    static AddressPresenter provideAddressPresenter(AddressView addressView, Tron tron,
            RxJavaSchedulers rxJavaSchedulers) {
        return new AddressPresenter(addressView, tron, rxJavaSchedulers);
    }
}
