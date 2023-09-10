package com.daniel.organisation.database;

import android.content.Context;

import androidx.room.Room;

import com.daniel.organisation.dao.TareaDao;
import com.daniel.organisation.model.Tarea;

import java.util.ArrayList;
import java.util.List;

/**
 * Maneja las consultas a la BD de las tareas
 */
public class TareaManager {
    private final TareaDao tareaDao;

    public TareaManager(Context context){
        Context appContext = context.getApplicationContext();
        AppDatabase db = Room.databaseBuilder(appContext, AppDatabase.class, "user")
                .allowMainThreadQueries().build();

        tareaDao = db.tareaDao();
    }
    public List<Tarea> getTareasByUserId(int id){
        List<Tarea> tareas = new ArrayList<>();
        try{
            tareas = tareaDao.getTareasByUserId(id);
        }catch(Exception e){
            System.err.println(e.getLocalizedMessage());
        }
        return tareas;
    }

    public Tarea getTareaById(int id){
        return tareaDao.getTareaById(id);
    }
    public void updateTarea(Tarea tarea){
        tareaDao.update(tarea);
    }
    public void deleteTarea(Tarea tarea){
        tareaDao.delete(tarea);
    }
    public void addTarea(Tarea tarea){
        tareaDao.insert(tarea);
    }
}
