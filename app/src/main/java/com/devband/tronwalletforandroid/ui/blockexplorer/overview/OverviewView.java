package com.devband.tronwalletforandroid.ui.blockexplorer.overview;

import com.devband.tronlib.dto.BlockStats;
import com.devband.tronlib.dto.SystemStatus;
import com.devband.tronlib.dto.TopAddressAccounts;
import com.devband.tronwalletforandroid.ui.mvp.IView;

import java.util.List;

/**
 * Created by user on 2018. 5. 28..
 */

public interface OverviewView extends IView {

    void getBlockStatus(SystemStatus systemStatus);
    void overviewDataLoadSuccess(TopAddressAccounts topAddressAccounts);
    void overviewTransferPastHour();
    void overviewTransactionPastHour();
    void overviewAvgBlockSize(List<BlockStats> stats);
    void showLoadingDialog();
    void showServerError();

}
