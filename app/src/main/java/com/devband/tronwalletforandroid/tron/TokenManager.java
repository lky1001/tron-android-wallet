package com.devband.tronwalletforandroid.tron;

import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.tronscan.TokenInfo;
import com.devband.tronlib.tronscan.TokenInfos;
import com.devband.tronwalletforandroid.database.dao.TokenIdNameDao;
import com.devband.tronwalletforandroid.database.model.TokenIdNameModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;

import io.reactivex.Maybe;
import io.reactivex.Single;

public class TokenManager {

    private TronNetwork mTronNetwork;
    private TokenIdNameDao mTokenIdNameDao;

    public TokenManager(TronNetwork tronNetwork, TokenIdNameDao tokenIdNameDao) {
        this.mTronNetwork = tronNetwork;
        this.mTokenIdNameDao = tokenIdNameDao;
    }

    public Single<String> getTokenName(String id) {
        return Maybe.fromCallable(() -> mTokenIdNameDao.findByTokenId(id))
                .switchIfEmpty(Maybe.just(TokenIdNameModel.builder().tokenId("").name("").build()))
                .toSingle()
                .map(tokenIdNameModel -> {
                    if (tokenIdNameModel.getId() > 0) {
                        return tokenIdNameModel.getName();
                    } else {
                        return "";
                    }
                })
                .flatMap(name -> {
                    if ("".equalsIgnoreCase(name)) {
                        return mTronNetwork.getTokenInfo(id);
                    } else {
                        return Single.just(TokenInfos.builder()
                                .total(0)
                                .data(Collections.singletonList(TokenInfo.builder().name(name).build()))
                                .build());
                    }
                })
                .map(tokenInfo -> {
                    String name = id;

                    for (TokenInfo data : tokenInfo.getData()) {
                        name = data.getName();
                    }

                    if (tokenInfo.getTotal() > 0) {
                        TokenIdNameModel tokenIdNameModel = TokenIdNameModel.builder()
                                .tokenId(id)
                                .name(name)
                                .detailJson(new ObjectMapper().writeValueAsString(tokenInfo))
                                .build();

                        mTokenIdNameDao.insert(tokenIdNameModel);
                    }

                    return name;
                });
    }
}
