package com.example.proyecto_entrega2_grupo7.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.proyecto_entrega2_grupo7.database.FirebaseListCallback;
import com.example.proyecto_entrega2_grupo7.R;
import com.example.proyecto_entrega2_grupo7.database.dao.UsuarioDAO;
import com.example.proyecto_entrega2_grupo7.entities.Usuario;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * En la actividad se llama al Servicio (UsuarioDAO), y este pilla la conexión
     * No es necesario hacer un "getInstance()" aqui
     */
    UsuarioDAO ges = new UsuarioDAO();
    List<Usuario> users = new ArrayList<>();
    List<Usuario> users2 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //ges.registrarusuario("carlos", "blanco", "cobrador de frak","123456","test@test.test");
        ges.obtenerUsuarios(new FirebaseListCallback() {
           @Override
           public void onCallback(List<Usuario> list) {
               users = list;
           }
        });
    }

    public void btListarEmpleados(View view){
        Intent intent = new Intent(this, ListarActivity.class);
        startActivity(intent);
    }


}