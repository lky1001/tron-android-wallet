package com.devband.tronwalletforandroid.ui.vote.dto;

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
public class VoteItemList {

    private long myVotePoint;

    private long totalMyVotes;

    private long voteItemCount;

    private long totalVotes;

    private List<VoteItem> voteItemList;
}
