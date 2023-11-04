package com.example.proyecto_entrega2_grupo7.activities;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    EditText editTextEmail;
    EditText editTextPass;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //guardamos el contenido del editText en una variable
        editTextEmail = findViewById(R.id.textEmail);
        editTextPass = findViewById(R.id.textPass);
        btlogin = findViewById(R.id.btlogin);
        UtilsCheckNetwork con = new UtilsCheckNetwork();
        //llama a la funcion comprobarLogin cuando se le da al boton
        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(con.isOnline(LoginActivity.this)){
                    comprobarLogin();
                }
                else {
                    Toast.makeText(LoginActivity.this, "No hay conexion a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * comprueba que el usuario y la contraseña no esten vacios.
     * comprueba que el usuario y la contraseña coincidan en la base de datos y llama a la pantalla del menu
     */
    public void comprobarLogin() {
        email = editTextEmail.getText().toString();
        pass = editTextPass.getText().toString();
        final Context context = this;
        ges.obtenerTodos(new FirebaseListCallback() {
            @Override
            public void onCallback(List list) {
                if (!email.isEmpty() && !pass.isEmpty()) {
                    String encriptedpass = UtilsEncriptador.passEncriptada(pass);

                    // Convertir la lista genérica en lista de usuarios
                    List<Usuario> listUsers = new ArrayList<Usuario>(list);
                    boolean loginExitoso = false;

                    for (Usuario user : listUsers) {
                        String uemail = user.getCorreo();
                        String upass = user.getPass();

                        if (uemail.equals(email) && upass.equals(encriptedpass)) {
                            Toast.makeText(LoginActivity.this, "Login correcto", Toast.LENGTH_SHORT).show();
                            intent = new Intent(context, MenuActivity.class);
                            startActivity(intent);
                            loginExitoso = true;
                            break; // Sal del bucle después del inicio de sesión exitoso
                        }
                    }

                    if (!loginExitoso) {
                        Toast.makeText(LoginActivity.this, "Email o contraseña incorrecto", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Email o contraseña incorrecto", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
