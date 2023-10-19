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

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        registrarusuario();
    }
    void registrarusuario(){
        String correo = "test@test.test";
        String pass = "12345";
        mAuth.createUserWithEmailAndPassword(correo, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // todo: subir datos de usuario
                    UserData user = new UserData();
                    user.setId(mAuth.getUid());
                    user.setNombre("Carlos");
                    user.setApellidos("Blanco");
                    user.setPuesto("picateclas");
                    db.collection("usuarios").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            System.out.println("correcto");
                        }
                    });

                }else{
                    System.out.println("algo fallo");
                }
            }
        });
    }
}