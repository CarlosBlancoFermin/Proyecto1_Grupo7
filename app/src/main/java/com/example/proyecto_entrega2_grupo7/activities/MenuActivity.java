package com.example.proyecto_entrega2_grupo7.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.proyecto_entrega2_grupo7.R;

public class MenuActivity extends SuperLoggedActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        String titulo = "";
        if(userLogged != null)
            titulo = new StringBuilder().append("¡Hola, ")
                            .append(userLogged.getNombre())
                                    .append("!").toString();
        else
            titulo = getResources().getString(R.string.tag_menu);
        super.crearHomebar(titulo);

    }

    @Override
    protected void pulsarVolver() {
        cerrarAlVolver();
    }
    @Override
    public void onBackPressed() {
        cerrarAlVolver();
    }

    /**
     * Sobreescribe la función al volver a la actividad anterior,
     * haciendo un 'Cierre de sesión'
     * previa confirmación del usuario
     * @return
     */
    private AlertDialog cerrarAlVolver(){
        return new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.menu_cerrarSesion))
                .setMessage("¿Deseas cerrar sesión?")
                .setPositiveButton("Sí", (dialog, which) -> pulsarCerrarSesion())
                .setNegativeButton("No", (dialog, which) -> {})
                .show();
    }

    /**
     * Lanza una nueva actividad segun el boton pulsado
     * @param view boton pulsado
     */
    public void pulsarBotonMenu(View view){
        int idButton = view.getId();
        Class nuevaActividad = null;
        if(idButton == R.id.btMenuCrearUsuario)
            nuevaActividad = EmpleadoInfoActivity.class;
        else if(idButton == R.id.btMenuListaUsuarios)
            nuevaActividad = ListarActivity.class;
        else if(idButton == R.id.btMenuCreditos)
            nuevaActividad = CreditosActivity.class;

        //Crear el intent de la nueva actividad
        Intent intent = new Intent(this, nuevaActividad);
        if(idButton == R.id.btMenuCrearUsuario)
            intent.putExtra(ACTION_TYPE,MODO_CREAR);

        //Lanzar la nueva actividad
        startActivity(intent);
    }
}
