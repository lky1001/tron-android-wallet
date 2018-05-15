package com.devband.tronwalletforandroid.ui.tronaccount.dto;

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
public class TronAccountList {

    private int accountCount;

    private long highestBalance;

    private List<TronAccount> accountList;
}
