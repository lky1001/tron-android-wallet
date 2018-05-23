package com.devband.tronlib.services;

import com.devband.tronlib.dto.Tokens;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TokenService {

    @GET("/api/token")
    Single<Tokens> getTokens(@Query("start") int start, @Query("limit") int limit,
            @Query("sort") String sort, @Query("status") String status);
}
