package com.devband.tronwalletforandroid.ui.blockexplorer.overview;

import com.devband.tronlib.dto.Stat;
import com.devband.tronlib.dto.SystemStatus;
import com.devband.tronlib.dto.TopAddressAccounts;
import com.devband.tronwalletforandroid.ui.mvp.IView;

import java.util.List;

/**
 * Created by user on 2018. 5. 28..
 */

public interface OverviewView extends IView {

    void overviewBlockStatus(SystemStatus systemStatus);
    void overviewDataLoadSuccess(TopAddressAccounts topAddressAccounts);
    void overviewTransferPastHour(List<Stat> stats);
    void overviewTransactionPastHour(List<Stat> stats);
    void overviewAvgBlockSize(List<Stat> stats);
    void showLoadingDialog();
    void showServerError();

}
