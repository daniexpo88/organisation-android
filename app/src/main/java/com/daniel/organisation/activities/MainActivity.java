package com.daniel.organisation.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.daniel.organisation.R;
import com.daniel.organisation.adapter.TareaListAdapter;
import com.daniel.organisation.database.TareaManager;
import com.daniel.organisation.model.Tarea;
import com.daniel.organisation.model.UserLocalStore;
import com.daniel.organisation.adapter.NotaListAdapter;
import com.daniel.organisation.database.NotaManager;
import com.daniel.organisation.model.Nota;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Actividad principal
 */
public class MainActivity extends AppCompatActivity {
    UserLocalStore userLocalStore; //Información del usuario registrado
    //De los recycler views
    RecyclerView recyclerViewNotas, recyclerViewTareas; //RecyclerViews
    NotaListAdapter adapter; //Adaptador de recyclerview notas
    TareaListAdapter tareaAdapter; //Adaptador de recyclerview tareas
    //De la bd
    NotaManager notaManager; //Maneja las consultas sobre notas
    TareaManager tareaManager;//Maneja las consultas sobre tareas
    //Componentes visuales
    TextView tituloNotas, tituloTareas, fechaDeHoy, vacioDelTodo; //TextViews
    FloatingActionButton fab, fab1, fab2; //Floating Action Buttons
    boolean isFABOpen = false; //Para saber si los FAB están desplegados o no
    ConstraintLayout mainActivityLayout; //ConstraintLayout del main
    Button buttonMisNotas, buttonMisTareas; //Botones para ir a diferentes activities
    LinearLayoutManager llm; //LinearLayoutManager
    Switch switch2; //Switch para el modo oscuro

    /**
     * Metodo onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Lo primero que hago es iniciar el userLocalStore para comprobar
        //si el usuario está autenticado
        userLocalStore = new UserLocalStore(this);
        //Comprueba si está autenticado
        if (autenticado()) {
            //Guardo el llm de la aplicación para usarlo posteriormente
            llm = new LinearLayoutManager(getApplicationContext());
            setContentView(R.layout.activity_main);
            //Establezco la fecha de hoy
            estableceFechaDeHoy();
            //Inicio los botones de mis tareas y mis notas y manejo el onClick
            iniciarBotones();
            //Este método inicia los FAB y maneja el despliegue y el onClick de cada uno
            inciarFAB();
            //Esté método inicia y maneja el switch de modoOscuro
            iniciaSwitchModoOscuro();

        } else {//Si no está autenticado lo manda al Login
            userLocalStore.setUserLoggedIn(false);
            startActivity(new Intent(this, Login.class));
        }
    }

    /**
     *
     */
    @Override
    protected void onStart() {
        super.onStart();
        //Iniciamos el text view y establezco la fecha de hoy
        // por si el usuario deja la aplicación en segundo plano
        // y cuando entra, es otro día
        if(autenticado()){
            estableceFechaDeHoy();
            //ACTUALIZAMOS EL RECYCLER VIEW EN EL ONSTART POR SI VUELVE DE ELIMINAR UNA NOTA
            iniciarRecyclerView();
            //Comprobamos que esté el modo noche o no activo
            compruebaModoNoche();
        }

    }

    /**
     * Autentica que el usuario esté loggedIn
     * @return si está autenticado o no
     */
    private boolean autenticado(){
        return userLocalStore.getUserLoggedIn();
    }

    /**
     * Establece la fecha de hoy
     */
    private void estableceFechaDeHoy() {
        fechaDeHoy = findViewById(R.id.fechaDeHoy);
        fechaDeHoy.setText(LocalDate.now().
                format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                .toString());
    }

