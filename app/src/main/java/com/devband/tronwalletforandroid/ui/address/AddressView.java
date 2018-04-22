package com.devband.tronwalletforandroid.ui.address;

import android.support.annotation.Nullable;

import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface AddressView extends IView {

    void addressResult(@Nullable AddressPresenter.AddressInfo addressInfo);

}
