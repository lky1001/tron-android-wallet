package com.devband.tronlib;

import com.devband.tronlib.dto.Blocks;
import com.devband.tronlib.dto.CoinMarketCap;
import com.devband.tronlib.dto.Market;
import com.devband.tronlib.dto.Token;
import com.devband.tronlib.dto.TokenHolders;
import com.devband.tronlib.dto.Tokens;
import com.devband.tronlib.dto.Transactions;
import com.devband.tronlib.dto.TronAccounts;
import com.devband.tronlib.dto.Votes;
import com.devband.tronlib.services.AccountService;
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
    private AccountService mAccountService;

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
        mVoteService = ServiceBuilder.createService(VoteService.class, Hosts.TRONSCAN_API);
        mCoinMarketCapService = ServiceBuilder.createService(CoinMarketCapService.class, Hosts.COINMARKETCAP_API);
        mTronScanService = ServiceBuilder.createService(TronScanService.class, Hosts.TRONSCAN_API);
        mTokenService = ServiceBuilder.createService(TokenService.class, Hosts.TRONSCAN_API);
        mAccountService = ServiceBuilder.createService(AccountService.class, Hosts.TRONSCAN_API);
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

    public Single<Token> getTokenDetail(String tokenName) {
        return mTokenService.getTokenDetail(tokenName);
    }

    public Single<TokenHolders> getTokenHolders(String tokenName, int start, int limit, String sort) {
        return mTokenService.getTokenHolders(tokenName, start, limit, sort);
    }

    public Single<List<Market>> getMarkets() {
        return mTronScanService.getMarket();
    }

    public Single<Blocks> getBlocks(int limit, int start) {
        return mTronScanService.getBlock("-number", limit, start);
    }

    public Single<TronAccounts> getAccounts(int start, int limit, String sort) {
        return mAccountService.getAccounts(start, limit, sort);
    }
}
