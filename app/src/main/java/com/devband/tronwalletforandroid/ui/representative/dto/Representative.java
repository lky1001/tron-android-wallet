package com.devband.tronwalletforandroid.ui.representative.dto;

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
public class Representative {

    private String address;

    private String url;

    private Long voteCount;

    private long latestBlockNum;

    private long totalMissed;

    private long totalProduced;

    private double productivity;
}
