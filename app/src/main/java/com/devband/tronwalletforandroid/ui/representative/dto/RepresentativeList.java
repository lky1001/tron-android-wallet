package com.devband.tronwalletforandroid.ui.representative.dto;

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
public class RepresentativeList {

    private int representativeCount;

    private long highestVotes;

    private List<Representative> representativeList;
}
