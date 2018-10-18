package com.devband.tronwalletforandroid.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.devband.tronwalletforandroid.database.model.FavoriteTokenModel;

import java.util.List;

@Dao
public interface FavoriteTokenDao {

    @Query("SELECT * FROM favorite_token WHERE id = :id LIMIT 1")
    FavoriteTokenModel findById(int id);

    @Query("SELECT * FROM favorite_token WHERE account_id = :accountId AND token_name = :name LIMIT 1")
    FavoriteTokenModel findByAccountIdAndTokenName(long accountId, String name);

    @Query("SELECT * FROM favorite_token")
    List<FavoriteTokenModel> findAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavoriteTokenModel favoriteTokenModel);

    @Delete
    void delete(FavoriteTokenModel favoriteTokenModel);
}
