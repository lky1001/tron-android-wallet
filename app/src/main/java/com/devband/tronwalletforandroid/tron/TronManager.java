package com.devband.tronwalletforandroid.tron;

import com.devband.tronwalletforandroid.tron.grpc.GrpcClient;

import org.tron.api.GrpcAPI;
import org.tron.protos.Contract;
import org.tron.protos.Protocol;

import io.reactivex.Single;

class TronManager implements ITronManager {

    private GrpcClient grpcClient;

    TronManager(String fullNodeHost, String solidityNodeHost) {
        grpcClient = new GrpcClient(fullNodeHost, solidityNodeHost);
    }

    @Override
    public void shutdown() throws InterruptedException {
        grpcClient.shutdown();
    }

    @Override
    public Single<Protocol.Account> queryAccount(byte[] address) {
        return Single.fromCallable(() -> grpcClient.queryAccount(address));
    }

    @Override
    public Single<GrpcAPI.WitnessList> listWitnesses() {
        return Single.fromCallable(() -> grpcClient.listWitnesses());
    }

    @Override
    public Single<GrpcAPI.AssetIssueList> getAssetIssueList() {
        return Single.fromCallable(() -> grpcClient.getAssetIssueList());
    }

    @Override
    public Single<GrpcAPI.NodeList> listNodes() {
        return Single.fromCallable(() -> grpcClient.listNodes());
    }

    @Override
    public Single<GrpcAPI.AssetIssueList> getAssetIssueByAccount(byte[] address) {
        return Single.fromCallable(() -> grpcClient.getAssetIssueByAccount(address));
    }

    @Override
    public Single<GrpcAPI.TransactionExtention> createTransaction(Contract.TransferContract contract) {
        return Single.fromCallable(() -> grpcClient.createTransaction(contract));
    }

    @Override
    public Single<GrpcAPI.TransactionExtention> createTransaction(Contract.FreezeBalanceContract contract) {
        return Single.fromCallable(() -> grpcClient.createFreezeBalance(contract));
    }

    @Override
    public Single<GrpcAPI.TransactionExtention> createTransaction(Contract.WithdrawBalanceContract contract) {
        return Single.fromCallable(() -> grpcClient.createWithdrawBalance(contract));
    }

    @Override
    public Single<GrpcAPI.TransactionExtention> createTransaction(Contract.UnfreezeBalanceContract contract) {
        return Single.fromCallable(() -> grpcClient.createUnfreezeBalance(contract));
    }

    @Override
    public Single<GrpcAPI.TransactionExtention> createTransaction(Contract.UnfreezeAssetContract contract) {
        return Single.fromCallable(() -> grpcClient.createUnfreezeAsset(contract));
    }

    @Override
    public Single<GrpcAPI.TransactionExtention> createTransaction(Contract.VoteWitnessContract contract) {
        return Single.fromCallable(() -> grpcClient.voteWitnessAccount(contract));
    }

    @Override
    public Single<GrpcAPI.TransactionExtention> createTransaction(Contract.TransferAssetContract contract) {
        return Single.fromCallable(() -> grpcClient.createTransferAssetTransaction(contract));
    }

    @Override
    public Single<GrpcAPI.TransactionExtention> createTransaction(Contract.ParticipateAssetIssueContract contract) {
        return Single.fromCallable(() -> grpcClient.createParticipateAssetIssueTransaction(contract));
    }

    @Override
    public Single<Boolean> broadcastTransaction(Protocol.Transaction transaction) {
        return Single.fromCallable(() -> grpcClient.broadcastTransaction(transaction));
    }

    @Override
    public Single<GrpcAPI.BlockExtention> getBlockHeight() {
        return Single.fromCallable(() -> grpcClient.getBlock(-1));
    }
}
