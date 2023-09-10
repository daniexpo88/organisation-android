package com.daniel.organisation.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.daniel.organisation.R;
import com.daniel.organisation.model.UserLocalStore;
import com.daniel.organisation.adapter.NotaListAdapter;
import com.daniel.organisation.database.NotaManager;
import com.daniel.organisation.model.Nota;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity con recyclerView que muestra las notas guardadas por el usuario.
 */
public class MisNotasActivity extends AppCompatActivity {
    UserLocalStore userLocalStore; //Para trabajar con la sesión del usuario
    RecyclerView recyclerView; //RecyclerView con la lista de las notas
    NotaListAdapter adapter; //Adapter de notas para el recyclerview
    NotaManager notaManager; //Manager de la BD de notas
    //Componentes
    SearchView buscarMisNotas;
    boolean isFABOpen = false;
    FloatingActionButton fabN, fabN1, fabN2;
    //Componentes en caso de que no haya notas
    Button buttonEmptyCrearNuevaMisNotas; //Boton para crear nueva nota si no hay notas
    ImageView ivImagenEmptyMisNotas; //Imagen por si no hay notas
    TextView tvEmptyMisNotas; //TextView por si no hay notas

    /**
     * onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Inicio el userLocalStore lo primero
        userLocalStore = new UserLocalStore(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_notas);

        iniciarFAB();

        iniciaBotonNotasEmpty();

        iniciaBusquedaNotas();

    }
    @Override
    protected void onStart() {
        super.onStart();
        iniciarRecyclerView();
    }

    /**
     * Inicia los FloatingActionButtons y maneja
     * su despliegue y sus correspondientes métodos
     * onClick
     */
    private void iniciarFAB(){
        fabN = findViewById(R.id.fabN);
        fabN1 = findViewById(R.id.fabN1);
        fabN2 = findViewById(R.id.fabN2);

        fabN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });

        fabN1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddNotaActivity.class));
            }
        });
        fabN2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddTareaActivity.class));
            }
        });
    }

    /**
     * Inicia el SearchView y maneja el OnQueryText
     * en este caso utilizo el OnQueryTextChange, para
     * que cada vez que el texto cambie, haga una consulta de si
     * contiene ese texto
     */
    private void iniciaBusquedaNotas(){
        buscarMisNotas = findViewById(R.id.buscarMisNotas);
        buscarMisNotas.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.filtrado(s);
                return true;
            }
        });
    }

    /**
     * Abre los botones FAB
     */
    private void showFABMenu(){
        isFABOpen=true;
        fabN1.animate().translationY(-getResources().getDimension(R.dimen.standard_65));
        fabN2.animate().translationY(-getResources().getDimension(R.dimen.standard_130));
    }

    /**
     * Cierra los botones FAB
     */
    private void closeFABMenu(){
        isFABOpen=false;
        fabN1.animate().translationY(0);
        fabN2.animate().translationY(0);
    }

    /**
     * Inicia el recycler view de notas
     */
    private void iniciarRecyclerView(){
        try{
            recyclerView = findViewById(R.id.recyclerViewMisNotas);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            List<Nota> listaNotas = loadNotasList();
            adapter = new NotaListAdapter(listaNotas);
            recyclerView.setAdapter(adapter);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Inicia los objetos y maneja el onClick del botón crear nueva nota
     */
    private void iniciaBotonNotasEmpty(){
        buttonEmptyCrearNuevaMisNotas = findViewById(R.id.buttonEmptyCrearNuevaMisNotas);
        ivImagenEmptyMisNotas = findViewById(R.id.ivImagenEmptyMisNotas);
        tvEmptyMisNotas = findViewById(R.id.tvEmptyMisNotas);

        //OnClickListener del boton en caso de que el recyclerView esté empty
        buttonEmptyCrearNuevaMisNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddNotaActivity.class));
            }
        });
    }

    /**
     * Carga la lista de las notas y comprueba si está vacía
     * Si está vacía mostrará los elementos para ese caso.
     * @return la lista de las notas
     */
    private List<Nota> loadNotasList(){
        List<Nota> listaNotas = new ArrayList();
        try{
            notaManager = new NotaManager(getApplicationContext());
            listaNotas = notaManager.getNotasByUserId(userLocalStore.getLoggedInUser().getUid());
            //Si el tamaño de la lista es 0, muestra un mensaje,
            // un icono y un botón para crear una nueva nota
            if(listaNotas.size()==0){
                recyclerView.setVisibility(View.GONE);
                buttonEmptyCrearNuevaMisNotas.setVisibility(View.VISIBLE);
                tvEmptyMisNotas.setVisibility(View.VISIBLE);
                ivImagenEmptyMisNotas.setVisibility(View.VISIBLE);
            }else{
                recyclerView.setVisibility(View.VISIBLE);
                buttonEmptyCrearNuevaMisNotas.setVisibility(View.GONE);
                tvEmptyMisNotas.setVisibility(View.GONE);
                ivImagenEmptyMisNotas.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listaNotas;
    }
}