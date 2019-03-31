package com.devband.tronwalletforandroid.ui.sendtrc20;

import com.devband.tronwalletforandroid.database.model.Trc20ContractModel;
import com.devband.tronwalletforandroid.ui.mvp.IView;

import java.util.List;

public interface SendTrc20View extends IView {

    void showLoadingDialog();
    void setTrc20List(List<Trc20ContractModel> trc20ContractModels);

    void success();
    void failed();
    void invalidPassword();
}
