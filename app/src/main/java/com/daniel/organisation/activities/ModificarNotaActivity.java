package com.daniel.organisation.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daniel.organisation.model.NotaLocalStore;
import com.daniel.organisation.R;
import com.daniel.organisation.adapter.NotaListAdapter;
import com.daniel.organisation.database.NotaManager;
import com.daniel.organisation.model.Nota;

import java.util.List;

/**
 * Este activity, va a manejar las notas que ya estén creadas para modificarlas
 */
public class ModificarNotaActivity extends AppCompatActivity {
    //Manejo de notas
    NotaLocalStore notaLocalStore; //LocalStore para guardar la última nota seleccionada por el user
    NotaManager notaManager; //Para hacer consultas a la base de datos.
    NotaListAdapter notaListAdapter;
    Nota nota; //Nota con la que vamos a trabajar y el usuario quiere modificar
    boolean eliminada = false; //Si la nota está eliminada o no
    //Componentes
    Button botonEliminarNota;
    Button botonGuardarNotaModificar; //Botón de la actividad
    EditText etContenidoNotaModificar; //EditText del contenido de la nota
    EditText etTituloNotaModificar; //EditText del título de la nota

    /**
     * onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Establezco la view activity_modificar_nota
        setContentView(R.layout.activity_modificar_nota);
        //Inicializo los objetos para el manejo de notas.
        iniciarManejoNotas();
        //Inicio los elementos de la activity
        iniciarElementosActivity();
        //El onClick de los botones guardar y eliminar
        onClickBotones();
    }

    /**
     * Manejo el onClick de los dos botones
     * GUARDAR y ELIMINAR
     */
    private void onClickBotones() {
        //OnClick del botón de guardar
        botonGuardarNotaModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Guardo el titulo y el contenido cuando pulse el botón de guardar
                String titulo = etTituloNotaModificar.getText().toString();
                String contenido = etContenidoNotaModificar.getText().toString();
                //Actualizamos la nota y le decimos al manager que la actualice en la bd.
                nota.setTitulo(titulo);
                nota.setDescripcion(contenido);
                notaManager.updateNota(nota);
                //Limpiamos el localstore, para que deje de tener guardada esta nota.
                notaLocalStore.clearNotaData();
                //Finalizo la actividad
                finish();
            }
        });

        botonEliminarNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(view.getContext());
                dialogo1.setTitle("Importante");
                dialogo1.setMessage("¿Estás seguro de eliminar la nota "+
                        etTituloNotaModificar.getText().toString()+"?");
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
     * Inicia los objetos para el manejo de notas
     */
    private void iniciarManejoNotas() {
        notaLocalStore = new NotaLocalStore(this);
        nota = notaLocalStore.getUltimaNota();
        notaManager = new NotaManager(this);
        notaListAdapter = new NotaListAdapter(notaManager.getNotasByUserId(nota.userId));
    }

    /**
     * Inicia los elementos de la activity
     */
    private void iniciarElementosActivity() {
        //Inicializo los elementos de la vista
        botonEliminarNota = findViewById(R.id.botonEliminarNota);
        botonGuardarNotaModificar = findViewById(R.id.botonGuardarNotaModificar);
        etContenidoNotaModificar = findViewById(R.id.etContenidoNotaModificar);
        etTituloNotaModificar = findViewById(R.id.etTituloNotaModificar);
        //Establezco los textos de la nota a modificar y el onClickListener
        etTituloNotaModificar.setText(nota.getTitulo());
        etContenidoNotaModificar.setText(nota.getDescripcion());
    }

    /**
     * Si el usuario pulsa aceptar en el AlertDialog a la hora de eliminar
     */
    private void aceptar(){
        eliminada = true;
        notaManager.deleteNota(nota);
        notaListAdapter.notifyItemRemoved(getPosicionNotaParaNotificar());
        Toast t=Toast.makeText(this,"Nota eliminada.", Toast.LENGTH_SHORT);
        t.show();
        finish();
    }

    /**
     * Devuelve la posicion de la nota para notificar que se ha eliminado
     * @return la posicion de la nota
     */
    private int getPosicionNotaParaNotificar(){
        List<Nota> notaList = notaManager.getNotasByUserId(nota.getUserId());
        int i = 0;
        for(Nota n : notaList){
            if(n.getNid() == nota.getNid()){
                return i;
            }
            i++;
        }
        return i;
    }

    /**
     * onStop
     */
    @Override
    protected void onStop() {
        super.onStop();
        //Por si para la aplicación y se sale hago lo mismo que en el onClick
        if(!eliminada){
            String titulo = etTituloNotaModificar.getText().toString();
            String contenido = etContenidoNotaModificar.getText().toString();
            nota.setTitulo(titulo);
            nota.setDescripcion(contenido);
            notaManager.updateNota(nota);
            //Esta vez no limpio el notaLocalStore por si el usuario vuelve a la aplicación sin cerrarla
        }
    }
}