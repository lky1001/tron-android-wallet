package com.devband.tronwalletforandroid.ui.main.dto;

import com.devband.tronlib.tronscan.Account;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TronAccount {

    private String name;

    private long balance;

    private long bandwidth;

    private long transactions;

    private long transactionIn;

    private long transactionOut;

    private List<Frozen> frozenList;

    private List<Asset> assetList;

    private Account account;
}
