package com.devband.tronlib.services;

import com.devband.tronlib.dto.AccountVotes;
import com.devband.tronlib.dto.Votes;
import com.devband.tronlib.dto.Witnesses;

import java.util.Map;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VoteService {

    @GET("api/vote/current-cycle")
    Single<Votes> getVoteCurrentCycle();

    @GET("api/vote/next-cycle")
    Single<Map<String, Long>> getRemainNextCycle();

    @GET("api/vote")
    Single<AccountVotes> getAccountVotes(@Query("voter") String voterAddress, @Query("start") long start,
            @Query("limit") int limit, @Query("sort") String sort);

    @GET("api/vote/witness")
    Single<Witnesses> getVoteWitnesses();
}
