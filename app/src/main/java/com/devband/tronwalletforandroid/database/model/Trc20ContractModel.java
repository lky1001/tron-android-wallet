package com.devband.tronwalletforandroid.database.model;

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
@Entity(tableName = "trc_20_contract")
public class Trc20ContractModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String address;

    @NonNull
    private int decimal;

    @NonNull
    private boolean isFavorite;
}
