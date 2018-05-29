package com.devband.tronlib.services;

import com.devband.tronlib.dto.Block;
import com.devband.tronlib.dto.Blocks;
import com.devband.tronlib.dto.Market;
import com.devband.tronlib.dto.TransactionStats;
import com.devband.tronlib.dto.Transactions;
import com.devband.tronlib.dto.Transfers;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by user on 2018. 5. 17..
 */

public interface TronScanService {
    @GET("api/transfer")
    Single<Transfers> getTransfers(@Query("address") String address, @Query("token") String token);

    @GET("api/transfer")
    Single<Transfers> getTransfers(@Query("address") String address, @Query("start") long start,
            @Query("limit") int limit, @Query("sort") String sort, @Query("count") boolean count);

    @GET("api/transfer")
    Single<Transfers> getTransfers(@Query("start") long start, @Query("limit") int limit,
            @Query("sort") String sort, @Query("count") boolean count);

    @GET("api/transfer")
    Single<Transfers> getTransfers(@Query("start") int start, @Query("limit") int limit,
                                   @Query("sort") String sort, @Query("count") boolean count,
                                   @Query("address") String address);

    @GET("api/market/markets")
    Single<List<Market>> getMarket();

    @GET("api/block")
    Single<Blocks> getBlocks(@Query("sort") String sort, @Query("limit") int limit, @Query("start") long start);

    @GET("api/block")
    Single<Blocks> getBlock(@Query("sort") String sort, @Query("limit") int limit, @Query("number") long blockNumber);

    @GET("api/account/{address}/stats")
    Single<TransactionStats> getTransactionStats(@Path("address") String address);

    @GET("api/transaction")
    Single<Transactions> getTransactions(@Query("start") long start, @Query("limit") int limit,
            @Query("sort") String sort, @Query("count") boolean count);

    @GET("api/transaction")
    Single<Transactions> getTransactions(@Query("address") String address, @Query("start") long start, @Query("limit") int limit,
            @Query("sort") String sort, @Query("count") boolean count);
}
