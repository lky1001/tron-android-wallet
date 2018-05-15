package com.devband.tronwalletforandroid.ui.node;

import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface NodeView extends IView {

    void displayNodeList(int count);
    void errorNodeList();

}
