package com.devband.tronlib.services;

import com.devband.tronlib.dto.CoinMarketCap;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CoinMarketCapService {

    @POST("v1/ticker/{symbol}")
    Single<List<CoinMarketCap>> getPrice(@Path("symbol") String symbol);
}
