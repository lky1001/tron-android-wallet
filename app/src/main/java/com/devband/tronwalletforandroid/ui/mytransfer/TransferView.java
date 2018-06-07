package com.devband.tronwalletforandroid.ui.mytransfer;

import com.devband.tronwalletforandroid.ui.mvp.IView;
import com.devband.tronwalletforandroid.ui.mytransfer.dto.TransferInfo;

import java.util.List;

/**
 * Created by user on 2018. 5. 17..
 */

public interface TransferView extends IView {

    void transferDataLoadSuccess(long total);

    void showLoadingDialog();

    void showServerError();
}
