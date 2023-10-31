package com.example.proyecto_entrega2_grupo7.database;

import com.example.proyecto_entrega2_grupo7.entities.Usuario;

public interface FirebaseCallback {
    void onCallback(Usuario usuario);
    void onFailedCallback();
}
