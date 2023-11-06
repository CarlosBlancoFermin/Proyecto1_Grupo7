package com.example.proyecto_entrega2_grupo7.database;

import com.example.proyecto_entrega2_grupo7.entities.Usuario;

/**
 * Callback de firebase para obtener un objeto/entidad
 */
public interface FirebaseCallback {
    void onCallback(Object element);
}
