package com.devband.tronwalletforandroid.ui.blockexplorer.overview;

import com.devband.tronlib.dto.TronAccounts;
import com.devband.tronwalletforandroid.ui.mvp.IView;

/**
 * Created by user on 2018. 5. 28..
 */

public interface OverviewView extends IView {

    void overviewDataLoadSuccess(TronAccounts topAddressAccounts);
    void showLoadingDialog();
    void showServerError();

}
