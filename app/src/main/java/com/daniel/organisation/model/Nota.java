package com.daniel.organisation.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.daniel.organisation.model.User;

import java.util.Date;

/**
 * @Author daniexpo
 * POJO Nota
 */
@Entity(foreignKeys = {@ForeignKey(entity = User.class,
        parentColumns = "uid",
        childColumns = "userId",
        onDelete = ForeignKey.CASCADE)
        })
public class Nota {
    @PrimaryKey(autoGenerate = true)
    public int nid;
    @ColumnInfo(index = true)
    public int userId;
    @ColumnInfo(name="titulo")
    public String titulo;
    @ColumnInfo(name="descripcion")
    public String descripcion;
    @ColumnInfo(name="tipo")
    public String tipo;


    public Nota(){}
    @Ignore
    public Nota(String titulo, String descripcion){
        this.titulo=titulo;
        this.descripcion = descripcion;
    }

    //GETTERS Y SETTERS
    public int getNid() {
        return nid;
    }
    public void setNid(int nid) {
        this.nid = nid;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getTipo() {
        return tipo;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
