package com.devband.tronwalletforandroid.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.devband.tronwalletforandroid.database.dao.AddressDao;
import com.devband.tronwalletforandroid.database.model.AddressModel;

@Database(entities = {
        AddressModel.class
}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "tron_wallet_db";
    private static volatile AppDatabase mInstance;

    public abstract AddressDao addressDao();

    public static AppDatabase getDatabase(Context context) {
        if (mInstance == null) {
            synchronized (AppDatabase.class) {
                if (mInstance == null) {
                    mInstance = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }

        return mInstance;
    }

    public static void destroyInstance() {
        mInstance = null;
    }
}