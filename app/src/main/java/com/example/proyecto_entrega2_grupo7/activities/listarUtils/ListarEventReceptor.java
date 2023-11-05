package com.example.proyecto_entrega2_grupo7.activities.listarUtils;

import android.widget.CheckBox;

import com.example.proyecto_entrega2_grupo7.entities.Filtros;
import com.example.proyecto_entrega2_grupo7.entities.Usuario;

/**
 * Interfaz a modo de callback implementado por ListarActivity
 * que permite gestionar los eventos de los componentes pulsados
 * en un item de otro layout dependiente
 * y enviarlos a la Activity de referencia.
 */
public interface ListarEventReceptor {
    void onButtonClick(Usuario usuario, int idButton);
    void onCheckboxClick (Filtros item, int position, CheckBox cb);
    void onDialogAccept();
}
