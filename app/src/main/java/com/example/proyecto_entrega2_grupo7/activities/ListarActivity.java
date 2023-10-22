package com.example.proyecto_entrega2_grupo7.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.proyecto_entrega2_grupo7.R;
import com.example.proyecto_entrega2_grupo7.database.FirebaseListCallback;
import com.example.proyecto_entrega2_grupo7.database.dao.UsuarioDAO;
import com.example.proyecto_entrega2_grupo7.entities.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ListarActivity extends AppCompatActivity {

    UsuarioDAO userService = new UsuarioDAO();
    List<Usuario> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        userService.obtenerusuarios(new FirebaseListCallback() {
            @Override
            public void onCallback(List<Usuario> list) {
                users = list;
            }
        });

        Log.d("lista",users + "");
    }
}