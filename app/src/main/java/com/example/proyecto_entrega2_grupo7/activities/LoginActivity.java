package com.example.proyecto_entrega2_grupo7.activities;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.proyecto_entrega2_grupo7.R;
import com.example.proyecto_entrega2_grupo7.database.dao.UsuarioDAO;
import com.example.proyecto_entrega2_grupo7.database.utils.UtilsEncriptador;
import com.example.proyecto_entrega2_grupo7.database.utils.UtilsCheckNetwork;
import com.example.proyecto_entrega2_grupo7.entities.Usuario;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends SuperActivityBase {
    UsuarioDAO ges = new UsuarioDAO();
    Button btlogin;
    String email;
    String pass;
    EditText etEmail;
    EditText etPass;
    ImageButton btShowPass;
    UtilsCheckNetwork con = new UtilsCheckNetwork();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle(getResources().getString(R.string.tag_login));

        //Asignacion de variables
        dataBinding();
    }

    /**
     * Funcion para que no se cree el submenu en esta pantalla
     * @param menu
     * @return false
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void dataBinding(){
        etEmail = findViewById(R.id.etLoginEmail);
        etPass = findViewById(R.id.etLoginPass);
        btlogin = findViewById(R.id.btlogin);
        btShowPass = findViewById(R.id.btLoginShowPass);

        setBotonMostrarPass(btShowPass,etPass);//Funcion de la SuperActivityBase
    }

    /**
     * comprueba que el usuario y la contraseña no esten vacios.
     * comprueba que el usuario y la contraseña coincidan en la base de datos y llama a la pantalla del menu
     */
    public void pulsarAcceder(View view) {
        //Primero comprueba que no haya conexion
        if(!hayConexion())
            return;

        //Luego comprueba que ambos campos esten cubiertos
        email = etEmail.getText().toString();
        pass = etPass.getText().toString();
        if(email.isEmpty() || pass.isEmpty()){
            Toast.makeText(LoginActivity.this,
                    getResources().getString(R.string.msj_rellenarCampos),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        //Finalmente se hace el cruce con la BD
        ges.obtenerTodos(list -> {
            String encriptedpass = UtilsEncriptador.passEncriptada(pass);

            // Convertir la lista genérica en lista de usuarios
            List<Usuario> listUsers = new ArrayList<Usuario>(list);
            boolean loginExitoso = false;

            for (Usuario user : listUsers) {
                String uemail = user.getCorreo();
                String upass = user.getPass();

                if (uemail.equals(email) && upass.equals(encriptedpass)) {
                    Toast.makeText(LoginActivity.this,
                            getResources().getString(R.string.msj_loginOK),
                            Toast.LENGTH_SHORT).show();
                    SuperActivityBase.setUserLogged(user);
                    Intent intent = new Intent(this, MenuActivity.class);
                    startActivity(intent);
                    loginExitoso = true;
                    break; // Sal del bucle después del inicio de sesión exitoso
                }
            }

            if (!loginExitoso) {
                Toast.makeText(LoginActivity.this,
                        getResources().getString(R.string.msj_loginKO),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void pulsarCrearEmpleado(View view){
        if(!hayConexion())
            return;

        startActivity(new Intent(this, InfoActivity.class));
    }


}
