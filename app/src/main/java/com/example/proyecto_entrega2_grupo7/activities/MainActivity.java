package com.example.proyecto_entrega2_grupo7.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.proyecto_entrega2_grupo7.R;
import com.example.proyecto_entrega2_grupo7.entities.Usuario;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    /**
     * En la actividad se llama al Servicio (UsuarioDAO), y este pilla la conexión
     * No es necesario hacer un "getInstance()" aqui
     */
//    UsuarioDAO usuarioService = new UsuarioDAO();
//    PuestoDAO puestoService = new PuestoDAO();
//    HorarioDAO horarioService = new HorarioDAO();
    List<Usuario> users = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        usuarioService.registrarUsuario(
//                "test@test.test", "123456",
//                "Carlos","Blanco",
//                "6L5EauTwGPZZuHGTiRgN",
//                "PgcuwWqMy4SJdcJzMxsz");
//
//        usuarioService.registrarUsuario(
//                "testing@test.test", "123456",
//                "Eduardo","Camavinga",
//                "lxhvRBbVmoAVvAzNvoOz",
//                "PgcuwWqMy4SJdcJzMxsz");

//        puestoService.registrarPuesto("Jefe de sección","50.000,00");
//        puestoService.registrarPuesto("Programador senior","35.000,00");
//        puestoService.registrarPuesto("Programador junior","25.000,00");
//
//        horarioService.registrarHorario("Matutino","08:30","14:00");
//        horarioService.registrarHorario("Vespertino","15:00","21:30");
        /*
         * La primera pantalla tiene que cargarse con setContentView;
         * si se carga como Intent y le das para atrás se queda una pantalla en blanco
         *
         * Intent intent = new Intent(this, LoginActivity.class);
         * startActivity(intent);
         */
    }

    public void btListarEmpleados(View view){
        Intent intent = new Intent(this, ListarActivity.class);
        startActivity(intent);
    }

    public void btLogin(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}