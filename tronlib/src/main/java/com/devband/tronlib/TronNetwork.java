package com.devband.tronlib;

import com.devband.tronlib.dto.CoinMarketCap;
import com.devband.tronlib.dto.Tokens;
import com.devband.tronlib.dto.Transactions;
import com.devband.tronlib.dto.Votes;
import com.devband.tronlib.services.CoinMarketCapService;
import com.devband.tronlib.services.TokenService;
import com.devband.tronlib.services.TronScanService;
import com.devband.tronlib.services.VoteService;

import java.util.List;

import io.reactivex.Single;

public class TronNetwork {

    //https://github.com/grpc-ecosystem/grpc-gateway/blob/master/runtime/errors.go#L15

    private static TronNetwork instance;

    private VoteService mVoteService;
    private CoinMarketCapService mCoinMarketCapService;
    private TronScanService mTronScanService;
    private TokenService mTokenService;

    public static synchronized TronNetwork getInstance() {
        if (instance == null) {
            synchronized (TronNetwork.class) {
                if (instance == null) {
                    instance = new TronNetwork();
                }
            }
        }
        return instance;
    }

    private TronNetwork() {
        mVoteService = ServiceBuilder.createService(VoteService.class, Hosts.TRONSCAM_API);
        mCoinMarketCapService = ServiceBuilder.createService(CoinMarketCapService.class, Hosts.COINMARKETCAP_API);
        mTronScanService = ServiceBuilder.createService(TronScanService.class, Hosts.TRONSCAM_API);
        mTokenService = ServiceBuilder.createService(TokenService.class, Hosts.TRONSCAM_API);
    }

    public Single<Votes> getVoteCurrentCycle() {
        return mVoteService.getVoteCurrentCycle();
    }

    public Single<List<CoinMarketCap>> getCoinInfo(String symbol) {
        return mCoinMarketCapService.getPrice(symbol);
    }

    public Single<Transactions> getTransactions(String address, String symbol) {
        return mTronScanService.getTransactions(address, symbol);
    }

    public Single<Tokens> getTokens(int start, int limit, String sort, String status) {
        return mTokenService.getTokens(start, limit, sort, status);
    }
}
