package com.devband.tronwalletforandroid.tron;

import com.devband.tronwalletforandroid.database.dao.Trc10AssetDao;
import com.devband.tronwalletforandroid.database.model.Trc10AssetModel;

import org.tron.protos.Contract;

import io.reactivex.Maybe;
import io.reactivex.Single;

public class TokenManager {

    private Tron mTron;
    private Trc10AssetDao mTrc10AssetDao;

    public TokenManager(Trc10AssetDao trc10AssetDao) {
        this.mTrc10AssetDao = trc10AssetDao;
    }

    public void setTron(Tron tron) {
        this.mTron = tron;
    }

    public Single<Trc10AssetModel> getTokenInfo(String id) {
        return Maybe.fromCallable(() -> mTrc10AssetDao.findByTokenId(id))
                .switchIfEmpty(Maybe.just(Trc10AssetModel.builder()
                        .tokenId("").ownerAddress("").name("").build()))
                .toSingle()
                .map(trc10AssetModel -> {
                    if (trc10AssetModel.getId() == 0) {
                        Contract.AssetIssueContract assetIssueContract = mTron.getAssetIssueById(id).blockingGet();

                        if (assetIssueContract != null) {
                            Trc10AssetModel trc10Asset = Trc10AssetModel.builder()
                                    .tokenId(assetIssueContract.getId())
                                    .name(assetIssueContract.getName().toStringUtf8())
                                    .ownerAddress(assetIssueContract.getOwnerAddress().toStringUtf8())
                                    .precision(assetIssueContract.getPrecision())
                                    .totalSupply(assetIssueContract.getTotalSupply())
                                    .build();

                            mTrc10AssetDao.insert(trc10Asset);

                            return trc10Asset;
                        }

                        return null;
                    }

                    return trc10AssetModel;
                });
    }
}
