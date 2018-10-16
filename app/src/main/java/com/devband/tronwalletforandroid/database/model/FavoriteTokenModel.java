package com.devband.tronwalletforandroid.database.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(tableName = "favorite_token")
public class FavoriteTokenModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "account_id")
    private long accountId;

    @ColumnInfo(name = "token_name")
    private String tokenName;
}
