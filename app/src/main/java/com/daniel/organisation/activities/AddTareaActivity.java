package com.daniel.organisation.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.daniel.organisation.R;
import com.daniel.organisation.database.TareaManager;
import com.daniel.organisation.model.Tarea;
import com.daniel.organisation.model.TareaLocalStore;
import com.daniel.organisation.model.User;
import com.daniel.organisation.model.UserLocalStore;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalField;
import java.util.Date;

public class AddTareaActivity extends AppCompatActivity {
    TareaManager tareaManager; //Maneja el insertar la nueva tarea
    UserLocalStore userLocalStore; //Información del usuario de la sesión
    //Componentes de la vista
    EditText tituloTareaAdd, contenidoTareaAdd, fechaLimiteAdd;
    Button botonGuardarTareaAdd;
    Date fechaLimite;
    Tarea tarea; //La tarea que se va a guardar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tarea);
        tarea = new Tarea(); //Inicializo la tarea
        //Inicializo el resto de componentes y objetos.
        userLocalStore = new UserLocalStore(getApplicationContext());
        tareaManager = new TareaManager(getApplicationContext());
        tituloTareaAdd = findViewById(R.id.tituloTareaAdd);
        contenidoTareaAdd = findViewById(R.id.contenidoTareaAdd);
        fechaLimiteAdd = findViewById(R.id.fechaLimiteAdd);
        botonGuardarTareaAdd = findViewById(R.id.botonGuardarTareaAdd);

        /*
         * Este onClick le va a mostrar al usuario un DatePicker
         * para que seleccione la fecha que quiera
         */
        fechaLimiteAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        /*
         * Este onClick va a gestionar que la tarea se guarde correctamente
         * en la BD
         */
        botonGuardarTareaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tarea.setTitulo(tituloTareaAdd.getText().toString());
                tarea.setContenido(contenidoTareaAdd.getText().toString());
                tarea.setUserId(userLocalStore.getLoggedInUser().getUid());
                //Compruebo que la fecha no sea null
                if(tarea.getFechaLimite() == null){
                    /*Si es nulo le pongo de fecha límite hoy para que no de
                    * error cuando inserte
                    */
                    LocalDate ld = LocalDate.now();
                    //Resto un día porque sino no me lo guarda correctamente
                    ld = ld.minusMonths(1);
                    Date hoy = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    tarea.setFechaLimite(hoy);
                }
                try{
                    tareaManager.addTarea(tarea);
                    System.out.println("TAREA AÑADIDA CORRECTAMENTE");
                }catch (Exception e){
                    System.err.println("HA HABIDO UN PROBLEMA AL AÑADIR LA TAREA");
                }
                //Cierro el activity después de guardar la tarea
                finish();
            }
        });

    }

    /**
     * Este método va a mostrar al usuario un DatePicker.
     * Una vez el usuario seleccione una fecha, el editText de la fecha
     * se actualizará y se guardará en la tarea esa fecha.
     */
    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            /*
            Este onDateSet va a recibir la fecha cuando el usuario
             haya seleccionado una fecha y le haya dado a aceptar
             */
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + "/" + (month+1) + "/" + year;

                fechaLimite = Date.from(LocalDate.of(year, month, day).
                        atStartOfDay(ZoneId.systemDefault()).
                        toInstant());

                tarea.setFechaLimite(fechaLimite);
                fechaLimiteAdd.setText(selectedDate);
                fechaLimiteAdd.setHint(selectedDate);
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}