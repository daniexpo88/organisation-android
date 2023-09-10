package com.daniel.organisation.database;
import android.content.Context;

import androidx.room.Room;

import com.daniel.organisation.dao.NotaDao;
import com.daniel.organisation.model.Nota;

import java.util.ArrayList;
import java.util.List;

/**
 * Maneja las consultas a la BD de las notas
 */
public class NotaManager {
    private final NotaDao notaDao;

    public NotaManager(Context context){
        Context appContext = context.getApplicationContext();
        AppDatabase db = Room.databaseBuilder(appContext, AppDatabase.class, "user")
                .allowMainThreadQueries().build();

        notaDao = db.notaDao();
    }
    public List<Nota> getNotasByUserId(int id){
        List<Nota> notas = new ArrayList<>();
        try{
            notas = notaDao.getNotasByUserId(id);
        }catch(Exception e){
            System.err.println(e.getLocalizedMessage());
        }
        return notas;
    }

    public Nota getNotaById(int id){
        return notaDao.getNotaById(id);
    }

    public void updateNota(Nota nota){
        notaDao.update(nota);
    }
    public void deleteNota(Nota nota){
        notaDao.delete(nota);
    }
    public void addNota(Nota nota){
        notaDao.insert(nota);
    }
}
