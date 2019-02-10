package com.devband.tronlib;

import com.devband.tronlib.dto.AccountMedia;
import com.devband.tronlib.dto.AccountVotes;
import com.devband.tronlib.dto.Blocks;
import com.devband.tronlib.dto.CoinMarketCap;
import com.devband.tronlib.dto.Market;
import com.devband.tronlib.dto.RichData;
import com.devband.tronlib.dto.SystemStatus;
import com.devband.tronlib.dto.Token;
import com.devband.tronlib.dto.TokenHolders;
import com.devband.tronlib.dto.Tokens;
import com.devband.tronlib.dto.TransactionStats;
import com.devband.tronlib.dto.Transactions;
import com.devband.tronlib.dto.Transfers;
import com.devband.tronlib.dto.TronAccounts;
import com.devband.tronlib.dto.Votes;
import com.devband.tronlib.dto.Witnesses;
import com.devband.tronlib.services.AccountService;
import com.devband.tronlib.services.CoinMarketCapService;
import com.devband.tronlib.services.TokenService;
import com.devband.tronlib.services.TronScanService;
import com.devband.tronlib.services.VoteService;
import com.devband.tronlib.tronscan.Account;
import com.devband.tronlib.tronscan.TokenInfos;
import com.devband.tronlib.tronscan.Trc20Tokens;

import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import lombok.NonNull;

public class TronNetwork {

    //https://github.com/grpc-ecosystem/grpc-gateway/blob/master/runtime/errors.go#L15

    private VoteService mVoteService;
    private CoinMarketCapService mCoinMarketCapService;
    private TronScanService mTronScanService;
    private TokenService mTokenService;
    private AccountService mAccountService;

    public TronNetwork(VoteService voteService, CoinMarketCapService coinMarketCapService,
            TronScanService tronScanService, TokenService tokenService, AccountService accountService) {
        this.mVoteService = voteService;
        this.mCoinMarketCapService = coinMarketCapService;
        this.mTronScanService = tronScanService;
        this.mTokenService = tokenService;
        this.mAccountService = accountService;
    }

    public Single<Witnesses> getVoteWitnesses() {
        return mVoteService.getVoteWitnesses();
    }

    public Single<Votes> getVoteCurrentCycle() {
        return mVoteService.getVoteCurrentCycle();
    }

    public Single<Map<String, Long>> getRemainNextCycle() {
        return mVoteService.getRemainNextCycle();
    }

    public Single<List<CoinMarketCap>> getCoinInfo(String symbol) {
        return mCoinMarketCapService.getPrice(symbol);
    }

    public Single<Transfers> getTransfers(String sort, boolean hasCount, int limit,
            long start, long block) {
        return mTronScanService.getTransfers(sort, hasCount, limit, start, block);
    }

    public Single<Transfers> getTransfers(String sort, boolean hasCount, int limit,
            long start, String tokenName) {
        return mTronScanService.getTransfers(sort, hasCount, limit, start, tokenName);
    }

    public Single<Transfers> getTransfersByAddress(String sort, boolean hasTotal, int limit, long start, String address) {
        return mTronScanService.getTransfersByAddress(sort, hasTotal, limit, start, address);
    }

    public Single<Transfers> getTransfers(String address, String symbol) {
        return mTronScanService.getTransfers(address, symbol);
    }

    public Single<Transfers> getTransfers(String address, long start, int limit, String sort, boolean hasTotal) {
        return mTronScanService.getTransfers(address, start, limit, sort, hasTotal);
    }

    public Single<Transfers> getTransfers(long start, int limit, String sort, boolean hasTotal) {
        return mTronScanService.getTransfers(start, limit, sort, hasTotal);
    }

    public Single<Transfers> getTransfers(int start, int limit, String sort, boolean hasTotal, String address) {
        return mTronScanService.getTransfers(start, limit, sort, hasTotal, address);
    }

    public Single<Tokens> getTokens(long start, int limit, String sort, String status) {
        return mTokenService.getTokens(start, limit, sort, status);
    }

    public Single<Token> getTokenDetail(String tokenName) {
        return mTokenService.getTokenDetail(tokenName);
    }

    public Single<TokenHolders> getTokenHolders(String tokenName, long start, int limit, String sort) {
        return mTokenService.getTokenHolders(tokenName, start, limit, sort);
    }

    public Single<Tokens> findTokens(String query, long start, int limit, String sort) {
        return mTokenService.findTokens(query, start, limit, sort);
    }

    public Single<List<Market>> getMarkets() {
        return mTronScanService.getMarket();
    }

    public Single<Blocks> getBlocks(int limit, long start) {
        return mTronScanService.getBlocks("-number", limit, start);
    }

    public Single<Blocks> getBlock(long blockNumber) {
        return mTronScanService.getBlock("-number", 1, blockNumber);
    }

    public Single<RichData> getRichList() {
        return mAccountService.getRichData();
    }

    public Single<Account> getAccountInfo(String address) {
        return mAccountService.getAccountInfo(address);
    }

    public Single<TronAccounts> getAccounts(long start, int limit, String sort) {
        return mAccountService.getAccounts(start, limit, sort);
    }

    public Single<TransactionStats> getTransactionStats(String address) {
        return mTronScanService.getTransactionStats(address);
    }

    public Single<AccountVotes> getAccountVotes(String voterAddress, long start, int limit, String sort) {
        return mVoteService.getAccountVotes(voterAddress, start, limit, sort);
    }

    public Single<Transactions> getTransactions(long start, int limit, String sort, boolean hasTotal) {
        return mTronScanService.getTransactions(start, limit, sort, hasTotal);
    }

    public Single<Transactions> getTransactions(String address, long start, int limit, String sort, boolean hasTotal) {
        return mTronScanService.getTransactions(address, start, limit, sort, hasTotal);
    }

    public Single<Transactions> getTransactions(long block, long start, int limit, String sort, boolean hasTotal) {
        return mTronScanService.getTransactions(sort, hasTotal, limit, start, block);
    }

    public Single<AccountMedia> getAccountMedia(String address) {
        return mAccountService.getAccountMedia(address);
    }

    public Single<SystemStatus> getSystemStatus() {
        return mTronScanService.getStatus();
    }

    public Single<TokenInfos> getTokenInfo(String id) {
        return mTronScanService.getTokenInfo(id, 1);
    }

    public Single<Trc20Tokens> getTrc20TokenList() {
        return mTokenService.getTrc20Tokens("", 0, "issue_time");
    }

    public Single<Trc20Tokens> getTrc20Token(@NonNull String contractAddress) {
        return mTokenService.getTrc20Tokens(contractAddress, 0, "issue_time");
    }
}
