package com.devband.tronlib.services;

import com.devband.tronlib.dto.AccountMedia;
import com.devband.tronlib.dto.RichData;
import com.devband.tronlib.dto.TronAccounts;
import com.devband.tronlib.tronscan.Account;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccountService {

    @GET("api/account")
    Single<Account> getAccountInfo(@Query("address") String address);

    @GET("api/account/list")
    Single<TronAccounts> getAccounts(@Query("start") long start, @Query("limit") int limit,
            @Query("sort") String sort);
    
    @GET("api/account/{address}/media")
    Single<AccountMedia> getAccountMedia(@Path("address") String address);

    @GET("api/account/richlist")
    Single<RichData> getRichData();

}
