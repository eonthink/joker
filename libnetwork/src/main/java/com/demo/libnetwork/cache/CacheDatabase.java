package com.demo.libnetwork.cache;

import com.demo.libcommon.utils.AppGlobals;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Cache.class}, version = 1)
//数据读取、存储时数据转换器,比如将写入时将Date转换成Long存储，读取时把Long转换Date返回
//@TypeConverters(DateConverter.class)
public abstract class CacheDatabase extends RoomDatabase {
    private static final CacheDatabase database;

    static {
        database = Room.databaseBuilder(AppGlobals.getApplication(), CacheDatabase.class, "joker_cache")
                .allowMainThreadQueries()
                .build();

    }

    public abstract CacheDao getCache();


    public static CacheDatabase get() {
        return database;
    }


}
