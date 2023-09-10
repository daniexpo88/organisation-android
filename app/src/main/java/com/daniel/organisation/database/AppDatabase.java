package com.daniel.organisation.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.daniel.organisation.dao.TareaDao;
import com.daniel.organisation.model.Nota;
import com.daniel.organisation.model.Tarea;
import com.daniel.organisation.model.User;
import com.daniel.organisation.dao.NotaDao;
import com.daniel.organisation.dao.UserDao;

@Database(entities = {User.class, Nota.class, Tarea.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao getUserDao();
    public abstract NotaDao notaDao();
    public abstract TareaDao tareaDao();
}
