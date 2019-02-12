package com.devband.tronwalletforandroid.database.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

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
@Entity(tableName = "trc_10_asset")
public class Trc10AssetModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "token_id")
    private String tokenId;

    @NonNull
    @ColumnInfo(name = "token_name")
    private String name;

    @NonNull
    @ColumnInfo(name = "owner_address")
    private String ownerAddress;

    @NonNull
    @ColumnInfo(name = "total_supply")
    private long totalSupply;

    private int precision;
}
