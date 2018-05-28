package com.devband.tronwalletforandroid.ui.detail_block;

import com.devband.tronlib.dto.TransactionStats;
import com.devband.tronwalletforandroid.ui.mvp.IView;

/**
 * Created by user on 2018. 5. 28..
 */

public interface DetailBlockView extends IView {
    void transactionStatsLoadSuccess(TransactionStats data);
    void showLoadingDilaog();
    void showServerError();
}
