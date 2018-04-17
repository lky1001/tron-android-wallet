package com.devband.tronlib.services;

import com.devband.tronlib.dto.Account;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AccountService {

    @POST("v1/account")
    Single<Account> getAccount(@Body Account account);

    @POST("v1/account/list")
    Single<List<Account>> getAccountList();
}
