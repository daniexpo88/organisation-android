package com.daniel.organisation.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * @Author daniexpo
 * POJO Tarea
 */
@Entity(foreignKeys = {@ForeignKey(entity = User.class,
        parentColumns = "uid",
        childColumns = "userId",
        onDelete = ForeignKey.CASCADE)
}, tableName="tarea")
public class Tarea {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(index = true)
    public int userId;
    @ColumnInfo(name="titulo")
    private String titulo;
    @ColumnInfo(name="contenido")
    private String contenido;
    @ColumnInfo(name="fechaLimite")
    private Date fechaLimite;

    public Tarea(){}

    @Ignore
    public Tarea(String titulo, String contenido, Date fechaLimite){
        this.titulo = titulo;
        this.contenido = contenido;
        this.fechaLimite = fechaLimite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }


    public Date getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(Date fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
