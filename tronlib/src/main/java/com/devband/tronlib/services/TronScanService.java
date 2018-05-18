package com.devband.tronlib.services;

import com.devband.tronlib.dto.Transactions;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by user on 2018. 5. 17..
 */

public interface TronScanService {
    @GET("api/transaction")
    Single<Transactions> getTransactions(@Query("address") String address, @Query("token") String token);
}
