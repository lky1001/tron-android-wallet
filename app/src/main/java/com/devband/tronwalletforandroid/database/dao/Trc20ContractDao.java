package com.devband.tronwalletforandroid.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.devband.tronwalletforandroid.database.model.Trc20ContractModel;

import java.util.List;

@Dao
public interface Trc20ContractDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Trc20ContractModel trc20ContractModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Trc20ContractModel> trc20ContractModels);

    @Query("SELECT * FROM trc_20_contract WHERE address = :contractAddress LIMIT 1")
    Trc20ContractModel findByContractAddress(String contractAddress);

    @Delete
    void delete(Trc20ContractModel trc20ContractModel);

    @Query("SELECT * FROM trc_20_contract")
    List<Trc20ContractModel> getAll();
}
