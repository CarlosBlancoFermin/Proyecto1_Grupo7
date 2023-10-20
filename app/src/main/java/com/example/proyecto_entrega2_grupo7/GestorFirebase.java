package com.example.proyecto_entrega2_grupo7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GestorFirebase extends AppCompatActivity {

    FirebaseFirestore db;

    void registrarusuario(String nombre, String apellidos, String puesto, String pass, String correo) {


        UserData user = new UserData();
        user.setId("");
        user.setNombre(nombre);
        user.setApellidos(apellidos);
        user.setPuesto(puesto);
        user.setPass(Encriptador.passEncriptada(pass));
        user.setCorreo(correo);
        db.collection("usuarios").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                user.setId(documentReference.getId());
                db.collection("usuarios").document(user.getId()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("usuario creado");
                    }
                });
            }
        });

    }

    void actualizarusuario(UserData user){
        db.collection("usuarios").document(user.getId()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("usuario actualizado");
            }
        });
    }

    void obtenerusuarios(FirebaseListCallback callback){
        List<UserData> listausuarios = new ArrayList<>();

        db.collection("usuarios").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    for (QueryDocumentSnapshot doc : task.getResult()){
                        UserData user = doc.toObject(UserData.class);
                        listausuarios.add(user);
                        System.out.println(user.getNombre());
                    }
                    callback.onCallback(listausuarios);
                }
            }
        });


    }

}




