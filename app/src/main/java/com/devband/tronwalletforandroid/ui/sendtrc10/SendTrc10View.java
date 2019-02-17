package com.devband.tronwalletforandroid.ui.sendtrc10;

import com.devband.tronwalletforandroid.ui.main.dto.Asset;
import com.devband.tronwalletforandroid.ui.mvp.IView;

import java.util.List;

public interface SendTrc10View extends IView {

    void sendTokenResult(boolean result);

    void invalidPassword();

    void displayAccountInfo(List<Asset> assets);

    void invalidAddress();

    void connectionError();
}
