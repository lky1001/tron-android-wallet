package com.devband.tronwalletforandroid.ui.transaction;

import com.devband.tronwalletforandroid.ui.mvp.IView;
import com.devband.tronwalletforandroid.ui.transaction.dto.TransactionInfo;

import java.util.List;

/**
 * Created by user on 2018. 5. 17..
 */

public interface TransactionView extends IView {

    void transactionDataLoadSuccess(List<TransactionInfo> transactionInfos);

    void showLoadingDialog();

    void showServerError();
}
