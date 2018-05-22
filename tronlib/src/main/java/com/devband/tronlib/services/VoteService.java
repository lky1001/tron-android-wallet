package com.devband.tronlib.services;

import com.devband.tronlib.dto.Votes;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface VoteService {

    @GET("api/vote/current-cycle")
    Single<Votes> getVoteCurrentCycle();
}
