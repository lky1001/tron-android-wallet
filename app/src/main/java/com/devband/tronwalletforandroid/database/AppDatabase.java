package com.devband.tronwalletforandroid.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;

import com.devband.tronwalletforandroid.database.dao.AccountDao;
import com.devband.tronwalletforandroid.database.dao.FavoriteTokenDao;
import com.devband.tronwalletforandroid.database.dao.TokenIdNameDao;
import com.devband.tronwalletforandroid.database.dao.Trc20ContractDao;
import com.devband.tronwalletforandroid.database.dao.WalletDao;
import com.devband.tronwalletforandroid.database.model.AccountModel;
import com.devband.tronwalletforandroid.database.model.FavoriteTokenModel;
import com.devband.tronwalletforandroid.database.model.TokenIdNameModel;
import com.devband.tronwalletforandroid.database.model.TransferHistoryModel;
import com.devband.tronwalletforandroid.database.model.Trc20ContractModel;
import com.devband.tronwalletforandroid.database.model.WalletModel;

@Database(entities = {
        WalletModel.class,
        AccountModel.class,
        FavoriteTokenModel.class,
        TokenIdNameModel.class,
        TransferHistoryModel.class,
        Trc20ContractModel.class
}, version = AppDatabase.VERSION, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    static final int VERSION = 4;

    public abstract AccountDao accountDao();
    public abstract WalletDao walletDao();
    public abstract FavoriteTokenDao favoriteTokenDao();
    public abstract TokenIdNameDao tokenIdNameDao();
    public abstract Trc20ContractDao trc20ContractDao();

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

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            try {
                database.execSQL("CREATE TABLE IF NOT EXISTS token_id_name_map "
                        + "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, token_id TEXT NOT NULL, token_name TEXT NOT NULL, detail_json TEXT)");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                database.execSQL("CREATE TABLE IF NOT EXISTS transfer_history "
                        + "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, account_id INTEGER NOT NULL, to_address TEXT NOT NULL, memo TEXT)");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            try {
                database.execSQL("CREATE TABLE IF NOT EXISTS trc_20_contract "
                        + "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, address TEXT NOT NULL, decimal INTEGER NOT NULL, isFavorite INTEGER NOT NULL)");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}