package com.devband.tronlib.services;

import com.devband.tronlib.dto.Account;
import com.devband.tronlib.dto.TronAccounts;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccountService {

    @GET("api/account/{address}")
    Single<Account> getAccount(@Path("address") String address);

    @GET("api/account")
    Single<TronAccounts> getAccounts(@Query("start") int start, @Query("limit") int limit,
            @Query("sort") String sort);
}
