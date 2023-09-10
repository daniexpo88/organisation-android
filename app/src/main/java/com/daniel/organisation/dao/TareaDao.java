package com.daniel.organisation.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.daniel.organisation.model.Tarea;

import java.util.List;

@Dao
public interface TareaDao {
    @Query("SELECT * FROM tarea WHERE userId = :id")
    List<Tarea> getTareasByUserId(int id);

    @Query("SELECT * FROM tarea WHERE id = :id")
    Tarea getTareaById(int id);

    @Update
    void update(Tarea tarea);

    @Delete
    void delete(Tarea tarea);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Tarea tarea);
}
