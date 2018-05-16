package com.devband.tronwalletforandroid.network;

import com.devband.tronwalletforandroid.network.model.response.TransactionsModel;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;

public interface ApiService {

    @GET("/api/transaction")
    Single<Response<TransactionsModel>> getTransactions();
}
