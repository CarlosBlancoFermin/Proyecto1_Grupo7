package com.example.proyecto_entrega2_grupo7.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto_entrega2_grupo7.R;
import com.example.proyecto_entrega2_grupo7.activities.listar.ListarActivity;

public class MenuActivity extends SuperLoggedActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        String titulo = "";
        if(SuperLoggedActivity.getUserLogged() != null)
            titulo = "Hola, ".concat(SuperLoggedActivity.getUserLogged().getNombre());
        else
            titulo = getResources().getString(R.string.tag_menu);
        super.crearHomebar(titulo);

    }
    public void btcreditos(View view){
        Intent intent = new Intent(this, CreditosActivity.class);
        startActivity(intent);
    }
    public void btListarEmpleados(View view){
        Intent intent = new Intent(this, ListarActivity.class);
        startActivity(intent);
    }
    public void btNuevoempleado(View view){
        Intent intent = new Intent(this, ListarActivity.class);
        startActivity(intent);
    }
}
