package com.devband.tronwalletforandroid.ui.witness.dto;

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
public class Witness {

    private String url;

    private Long voteCount;

    private long latestBlockNum;

    private long totalMissed;

    private long totalProduced;
}
