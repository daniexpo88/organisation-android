package com.daniel.organisation.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.daniel.organisation.database.NotaManager;
import com.daniel.organisation.database.UserManager;
import com.daniel.organisation.model.Nota;

/**
 * Con esta clase voy a manejar las notas
 * de manera local
 */
public class NotaLocalStore {
    public static final String SP_NAME = "notaDetails";
    SharedPreferences userLocalDatabase; //Aquí guardo las sharedPreferences
    NotaManager notaManager;

    /**
     * Constructor para el localStore
     * @param context de la aplicación
     */
    public NotaLocalStore(Context context){
        //Inicio el sharedPreferences
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
        //Inicio el notaManager
        notaManager = new NotaManager(context);
    }

    /**
     * Guarda la última nota seleccionada para
     * trabajar con ella.
     * @param nota
     */
    public void storeUltimaNota(Nota nota){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putInt("id", nota.getNid());
        spEditor.commit();
    }

    /**
     * Getter de la última nota que se seleccionó
     * @return la última nota
     */
    public Nota getUltimaNota() {
        Nota nota = notaManager.getNotaById(userLocalDatabase.getInt("id",0));
        return nota;
    }

    /**
     * Limpia los datos de las notas
     */
    public void clearNotaData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }


}
