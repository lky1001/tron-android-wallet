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
@Entity(tableName = "transfer_history")
public class TransferHistoryModel {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(name = "account_id")
    private long accountId;

    @NonNull
    @ColumnInfo(name = "to_address")
    private String toAddress;

    @Nullable
    private String memo;
}
