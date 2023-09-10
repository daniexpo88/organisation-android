package com.daniel.organisation.database;

import android.content.Context;

import androidx.room.Room;

import com.daniel.organisation.model.User;
import com.daniel.organisation.dao.UserDao;

import java.util.List;

/**
 * Maneja las consultas a la BD de los users
 */
public class UserManager {
    private final UserDao userDao;

    public UserManager(Context context){
        Context appContext = context.getApplicationContext();
        AppDatabase db = Room.databaseBuilder(appContext, AppDatabase.class, "user")
                .allowMainThreadQueries()
                .build();
        userDao = db.getUserDao();
    }
    public List<String> getEmails() {return userDao.getEmails();}
    public List<String> getUsernames(){
        return userDao.getUsernames();
    }
    public List<User> getUsers(){
        return userDao.getAll();
    }
    public User getUserById(int id){
        return userDao.findById(id);
    }
    public User getUserByUsername(String username){
        return userDao.findByUsername(username);
    }
    public void addUser(User user){
        userDao.insert(user);
    }
    public void updateUser(User user){
        userDao.update(user);
    }
    public void deleteUser(User user){
        userDao.delete(user);
    }

}
