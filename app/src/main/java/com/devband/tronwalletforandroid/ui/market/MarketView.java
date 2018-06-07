package com.devband.tronwalletforandroid.ui.market;

import com.devband.tronlib.dto.Market;
import com.devband.tronwalletforandroid.ui.mvp.IView;

import java.util.List;

/**
 * Created by user on 2018. 5. 24..
 */

public interface MarketView extends IView {
    void marketDataLoadSuccess(List<Market> markets);
    void showLoadingDialog();
    void showServerError();
}
