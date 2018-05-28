package com.devband.tronwalletforandroid.tron.grpc;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.protobuf.ByteString;

import org.tron.api.GrpcAPI;
import org.tron.api.GrpcAPI.AssetIssueList;
import org.tron.api.GrpcAPI.BytesMessage;
import org.tron.api.GrpcAPI.EmptyMessage;
import org.tron.api.GrpcAPI.NodeList;
import org.tron.api.GrpcAPI.NumberMessage;
import org.tron.api.GrpcAPI.WitnessList;
import org.tron.api.WalletGrpc;
import org.tron.api.WalletSolidityGrpc;
import org.tron.protos.Contract;
import org.tron.protos.Contract.AssetIssueContract;
import org.tron.protos.Protocol.Account;
import org.tron.protos.Protocol.Block;
import org.tron.protos.Protocol.Transaction;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * https://github.com/tronprotocol/wallet-cli/blob/master/src/main/java/org/tron/walletserver/GrpcClient.java
 */
public class GrpcClient {

    private ManagedChannel channel;
    private ManagedChannel channelSolidity = null;

    private WalletGrpc.WalletBlockingStub blockingStubFullNode;
    private WalletSolidityGrpc.WalletSolidityBlockingStub blockingStubSolidityNode;

    public GrpcClient(String fullNodeHost, String solidityNodeHost) {
        if (!TextUtils.isEmpty(fullNodeHost)) {
            channel = ManagedChannelBuilder.forTarget(fullNodeHost)
                    .usePlaintext()
                    .build();
            blockingStubFullNode = WalletGrpc.newBlockingStub(channel);
        }

        if (!TextUtils.isEmpty(solidityNodeHost)) {
            channelSolidity = ManagedChannelBuilder.forTarget(solidityNodeHost)
                    .usePlaintext()
                    .build();
            blockingStubSolidityNode = WalletSolidityGrpc.newBlockingStub(channelSolidity);
        }
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public Account queryAccount(byte[] address) {
        ByteString addressBS = ByteString.copyFrom(address);
        Account request = Account.newBuilder().setAddress(addressBS).build();
        return blockingStubFullNode.getAccount(request);
    }

    public Transaction createTransaction(Contract.TransferContract contract) {
        return blockingStubFullNode.createTransaction(contract);
    }

    public Transaction createTransaction(Contract.VoteWitnessContract contract) {
        return blockingStubFullNode.voteWitnessAccount(contract);
    }

    public Transaction createTransferAssetTransaction(Contract.TransferAssetContract contract) {
        return blockingStubFullNode.transferAsset(contract);
    }

    public Transaction createParticipateAssetIssueTransaction(
            Contract.ParticipateAssetIssueContract contract) {
        return blockingStubFullNode.participateAssetIssue(contract);
    }

    public Transaction createAssetIssue(Contract.AssetIssueContract contract) {
        return blockingStubFullNode.createAssetIssue(contract);
    }

    public Transaction voteWitnessAccount(Contract.VoteWitnessContract contract) {
        return blockingStubFullNode.voteWitnessAccount(contract);
    }

    public Transaction createWitness(Contract.WitnessCreateContract contract) {
        return blockingStubFullNode.createWitness(contract);
    }

    public boolean broadcastTransaction(Transaction signaturedTransaction) {
        GrpcAPI.Return response = blockingStubFullNode.broadcastTransaction(signaturedTransaction);
        return response.getResult();
    }

    public Block getBlock(long blockNum) {
        if (blockNum < 0) {
            return blockingStubFullNode.getNowBlock(EmptyMessage.newBuilder().build());
        }
        NumberMessage.Builder builder = NumberMessage.newBuilder();
        builder.setNum(blockNum);
        return blockingStubFullNode.getBlockByNum(builder.build());
    }

    @Nullable
    public WitnessList listWitnesses() {
        WitnessList witnessList = blockingStubFullNode.listWitnesses(EmptyMessage.newBuilder().build());
        return witnessList;
    }

    @Nullable
    public AssetIssueList getAssetIssueList() {
        AssetIssueList assetIssueList = blockingStubFullNode
                .getAssetIssueList(EmptyMessage.newBuilder().build());
        return assetIssueList;
    }

    @Nullable
    public NodeList listNodes() {
        NodeList nodeList = blockingStubFullNode
                .listNodes(EmptyMessage.newBuilder().build());
        return nodeList;
    }

    @Nullable
    public AssetIssueList getAssetIssueByAccount(byte[] address) {
        ByteString addressBS = ByteString.copyFrom(address);
        Account request = Account.newBuilder().setAddress(addressBS).build();
        AssetIssueList assetIssueList = blockingStubFullNode
                .getAssetIssueByAccount(request);
        return assetIssueList;
    }

    public AssetIssueContract getAssetIssueByName(String assetName) {
        ByteString assetNameBs = ByteString.copyFrom(assetName.getBytes());
        BytesMessage request = BytesMessage.newBuilder().setValue(assetNameBs).build();
        return blockingStubFullNode.getAssetIssueByName(request);
    }

    public NumberMessage getTotalTransaction() {
        return blockingStubFullNode.totalTransaction(EmptyMessage.newBuilder().build());
    }

    public Transaction createTransaction(Contract.FreezeBalanceContract contract) {
        return blockingStubFullNode.freezeBalance(contract);
    }

    public Transaction createTransaction(Contract.WithdrawBalanceContract contract) {
        return blockingStubFullNode.withdrawBalance(contract);
    }

    public Transaction createTransaction(Contract.UnfreezeBalanceContract contract) {
        return blockingStubFullNode.unfreezeBalance(contract);
    }

    public Transaction createTransaction(Contract.UnfreezeAssetContract contract) {
        return blockingStubFullNode.unfreezeAsset(contract);
    }

}
