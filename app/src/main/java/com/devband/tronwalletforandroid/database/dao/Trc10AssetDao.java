package com.devband.tronwalletforandroid.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.devband.tronwalletforandroid.database.model.Trc10AssetModel;

@Dao
public interface Trc10AssetDao {

    @Query("SELECT * FROM trc_10_asset WHERE token_id = :tokenId LIMIT 1")
    Trc10AssetModel findByTokenId(String tokenId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Trc10AssetModel trc10AssetModel);
}
