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

    @NonNull
    @ColumnInfo(name = "token_id")
    private String tokenId;

    @NonNull
    @ColumnInfo(name = "token_name")
    private String name;

    @ColumnInfo(name = "detail_json")
    @Nullable
    private String detailJson;
}
