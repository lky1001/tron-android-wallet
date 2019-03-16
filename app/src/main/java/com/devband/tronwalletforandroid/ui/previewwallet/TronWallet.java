package com.devband.tronwalletforandroid.ui.previewwallet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TronWallet {

    private String accountName;
    private String address;
    private double balance;
}
