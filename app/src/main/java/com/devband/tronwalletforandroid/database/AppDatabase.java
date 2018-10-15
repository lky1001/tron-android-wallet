package com.devband.tronwalletforandroid.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;

import com.devband.tronwalletforandroid.database.dao.AccountDao;
import com.devband.tronwalletforandroid.database.dao.FavoriteTokenDao;
import com.devband.tronwalletforandroid.database.dao.WalletDao;
import com.devband.tronwalletforandroid.database.model.AccountModel;
import com.devband.tronwalletforandroid.database.model.FavoriteTokenModel;
import com.devband.tronwalletforandroid.database.model.WalletModel;

@Database(entities = {
        WalletModel.class,
        AccountModel.class,
        FavoriteTokenModel.class
}, version = AppDatabase.VERSION, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    static final int VERSION = 2;

    public abstract AccountDao accountDao();
    public abstract WalletDao walletDao();
    public abstract FavoriteTokenDao favoriteTokenDao();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE account ADD COLUMN address TEXT");

            try {
                database.execSQL("CREATE TABLE IF NOT EXISTS favorite_token "
                        + "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, account_id INTEGER NOT NULL, token_name TEXT)");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}