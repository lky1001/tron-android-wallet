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
        return Single.just(grpcClient.queryAccount(address));
    }

    @Override
    public Single<GrpcAPI.WitnessList> listWitnesses() {
        return Single.just(grpcClient.listWitnesses());
    }

    @Override
    public Single<GrpcAPI.AssetIssueList> getAssetIssueList() {
        return Single.just(grpcClient.getAssetIssueList());
    }

    @Override
    public Single<GrpcAPI.NodeList> listNodes() {
        return Single.just(grpcClient.listNodes());
    }

    @Override
    public Single<GrpcAPI.AssetIssueList> getAssetIssueByAccount(byte[] address) {
        return Single.just(grpcClient.getAssetIssueByAccount(address));
    }

    @Override
    public Single<GrpcAPI.TransactionExtention> createTransaction(Contract.TransferContract contract) {
        return Single.just(grpcClient.createTransaction(contract));
    }

    @Override
    public Single<GrpcAPI.TransactionExtention> createTransaction(Contract.FreezeBalanceContract contract) {
        return Single.just(grpcClient.createFreezeBalance(contract));
    }

    @Override
    public Single<GrpcAPI.TransactionExtention> createTransaction(Contract.WithdrawBalanceContract contract) {
        return Single.just(grpcClient.createWithdrawBalance(contract));
    }

    @Override
    public Single<GrpcAPI.TransactionExtention> createTransaction(Contract.UnfreezeBalanceContract contract) {
        return Single.just(grpcClient.createUnfreezeBalance(contract));
    }

    @Override
    public Single<GrpcAPI.TransactionExtention> createTransaction(Contract.UnfreezeAssetContract contract) {
        return Single.just(grpcClient.createUnfreezeAsset(contract));
    }

    @Override
    public Single<GrpcAPI.TransactionExtention> createTransaction(Contract.VoteWitnessContract contract) {
        return Single.just(grpcClient.voteWitnessAccount(contract));
    }

    @Override
    public Single<GrpcAPI.TransactionExtention> createTransaction(Contract.TransferAssetContract contract) {
        return Single.just(grpcClient.createTransferAssetTransaction(contract));
    }

    @Override
    public Single<GrpcAPI.TransactionExtention> createTransaction(Contract.ParticipateAssetIssueContract contract) {
        return Single.just(grpcClient.createParticipateAssetIssueTransaction(contract));
    }

    @Override
    public Single<Boolean> broadcastTransaction(Protocol.Transaction transaction) {
        return Single.just(grpcClient.broadcastTransaction(transaction));
    }

    @Override
    public Single<GrpcAPI.BlockExtention> getBlockHeight() {
        return Single.just(grpcClient.getBlock(-1));
    }
}
