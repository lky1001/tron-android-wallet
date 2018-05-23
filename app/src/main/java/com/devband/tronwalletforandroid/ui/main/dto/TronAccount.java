package com.devband.tronwalletforandroid.ui.main.dto;

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

    private long balance;

    private long bandwidth;

    private List<Frozen> frozenList;

    private List<Asset> assetList;
}
