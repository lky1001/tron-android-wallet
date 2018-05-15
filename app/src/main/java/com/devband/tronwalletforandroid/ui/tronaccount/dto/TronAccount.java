package com.devband.tronwalletforandroid.ui.tronaccount.dto;

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

    private String address;

    private Long balance;
}
