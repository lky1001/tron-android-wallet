package com.devband.tronlib;

import com.devband.tronlib.dto.Account;
import com.devband.tronlib.dto.AccountVotes;
import com.devband.tronlib.dto.Blocks;
import com.devband.tronlib.dto.CoinMarketCap;
import com.devband.tronlib.dto.Market;
import com.devband.tronlib.dto.Token;
import com.devband.tronlib.dto.TokenHolders;
import com.devband.tronlib.dto.Tokens;
import com.devband.tronlib.dto.TopAddressAccounts;
import com.devband.tronlib.dto.TransactionStats;
import com.devband.tronlib.dto.Transactions;
import com.devband.tronlib.dto.Transfers;
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

    public Single<Transfers> getTransfers(String address, String symbol) {
        return mTronScanService.getTransfers(address, symbol);
    }

    public Single<Transfers> getTransfers(String address, int start, int limit, String sort, boolean hasTotal) {
        return mTronScanService.getTransfers(address, start, limit, sort, hasTotal);
    }

    public Single<Transfers> getTransfers(int start, int limit, String sort, boolean hasTotal) {
        return mTronScanService.getTransfers(start, limit, sort, hasTotal);
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

    public Single<Account> getAccount(String address) {
        return mAccountService.getAccount(address);
    }

    public Single<TronAccounts> getAccounts(int start, int limit, String sort) {
        return mAccountService.getAccounts(start, limit, sort);
    }

    public Single<TopAddressAccounts> getTopAddressAccounts(int limit) {
        return mAccountService.getTopAddressAccounts("-balance", limit);
    }

    public Single<TransactionStats> getTransactionStats(String address) {
        return mTronScanService.getTransactionStats(address);
    }

    public Single<AccountVotes> getAccountVotes(String voterAddress, int start, int limit, String sort) {
        return mVoteService.getAccountVotes(voterAddress, start, limit, sort);
    }

    public Single<Transactions> getTransactions(int start, int limit, String sort, boolean hasTotal) {
        return mTronScanService.getTransactions(start, limit, sort, hasTotal);
    }
}
