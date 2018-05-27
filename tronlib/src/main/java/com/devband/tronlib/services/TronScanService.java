package com.devband.tronlib.services;

import com.devband.tronlib.dto.Blocks;
import com.devband.tronlib.dto.Market;
import com.devband.tronlib.dto.Transactions;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by user on 2018. 5. 17..
 */

public interface TronScanService {
    @GET("api/transaction")
    Single<Transactions> getTransactions(@Query("address") String address, @Query("token") String token);

    @GET("api/transaction")
    Single<Transactions> getAllTransactions(@Query("start") int start, @Query("limit") int limit,
            @Query("sort") String sort, @Query("count") boolean count);

    @GET("api/market/markets")
    Single<List<Market>> getMarket();

    @GET("api/block")
    Single<Blocks> getBlock(@Query("sort") String sort, @Query("limit") int limit, @Query("start") int start);
}
