package com.daniel.organisation.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

import com.daniel.organisation.database.UserManager;
import com.daniel.organisation.model.Nota;
import com.daniel.organisation.model.User;

/**
 * Con esta clase voy a manejar los usuarios
 * de manera local
 */
public class UserLocalStore {
    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;
    UserManager userManager;

    /**
     * Constructor para el localStore de usuario
     * @param context
     */
    public UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
        userManager = new UserManager(context);
    }

    /**
     * Guarda los datos del usuario para
     * trabajar con ellos.
     * @param user
     */
    public void storeUserData(User user){
            System.out.println("HASTA AQUI LLEGA");
        try{
            SharedPreferences.Editor spEditor = userLocalDatabase.edit();
            spEditor.putString("uid", String.valueOf(user.getUid()));
            spEditor.putString("name", user.getName());
            spEditor.putString("email", user.getEmail());
            spEditor.putString("username", user.getUsername());
            spEditor.putString("password", user.getPassword());
            spEditor.commit();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Sirve para obtener el usuario de la sesión
     * @return
     */
    public User getLoggedInUser(){

        String username = userLocalDatabase.getString("username", "");

        User user = userManager.getUserByUsername(username);

        return user;
    }

    public boolean getUserLoggedIn(){
        return userLocalDatabase.getBoolean("loggedIn", false);
    }

    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }

    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }


}
