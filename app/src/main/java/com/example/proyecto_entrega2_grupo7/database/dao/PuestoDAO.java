package com.example.proyecto_entrega2_grupo7.database.dao;

import androidx.annotation.NonNull;

import com.example.proyecto_entrega2_grupo7.database.FirebaseListCallback;
import com.example.proyecto_entrega2_grupo7.database.FirebaseManager;
import com.example.proyecto_entrega2_grupo7.entities.Puesto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PuestoDAO {
    //Cuando se instancia este servicio se conecta a la base de datos
    final CollectionReference DB_COLECCION =
            FirebaseManager.getDatabase()
                    .collection("puestos");

    public void registrarPuesto(String nombre, String salario) {
        Puesto p = new Puesto(nombre,salario);
        DB_COLECCION.add(p).addOnSuccessListener(documentReference -> {
            p.setId(documentReference.getId());
            DB_COLECCION.document(p.getId()).set(p)
                    .addOnSuccessListener(unused -> System.out.println("puesto creado"));
        });

    }

    public void obtenerPuestos(FirebaseListCallback callback){

        DB_COLECCION.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                List<Puesto> puestos = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()){
                    Puesto p = doc.toObject(Puesto.class);
                    puestos.add(p);
                    System.out.println(p.getNombre());
                }
                callback.onCallback(puestos);
            }
        });
    }

    public void actualizarPuesto(Puesto p){
        DB_COLECCION.document(p.getId()).set(p)
                .addOnSuccessListener(unused -> System.out.println("puesto actualizado"));
    }

    public void borrarPuesto(Puesto p){
        DB_COLECCION.document(p.getId()).delete()
                .addOnSuccessListener(unused -> System.out.println("puesto " + p.getNombre() + "eliminado"));
    }

}
