package com.devband.tronwalletforandroid.ui.accountdetail.representative.model;

/**
 * Created by user on 2018. 5. 29..
 */

public enum ViewType {
    BLOCK_STAT, TRANSFER_HISTORY_ITEM, HEADER;

    public static ViewType valueOf(int ordinal) {
        for (ViewType viewType : ViewType.values()) {
            if (viewType.ordinal() == ordinal) {
                return viewType;
            }
        }
        return BLOCK_STAT;
    }
}
