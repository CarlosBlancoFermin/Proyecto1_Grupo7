package com.example.proyecto_entrega2_grupo7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    GestorFirebase ges = new GestorFirebase();
    List<UserData> users = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ges.db = FirebaseFirestore.getInstance();
        //ges.registrarusuario("carlos", "blanco", "cobrador de frak","123456","test@test.test");
       ges.obtenerusuarios(new FirebaseListCallback() {
           @Override
           public void onCallback(List<UserData> list) {
               users = list;
           }
       });
    }


}