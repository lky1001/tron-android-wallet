package com.devband.tronwalletforandroid.ui.sendtrc20;

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
public class TokenContract {

    private String name;

    private String displayName;

    private double balance;

    private int precision;

    private String contractAddress;
}
