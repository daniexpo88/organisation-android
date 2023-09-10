package com.daniel.organisation.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daniel.organisation.R;
import com.daniel.organisation.adapter.TareaListAdapter;
import com.daniel.organisation.database.TareaManager;
import com.daniel.organisation.model.Tarea;
import com.daniel.organisation.model.UserLocalStore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MisTareasActivity extends AppCompatActivity {
    UserLocalStore userLocalStore; //Maneja el usuario de la sesión
    RecyclerView recyclerView; //RecyclerView de las tareas
    TareaListAdapter adapter; //Adapter para el recyclerview
    TareaManager tareaManager; //Maneja la BD para las tareas
    //Componentes
    SearchView buscarMisTareas;
    boolean isFABOpen = false;
    FloatingActionButton fabT, fabT1, fabT2;
    //Componentes en caso de que no haya tareas
    Button buttonEmptyCrearNuevaMisTareas;
    ImageView ivImagenEmptyMisTareas;
    TextView tvEmptyMisTareas;

    /**
     * onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userLocalStore = new UserLocalStore(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_tareas);

        iniciarElementosTareasEmpty();

        inciarFAB();

        iniciaBusquedaTareas();

    }

    /**
     * onStart
     */
    @Override
    protected void onStart() {
        super.onStart();
        iniciarRecyclerView();
    }

    /**
     * Inicia los elementos por si no hay tareas
     * y maneja el onClick del botón por si quiere
     * crear una nueva
     */
    private void iniciarElementosTareasEmpty() {
        buttonEmptyCrearNuevaMisTareas = findViewById(R.id.buttonEmptyCrearNuevaMisTareas);
        ivImagenEmptyMisTareas = findViewById(R.id.ivImagenEmptyMisTareas);
        tvEmptyMisTareas = findViewById(R.id.tvEmptyMisTareas);

        buttonEmptyCrearNuevaMisTareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddTareaActivity.class));
            }
        });
    }

    /**
     * Inicia los FloatingActionButtons y maneja
     * su despliegue y sus correspondientes métodos
     * onClick
     */
    private void inciarFAB() {
        fabT = findViewById(R.id.fabT);
        fabT1 = findViewById(R.id.fabT1);
        fabT2 = findViewById(R.id.fabT2);

        fabT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });

        fabT1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddNotaActivity.class));
            }
        });
        fabT2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddTareaActivity.class));
            }
        });
    }

    /**
     * Muestra los botones FAB
     */
    private void showFABMenu(){
        isFABOpen=true;
        fabT1.animate().translationY(-getResources().getDimension(R.dimen.standard_65));
        fabT2.animate().translationY(-getResources().getDimension(R.dimen.standard_130));
    }

    /**
     * Cierra los botones FAB
     */
    private void closeFABMenu(){
        isFABOpen=false;
        fabT1.animate().translationY(0);
        fabT2.animate().translationY(0);
    }

    /**
     * Inicia el SearchView que va a buscar
     * con onQueryTextChange tareas con un título que contenga
     * la consulta introducida
     */
    private void iniciaBusquedaTareas() {
        buscarMisTareas = findViewById(R.id.buscarMisTareas);
        buscarMisTareas.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    private void iniciarRecyclerView(){
        try{
            recyclerView = findViewById(R.id.recyclerViewMisTareas);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            List<Tarea> listaTareas = loadTareasList();
            adapter = new TareaListAdapter(listaTareas, this);
            recyclerView.setAdapter(adapter);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private List<Tarea> loadTareasList(){
        List<Tarea> listaTareas = new ArrayList<>();
        try{
            tareaManager = new TareaManager(getApplicationContext());
            listaTareas = tareaManager.getTareasByUserId(userLocalStore.getLoggedInUser().getUid());
            if(listaTareas.size()==0){
                recyclerView.setVisibility(View.GONE);
                buttonEmptyCrearNuevaMisTareas.setVisibility(View.VISIBLE);
                tvEmptyMisTareas.setVisibility(View.VISIBLE);
                ivImagenEmptyMisTareas.setVisibility(View.VISIBLE);
            }else{
                recyclerView.setVisibility(View.VISIBLE);
                buttonEmptyCrearNuevaMisTareas.setVisibility(View.GONE);
                tvEmptyMisTareas.setVisibility(View.GONE);
                ivImagenEmptyMisTareas.setVisibility(View.GONE);
            }


        }catch(Exception e ){
            e.printStackTrace();
        }
        return listaTareas;
    }


}