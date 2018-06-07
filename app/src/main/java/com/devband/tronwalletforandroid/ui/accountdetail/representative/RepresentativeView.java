package com.devband.tronwalletforandroid.ui.accountdetail.representative;

import com.devband.tronwalletforandroid.ui.accountdetail.representative.model.BaseModel;
import com.devband.tronwalletforandroid.ui.mvp.IView;

import java.util.List;

public interface RepresentativeView extends IView {
    void dataLoadSuccess(List<BaseModel> viewModels);
    void showLoadingDialog();
    void showServerError();
}
