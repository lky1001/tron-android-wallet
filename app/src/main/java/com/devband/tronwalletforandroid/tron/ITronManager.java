package com.devband.tronwalletforandroid.tron;

import org.tron.api.GrpcAPI.AccountList;
import org.tron.api.GrpcAPI.AssetIssueList;
import org.tron.api.GrpcAPI.NodeList;
import org.tron.api.GrpcAPI.WitnessList;
import org.tron.protos.Protocol.Account;

import io.reactivex.Single;

public interface ITronManager {

    void shutdown() throws InterruptedException;

    Single<Account> queryAccount(byte[] address);

    Single<AccountList> listAccounts();

    Single<WitnessList> listWitnesses();

    Single<AssetIssueList> getAssetIssueList();

    Single<NodeList> listNodes();

    Single<AssetIssueList> getAssetIssueByAccount(byte[] address);
}
