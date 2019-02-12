package com.devband.tronwalletforandroid.tron.grpc;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.devband.tronwalletforandroid.common.Constants;
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
import org.tron.protos.Protocol;
import org.tron.protos.Protocol.Account;
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

    public GrpcAPI.TransactionExtention createTransaction(Contract.TransferContract contract) {
        return blockingStubFullNode.createTransaction2(contract);
    }

    public GrpcAPI.TransactionExtention createTransferAssetTransaction(Contract.TransferAssetContract contract) {
        return blockingStubFullNode.transferAsset2(contract);
    }

    public GrpcAPI.TransactionExtention createParticipateAssetIssueTransaction(Contract.ParticipateAssetIssueContract contract) {
        return blockingStubFullNode.participateAssetIssue2(contract);
    }

    public GrpcAPI.TransactionExtention createAssetIssue(Contract.AssetIssueContract contract) {
        return blockingStubFullNode.createAssetIssue2(contract);
    }

    public GrpcAPI.TransactionExtention voteWitnessAccount(Contract.VoteWitnessContract contract) {
        return blockingStubFullNode.voteWitnessAccount2(contract);
    }

    public GrpcAPI.TransactionExtention createWitness(Contract.WitnessCreateContract contract) {
        return blockingStubFullNode.createWitness2(contract);
    }

    public GrpcAPI.TransactionExtention createFreezeBalance(Contract.FreezeBalanceContract contract) {
        return blockingStubFullNode.freezeBalance2(contract);
    }

    public GrpcAPI.TransactionExtention createWithdrawBalance(Contract.WithdrawBalanceContract contract) {
        return blockingStubFullNode.withdrawBalance2(contract);
    }

    public GrpcAPI.TransactionExtention createUnfreezeBalance(Contract.UnfreezeBalanceContract contract) {
        return blockingStubFullNode.unfreezeBalance2(contract);
    }

    public GrpcAPI.TransactionExtention createUnfreezeAsset(Contract.UnfreezeAssetContract contract) {
        return blockingStubFullNode.unfreezeAsset2(contract);
    }

    public boolean broadcastTransaction(Transaction signaturedTransaction) {
        return blockingStubFullNode.broadcastTransaction(signaturedTransaction)
                .getResult();
    }

    public GrpcAPI.BlockExtention getBlock(long blockNum) {
        if (blockNum < 0) {
            return blockingStubFullNode
                    .withDeadlineAfter(Constants.GRPC_TIME_OUT_IN_MS, TimeUnit.MILLISECONDS)
                    .getNowBlock2(EmptyMessage.newBuilder().build());
        }
        NumberMessage.Builder builder = NumberMessage.newBuilder();
        builder.setNum(blockNum);
        return blockingStubFullNode.getBlockByNum2(builder.build());
    }

    @Nullable
    public WitnessList listWitnesses() {
        return blockingStubFullNode.listWitnesses(EmptyMessage.newBuilder().build());
    }

    @Nullable
    public AssetIssueList getAssetIssueList() {
        return blockingStubFullNode.getAssetIssueList(EmptyMessage.newBuilder().build());
    }

    @Nullable
    public NodeList listNodes() {
        return blockingStubFullNode.listNodes(EmptyMessage.newBuilder().build());
    }

    @Nullable
    public AssetIssueList getAssetIssueByAccount(byte[] address) {
        ByteString addressBS = ByteString.copyFrom(address);
        Account request = Account.newBuilder().setAddress(addressBS).build();
        return blockingStubFullNode.getAssetIssueByAccount(request);
    }

    public AssetIssueContract getAssetIssueByName(String assetName) {
        ByteString assetNameBs = ByteString.copyFrom(assetName.getBytes());
        BytesMessage request = BytesMessage.newBuilder().setValue(assetNameBs).build();
        return blockingStubFullNode.getAssetIssueByName(request);
    }

    public NumberMessage getTotalTransaction() {
        return blockingStubFullNode.totalTransaction(EmptyMessage.newBuilder().build());
    }

    public GrpcAPI.ExchangeList getExchangeList() {
        return blockingStubFullNode.listExchanges(EmptyMessage.newBuilder().build());
    }

    public GrpcAPI.ExchangeList getPaginatedExchangeList(GrpcAPI.PaginatedMessage paginatedMessage) {
        return blockingStubFullNode.getPaginatedExchangeList(paginatedMessage);
    }

    public Protocol.Exchange getExchangeById(byte[] id) {
        ByteString idBS = ByteString.copyFrom(id);
        return blockingStubFullNode.getExchangeById(BytesMessage.newBuilder().setValue(idBS).build());
    }

    public GrpcAPI.TransactionExtention createExchangeCreateContract(Contract.ExchangeCreateContract contract) {
        return blockingStubFullNode.exchangeCreate(contract);
    }

    public GrpcAPI.TransactionExtention createExchangeInjectContract(Contract.ExchangeInjectContract contract) {
        return blockingStubFullNode.exchangeInject(contract);
    }

    public GrpcAPI.TransactionExtention createExchangeWithdrawContract(Contract.ExchangeWithdrawContract contract) {
        return blockingStubFullNode.exchangeWithdraw(contract);
    }

    public GrpcAPI.TransactionExtention createExchangeTransactionContract(Contract.ExchangeTransactionContract contract) {
        return blockingStubFullNode.exchangeTransaction(contract);
    }

    public Protocol.SmartContract getSmartContract(byte[] address) {
        ByteString byteString = ByteString.copyFrom(address);
        BytesMessage bytesMessage = BytesMessage.newBuilder().setValue(byteString).build();
        return blockingStubFullNode.getContract(bytesMessage);
    }

    public GrpcAPI.TransactionExtention triggerContract(Contract.TriggerSmartContract triggerContract) {
        return blockingStubFullNode.triggerContract(triggerContract);
    }

    public AssetIssueContract getAssetIssueById(String id) {
        ByteString assetIdBs = ByteString.copyFrom(id.getBytes());
        BytesMessage request = BytesMessage.newBuilder().setValue(assetIdBs).build();
        if (blockingStubSolidityNode != null) {
            return blockingStubSolidityNode.getAssetIssueById(request);
        } else {
            return blockingStubFullNode.getAssetIssueById(request);
        }
    }
}
