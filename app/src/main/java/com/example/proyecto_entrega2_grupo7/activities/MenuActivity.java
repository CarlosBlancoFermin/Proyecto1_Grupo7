package com.example.proyecto_entrega2_grupo7.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto_entrega2_grupo7.R;
import com.example.proyecto_entrega2_grupo7.activities.listar.Creditos;
import com.example.proyecto_entrega2_grupo7.activities.listar.ListarActivity;

public class MenuActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }
    public void btcreditos(View view){
        Intent intent = new Intent(this, Creditos.class);
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
