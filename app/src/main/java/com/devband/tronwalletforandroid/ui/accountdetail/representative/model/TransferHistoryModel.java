package com.devband.tronwalletforandroid.ui.accountdetail.representative.model;

import com.devband.tronlib.dto.Transfer;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by user on 2018. 5. 29..
 */

@Getter
@Setter
public class TransferHistoryModel implements BaseModel {

    private Transfer transfer;

    public TransferHistoryModel(Transfer transfer) {
        this.transfer = transfer;
    }

    @Override
    public ViewType getViewType() {
        return ViewType.TRANSFER_HISTORY_ITEM;
    }
}
