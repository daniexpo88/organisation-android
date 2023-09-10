package com.daniel.organisation.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.daniel.organisation.model.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT username FROM user")
    List<String> getUsernames();

    @Query("SELECT email FROM user")
    List<String> getEmails();

    @Query("SELECT * FROM user WHERE uid LIKE :id")
    User findById(int id);

    @Query("SELECT * FROM user WHERE username LIKE :uname")
    User findByUsername(String uname);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User... users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Update
    void update(User... users);

    @Delete
    void delete(User... users);

}
