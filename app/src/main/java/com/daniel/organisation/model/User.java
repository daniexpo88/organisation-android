package com.daniel.organisation.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

/**
 * @Author daniexpo
 * POJO User
 */
@Entity(tableName = "user")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name="name")
    private String name;

    @ColumnInfo(name="email")
    private String email;

    @ColumnInfo(name="username", index = true)
    private String username;

    @ColumnInfo(name="password")
    private String password;

    public User(){
    }

    @Ignore
    public User(String name, String email, String username, String password){
        this.name=name;
        this.email=email;
        this.username=username;
        this.password=password;
    }
    @Ignore
    public User(String username, String password) {
        this.username=username;
        this.password=password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
