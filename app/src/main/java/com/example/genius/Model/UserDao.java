package com.example.genius.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User... users);

    @Query("select * from User")
    List<User> getAllUsers();

    @Query("SELECT * FROM User WHERE email=:email")
    LiveData<List<User>> getUserByEmail(String email);
}
