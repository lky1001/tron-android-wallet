package com.devband.tronwalletforandroid.tron;

import android.text.TextUtils;

import com.devband.tronwalletforandroid.tron.grpc.GrpcClient;
import com.google.protobuf.ByteString;

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

    @Override
    public Single<GrpcAPI.ExchangeList> getExchangeList() {
        return Single.fromCallable(() -> grpcClient.getExchangeList());
    }

    @Override
    public Single<GrpcAPI.ExchangeList> getPaginatedExchangeList(long offset, long limit) {
        GrpcAPI.PaginatedMessage paginatedMessage = GrpcAPI.PaginatedMessage.newBuilder()
                .setOffset(offset)
                .setLimit(limit)
                .build();
        return Single.fromCallable(() -> grpcClient.getPaginatedExchangeList(paginatedMessage));
    }

    @Override
    public Single<Protocol.Exchange> getExchangeById(String id) {
        return Single.fromCallable(() -> grpcClient.getExchangeById(id.getBytes()));
    }

    @Override
    public Single<GrpcAPI.TransactionExtention> createExchangeCreateContract(Contract.ExchangeCreateContract contract) {
        return Single.fromCallable(() -> grpcClient.createExchangeCreateContract(contract));
    }

    @Override
    public Single<GrpcAPI.TransactionExtention> createExchangeInjectContract(Contract.ExchangeInjectContract contract) {
        return Single.fromCallable(() -> grpcClient.createExchangeInjectContract(contract));
    }

    @Override
    public Single<GrpcAPI.TransactionExtention> createExchangeWithdrawContract(Contract.ExchangeWithdrawContract contract) {
        return Single.fromCallable(() -> grpcClient.createExchangeWithdrawContract(contract));
    }

    @Override
    public Single<GrpcAPI.TransactionExtention> createExchangeTransactionContract(Contract.ExchangeTransactionContract contract) {
        return Single.fromCallable(() -> grpcClient.createExchangeTransactionContract(contract));
    }

    @Override
    public Single<Protocol.SmartContract> getSmartContract(byte[] address) {
        return Single.fromCallable(() -> grpcClient.getSmartContract(address));
    }

    @Override
    public Single<GrpcAPI.TransactionExtention> triggerContract(byte[] addressBytes, byte[] contractAddress, long callValue, byte[] input, long feeLimit, long tokenCallValue, String tokenId) {
        return Single.fromCallable(() -> {
            Contract.TriggerSmartContract triggerContract = triggerCallContract(addressBytes, contractAddress, callValue, input, tokenCallValue, tokenId);
            return grpcClient.triggerContract(triggerContract);
        });
    }

    @Override
    public Single<Contract.AssetIssueContract> getAssetIssueById(String id) {
        return Single.fromCallable(() -> grpcClient.getAssetIssueById(id));
    }

    private Contract.TriggerSmartContract triggerCallContract(byte[] address, byte[] contractAddress,
            long callValue, byte[] data, long tokenValue, String tokenId) {
        Contract.TriggerSmartContract.Builder builder = Contract.TriggerSmartContract.newBuilder();
        builder.setOwnerAddress(ByteString.copyFrom(address));
        builder.setContractAddress(ByteString.copyFrom(contractAddress));
        builder.setData(ByteString.copyFrom(data));
        builder.setCallValue(callValue);

        if (!TextUtils.isEmpty(tokenId)) {
            builder.setCallTokenValue(tokenValue);
            builder.setTokenId(Long.parseLong(tokenId));
        }
        return builder.build();
    }
}
