package com.devband.tronlib;

import com.devband.tronlib.dto.Account;
import com.devband.tronlib.dto.AccountMedia;
import com.devband.tronlib.dto.AccountVotes;
import com.devband.tronlib.dto.Blocks;
import com.devband.tronlib.dto.CoinMarketCap;
import com.devband.tronlib.dto.Market;
import com.devband.tronlib.dto.RichData;
import com.devband.tronlib.dto.Stat;
import com.devband.tronlib.dto.SystemStatus;
import com.devband.tronlib.dto.Token;
import com.devband.tronlib.dto.TokenHolders;
import com.devband.tronlib.dto.Tokens;
import com.devband.tronlib.dto.TopAddressAccounts;
import com.devband.tronlib.dto.TransactionStats;
import com.devband.tronlib.dto.Transactions;
import com.devband.tronlib.dto.TransferStats;
import com.devband.tronlib.dto.Transfers;
import com.devband.tronlib.dto.TronAccounts;
import com.devband.tronlib.dto.Votes;
import com.devband.tronlib.dto.Witnesses;
import com.devband.tronlib.services.AccountService;
import com.devband.tronlib.services.CoinMarketCapService;
import com.devband.tronlib.services.TokenService;
import com.devband.tronlib.services.TronScanService;
import com.devband.tronlib.services.VoteService;

import java.util.List;
import java.util.Map;

import io.reactivex.Single;

public class TronNetwork {

    //https://github.com/grpc-ecosystem/grpc-gateway/blob/master/runtime/errors.go#L15

    private VoteService mVoteService;
    private CoinMarketCapService mCoinMarketCapService;
    private TronScanService mTronScanService;
    private TokenService mTokenService;
    private AccountService mAccountService;

    public void setVoteService(VoteService voteService) {
        this.mVoteService = voteService;
    }

    public void setCoinMarketCapService(CoinMarketCapService coinMarketCapService) {
        this.mCoinMarketCapService = coinMarketCapService;
    }

    public void setTronScanService(TronScanService tronScanService) {
        this.mTronScanService = tronScanService;
    }

    public void setTokenService(TokenService tokenService) {
        this.mTokenService = tokenService;
    }

    public void setAccountService(AccountService accountService) {
        this.mAccountService = accountService;
    }

    public Single<Witnesses> getVoteWitnesses() {
        return mVoteService.getVoteWitnesses();
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

    public Single<Account> getAccount(String address) {
        return mAccountService.getAccount(address);
    }

    public Single<TronAccounts> getAccounts(long start, int limit, String sort) {
        return mAccountService.getAccounts(start, limit, sort);
    }

    public Single<TopAddressAccounts> getTopAddressAccounts(int limit) {
        return mAccountService.getTopAddressAccounts("-balance", limit);
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

    public Single<List<Stat>> getAvgBlockSize() {
        return mTronScanService.getBlockStats("avg-block-size");
    }

    public Single<TransferStats> getTransferStats() {
        return mTronScanService.getTransferStats("timestamp", "hour");
    }
}
