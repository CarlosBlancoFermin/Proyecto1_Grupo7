package com.example.proyecto_entrega2_grupo7.database;

import com.example.proyecto_entrega2_grupo7.entities.Usuario;

import java.util.List;

public interface FirebaseListCallback {
    void onCallback(List<Usuario> list);
    }

