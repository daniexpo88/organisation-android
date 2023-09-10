package com.daniel.organisation.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.daniel.organisation.R;
import com.daniel.organisation.model.UserLocalStore;
import com.daniel.organisation.database.UserManager;
import com.daniel.organisation.model.User;

import java.util.List;

/**
 * Este activity se encargará del Login
 */
public class Login extends AppCompatActivity{
    UserManager userManager; //Para las consultas de usuarios
    UserLocalStore userLocalStore; //Para guardar información de la sesión
    //Componentes
    EditText etPassword;
    EditText etUsername;

    /**
     * OnCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userManager = new UserManager(getApplicationContext());
        userLocalStore = new UserLocalStore(getApplicationContext());
        //Limpio el userLocalStore por si acaso el usuario ha llegado ahí de algún modo
        userLocalStore.clearUserData();
    }
    //Hago el onClick de esta manera para probar

    /**
     * Este es el onClick del botón de Register, que nos va a mandar a
     * esa actividad
     * @param view
     */
    public void deLoginARegister(View view){
        Intent register = new Intent(this,
                Register.class);
        startActivity(register);
    }

    /**
     * Va a manejar el intento de Login del Usuario
     * @param view
     */
    public void intentoLogin(View view) {

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        //Si el login es correcto...
        if(loginCorrecto(username, password)){
            //Si el usuario ni la contraseña están vacías...
            if(!username.isEmpty() && !password.isEmpty()){
                //Buscaremos el usuaroi en la base de datos y
                // guardaremos ese usuario en el UserLocalStore
                User user = userManager.getUserByUsername(username);
                userLocalStore.storeUserData(user);
                userLocalStore.setUserLoggedIn(true);
                System.out.println("USUARIO LOGGED IN");
                //Comienzo la actividad y finalizo el login
                // para que al volver atrás no le mande al login
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }else{
                //Muestro mensaje de Login Incorrecto
                Toast.makeText(getApplicationContext(), "Login incorrecto", Toast.LENGTH_SHORT).show();
                etUsername.setText("");
                etPassword.setText("");
            }
        }else{//Muestro mensaje de Login Incorrecto
            Toast.makeText(getApplicationContext(), "Login incorrecto", Toast.LENGTH_SHORT).show();
            etUsername.setText("");
            etPassword.setText("");
        }

    }

    /**
     * Comprueba que el usuario exista y las contraseñas coincidan
     * @param username
     * @param password
     * @return true si es correcto, false si es incorrecto
     */
    private boolean loginCorrecto(String username, String password){
        List<String> usernames = userManager.getUsernames();
        if(usernames.contains(username)){
            User usuarioComparado = userManager.getUserByUsername(username);
            if(usuarioComparado.getUsername().equals(username)
                    && usuarioComparado.getPassword().equals(password)){
                System.out.println("LOGIN CORRECTO");
                return true;
            }else{
                return false;
            }
        }
        return false;
    }
}