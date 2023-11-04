package com.example.proyecto_entrega2_grupo7.activities;

import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto_entrega2_grupo7.R;
import com.example.proyecto_entrega2_grupo7.entities.Usuario;

/** Clase abstracta de la que han de heredar
 * todas las Actividades posteriores a la pantalla de login
 * para tener la barra superior con el botón volver
 * y 3 puntos con el minimenu de usuario (ver perfil e iniciar sesion)
 */
public abstract class SuperLoggedActivity extends AppCompatActivity {

    private static Usuario userLogged = null;

    //CODIGOS DE RESULT ENTRE ACTIVIDADES
    public static final int ACTUALIZABLE = 1;

    //MODOS DE EMPLEADOINFO_ACTIVITY
    public static final int MODO_CREAR = 0;
    public static final int MODO_DETALLES = 1;
    public static final int MODO_MODIFICAR = 2;


    //Crear barra superior en la actividad
    protected void crearHomebar(String titulo){
//        Toolbar toolbar = findViewById(R.id.tbBarraSuperior);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(titulo);
    }

    //Inflar layout en el menu de la barra superior
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homebar_menu, menu);
        return true;
    }

    //Configurar flecha de retroceso de la barra superior
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Usuario getUserLogged() {
        return userLogged;
    }

    public static void setUserLogged(Usuario userLogged) {
        SuperLoggedActivity.userLogged = userLogged;
    }
}
