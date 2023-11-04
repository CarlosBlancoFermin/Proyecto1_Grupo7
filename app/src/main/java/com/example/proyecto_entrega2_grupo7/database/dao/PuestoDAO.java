package com.example.proyecto_entrega2_grupo7.database.dao;

import com.example.proyecto_entrega2_grupo7.database.FirebaseCallback;
import com.example.proyecto_entrega2_grupo7.database.FirebaseListCallback;
import com.example.proyecto_entrega2_grupo7.entities.Puesto;
import com.example.proyecto_entrega2_grupo7.entities.Usuario;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PuestoDAO implements IServiceDAO {
    //Acceso a la colección horarios de la BDD
    final CollectionReference DB_COLECCION = DB.collection("puestos");

    @Override
    public void insertarRegistro(Object puesto) {
        DB_COLECCION.add(puesto).addOnSuccessListener(documentReference -> {
            ((Puesto)puesto).setId(documentReference.getId());
            DB_COLECCION.document(((Puesto)puesto).getId()).set(puesto)
                    .addOnSuccessListener(unused -> System.out.println("puesto creado"));
        });
    }

    @Override
    public void obtenerRegistroPorId(String id, FirebaseCallback callback){
        DB_COLECCION.whereEqualTo("id", id).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Puesto puesto = new Puesto();
                for (QueryDocumentSnapshot doc : task.getResult()){
                    puesto = doc.toObject(Puesto.class);
                }
                callback.onCallback(puesto);
            }
        });
    }

    /**
     * Devuelve todos los horarios
     * ordenados por salario
     * de mayor a menor
     * @param callback
     */
    @Override
    public void obtenerTodos(FirebaseListCallback callback){
        Query query = DB_COLECCION.orderBy("salario", Query.Direction.DESCENDING);
        query.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                List<Puesto> puestos = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()){
                    Puesto p = doc.toObject(Puesto.class);
                    puestos.add(p);
                }
                callback.onCallback(puestos);
            }
        });
    }

    @Override
    public void actualizarRegistro(Object puesto){
        DB_COLECCION.document(((Puesto)puesto).getId()).set(puesto)
                .addOnSuccessListener(unused -> System.out.println("puesto actualizado"));
    }

    public void borrarRegistro(Object puesto){
        DB_COLECCION.document(((Puesto)puesto).getId()).delete()
                .addOnSuccessListener(unused ->
                        System.out.println("puesto " + ((Puesto)puesto).getNombre() + "eliminado"));
    }


    public void obtenerPuestoPorId(String id, FirebaseCallback callback){
        DB_COLECCION.whereEqualTo("id", id).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Puesto puesto = new Puesto();
                for (QueryDocumentSnapshot doc : task.getResult()){
                   puesto = doc.toObject(Puesto.class);
                }
                callback.onCallback(puesto);
            }
        });
    }

}
