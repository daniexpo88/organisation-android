package com.daniel.organisation.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.daniel.organisation.R;
import com.daniel.organisation.adapter.TareaListAdapter;
import com.daniel.organisation.database.TareaManager;
import com.daniel.organisation.model.Tarea;
import com.daniel.organisation.model.TareaLocalStore;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class ModificarTareaActivity extends AppCompatActivity {

    Button botonEliminarTarea;
    Button botonGuardarTareaModificar; //Botón de la actividad
    EditText etContenidoTareaModificar; //EditText del contenido de la Tarea
    EditText etTituloTareaModificar; //EditText del título de la Tarea
    EditText etFechaTareaModificar;
    TareaLocalStore tareaLocalStore; //LocalStore para guardar la última Tarea seleccionada por el user
    TareaManager tareaManager; //Para hacer consultas a la base de datos.
    TareaListAdapter tareaListAdapter;
    Tarea tarea; //Tarea con la que vamos a trabajar y el usuario quiere modificar
    Date fechaLimite;
    boolean eliminada = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Inicializo los objetos para el manejo de Tareas.
        inicioElementosTareas();
        //Establezco la view activity_modificar_Tarea
        setContentView(R.layout.activity_modificar_tarea);
        //Inicializo los elementos de la vista y establezco los textos
        iniciarElementosActivity();
        //Inicializo los botones para guardar y eliminar la tarea
        iniciarBotones();
    }

    /**
     * Maneja los onClick de los botones
     * para guardar la modificación de una tarea
     * y la eliminación de la misma
     */
    private void iniciarBotones() {
        botonGuardarTareaModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Guardo el titulo y el contenido cuando pulse el botón de guardar
                String titulo = etTituloTareaModificar.getText().toString();
                String contenido = etContenidoTareaModificar.getText().toString();
                String fecha = etFechaTareaModificar.getText().toString();
                //Actualizamos la Tarea y le decimos al manager que la actualice en la bd.
                tarea.setTitulo(titulo);
                tarea.setContenido(contenido);
                //La fecha limite la establece en el show date picker
                //Hacemos update
                tareaManager.updateTarea(tarea);
                //Limpiamos el localstore, para que deje de tener guardada esta Tarea.
                tareaLocalStore.clearTareaData();
                //Finalizo la actividad
                finish();
            }
        });

        botonEliminarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(view.getContext());
                dialogo1.setTitle("Importante");
                dialogo1.setMessage("¿Estás seguro de eliminar la Tarea "+
                        etTituloTareaModificar.getText().toString()+"?");
                //dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        aceptar();
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                dialogo1.show();
            }
        });
    }

    /**
     * Inicia los elementos del activity y establece los titulos
     */
    private void iniciarElementosActivity() {
        botonEliminarTarea = findViewById(R.id.botonEliminarTareaModificar);
        botonGuardarTareaModificar = findViewById(R.id.botonGuardarTareaModificar);
        etContenidoTareaModificar = findViewById(R.id.contenidoTareaModificar);
        etFechaTareaModificar = findViewById(R.id.fechaLimiteModificar);
        etTituloTareaModificar = findViewById(R.id.tituloTareaModificar);
        //Establezco los textos de la Tarea a modificar y el onClickListener
        etTituloTareaModificar.setText(tarea.getTitulo());
        etContenidoTareaModificar.setText(tarea.getContenido());
        //Establece la fecha de la tarea y el onClickListener del DatePicker
        estableceFecha();
    }

    /**
     * Establece la fecha de la tarea y
     * maneja el onClickListener del DatePicker en
     * caso de que la quiera modificar
     */
    private void estableceFecha(){
        LocalDate ld = tarea.getFechaLimite().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int day = ld.getDayOfMonth();
        int month = ld.getMonthValue();
        int year = ld.getYear();
        System.out.println(year);
        String selectedDate = day + " / " + (month+1) + " / " + year;
        etFechaTareaModificar.setText(selectedDate);
        //OnClick del botón de guardar

        etFechaTareaModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
    }

    /**
     * Inicia los elementos relacionados con el
     * manejo de tareas
     */
    private void inicioElementosTareas() {
        tareaLocalStore = new TareaLocalStore(this);
        tarea = tareaLocalStore.getUltimaTarea();
        tareaManager = new TareaManager(this);
        tareaListAdapter = new TareaListAdapter(tareaManager.
                getTareasByUserId(tarea.userId), this);
    }

    /**
     * Si el usuario pulsa aceptar en el AlertDialog a la hora de eliminar
     */
    private void aceptar(){
        eliminada = true;
        tareaManager.deleteTarea(tarea);
        tareaListAdapter.notifyItemRemoved(getPosicionTareaParaNotificar());
        Toast t=Toast.makeText(this,"Tarea eliminada.", Toast.LENGTH_SHORT);
        t.show();
        finish();
    }

    /**
     * Devuelve la posicion de la tarea para notificar que ha
     * cambiado en la BD
     * @return la posicion de esa tarea en la listas
     */
    private int getPosicionTareaParaNotificar(){
        List<Tarea> tareaList = tareaManager.getTareasByUserId(tarea.getUserId());
        int i = 0;
        for(Tarea n : tareaList){
            if(n.getId() == tarea.getId()){
                return i;
            }
            i++;
        }
        return i;
    }

    /**
     * OnStop
     */
    @Override
    protected void onStop() {
        super.onStop();
        //Por si para la aplicación y se sale hago lo mismo que en el onClick
        if(!eliminada){
            String titulo = etTituloTareaModificar.getText().toString();
            String contenido = etContenidoTareaModificar.getText().toString();
            tarea.setTitulo(titulo);
            tarea.setContenido(contenido);
            tareaManager.updateTarea(tarea);
            //Esta vez no limpio el TareaLocalStore por si el usuario vuelve a la aplicación sin cerrarla
        }
    }

    /**
     * Este método va a mostrar al usuario un DatePicker.
     * Una vez el usuario seleccione una fecha, el editText de la fecha
     * se actualizará y se guardará en la tarea esa fecha.
     */
    private void showDatePickerDialog(){
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + " / " + (month+1) + " / " + year;
                fechaLimite = Date.from(LocalDate.of(year, month, day).
                        atStartOfDay(ZoneId.systemDefault()).
                        toInstant());

                tarea.setFechaLimite(fechaLimite);
                etFechaTareaModificar.setText(selectedDate);
                etFechaTareaModificar.setHint(selectedDate);

            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}