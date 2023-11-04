package com.example.proyecto_entrega2_grupo7.database.dao;

import com.example.proyecto_entrega2_grupo7.database.FirebaseCallback;
import com.example.proyecto_entrega2_grupo7.database.FirebaseListCallback;
import com.example.proyecto_entrega2_grupo7.entities.Horario;
import com.example.proyecto_entrega2_grupo7.entities.Usuario;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HorarioDAO implements IServiceDAO{
    //Acceso a la colección horarios de la BDD
    final CollectionReference DB_COLECCION = DB.collection("horarios");

    @Override
    public void insertarRegistro(Object horario) {
        DB_COLECCION.add(horario).addOnSuccessListener(documentReference -> {
            ((Horario)horario).setId(documentReference.getId());
            DB_COLECCION.document(((Horario)horario).getId()).set(horario)
                    .addOnSuccessListener(unused -> System.out.println("horario creado"));
        });

    }

    @Override
    public void obtenerRegistroPorId(String id, FirebaseCallback callback){
        DB_COLECCION.whereEqualTo("id", id).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Horario horario = new Horario();
                for (QueryDocumentSnapshot doc : task.getResult()){
                    horario = doc.toObject(Horario.class);
                }
                callback.onCallback(horario);
            }
        });
    }

    /**
     * Devuelve todos los horarios
     * ordenados por hora de salida
     * de menor a mayor
     * @param callback
     */
    @Override
    public void obtenerTodos(FirebaseListCallback callback){
        Query query = DB_COLECCION.orderBy("horaSalida", Query.Direction.ASCENDING);
        DB_COLECCION.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                List<Horario> horarios = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()){
                    Horario h = doc.toObject(Horario.class);
                    horarios.add(h);
                }
                callback.onCallback(horarios);
            }
        });
    }

    @Override
    public void actualizarRegistro(Object horario){
        DB_COLECCION.document(((Horario)horario).getId()).set(((Horario)horario))
                .addOnSuccessListener(unused -> System.out.println("horario actualizado"));
    }

    public void borrarRegistro(Object horario){
        DB_COLECCION.document(((Horario)horario).getId()).delete()
                .addOnSuccessListener(unused ->
                        System.out.println("horario " + ((Horario)horario).getNombre() + "eliminado"));
    }


}
