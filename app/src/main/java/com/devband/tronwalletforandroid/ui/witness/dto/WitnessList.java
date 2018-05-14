package com.devband.tronwalletforandroid.ui.witness.dto;

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
public class WitnessList {

    private int witnessCount;

    private long highestVotes;

    private List<Witness> witnessList;
}
