package com.daniel.organisation.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.daniel.organisation.R;
import com.daniel.organisation.model.UserLocalStore;
import com.daniel.organisation.database.NotaManager;
import com.daniel.organisation.model.Nota;

/**
 * @Author daniexpo
 * Activity que maneja cuando un usuario crea una nueva nota
 */
public class AddNotaActivity extends AppCompatActivity {
    NotaManager notaManager; //Maneja las consultas a la bd
    UserLocalStore userLocalStore; //Información del usuario de la sesión

    /**
     * Método onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nota);
        //Inicializo los objetos
        userLocalStore = new UserLocalStore(getApplicationContext());
        notaManager = new NotaManager(getApplicationContext());
        //Recojo los componentes de la actividad
        EditText tituloNota = findViewById(R.id.tituloNota);
        EditText contenidoNota = findViewById(R.id.contenidoNota);
        Button botonGuardarNota = findViewById(R.id.botonGuardarNota);
        //OnClickListener para guardar la nota
        botonGuardarNota.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                guardarNuevaNota(tituloNota.getText().toString(), contenidoNota.getText().toString());
            }
        });
    }

    /**
     * Este método guarda en la bd una nueva nota
     * @param titulo que haya introducido el usuario
     * @param contenido que haya introducido el usuario
     */

    private void guardarNuevaNota(String titulo, String contenido){
        Nota nota = new Nota(titulo, contenido);
        nota.setUserId(userLocalStore.getLoggedInUser().getUid());
        notaManager.addNota(nota);
        finish();
        startActivity(new Intent(this, MainActivity.class));

    }
}