package com.daniel.organisation.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.daniel.organisation.model.Nota;

import java.util.List;

@Dao
public interface NotaDao {
    @Query("SELECT * FROM nota WHERE userId = :id")
    List<Nota> getNotasByUserId(int id);

    @Query("SELECT * FROM nota WHERE nid = :id")
    Nota getNotaById(int id);

    @Update
    void update(Nota nota);

    @Delete
    void delete(Nota nota);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Nota nota);
}
