package com.devband.tronlib.services;

import com.devband.tronlib.dto.Account;
import com.devband.tronlib.dto.TopAddressAccounts;
import com.devband.tronlib.dto.TronAccounts;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AccountService {

    @POST("v1/account")
    Single<Account> getAccount(@Body Account account);

    @POST("v1/account/list")
    Single<List<Account>> getAccountList();

    @GET("/api/account")
    Single<TronAccounts> getAccounts(@Query("start") int start, @Query("limit") int limit,
            @Query("sort") String sort);

    @GET("api/account")
    Single<TopAddressAccounts> getTopAddressAccounts(@Query("sort") String sort,
                                                     @Query("limit") int limit);
}
