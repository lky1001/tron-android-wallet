package com.devband.tronwalletforandroid.ui.blockexplorer.block;


import com.devband.tronlib.dto.Blocks;
import com.devband.tronwalletforandroid.ui.mvp.IView;

/**
 * Created by user on 2018. 5. 25..
 */

public interface BlockView extends IView {
    void blockDataLoadSuccess(Blocks blocks, boolean added);
    void showLoadingDialog();
    void showServerError();
}
