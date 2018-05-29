package com.devband.tronwalletforandroid.ui.detail_block;

import com.devband.tronwalletforandroid.ui.detail_block.model.BaseModel;
import com.devband.tronwalletforandroid.ui.mvp.IView;

import java.util.List;

/**
 * Created by user on 2018. 5. 28..
 */

public interface DetailBlockView extends IView {
    void dataLoadSuccess(List<BaseModel> viewModels);
    void showLoadingDilaog();
    void showServerError();
}
