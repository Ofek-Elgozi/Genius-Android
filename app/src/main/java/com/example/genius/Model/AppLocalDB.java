package com.example.genius.Model;

import com.example.genius.MyApplication;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = User.class, version =15)
abstract class AppLocalDbRepository extends RoomDatabase
{
    public abstract UserDao userDao();
}

public class AppLocalDB
{
    static public final AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.getContext(),
                            AppLocalDbRepository.class,
                            "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
    private AppLocalDB(){}
}
