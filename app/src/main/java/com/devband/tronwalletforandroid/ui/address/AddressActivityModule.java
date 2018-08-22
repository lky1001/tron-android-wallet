package com.devband.tronwalletforandroid.ui.address;

import com.devband.tronwalletforandroid.tron.Tron;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public abstract class AddressActivityModule {

    @Binds
    public abstract AddressView view(AddressActivity addressActivity);

    @Provides
    static AddressPresenter provideAddressPresenter(AddressView addressView, Tron tron) {
        return new AddressPresenter(addressView, tron, Schedulers.io(), AndroidSchedulers.mainThread());
    }
}
