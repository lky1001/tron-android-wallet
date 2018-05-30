package com.devband.tronwalletforandroid.tron;

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

    Single<Protocol.Transaction> createTransaction(Contract.TransferContract contract);

    Single<Protocol.Transaction> createTransaction(Contract.FreezeBalanceContract contract);

    Single<Protocol.Transaction> createTransaction(Contract.WithdrawBalanceContract contract);

    Single<Protocol.Transaction> createTransaction(Contract.UnfreezeBalanceContract contract);

    Single<Protocol.Transaction> createTransaction(Contract.UnfreezeAssetContract contract);

    Single<Protocol.Transaction> createTransaction(Contract.VoteWitnessContract contract);

    Single<Protocol.Transaction> createTransferAssetTransaction(Contract.TransferAssetContract contract);

    Single<Protocol.Transaction> createParticipateAssetIssueTransaction(Contract.ParticipateAssetIssueContract contract);

    Single<Boolean> broadcastTransaction(Protocol.Transaction transaction);
}
