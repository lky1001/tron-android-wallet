package com.devband.tronwalletforandroid.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.devband.tronwalletforandroid.database.model.AccountModel;

@Dao
public interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AccountModel accountModel);

    @Update
    void update(AccountModel accountModel);

    @Delete
    void delete(AccountModel accountModel);

    @Query("SELECT COUNT(*) from account")
    int countAccounts();

    @Query("SELECT * from account LIMIT 1")
    AccountModel loadAccount();
}
