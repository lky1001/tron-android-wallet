package com.devband.tronwalletforandroid.database.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

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
@Entity(tableName = "account")
public class AccountModel {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;

    private String address;

    private String account;

    private boolean imported;

    private Date created;

    private Date updated;

    @NonNull
    @Override
    public String toString() {
        if (imported) {
            return "[I] " + name;
        }

        return name;
    }
}
