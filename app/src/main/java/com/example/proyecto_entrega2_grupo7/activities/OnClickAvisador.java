package com.example.proyecto_entrega2_grupo7.activities;

import com.example.proyecto_entrega2_grupo7.entities.Usuario;

/**
 * Interfaz a modo de callback
 * que permite gestionar los botones pulsados
 * en un item del RecyclerView
 */
public interface OnClickAvisador {
    void onUserClick(Usuario usuario, int id);
}
