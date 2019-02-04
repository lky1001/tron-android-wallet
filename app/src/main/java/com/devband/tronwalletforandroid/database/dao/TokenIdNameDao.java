package com.devband.tronwalletforandroid.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.devband.tronwalletforandroid.database.model.TokenIdNameModel;

@Dao
public interface TokenIdNameDao {

    @Query("SELECT * FROM token_id_name_map WHERE token_id = :tokenId LIMIT 1")
    TokenIdNameModel findByTokenId(String tokenId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(TokenIdNameModel tokenIdNameModel);
}
