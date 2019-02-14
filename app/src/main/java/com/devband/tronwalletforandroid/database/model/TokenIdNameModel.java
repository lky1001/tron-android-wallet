package com.devband.tronwalletforandroid.database.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
@Entity(tableName = "token_id_name_map")
public class TokenIdNameModel {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "token_id")
    @NonNull
    private String tokenId;

    @ColumnInfo(name = "token_name")
    @NonNull
    private String name;

    private int precision;

    @ColumnInfo(name = "detail_json")
    @Nullable
    private String detailJson;
}
