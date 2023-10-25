package com.example.proyecto_entrega2_grupo7.database.dao;

import androidx.annotation.NonNull;

import com.example.proyecto_entrega2_grupo7.database.FirebaseListCallback;
import com.example.proyecto_entrega2_grupo7.database.FirebaseManager;
import com.example.proyecto_entrega2_grupo7.entities.Horario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HorarioDAO {
    //Cuando se instancia este servicio se conecta a la base de datos
    final CollectionReference DB_COLECCION =
            FirebaseManager.getDatabase()
                    .collection("horarios");

    public void registrarHorario(String nombre, String horaEntrada, String horaSalida) {
        Horario h = new Horario(nombre,horaEntrada,horaSalida);
        DB_COLECCION.add(h).addOnSuccessListener(documentReference -> {
            h.setId(documentReference.getId());
            DB_COLECCION.document(h.getId()).set(h)
                    .addOnSuccessListener(unused -> System.out.println("puesto creado"));
        });

    }

    public void obtenerHorarios(FirebaseListCallback callback){
        DB_COLECCION.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                List<Horario> horarios = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()){
                    Horario h = doc.toObject(Horario.class);
                    horarios.add(h);
                    System.out.println(h.getNombre());
                }
                callback.onCallback(horarios);
            }
        });
    }

    public void actualizarHorario(Horario h){
        DB_COLECCION.document(h.getId()).set(h)
                .addOnSuccessListener(unused -> System.out.println("horario actualizado"));
    }

    public void borrarHorario(Horario h){
        DB_COLECCION.document(h.getId()).delete()
                .addOnSuccessListener(unused -> System.out.println("horario " + h.getNombre() + "eliminado"));
    }
}
