package com.example.proyecto_entrega2_grupo7.activities;

import android.view.View;

import com.example.proyecto_entrega2_grupo7.entities.Usuario;

/**
 * Interfaz a modo de callback
 * que permite gestionar los botones pulsados
 * en un item del RecyclerView
 */
public interface OnClickCallback {
    void onUserClick(Usuario usuario, int id);
}
