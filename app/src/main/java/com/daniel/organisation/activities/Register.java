package com.daniel.organisation.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.daniel.organisation.R;
import com.daniel.organisation.database.UserManager;
import com.daniel.organisation.model.User;

import java.util.List;

/**
 * Actividad para el registro de los usuarios
 */
public class Register extends AppCompatActivity {
    EditText etNameRegister, etEmailRegister, etUsernameRegister, etPasswordRegister; //Componentes
    UserManager userManager; //Manager para la bd de usuarios

    /**
     * onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void deRegisterALogin(View view){
        userManager = new UserManager(this.getApplicationContext());
        if(registrarDatos()){
            Intent login = new Intent(this, Login.class);
            startActivity(login);
        }
    }

    /**
     * Esta clase realiza todo el proceso de registro de un usuario
     * @return true si todo ha ido bien, false si algo es incorrecto y notifica al usuario
     */
    private boolean registrarDatos(){
        try{
            //Guardo los edit text del register
            etUsernameRegister = findViewById(R.id.etUsernameRegister);
            etEmailRegister = findViewById(R.id.etEmailRegister);
            etNameRegister = findViewById(R.id.etNameRegister);
            etPasswordRegister = findViewById(R.id.etPasswordRegister);

            String username = etUsernameRegister.getText().toString();
            String email = etEmailRegister.getText().toString();
            String name = etNameRegister.getText().toString();
            String password = etPasswordRegister.getText().toString();

            System.out.println("--->"+username+"/"+name+"/"+password);
            //Valido el username
            if(!usernameValidation(username)){
                System.out.println("::::USUARIO INVALIDO");
                Toast.makeText(getApplicationContext(),
                        "No se ha registrado al usuario, nombre de usuario invalido",
                        Toast.LENGTH_SHORT).show();
                etNameRegister.setText("");
                etUsernameRegister.setText("");
                etPasswordRegister.setText("");
                etEmailRegister.setText("");
                return false;
            }else{
                System.out.println(":::::USUARIO VALIDO");
                //Si el username es válido, valido la contraseña
                if(emailValidation(email)){
                    System.out.println("::::EMAIL VALIDO");
                }else{//Si la contraseña es inválida notifica al usuario
                    Toast.makeText(getApplicationContext(),
                            "Email inválido",
                            Toast.LENGTH_SHORT).show();
                    etEmailRegister.setText("");
                    return false;
                }
                //Valido la contraseña y notifica si se ha registrado correctamente o no
                if(passwordValidation(password)){
                    System.out.println("::::::CONTRASEÑA VALIDA");
                    //Si el nombre está vacío, le pondremos de nombre el nombre de usuario
                    if(name.isEmpty()){
                        name = username;
                    }
                    User registeredUser = new User(name, email, username, password);
                    //Guardamos el usuario en la bd
                    userManager.addUser(registeredUser);
                    System.out.println("USUARIO AÑADIDO CORRECTAMENTE");
                    //Notifico al usuario de que ha sido registrado.
                    Toast.makeText(getApplicationContext(),
                            "Usuario "+registeredUser.getUsername()+" registrado correctamente",
                            Toast.LENGTH_SHORT).show();

                    return true;
                }else{//Si la contraseña es inválida notifica al usuario
                    Toast.makeText(getApplicationContext(),
                            "Contraseña inválida, debe tener más de 5 caracteres y sin espacios",
                            Toast.LENGTH_SHORT).show();
                    etPasswordRegister.setText("");
                    return false;
                }

            }
        }catch(Exception e){
            return false;
        }
    }

    /**
     * Valida que la contraseña tenga más de 5 caracteres y no tenga espacios
     * @param password la contraseña que ha introducido y que se va a comprobar
     * @return true si es válida false si no lo es
     */
    private boolean passwordValidation(String password) {
        if(password.contains(" ")){
            return false;
        }
        return password.length() >= 5;
    }
    /**
     * Validacion del usuername, para ver si existe y si es válido
     * @param username que se ha introducido y se va a validar
     * @return true si es valido, false si es inválido
     */
    private boolean usernameValidation(String username){
        //Quito los espacios del principio y del final
        username = username.trim();

        //Si el nombre de usuario contiene espacios devuelve false
        if(username.contains(" ")){
            System.err.println("::::EL USERNAME ES ERRÓNEO");
            return false;
        }
        //Comprobaciones
        if(!username.isEmpty()){
            List<String> lista;
            try{
                //Guarda todos los usernames en una lista para comprobar si ya existe.
                lista = userManager.getUsernames();
            }catch(Exception e){
                e.printStackTrace();
                lista = null;
            }
            if(lista.isEmpty()){
                return true;
            }
            return !lista.contains(username);
        }
        return false;
    }

    /**
     * Validacion del email para ver si existe y es valido
     * @param email
     * @return true si no está registrado y es válido
     */
    private boolean emailValidation(String email){
        List<String> lista;
        try{
            lista = userManager.getEmails();
        }catch(Exception e){
            lista = null;
            e.printStackTrace();
        }
        if(lista.isEmpty()){
            return true;
        }

        if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            if(!lista.contains(email))
            return true;
            else return false;
        }else{
            return false;
        }
    }
}