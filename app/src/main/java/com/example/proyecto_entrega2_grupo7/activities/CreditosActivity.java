package com.example.proyecto_entrega2_grupo7.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.proyecto_entrega2_grupo7.R;

public class CreditosActivity extends AppCompatActivity {

    String URl;
    Uri link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditos);
    }
    public void btcreditos2(View view){
        URl = "https://es.linkedin.com/in/carlos-blanco-ferm%C3%ADn-b9ab13229";
        link = Uri.parse(URl);
        Intent intent = new Intent(Intent.ACTION_VIEW,link);
        startActivity(intent);
    }
    public void btcreditos1(View view){
        URl = "https://www.linkedin.com/in/andresmonvi/";
        link = Uri.parse(URl);
        Intent intent = new Intent(Intent.ACTION_VIEW,link);
        startActivity(intent);
    }
    public void btcreditos3(View view){
        URl = "https://www.linkedin.com/in/andresmonvi/";
        link = Uri.parse(URl);
        Intent intent = new Intent(Intent.ACTION_VIEW,link);
        startActivity(intent);
    }
    public void btcodigo(View view){
        URl = "https://github.com/CarlosBlancoFermin/Proyecto1_Grupo7";
        link = Uri.parse(URl);
        Intent intent = new Intent(Intent.ACTION_VIEW,link);
        startActivity(intent);
    }
    public void btcreditosvolver(View view){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}