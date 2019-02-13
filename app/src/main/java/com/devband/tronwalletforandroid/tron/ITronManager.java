package com.devband.tronwalletforandroid.tron;

import org.tron.api.GrpcAPI;
import org.tron.api.GrpcAPI.AssetIssueList;
import org.tron.api.GrpcAPI.NodeList;
import org.tron.api.GrpcAPI.WitnessList;
import org.tron.protos.Contract;
import org.tron.protos.Protocol;
import org.tron.protos.Protocol.Account;

import io.reactivex.Single;

public interface ITronManager {

    void shutdown() throws InterruptedException;

    Single<Account> queryAccount(byte[] address);

    Single<WitnessList> listWitnesses();

    Single<AssetIssueList> getAssetIssueList();

    Single<NodeList> listNodes();

    Single<AssetIssueList> getAssetIssueByAccount(byte[] address);

    Single<GrpcAPI.TransactionExtention> createTransaction(Contract.TransferContract contract);

    Single<GrpcAPI.TransactionExtention> createTransaction(Contract.FreezeBalanceContract contract);

    Single<GrpcAPI.TransactionExtention> createTransaction(Contract.WithdrawBalanceContract contract);

    Single<GrpcAPI.TransactionExtention> createTransaction(Contract.UnfreezeBalanceContract contract);

    Single<GrpcAPI.TransactionExtention> createTransaction(Contract.UnfreezeAssetContract contract);

    Single<GrpcAPI.TransactionExtention> createTransaction(Contract.VoteWitnessContract contract);

    Single<GrpcAPI.TransactionExtention> createTransaction(Contract.TransferAssetContract contract);

    Single<GrpcAPI.TransactionExtention> createTransaction(Contract.ParticipateAssetIssueContract contract);

    Single<Boolean> broadcastTransaction(Protocol.Transaction transaction);

    Single<GrpcAPI.BlockExtention> getBlockHeight();

    Single<GrpcAPI.ExchangeList> getExchangeList();

    Single<GrpcAPI.ExchangeList> getPaginatedExchangeList(long offset, long limit);

    Single<Protocol.Exchange> getExchangeById(String id);

    Single<GrpcAPI.TransactionExtention> createExchangeCreateContract(Contract.ExchangeCreateContract contract);

    Single<GrpcAPI.TransactionExtention> createExchangeInjectContract(Contract.ExchangeInjectContract contract);

    Single<GrpcAPI.TransactionExtention> createExchangeWithdrawContract(Contract.ExchangeWithdrawContract contract);

    Single<GrpcAPI.TransactionExtention> createExchangeTransactionContract(Contract.ExchangeTransactionContract contract);

    Single<Protocol.SmartContract> getSmartContract(byte[] address);

    Single<GrpcAPI.TransactionExtention> triggerContract(byte[] addressBytes, byte[] contractAddress, long callValue, byte[] input, long feeLimit, long tokenCallValue, String tokenId);

    Single<Contract.AssetIssueContract> getAssetIssueById(String id);
}
