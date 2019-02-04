package com.devband.tronwalletforandroid.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.devband.tronwalletforandroid.database.model.AccountModel;

import java.util.List;

@Dao
public interface AccountDao {

    @Query("SELECT * FROM account WHERE id = :id LIMIT 1")
    AccountModel loadAccountById(long id);

    @Query("SELECT * FROM account")
    List<AccountModel> loadAllAccounts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(AccountModel accountModel);

    @Update
    void update(AccountModel accountModel);

    @Delete
    void delete(AccountModel accountModel);

    @Query("SELECT COUNT(*) FROM account")
    int countAccounts();

    @Query("SELECT * FROM account WHERE account = :accountKey LIMIT 1")
    AccountModel loadByAccountKey(String accountKey);

    @Query("SELECT * FROM account LIMIT 1")
    AccountModel loadAccount();
}