    /**
     * Inicio los botones de mis notas y mis tareas y manejo el onClick
     * para que inicie la actividad correspondiente
     */
    private void iniciarBotones() {
        buttonMisNotas = findViewById(R.id.buttonMisNotas);
        buttonMisNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MisNotasActivity.class));
            }
        });
        //Inicio el botón que el usuario utiliza para ver todas sus notas
        buttonMisTareas = findViewById(R.id.buttonMisTareas);
        buttonMisTareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MisTareasActivity.class));
            }
        });
    }

    /**
     * Va a iniciar los FAB y va a manejar su despliegue
     * y diferentes métodos on Click que nos llevarán
     * a crear nuevas tareas o notas
     */
    private void inciarFAB() {
        /*
            Estos son los FloatingActionButtons que al pulsar
            el primero el usuario, se desplegarán los otros dos
             */
        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Si los FAB no están desplegados
                if(!isFABOpen){
                    //Muestra el resto de FAB
                    showFABMenu();
                }else{
                    //Cierra los FAB
                    closeFABMenu();
                }
            }
        });
            /*
            Va a mandar al usuario si hace click a
            la actividad AddNota.
             */
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddNotaActivity.class));
            }
        });
            /*
            Va a mandar al usuario si hace click a
            la actividad AddNota.
             */
        fab2.setOnClickListener(new View.OnClickListener() {
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
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_65));
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_130));
    }

    /**
     * Cierra los botones FAB
     */
    private void closeFABMenu(){
        isFABOpen=false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
    }

    /**
     * Dependiendo si esta activo o no hará switch entre
     * modo noche y modo normal
     */
    private void iniciaSwitchModoOscuro() {
        //Esto es un switch que cambia entre modo oscuro y modo normal
        switch2 = findViewById(R.id.switch2);
        compruebaModoNoche();
        /*Función lambda que va a comprobar si está selected y en cada caso
          establecerá el modo noche o modo dia
        */
        try{
            switch2.setOnCheckedChangeListener((compoundButton, b) -> {
                if(b){
                    //Si se activa el switch, MODO NOCHE
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else{
                    //Si se desactiva, MODO NORMAL
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            });
        }catch(Exception e){
            System.err.println("ERR EN SWITCH");
            e.printStackTrace();
        }

    }

    /**
     * Comprueba si está activo el modo noche y chequea el switch
     * en función a eso
     */
    private void compruebaModoNoche(){
        //Devuelve un int que dice si el modo noche está activo o no
        int currentNightMode = Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                //Si no está activo, pongo el switch desactivado
                switch2.setChecked(false);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                //Si está activo, pongo el switch activo
                switch2.setChecked(true);
                break;
        }
    }

    /**
     * Inicia el recyclerView correspondiente.
     * Comprueba si hay tareas para el día de hoy y si hay notas.
     * Hace visibles los elementos dependiendo de si hay tareas o no.
     */
    private void iniciarRecyclerView(){
        //Inicia todos los componentes que tienen que ver con los recyclerview
        mainActivityLayout = findViewById(R.id.mainActivityLayout);
        recyclerViewNotas = findViewById(R.id.recyclerViewNotas);
        recyclerViewTareas = findViewById(R.id.recyclerViewTareas);
        tituloNotas = findViewById((R.id.tituloMainNotas));
        tituloTareas = findViewById(R.id.tituloMainTareas);
        vacioDelTodo = findViewById(R.id.vacioDelTodo);
        try{
            /*
            Si hay tareas en el día de hoy, el tema cambiará a azul,
            que es el color de las tareas y mostrará el recyclerview de las tareas
            y ocultará el de las notas
             */
        if(hoyHayTareas()){
            setTheme(R.style.Theme_OrganisationBlue);
            tituloTareas.setVisibility(View.VISIBLE);
            recyclerViewTareas.setVisibility(View.VISIBLE);
            tituloNotas.setVisibility(View.GONE);
            recyclerViewNotas.setVisibility(View.GONE);
            vacioDelTodo.setVisibility(View.GONE);

            recyclerViewTareas.setLayoutManager(llm);
            //Cargamos las tareas de hoy
            List<Tarea> listaTareas = loadTareasDeHoyList();
            //Le pasamos al adapter la lista
            tareaAdapter = new TareaListAdapter(listaTareas, this);
            //Le pasamos al recyclerView el adapter
            recyclerViewTareas.setAdapter(tareaAdapter);
        }else{
            /*
            En el caso de que no haya tareas en el dia de hoy,
            el tema será el normal, que es el de notas y mostrará
            el recyclerView de notas si hay notas, pero sino, mostrará
            un mensaje que informará de que no hay nada.
             */
            setTheme(R.style.Theme_Organisation);
            //Si el tamaño de la lista de notas es 0 o nulo, mostrará el mensaje informando
                if(loadNotasList().size()==0 || loadNotasList()==null){
                    tituloTareas.setVisibility(View.GONE);
                    recyclerViewTareas.setVisibility(View.GONE);
                    tituloNotas.setVisibility(View.GONE);
                    recyclerViewNotas.setVisibility(View.GONE);
                    vacioDelTodo.setVisibility(View.VISIBLE);
                }else{//Sino, mostrará los elementos relacionados
                    // con las notas y ocultará el resto
                    tituloTareas.setVisibility(View.GONE);
                    recyclerViewTareas.setVisibility(View.GONE);
                    tituloNotas.setVisibility(View.VISIBLE);
                    recyclerViewNotas.setVisibility(View.VISIBLE);
                    vacioDelTodo.setVisibility(View.GONE);

                    recyclerViewNotas.setLayoutManager(llm);
                    //Carga las notas
                    List<Nota> listaNotas = loadNotasList();
                    //Le pasa al adapter la lista de notas
                    adapter = new NotaListAdapter(listaNotas);
                    //Le paso al recyclerView el adaptador
                    recyclerViewNotas.setAdapter(adapter);
                }
             }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Comprueba si el día de hoy el usuario tiene tareas por hacer
     * @return
     */
    private boolean hoyHayTareas(){
        if(loadTareasDeHoyList() == null || loadTareasDeHoyList().size()==0) {
            return false;
        }else{
            return true;
        }
    }

    /**
     * Devuelve la lista de las tareas de hoy
     * @return la lista de tareas con fecha a día de hoy
     */
    private List<Tarea> loadTareasDeHoyList(){
        try{
            List<Tarea> tareaList = new ArrayList<>();
            tareaManager = new TareaManager(getApplicationContext());
            tareaList = tareaManager.getTareasByUserId(userLocalStore.getLoggedInUser().getUid());
            //Compruebo que la lista no esté vacía y si lo está devuelvo null
            if(tareaList.size()==0){
                return null;
            }
            //Comprueba que la fecha sea igual a la de hoy
            // y añade la tarea al arrayList listaFechaHoy si coinciden
            List<Tarea> listaFechaHoy = new ArrayList<>();
            String hoy = fechaDeHoy.getText().toString();
            for(Tarea t : tareaList){
                if(fechaDevuelta(t.getFechaLimite()).equalsIgnoreCase(fechaDeHoy.getText().toString())){
                    listaFechaHoy.add(t);
                }
            }
            return listaFechaHoy;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Devuelve la fecha formateada que se le pase por parámetros
     * @param fecha
     * @return la fecha en formato texto dd/MM/yyyy
     */
    private String fechaDevuelta(java.util.Date fecha){
        fecha.setMonth(fecha.getMonth()+1);
        LocalDate ld = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return ld.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString();
    }

    /**
     * Carga la lista de notas
     * @return la lista con las notas
     */
    private List<Nota> loadNotasList(){
        List<Nota> listaNotas = null;
        try{
            notaManager = new NotaManager(getApplicationContext());
            listaNotas = notaManager.getNotasByUserId(userLocalStore.getLoggedInUser().getUid());
        }catch (Exception e){
            e.printStackTrace();
        }
        return listaNotas;
    }



    /**
     * Si pulsan el botón de Logout, limpiará la información del usuario loggedIn,
     * finalizará el MainActivity
     * y empezará la actividad del login.
     * @param view
     */
    public void logout(View view) {
        userLocalStore.clearUserData();
        userLocalStore.setUserLoggedIn(false);

        finish();
        startActivity(new Intent(this, Login.class));
    }




}