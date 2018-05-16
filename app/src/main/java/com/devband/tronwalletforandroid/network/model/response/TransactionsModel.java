package com.devband.tronwalletforandroid.network.model.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionsModel extends BaseModel {
    private List<TransactionInfo> data;
    private int total;
}
