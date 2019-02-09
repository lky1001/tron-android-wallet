package com.devband.tronlib.services;

import com.devband.tronlib.dto.TriggerRequest;
import com.devband.tronlib.dto.TriggerResult;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TronGridService {

    @POST("wallet/triggersmartcontract")
    public Single<TriggerResult> triggerSmartContract(@Body TriggerRequest request);
}
