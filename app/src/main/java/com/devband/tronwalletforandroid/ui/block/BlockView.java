package com.devband.tronwalletforandroid.ui.block;


import com.devband.tronlib.dto.Blocks;
import com.devband.tronwalletforandroid.ui.mvp.IView;

import java.util.List;

/**
 * Created by user on 2018. 5. 25..
 */

public interface BlockView extends IView {
    void blockDataLoadSuccess(Blocks blocks, boolean added);
    void showLoadingDialog();
    void showServerError();
}
