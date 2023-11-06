package com.example.proyecto_entrega2_grupo7.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto_entrega2_grupo7.R;
import com.example.proyecto_entrega2_grupo7.database.utils.UtilsCheckNetwork;
import com.example.proyecto_entrega2_grupo7.entities.Usuario;

/** Clase abstracta de la que han de heredar
 * todas las Actividades para gestionar la barra superior con el botón volver
 * y 3 puntos con el minimenu de usuario (ver perfil e iniciar sesion).
 * También se implementa una función para botón de ver contraseña (login e info/MODIFICAR)
 */
public abstract class SuperActivityBase extends AppCompatActivity {

    protected static Usuario userLogged = null;

    //CODIGOS DE RESULT ENTRE ACTIVIDADES
    public static final int ACTUALIZABLE = 1;

    //MODOS DE INFO_ACTIVITY
    public static final String ACTION_TYPE = "ACTION_TYPE";
    public static final int MODO_CREAR = 0;
    public static final int MODO_DETALLES = 1;
    public static final int MODO_MODIFICAR = 2;

    //OPCIONES DEL MENU DE BARRA SUPERIOR
    public static final int OP_VER_PERFIL = 1;
    public static final int OP_CERRAR_SESION = 2;

    /**
     * Crear barra superior en la actividad
     */
    protected void crearHomebar(String titulo) {
//        Toolbar toolbar = findViewById(R.id.tbBarraSuperior);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(titulo);
    }

    /**
     * Inflar layout en el submenu de la barra superior
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homebar_submenu, menu);
        return true;
    }

    /**
     * Configurar opciones del submenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //Por algún motivo, Android Studio no admite switch en este caso
        if (id == android.R.id.home) {
            pulsarVolver();
            return true;
        } else if (id == R.id.hbOpcion1) {
            pulsarVerPerfil();
            return true;
        } else if (id == R.id.hbOpcion2) {
            pulsarCerrarSesion();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    /**
     * Accion de la flecha del submenu:
     * volver a la pantalla anterior
     */
    protected void pulsarVolver() {
        onBackPressed();
        finish();
    }

    /**
     * Muestra detalles del usuario logeado
     */
    protected void pulsarVerPerfil() {
        if(!hayConexion())
            return;
        if (userLogged != null) {
            if (esMismoPerfil()) {
                Toast.makeText(this, "Ya estás en tu perfil", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, InfoActivity.class);
            intent.putExtra("usuario", userLogged);
            intent.putExtra(ACTION_TYPE, MODO_DETALLES);
            startActivityForResult(intent, ACTUALIZABLE);
        } else
            Toast.makeText(this, "No has iniciado sesión", Toast.LENGTH_SHORT).show();
    }

    /**
     * Si el mismo usuario está consultando su perfil
     * devuelve verdadero.
     * @return
     */
    private boolean esMismoPerfil() {
        Intent intent = getIntent();
        if (intent != null) {
            return intent.getIntExtra(ACTION_TYPE, 0) == MODO_DETALLES
                    && intent.getParcelableExtra("usuario").equals(userLogged);
        }
        return false;
    }

    /**
     * Devuelve a la pantalla de login
     * y pasa el usuario logeado a null
     */
    protected void pulsarCerrarSesion() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        userLogged = null;
        finish();
    }

    public static Usuario getUserLogged() {
        return userLogged;
    }

    public static void setUserLogged(Usuario userLogged) {
        SuperActivityBase.userLogged = userLogged;
    }

    /**
     * Metodo comun para comprobar si hay conexion a Internet
     * antes de ejecutar acciones que requieran acceso a BD,
     * y lanzar un mensaje en caso false
     * @return true/false
     */
    protected boolean hayConexion(){
        UtilsCheckNetwork con = new UtilsCheckNetwork();
        //Salvo los creditos, las demas actividades requieren conexion
        if(!con.isOnline(this)) {
            Toast.makeText(this,
                    "No hay conexion a internet", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Muestra un Imagebutton para mostrar la contraseña escrita
     * siempre que el campo no esté vacío
     * @param btShowPass boton
     * @param etPass campo de texto
     */
    @SuppressLint("ClickableViewAccessibility")
    protected void setBotonMostrarPass(ImageButton btShowPass, EditText etPass) {
        etPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty())
                    btShowPass.setVisibility(View.GONE);
                else
                    btShowPass.setVisibility(View.VISIBLE);
            }
        });

        btShowPass.setOnTouchListener((v, event) -> {
            if (etPass.getText().toString().isEmpty())
                return false;
            int cursor = etPass.getSelectionStart();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    etPass.setInputType(//Texto visible
                            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    etPass.setSelection(cursor);
                    break;
                case MotionEvent.ACTION_UP:
                    new Handler().postDelayed(() -> {
                                etPass.setInputType(
                                        InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                    etPass.setSelection(cursor);},
                            400);
                    break;
            }
            return true;
        });
    }
}
