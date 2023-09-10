package com.daniel.organisation.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.daniel.organisation.database.NotaManager;
import com.daniel.organisation.database.TareaManager;

/**
 * Con esta clase voy a manejar las tareas
 * de manera local
 */
public class TareaLocalStore {

    public static final String SP_NAME = "tareaDetails";
    SharedPreferences tareaLocalDatabase; //SharedPreferences
    TareaManager tareaManager;

    /**
     * Constructor para el localStore
     * @param context de la aplicación
     */
    public TareaLocalStore(Context context){
        //Inicio el sharedPreferences
        tareaLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
        //Inicio el tareaManager para manejar las consultas de las tareas
        tareaManager = new TareaManager(context);
    }

    /**
     * Guarda la última tarea seleccionada para
     * trabajar con ella.
     * @param tarea
     */
    public void storeUltimaTarea(Tarea tarea){
        SharedPreferences.Editor spEditor = tareaLocalDatabase.edit();
        spEditor.putInt("id", tarea.getId());
        spEditor.commit();
    }

    /**
     * Getter de la última tarea que se seleccionó
     * @return la última tarea
     */
    public Tarea getUltimaTarea() {
        Tarea tarea = tareaManager.getTareaById(tareaLocalDatabase.getInt("id",0));
        return tarea;
    }

    /**
     * Limpia los datos de las tareas
     */
    public void clearTareaData(){
        SharedPreferences.Editor spEditor = tareaLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }



}
