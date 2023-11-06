package com.example.proyecto_entrega2_grupo7.activities;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto_entrega2_grupo7.R;
import com.example.proyecto_entrega2_grupo7.database.FirebaseListCallback;
import com.example.proyecto_entrega2_grupo7.database.dao.UsuarioDAO;
import com.example.proyecto_entrega2_grupo7.database.utils.UtilsEncriptador;
import com.example.proyecto_entrega2_grupo7.database.utils.UtilsCheckNetwork;
import com.example.proyecto_entrega2_grupo7.entities.Usuario;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
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


        //llama a la funcion comprobarLogin cuando se le da al boton
//        btlogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(con.isOnline(LoginActivity.this)){
//                    comprobarLogin();
//                }
//                else {
//
//                }
//            }
//        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void dataBinding(){
        etEmail = findViewById(R.id.etLoginEmail);
        etPass = findViewById(R.id.etLoginPass);
        btlogin = findViewById(R.id.btlogin);
        btShowPass = findViewById(R.id.btShowPass);

        btShowPass.setOnTouchListener((v, event) -> {
            if(etPass.getText().toString().equals(""))
                return false;
            switch (event.getAction() ) {
                case MotionEvent.ACTION_DOWN:
                    etPass.setInputType(//Texto visible
                            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    break;
                case MotionEvent.ACTION_UP:
                    new Handler().postDelayed(() ->
                                    etPass.setInputType(
                                            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD),
                            400);
                    break;
            }
            return true;
        });
    }



    /**
     * comprueba que el usuario y la contraseña no esten vacios.
     * comprueba que el usuario y la contraseña coincidan en la base de datos y llama a la pantalla del menu
     */
    public void comprobarLogin(View view) {
        email = etEmail.getText().toString();
        pass = etPass.getText().toString();

        if(email.isEmpty() && pass.isEmpty()){
            Toast.makeText(LoginActivity.this,
                    "Rellene todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!con.isOnline(LoginActivity.this)){
            Toast.makeText(LoginActivity.this,
                    "No hay conexion a internet", Toast.LENGTH_SHORT).show();
            return;
        }

        ges.obtenerTodos(list -> {
            String encriptedpass = UtilsEncriptador.passEncriptada(pass);

            // Convertir la lista genérica en lista de usuarios
            List<Usuario> listUsers = new ArrayList<Usuario>(list);
            boolean loginExitoso = false;

            for (Usuario user : listUsers) {
                String uemail = user.getCorreo();
                String upass = user.getPass();

                if (uemail.equals(email) && upass.equals(encriptedpass)) {
                    Toast.makeText(LoginActivity.this, "Login correcto", Toast.LENGTH_SHORT).show();
                    SuperLoggedActivity.setUserLogged(user);
                    Intent intent = new Intent(this, MenuActivity.class);
                    startActivity(intent);
                    loginExitoso = true;
                    break; // Sal del bucle después del inicio de sesión exitoso
                }
            }

            if (!loginExitoso) {
                Toast.makeText(LoginActivity.this, "Email o contraseña incorrecto", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
