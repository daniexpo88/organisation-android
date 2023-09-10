package com.daniel.organisation.database;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Clase para convertir las fechas a la hora
 * de insertar y seleccionar de la BD
 */
public class DateConverter {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
